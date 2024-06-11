package glengineer.agents;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class GroupAgentTest {
    private static class TestGroupAgent extends GroupAgent {
        @Override
        public GroupLayout.Group groupContent(final GroupCreatorAndElementAdder p0) {
            return null;
        }
        
        @Override
        public FunctionsOnGroup getFunctionsOnGroupImplemenation() {
            return null;
        }
    }
    
    private TestGroupAgent groupAgent;
    
    @Before
    public void setUp() {
        groupAgent = new TestGroupAgent();
    }
    
    @Test
    public void testGetFirstName() {
        assertNull(groupAgent.getFirstName());
    }
    
    @Test
    public void testGetLastName() {
        assertNull(groupAgent.getLastName());
    }
    
    @Test
    public void testIsComponent() {
        assertFalse(groupAgent.isComponent("name"));
    }
    
    @Test
    public void testIsGroup() {
        assertFalse(groupAgent.isGroup("first", "last"));
    }
    
    @Test
    public void testIsGap() {
        assertFalse(groupAgent.isGap());
    }
    
    @Test
    public void testEquals() {
        assertFalse(groupAgent.equals(null));
    }
    
    @Test
    public void testAddAgent() {
        Agent agent = new Agent();
        groupAgent.addAgent(agent);
        assertEquals(1, groupAgent.getNumberOfChildren());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddAgentWithNullAgent() {
        groupAgent.addAgent(null);
    }
    
    @Test
    public void testGetChildren() {
        assertTrue(groupAgent.getChildren().isEmpty());
    }
    
    @Test
    public void testGetNumberOfChildren() {
        assertEquals(0, groupAgent.getNumberOfChildren());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetComponentNotFound() {
        groupAgent.getComponent("name");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetGroupNotFound() {
        groupAgent.getGroup("first", "last");
    }
    
    @Test
    public void testFindDependingComponentByName() {
        assertNull(groupAgent.findDependingComponentByName("name"));
    }
    
    @Test
    public void testFindDependingGroupByNames() {
        assertNull(groupAgent.findDependingGroupByNames("first", "last"));
    }
    
    @Test
    public void testFindDependingParallelGroupByNames() {
        assertNull(groupAgent.findDependingParallelGroupByNames("first", "last"));
    }
    
    @Test
    public void testFindDependingSequentialGroupByNames() {
        assertNull(groupAgent.findDependingSequentialGroupByNames("first", "last"));
    }
    
    @Test
    public void testRemoveGapsFromParallelGroups() {
        groupAgent.removeGapsFromParallelGroups();
        // assert something
    }
    
    @Test
    public void testReplaceTemporaryGapsByRealGaps() {
        groupAgent.replaceTemporaryGapsByRealGaps(Map);
        // assert something
    }
    
    @Test
    public void testAddContentToGroup() {
        GroupLayout.Group parentGroup = new GroupLayout.Group();
        GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder();
        groupAgent.addContentToGroup(parentGroup, creatorAndAdder);
        // assert something
    }
    
    @Test
    public void testGapBefore() {
        GapAgent gap = new GapAgent(10);
        Agent element = new Agent();
        groupAgent.addGapBefore(gap, element);
        // assert something
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGapBeforeWithElementNotFound() {
        GapAgent gap = new GapAgent(10);
        Agent element = new Agent();
        groupAgent.addGapBefore(gap, element);
    }
    
    @Test
    public void testGapAfter() {
        GapAgent gap = new GapAgent(10);
        Agent element = new Agent();
        groupAgent.addGapAfter(gap, element);
        // assert something
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGapAfterWithElementNotFound() {
        GapAgent gap = new GapAgent(10);
        Agent element = new Agent();
        groupAgent.addGapAfter(gap, element);
    }
}