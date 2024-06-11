
// JUnit 4 test cases for the InlineCostEstimator class

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

    @Test(expected = NullPointerException.class)
    public void testGetCostWithNullRoot() {
        InlineCostEstimator.getCost(null);
    }

    @Test(expected = NullPointerException.class)
    public void testGetCostWithThresholdNullRoot() {
        InlineCostEstimator.getCost(null, 100);
    }

    @Test
    public void testGetCostWithMultipleNodes() {
        Node root = new Node();
        root.addChildToFront(new Node());
        root.addChildToFront(new Node());

        int cost = InlineCostEstimator.getCost(root);
        assertEquals(6, cost);
    }

    @Test
    public void testGetCostWithSmallThreshold() {
        Node root = new Node();
        int cost = InlineCostEstimator.getCost(root, 1);
        assertEquals(0, cost);
    }
}