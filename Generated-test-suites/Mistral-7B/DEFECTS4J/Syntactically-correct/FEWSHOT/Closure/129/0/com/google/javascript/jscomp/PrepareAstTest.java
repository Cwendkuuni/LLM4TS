package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.jscomp.PrepareAnnotations;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    Node root = NodeFactory.createObject();
    Node exprVoid = NodeFactory.createCall(NodeFactory.createName("foo"), new Node[0]);
    exprVoid.setType(Token.EXPR_VOID);
    root.addChild(exprVoid);

    PrepareAst prepareAst = new PrepareAst(null, false);
    Node normalizedRoot = NodeFactory.createObject();
    prepareAst.process(null, normalizedRoot);
    Node normalizedExprVoid = normalizedRoot.getFirstChild();

    assertThat(normalizedExprVoid.getType()).isEqualTo(Token.EXPR_RESULT);

    Node normalizedExterns = NodeFactory.createObject();
    prepareAst.process(normalizedExterns, root);
    Node normalizedRootWithExterns = NodeFactory.createObject();
    prepareAst.process(normalizedExterns, normalizedRootWithExterns);

    Node normalizedRootAndExterns = normalizedRootWithExterns;
    assertNormalizedNode(normalizedRootAndExterns, root);
  }

  @Test
  public void testNormalizeBlocks() {
    Node root = NodeFactory.createIf(
        NodeFactory.createName("x"),
        NodeFactory.createCall(NodeFactory.createName("print"), new Node[0]),
        NodeFactory.createBlock(new Node[0]));

    PrepareAst prepareAst = new PrepareAst(null, false);
    Node normalizedRoot = NodeFactory.createIf(
        NodeFactory.createName("x"),
        NodeFactory.createBlock(new Node[0]),
        NodeFactory.createBlock(new Node[0]));
    prepareAst.process(null, normalizedRoot);
    assertNormalizedNode(normalizedRoot, root);
  }

  @Test
  public void testPrepareAnnotations() {
    Node root = NodeFactory.createFunction(
        NodeFactory.createName("foo"),
        new Node[0],
        NodeFactory.createBlock(
            NodeFactory.createCall(NodeFactory.createName("alert"), new Node[0]),
            NodeFactory.createReturn(NodeFactory.createNull())
        ));

    Node parent = NodeFactory.createAssign(
        NodeFactory.createName("x"),
        NodeFactory.createName("foo"));

    PrepareAst prepareAst = new PrepareAst(null, false);
    Node normalizedRoot = NodeFactory.createFunction(
        NodeFactory.createName("foo"),
        new Node[0],
        NodeFactory.createBlock(
            NodeFactory.createName("alert"),
            NodeFactory.createReturn(NodeFactory.createNull())
        ));
    prepareAst.process(null, normalizedRoot);

    Node normalizedParent = NodeFactory.createAssign(
        NodeFactory.createName("x"),
        NodeFactory.createName("foo"),
        NodeFactory.createObject().setJSDocInfo(
            NodeFactory.createJSDocInfo("this is a test")));
    prepareAst.process(null, normalizedParent);

    Node normalizedRootWithParent = NodeFactory.createObject().addChild(normalizedParent).addChild(normalizedRoot);
    prepareAst.process(null, normalizedRootWithParent);

    assertNormalizedNode(normalizedRootWithParent, root);
  }

  private void assertNormalizedNode(Node normalizedNode, Node originNode) {
    Node normalizedChild = normalizedNode;
    Node originChild = originNode.getFirstChild();

    while (normalizedChild != null && originChild != null) {
      assertEquals(normalizedChild.getType(), originChild.getType());
      assertNormalizedNode(normalizedChild.getFirstChild(), originChild.getFirstChild());
      normalizedChild = normalizedChild.getNext();
      originChild = originChild.getNext();
    }

    assertEquals(normalizedNode.getNumChildren(), originNode.getNumChildren());
    if (normalizedNode.getLastChild() != null && originNode.getLastChild() != null) {
      assertEquals(normalizedNode.getLastChild().getType(), originNode.getLastChild().getType());
    } else if (normalizedNode.getLastChild() == null && originNode.getLastChild() != null) {
      assertNull(originNode.getLastChild());
    } else if (normalizedNode.getLastChild() != null && originNode.getLastChild() == null) {
      assertNull(normalizedNode.getLastChild());
    } else {
      // empty
    }
  }

  private void assertEquals(int expectedType, int actualType) {
    assertThat(actualType).isEqualTo(expectedType);
  }
}

class NodeFactory {
  static Node createObject() {
    return new Node(Token.OBJ, new Node[0]);
  }

  static Node createObjectLit() {
    return new Node(Token.OBJECT_LITERAL, new Node[0]);
  }

  static Node createFunction(Node name, Node[] params, Node body) {
    return new Node(Token.FUNCTION, params, body);
  }

  static Node createCall(Node callee, Node[] args) {
    return new Node(Token.CALL, callee, args);
  }

  static Node createName(String name) {
    return new Node(Token.NAME, name);
  }

  static Node createBlock(Node[] statements) {
    return new Node(Token.BLOCK, statements);
  }

  static Node createAssign(Node lhs, Node rhs) {
    return new Node(Token.SETPROP, lhs, rhs);
  }

  static Node createReturn(Node value) {
    return new Node(Token.RETURN, value);
  }

  static Node createJSDocInfo(String tag) {
    return new Node(Token.JSDOC_INFO, new Node[0], null, tag);
  }
}