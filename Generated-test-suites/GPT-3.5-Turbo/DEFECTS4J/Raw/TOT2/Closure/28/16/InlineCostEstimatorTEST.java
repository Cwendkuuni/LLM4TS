import static org.junit.Assert.*;
import org.junit.Test;

public class InlineCostEstimatorTest {

    // Test for getCost(Node root) method
    @Test
    public void testGetCostSingleNode() {
        Node root = new Node(Node.Token.EMPTY);
        int cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);
    }

    // Test for getCost(Node root, int costThreshhold) method
    @Test
    public void testGetCostWithThreshold() {
        Node root = new Node(Node.Token.EMPTY);
        int cost = InlineCostEstimator.getCost(root, 10);
        assertEquals(0, cost);
    }

    // Test for CompiledSizeEstimator class
    @Test
    public void testCompiledSizeEstimator() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        estimator.addIdentifier("test");
        assertEquals(2, estimator.getCost()); // Since identifier "ab" is used
    }

}