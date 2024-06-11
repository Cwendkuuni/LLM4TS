import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import glengineer.agents.*;

public class GroupAgentTest {

    @Test
    public void testGetFirstName() {
        // Test case 1: Testing when no children agents present
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getFirstName());
        
        // Test case 2: Testing when first child agent has a first name
        Agent childAgent = new Agent();
        childAgent.setFirstName("John");
        groupAgent.addAgent(childAgent);
        assertEquals("John", groupAgent.getFirstName());
    }
    
    @Test
    public void testGetLastName() {
        // Test case 1: Testing when no children agents present
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getLastName());
        
        // Test case 2: Testing when last child agent has a last name
        Agent firstChildAgent = new Agent();
        Agent secondChildAgent = new Agent();
        firstChildAgent.setLastName("Doe");
        secondChildAgent.setLastName("Smith");
        groupAgent.addAgent(firstChildAgent);
        groupAgent.addAgent(secondChildAgent);
        assertEquals("Smith", groupAgent.getLastName());
    }

    // Add more test cases for the remaining methods...

}