import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null, 0, 0);
    Node nodeNotForIn = new Node(Token.SCRIPT, null, 0, 0);

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, null, 0, 0);
    Node nodeExpression = new Node(Token.FUNCTION, null, 0, 0);
    Node nodeScript = new Node(Token.SCRIPT, null, 0, 0);

    Node parentStatement = new Node(Token.BLOCK, null, 0, 0);
    parentStatement.addChild(nodeStatement);

    Node parentExpression = new Node(Token.ASSIGN, null, 0, 0);
    parentExpression.addChild(nodeExpression);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeExpression));
    assertTrue(isStatement(parentStatement));
    assertFalse(isStatement(nodeScript));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null, 0, 0);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, null, 0, 0);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, null, 0, 0);
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null, 0, 0);
    Node nodeScript = new Node(Token.SCRIPT, null, 0, 0);
    Node nodeFunction = new Node(Token.FUNCTION, null, 0, 0);

    nodeFunctionDeclaration.addChild(nodeScript);
    nodeScript.addChild(nodeFunctionDeclaration);
    nodeScript.addChild(nodeFunction);

    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeFunction));
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckState() {
    Node node = new Node(Token.SCRIPT, null, 0, 0);
    Preconditions.checkState(node.getParent() != null);
  }
}