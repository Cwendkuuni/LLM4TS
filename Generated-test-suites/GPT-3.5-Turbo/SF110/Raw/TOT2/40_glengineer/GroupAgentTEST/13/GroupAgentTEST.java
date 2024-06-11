import static org.junit.Assert.*;
import org.junit.Test;

public class GroupAgentTest {
    
    @Test
    public void testGetFirstName() {
        // Test for empty childrenAgents
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getFirstName());
        
        // Test for childrenAgents with non-null first name
        Agent childAgent = new Agent() {
            @Override
            public String getFirstName() {
                return "Child";
            }
        };
        groupAgent.addAgent(childAgent);
        assertEquals("Child", groupAgent.getFirstName());
    }
    
    @Test
    public void testGetLastName() {
        // Test for empty childrenAgents
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getLastName());
        
        // Test for childrenAgents with non-null last name
        Agent childAgent = new Agent() {
            @Override
            public String getLastName() {
                return "Child";
            }
        };
        groupAgent.addAgent(childAgent);
        assertEquals("Child", groupAgent.getLastName());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddAgent() {
        GroupAgent groupAgent = new GroupAgent();
        groupAgent.addAgent(null);
    }
    
    @Test
    public void testGetChildren() {
        GroupAgent groupAgent = new GroupAgent();
        Agent childAgent = new Agent();
        groupAgent.addAgent(childAgent);
        
        assertEquals(1, groupAgent.getChildren().size());
        assertEquals(childAgent, groupAgent.getChildren().get(0));
    }
    
    // Add more test methods for other public methods
    
    // Edge cases and exception handling scenarios should be tested as well
    
}