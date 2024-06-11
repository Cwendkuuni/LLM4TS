import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class GroupAgentTest {

    private GroupAgent groupAgent;

    @Before
    public void setup() {
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
    public void testGetFirstName_EmptyChildrenAgents_ReturnsNull() {
        assertNull(groupAgent.getFirstName());
    }

    @Test
    public void testGetLastName_EmptyChildrenAgents_ReturnsNull() {
        assertNull(groupAgent.getLastName());
    }

    @Test
    public void testAddAgent_NullAgent_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            groupAgent.addAgent(null);
        });
    }

    @Test
    public void testGetComponent_ComponentExists_ReturnsComponentAgent() {
        ComponentAgent componentAgent = new ComponentAgent();
        groupAgent.addAgent(componentAgent);
        
        assertEquals(componentAgent, groupAgent.getComponent("test"));
    }

    // Add more test cases for other methods here

}