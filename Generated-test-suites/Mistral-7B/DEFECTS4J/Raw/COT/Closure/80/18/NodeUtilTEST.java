import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    nodeForIn.setChild(0, new Node(Token.IDENT, "for"));
    nodeForIn.setChild(1, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "i"), new Node(Token.NUM, 0)));
    nodeForIn.setChild(2, new Node(Token.LPAREN, null, new Node(Token.IDENT, "array"), Token.RPAREN));
    nodeForIn.setChild(3, new Node(Token.COLON, null));
    nodeForIn.setChild(4, new Node(Token.BLOCK, null));

    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF);
    nodeStatement.setChild(0, new Node(Token.IDENT, "condition"));
    nodeStatement.setChild(1, new Node(Token.LPAREN, null));
    nodeStatement.setChild(2, new Node(Token.BLOCK, null));
    nodeStatement.setChild(3, new Node(Token.RPAREN, null));

    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.NUM, 0);

    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new Node(Token.IDENT, "funcName"));
    nodeFunctionDeclaration.setChild(1, new Node(Token.LPAREN, null));
    nodeFunctionDeclaration.setChild(2, new Node(Token.RPAREN, null));
    nodeFunctionDeclaration.setChild(3, new Node(Token.BLOCK, null));

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeNotFunctionDeclaration = new Node(Token.ASSIGN, null, new Node(Token.IDENT, "x"), new Node(Token.NUM, 5));

    assertFalse(isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new Node(Token.IDENT, "funcName"));
    nodeFunctionDeclaration.setChild(1, new Node(Token.LPAREN, null));
    nodeFunctionDeclaration.setChild(2, new Node(Token.RPAREN, null));
    nodeFunctionDeclaration.setChild(3, new Node(Token.BLOCK, null));

    nodeScript.setChild(0, nodeFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionInBlock = new Node(Token.FUNCTION, new Node(Token.IDENT, "funcName"));
    nodeFunctionInBlock.setChild(1, new Node(Token.LPAREN, null));
    nodeFunctionInBlock.setChild(2, new Node(Token.RPAREN, null));
    nodeFunctionInBlock.setChild(3, new Node(Token.BLOCK, null));
    Node nodeBlock = new Node(Token.BLOCK, null);
    nodeBlock.setChild(0, nodeFunctionInBlock);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInBlock));
  }

  @Test(expected = AssertionError.class)
  public void testNullParentNodeInIsStatement() {
    Node node = new Node(Token.NUM, 0);
    Preconditions.checkState(node.getParent() != null);

    isStatement(node);
  }

  @Test(expected = AssertionError.class)
  public void testNegativeConditionInIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new Node(Token.IDENT, "funcName"));
    nodeFunctionDeclaration.setChild(1, new Node(Token.LPAREN, null));
    nodeFunctionDeclaration.setChild(2, new Node(Token.MINUS, null, new Node(Token.NUM, 5), null));
    nodeFunctionDeclaration.setChild(3, new Node(
        Token.BLOCK, null));

    isFunctionDeclaration(nodeFunctionDeclaration);
  }
}