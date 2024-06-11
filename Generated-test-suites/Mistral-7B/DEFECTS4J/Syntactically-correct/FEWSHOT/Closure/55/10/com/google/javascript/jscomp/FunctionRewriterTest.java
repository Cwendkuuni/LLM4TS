package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Token;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node root = functionRewriter.parseHelperCode(EmptyFunctionReducer.class);
    assertNull(root);
  }

  @Test
  public void testIdentityReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node helperCode = functionRewriter.parseHelperCode(IdentityReducer.class);
    assertNull(helperCode);
  }

  @Test
  public void testReturnConstantReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node helperCode = functionRewriter.parseHelperCode(ReturnConstantReducer.class);
    assertNotNull(helperCode);
  }

  @Test
  public void testGetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node helperCode = functionRewriter.parseHelperCode(GetterReducer.class);
    assertNotNull(helperCode);
  }

  @Test
  public void testSetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    Node helperCode = functionRewriter.parseHelperCode(SetterReducer.class);
    assertNotNull(helperCode);
  }

  @Test
  public void testProcessFunctionRewriter() {
    String testCode = "function Test() {return this.Test;}";
    Node root = new Node(Token.BLOCK, 1, 1);
    root.addChildrenToBack(new Node(Token.FUNCTION, 1, 1, "Test", null, null,
        null, null, null, null, null, null));
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);
    Node rewrittenCode = root.getLastChild();

    Node expectedHelperCode = functionRewriter.parseHelperCode(IdentityReducer.class);
    Node expectedCallNode = expectedHelperCode.getFirstChild();
    Node expectedStringNode = expectedCallNode.getFirstChild();
    Node expectedFunctionNode = expectedCallNode.getLastChild();

    Node rewrittenHelperCode = rewrittenCode.getLastChild().getLastChild();
    Node rewrittenCallNode = rewrittenHelperCode.getFirstChild();
    Node rewrittenStringNode = rewrittenCallNode.getFirstChild();
    Node rewrittenFunctionNode = rewrittenCallNode.getLastChild();

    assertEquals(expectedStringNode.getString(), rewrittenStringNode.getString());
    assertEquals(expectedFunctionNode.getType(), rewrittenFunctionNode.getType());
  }

  @Test
  public void testProcessGetFunctionRewriter() {
    String testCode = "function Test() {return this.Test;}";
    Node root = new Node(Token.BLOCK, 1, 1);
    root.addChildrenToBack(new Node(Token.FUNCTION, 1, 1, "Test", null, null,
        null, null, null, null, null, null));
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);
    Node rewrittenCode = root.getLastChild();

    Node expectedHelperCode = functionRewriter.parseHelperCode(GetterReducer.class);
    Node expectedStringNode = expectedHelperCode.getFirstChild();
    Node expectedFunctionNode = expectedHelperCode.getLastChild();

    Node rewrittenHelperCode = rewrittenCode.getLastChild().getLastChild();
    Node rewrittenStringNode = rewrittenHelperCode.getFirstChild();
    Node rewrittenFunctionNode = rewrittenHelperCode.getLastChild();

    assertEquals(expectedStringNode.getString(), rewrittenStringNode.getString());
    assertEquals(expectedFunctionNode.getType(), rewrittenFunctionNode.getType());
  }

  @Test
  public void testProcessSetFunctionRewriter() {
    String testCode = "function Test(value) {this.Test = value;}";
    Node root = new Node(Token.BLOCK, 1, 1);
    root.addChildrenToBack(new Node(Token.FUNCTION, 1, 1, "Test", null, null,
        null, null, new Node(Token.NAME, 1, 1, "value"), null, null, null));
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);
    Node rewrittenCode = root.getLastChild();

    Node expectedHelperCode = functionRewriter.parseHelperCode(SetterReducer.class);
    Node expectedStringNode = expectedHelperCode.getFirstChild();
    Node expectedFunctionNode = expectedHelperCode.getLastChild();

    Node rewrittenHelperCode = rewrittenCode.getLastChild().getLastChild();
    Node rewrittenStringNode = rewrittenHelperCode.getFirstChild();
    Node rewrittenFunctionNode = rewrittenHelperCode.getLastChild();

    assertEquals(expectedStringNode.getString(), rewrittenStringNode.getString());
    assertEquals(expectedFunctionNode.getType(), rewrittenFunctionNode.getType());
  }

  @Test
  public void testProcessSetFunctionRewriterWithEmptyFunction() {
    String testCode = "function Test() {}";
    Node root = new Node(Token.BLOCK, 1, 1);
    root.addChildrenToBack(new Node(Token.FUNCTION, 1, 1, "Test", null, null,
        null, null, null, null, null, null));
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);
    Node rewrittenCode = root.getLastChild();

    Node expectedHelperCode = functionRewriter.parseHelperCode(EmptyFunctionReducer.class);
    Node expectedCallNode = expectedHelperCode.getFirstChild();

    Node rewrittenHelperCode = rewrittenCode.getLastChild().getLastChild();
    Node rewrittenCallNode = rewrittenHelperCode.getFirstChild();

    assertSame(expectedCallNode, rewrittenCallNode);
  }

  @Test
  public void testProcessReturnConstantFunctionRewriter() {
    String testCode = "function Test() {return 42;}";
    Node root = new Node(Token.BLOCK, 1, 1);
    root.addChildrenToBack(new Node(Token.FUNCTION, 1, 1, "Test", null, null,
        null, null, null, null, null, null));
    FunctionRewriter functionRewriter = new FunctionRewriter(null);
    functionRewriter.process(null, root);
    Node rewrittenCode = root.getLastChild();

    Node expectedHelperCode = functionRewriter.parseHelperCode(ReturnConstantReducer.class);
    Node expectedConstantNode = expectedHelperCode.getFirstChild();
    Node expectedFunctionNode = expectedHelperCode.getLastChild();

    Node rewrittenHelperCode = rewrittenCode.getLastChild().getLastChild();
    Node rewrittenConstantNode = rewrittenHelperCode.getFirstChild();
    Node rewrittenFunctionNode = rewrittenHelperCode.getLastChild();

    assertEquals(expectedConstantNode.getFirstChild().getInt(),
        rewrittenConstantNode.getFirstChild().getInt());
    assertEquals(expectedFunctionNode.getType(), rewrittenFunctionNode.getType());
  }
}