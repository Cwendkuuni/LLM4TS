package com.google.javascript.jscomp.test;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.test.testType.TestPrepareAst;

import org.junit.Test;

public class PrepareAstTest {

  @Test
  public void testNormalizeNodeTypes() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = new TestPrepareAst().getTestNode();
    PrepareAst prepareAst = new PrepareAst(compiler, options.checkOnly());
    prepareAst.process(null, root);

    Node child = root.getFirstChild();
    assertTrue(child.isBlock());

    child = child.getNext();
    assertFalse(child.isBlock());

    child = (Node) child.getFirstChild();
    assertTrue(child.isCall());

    Node callFirst = child.getFirstChild();
    assertTrue(callFirst.isName());
    assertEquals("normalizeNodeTypes", callFirst.getString());

    Node callSecond = callFirst.getNext();
    assertTrue(callSecond.isName());
    assertEquals("PrepareAst", callSecond.getString());
  }

  @Test
  public void testPrepareAnnotations() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = new TestPrepareAst().getTestNode();
    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    Node callNode = findCallNode(root, "annotateCalls");
    assertTrue(callNode.getBooleanProp(Node.FREE_CALL));

    Node evalNode = findCallNode(root, "annotateDispatchers");
    assertTrue(evalNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareAnnotationsWithCheckOnly() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = new TestPrepareAst().getTestNode();
    PrepareAst prepareAst = new PrepareAst(compiler, options.checkOnly(true));
    prepareAst.process(null, root);

    // checkOnly mode should not change the AST
    Node callNode = findCallNode(root, "annotateCalls");
    assertFalse(callNode.getBooleanProp(Node.FREE_CALL));

    Node evalNode = findCallNode(root, "annotateDispatchers");
    assertFalse(evalNode.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareObjectLiteralAnnotations() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    Node root = new TestPrepareAst().getTestNode();
    PrepareAst prepareAst = new PrepareAst(compiler);
    prepareAst.process(null, root);

    Node objlit = findObjectLiteralNode(root);
    Node keyA = objlit.getFirstChild();
    Node keyB = keyA.getNext();
    Node valueA = keyA.getLastChild();
    Node valueB = keyB.getLastChild();

    assertNull(valueA.getJSDocInfo());
    assertNull(valueB.getJSDocInfo());

    String jsdocA = "/* JsDoc */ a: 'b'";
    String jsdocB = "/* JsDoc */ c: 'd'";

    keyA.setJSDocInfo(new Node.JsDocInfo(jsdocA, null));
    keyB.setJSDocInfo(new Node.JsDocInfo(jsdocB, null));

    prepareAst.process(null, root);

    assertEquals(jsdocA, valueA.getJSDocInfo().getCommentText());
    assertEquals(jsdocB, valueB.getJSDocInfo().getCommentText());
  }

  private Node findCallNode(Node node, String name) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isCall() && child.getFirstChild().isName() && child.getFirstChild().getString().equals(name)) {
        return child;
      }
    }
    return null;
  }

  private Node findObjectLiteralNode(Node node) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isObjectLit()) {
        return child;
      }
    }
    return null;
  }
}