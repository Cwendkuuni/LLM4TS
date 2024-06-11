import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostWithNullNode() {
    int cost = getCost(null, 100);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithEmptyNode() {
    Node root = Node.NULL();
    int cost = getCost(root, 100);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostWithSimpleNode() {
    Node root = Node.identifier("a");
    int cost = getCost(root, 100);
    assertEquals(2, cost);
  }

  @Test
  public void testGetCostWithComplexNode() {
    Node root = Node.newBinOp(Node.OP_ADD, Node.literal(1), Node.literal(2));
    int cost = getCost(root, 100);
    assertEquals(4, cost);
  }

  @Test
  public void testGetCostWithLargeNode() {
    String complexExpression = "a + b + c + d + e + f + g + h + i + j + k + l + m + n + o + p + q + r + s + t + u + v + w + x + y + z";
    Node root = Node.parse(complexExpression);
    int cost = getCost(root, 100);
    int expectedCost = complexExpression.length();
    assertEquals(expectedCost, cost);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostWithNegativeCostThreshold() {
    getCost(Node.NULL(), -1);
  }

  @Test
  public void testCompiledSizeEstimator() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    Node root = Node.identifier("a");
    estimator.add(root);
    int cost = estimator.getCost();
    assertEquals(2, cost);
  }

  @Test
  public void testCompiledSizeEstimatorWithLargeNode() {
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(100);
    String complexExpression = "a + b + c + d + e + f + g + h + i + j + k + l + m + n + o + p + q + r + s + t + u + v + w + x + y + z";
    Node root = Node.parse(complexExpression);
    estimator.add(root);
    int cost = estimator.getCost();
    int expectedCost = complexExpression.length();
    assertEquals(expectedCost, cost);
  }
}