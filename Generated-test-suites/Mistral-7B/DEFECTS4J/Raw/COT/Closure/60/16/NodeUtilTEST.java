import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = createNode(TokenType.FOR, 0, new int[] {1, 2, 3});
    assertFalse("Not FOR-IN", NodeUtil.isForIn(node));

    node.setType(Token.IF);
    assertFalse("Not FOR-IN", NodeUtil.isForIn(node));

    node.setType(FOR);
    assertFalse("Invalid FOR-IN", NodeUtil.isForIn(node)
        && node.getChildCount() != 3);

    node.setChild(1, createNode(Token.IDENT, "myVar"));
    node.setChild(2, createNode(Token.LEFT_PAREN, 0));
    node.setChild(3, createNode(Token.RIGHT_PAREN, 0));
    assertTrue("FOR-IN", NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = createNode(Token.SCRIPT, 0, null);
    Node statement = createNode(Token.PRINT, 0, null);
    node.setChild(0, statement);

    assertTrue("Is statement", NodeUtil.isStatement(statement));
    assertTrue("Is statement", NodeUtil.isStatement(node));

    node.setType(VAR);
    assertFalse("Not a statement", NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = createNode(FunctionDeclaration, 0, null);
    assertFalse("Not a function declaration", NodeUtil.isFunctionDeclaration(node));

    node.setChild(0, createNode(Token.FUNCTION, 0, null));
    node.setChild(1, createNode(Token.IDENT, "myFunction", null));
    node.setChild(2, createNode(Token.LEFT_PAREN, 0));
    node.setChild(3, createNode(Token.RIGHT_PAREN, 0));
    node.setChild(4, createNode(Block, 0, null));
    assertTrue("Function declaration", NodeUtil.isFunctionDeclaration(node));

    node.setType(Token.IF);
    assertFalse("Not a function declaration", NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node script = createNode(Token.SCRIPT, 0, null);
    Node functionNode = createNode(FunctionDeclaration, 0, null);
    Node functionDeclaration = functionNode.getFirstChild();

    script.setChild(0, functionDeclaration);
    assertTrue("Hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));

    Node block = createNode(Block, 0, null);
    functionDeclaration.setParent(block);
    assertFalse("Not hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));

    Node function = createNode(FunctionExpression, 0, null);
    function.setChild(0, createNode(Token.FUNCTION, 0, null));
    function.setChild(1, createNode(Token.IDENT, "myFunction", null));
    function.setChild(2, createNode(Token.LEFT_PAREN, 0));
    function.setChild(3, createNode(Token.RIGHT_PAREN, 0));
    function.setChild(4, block);
    assertFalse("Not hoisted function declaration", NodeUtil.isHoistedFunctionDeclaration(function));
  }
}