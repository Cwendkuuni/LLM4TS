import static org.junit.Assert.*;
import org.junit.Test;

public class GroupAgentTest {

    private GroupAgent groupAgent;

    @Before
    public void setUp() throws Exception {
        groupAgent = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
    }

    @Test
    public void testGetFirstName() {
        // Test when no children agents are present
        assertNull(groupAgent.getFirstName());
        
        // Test when a child agent has a first name
        Agent childAgent = new Agent() {
            @Override
            public String getFirstName() {
                return "John";
            }
        };
        groupAgent.addAgent(childAgent);
        assertEquals("John", groupAgent.getFirstName());
    }

    @Test
    public void testGetNumberOfChildren() {
        // Test when no children agents are present
        assertEquals(0, groupAgent.getNumberOfChildren());
        
        // Test after adding children agents
        Agent childAgent1 = new Agent();
        Agent childAgent2 = new Agent();
        groupAgent.addAgent(childAgent1);
        groupAgent.addAgent(childAgent2);
        assertEquals(2, groupAgent.getNumberOfChildren());
    }
    
    //Additional test cases for other methods should be included here

}