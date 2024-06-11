package glengineer.agents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GroupAgentTest {
    
    private class MockAgent extends Agent {
        @Override
        public String getFirstName() {
            return "John";
        }
        
        @Override
        public String getLastName() {
            return "Doe";
        }
        
        @Override
        public boolean isComponent(final String componentName) {
            return componentName.equals("Component");
        }
        
        @Override
        public boolean isGroup(final String firstName, final String lastName) {
            return firstName.equals("John") && lastName.equals("Doe");
        }
        
        @Override
        public boolean isGap() {
            return false;
        }
        
        @Override
        public boolean equals(final Agent agent) {
            return agent.isGroup("John", "Doe");
        }
        
        @Override
        public void addContentToGroup(final GroupLayout.Group parentGroup, final GroupCreatorAndElementAdder creatorAndAdder) {
            // Do nothing
        }
        
        @Override
        public ComponentAgent findDependingComponentByName(final String componentName) {
            return null;
        }
        
        @Override
        public GroupAgent findDependingGroupByNames(final String firstName, final String lastName) {
            return null;
        }
        
        @Override
        public ParallelGroupAgent findDependingParallelGroupByNames(final String firstName, final String lastName) {
            return null;
        }
        
        @Override
        public SequentialGroupAgent findDependingSequentialGroupByNames(final String firstName, final String lastName) {
            return null;
        }
    }
    
    private GroupAgent groupAgent;
    private MockAgent mockAgent1;
    private MockAgent mockAgent2;
    
    @Before
    public void setUp() {
        groupAgent = new GroupAgent() {
            @Override
            public GroupLayout.Group groupContent(GroupCreatorAndElementAdder p0) {
                return null;
            }

            @Override
            public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
                return new FunctionsOnGroupAndElementImplementation(this, mockAgent1);
            }
        };
        
        mockAgent1 = new MockAgent();
        mockAgent2 = new MockAgent();
    }
    
    @Test
    public void testGetFirstName_emptyChildrenAgents() {
        assertNull(groupAgent.getFirstName());
    }
    
    @Test
    public void testGetFirstName_noFirstNameFound() {
        groupAgent.addAgent(mockAgent2);
        assertNull(groupAgent.getFirstName());
    }
    
    @Test
    public void testGetFirstName_firstNameFound() {
        groupAgent.addAgent(mockAgent1);
        assertEquals("John", groupAgent.getFirstName());
    }
    
    @Test
    public void testGetLastName_emptyChildrenAgents() {
        assertNull(groupAgent.getLastName());
    }
    
    @Test
    public void testGetLastName_noLastNameFound() {
        groupAgent.addAgent(mockAgent2);
        assertNull(groupAgent.getLastName());
    }
    
    @Test
    public void testGetLastName_lastNameFound() {
        groupAgent.addAgent(mockAgent1);
        assertEquals("Doe", groupAgent.getLastName());
    }
    
    @Test
    public void testIsComponent_false() {
        assertFalse(groupAgent.isComponent("Component"));
    }
    
    @Test
    public void testIsGroup_false() {
        assertFalse(groupAgent.isGroup("John", "Doe"));
    }
    
    @Test
    public void testIsGap_false() {
        assertFalse(groupAgent.isGap());
    }
    
    @Test
    public void testEquals_false() {
        assertFalse(groupAgent.equals(mockAgent1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddAgent_nullAgent() {
        groupAgent.addAgent(null);
    }
    
    @Test
    public void testGetChildren_emptyChildrenAgents() {
        List<Agent> expected = new ArrayList<>();
        assertEquals(expected, groupAgent.getChildren());
    }
    
    @Test
    public void testGetChildren_nonEmptyChildrenAgents() {
        groupAgent.addAgent(mockAgent1);
        groupAgent.addAgent(mockAgent2);
        
        List<Agent> expected = Arrays.asList(mockAgent1, mockAgent2);
        assertEquals(expected, groupAgent.getChildren());
    }
    
    @Test
    public void testGetNumberOfChildren_emptyChildrenAgents() {
        assertEquals(0, groupAgent.getNumberOfChildren());
    }
    
    @Test
    public void testGetNumberOfChildren_nonEmptyChildrenAgents() {
        groupAgent.addAgent(mockAgent1);
        groupAgent.addAgent(mockAgent2);
        
        assertEquals(2, groupAgent.getNumberOfChildren());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetComponent_notFound() {
        groupAgent.getComponent("Component");
    }
    
    @Test
    public void testGetComponent_found() {
        groupAgent.addAgent(mockAgent1);
        ComponentAgent expected = (ComponentAgent)mockAgent1;
        assertEquals(expected, groupAgent.getComponent("Component"));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetGroup_notFound() {
        groupAgent.getGroup("John", "Smith");
    }
    
    @Test
    public void testGetGroup_found() {
        groupAgent.addAgent(mockAgent1);
        GroupAgent expected = (GroupAgent)mockAgent1;
        assertEquals(expected, groupAgent.getGroup("John", "Doe"));
    }
    
    @Test
    public void testFindDependingComponentByName_notFound() {
        assertNull(groupAgent.findDependingComponentByName("Component"));
    }
    
    @Test
    public void testFindDependingGroupByNames_notFound() {
        assertNull(groupAgent.findDependingGroupByNames("John", "Smith"));
    }
    
    @Test
    public void testFindDependingParallelGroupByNames_notFound() {
        assertNull(groupAgent.findDependingParallelGroupByNames("John", "Smith"));
    }
    
    @Test
    public void testFindDependingSequentialGroupByNames_notFound() {
        assertNull(groupAgent.findDependingSequentialGroupByNames("John", "Smith"));
    }
    
    @Test
    public void testRemoveGapsFromParallelGroups() {
        // TODO: Implement test case
    }
    
    @Test
    public void testReplaceTemporaryGapsByRealGaps() {
        // TODO: Implement test case
    }

}