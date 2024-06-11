import org.junit.Test;
import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        // Test getCost method with empty root
        int cost = InlineCostEstimator.getCost(null);
        assertEquals(0, cost);

        // Test getCost method with non-empty root
        Node root = new Node();
        cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithThreshold() {
        // Test getCost method with empty root and cost threshold
        int cost = InlineCostEstimator.getCost(null, 100);
        assertEquals(0, cost);

        // Test getCost method with non-empty root and cost threshold
        Node root = new Node();
        cost = InlineCostEstimator.getCost(root, 100);
        assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimator() {
        // Test add method with empty root
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        estimator.add(null);
        int cost = estimator.getCost();
        assertEquals(0, cost);

        // Test add method with non-empty root
        Node root = new Node();
        estimator.add(root);
        cost = estimator.getCost();
        assertEquals(0, cost);
    }

    @Test
    public void testAppend() {
        // Test append method with empty string
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        estimator.append("");
        int cost = estimator.getCost();
        assertEquals(0, cost);

        // Test append method with non-empty string
        estimator.append("abc");
        cost = estimator.getCost();
        assertEquals(3, cost);
    }

    @Test
    public void testAddIdentifier() {
        // Test addIdentifier method
        InlineCostEstimator.CompiledSizeEstimator estimator = new InlineCostEstimator.CompiledSizeEstimator(100);
        estimator.addIdentifier("identifier");
        int cost = estimator.getCost();
        assertEquals(2, cost);
    }
}