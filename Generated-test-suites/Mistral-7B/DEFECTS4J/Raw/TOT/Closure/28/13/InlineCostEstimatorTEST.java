import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostRoot() {
    Node root = Node.objectLiteral(); // empty object
    int cost = getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostRootWithThreshold() {
    Node root = Node.objectLiteral(); // empty object
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(1);
    getCost(root, estimator);
    assertEquals(0, estimator.getCost());
  }

  @Test
  public void testGetCostSimpleExpression() {
    Node root = Node.name("x");
    root.setDouble(10.0);
    int cost = getCost(root);
    assertEquals(ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testGetCostComplexExpression() {
    Node root = Node.binaryOp("+", Node.name("x"), Node.number(5));
    int cost = getCost(root);
    assertEquals(2 * ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testGetCostArray() {
    Node root = Node.arrayLiteral();
    int cost = getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostArrayWithElements() {
    Node root = Node.arrayLiteral(Node.number(1), Node.number(2), Node.number(3));
    int cost = getCost(root);
    assertEquals(3 * ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testGetCostObject() {
    Node root = Node.objectLiteral();
    root.putProperty("key1", Node.number(1));
    root.putProperty("key2", Node.number(2));
    int cost = getCost(root);
    assertEquals(3 * ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testGetCostComplexObject() {
    Node root = Node.objectLiteral();
    root.putProperty("key1", Node.binaryOp("+", Node.name("x"), Node.number(5)));
    root.putProperty("key2", Node.binaryOp("*", Node.name("y"), Node.number(3)));
    int cost = getCost(root, Integer.MAX_VALUE);
    assertEquals(2 * ESTIMATED_IDENTIFIER_COST * 2 + 3 * ESTIMATED_IDENTIFIER_COST, cost);
  }

  @Test
  public void testGetCostComplexExpressionWithThreshold() {
    Node root = Node.binaryOp("*", Node.name("x"), Node.binaryOp("+", Node.name("y"), Node.number(5)));
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(10);
    getCost(root, estimator);
    assertEquals(2 * ESTIMATED_IDENTIFIER_COST * 2 + 2 * ESTIMATED_IDENTIFIER_COST + 1, estimator.getCost());
  }
}