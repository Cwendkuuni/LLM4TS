import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.NodeFactory.makeNode;

import org.junit.Test;

import com.google.common.base.Preconditions;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = makeNode(createToken(Token.FOR, 0, 0), makeNode(createToken(Token.IDENT, "for"), 1), makeNode(createToken(Token.ASSIGN, 2), 2), makeNode(createToken(Token.IDENT, "in"), 3), makeNode(createToken(Token.IDENT, "array"), 4), null);
    assertTrue(NodeUtil.isForIn(node));

    Node nodeWithoutThreeChildren = makeNode(createToken(Token.FOR, 0, 0), makeNode(createToken(Token.IDENT, "for")), null);
    assertFalse(NodeUtil.isForIn(nodeWithoutThreeChildren));
  }

  @Test
  public void testIsStatement() {
    Node node = makeNode(createToken(Token.FUNCTION, 0, 0), makeNode(createToken(Token.IDENT, "func"), 1), null, null, null);
    assertFalse(NodeUtil.isStatement(node));

    Node nodeInScript = makeNode(createToken(Token.SCRIPT, 0, 0), makeNode(createToken(Token.IDENT, "func"), 1), null, null, null);
    assertTrue(NodeUtil.isStatement(nodeInScript));
  }

  @Test(expected = AssertionError.class)
  public void testIsStatementWithNullParent() {
    Node node = makeNode(createToken(Token.SCRIPT, 0, 0), null, null);
    NodeUtil.isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeInScript = makeNode(createToken(Token.FUNCTION, 0, 0), makeNode(createToken(Token.IDENT, "func"), 1), null, null, null);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeInScript));

    Node nodeInFunction = makeNode(createToken(Token.FUNCTION, 0, 0), makeNode(createToken(Token.IDENT, "func"), 1), null, null, null, makeNode(createToken(Token.BLOCK, 0, 0), null, null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeInFunction));

    Node node = makeNode(createToken(Token.FUNCTION, 0, 0), makeNode(createToken(Token.IDENT, "func"), 1), null, null, null, makeNode(createToken(Token.BLOCK, 0, 0), null, null), null);
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeInScript = makeNode(createToken(Token.FUNCTION, 0, 0), makeNode(createToken(Token.IDENT, "func"), 1), null, null, null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeInScript));

    Node nodeInFunction = makeNode(createToken(Token.FUNCTION, 0, 0), makeNode(createToken(Token.IDENT, "func"), 1), null, null, null, makeNode(createToken(Token.BLOCK, 0, 0), null, null), null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeInFunction));

    Node nodeInRoot = makeNode(createToken(Token.SCRIPT, 0, 0), makeNode(createToken(Token.IDENT, "func"), 1), null, null, null);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeInRoot));

    Node nodeInRootWithFunctionParent = makeNode(createToken(Token.FUNCTION, 0, 0), makeNode(createToken(Token.IDENT, "parentFunc"), 1), makeNode(createToken(Token.BLOCK, 0, 0), makeNode(createToken(Token.IDENT, "func"), 1), null, null, null), null);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeInRootWithFunctionParent));
  }
}