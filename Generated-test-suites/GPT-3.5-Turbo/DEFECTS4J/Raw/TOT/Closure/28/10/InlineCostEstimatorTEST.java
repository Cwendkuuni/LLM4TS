import org.junit.Test;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    // Typical use case
    Node root = createRootNode();
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(10, cost);

    // Edge case
    Node emptyRoot = createEmptyRootNode();
    int emptyCost = InlineCostEstimator.getCost(emptyRoot);
    assertEquals(0, emptyCost);

    // Error scenario
    Node nullRoot = null;
    int nullCost = InlineCostEstimator.getCost(nullRoot);
    assertEquals(0, nullCost);
  }

  @Test
  public void testGetCostWithThreshold() {
    // Typical use case
    Node root = createRootNode();
    int cost = InlineCostEstimator.getCost(root, 15);
    assertEquals(10, cost);

    // Edge case
    Node emptyRoot = createEmptyRootNode();
    int emptyCost = InlineCostEstimator.getCost(emptyRoot, 5);
    assertEquals(0, emptyCost);

    // Error scenario
    Node nullRoot = null;
    int nullCost = InlineCostEstimator.getCost(nullRoot, 8);
    assertEquals(0, nullCost);
  }

  private Node createRootNode() {
    Node root = new Node("root");
    Node child1 = new Node("child1");
    Node child2 = new Node("child2");
    root.addChildToFront(child1);
    root.addChildToBack(child2);
    return root;
  }

  private Node createEmptyRootNode() {
    Node root = new Node("root");
    return root;
  }
}