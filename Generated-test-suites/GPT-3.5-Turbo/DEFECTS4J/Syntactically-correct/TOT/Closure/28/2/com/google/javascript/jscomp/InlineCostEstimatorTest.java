import static org.junit.Assert.*;
import org.junit.Test;

public class InlineCostEstimatorTest {
  @Test
  public void testGetCost_emptyNode() {
    Node root = new Node();
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCost_singleCharacterNode() {
    Node root = new Node((char)97);
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(1, cost);
  }

  @Test
  public void testGetCost_multipleCharacterNode() {
    Node root = new Node("abc");
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(3, cost);
  }

  @Test
  public void testGetCost_identifierNode() {
    Node root = new Node("foo");
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(2, cost);
  }

  @Test
  public void testGetCost_identifierNodeWithCostThreshold() {
    Node root = new Node("foo");
    int cost = InlineCostEstimator.getCost(root, 5);
    assertEquals(2, cost);
  }

  @Test
  public void testGetCost_fullCodeSnippet() {
    Node root = createFullCodeSnippet();
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(100, cost);
  }

  @Test
  public void testGetCost_fullCodeSnippetWithCostThreshold() {
    Node root = createFullCodeSnippet();
    int cost = InlineCostEstimator.getCost(root, 80);
    assertEquals(80, cost);
  }

  private Node createFullCodeSnippet() {
    Node root = new Node();

    Node parentNode = new Node();
    root.addChildToBack(parentNode);

    for (int i = 0; i < 10; i++) {
      Node childNode = new Node("abc");
      parentNode.addChildToBack(childNode);
    }

    return root;
  }
}