package org.apache.commons.cli.inlineObjectLiterals; // assuming a package declaration has been added for testing purposes

import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeUtil;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass inlineObjectLiterals = new InlineObjectLiterals(compiler, Node::newString);
    Node root = new Node(Token.SCRIPTLET, null);
    inlineObjectLiterals.process(null, root);

    assertInlineObjectLiteral(root, "var obj = {a: 1, b: 2};", "var a = 1, b = 2;");
    assertInlineObjectLiteral(root, "function foo() { var obj = {a: 1, b: 2}; }",
        "function foo() { var a = 1, b = 2; }");
    assertInlineObjectLiteral(root, "function foo() { var obj = {a: 1, b: {c: 3}}; }",
        "function foo() { var a = 1, c = 3, b = c; }");
    assertInlineObjectLiteral(root, "function foo() { var obj = {a: [1, 2]}; }",
        "function foo() { var a = [1, 2]; }");
    assertInlineObjectLiteral(root, "function foo() { var obj = {a: null}; }",
        "function foo() { var a = null; }");
    assertInlineObjectLiteral(root, "function foo() { var obj = {}; }",
        "function foo() { var a; }");
    assertInlineObjectLiteral(root, "function foo() { var obj = {a: null}; foo(); }",
        "function foo() { var a; foo(); }");
    assertInlineObjectLiteral(root, "function foo() { var obj = {get a() { return 1; }}; }",
        "function foo() { var getter; }"); // getter function not handled

    assertInlineObjectLiteralWithInitialValue(root, "function foo() { var obj = {a: 1, b: 1}; }",
        "function foo() { var a = 1, b = 1; }");
    assertInlineObjectLiteralWithInitialValue(root, "function foo() { var obj = {a: 1, b: {c: 1}}; }",
        "function foo() { var a = 1, c = 1, b = c; }");
    assertInlineObjectLiteralWithInitialValue(root, "function foo() { var obj = {a: [1, 1]}; }",
        "function foo() { var a = [1, 1]; }");
    assertInlineObjectLiteralWithInitialValue(root, "function foo() { var obj = {a: null}; }",
        "function foo() { var a = null; }");
    assertInlineObjectLiteralWithInitialValue(root, "function foo() { var obj = {}; }",
        "function foo() { var a; }");

    assertInlineObjectLiteralWithNestedObject(root, "function foo() { var obj = {a: {b: 1}}; }",
        "function foo() { var b = 1; var a = {b: b}; }");

    assertInlineObjectLiteralWithCycle(root, "function foo() { var obj = {a: obj}; }",
        "// Error: Infinite recursion when splitting object literals.");

    assertInlineObjectLiteralWithSelfReference(root, "function foo() { var obj = {a: function() { return obj; }}; }",
        "// Error: Self-referential object literal not supported.");

    compiler.reportCodeChange();
  }

  private void assertInlineObjectLiteral(Node root, String input, String expectedOutput) {
    Node origRoot = root;
    root = NodeUtil.parse(input);
    root.getParent().replaceChild(origRoot, root);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        (AbstractCompiler) compiler, Node::newString);
    inlineObjectLiterals.process(null, root);

    assertEquals(expectedOutput, NodeUtil.scriptletToString(root));
  }

  private void assertInlineObjectLiteralWithInitialValue(Node root, String input, String expectedOutput) {
    Node origRoot = root;
    root = NodeUtil.parse(input);
    root.getParent().replaceChild(origRoot, root);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        (AbstractCompiler) compiler, node -> Node.newVarDecl(Node.newName("t" + root.getUniqueIdSuffix()), null));
    inlineObjectLiterals.process(null, root);

    assertEquals(expectedOutput, NodeUtil.scriptletToString(root));
  }

  private void assertInlineObjectLiteralWithNestedObject(Node root, String input, String expectedOutput) {
    Node origRoot = root;
    root = NodeUtil.parse(input);
    root.getParent().replaceChild(origRoot, root);

    InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
        (AbstractCompiler) compiler, node -> Node.newVarDecl(Node.newName("t" + root.getUniqueIdSuffix()), null));
    inlineObjectLiterals.process(null, root);

    assertEquals(expectedOutput, NodeUtil.scriptletToString(root));
  }

  private void assertInlineObjectLiteralWithCycle(Node root, String input, String expectedErrorMessage) {
    Node origRoot = root;
    root = NodeUtil.parse(input);
    root.getParent().replaceChild(origRoot, root);

    try {
      InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
          (AbstractCompiler) compiler, Node::newString);
      inlineObjectLiterals.process(null, root);
      fail();
    } catch (RuntimeException e) {
      assertEquals(expectedErrorMessage, e.getMessage());
    }
  }

  private void assertInlineObjectLiteralWithSelfReference(Node root, String input, String expectedErrorMessage) {
    Node origRoot = root;
    root = NodeUtil.parse(input);
    root.getParent().replaceChild(origRoot, root);

    try {
      InlineObjectLiterals inlineObjectLiterals = new InlineObjectLiterals(
          (AbstractCompiler) compiler, Node::newString);
      inlineObjectLiterals.process(null, root);
      fail();
    } catch (RuntimeException e) {
      assertEquals(expectedErrorMessage, e.getMessage());
    }
  }
}