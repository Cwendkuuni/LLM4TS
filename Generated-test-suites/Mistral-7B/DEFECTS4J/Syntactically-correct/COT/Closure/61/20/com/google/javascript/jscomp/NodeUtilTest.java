import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    nodeForIn.setChild(0, new Node(Token.IDENT, "for"));
    nodeForIn.setChild(1, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "i"), new Node(Token.NUM, 0)));
    nodeForIn.setChild(2, new Node(Token.IN, null, new Node(Token.IDENT, "array")));

    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.FOR);
    nodeNotForIn.setChild(0, new Node(Token.IDENT, "for"));
    nodeNotForIn.setChild(1, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "i"), new Node(Token.NUM, 1)));
    nodeNotForIn.setChild(2, new Node(Token.IF, null, new Node(Token.IDENT, "condition")));

    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, null, new Node(Token.NUM, 0), new Node(Token.BLOCK, null));
    Node nodeNotStatement = new Node(Token.NUM, 0);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeNotStatement));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailure() {
    Node node = new Node(Token.SCRIPT);
    Preconditions.checkState(node.getParent() == null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null, new Node(Token.IDENT, "functionName"));
    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, null, new Node(Token.BLOCK, null));
    Node nodeNotFunction = new Node(Token.NUM, 0);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(isFunctionDeclaration(nodeNotFunction));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null, new Node(Token.IDENT, "functionName"));
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, null, null, new Node(nodeFunctionDeclaration, null));

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}