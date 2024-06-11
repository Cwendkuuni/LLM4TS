import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        // Typical use case
        Node root = new Node("root");
        int cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);

        // Edge case: when root is null
        cost = InlineCostEstimator.getCost(null);
        assertEquals(0, cost);

        // Edge case: cost threshold is reached
        cost = InlineCostEstimator.getCost(root, 1);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithThreshold() {
        // Typical use case
        Node root = new Node("root");
        int cost = InlineCostEstimator.getCost(root, Integer.MAX_VALUE);
        assertEquals(0, cost);

        // Edge case: when root is null
        cost = InlineCostEstimator.getCost(null, Integer.MAX_VALUE);
        assertEquals(0, cost);

        // Edge case: cost threshold is reached
        cost = InlineCostEstimator.getCost(root, 1);
        assertEquals(0, cost);
    }
}