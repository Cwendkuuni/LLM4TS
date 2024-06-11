import org.junit.Test;
import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost_WithRootNode_ReturnsCost() {
        Node rootNode = new Node();
        rootNode.setType(Node.TYPE_ROOT);
        rootNode.setCost(10);
        
        int result = InlineCostEstimator.getCost(rootNode);
        
        assertEquals(10, result);
    }
    
    @Test
    public void testGetCost_WithRootNodeAndCostThreshold_ReturnsCost() {
        Node rootNode = new Node();
        rootNode.setType(Node.TYPE_ROOT);
        rootNode.setCost(20);
        
        int result = InlineCostEstimator.getCost(rootNode, 15);
        
        assertEquals(20, result);
    }
    
    @Test
    public void testGetCost_WithRootNodeAndCostThresholdExceeded_ReturnsCost() {
        Node rootNode = new Node();
        rootNode.setType(Node.TYPE_ROOT);
        rootNode.setCost(30);
        
        int result = InlineCostEstimator.getCost(rootNode, 25);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testGetCost_WithEmptyRootNode_ReturnsZeroCost() {
        Node rootNode = new Node();
        rootNode.setType(Node.TYPE_ROOT);
        
        int result = InlineCostEstimator.getCost(rootNode);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testGetCost_WithEmptyRootNodeAndCostThreshold_ReturnsZeroCost() {
        Node rootNode = new Node();
        rootNode.setType(Node.TYPE_ROOT);
        
        int result = InlineCostEstimator.getCost(rootNode, 5);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testGetCost_WithMultipleChildren_ReturnsCombinedCost() {
        Node rootNode = new Node();
        rootNode.setType(Node.TYPE_ROOT);
        
        Node child1 = new Node();
        child1.setType(Node.TYPE_CHILD);
        child1.setCost(5);
        
        Node child2 = new Node();
        child2.setType(Node.TYPE_CHILD);
        child2.setCost(10);
        
        Node child3 = new Node();
        child3.setType(Node.TYPE_CHILD);
        child3.setCost(15);
        
        rootNode.addChild(child1);
        rootNode.addChild(child2);
        rootNode.addChild(child3);
        
        int result = InlineCostEstimator.getCost(rootNode);
        
        assertEquals(30, result);
    }
    
    @Test
    public void testGetCost_WithMultipleChildrenAndCostThreshold_ReturnsCombinedCost() {
        Node rootNode = new Node();
        rootNode.setType(Node.TYPE_ROOT);
        
        Node child1 = new Node();
        child1.setType(Node.TYPE_CHILD);
        child1.setCost(5);
        
        Node child2 = new Node();
        child2.setType(Node.TYPE_CHILD);
        child2.setCost(10);
        
        Node child3 = new Node();
        child3.setType(Node.TYPE_CHILD);
        child3.setCost(15);
        
        rootNode.addChild(child1);
        rootNode.addChild(child2);
        rootNode.addChild(child3);
        
        int result = InlineCostEstimator.getCost(rootNode, 25);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testGetCost_WithNoCostThreshold_ReturnsMaximumCost() {
        Node rootNode = new Node();
        rootNode.setType(Node.TYPE_ROOT);
        rootNode.setCost(Integer.MAX_VALUE);
        
        int result = InlineCostEstimator.getCost(rootNode);
        
        assertEquals(Integer.MAX_VALUE, result);
    }

}