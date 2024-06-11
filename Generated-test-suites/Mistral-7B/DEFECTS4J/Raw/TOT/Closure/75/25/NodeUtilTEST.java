import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Context.getCurrentContext().newNode("for (var i in array) { }");
    assertTrue(isForIn(nodeForIn));

    Node nodeFor = Context.getCurrentContext().newNode("for (var i = 0; i < 10; i++) { }");
    assertFalse(isForIn(nodeFor));

    Node nodeIf = Context.getCurrentContext().newNode("if (true) {}");
    assertFalse(isForIn(nodeIf));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Context.getCurrentContext().newNode("{}");
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Context.getCurrentContext().newNode("5 + 3");
    assertFalse(isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Context.getCurrentContext().newNode(
        "function myFunction() { }", null, null);
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Context.getCurrentContext().newNode(
        "var myFunction = function() { }", null, null);
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Context.getCurrentContext().newNode("function myFunction() { }", null, null);
    Node nodeFunctionDeclarationInScript = nodeScript.getFirstChild();
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

    Node nodeFunctionDeclarationInFunction = Context.getCurrentContext().newNode(
        "function myFunction() { }\nfunction myFunction2() { }", null, null);
    Node nodeFunctionDeclaration = nodeFunctionDeclarationInFunction.getFirstChild();
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }
}