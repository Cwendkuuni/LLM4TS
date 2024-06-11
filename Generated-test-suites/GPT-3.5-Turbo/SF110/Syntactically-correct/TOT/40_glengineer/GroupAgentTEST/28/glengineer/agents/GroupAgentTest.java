import org.junit.Test;
import static org.junit.Assert.*;

public class GroupAgentTest {

    @Test
    public void testGetFirstName_emptyChildrenList_returnsNull() {
        GroupAgent groupAgent = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }
            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        assertNull(groupAgent.getFirstName());
    }

    @Test
    public void testGetFirstName_firstChildHasFirstName_returnsFirstName() {
        GroupAgent groupAgent = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }
            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        Agent childAgent1 = new Agent() {
            @Override
            public String getFirstName() {
                return "John";
            }
        };
        groupAgent.addAgent(childAgent1);
        assertEquals("John", groupAgent.getFirstName());
    }

    @Test
    public void testGetFirstName_multipleChildrenWithFirstName_returnFirstNameOfFirstNonNullChild() {
        GroupAgent groupAgent = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }
            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        Agent childAgent1 = new Agent() {
            @Override
            public String getFirstName() {
                return null;
            }
        };
        Agent childAgent2 = new Agent() {
            @Override
            public String getFirstName() {
                return "John";
            }
        };
        groupAgent.addAgent(childAgent1);
        groupAgent.addAgent(childAgent2);
        assertEquals("John", groupAgent.getFirstName());
    }

    @Test
    public void testGetFirstName_allChildrenNull_returnsNull() {
        GroupAgent groupAgent = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }
            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
        Agent childAgent1 = new Agent() {
            @Override
            public String getFirstName() {
                return null;
            }
        };
        Agent childAgent2 = new Agent() {
            @Override
            public String getFirstName() {
                return null;
            }
        };
        groupAgent.addAgent(childAgent1);
        groupAgent.addAgent(childAgent2);
        assertNull(groupAgent.getFirstName());
    }

    // Similar tests for other methods...

}