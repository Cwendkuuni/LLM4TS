import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        GroupAgent groupAgent = new GroupAgent();
        assertEquals(null, groupAgent.getFirstName());
        
        // Add child agent with first name
        Agent childAgent = new Agent();
        childAgent.setFirstName("John");
        groupAgent.addAgent(childAgent);
        
        assertEquals("John", groupAgent.getFirstName());
    }

    @Test
    public void testGetLastName() {
        GroupAgent groupAgent = new GroupAgent();
        assertEquals(null, groupAgent.getLastName());
        
        // Add child agent with last name
        Agent childAgent = new Agent();
        childAgent.setLastName("Doe");
        groupAgent.addAgent(childAgent);
        
        assertEquals("Doe", groupAgent.getLastName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullAgent() {
        GroupAgent groupAgent = new GroupAgent();
        groupAgent.addAgent(null);
    }

    // Add more test cases for other public methods

}