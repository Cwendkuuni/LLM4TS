
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class InlineCostEstimatorTest {
    @Test
    public void testGetCost() {
        Node root = new Node();
        int cost = InlineCostEstimator.getCost(root);
        assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithCostThreshold() {
        Node root = new Node();
        int cost = InlineCostEstimator.getCost(root, 10);
        assertEquals(0, cost);
    }
}
