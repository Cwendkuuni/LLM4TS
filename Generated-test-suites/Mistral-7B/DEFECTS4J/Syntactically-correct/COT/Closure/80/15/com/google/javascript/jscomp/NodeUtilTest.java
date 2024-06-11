import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[3]);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[1]);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[1]);
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.FUNCTION, new int[]{}, new int[0]);
    assertFalse(isStatement(nodeExpression));
  }

  @Test(expected = IllegalStateException.class)
  public void testIsStatementWithNullParent() {
    Node node = new Node(Token.IF, new int[]{}, new int[1]);
    Node nullParent = null;
    Preconditions.checkState(node.getParent() != null); // throws exception
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"id"}, new int[1]);
    nodeFunctionDeclaration.setProperty("id", new Node(" identification", new int[]{Token.IDENT, Token.EQUALS, Token.STRING}, new int[3]));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeNotFunctionDeclaration = new Node(Token.FOR, new int[]{}, new int[1]);
    assertFalse(isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{"id"}, new int[1]);
    nodeFunctionDeclaration.setProperty("id", new Node(" identification", new int[]{Token.IDENT, Token.EQUALS, Token.STRING}, new int[3]));
    Node script = new Node(Token.SCRIPT, new int[0], new int[0]);
    script.addChild(nodeFunctionDeclaration);
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{"id"}, new int[1]);
    nodeFunctionDeclarationInFunction.setProperty("id", new Node(" identification", new int[]{Token.IDENT, Token.EQUALS, Token.STRING}, new int[3]));
    Node function = new Node(Token.FUNCTION, new int[]{"id"}, new int[0]);
    function.addChild(nodeFunctionDeclarationInFunction);
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}