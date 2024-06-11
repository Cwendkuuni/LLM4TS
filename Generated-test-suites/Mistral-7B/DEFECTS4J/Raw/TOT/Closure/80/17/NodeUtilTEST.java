import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn("for (var i in array) {}", 0);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement("var i = 0;", 0);
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement("var i = 0;", 0);
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newExpression("++i", 0);
    assertFalse(isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction("function foo() {}", 0);
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionFunction("function foo() {}", 0);
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunctionStatement("function foo() {}", 0);
    Node nodeScript = Node.newScript("function bar() {}", 0);
    Node nodeFunctionInScript = Node.newFunctionStatement("function foo() {}", nodeScript);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionInScript));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionExpression));
  }
}