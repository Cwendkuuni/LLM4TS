import org.junit.Test;
import static org.junit.Assert.*;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        // Test when childrenAgents is empty
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getFirstName());
        
        // Test when childrenAgents have Agents with first names
        Agent agent1 = new Agent() {
            @Override
            public String getFirstName() {
                return "John";
            }
        };
        Agent agent2 = new Agent() {
            @Override
            public String getFirstName() {
                return null;
            }
        };
        groupAgent.addAgent(agent1);
        groupAgent.addAgent(agent2);
        assertEquals("John", groupAgent.getFirstName());
    }

    @Test
    public void testGetLastName() {
        // Test when childrenAgents is empty
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getLastName());
        
        // Test when childrenAgents have Agents with last names
        Agent agent1 = new Agent() {
            @Override
            public String getLastName() {
                return "Doe";
            }
        };
        Agent agent2 = new Agent() {
            @Override
            public String getLastName() {
                return null;
            }
        };
        groupAgent.addAgent(agent1);
        groupAgent.addAgent(agent2);
        assertEquals("Doe", groupAgent.getLastName());
    }

    // Add more test cases for the other methods in GroupAgent

}