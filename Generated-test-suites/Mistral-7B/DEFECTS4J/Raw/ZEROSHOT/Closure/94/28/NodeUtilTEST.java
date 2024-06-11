import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;
import org.mockito.Mockito;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Mockito.mock(Node.class);
    when(nodeForIn.getType()).thenReturn(Token.FOR);
    when(nodeForIn.getChildCount()).thenReturn(3);

    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Mockito.mock(Node.class);
    when(nodeNotForIn.getType()).thenReturn(Token.VAR);

    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Mockito.mock(Node.class);
    when(nodeScript.getType()).thenReturn(Token.SCRIPT);

    assertTrue(isStatement(nodeScript));

    Node nodeFunction = Mockito.mock(Node.class);
    when(nodeFunction.getType()).thenReturn(Token.FUNCTION);

    Node parentScript = Mockito.mock(Node.class);
    when(parentScript.getType()).thenReturn(Token.SCRIPT);
    when(nodeFunction.getParent()).thenReturn(parentScript);

    assertTrue(isStatement(nodeFunction));

    Node nodeBlock = Mockito.mock(Node.class);
    when(nodeBlock.getType()).thenReturn(Token.BLOCK);

    assertTrue(isStatement(nodeBlock));

    Node nodeLabel = Mockito.mock(Node.class);
    when(nodeLabel.getType()).thenReturn(Token.LABEL);

    assertTrue(isStatement(nodeLabel));

    Node nodeExpression = Mockito.mock(Node.class);
    when(nodeExpression.getType()).thenReturn(Token.NUMBER_LITERAL);

    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Mockito.mock(Node.class);
    when(nodeFunctionDeclaration.getType()).thenReturn(Token.FUNCTION);
    when(isStatement(nodeFunctionDeclaration)).thenReturn(true);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Mockito.mock(Node.class);
    when(nodeFunctionExpression.getType()).thenReturn(Token.FUNCTION);
    when(isStatement(nodeFunctionExpression)).thenReturn(false);

    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Mockito.mock(Node.class);
    when(nodeHoistedFunctionDeclaration.getType()).thenReturn(Token.FUNCTION);
    when(isStatement(nodeHoistedFunctionDeclaration)).thenReturn(true);

    Node parentScript = Mockito.mock(Node.class);
    when(parentScript.getType()).thenReturn(Token.SCRIPT);
    when(nodeHoistedFunctionDeclaration.getParent()).thenReturn(parentScript);

    Node grandParentFunction = Mockito.mock(Node.class);
    when(parentScript.getParent()).thenReturn(grandParentFunction);
    when(grandParentFunction.getType()).thenReturn(Token.FUNCTION);

    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInBlock = Mockito.mock(Node.class);
    when(nodeFunctionDeclarationInBlock.getType()).thenReturn(Token.FUNCTION);
    when(isStatement(nodeFunctionDeclarationInBlock)).thenReturn(true);

    Node parentBlock = Mockito.mock(Node.class);
    when(parentBlock.getType()).thenReturn(Token.BLOCK);
    when(nodeFunctionDeclarationInBlock.getParent()).thenReturn(parentBlock);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
  }

  private static Node mockNode(int type) {
    Node node = Mockito.mock(Node.class);
    when(node.getType()).thenReturn(type);
    return node;
  }

  private static void when(Node node, int type) {
    when(node.getType()).thenReturn(type);
  }

  private static void when(Node node, int childCount) {
    when(node.getChildCount()).thenReturn(childCount);
  }

  private static void when(Node parent, Node child) {
    when(child.getParent()).thenReturn(parent);
  }
}