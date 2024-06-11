import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createToken;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createScript;
import static com.google.javascript.rhino.NodeFactory.createBlock;
import static com.google.javascript.rhino.NodeFactory.createLabel;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

/**
 * Test cases for NodeUtil class.
 */
public class NodeUtilTest {

  /**
   * Test case for NodeUtil.isForIn method.
   */
  @Test
  public void testIsForIn() {
    Node node = createToken(Token.FOR, "for", null);
    Node child1 = createToken(Token.IDENT, "var", null);
    Node child2 = createToken(Token.ASSIGN, ":=", null);
    Node child3 = createToken(Token.IDENT, "i", null);
    Node nodeWithThreeChildren = NodeUtil.createNodeWithChildren(node, child1, child2, child3);
    assertTrue(NodeUtil.isForIn(nodeWithThreeChildren));
  }

  /**
   * Test case for NodeUtil.isForIn method with a node that does not represent a FOR-IN loop.
   */
  @Test
  public void testIsForInFalse() {
    Node node = createToken(Token.FUNCTION, "function", null);
    assertFalse(NodeUtil.isForIn(node));
  }

  /**
   * Test case for NodeUtil.isStatement method.
   */
  @Test
  public void testIsStatement() {
    Node node = createToken(Token.FUNCTION, "function", null);
    Node parentScript = createScript();
    Node parentBlock = createBlock();
    Node parentLabel = createLabel("label");

    parentScript.addChildToEnd(node);
    parentBlock.addChildToEnd(node);
    parentLabel.addChildToEnd(node);

    assertTrue(NodeUtil.isStatement(node));
    assertTrue(NodeUtil.isStatement(node, parentScript));
    assertTrue(NodeUtil.isStatement(node, parentBlock));
    assertFalse(NodeUtil.isStatement(node, parentLabel));
  }

  /**
   * Test case for NodeUtil.isFunctionDeclaration method.
   */
  @Test
  public void testIsFunctionDeclaration() {
    Node node = createFunction("function foo() {}");
    assertTrue(NodeUtil.isFunctionDeclaration(node));
  }

  /**
   * Test case for NodeUtil.isFunctionDeclaration method with a node that is not a function declaration.
   */
  @Test
  public void testIsFunctionDeclarationFalse() {
    Node node = createToken(Token.VAR, "var", null);
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  /**
   * Test case for NodeUtil.isHoistedFunctionDeclaration method.
   */
  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node functionDeclaration = createFunction("function foo() {}");
    Node scriptNode = createScript();
    Node functionNode = createFunction("function bar() {}");

    scriptNode.addChildToEnd(functionDeclaration);
    scriptNode.addChildToEnd(functionNode);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }

  /**
   * Test case for NodeUtil.isHoistedFunctionDeclaration method with a null parent node.
   */
  @Test(expected = NullPointerException.class)
  public void testIsHoistedFunctionDeclarationWithNullParent() {
    Node functionDeclaration = createFunction("function foo() {}");
    NodeUtil.isHoistedFunctionDeclaration(functionDeclaration, null);
  }
}