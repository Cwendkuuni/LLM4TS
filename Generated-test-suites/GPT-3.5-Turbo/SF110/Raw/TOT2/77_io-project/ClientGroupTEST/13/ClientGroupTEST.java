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
        // Test case for sending a message to a client in the group
        // Invalid client
        // Edge case: Empty subGroups
        
        // Implement the test cases based on the requirements
    }

    @Test
    public void testNotifyMessageReceived() {
        // Test case for receiving a message from a client in the group
        // Invalid client
        // Edge case: Empty subGroups

        // Implement the test cases based on the requirements
    }

    @Test
    public void testNotifyExceptionCaught() {
        // Test case for catching an exception from a client in the group
        // Invalid client
        // Edge case: Empty subGroups

        // Implement the test cases based on the requirements
    }

    @Test
    public void testContains() {
        // Test case for checking if a client is in the group
        
        // Implement the test cases based on the requirements
    }

    @Test
    public void testSize() {
        // Test case for getting the size of the group
        
        // Implement the test cases based on the requirements
    }

    @Test
    public void testAdd() {
        // Test case for adding a client to the group
        // Existing client
        // Edge case: Null parent group
        
        // Implement the test cases based on the requirements
    }

    @Test
    public void testRemove() {
        // Test case for removing a client from the group
        // Non-existing client
        // Edge case: Recursive client removal
        
        // Implement the test cases based on the requirements
    }

    @Test
    public void testCreateSubGroup() {
        // Test case for creating a sub-group
        
        // Implement the test cases based on the requirements
    }

    @Test
    public void testAddClientHandler() {
        // Test case for adding a client handler

        // Implement the test cases based on the requirements
    }
}