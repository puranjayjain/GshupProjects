package Roomservice;



import com.tc.sol.server.util.sms.SMSUtil; //For sending SMS
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.Room;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class mainservice{
	public static final String bot = "himanshu.rathee@st.niituniversity.in";//bot email name
    public static final String password = "p@$$word4";//botpassword
    public static final String admin = "rathee.coolhimanshu@gmail.com";//admin email 
    public static final String electrician="9671847176",plumber="8828090019",carpenter="9680000880",cleaner="9671847176";//Numbers of different workers
@OnKeyword("roomservice")//displays form for filling complaint 
public void onroomservice(TeamchatAPI api) throws Exception
{
	Form f = api.objects().form();
	f.addField(api.objects().select().name("nameofworker").label("Person to contact").addOption("Electrician").addOption("Plumber").addOption("Carpenter").addOption("Cleaner"));
	f.addField(api.objects().input().name("name").label("Your Name"));
	f.addField(api.objects().input().name("roomno").label("Your Room no."));
	f.addField(api.objects().input().name("problem").label("Description of Problem (not more than 100 words)"));
	//Room x = api.context().create().setName("Service Request").add(admin).add(api.context().currentReply().senderEmail());
	api.perform(api.context().currentRoom().post(
					new PrimaryChatlet().setQuestion("**Service Request Form**")
					.setReplyScreen(f)
					.setReplyLabel("CLICK")
					.alias("post")));
}
@OnAlias("post")//Displays number of the worker whom you want to contact 
public void onpost(TeamchatAPI api) throws Exception
{
	String nameofworker = api.context().currentReply().getField("nameofworker");
	String name = api.context().currentReply().getField("name");
	String roomno = api.context().currentReply().getField("roomno");
	String problem = api.context().currentReply().getField("problem");
	String groupID = api.context().currentRoom().getId();
	if(nameofworker.equals("Electrician")){
		//SMSUtil.sendSMS(electrician,"Name:- "+name+ " Room no:- "+roomno+ " problem:- "+problem+ " secret:- " +groupID);
		api.perform(api.context().currentRoom().post(
			new PrimaryChatlet().setQuestion("Your request is been processed.Please wait for 2 hours or else contact " +electrician)));
}
	else if(nameofworker.equals("Plumber")){
		//SMSUtil.sendSMS(plumber,"Name:- "+name+ "Room no:- "+roomno+ "problem:- "+problem+ "secret:- " +groupID);
		api.perform(api.context().currentRoom().post(
				new PrimaryChatlet().setQuestion("Your request is been processed.Please wait for 2 hours or else contact " +plumber)));
	}
	else if(nameofworker.equals("Carpenter")){
		//SMSUtil.sendSMS(carpenter,"Name:- "+name+ "Room no:- "+roomno+ "problem:- "+problem+ "secret:- " +groupID);
		api.perform(api.context().currentRoom().post(
				new PrimaryChatlet().setQuestion("Your request is been processed.Please wait for 2 hours or else contact " +carpenter)));
	}
	else if(nameofworker.equals("Cleaner")){
		//SMSUtil.sendSMS(cleaner,"Name:- "+name+ "Room no:- "+roomno+ "problem:- "+problem+ "secret:- " +groupID);
		api.perform(api.context().currentRoom().post(
				new PrimaryChatlet().setQuestion("Your request is been processed.Please wait for 2 hours or else contact " +cleaner)));
	}
		api.perform(api.context().currentRoom().post(
			new PrimaryChatlet()
			.setQuestion("You will get a secret code from the worker.Please use that to confirm whether you are satisfied with the work or not")));
		Form g = api.objects().form();
		g.addField(api.objects().input().name("roomid").label("Enter the secret code"));
		api.perform(api.context().currentRoom().post(
				new PrimaryChatlet().setQuestion("**Enter the secret code**")
				.setReplyScreen(g)
				.setReplyLabel("CLICK")
				.alias("code")));
	}
	@OnAlias("code")//Asks for acknowledgment if work is done or not
	public void oncode(TeamchatAPI api) throws Exception
	{
		
		String roomid=api.context().currentReply().getField("roomid");
		String groupID = api.context().currentRoom().getId();
		if(groupID.equals(roomid)){
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestion("The code matches.REVIEW THE WORK DONE")));
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestion(
					"Are you satisfied with the work done??")
			.setReplyScreen(
					api.objects()
							.form()
							.addField(
									api.objects().input()
											.name("answer")
											.label("Enter Yes/No")))
			.setReplyLabel("Enter").alias("finalalias")));
		}
		else{
			Form g = api.objects().form();
			g.addField(api.objects().input().name("roomid").label("Enter the secret code"));
	api.perform(api.context().currentRoom().post(new PrimaryChatlet()
			.setQuestion("The code does not match.")
			.setQuestion("Enter the code again")
			.setReplyScreen(g)
			.setReplyLabel("CLICK")
			.alias("code")));
		}
	}

public static void main(String[] args)//main function 
{
	TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
			.setEmail(bot)  
			.setPassword(password) 
			.startReceivingEvents(new mainservice());
		}
}
