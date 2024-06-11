import org.junit.Test;
import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    // Test case 1: Empty root node
    Node root1 = new Node();
    int cost1 = InlineCostEstimator.getCost(root1);
    assertEquals(0, cost1);

    // Test case 2: Root node with one child node
    Node root2 = new Node();
    Node child2 = new Node();
    root2.addChildToBack(child2);
    int cost2 = InlineCostEstimator.getCost(root2);
    assertEquals(2, cost2);

    // Test case 3: Root node with multiple child nodes
    Node root3 = new Node();
    Node child31 = new Node();
    Node child32 = new Node();
    root3.addChildToFront(child31);
    root3.addChildToBack(child32);
    int cost3 = InlineCostEstimator.getCost(root3);
    assertEquals(4, cost3);

    // Test case 4: Root node with nested child nodes
    Node root4 = new Node();
    Node child41 = new Node();
    Node child42 = new Node();
    Node grandchild41 = new Node();
    child41.addChildToBack(grandchild41);
    root4.addChildToFront(child41);
    root4.addChildToBack(child42);
    int cost4 = InlineCostEstimator.getCost(root4);
    assertEquals(6, cost4);
  }

  @Test
  public void testGetCostWithThreshold() {
    // Test case 1: Empty root node with threshold
    Node root1 = new Node();
    int cost1 = InlineCostEstimator.getCost(root1, 5);
    assertEquals(0, cost1);

    // Test case 2: Root node with one child node and threshold
    Node root2 = new Node();
    Node child2 = new Node();
    root2.addChildToBack(child2);
    int cost2 = InlineCostEstimator.getCost(root2, 5);
    assertEquals(2, cost2);

    // Test case 3: Root node with multiple child nodes and threshold
    Node root3 = new Node();
    Node child31 = new Node();
    Node child32 = new Node();
    root3.addChildToFront(child31);
    root3.addChildToBack(child32);
    int cost3 = InlineCostEstimator.getCost(root3, 5);
    assertEquals(4, cost3);

    // Test case 4: Root node with nested child nodes and threshold
    Node root4 = new Node();
    Node child41 = new Node();
    Node child42 = new Node();
    Node grandchild41 = new Node();
    child41.addChildToBack(grandchild41);
    root4.addChildToFront(child41);
    root4.addChildToBack(child42);
    int cost4 = InlineCostEstimator.getCost(root4, 5);
    assertEquals(5, cost4);
  }
}