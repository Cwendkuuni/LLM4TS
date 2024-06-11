
package net.sf.xbus.base.core;

import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class MessageHandlerTest {

    @Test
    public void testGetMessage() {
        MessageHandler messageHandler = new MessageHandler("basename");
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        String expected = "Key: " + key + " not found in message file";
        String actual = messageHandler.getMessage(key, params);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetMessageOptional() {
        MessageHandler messageHandler = new MessageHandler("basename");
        String key = "key";
        List<String> params = new ArrayList<>();
        params.add("param1");
        String expected = null;
        String actual = messageHandler.getMessageOptional(key, params);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetInstance() {
        String basename = "basename";
        MessageHandler instance1 = MessageHandler.getInstance(basename);
        MessageHandler instance2 = MessageHandler.getInstance(basename);
        Assert.assertSame(instance1, instance2);
    }
}

