import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import java.util.*;

public class MessageHandlerTest {
    
    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = mock(MessageHandler.class);
        List<String> params = new ArrayList<String>();
        params.add("param1");
        params.add("param2");
        when(messageHandler.getMessage("key1", params)).thenReturn("Message 1");
        when(messageHandler.getMessage("key2", params)).thenReturn(null);
        
        String result1 = messageHandler.getMessage("key1", params);
        assertEquals("Message 1", result1);
        
        String result2 = messageHandler.getMessage("key2", params);
        assertEquals("Key: key2 not found in message file", result2);
        
        verify(messageHandler, times(2)).getMessage(anyString(), anyList());
    }
    
    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = mock(MessageHandler.class);
        List<String> params = new ArrayList<String>();
        params.add("param1");
        params.add("param2");
        when(messageHandler.getMessageOptional("key1", params)).thenReturn("Message 1");
        when(messageHandler.getMessageOptional("key2", params)).thenReturn(null);
        
        String result1 = messageHandler.getMessageOptional("key1", params);
        assertEquals("Message 1", result1);
        
        String result2 = messageHandler.getMessageOptional("key2", params);
        assertNull(result2);
        
        verify(messageHandler, times(2)).getMessageOptional(anyString(), anyList());
    }
    
    @Test
    public void testGetInstance() {
        MessageHandler messageHandler = mock(MessageHandler.class);
        when(messageHandler.getInstance("basename")).thenReturn(messageHandler);
        
        MessageHandler result = messageHandler.getInstance("basename");
        assertEquals(messageHandler, result);
        
        verify(messageHandler).getInstance(anyString());
    }
    
    @Test
    public void testAddMessages() {
        MessageHandler messageHandler = spy(new MessageHandler("basename"));
        when(messageHandler.addMessages("basename", Constants.XBUS_ETC)).thenReturn(null);
        when(messageHandler.addMessages("basename", Constants.XBUS_PLUGIN_ETC)).thenReturn(null);
        
        messageHandler.addMessages("basename", Constants.XBUS_ETC);
        messageHandler.addMessages("basename", Constants.XBUS_PLUGIN_ETC);
        
        verify(messageHandler).addMessages(anyString(), anyString());
    }
    
}