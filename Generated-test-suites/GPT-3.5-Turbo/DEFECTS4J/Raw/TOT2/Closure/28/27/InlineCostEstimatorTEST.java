import static org.junit.Assert.*;
import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        Node root = new Node("sample");
        int cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithThreshold() {
        Node root = new Node("sample");
        int cost = InlineCostEstimator.getCost(root, 10);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithLargeThreshold() {
        Node root = new Node("sample");
        int cost = InlineCostEstimator.getCost(root, Integer.MAX_VALUE);
        assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimatorAdd() {
        Node root = new Node("sample");
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        estimator.add(root);
        assertEquals(6, estimator.getCost());
    }

    @Test
    public void testCompiledSizeEstimatorGetCost() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        assertEquals(0, estimator.getCost());
    }

    @Test
    public void testCompiledSizeEstimatorAppend() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        estimator.append("sample");
        assertEquals(6, estimator.getCost());
    }

    @Test
    public void testCompiledSizeEstimatorAddIdentifier() {
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(10);
        estimator.addIdentifier("sample");
        assertEquals(4, estimator.getCost());
    }
}