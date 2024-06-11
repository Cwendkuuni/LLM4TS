import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = createNode("for (var i in array) { }");
    Node invalidNode = createNode("if (true) { }");

    assertTrue(isForIn(forNode));
    assertFalse(isForIn(invalidNode));
  }

  @Test
  public void testIsStatement() {
    Node scriptNode = createNode("function test() { }");
    Node expressionNode = createNode("function test() { return 0; }");

    assertFalse(isStatement(scriptNode.getChildren().get(0))); // function declaration is not a statement in this context
    assertTrue(isStatement(expressionNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = createNode("function test() { }");
    Node functionExpression = createNode("(function() { })()");

    assertTrue(isFunctionDeclaration(functionDeclaration));
    assertFalse(isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = createNode("function test() { }");
    Node functionDeclarationInFunction = createNode("function testFunction() { function test() { } }");

    assertTrue(isHoistedFunctionDeclaration(scriptNode));
    assertFalse(isHoistedFunctionDeclaration(functionDeclarationInFunction));
  }

  private Node createNode(String nodeCode) {
    return new rhino.eval(new java.io.StringReader(nodeCode)).exec();
  }
}