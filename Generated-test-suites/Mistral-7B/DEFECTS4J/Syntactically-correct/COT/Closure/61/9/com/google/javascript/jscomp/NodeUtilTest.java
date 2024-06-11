import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new Node[3]);
    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new Node[0]);

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{}, new Node[0]);
    Node nodeExpression = Node.newNode(Token.FUNCTION, new int[]{}, new Node[0]);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeExpression));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementWithNullParent() {
    Node node = Node.newNode(Token.IF, new int[]{}, new Node[0]);
    Preconditions.checkState(node.getParent() != null);
    node.setParent(null);
    isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, new Node[0]);
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{}, new Node[0], Node.newNode(Token.ASSIGN, new int[]{}, Node.newNode(Token.IDENT, new int[]{}, "funcName"), nodeFunctionDeclaration));

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationTopLevel = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, new Node[0]);
    Node nodeFunctionDeclarationNested = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, new Node[0]);
    Node nodeFunctionDeclarationNestedInFunction = Node.newNode(
        Token.FUNCTION,
        new int[]{},
        new Node[]{},
        Node.newNode(Token.BLOCK, new int[]{}, new Node[]{}, Node.newNode(Token.VAR, new int[]{}, new Node[]{}, nodeFunctionDeclarationNested))
    );

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationTopLevel));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationNested));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationNestedInFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationWithNullParent() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new Node[]{}, new Node[0]);
    Preconditions.checkState(nodeFunctionDeclaration.getParent() != null);
    nodeFunctionDeclaration.setParent(null);
    isHoistedFunctionDeclaration(nodeFunctionDeclaration);
  }
}