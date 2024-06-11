import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null);
    nodeForIn.setProperty("getType", 0);
    nodeForIn.setProperty("getChildCount", 3);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.PRINT, null);
    Node parentScript = new Node(Token.SCRIPT, null);
    nodeStatement.setParent(parentScript);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.IF, null);
    Node parentBlock = new Node(Token.BLOCK, null);
    nodeNotStatement.setParent(parentBlock);

    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null);
    nodeFunctionDeclaration.setProperty("getType", Token.FUNCTION.getValue());
    Node parentScript = new Node(Token.SCRIPT, null);
    nodeFunctionDeclaration.setParent(parentScript);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, null);
    nodeFunctionExpression.setProperty("getType", Token.FUNCTION.getValue());
    Node parentExpression = new Node(Token.IDENTIFIER, null);
    nodeFunctionExpression.setParent(parentExpression);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, null);
    nodeHoistedFunctionDeclaration.setProperty("getType", Token.FUNCTION.getValue());
    Node parentScript = new Node(Token.SCRIPT, null);
    Node grandparentFunction = new Node(Token.FUNCTION, null);
    nodeHoistedFunctionDeclaration.setParent(parentScript);
    grandparentFunction.setProperty("getType", Token.FUNCTION.getValue());
    parentScript.setParent(grandparentFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationNotHoisted = new Node(Token.FUNCTION, null);
    nodeFunctionDeclarationNotHoisted.setProperty("getType", Token.FUNCTION.getValue());
    Node parentBlock = new Node(Token.BLOCK, null);
    nodeFunctionDeclarationNotHoisted.setParent(parentBlock);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationNotHoisted));
  }

  @Test(expected = AssertionError.class)
  public void testPreconditionsCheckStateFailure() {
    Node node = new Node(Token.SCRIPT, null);
    NodeUtil.isStatement(node);
  }
}