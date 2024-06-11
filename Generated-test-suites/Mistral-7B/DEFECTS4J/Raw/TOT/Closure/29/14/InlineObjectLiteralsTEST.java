package com.google.javascript.jscomp.tests;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ReferenceCollectingCallback;
import com.google.javascript.jscomp.Scope;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  private Compiler compiler;
  private InlineObjectLiterals pass;

  @Before
  public void setUp() {
    compiler = new Compiler();
    pass = new InlineObjectLiterals(compiler, () -> null);
  }

  @Test
  public void testInlineObjectLiteral() {
    String source = "function f() {" +
        "  var x = {" +
        "    a: 1," +
        "    b: 2," +
        "  };" +
        "  var y = x.a;" +
        "  return y;" +
        "}";

    Node root = parser(source);
    Node externs = NodeFactory.createRootNode();
    pass.process(externs, root);
    Node expected = NodeFactory.parse("return 1;");

    assertTreeEquals(expected, root);
  }

  @Test
  public void testInlineObjectLiteralWithGetters() {
    String source = "function f() {" +
        "  var x = {" +
        "    get a() { return 1; }," +
        "    get b() { return 2; }" +
        "  };" +
        "  return x.a + x.b;" +
        "}";

    Node root = parser(source);
    Node externs = NodeFactory.createRootNode();
    pass.process(externs, root);

    Node expected = NodeFactory.parse("return 3;");
    assertTreeEquals(expected, root);
  }

  @Test
  public void testDontInlineObjectLiteralWithSelfReference() {
    String source = "function f() {" +
        "  var x = {" +
        "    get a() { return this; }," +
        "    get b() { return this.a; }" +
        "  };" +
        "  return x.b;" +
        "}";

    Node root = parser(source);
    Node externs = NodeFactory.createRootNode();
    pass.process(externs, root);

    Node expected = NodeFactory.parse("return this;");
    assertTreeEquals(expected, root);
  }

  @Test
  public void testInlineObjectLiteralWithCycle() {
    String source = "function f() {" +
        "  var x = {" +
        "    get a() { return x.b; }," +
        "    get b() { return x.a; }" +
        "  };" +
        "  return x.a;" +
        "}";

    Node root = parser(source);
    Node externs = NodeFactory.createRootNode();
    pass.process(externs, root);

    assertThrows(RuntimeException.class, () -> {
      Node expected = NodeFactory.parse("return undefined;");
      assertTreeEquals(expected, root);
    });
  }

  @Test
  public void testDontInlineGlobalObjectLiteral() {
    String source = "var x = { foo: 1 };";

    Node root = parser(source);
    Node externs = NodeFactory.createRootNode();
    pass.process(externs, root);

    Node expected = NodeFactory.parse(source);
    assertTreeEquals(expected, root);
  }

  @Test
  public void testDontInlineStringLiteralObject() {
    String source = "function f() {" +
        "  var x = {'toString': function() { return 'hi'; }};" +
        "  return x.toString();" +
        "}";

    Node root = parser(source);
    Node externs = NodeFactory.createRootNode();
    pass.process(externs, root);

    Node expected = NodeFactory.parse("return 'hi';");
    assertTreeEquals(expected, root);
  }

  @Test
  public void testDontInlineObjectLiteralWithFunctionExpression() {
    String source = "function f() {" +
        "  var x = {" +
        "    foo: function() { return 1; }" +
        "  };" +
        "  return x.foo();" +
        "}";

    Node root = parser(source);
    Node externs = NodeFactory.createRootNode();
    pass.process(externs, root);

    Node expected = NodeFactory.parse("return 1;");
    assertTreeEquals(expected, root);
  }

  private static Node parser(String source) {
    return new Parser().parse(source, "test", null);
  }

  private static void assertTreeEquals(Node expected, Node actual) {
    if (!NodeUtil.isTreeEquivalent(expected, actual)) {
      StringBuilder sb = new StringBuilder();
      NodeUtil.printTree(expected, sb, 0);
      System.out.println("Expected: " + sb);
      NodeUtil.printTree(actual, sb, 0);
      System.out.println("Actual: " + sb);
      throw new AssertionError();
    }
  }
}