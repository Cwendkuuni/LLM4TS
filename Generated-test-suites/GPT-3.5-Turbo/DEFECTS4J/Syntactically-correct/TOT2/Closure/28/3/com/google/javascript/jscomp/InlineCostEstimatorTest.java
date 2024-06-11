import static org.junit.Assert.*;
import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        Node root = new Node("root");
        int cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithThreshold() {
        Node root = new Node("root");
        int cost = InlineCostEstimator.getCost(root, 100);
        assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimator() {
        Node root = new Node("root");
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        estimator.add(root);
        int cost = estimator.getCost();
        assertEquals(0, cost);
    }

    @Test
    public void testAddIdentifier() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        estimator.addIdentifier("identifier");
        int cost = estimator.getCost();
        assertEquals(2, cost);
    }

    @Test
    public void testEstimatorThresholdReached() {
        Node root = new Node("root");
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
        estimator.add(root);
        int cost = estimator.getCost();
        assertTrue(estimator.continueProcessing());
        assertEquals(4, cost);
    }

    @Test
    public void testAppend() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        estimator.append("abc");
        int cost = estimator.getCost();
        assertEquals(3, cost);
    }
}