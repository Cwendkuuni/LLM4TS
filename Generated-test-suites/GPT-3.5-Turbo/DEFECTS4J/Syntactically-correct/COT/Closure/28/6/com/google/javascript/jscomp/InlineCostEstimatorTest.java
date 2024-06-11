import static org.junit.Assert.*;
import org.junit.Test;

public class InlineCostEstimatorTest {
    @Test
    public void testGetCost() {
        // Test case 1
        Node root1 = null;
        int cost1 = InlineCostEstimator.getCost(root1);
        assertEquals(0, cost1);

        // Test case 2
        Node root2 = new Node(1);
        int cost2 = InlineCostEstimator.getCost(root2);
        assertEquals(1, cost2);
    }

    @Test
    public void testGetCostWithThreshold() {
        // Test case 1
        Node root1 = null;
        int costThreshold1 = 10;
        int cost1 = InlineCostEstimator.getCost(root1, costThreshold1);
        assertEquals(0, cost1);

        // Test case 2
        Node root2 = new Node(1);
        int costThreshold2 = 10;
        int cost2 = InlineCostEstimator.getCost(root2, costThreshold2);
        assertEquals(1, cost2);
    }
}