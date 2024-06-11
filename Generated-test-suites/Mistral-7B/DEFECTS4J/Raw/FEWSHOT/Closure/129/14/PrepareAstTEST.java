package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.Token;

/**
 * Test class for PrepareAst class.
 */
public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    Node root = createNode("function foo() {}");
    PrepareAst prepareAst = new PrepareAst(mock(AbstractCompiler.class));

    prepareAst.process(null, root);

    Node functionNode = root.getFirstChild();
    Node functionBlock = functionNode.getFirstChild();

    assertThat(functionBlock.isBlock()).isTrue();
  }

  @Test
  public void testNormalizeBlocks() {
    Node root = createNode("if (true) {} else {}");
    PrepareAst prepareAst = new PrepareAst(mock(AbstractCompiler.class));

    prepareAst.process(null, root);

    Node ifNode = root.getFirstChild();
    Node ifBlock = ifNode.getFirstChild();
    Node elseBlock = ifBlock.getNextSibling();

    assertThat(ifBlock.isBlock()).isTrue();
    assertThat(elseBlock.isBlock()).isTrue();
  }

  @Test
  public void testPrepareAnnotations() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = createNode("function foo() {}");
    prepareAst.process(null, root);

    Node functionNode = root.getFirstChild();
    Node functionBlock = functionNode.getFirstChild();

    // Check free calls.
    Node callNode = functionBlock.getFirstChild();
    Node callFunctionNode = callNode.getFirstChild();
    Node callIdentifierNode = callFunctionNode.getFirstChild();

    assertThat(callIdentifierNode.getString()).isEqualTo("call");
    assertThat(callNode.getBooleanProp(Node.FREE_CALL)).isTrue();

    // Check direct eval.
    callNode = functionBlock.getLastChild();
    callFunctionNode = callNode.getFirstChild();
    callIdentifierNode = callFunctionNode.getFirstChild();

    assertThat(callIdentifierNode.getString()).isEqualTo("eval");
    assertThat(callNode.getBooleanProp(Node.DIRECT_EVAL)).isTrue();
  }

  @Test
  public void testNormalizeObjectLiteralAnnotations() {
    Node root = createNode("{a: function() {}, b: function() {}}");
    PrepareAst prepareAst = new PrepareAst(mock(AbstractCompiler.class));

    prepareAst.process(null, root);

    Node objectLitNode = root.getFirstChild();
    Node aFunctionNode = objectLitNode.getFirstChild().getNextSibling();
    Node bFunctionNode = objectLitNode.getLastChild().getPreviousSibling();

    // Check JSDoc info on value.
    assertThat(aFunctionNode.getJSDocInfo()).isNotNull();
    assertThat(bFunctionNode.getJSDocInfo()).isNotNull();
  }

  @Test
  public void testAnnotateDispatchers() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PrepareAst prepareAst = new PrepareAst(compiler);

    Node root = createNode("var x = function() {}; x.foo = function() {};");
    prepareAst.process(null, root);

    Node assignNode = root.getFirstChild();
    Node assignValueNode = assignNode.getLastChild();
    Node functionNode = assignValueNode.getFirstChild();
    Node dispatcherNode = functionNode.getLastChild();

    // Check dispatcher node.
    assertThat(dispatcherNode.getBooleanProp(Node.IS_DISPATCHER)).isTrue();
  }

  private Node createNode(String code) {
    return IR.parseNode(code, null, null).getFirstChild();
  }

  private static class AbstractCompiler {}
}