import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class InlineCostEstimatorTest {
    
    @Test
    public void testGetCost() {
        // Test if the getCost method returns the correct cost for a given Node
        Node node = new Node(Node.Type.VAR, "var");
        int expectedCost = 3; // Assuming 'var' takes 3 characters
        int actualCost = InlineCostEstimator.getCost(node);
        assertEquals(expectedCost, actualCost);
        
        // Test if the getCost method returns 0 when the root is null
        int expectedCostNull = 0;
        int actualCostNull = InlineCostEstimator.getCost(null);
        assertEquals(expectedCostNull, actualCostNull);
    }
    
    @Test
    public void testGetCostThreshold() {
        // Test if the getCost method with a cost threshold returns the correct cost for a given Node
        Node node = new Node(Node.Type.VAR, "var");
        int costThreshold = 5;
        int expectedCost = 3; // Assuming 'var' takes 3 characters
        int actualCost = InlineCostEstimator.getCost(node, costThreshold);
        assertEquals(expectedCost, actualCost);
        
        // Test if the getCost method with a cost threshold returns 0 when the root is null
        int expectedCostNull = 0;
        int actualCostNull = InlineCostEstimator.getCost(null, costThreshold);
        assertEquals(expectedCostNull, actualCostNull);
        
        // Test if the getCost method with a cost threshold stops processing when the cost exceeds the threshold
        Node rootNode = new Node(Node.Type.BLOCK);
        Node childNode1 = new Node(Node.Type.EXPR_RESULT, "expression1");
        Node childNode2 = new Node(Node.Type.EXPR_RESULT, "expression2");
        rootNode.addChildToBack(childNode1);
        rootNode.addChildToBack(childNode2);
        int costThresholdExceeded = 10;
        int expectedCostThresholdExceeded = 9; // Assuming 'expression1' and 'expression2' take 9 characters
        int actualCostThresholdExceeded = InlineCostEstimator.getCost(rootNode, costThresholdExceeded);
        assertEquals(expectedCostThresholdExceeded, actualCostThresholdExceeded);
    }
}