import static org.junit.Assert.*;
import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        Node root = new Node("test");
        int result = InlineCostEstimator.getCost(root);
        assertEquals(4, result);
    }

    @Test
    public void testGetCostWithThreshold() {
        Node root = new Node("test");
        int result = InlineCostEstimator.getCost(root, 10);
        assertEquals(4, result);
    }

    @Test
    public void testCompiledSizeEstimatorAdd() {
        Node root = new Node("test");
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        estimator.add(root);
        assertEquals(4, estimator.getCost());
    }

    @Test
    public void testCompiledSizeEstimatorThreshold() {
        Node root = new Node("test");
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(5);
        estimator.add(root);
        assertEquals(4, estimator.getCost());
    }
}