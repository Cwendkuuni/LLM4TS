import org.junit.Test;
import static org.junit.Assert.*;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        // Test case for when childrenAgents is empty
        GroupAgent groupAgent = new GroupAgent() {
            @Override
            public String getFirstName() {
                return null;
            }
        };

        assertNull(groupAgent.getFirstName());
    }

    // Add more test cases for other methods here
    // ...
    
    @Test
    public void testAddAgent() {
        // Test case for adding a valid agent
        GroupAgent groupAgent = new GroupAgent();
        Agent agent = new Agent();
        groupAgent.addAgent(agent);

        assertEquals(1, groupAgent.getNumberOfChildren());
    }
    
    @Test
    public void testGetGroup() {
        // Test case for when the specified group is not found
        GroupAgent groupAgent = new GroupAgent();
        Agent agent = new Agent();
        groupAgent.addAgent(agent);

        assertThrows(IllegalArgumentException.class, () -> groupAgent.getGroup("John", "Doe"));
    }

    // Include more test cases for edge cases and exception handling
}