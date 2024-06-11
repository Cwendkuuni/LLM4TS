import glengineer.agents.*;
import glengineer.agents.GroupAgent;
import glengineer.agents.settings.Settings;
import glengineer.agents.setters.Agent;
import glengineer.agents.setters.GroupAgent;
import org.junit.Test;
import java.util.LinkedList;
import java.util.List;

public class GroupAgentTest {
    
    @Test
    public void testGetFirstNameEmptyChildren() {
        GroupAgent groupAgent = new GroupAgent();
        String result = groupAgent.getFirstName();
        assertNull(result);
    }

    @Test
    public void testGetLastNameEmptyChildren() {
        GroupAgent groupAgent = new GroupAgent();
        String result = groupAgent.getLastName();
        assertNull(result);
    }

    @Test
    public void testIsComponent() {
        GroupAgent groupAgent = new GroupAgent();
        boolean result = groupAgent.isComponent("ComponentName");
        assertFalse(result);
    }

    @Test
    public void testIsGroup() {
        GroupAgent groupAgent = new GroupAgent();
        groupAgent.addAgent(new Agent());
        boolean result = groupAgent.isGroup("First", "Last");
        assertFalse(result);
    }

    @Test
    public void testEquals() {
        GroupAgent groupAgent = new GroupAgent();
        Agent agent = new Agent();
        groupAgent.addAgent(agent);
        boolean result = groupAgent.equals(agent);
        assertTrue(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAgentNull() {
        GroupAgent groupAgent = new GroupAgent();
        groupAgent.addAgent(null);
    }

    @Test
    public void testGetChildren() {
        GroupAgent groupAgent = new GroupAgent();
        List<Agent> children = groupAgent.getChildren();
        assertNotNull(children);
    }

    @Test
    public void testGetNumberOfChildren() {
        GroupAgent groupAgent = new GroupAgent();
        int result = groupAgent.getNumberOfChildren();
        assertEquals(0, result);
    }

    // Include additional test cases for the remaining methods

}