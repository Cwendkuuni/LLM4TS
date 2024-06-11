import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import javax.swing.*;
import glengineer.*;
import glengineer.agents.*;
import glengineer.agents.setters.*;

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
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
    }
    
    @Test
    public void testGetFirstName_EmptyChildrenAgents_ReturnsNull() {
        String result = groupAgent.getFirstName();
        assertNull(result);
    }
    
    @Test
    public void testGetFirstName_NonEmptyChildrenAgents_ReturnsFirstName() {
        Agent agent1 = new Agent() {
            @Override
            public String getFirstName() {
                return "John";
            }
        };
        Agent agent2 = new Agent() {
            @Override
            public String getFirstName() {
                return null;
            }
        };
        groupAgent.addAgent(agent1);
        groupAgent.addAgent(agent2);
        
        String result = groupAgent.getFirstName();
        assertEquals("John", result);
    }
    
    @Test
    public void testGetLastName_EmptyChildrenAgents_ReturnsNull() {
        String result = groupAgent.getLastName();
        assertNull(result);
    }
    
    @Test
    public void testGetLastName_NonEmptyChildrenAgents_ReturnsLastName() {
        Agent agent1 = new Agent() {
            @Override
            public String getLastName() {
                return "Doe";
            }
        };
        Agent agent2 = new Agent() {
            @Override
            public String getLastName() {
                return null;
            }
        };
        groupAgent.addAgent(agent1);
        groupAgent.addAgent(agent2);
        
        String result = groupAgent.getLastName();
        assertEquals("Doe", result);
    }
    
    // Write tests for the remaining methods in the GroupAgent class
    
}