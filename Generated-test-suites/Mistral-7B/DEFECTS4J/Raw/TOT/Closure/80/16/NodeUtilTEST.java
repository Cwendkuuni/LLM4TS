import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import org.junit.Test;

/**
 * Test cases for NodeUtil class.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new int[]{0, 1, 2});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new int[]{0, 1, 2});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{}, new int[]{0, 1, 2});
    Node parentScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{});
    parentScript.addChild(nodeStatement);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.IF, new int[]{}, new int[]{0, 1});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{"name"}, new int[]{1, 2});
    nodeFunctionDeclaration.setProperty("name", "testFunction");

    Node parentScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{});
    parentScript.addChild(nodeFunctionDeclaration);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{1, 2});
    Node parentExpression = Node.newNode(Token.ASSIGN, new int[]{"x"}, new int[]{0, 1});
    parentExpression.addChild(nodeFunctionExpression);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{"name"}, new int[]{1, 2});
    nodeFunctionDeclaration.setProperty("name", "testFunction");

    Node parentScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{});
    parentScript.addChild(nodeFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node parentFunction = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{});
    parentFunction.addChild(nodeFunctionDeclaration);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsFunctionDeclarationWithEmptyName() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{1, 2});
    Node parentScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{});
    parentScript.addChild(nodeFunctionDeclaration);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsFunctionDeclarationWithMissingName() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{1, 2});
    Node parentScript = Node.newNode(Token.SCRIPT, new int[]{}, new int[]{});
    parentScript.addChild(nodeFunctionDeclaration);
    assertNull(nodeFunctionDeclaration.getProperty("name"));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsFunctionDeclarationWithNullParent() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{"name"}, new int[]{1, 2});
    nodeFunctionDeclaration.setProperty("name", "testFunction");
    assertNull(nodeFunctionDeclaration.getParent());
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsFunctionDeclarationWithNonStatementParent() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{"name"}, new int[]{1, 2});
    nodeFunctionDeclaration.setProperty("name", "testFunction");

    Node parentExpression = Node.newNode(Token.ASSIGN, new int[]{"x"}, new int[]{0, 1});
    parentExpression.addChild(nodeFunctionDeclaration);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test
  public void testIsFunctionDeclarationWithFunctionParent() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{"name"}, new int[]{1, 2});
    nodeFunctionDeclaration.setProperty("name", "testFunction");

    Node parentFunction = Node.newNode(Token.FUNCTION, new int[]{}, new int[]{});
    parentFunction.addChild(nodeFunctionDeclaration);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }
}