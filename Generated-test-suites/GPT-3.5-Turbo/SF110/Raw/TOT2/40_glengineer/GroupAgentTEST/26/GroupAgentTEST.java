package glengineer.agents;

import static org.junit.Assert.*;
import org.junit.Test;

public class GroupAgentTest {

    @Test
    public void testGetFirstName_EmptyChildren() {
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getFirstName());
    }

    @Test
    public void testGetLastName_EmptyChildren() {
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getLastName());
    }

    @Test
    public void testIsGroup() {
        GroupAgent groupAgent = new GroupAgent();
        assertTrue(groupAgent.isGroup("", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAgent_NullAgent() {
        GroupAgent groupAgent = new GroupAgent();
        groupAgent.addAgent(null);
    }

    @Test
    public void testGetChildren() {
        GroupAgent groupAgent = new GroupAgent();
        assertNotNull(groupAgent.getChildren());
    }

    @Test
    public void testGetNumberOfChildren() {
        GroupAgent groupAgent = new GroupAgent();
        assertEquals(0, groupAgent.getNumberOfChildren());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetComponent_NotFound() {
        GroupAgent groupAgent = new GroupAgent();
        groupAgent.getComponent("Test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetGroup_NotFound() {
        GroupAgent groupAgent = new GroupAgent();
        groupAgent.getGroup("Test", "Test");
    }

    @Test
    public void testRemoveGapsFromParallelGroups() {
        GroupAgent groupAgent = new GroupAgent();
        groupAgent.removeGapsFromParallelGroups();
        // Test for side effects
    }

    // More test cases can be added as needed for full coverage

}