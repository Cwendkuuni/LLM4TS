import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Test;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost() {
        Node root = new Node(Node.SCRIPT);
        int cost = InlineCostEstimator.getCost(root);
        Assert.assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithCostThreshold() {
        Node root = new Node(Node.SCRIPT);
        int cost = InlineCostEstimator.getCost(root, 100);
        Assert.assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithRootNode() {
        Node root = new Node(Node.SCRIPT);
        int cost = InlineCostEstimator.getCost(root);
        Assert.assertEquals(0, cost);
    }

    @Test
    public void testGetCostWithRootNodeAndCostThreshold() {
        Node root = new Node(Node.SCRIPT);
        int cost = InlineCostEstimator.getCost(root, 100);
        Assert.assertEquals(0, cost);
    }
}