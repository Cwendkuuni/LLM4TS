import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class GroupAgentTest {
    
    private GroupAgent groupAgent;
    
    @Before
    public void setUp() {
        groupAgent = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
                return null;
            }
            
            @Override
            public FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
    }
    
    @Test
    public void testGetFirstName_emptyChildrenList_returnNull() {
        assertNull(groupAgent.getFirstName());
    }
    
    @Test
    public void testGetFirstName_oneChildHasFirstName_returnFirstName() {
        GroupAgent child = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
                return null;
            }
            
            @Override
            public FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
                return null;
            }
            
            @Override
            public String getFirstName() {
                return "John";
            }
        };
        groupAgent.addAgent(child);
        
        assertEquals("John", groupAgent.getFirstName());
    }
    
    @Test
    public void testGetFirstName_multipleChildren_returnFirstNonEmptyFirstName() {
        GroupAgent child1 = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
                return null;
            }
            
            @Override
            public FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
                return null;
            }
            
            @Override
            public String getFirstName() {
                return "";
            }
        };
        GroupAgent child2 = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
                return null;
            }
            
            @Override
            public FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
                return null;
            }
            
            @Override
            public String getFirstName() {
                return "John";
            }
        };
        GroupAgent child3 = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
                return null;
            }
            
            @Override
            public FunctionsOnGroupAndElement getFunctionsOnGroupImplemenation() {
                return null;
            }
            
            @Override
            public String getFirstName() {
                return "Jane";
            }
        };
        groupAgent.addAgent(child1);
        groupAgent.addAgent(child2);
        groupAgent.addAgent(child3);
        
        assertEquals("John", groupAgent.getFirstName());
    }
    
    // other test methods for the remaining methods in GroupAgent class
    
}