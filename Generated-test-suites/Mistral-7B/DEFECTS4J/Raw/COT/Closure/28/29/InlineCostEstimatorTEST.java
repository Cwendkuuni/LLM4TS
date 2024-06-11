import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCost() {
    Node root = Node.newNull(); // Test with an empty node
    int cost = InlineCostEstimator.getCost(root);
    assertEquals(0, cost);

    // Test with a simple Node
    Node simpleNode = Node.newText("var x = 10;");
    cost = InlineCostEstimator.getCost(simpleNode);
    assertEquals(12, cost); // 2 for "var", 1 for "x", 1 for "=", 1 for "10", 7 for ";"

    // Test with a complex Node
    Node complexNode = Node.newObjectLiteral();
    complexNode.setProperty("a", Node.newNumberLiteral(1.0));
    complexNode.setProperty("b", Node.newObjectLiteral()
        .setProperty("c", Node.newNumberLiteral(2.0))
        .setProperty("d", Node.newArrayLiteral()
            .setElementAt(Node.newNumberLiteral(3.0), 0)
            .setElementAt(Node.newStringLiteral("foo"), 1)
        )
    );
    cost = InlineCostEstimator.getCost(complexNode);
    assertEquals(111, cost); // 2 for "{" and "}", 2 for each property name, 7 for each number and string, 3 for "new ArrayLiteral"
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeCostThreshold() {
    Node root = Node.newNull();
    InlineCostEstimator.getCost(root, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNullNode() {
    InlineCostEstimator.getCost(null);
  }
}