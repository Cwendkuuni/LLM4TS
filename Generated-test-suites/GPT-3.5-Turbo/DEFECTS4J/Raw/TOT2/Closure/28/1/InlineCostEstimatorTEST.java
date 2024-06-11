import static org.junit.Assert.*;
import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        Node root = new Node("root");
        int result = InlineCostEstimator.getCost(root);
        assertEquals(0, result);
    }

    @Test
    public void testGetCostWithThreshold() {
        Node root = new Node("root");
        int result = InlineCostEstimator.getCost(root, 10);
        assertEquals(0, result);
    }

    @Test
    public void testCompiledSizeEstimatorAdd() {
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
        Node root = new Node("root");
        estimator.add(root);
        int cost = estimator.getCost();
        assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimatorGetCost() {
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
        int cost = estimator.getCost();
        assertEquals(0, cost);
    }
}