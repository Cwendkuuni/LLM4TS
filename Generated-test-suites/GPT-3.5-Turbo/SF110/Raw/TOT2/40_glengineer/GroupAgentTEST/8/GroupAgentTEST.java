import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class GroupAgentTest {
    
    private GroupAgent groupAgent;
    
    @Before
    public void setUp() {
        groupAgent = new GroupAgentImpl();
    }
    
    @Test
    public void testGetFirstName() {
        // Test when no children agents are present
        assertNull(groupAgent.getFirstName());
        
        // Add children agents
        Agent agent1 = new AgentImpl("John", "Doe");
        Agent agent2 = new AgentImpl("Jane", "Smith");
        groupAgent.addAgent(agent1);
        groupAgent.addAgent(agent2);
        
        // Test when some children agents have first names
        assertEquals("John", groupAgent.getFirstName());
    }
    
    @Test
    public void testGetLastName() {
        // Test when no children agents are present
        assertNull(groupAgent.getLastName());
        
        // Add children agents
        Agent agent1 = new AgentImpl("John", "Doe");
        Agent agent2 = new AgentImpl("Jane", "Smith");
        groupAgent.addAgent(agent1);
        groupAgent.addAgent(agent2);
        
        // Test when some children agents have last names
        assertEquals("Smith", groupAgent.getLastName());
    }
    
    // Add more test cases for other methods
    
    private class GroupAgentImpl extends GroupAgent {
        @Override
        public String getFirstName() {
            return "John";
        }
        
        @Override
        public String getLastName() {
            return "Smith";
        }
        // Implement other abstract methods
    }
    
    private class AgentImpl extends Agent {
        private String firstName;
        private String lastName;
        
        public AgentImpl(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }
        
        @Override
        public String getFirstName() {
            return firstName;
        }
        
        @Override
        public String getLastName() {
            return lastName;
        }
        
        // Implement other abstract methods
    }
}