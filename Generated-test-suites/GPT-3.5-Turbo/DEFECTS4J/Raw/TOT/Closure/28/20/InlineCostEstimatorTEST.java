import org.junit.Test;
import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

    @Test
    public void testGetCost_basicCase() {
        Node root = new Node();
        root.addChildToFront(new Node());

        int cost = InlineCostEstimator.getCost(root);

        assertEquals(2, cost);
    }

    @Test
    public void testGetCost_emptyTree() {
        Node root = new Node();

        int cost = InlineCostEstimator.getCost(root);

        assertEquals(0, cost);
    }

    @Test
    public void testGetCost_thresholdCase() {
        Node root = new Node();
        Node child1 = new Node();
        Node child2 = new Node();

        child1.addChildToFront(new Node());
        child2.addChildToFront(new Node());

        root.addChildToFront(child1);
        root.addChildToFront(child2);

        int cost = InlineCostEstimator.getCost(root, 5);

        assertEquals(5, cost);
    }

    @Test
    public void testGetCost_thresholdExceeded() {
        Node root = new Node();
        Node child1 = new Node();
        Node child2 = new Node();

        child1.addChildToFront(new Node());
        child2.addChildToFront(new Node());

        root.addChildToFront(child1);
        root.addChildToFront(child2);

        int cost = InlineCostEstimator.getCost(root, 3);

        assertEquals(2, cost);
    }

    @Test
    public void testGetCost_negativeThreshold() {
        Node root = new Node();

        int cost = InlineCostEstimator.getCost(root, -1);

        assertEquals(0, cost);
    }

    @Test
    public void testGetCost_nullRoot() {
        int cost = InlineCostEstimator.getCost(null);

        assertEquals(0, cost);
    }

    @Test
    public void testGetCost_thresholdNullRoot() {
        int cost = InlineCostEstimator.getCost(null, 5);

        assertEquals(0, cost);
    }

    @Test
    public void testGetCost_identifierCost() {
        Node root = new Node();
        root.addChildToFront(new Node());

        int cost = InlineCostEstimator.getCost(root);
        int expectedCost = 2 + 2; // 2 for identifier "ab"

        assertEquals(expectedCost, cost);
    }

    @Test
    public void testGetCostIdentifier_edgeCase() {
        Node root = new Node();
        root.addChildToFront(new Node());

        int cost = InlineCostEstimator.getCost(root, Integer.MAX_VALUE);

        assertEquals(Integer.MAX_VALUE, cost);
    }

    @Test
    public void testGetCostIdentifier_negativeCost() {
        Node root = new Node();
        root.addChildToFront(new Node());

        int cost = InlineCostEstimator.getCost(root, -5);

        assertEquals(0, cost);
    }

    @Test
    public void testGetCostIdentifier_nullRoot() {
        int cost = InlineCostEstimator.getCost(null);

        assertEquals(0, cost);
    }

    @Test
    public void testGetCostIdentifier_nullRootWithThreshold() {
        int cost = InlineCostEstimator.getCost(null, 5);

        assertEquals(0, cost);
    }

    @Test
    public void testGetCostIdentifier_emptyRoot() {
        Node root = new Node();

        int cost = InlineCostEstimator.getCost(root);

        assertEquals(0, cost);
    }

    @Test
    public void testGetCostIdentifier_emptyRootWithThreshold() {
        Node root = new Node();

        int cost = InlineCostEstimator.getCost(root, 5);

        assertEquals(0, cost);
    }

    @Test
    public void testGetCostIdentifier_thresholdExceeded() {
        Node root = new Node();
        Node child1 = new Node();
        Node child2 = new Node();

        child1.addChildToFront(new Node());
        child2.addChildToFront(new Node());

        root.addChildToFront(child1);
        root.addChildToFront(child2);

        int cost = InlineCostEstimator.getCost(root, 3);
        int expectedCost = 2 + 2; // 2 for identifier "ab"

        assertEquals(expectedCost, cost);
    }
}