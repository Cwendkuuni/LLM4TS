import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    // Typical use case
    Node rootNode = new Node();
    rootNode.setType(Node.Type.SCRIPT);
    Node childNode = new Node(Token.NAME);
    rootNode.addChildToBack(childNode);

    int cost = InlineCostEstimator.getCost(rootNode);
    assertEquals(2, cost);

    // Edge case: empty root node
    Node emptyNode = new Node();
    emptyNode.setType(Node.Type.EMPTY);
    cost = InlineCostEstimator.getCost(emptyNode);
    assertEquals(0, cost);

    // Error scenario: null root node
    cost = InlineCostEstimator.getCost(null);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithThreshold() {
    // Typical use case
    Node rootNode = new Node();
    rootNode.setType(Node.Type.SCRIPT);
    Node childNode = new Node(Token.NAME);
    rootNode.addChildToBack(childNode);

    int threshold = 5;
    int cost = InlineCostEstimator.getCost(rootNode, threshold);
    assertEquals(2, cost);

    // Edge case: threshold is greater than cost
    threshold = 10;
    cost = InlineCostEstimator.getCost(rootNode, threshold);
    assertEquals(2, cost);

    // Edge case: threshold is smaller than cost
    threshold = 1;
    cost = InlineCostEstimator.getCost(rootNode, threshold);
    assertEquals(0, cost);
  }

}