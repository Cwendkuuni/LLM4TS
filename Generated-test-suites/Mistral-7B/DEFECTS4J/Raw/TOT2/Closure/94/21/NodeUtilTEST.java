import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import org.junit.experimental.annotations.ExpectedException;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{0, 0, 0}, new Object[]{});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{0, 0}, new Object[]{});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{0, 0}, new Object[]{});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.FUNCTION, new int[]{0, 0, 0}, new Object[]{});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(
        Token.FUNCTION, new int[]{0, 0, 0, 0}, new Object[]{"testFunction", new Object[]{}});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(
        Token.FUNCTION, new int[]{0, 0, 0}, new Object[]{});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = new Node(
        Token.FUNCTION, new int[]{0, 0, 0, 0}, new Object[]{"testFunction", new Object[]{}});
    Node parentOfHoistedFunctionDeclaration = new Node(
        Token.SCRIPT, new int[]{0, 0}, new Object[]{});
    parentOfHoistedFunctionDeclaration.addChild(hoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
  }

  @Test
  public void testIsNotHoistedFunctionDeclarationUnderFunctionNode() {
    Node functionNode = new Node(
        Token.FUNCTION, new int[]{0, 0}, new Object[]{"testFunction", new Object[]{}});
    Node hoistedFunctionDeclaration = new Node(
        Token.FUNCTION, new int[]{0, 0, 0, 0}, new Object[]{"testFunction2", new Object[]{}});
    functionNode.addChild(hoistedFunctionDeclaration);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsForInWithInvalidNode() {
    Node invalidNode = new Node(Token.UNKNOWN, new int[]{0, 0}, new Object[]{});
    assertFalse(NodeUtil.isForIn(invalidNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithInvalidNode() {
    Node invalidNode = new Node(Token.UNKNOWN, new int[]{0, 0}, new Object[]{});
    assertFalse(NodeUtil.isStatement(invalidNode));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithInvalidNode() {
    Node invalidNode = new Node(Token.UNKNOWN, new int[]{0, 0}, new Object[]{});
    assertFalse(NodeUtil.isFunctionDeclaration(invalidNode));
  }

  @Test
  public void testIsHoistedFunctionDeclarationUnderScriptNode() {
    Node hoistedFunctionDeclaration = new Node(
        Token.FUNCTION, new int[]{0, 0, 0, 0}, new Object[]{"testFunction", new Object[]{}});
    Node parentOfHoistedFunctionDeclaration = new Node(
        Token.SCRIPT, new int[]{0, 0}, new Object[]{});
    parentOfHoistedFunctionDeclaration.addChild(hoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
  }

  @Test
  public void testIsNotHoistedFunctionDeclarationUnderFunctionNode() {
    Node functionNode = new Node(
        Token.FUNCTION, new int[]{0, 0}, new Object[]{"testFunction", new Object[]{}});
    Node hoistedFunctionDeclaration = new Node(
        Token.FUNCTION, new int[]{0, 0, 0, 0}, new Object[]{"testFunction2", new Object[]{}});
    functionNode.addChild(hoistedFunctionDeclaration);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));
  }
}