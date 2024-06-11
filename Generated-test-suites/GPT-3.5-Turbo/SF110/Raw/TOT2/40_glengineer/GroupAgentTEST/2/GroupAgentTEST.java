import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class GroupAgentTest {
    private GroupAgent groupAgent;

    @Before
    public void setup() {
        groupAgent = new GroupAgentImpl();
    }

    @After
    public void tearDown() {
        groupAgent = null;
    }

    @Test
    public void testGetFirstName() {
        // Typical use case
        assertNull(groupAgent.getFirstName());
        
        // Edge case
        Agent childAgent = new ChildAgent("John");
        groupAgent.addAgent(childAgent);
        assertEquals("John", groupAgent.getFirstName());
        
        // Error scenario
        groupAgent.addAgent(null); // adding null agent should throw IllegalArgumentException
    }

    @Test
    public void testGetLastName() {
        // Typical use case
        assertNull(groupAgent.getLastName());
        
        // Edge case
        Agent childAgent = new ChildAgent("Doe");
        groupAgent.addAgent(childAgent);
        assertEquals("Doe", groupAgent.getLastName());
        
        // Error scenario
        groupAgent.addAgent(null); // adding null agent should throw IllegalArgumentException
    }

    @Test
    public void testAddAgent() {
        // Typical use case
        Agent childAgent = new ChildAgent("Alice");
        groupAgent.addAgent(childAgent);
        assertEquals(1, groupAgent.getNumberOfChildren());
        
        // Error scenario
        assertThrows(IllegalArgumentException.class, () -> {
            groupAgent.addAgent(null);
        });
    }
    
    // Add test cases for other public methods in GroupAgent class
    
}