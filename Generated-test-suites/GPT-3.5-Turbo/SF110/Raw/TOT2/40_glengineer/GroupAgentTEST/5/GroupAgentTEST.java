import static org.junit.Assert.*;
import org.junit.Test;

public class GroupAgentTest {
    
    @Test
    public void testGetFirstName_emptyChildren_returnNull() {
        // Test when childrenAgents list is empty
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getFirstName());
    }
    
    @Test
    public void testGetLastName_emptyChildren_returnNull() {
        // Test when childrenAgents list is empty
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.getLastName());
    }
    
    @Test
    public void testAddAgent_nullAgent_throwIllegalArgumentException() {
        // Test adding null agent to childrenAgents
        GroupAgent groupAgent = new GroupAgent();
        assertThrows(IllegalArgumentException.class, () -> groupAgent.addAgent(null));
    }
    
    @Test
    public void testGetNumberOfChildren_multipleChildren_returnCorrectCount() {
        // Test getting number of children in childrenAgents
        GroupAgent groupAgent = new GroupAgent();
        groupAgent.addAgent(new Agent());
        groupAgent.addAgent(new Agent());
        assertEquals(2, groupAgent.getNumberOfChildren());
    }
    
    @Test
    public void testGetComponent_componentNameNotExist_throwIllegalArgumentException() {
        // Test getting a non-existing component
        GroupAgent groupAgent = new GroupAgent();
        assertThrows(IllegalArgumentException.class, () -> groupAgent.getComponent("NonExistingComponent"));
    }
    
    @Test
    public void testFindDependingParallelGroupByNames_notParallelGroup_returnNull() {
        // Test finding depending ParallelGroupAgent when it's not of type ParallelGroupAgent
        GroupAgent groupAgent = new GroupAgent();
        assertNull(groupAgent.findDependingParallelGroupByNames("firstName", "lastName"));
    }
    
    @Test
    public void testReplaceTemporaryGapsByRealGaps_replaceTemporaryGaps_correctlyReplaces() {
        // Test replacing temporary gaps by real gaps
        GroupAgent groupAgent = new GroupAgent();
        GroupLayoutEngineer.TemporaryGapsToGaps map = new GroupLayoutEngineer.TemporaryGapsToGaps();
        groupAgent.addAgent(new TemporaryGapAgent(10)); // Add temporary gap
        groupAgent.replaceTemporaryGapsByRealGaps(map);
        assertTrue(groupAgent.getChildren().get(0) instanceof GapAgent);
    }
    
    @Test
    public void testAddContentToGroup_groupContentAdd_correctlyAdds() {
        // Test adding content to group
        GroupAgent groupAgent = new GroupAgent();
        GroupLayout.Group parentGroup = new GroupLayout.Group();
        GroupCreatorAndElementAdder creatorAndAdder = new GroupCreatorAndElementAdder();
        groupAgent.addContentToGroup(parentGroup, creatorAndAdder);
        assertEquals(1, parentGroup.getElements().size());
    }
    
    @Test
    public void testAddGapBefore_addGapBeforeElement_correctlyAdds() {
        // Test adding gap before element
        GroupAgent groupAgent = new GroupAgent();
        GapAgent gap = new GapAgent(5);
        Agent element = new Agent();
        groupAgent.addGapBefore(gap, element);
        assertEquals(gap, groupAgent.getChildren().get(0));
    }
    
    @Test
    public void testFunctionsOnGroupAndElementImplementation_addPrecedingGap_correctlyAdds() {
        // Test adding preceding gap
        GroupAgent groupAgent = new GroupAgent();
        Agent element = new Agent();
        GroupAgent.FunctionsOnGroupAndElementImplementation functions = 
            new GroupAgent.FunctionsOnGroupAndElementImplementation(groupAgent, element);
        functions.addPrecedingGap(5);
        assertEquals(GapAgent.class, groupAgent.getChildren().get(0).getClass());
    }
}