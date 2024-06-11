import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;

import org.junit.Test;

/**
 * Test class for NodeUtil.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node("for in");
    assertTrue(NodeUtil.isForIn(node));

    Node nodeNotForIn = new Node("if");;
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node node = new Node("Statement");
    assertTrue(NodeUtil.isStatement(node));

    Node nodeNotStatement = new Node("Expression");;
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node("function foo() {}");
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = new Node("function bar() {}"); // should be considered as statement
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = new Node("function foo() {}", new Node("script", new Node("function bar() {}")));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    Node notHoistedFunctionDeclaration = new Node("function foo() {}", new Node("node"));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclaration));
  }
}