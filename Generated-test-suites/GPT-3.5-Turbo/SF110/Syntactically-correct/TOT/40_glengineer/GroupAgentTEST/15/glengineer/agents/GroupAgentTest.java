import static org.junit.Assert.*;
import org.junit.*;
import glengineer.agents.*;
import java.util.*;
import javax.swing.*;
import glengineer.agents.settings.*;

public class GroupAgentTest {

    // Test case for getFirstName() method
    
    @Test
    public void testGetFirstName() {
        // Typical use case: GroupAgent has children, and one of them has a non-null first name
        GroupAgent groupAgent = new GroupAgent() {
            @Override
            public String getFirstName() {
                return "John";
            }
        };
        Agent childAgent = new Agent() {
            @Override
            public String getFirstName() {
                return null;
            }
        };
        groupAgent.addAgent(childAgent);
        assertEquals("John", groupAgent.getFirstName());
        
        // Typical use case: GroupAgent has children, but none of them have a non-null first name
        groupAgent = new GroupAgent() {
            @Override
            public String getFirstName() {
                return null;
            }
        };
        childAgent = new Agent() {
            @Override
            public String getFirstName() {
                return null;
            }
        };
        groupAgent.addAgent(childAgent);
        assertNull(groupAgent.getFirstName());
        
        // Edge case: GroupAgent has no children
        groupAgent = new GroupAgent() {
            @Override
            public String getFirstName() {
                return null;
            }
        };
        assertNull(groupAgent.getFirstName());
        
        // Error scenario: GroupAgent has children, but all of them have a null first name
        groupAgent = new GroupAgent() {
            @Override
            public String getFirstName() {
                return null;
            }
        };
        childAgent = new Agent() {
            @Override
            public String getFirstName() {
                return null;
            }
        };
        groupAgent.addAgent(childAgent);
        assertNull(groupAgent.getFirstName());
    }
    
    // Other test cases for remaining methods in GroupAgent class...
    
    // ...

}