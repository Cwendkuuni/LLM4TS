import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    Node nodeForInWithThreeChildren = new Node(Token.FOR);
    nodeForInWithThreeChildren.setChildCount(3);

    assertTrue(isForIn(nodeForInWithThreeChildren));
    assertFalse(isForIn(nodeForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT);
    Node nodeBlock = new Node(Token.BLOCK);
    Node nodeLabel = new Node(Token.LABEL);
    Node nodeFunction = new Node(Token.FUNCTION);
    Node nodeStatement = new Node(Token.PRINT);
    Node nodeExpression = new Node(Token.ASSIGN);

    Preconditions.checkState(nodeScript != null);
    Preconditions.checkState(nodeBlock != null);
    Preconditions.checkState(nodeLabel != null);
    Preconditions.checkState(nodeFunction != null);
    Preconditions.checkState(nodeStatement != null);
    Preconditions.checkState(nodeExpression != null);

    assertTrue(isStatement(nodeScript));
    assertTrue(isStatement(nodeBlock));
    assertTrue(isStatement(nodeLabel));
    assertTrue(isStatement(nodeStatement));

    assertFalse(isStatement(nodeFunction));
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION);
    Node nodeFunctionExpression = new Node(Token.FUNCTION);

    Preconditions.checkState(nodeFunctionDeclaration != null);
    Preconditions.checkState(nodeFunctionExpression != null);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInRoot = new Node(Token.FUNCTION);
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION);

    Preconditions.checkState(nodeFunctionDeclarationInRoot != null);
    Preconditions.checkState(nodeFunctionDeclarationInFunction != null);

    Node nodeScript = new Node(Token.SCRIPT);
    Node nodeFunction = new Node(Token.FUNCTION);

    Preconditions.checkState(nodeScript != null);
    Preconditions.checkState(nodeFunction != null);

    nodeFunctionDeclarationInRoot.setParent(nodeScript);
    nodeFunctionDeclarationInFunction.setParent(nodeFunction);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclarationInRoot));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = AssertionError.class)
  public void testParentIsNull() {
    Node node = new Node(Token.FUNCTION);

    Preconditions.checkState(node != null);

    Preconditions.checkState(node.getParent() == null);

    isStatement(node);
  }

}