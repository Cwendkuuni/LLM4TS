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
    public void testGetCostWithThreshold() {
        Node root = new Node();
        int cost = InlineCostEstimator.getCost(root, 100);
        assertEquals(0, cost);
    }
}