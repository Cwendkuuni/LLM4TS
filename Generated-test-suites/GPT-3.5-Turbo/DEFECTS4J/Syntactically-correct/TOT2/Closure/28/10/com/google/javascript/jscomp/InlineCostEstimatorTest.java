import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    // Typical use case
    Node root = new Node("root");
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(10, cost);

    // Edge case: empty node
    Node emptyRoot = new Node("");
    int emptyCost = InlineCostEstimator.getCost(emptyRoot);
    assertEquals(0, emptyCost);

    // Error scenario: null node
    Node nullRoot = null;
    int nullCost = InlineCostEstimator.getCost(nullRoot);
    assertEquals(0, nullCost);
  }

  @Test
  public void testGetCostWithThreshold() {
    // Typical use case
    Node root = new Node("root");
    int cost = InlineCostEstimator.getCost(root, 20);
    assertEquals(10, cost);

    // Edge case: exceeding threshold
    int highCost = InlineCostEstimator.getCost(root, 5);
    assertEquals(0, highCost);

    // Error scenario: negative threshold
    int negativeCost = InlineCostEstimator.getCost(root, -10);
    assertEquals(0, negativeCost);
  }
}