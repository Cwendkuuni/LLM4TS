import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        Node root = new Node();
        int result = InlineCostEstimator.getCost(root);
        assertEquals(0, result);
    }

    @Test
    public void testGetCostWithThreshold() {
        Node root = new Node();
        int costThreshold = 100;
        int result = InlineCostEstimator.getCost(root, costThreshold);
        assertEquals(0, result);
    }

}