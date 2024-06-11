
import org.junit.Test;
import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        Node root = createTestNode();
        int result = InlineCostEstimator.getCost(root);
        assertEquals(10, result);
    }

    @Test
    public void testGetCostWithThreshold() {
        Node root = createTestNode();
        int result = InlineCostEstimator.getCost(root, 5);
        assertEquals(5, result);
    }

    private Node createTestNode() {
        // Create a test node for simulation
        Node root = new Node("root");
        root.addChildToFront(new Node("child1"));
        root.addChildToFront(new Node("child2"));
        root.addChildToFront(new Node("child3"));
        return root;
    }
}

