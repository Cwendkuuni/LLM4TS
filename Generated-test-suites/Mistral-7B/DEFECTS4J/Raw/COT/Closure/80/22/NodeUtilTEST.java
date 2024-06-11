import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 0, 0});
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    assertFalse(isStatement(nodeExpression));
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckStateParentIsNull() {
    Node node = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    Preconditions.checkState(node.getParent() != null);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0, 1});
    nodeFunctionDeclaration.setProperty("id", "testFunction");

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    nodeFunctionExpression.setProperty("expression", "function testFunction() {}");

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0, 1});
    nodeHoistedFunctionDeclaration.setProperty("id", "testFunction");
    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    scriptNode.setChildren(new Node[]{nodeHoistedFunctionDeclaration});

    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationWithoutParent = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0, 1});
    nodeFunctionDeclarationWithoutParent.setProperty("id", "testFunction");

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationWithoutParent));

    Node nodeFunctionDeclarationAsChild = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0, 1});
    nodeFunctionDeclarationAsChild.setProperty("id", "testFunction");
    Node functionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{0, 0, 0});
    functionNode.setChildren(new Node[]{nodeFunctionDeclarationAsChild});

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationAsChild));
  }
}