package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

/**
 * Test cases for PrepareAst class.
 */
public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    Node root = createNode(Token.BLOCK, null);
    Node call = createNode(Token.CALL, null);
    Node objlit = createNode(Token.OBJECT_LITERAL, null);

    root.addChildrenToFront(call, objlit);

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    // Check that EXPR_VOID nodes are converted to EXPR_RESULT.
    assertThat(call.getType()).isEqualTo(Token.CALL);
    Node function = call.getFirstChild();
    Node arguments = function.getNextSibling();
    Node result = arguments.getNextSibling();
    assertThat(result.getType()).isEqualTo(Token.EXPR_RESULT);

    // Check that blocks are added to control structures.
    Node ifStatement = createNode(Token.IF, null);
    Node ifCondition = createBlock(Token.IF, ifStatement);
    nodeTraversal(n -> {
      if (n == ifStatement) {
        Preconditions.checkState(ifCondition.isBlock());
      }
      return true;
    }, root);

    // Check that JSDoc info is copied to function values.
    objlit.setJSDocInfo(createJSDocInfo("MyObject"));
    Node key = createNode(Token.IDENT, "a");
    Node value = createNode(Token.FUNCTION, null);
    value.setJSDocInfo(createJSDocInfo("MyFunction"));
    value.addChildrenToFront(createNode(Token.BLOCK, null));
    objlit.addChildrenToFront(key, value);

    prepareAst.process(null, objlit);

    Node valueJSDoc = value.getJSDocInfo();
    Node keyJSDoc = key.getJSDocInfo();

    // Check that JSDoc info is on the value node.
    assertNull(key.getJSDocInfo());
    assertThat(value.getJSDocInfo()).isEqualTo(valueJSDoc);
    assertThat(valueJSDoc.getName()).isEqualTo("MyFunction");
    assertThat(valueJSDoc.getTagName()).isEqualTo("function");

    // Check that JSDoc info is still on the original key node.
    assertThat(keyJSDoc.getName()).isEqualTo("a");
    assertThat(keyJSDoc.getTagName()).isEqualTo("param");
  }

  @Test
  public void testPrepareAnnotations() {
    Node root = createNode(Token.BLOCK, null);
    Node call1 = createNode(Token.CALL, null);
    Node call2 = createNode(Token.CALL, null);
    Node function = createNode(Token.FUNCTION, null);
    Node assign = createNode(Token.ASSIGN, null);

    root.addChildrenToFront(call1, createNode(Token.BLOCK, null), call2, assign);

    Node call1This = createNode(Token.THIS, null);
    Node call1Call = call1.getFirstChild();
    Node call1Arg1 = call1Call.getNextSibling();
    Node call1Arg2 = call1Arg1.getNextSibling();

    Node call2This = createNode(Token.THIS, null);
    Node call2Call = call2.getFirstChild();
    Node call2Arg = call2Call.getNextSibling();

    Node functionDispatcher = createNode(Token.NAME, "dispatch");
    function.addChildrenToFront(functionDispatcher);

    Node assignThis = assign.getFirstChild();
    Node assignValue = assignThis.getNextSibling();
    Node assignValueCall = assignValue.getFirstChild();

    call1Arg1.setJSDocInfo(createJSDocInfo("arg1"));
    call1Arg2.setJSDocInfo(createJSDocInfo("arg2"));
    assignValue.setJSDocInfo(createJSDocInfo("result"));

    root.setJSDocInfo(createJSDocInfo("root"));

    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    // Check that dispatcher info is copied to function.
    Node functionDispatcherJSDoc = functionDispatcher.getJSDocInfo();
    Node assignValueCallJSDoc = assignValueCall.getJSDocInfo();

    // Check that JSDoc info is on the call node.
    assertNull(call1Arg1.getJSDocInfo());
    assertNull(call1Arg2.getJSDocInfo());
    assertThat(call1.getJSDocInfo()).isEqualTo(call1Arg1.getJSDocInfo());

    // Check that JSDoc info is on the function node.
    assertNull(functionDispatcher.getJSDocInfo());
    assertThat(function.getJSDocInfo()).isEqualTo(functionDispatcherJSDoc);

    // Check that JSDoc info is on the call node with the dispatcher.
    assertNull(call2Arg.getJSDocInfo());
    assertThat(call2.getJSDocInfo()).isEqualTo(call2Arg.getJSDocInfo());

    // Check that JSDoc info is on the assign value call node.
    assertNull(assignValueCall.getJSDocInfo());
    assertThat(assignValueCall.getJSDocInfo()).isEqualTo(assignValueJSDoc);

    // Check that JSDoc info is on the assign this node.
    assertNull(assignThis.getJSDocInfo());
    assertNull(assignThis.getJSDocInfo().getTagName());
    assertNull(assignThis.getJSDocInfo().getParamNames());

    // Check that JSDoc info is on the assign value node.
    assertNull(assignValue.getJSDocInfo().getTagName());
    assertThat(assignValue.getJSDocInfo().getParamNames()).containsExactly("result");

    // Check that JSDoc info is on the root node.
    Node rootJSDoc = root.getJSDocInfo();
    assertThat(rootJSDoc.getTagName()).isEqualTo("Object");
    assertThat(rootJSDoc.getDescription()).isEqualTo("root");
  }

  private Node createNode(int type, Node parent) {
    Node node = IR.makeNode(type, null);
    if (parent != null) {
      parent.addChild(node);
    }
    return node;
  }

  private Node createBlock(int type, Node parent) {
    Node node = IR.block().srcref(parent);
    if (parent != null) {
      parent.addChild(node);
    }
    return node;
  }

  private NodeTraversal.Callback createNodeTraversalCallback(
      NodeTraversal.Callback callback) {
    return new NodeTraversal.Callback() {
      @Override
      public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
        return callback.shouldTraverse(t, n, parent);
      }

      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        callback.visit(t, n, parent);
      }
    };
  }

  private void nodeTraversal(NodeTraversal.Callback callback, Node root) {
    NodeTraversal.traverse(root, createNodeTraversalCallback(callback));
  }

  private JSDocInfo createJSDocInfo(String name) {
    JSDocInfo jsdocInfo = new JSDocInfo();
    jsdocInfo.setTagName("Object");
    jsdocInfo.setName(name);
    return jsdocInfo;
  }
}