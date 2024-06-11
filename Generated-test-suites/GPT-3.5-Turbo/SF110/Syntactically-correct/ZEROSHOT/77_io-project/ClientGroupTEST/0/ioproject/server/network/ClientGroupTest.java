package ioproject.server.network;

import org.junit.Test;
import static org.junit.Assert.*;

public class ClientGroupTest {

    @Test
    public void testParentGroup() {
        ClientGroup parentGroup = new ClientGroup(null);
        ClientGroup clientGroup = new ClientGroup(parentGroup);

        ClientGroup actualParentGroup = clientGroup.parentGroup();

        assertEquals(parentGroup, actualParentGroup);
    }

    @Test
    public void testNotifyMessageSent() {
        // Test implementation here
    }

    @Test
    public void testNotifyMessageReceived() {
        // Test implementation here
    }

    @Test
    public void testNotifyExceptionCaught() {
        // Test implementation here
    }

    @Test
    public void testIterator() {
        // Test implementation here
    }

    @Test
    public void testContains() {
        // Test implementation here
    }

    @Test
    public void testSize() {
        // Test implementation here
    }

    @Test
    public void testAdd() {
        // Test implementation here
    }

    @Test
    public void testRemove() {
        // Test implementation here
    }

    @Test
    public void testCreateSubGroup() {
        // Test implementation here
    }

    @Test
    public void testAddClientHandler() {
        // Test implementation here
    }
}