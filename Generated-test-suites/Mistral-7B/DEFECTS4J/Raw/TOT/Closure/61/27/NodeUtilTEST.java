import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.jscomp.testing.TopLevelTestingRuntime;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = TopLevelTestingRuntime.getCurrentRuntime().newAST(
        "for (var i in array) {}", Token.FOR);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = TopLevelTestingRuntime.getCurrentRuntime().newAST(
        "for (var i = 0; i < 10; i++) {}", Token.FOR);
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = TopLevelTestingRuntime.getCurrentRuntime().newAST(
        "if (x > 0) {}", Token.IF);
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = TopLevelTestingRuntime.getCurrentRuntime().newAST(
        "function foo() {}", Token.FUNCTION);
    assertFalse(isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = TopLevelTestingRuntime.getCurrentRuntime().newAST(
        "function foo() {}", Token.FUNCTION);
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = TopLevelTestingRuntime.getCurrentRuntime().newAST(
        "(function() {})()");
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = TopLevelTestingRuntime.getCurrentRuntime().newAST(
        "function foo() {}", Token.FUNCTION, null, null,
        new Node[0], new Node[0], null);
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNonHoistedFunctionDeclaration = TopLevelTestingRuntime.getCurrentRuntime().newAST(
        "function foo() {}",
        Token.FUNCTION, null, null, TopLevelTestingRuntime.getCurrentRuntime().newAST("var x = 5"),
        new Node[0], null);
    assertFalse(isHoistedFunctionDeclaration(nodeNonHoistedFunctionDeclaration));
  }
}