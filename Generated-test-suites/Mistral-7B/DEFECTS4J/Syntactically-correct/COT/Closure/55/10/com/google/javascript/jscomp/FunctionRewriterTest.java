package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new AbstractCompiler() {});
    Node root = new Node(Token.BLOCK, 1, 1);
    root.addChild(new Node(Token.VAR, 1, 1, "a", null, null, null));
    root.addChild(new Node(Token.VAR, 1, 1, "a.prototype.b", null, null, null));
    root.addChild(new Node(Token.FUNCTION, 1, 1, null, new Node[0], null));

    functionRewriter.process(null, root);

    Node expectedHelperCode = new Node(Token.BLOCK, 1, 1,
        new Node(Token.FUNCTION, 1, 1, "JSCompiler_emptyFn", new Node[0], null));

    assertHelperCodeMatches(expectedHelperCode, root);
  }

  @Test
  public void testIdentityReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new AbstractCompiler() {});
    Node root = new Node(Token.BLOCK, 1, 1);
    root.addChild(new Node(Token.VAR, 1, 1, "a", null, null, null));
    root.addChild(new Node(Token.VAR, 1, 1, "a.prototype.b", null, null, null));
    root.addChild(new Node(Token.FUNCTION, 1, 1, "b",
        new Node[]{new Node(Token.VAR, 1, 1, "a", null, null, null)}, null));

    functionRewriter.process(null, root);

    Node expectedHelperCode = new Node(Token.BLOCK, 1, 1,
        new Node(Token.FUNCTION, 1, 1, "JSCompiler_identityFn", new Node[0], null),
        new Node(Token.STRING, 1, 1, "b"));

    assertHelperCodeMatches(expectedHelperCode, root);
  }

  @Test
  public void testGetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new AbstractCompiler() {});
    Node root = new Node(Token.BLOCK, 1, 1);
    root.addChild(new Node(Token.VAR, 1, 1, "a", null, null, null));
    root.addChild(new Node(Token.VAR, 1, 1, "a.prototype.getB", null, null, null));
    root.addChild(new Node(Token.FUNCTION, 1, 1, "getB",
        new Node[]{new Node(Token.VAR, 1, 1, "this", null, null, null)}, null));
    root.addChild(new Node(Token.RETURN, 1, 1, null, new Node[0], null));

    functionRewriter.process(null, root);

    Node expectedHelperCode = new Node(Token.BLOCK, 1, 1,
        new Node(Token.FUNCTION, 1, 1, "JSCompiler_get", new Node[0], null),
        new Node(Token.STRING, 1, 1, "b"));

    assertHelperCodeMatches(expectedHelperCode, root);
  }

  @Test
  public void testSetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new AbstractCompiler() {});
    Node root = new Node(Token.BLOCK, 1, 1);
    root.addChild(new Node(Token.VAR, 1, 1, "a", null, null, null));
    root.addChild(new Node(Token.VAR, 1, 1, "a.prototype.setB", null, null, null));
    root.addChild(new Node(Token.FUNCTION, 1, 1, "setB",
        new Node[]{new Node(Token.VAR, 1, 1, "value", null, null, null)}, null));
    root.addChild(new Node(Token.VAR, 1, 1, "this", null, null, null));
    root.addChild(new Node(Token.ASSIGN, 1, 1, null, new Node[0], null));
    root.addChild(new Node(Token.STRING, 1, 1, "b", null, null, null));
    root.addChild(new Node(Token.RETURN, 1, 1, null, new Node[0], null));

    functionRewriter.process(null, root);

    Node expectedHelperCode = new Node(Token.BLOCK, 1, 1,
        new Node(Token.FUNCTION, 1, 1, "JSCompiler_set", new Node[0], null),
        new Node(Token.STRING, 1, 1, "b"));

    assertHelperCodeMatches(expectedHelperCode, root);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidFunctionReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new AbstractCompiler() {});
    Node root = new Node(Token.BLOCK, 1, 1);
    root.addChild(new Node(Token.VAR, 1, 1, "a", null, null, null));
    root.addChild(new Node(Token.VAR, 1, 1, "a.prototype.b", null, null, null));
    root.addChild(new Node(Token.FUNCTION, 1, 1, "b",
        new Node[]{new Node(Token.VAR, 1, 1, "a", null, null, null)}, null));
    root.addChild(new Node(Token.RETURN, 1, 1, null, new Node[0], null));

    Node invalidHelperCode = new Node(Token.BLOCK, 1, 1,
        new Node(Token.FUNCTION, 1, 1, "JSCompiler_emptyFn", new Node[0], null),
        new Node(Token.STRING, 1, 1, "b"));

    functionRewriter.process(null, root);
    functionRewriter.parseHelperCode(null); // This should throw an exception
  }

  private void assertHelperCodeMatches(Node expectedHelperCode, Node root) {
    Node actualHelperCode = functionRewriter.parseHelperCode(null);
    assertNotNull(actualHelperCode);
    assertEquals(expectedHelperCode, actualHelperCode);
  }
}