import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    // Test 1
    Node root1 = new Node(Node.Type.ARRAYLIT);
    int cost1 = InlineCostEstimator.getCost(root1);
    assertEquals(0, cost1);

    // Test 2
    Node root2 = new Node(Node.Type.ARRAYLIT);
    root2.addChildToBack(new Node(Node.Type.STRING));
    int cost2 = InlineCostEstimator.getCost(root2);
    assertEquals(1, cost2);

    // Test 3
    Node root3 = new Node(Node.Type.ARRAYLIT);
    root3.addChildToBack(new Node(Node.Type.STRING));
    root3.addChildToBack(new Node(Node.Type.NUMBER));
    int cost3 = InlineCostEstimator.getCost(root3);
    assertEquals(2, cost3);
  }

  @Test
  public void testGetCostWithThreshold() {
    // Test 1
    Node root1 = new Node(Node.Type.ARRAYLIT);
    int cost1 = InlineCostEstimator.getCost(root1, 0);
    assertEquals(0, cost1);

    // Test 2
    Node root2 = new Node(Node.Type.ARRAYLIT);
    root2.addChildToBack(new Node(Node.Type.STRING));
    int cost2 = InlineCostEstimator.getCost(root2, 1);
    assertEquals(1, cost2);

    // Test 3
    Node root3 = new Node(Node.Type.ARRAYLIT);
    root3.addChildToBack(new Node(Node.Type.STRING));
    root3.addChildToBack(new Node(Node.Type.NUMBER));
    int cost3 = InlineCostEstimator.getCost(root3, 2);
    assertEquals(2, cost3);
  }

}