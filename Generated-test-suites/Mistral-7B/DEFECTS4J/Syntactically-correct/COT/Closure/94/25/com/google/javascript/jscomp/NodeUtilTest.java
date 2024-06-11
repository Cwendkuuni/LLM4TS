import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new Node[]{});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new Node[]{});
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{}, new Node[]{Node.newNode(Token.PRINT, new int[]{}, new Node[]{Node.newText("Hello")})});
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.PRINT, new int[]{}, new Node[]{Node.newText("Hello")});
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{Node.newIdentifier("myFunction")}, Node.newBlock(null));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{Node.newIdentifier("myFunction")}, Node.newExpressionStatement(Node.newIdentifier("myFunction")));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(
        Token.FUNCTION,
        new int[]{},
        new Node[]{Node.newIdentifier("myFunction")},
        Node.newBlock(null),
        Node.newScript(null)
    );
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionInExpression = Node.newNode(
        Token.FUNCTION,
        new int[]{},
        new Node[]{Node.newIdentifier("myFunction")},
        Node.newBlock(null),
        Node.newExpressionStatement(Node.newCall(Node.newIdentifier("myFunction")))
    );
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInExpression));
  }

  @Test(expected = AssertionError.class)
  public void testIsFunctionDeclarationWithNullNode() {
    Node nullNode = null;
    isFunctionDeclaration(nullNode);
  }

  @Test(expected = AssertionError.class)
  public void testIsStatementWithNullNode() {
    Node nullNode = null;
    isStatement(nullNode);
  }
}