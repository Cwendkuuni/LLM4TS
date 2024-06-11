import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null);
    Node nodeFor = Node.newNode(Token.FOR, null, null, null, null);
    Node nodeIf = Node.newNode(Token.IF, null, null, null, null);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeFor));
    assertFalse(NodeUtil.isForIn(nodeIf));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null);
    Node nodeFunction = Node.newNode(Token.FUNCTION, null, null, null, null);
    Node nodeExpression = Node.newNode(Token.IDENT, "x", null, null, null);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newNode(Token.FUNCTION, null, null, null, null);
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, null, null, null, null, null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null);
    Node nodeFunction = Node.newNode(Token.FUNCTION, null, null, null, null);
    Node nodeFunctionInFunction = Node.newNode(Token.FUNCTION, null, null, nodeFunction, null);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testIsHoistedFunctionDeclarationNullParent() {
    Node nodeFunction = Node.newNode(Token.FUNCTION, null, null, null, null);
    Node nullParent = null;
    Node nodeFunctionInNullParent = Node.newNode(Token.FUNCTION, null, null, nodeFunction, null);
    NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInNullParent);
  }

  @Test
  public void testIsHoistedFunctionDeclarationNonFunctionParent() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null);
    Node nodeFunction = Node.newNode(Token.FUNCTION, null, null, null, null);
    Node nodeParent = Node.newNode(Token.IF, null, null, null, null);
    Node nodeFunctionInParent = Node.newNode(Token.FUNCTION, null, null, nodeFunction, null);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInParent));
  }
}