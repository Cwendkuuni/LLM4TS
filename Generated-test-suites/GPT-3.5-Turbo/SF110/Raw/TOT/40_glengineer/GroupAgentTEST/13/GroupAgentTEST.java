import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class GroupAgentTest {
    
    @Test
    public void testGetFirstName_emptyChildrenAgents_returnNull() {
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
    public void testGetFirstName_oneChildAgentHasFirstName_returnFirstName() {
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
    public void testGetFirstName_multipleChildAgentsHasFirstName_returnFirstName() {
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
        Agent childAgent2 = new Agent() {
            @Override
            public String getFirstName() {
                return "Mary";
            }
        };
        groupAgent.addAgent(childAgent1);
        groupAgent.addAgent(childAgent2);
        
        assertEquals("John", groupAgent.getFirstName());
    }
    
    @Test
    public void testGetFirstName_noChildAgentHasFirstName_returnNull() {
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
    
    // Similar tests can be added for other methods in GroupAgent class
    
}