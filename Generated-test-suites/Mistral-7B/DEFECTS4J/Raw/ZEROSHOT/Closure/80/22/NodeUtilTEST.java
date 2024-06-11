import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createToken;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeFor = createNode(Token.FOR, null);
    Node nodeForWithThreeChildren = createNode(Token.FOR, null, createNode(Token.IDENT, "i"), createNode(Token.ASSIGN, null, createNode(Token.IDENT, "a")), createNode(Token.SEMICOLON, null));

    assertTrue(NodeUtil.isForIn(nodeForWithThreeChildren));
    assertFalse(NodeUtil.isForIn(nodeFor));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = createNode(Token.SCRIPT, null);
    Node nodeBlock = createNode(Token.BLOCK, null);
    Node nodeLabel = createNode(Token.LABEL, null, createNode(Token.IDENT, "myLabel"));
    Node nodeFunction = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "myFunction"), createNode(Token.LEFT_PAREN, null), createNode(Token.RIGHT_PAREN, null), createNode(Token.LEFT_BRACE, null), createNode(Token.RETURN, null, createNode(Token.NUMBER, 42.0)), createNode(Token.RIGHT_BRACE, null));
    Node nodeFunctionExpression = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "myFunction"), createNode(Token.LEFT_PAREN, null), createNode(Token.RIGHT_PAREN, null), null);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeFunctionExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "myFunction"), createNode(Token.LEFT_PAREN, null), createNode(Token.RIGHT_PAREN, null), createNode(Token.LEFT_BRACE, null), createNode(Token.RETURN, null, createNode(Token.NUMBER, 42.0)), createNode(Token.RIGHT_BRACE, null));
    Node nodeFunctionExpression = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "myFunction"), createNode(Token.LEFT_PAREN, null), createNode(Token.RIGHT_PAREN, null), null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScriptWithFunction = createNode(Token.SCRIPT, null, createNode(Token.FUNCTION, null, createNode(Token.IDENT, "myFunction"), createNode(Token.LEFT_PAREN, null), createNode(Token.RIGHT_PAREN, null), createNode(Token.LEFT_BRACE, null), createNode(Token.RETURN, null, createNode(Token.NUMBER, 42.0)), createNode(Token.RIGHT_BRACE, null)));
    Node nodeFunctionInFunction = createNode(Token.FUNCTION, null, createNode(Token.IDENT, "myFunction"), createNode(Token.LEFT_PAREN, null), createNode(Token.RIGHT_PAREN, null), null, createNode(nodeScriptWithFunction, null));

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeScriptWithFunction));
  }
}