import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{0, 0, 1}, null);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{0, 0}, null);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{0, 0}, null);
    Node parentScript = new Node(Token.SCRIPT, new int[]{0, 0}, null);
    parentScript.addChildToEnd(nodeStatement);

    assertTrue(isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.FOR, new int[]{0, 0, 1}, null);
    Node parentExpression = new Node(Token.MUL, new int[]{0, 0}, null);
    parentExpression.addChildToEnd(nodeNotStatement);

    assertFalse(isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(
        Token.FUNCTION, new int[]{1, 0}, new String[]{"foo"});
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(
        Token.FUNCTION, new int[]{0, 0}, new String[]{""});
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(
        Token.FUNCTION, new int[]{1, 0}, new String[]{"foo"});
    Node parentScript = new Node(Token.SCRIPT, new int[]{0, 0}, null);
    parentScript.addChildToEnd(nodeHoistedFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = new Node(
        Token.FUNCTION, new int[]{1, 0}, new String[]{"foo"});
    Node parentFunction = new Node(Token.FUNCTION, new int[]{0, 0}, null);
    parentFunction.addChildToEnd(nodeFunctionDeclarationInFunction);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationWithNullNode() {
    Node node = null;
    isFunctionDeclaration(node);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullNode() {
    Node node = null;
    isStatement(node);
  }
}