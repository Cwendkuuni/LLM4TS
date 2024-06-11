import static org.junit.Assert.*;
import org.junit.Test;

public class GroupAgentTest {
    
    @Test
    public void testGetFirstName() {
        // Test when childrenAgents is empty
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getFirstName());
        
        // Test when one of the children has a first name
        Agent child1 = new Agent();
        child1.setFirstName("John");
        groupAgent.addAgent(child1);
        assertEquals("John", groupAgent.getFirstName());
    }
    
    @Test
    public void testGetLastName() {
        // Test when childrenAgents is empty
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getLastName());
        
        // Test when one of the children has a last name
        Agent child1 = new Agent();
        child1.setLastName("Doe");
        groupAgent.addAgent(child1);
        assertEquals("Doe", groupAgent.getLastName());
    }

    // Add more test cases for other methods
    
    @Test
    public void testGetNumberOfChildren() {
        GroupAgent groupAgent = new GroupAgent();
        
        Agent child1 = new Agent();
        Agent child2 = new Agent();
        
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        
        assertEquals(2, groupAgent.getNumberOfChildren());
    }
    
    @Test
    public void testGetComponent() {
        GroupAgent groupAgent = new GroupAgent();
        
        ComponentAgent component = new ComponentAgent();
        component.setName("Component1");
        
        groupAgent.addAgent(component);
        
        assertEquals(component, groupAgent.getComponent("Component1"));
        
        // Test case when component not found
        try {
            groupAgent.getComponent("NonExistentComponent");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    // Add more test cases for other methods
    
    @Test
    public void testRemoveGapsFromParallelGroups() {
        // Create and add agents with gaps for testing
        GroupAgent groupAgent = new GroupAgent();
        GapAgent gap1 = new GapAgent(5);
        GapAgent gap2 = new GapAgent(10);
        
        groupAgent.addAgent(gap1);
        groupAgent.addAgent(new Agent());
        groupAgent.addAgent(gap2);
        
        groupAgent.removeGapsFromParallelGroups();
        
        // Check if gaps were removed and handled correctly
        assertEquals(1, groupAgent.getNumberOfChildren());
        assertFalse(groupAgent.getChildren().contains(gap1));
        assertFalse(groupAgent.getChildren().contains(gap2));
    }
}