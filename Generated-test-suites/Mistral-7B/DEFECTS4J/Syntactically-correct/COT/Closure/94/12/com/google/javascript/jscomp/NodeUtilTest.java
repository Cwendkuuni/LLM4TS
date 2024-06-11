import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createToken;

import com.google.common.base.Preconditions;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = createNodeForTesting(Token.FOR, 0, null, null, null, null);
    assertTrue(NodeUtil.isForIn(node));

    node = createNodeForTesting(Token.IF, 0, null, null, null, null);
    assertFalse(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsStatement() {
    Node node = createNodeForTesting(Token.FUNCTION, 0, null, null, null, null);
    assertFalse(NodeUtil.isStatement(node));

    node = createNodeForTesting(Token.SCRIPT, 0, null, null, null, null);
    assertTrue(NodeUtil.isStatement(node));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node node = createNodeForFunctionDeclaration("function foo() {}");
    assertTrue(NodeUtil.isFunctionDeclaration(node));

    node = createNodeForFunctionDeclaration("var foo = function() {}");
    assertFalse(NodeUtil.isFunctionDeclaration(node));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node scriptNode = createNodeForTesting(Token.SCRIPT, 0, null, null, null, null);
    Node functionNode1 = createNodeForFunctionDeclaration("function foo() {}");
    Node functionNode2 = createNodeForFunctionDeclaration("function bar() {}");

    scriptNode.addChild(functionNode1);
    scriptNode.addChild(functionNode2);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(functionNode1));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode2));
  }

  private Node createNodeForTesting(int type, int lineNumber, Node... children) {
    Node parent = createToken(Token.SCRIPT, 0, null);
    Node node = NodeUtil.createNode(type, lineNumber, children, parent);
    parent.addChild(node);
    return node;
  }

  private Node createNodeForFunctionDeclaration(String jsCode) {
    Node scriptNode = createNodeForTesting(Token.SCRIPT, 0, null);
    Node node = NodeUtil.parseJsCodeToAbstractSyntaxTree(jsCode, null, scriptNode);
    return node;
  }
}