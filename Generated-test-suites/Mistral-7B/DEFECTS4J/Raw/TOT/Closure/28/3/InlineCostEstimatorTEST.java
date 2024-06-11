import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.InlineCostEstimator;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class InlineCostEstimatorTest {

  @Test
  public void testGetCostOfEmptyNode() {
    Node root = Node.NUL;
    InlineCostEstimator estimator = new InlineCostEstimator();
    int cost = estimator.getCost(root);
    assertEquals(0, cost);
  }

  @Test
  public void testGetCostOfSimpleStatementNode() {
    Node root = Node.newIdent("x");
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node mockCodePrinter = mock(CodePrinter.class);
    when(mockCodePrinter.print).thenReturn(Node.NUL);
    CodeGenerator codeGenerator = new CodeGenerator(mockCodePrinter);
    estimator.add(root);
    codeGenerator.add(root);
    int cost = estimator.getCost(root);
    assertEquals(2, cost); // 1 for "x" identifier and 1 for null terminator
  }

  @Test
  public void testGetCostOfComplexExpressionNode() {
    Node root = Node.newBinaryOp("+", Node.newIdent("x"), Node.newNumber(10));
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node mockCodePrinter = mock(CodePrinter.class);
    when(mockCodePrinter.print).thenReturn(Node.NUL);
    CodeGenerator codeGenerator = new CodeGenerator(mockCodePrinter);
    estimator.add(root);
    codeGenerator.add(root);
    int cost = estimator.getCost(root);
    assertEquals(5, cost); // 2 for "x" identifier, 2 for "+" and 1 for number
  }

  @Test
  public void testGetCostLimit() {
    Node root = Node.newArray(Node.newNumber(5), Node.newIdent("x"));
    InlineCostEstimator estimator = new InlineCostEstimator();
    Node mockCodePrinter = mock(CodePrinter.class);
    when(mockCodePrinter.print).thenReturn(Node.NUL);
    CodeGenerator codeGenerator = new CodeGenerator(mockCodePrinter);
    estimator.add(root);
    codeGenerator.add(root);
    int cost = estimator.getCost(root);
    assertEquals(12, cost); // 5 for "x" array identifier and 7 for null terminator
  }
}