package ioproject.server.network;

import static org.junit.Assert.*;
import org.junit.Test;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        assertEquals(null, parentGroup.parentGroup());
    }

    @Test
    public void testNotifyMessageSent() {
        // Test case for notifyMessageSent method
    }

    @Test
    public void testNotifyMessageReceived() {
        // Test case for notifyMessageReceived method
    }

    // Remaining test cases for each public method
}