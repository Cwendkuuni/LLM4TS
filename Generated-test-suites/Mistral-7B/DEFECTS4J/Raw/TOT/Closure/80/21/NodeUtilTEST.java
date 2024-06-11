import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null, null, null, null, 0, 0);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, null, null, null, null, null, null, null, 0, 0);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, null, null, null, null, null, null, null, 0, 0);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.ADD, null, null, null, null, null, null, null, 0, 0);
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, null, null, null, null, null, null, null, 0, 0);
    nodeFunctionDeclaration.setProperty("id", "testFunction");

    Node nodeFunctionExpression = Node.newNode(
        Token.FUNCTION, null, null, null, null, null, null, null, 0, 0);
    nodeFunctionExpression.setProperty("id", "testFunction");

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null, null, null, null, 0, 0);
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, null, null, null, null, null, null, null, 0, 0);
    nodeFunctionDeclaration.setProperty("id", "testFunction");

    Node nodeFunction = Node.newNode(
        Token.FUNCTION, null, null, null, null, null, null, null, 0, 0);
    nodeFunction.setProperty("id", "testFunction");
    Node child = Node.newNode(Token.BLOCK, null, null, null, null, null, null, null, 0, 0);
    nodeFunction.addChild(child);

    Node nodeFunctionDeclarationInFunction = Node.newNode(
        Token.FUNCTION, null, null, null, null, null, null, null, 0, 0);
    nodeFunctionDeclarationInFunction.setProperty("id", "testFunction");
    Node function = Node.newNode(Token.FUNCTION, null, null, null, null, null, null, null, 0, 0);
    function.addChild(nodeFunctionDeclarationInFunction);
    child.addChild(function);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}