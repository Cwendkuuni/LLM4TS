import org.junit.Test;
import static org.junit.Assert.*;

import net.sf.xbus.base.core.MessageHandler;
import java.util.List;
import java.util.ArrayList;

public class MessageHandlerTest {

	@Test
	public void getMessageTest() {
		MessageHandler messageHandler = new MessageHandler("basename");
		
		String key = "key";
		List<String> params = new ArrayList<>();
		params.add("param1");
		params.add("param2");
		
		String expectedMessage = "Key: " + key + " not found in message file";
		String actualMessage = messageHandler.getMessage(key, params);
		
		assertEquals(expectedMessage, actualMessage);
	}
	
	@Test
	public void getMessageOptionalTest() {
		MessageHandler messageHandler = new MessageHandler("basename");
		
		String key = "key";
		List<String> params = new ArrayList<>();
		params.add("param1");
		params.add("param2");
		
		String expectedMessage = null;
		String actualMessage = messageHandler.getMessageOptional(key, params);
		
		assertEquals(expectedMessage, actualMessage);
	}
	
	@Test
	public void getInstanceTest() {
		MessageHandler messageHandler = MessageHandler.getInstance("basename");
		
		assertNotNull(messageHandler);
	}
}