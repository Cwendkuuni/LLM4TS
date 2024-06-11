import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import glengineer.agents.*;

public class GroupAgentTest {
    
    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new GroupAgent() {
            public String getFirstName() {
                return "John";
            }
        };
        assertEquals("John", groupAgent.getFirstName());
    }
    
    @Test
    public void testGetLastName() {
        GroupAgent groupAgent = new GroupAgent() {
            public String getLastName() {
                return "Doe";
            }
        };
        assertEquals("Doe", groupAgent.getLastName());
    }
    
    @Test
    public void testIsComponent() {
        GroupAgent groupAgent = new GroupAgent();
        assertFalse(groupAgent.isComponent("Component"));
    }
    
    @Test
    public void testIsGroup() {
        GroupAgent groupAgent = new GroupAgent();
        assertTrue(groupAgent.isGroup("John", "Doe"));
    }
    
    @Test
    public void testIsGap() {
        GroupAgent groupAgent = new GroupAgent();
        assertFalse(groupAgent.isGap());
    }
    
    @Test
    public void testEquals() {
        GroupAgent groupAgent = new GroupAgent();
        Agent agent = new Agent() {
            public boolean isGroup(String firstName, String lastName) {
                return firstName.equals("John") && lastName.equals("Doe");
            }
        };
        assertTrue(groupAgent.equals(agent));
    }
    
    @Test
    public void testAddAgent() {
        GroupAgent groupAgent = new GroupAgent();
        Agent agent = new Agent();
        groupAgent.addAgent(agent);
        assertEquals(1, groupAgent.getNumberOfChildren());
    }
    
    // Add more test cases for other methods
    
}