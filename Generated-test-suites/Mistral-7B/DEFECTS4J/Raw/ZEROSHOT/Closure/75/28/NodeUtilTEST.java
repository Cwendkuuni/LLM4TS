import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR);
    Node nodeForInWithThreeChildren = new Node(Token.FOR);
    nodeForInWithThreeChildren.setChild(0, new Node(Token.IDENTIFIER, "a"));
    nodeForInWithThreeChildren.setChild(1, new Node(Token.LEFT_PAREN));
    nodeForInWithThreeChildren.setChild(2, new Node(Token.IDENTIFIER, "enum"));
    nodeForInWithThreeChildren.setChild(3, new Node(Token.RIGHT_PAREN));
    nodeForInWithThreeChildren.setChild(4, new Node(Token.BLOCK, null));

    assertTrue(isForIn(nodeForInWithThreeChildren));
    assertFalse(isForIn(new Node(Token.IF, null)));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, null);
    Node nodeExpression = new Node(Token.FUNCTION, null);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null);
    nodeFunctionDeclaration.setChild(0, new Node(Token.IDENTIFIER, "myFunction"));
    nodeFunctionDeclaration.setChild(1, new Node(Token.LEFT_PAREN, null));
    nodeFunctionDeclaration.setChild(2, new Node(Token.RIGHT_PAREN, null));
    nodeFunctionDeclaration.setChild(3, new Node(Token.BLOCK, null));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, null);
    nodeFunctionExpression.setChild(0, new Node(Token.IDENTIFIER, "myFunction"));
    Node arg = new Node(Token.IDENTIFIER, "arg");
    nodeFunctionExpression.setChild(1, arg);
    nodeFunctionExpression.setChild(2, new Node(Token.BLOCK, null));

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, null);
    nodeFunctionDeclaration.setChild(0, new Node(Token.IDENTIFIER, "myFunction"));
    nodeFunctionDeclaration.setChild(1, new Node(Token.LEFT_PAREN, null));
    nodeFunctionDeclaration.setChild(2, new Node(Token.RIGHT_PAREN, null));
    nodeFunctionDeclaration.setChild(3, new Node(Token.BLOCK, null));

    Node scriptNode = new Node(Token.SCRIPT, null);
    scriptNode.setChild(0, nodeFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node functionNode = new Node(Token.FUNCTION, null);
    functionNode.setChild(0, new Node(Token.IDENTIFIER, "myFunction"));
    functionNode.setChild(1, new Node(Token.LEFT_PAREN, null));
    functionNode.setChild(2, new Node(Token.RIGHT_PAREN, null));
    functionNode.setChild(3, new Node(Token.BLOCK, null));

    Node blockNode = new Node(Token.BLOCK, null);
    blockNode.setChild(0, functionNode);

    Node nodeFunctionExpressionInBlock = new Node(Token.FUNCTION, null);
    nodeFunctionExpressionInBlock.setChild(0, new Node(Token.IDENTIFIER, "myFunction"));
    nodeFunctionExpressionInBlock.setChild(1, new Node(Token.LEFT_PAREN, null));
    nodeFunctionExpressionInBlock.setChild(2, new Node(Token.RIGHT_PAREN, null));
    nodeFunctionExpressionInBlock.setChild(3, new Node(Token.BLOCK, null));

    Node nodeFunctionInScript = new Node(Token.FUNCTION, null);
    nodeFunctionInScript.setChild(0, new Node(Token.IDENTIFIER, "myFunction"));
    nodeFunctionInScript.setChild(1, new Node(Token.LEFT_PAREN, null));
    nodeFunctionInScript.setChild(2, new Node(Token.RIGHT_PAREN, null));
    nodeFunctionInScript.setChild(3, new Node(Token.BLOCK, null));

    scriptNode.setChild(0, nodeFunctionInScript);
    blockNode.setChild(0, nodeFunctionExpressionInBlock);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionExpressionInBlock));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInScript));
  }
}