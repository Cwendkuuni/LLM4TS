import static org.junit.Assert.assertEquals;
import org.junit.Test;
class InlineCostEstimatorTest{

    @Test
    public void testGetCost() {
        // Test case 1
        Node root1 = new Node();
        int cost1 = InlineCostEstimator.getCost(root1);
        assertEquals(0, cost1);

        // Test case 2
        Node root2 = null;
        int cost2 = InlineCostEstimator.getCost(root2);
        assertEquals(0, cost2);
    }

    @Test
    public void testGetCostWithThreshold() {
        int threshold = 10;

        // Test case 1
        Node root1 = new Node();
        int cost1 = InlineCostEstimator.getCost(root1, threshold);
        assertEquals(0, cost1);

        // Test case 2
        Node root2 = null;
        int cost2 = InlineCostEstimator.getCost(root2, threshold);
        assertEquals(0, cost2);

        // Test case 3
        Node root3 = new Node();
        int cost3 = InlineCostEstimator.getCost(root3, threshold + 1);
        assertEquals(0, cost3);

        // Test case 4
        Node root4 = null;
        int cost4 = InlineCostEstimator.getCost(root4, threshold + 1);
        assertEquals(0, cost4);
    }
}