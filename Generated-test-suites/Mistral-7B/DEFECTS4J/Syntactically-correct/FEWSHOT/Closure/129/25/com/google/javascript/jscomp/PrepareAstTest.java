package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    Node root = newNode("if (true)");
    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node ifStatement = root.getFirstChild();
    assertTrue(ifStatement.isBlock());

    Node child = ifStatement.getFirstChild();
    assertTrue(child.isIfStatement());
    assertFalse(child.isBlock());
  }

  @Test
  public void testNormalizeBlocks() {
    Node root = newNode("if (true) { }");
    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node ifStatement = root.getFirstChild();
    assertTrue(ifStatement.isBlock());
    Node ifCondition = ifStatement.getFirstChild();
    assertTrue(ifCondition.isIfStatement());
  }

  @Test
  public void testAnnotateCalls() {
    Node root = newNode("eval()");
    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node callNode = root.getFirstChild();
    assertTrue(callNode.isCall());
    assertTrue(callNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testAnnotateDispatchers() {
    Node root = newNode("var obj = {foo: function() {}}; obj.foo();");
    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node assignNode = root.getFirstChild();
    assertTrue(assignNode.isAssign());
    Node functionNode = assignNode.getLastChild();
    assertTrue(functionNode.isFunction());
    assertTrue(functionNode.getBooleanProp(Node.IS_DISPATCHER));
  }

  @Test
  public void testNormalizeObjectLiteralKeyAnnotations() {
    Node root = newNode("var obj = {a: function() {}, b: function() {}};");
    PrepareAst prepareAst = new PrepareAst(null, false);
    prepareAst.process(null, root);

    Node objectLiteralNode = root.getFirstChild();
    Node functionANode = null;
    Node functionBNode = null;

    for (Node child = objectLiteralNode.getFirstChild(); child != null; child = child.getNext()) {
      Node keyNode = child.getFirstChild();
      Node valueNode = keyNode.getNext();

      if (keyNode.getString().equals("a")) {
        functionANode = valueNode;
      } else if (keyNode.getString().equals("b")) {
        functionBNode = valueNode;
      }
    }

    Preconditions.checkState(functionANode != null && functionBNode != null);

    assertTrue(functionANode.isFunction());
    assertTrue(functionBNode.isFunction());
    assertNull(functionANode.getJSDocInfo());
    assertNull(functionBNode.getJSDocInfo());

    functionANode.setJSDocInfo(new JSDocInfo("@function"));
    functionBNode.setJSDocInfo(new JSDocInfo("@function"));

    prepareAst.process(null, root);

    assertTrue(functionANode.getJSDocInfo().equals(functionBNode.getJSDocInfo()));
  }

  private Node newNode(String nodeType) {
    Node root = new Node();
    root.setType(new Token(Token.TOKEN_ROOT, null, 0));
    root.setToken(new Token(Token.TOKEN_ROOT, null, 0));
    Node n = new Node(nodeType);
    root.addChildToEnd(n);
    return n;
  }
}