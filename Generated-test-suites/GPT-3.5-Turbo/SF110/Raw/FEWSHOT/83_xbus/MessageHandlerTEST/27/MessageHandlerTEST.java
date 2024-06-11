
package net.sf.xbus.base.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class MessageHandlerTest {
    
    @Before
    public void setup() {
        MessageHandler.mInstances.clear();
    }
    
    @Test
    public void testGetMessage_keyExists() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        MessageHandler handler = MessageHandler.getInstance("basename");
        
        String message = handler.getMessage("key", params);
        
        assertEquals("message", message);
    }
    
    @Test
    public void testGetMessage_keyDoesNotExist() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        MessageHandler handler = MessageHandler.getInstance("basename");
        
        String message = handler.getMessage("invalidKey", params);
        
        assertEquals("Key: invalidKey not found in message file", message);
    }
    
    @Test
    public void testGetMessageOptional_keyExists() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        MessageHandler handler = MessageHandler.getInstance("basename");
        
        String message = handler.getMessageOptional("key", params);
        
        assertEquals("message", message);
    }
    
    @Test
    public void testGetMessageOptional_keyDoesNotExist() {
        List<String> params = new ArrayList<>();
        params.add("param1");
        params.add("param2");
        MessageHandler handler = MessageHandler.getInstance("basename");
        
        String message = handler.getMessageOptional("invalidKey", params);
        
        assertNull(message);
    }
    
    @Test
    public void testGetMessageOptional_paramsNull() {
        List<String> params = null;
        MessageHandler handler = MessageHandler.getInstance("basename");
        
        String message = handler.getMessageOptional("key", params);
        
        assertEquals("message", message);
    }
    
    @Test
    public void testGetInstance_existingInstance() {
        MessageHandler.mInstances.put("basename", new MessageHandler("basename"));
        
        MessageHandler handler = MessageHandler.getInstance("basename");
        
        assertEquals(MessageHandler.mInstances.get("basename"), handler);
    }
    
    @Test
    public void testGetInstance_newInstance() {
        MessageHandler handler = MessageHandler.getInstance("basename");
        
        assertEquals(MessageHandler.mInstances.get("basename"), handler);
    }
    
    @Test
    public void testAddMessages_fileFound() throws IOException {
        MessageHandler handler = MessageHandler.getInstance("basename");
        
        handler.addMessages("basename", "path/to/directory");
        
        File dirFile = new File("path/to/directory");
        String[] messagesFiles = dirFile.list(new MessageHandler.MessagesFilter("basename", "_" + Locale.ENGLISH.toString() + ".properties"));
        
        assertEquals(1, messagesFiles.length);
    }
    
    @Test(expected = FileNotFoundException.class)
    public void testAddMessages_fileNotFound() throws IOException {
        MessageHandler handler = MessageHandler.getInstance("basename");
        
        handler.addMessages("basename", "nonexistent/directory");
    }
    
    @Test
    public void testMessagesFilter_accept() {
        File dir = new File("path/to/directory");
        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter("basename", "_" + Locale.ENGLISH.toString() + ".properties");
        
        boolean result = filter.accept(dir, "basename_en.properties");
        
        assertEquals(true, result);
    }
    
    @Test
    public void testMessagesFilter_decline() {
        File dir = new File("path/to/directory");
        MessageHandler.MessagesFilter filter = new MessageHandler.MessagesFilter("basename", "_" + Locale.ENGLISH.toString() + ".properties");
        
        boolean result = filter.accept(dir, "invalid.properties");
        
        assertEquals(false, result);
    }
}
