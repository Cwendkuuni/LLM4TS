import org.junit.Test;

import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        // Test getCost method with a root node
        Node root = new Node("root");

        int cost = InlineCostEstimator.getCost(root);

        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithCostThreshold() {
        // Test getCost method with a root node and cost threshold
        Node root = new Node("root");

        int cost = InlineCostEstimator.getCost(root, 100);

        assertEquals(0, cost);
    }
}