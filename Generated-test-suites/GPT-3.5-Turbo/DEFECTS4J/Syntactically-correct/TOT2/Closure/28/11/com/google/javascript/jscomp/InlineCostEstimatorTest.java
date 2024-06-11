// Expert 1
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        Node root = new Node("root");
        int cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithCostThreshold() {
        Node root = new Node("root");
        int costThreshold = 10;
        int cost = InlineCostEstimator.getCost(root, costThreshold);
        assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimator() {
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
        Node root = new Node("root");
        estimator.add(root);
        int cost = estimator.getCost();
        assertEquals(0, cost);
    }

    @Test
    public void testAddIdentifier() {
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
        estimator.addIdentifier("testIdentifier");
        int cost = estimator.getCost();
        assertEquals(2, cost); // Since ESTIMATED_IDENTIFIER = "ab"
    }
}