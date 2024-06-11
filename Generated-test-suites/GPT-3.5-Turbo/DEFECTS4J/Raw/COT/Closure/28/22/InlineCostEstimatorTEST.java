
import org.junit.Test;
import static org.junit.Assert.*;

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
        int costThreshold = 100;
        int cost = InlineCostEstimator.getCost(root, costThreshold);
        assertEquals(0, cost);
    }
}

