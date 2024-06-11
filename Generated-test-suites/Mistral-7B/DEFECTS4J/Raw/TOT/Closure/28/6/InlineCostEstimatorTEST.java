import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyNode() {
    Node root = Node.NULL();
    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostOfSimpleExpression() {
    Node root = Node.newIdent("x");
    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(root, 10);
    assertEquals(2, cost); // 1 for "x" and 1 for leading and trailing whitespace

    root = Node.newLiteral(true);
    cost = estimator.getCost(root, 10);
    assertEquals(0, cost); // Constants are considered essentially free
  }

  @Test
  public void testGetCostOfComplexExpression() {
    String complexExpression = "if (x > 10) { y = 5 * z + 3; }";
    Node root = Node.parse(complexExpression);
    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(root, 50);
    // Assuming "x", "y", "z" are used elsewhere in the code, their cost is already accounted for in the estimation of other parts of the code.
    // The "if" statement and assignment are free as well.
    // The multiplication and addition operations have an estimated cost of 3 each.
    // Whitespace and other characters add an additional 2-3 characters per operator.
    assertEquals(18, cost); // 3 for multiplication, 3 for addition, 2 for "if" statement, 1 for assignment, and some whitespace
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeCostThreshold() {
    Node root = Node.newLiteral(true);
    InlineCostEstimator estimator = new InlineCostEstimator();
    estimator.getCost(root, -1);
  }
}