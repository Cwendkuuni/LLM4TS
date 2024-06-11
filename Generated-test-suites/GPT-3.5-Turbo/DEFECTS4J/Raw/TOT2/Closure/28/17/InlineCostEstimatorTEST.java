import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        // Typical use case
        Node root = new Node("root");
        int cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithThreshold() {
        // Edge case: cost threshold is reached before processing completes
        Node root = new Node("root");
        int cost = InlineCostEstimator.getCost(root, 10);
        assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimator() {
        // Typical use case
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        estimator.addIdentifier("test");
        int cost = estimator.getCost();
        assertEquals(InlineCostEstimator.ESTIMATED_IDENTIFIER_COST, cost);
    }

    @Test
    public void testCompiledSizeEstimatorThreshold() {
        // Edge case: cost threshold is reached before processing completes
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
        estimator.addIdentifier("test");
        int cost = estimator.getCost();
        assertEquals(3, cost); // Only 'ab' should be counted due to threshold
    }
}