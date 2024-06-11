import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class GroupAgentTest {
    
    private GroupAgent groupAgent;
    
    @Before
    public void setUp() {
        groupAgent = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }
            
            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return null;
            }
        };
    }
    
    @Test
    public void testGetFirstName_EmptyChildrenAgents_ReturnNull() {
        assertNull(groupAgent.getFirstName());
    }
    
    @Test
    public void testGetLastName_EmptyChildrenAgents_ReturnNull() {
        assertNull(groupAgent.getLastName());
    }
    
    @Test
    public void testIsComponent_ReturnFalse() {
        assertFalse(groupAgent.isComponent(""));
        assertFalse(groupAgent.isComponent("componentName"));
    }
    
    @Test
    public void testIsGroup_ReturnFalse() {
        assertFalse(groupAgent.isGroup("", ""));
        assertFalse(groupAgent.isGroup("firstName", "lastName"));
    }
    
    @Test
    public void testIsGap_ReturnFalse() {
        assertFalse(groupAgent.isGap());
    }
    
    @Test
    public void testEquals_ReturnFalse() {
        Agent agent = new Agent() {
            @Override
            public String getFirstName() {
                return "";
            }

            @Override
            public String getLastName() {
                return "";
            }
        };
        assertFalse(groupAgent.equals(agent));
        
        agent = new GroupAgent() {
            @Override
            public String getFirstName() {
                return "";
            }

            @Override
            public String getLastName() {
                return "";
            }
        };
        assertFalse(groupAgent.equals(agent));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddAgent_NullAgent_ThrowIllegalArgumentException() {
        groupAgent.addAgent(null);
    }
    
    @Test
    public void testGetChildren_ReturnEmptyList() {
        List<Agent> children = groupAgent.getChildren();
        assertNotNull(children);
        assertTrue(children.isEmpty());
    }
    
    @Test
    public void testGetNumberOfChildren_ReturnZero() {
        assertEquals(0, groupAgent.getNumberOfChildren());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetComponent_ComponentNotFound_ThrowIllegalArgumentException() {
        groupAgent.getComponent("componentName");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetGroup_GroupNotFound_ThrowIllegalArgumentException() {
        groupAgent.getGroup("", "");
    }
    
    @Test
    public void testFindDependingComponentByName_ComponentNotFound_ReturnNull() {
        assertNull(groupAgent.findDependingComponentByName("componentName"));
    }
    
    @Test
    public void testFindDependingGroupByNames_GroupNotFound_ReturnNull() {
        assertNull(groupAgent.findDependingGroupByNames("", ""));
    }
    
    @Test
    public void testFindDependingParallelGroupByNames_GroupNotFound_ReturnNull() {
        assertNull(groupAgent.findDependingParallelGroupByNames("", ""));
    }
    
    @Test
    public void testFindDependingSequentialGroupByNames_GroupNotFound_ReturnNull() {
        assertNull(groupAgent.findDependingSequentialGroupByNames("", ""));
    }
    
    @Test
    public void testRemoveGapsFromParallelGroups_ValidInput_DoNotThrowException() {
        groupAgent.removeGapsFromParallelGroups();
    }
    
    @Test
    public void testReplaceTemporaryGapsByRealGaps_ValidInput_DoNotThrowException() {
        GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
        groupAgent.replaceTemporaryGapsByRealGaps(map);
    }
    
    @Test
    public void testAddContentToGroup_ValidInput_DoNotThrowException() {
        GroupLayout.Group parentGroup = null;
        GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder() {
            @Override
            public void addGroupToGroup(GroupLayout.Group group, Settings settings, GroupLayout.Group parentGroup) {
                
            }
        };
        groupAgent.addContentToGroup(parentGroup, creatorAndAdder);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddGapBefore_ElementNotFound_ThrowIllegalArgumentException() {
        groupAgent.addGapBefore(new GapAgent(0), new Agent() {
            @Override
            public String toString() {
                return "elementAgent";
            }
        });
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddGapAfter_ElementNotFound_ThrowIllegalArgumentException() {
        groupAgent.addGapAfter(new GapAgent(0), new Agent() {
            @Override
            public String toString() {
                return "elementAgent";
            }
        });
    }
}