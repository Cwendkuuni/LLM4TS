import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        Node root = new Node("testRoot");
        int cost = InlineCostEstimator.getCost(root);

        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithThreshold() {
        Node root = new Node("testRoot");
        int cost = InlineCostEstimator.getCost(root, 10);

        assertEquals(0, cost);
    }

    @Test
    public void testCompiledSizeEstimatorAdd() {
        CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
        Node root = new Node("testRoot");
        estimator.add(root);

        int cost = estimator.getCost();
        assertTrue(cost <= 10);
    }

}