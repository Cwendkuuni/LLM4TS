import static org.junit.Assert.*;
import org.junit.Test;
import glengineer.agents.*;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getFirstName());
        
        // Add child agent
        Agent childAgent = new Agent();
        childAgent.setFirstName("John");
        groupAgent.addAgent(childAgent);
        
        assertEquals("John", groupAgent.getFirstName());
    }
    
    @Test
    public void testGetLastName() {
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getLastName());
        
        // Add child agent
        Agent childAgent = new Agent();
        childAgent.setLastName("Doe");
        groupAgent.addAgent(childAgent);
        
        assertEquals("Doe", groupAgent.getLastName());
    }
    
    @Test
    public void testIsGroup() {
        GroupAgent groupAgent = new GroupAgent();
        
        // Add child agents
        Agent child1 = new Agent("Alice", "Smith");
        Agent child2 = new Agent("Bob", "Johnson");
        
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        
        assertTrue(groupAgent.isGroup("Alice", "Smith"));
        assertFalse(groupAgent.isGroup("Alice", "Jones"));
    }

    // Add more test cases for the remaining methods
    
}