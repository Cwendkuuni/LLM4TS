import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyNode() {
    Node root = Node.NULL();
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    int actualCost = estimator.getCost();
    int expectedCost = 0;
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfSimpleExpression() {
    Node root = Node.newBinaryOp("+", Node.newNumber(1), Node.newNumber(2));
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    int actualCost = estimator.getCost();
    int expectedCost = 5; // '+' and 2 numbers
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfComplexExpression() {
    Node root = Node.newBinaryOp("+", Node.newBinaryOp("*", Node.newNumber(1), Node.newNumber(2)), Node.newBinaryOp("/", Node.newNumber(3), Node.newNumber(4)));
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    int actualCost = estimator.getCost();
    int expectedCost = 19; // '+' and 3 binary operators and 4 numbers
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfStringLiteral() {
    Node root = Node.newStringLiteral("Hello World");
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    int actualCost = estimator.getCost();
    int expectedCost = 11; // "Hello World"
    assertEquals(expectedCost, actualCost);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetCostOfNullNode() {
    Node root = Node.NULL();
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(null);
  }

  @Test
  public void testGetCostOfSimpleStatement() {
    Node root = Node.newExpressionStatement(Node.newAssignment("x", Node.newNumber(10)));
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    int actualCost = estimator.getCost();
    int expectedCost = 8; // "x =" and 1 number
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfComplexStatement() {
    Node root = Node.newExpressionStatement(Node.newAssignment("x", Node.newBinaryOp("+", Node.newNumber(1), Node.newBinaryOp("*", Node.newNumber(2), Node.newNumber(3)))));
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    int actualCost = estimator.getCost();
    int expectedCost = 23; // "x =" and 1 '+' and 1 '*' and 3 numbers
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfFunctionExpression() {
    Node root = Node.newFunctionExpression("foo", Node.newArray(Node.newParameter("x", "number")), Node.newBlock(Node.newReturnStatement(Node.newNumber(1))));
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    int actualCost = estimator.getCost();
    int expectedCost = 69; // "function foo(" and 1 "number" and "{" and 1 "return" and 1 number and "}"
    assertEquals(expectedCost, actualCost);
  }

  @Test
  public void testGetCostOfFunctionDeclaration() {
    Node root = Node.newFunctionDeclaration("foo", Node.newArray(Node.newParameter("x", "number")), Node.newBlock(Node.newReturnStatement(Node.newNumber(1))));
    CompiledSizeEstimator estimator = new CompiledSizeEstimator(Integer.MAX_VALUE);
    estimator.add(root);
    int actualCost = estimator.getCost();
    int expectedCost = 58; // "function foo(" and 1 "number" and "{" and 1 "return" and 1 number and "}"
    assertEquals(expectedCost, actualCost);
  }
}