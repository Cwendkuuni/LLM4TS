import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    // Test typical use case
    Node root = createTestNode();
    int expectedCost = getExpectedCost(root);
    int actualCost = InlineCostEstimator.getCost(root);
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCost_CostThreshold() {
    // Test when cost threshold is reached
    Node root = createTestNode();
    int costThreshold = 5;
    int expectedCost = costThreshold;
    int actualCost = InlineCostEstimator.getCost(root, costThreshold);
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCost_InvalidRoot() {
    // Test when root is null
    Node root = null;
    int expectedCost = 0;
    int actualCost = InlineCostEstimator.getCost(root);
    assertEquals(expectedCost, actualCost);
  }

  private Node createTestNode() {
    // Create a test Node for the typical use case
    Node root = new Node(Token.ROOT);
    Node child = new Node(Token.FUNCTION);
    root.addChildToBack(child);
    return root;
  }

  private int getExpectedCost(Node root) {
    // Calculate the expected cost based on the test Node
    int cost = InlineCostEstimator.ESTIMATED_IDENTIFIER_COST;
    cost += 2; // function keyword
    return cost;
  }
}