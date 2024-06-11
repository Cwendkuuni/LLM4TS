import org.junit.Test;
import static org.junit.Assert.*;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {

    // Test case 1: Typical use case
    Node root1 = new Node();
    root1.addChildToFront(new Node(Token.ADD));
    root1.getFirstChild().addChildToFront(new Node(Token.NAME, new String("x")));
    root1.getFirstChild().addChildToBack(new Node(Token.NAME, new String("y")));

    int cost1 = InlineCostEstimator.getCost(root1);
    assertEquals(6, cost1);


    // Test case 2: Edge case with empty node
    Node root2 = new Node();

    int cost2 = InlineCostEstimator.getCost(root2);
    assertEquals(0, cost2);


    // Test case 3: Error scenario with negative costThreshold
    Node root3 = new Node();

    int cost3 = InlineCostEstimator.getCost(root3, -10);
    assertEquals(0, cost3);

  }
}