package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.rhino.NodeFactory;

public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    Compiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = false;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    String code = "function foo() {\n" +
        "  var s = \"this string is \";\n" +
        "  var t = \"continued on the next line but you forgot the +\";\n" +
        "  x = foo(); // should that be '='?\n" +
        "  foo();;\n" +
        "  var a = [1, 2, 3];\n" +
        "  var b = [1, 2, 3];\n" +
        "}\n";

    Node root = NodeFactory.createRootNode(code);
    checkSideEffects.process(null, root);

    List<Node> problemNodes = checkSideEffects.getProblemNodes();
    assertTrue(problemNodes.size() > 0);

    problemNodes.forEach(node -> {
      Node parent = node.getParent();
      int index = parent.getChildIndex(node);
      parent.removeChild(index);
    });

    String expectedCode = "function foo() {\n" +
        "  var s = \"this string is \";\n" +
        "  var t = \"continued on the next line but you forgot the +\";\n" +
        "  x = foo;\n" +
        "  foo();; // probably just a stray-semicolon. Doesn't hurt to check though\n" +
        "  var a = [1, 2, 3];\n" +
        "  var b = [1, 2, 3];\n" +
        "}";

    String actualCode = NodePrinter.print(compiler.getSourceOutput());
    assertTrue(actualCode.equals(expectedCode));
  }

  @Test
  public void testCheckSideEffectsProtectSideEffectFreeCode() {
    Compiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    String code = "function foo() {\n" +
        "  var s = \"this string is \";\n" +
        "  var t = \"continued on the next line but you forgot the +\";\n" +
        "  x == foo();  // should that be '='?\n" +
        "  foo();;\n" +
        "  var a = [1, 2, 3];\n" +
        "}" +
        "\n" +
        "function bar() {\n" +
        "  function baz() {\n" +
        "    for (var i = 0; i < 10; i++) {\n" +
        "      s = \"some string\";\n" +
        "    }\n" +
        "  }\n" +
        "  baz();\n" +
        "}\n";

    Node root = NodeFactory.createRootNode(code);
    checkSideEffects.process(null, root);

    List<Node> problemNodes = checkSideEffects.getProblemNodes();
    assertTrue(problemNodes.isEmpty());

    String expectedCode = "function foo() {\n" +
        "  var s = \"this string is \";\n" +
        "  var t = \"continued on the next line but you forgot the +\";\n" +
        "  x = foo;\n" +
        "  foo();; // probably just a stray-semicolon. Doesn't hurt to check though\n" +
        "  var a = [1, 2, 3];\n" +
        "}\n" +
        "\n" +
        "function bar() {\n" +
        "  function baz() {\n" +
        "    function JSCOMPILER_PRESERVE() {}\n" +
        "    for (var i = 0; i < 10; i++) {\n" +
        "      var _temp_ = s;\n" +
        "      s = \"some string\";\n" +
        "      JSCOMPILER_PRESERVE();\n" +
        "      s = _temp_;\n" +
        "    }\n" +
        "  }\n" +
        "  baz();\n" +
        "}";

    String actualCode = NodePrinter.print(compiler.getSourceOutput());
    assertTrue(actualCode.equals(expectedCode));
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsProtectSideEffectFreeCodeError() {
    Compiler compiler = new Compiler();
    CheckLevel level = CheckLevel.WARNING;
    boolean protectSideEffectFreeCode = true;
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, level, protectSideEffectFreeCode);

    String code = "function foo() {\n" +
        "  x = 5;\n" +
        "}" +
        "\n" +
        "function bar() {\n" +
        "  foo();\n" +
        "}" +
        "\n" +
        "function baz() {\n" +
        "  bar();\n" +
        "}";

    Node root = NodeFactory.createRootNode(code);
    checkSideEffects.process(null, root);
  }

  @Test
  public void testStripProtection() {
    Compiler compiler = new Compiler();
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.ERROR, false);
    checkSideEffects.process(null, NodeFactory.createRootNode(""));
    new StripProtection(compiler).process(null, NodeFactory.createRootNode(""));

    String expectedCode = "function foo() {\n" +
        "  var s = \"this string is \";\n" +
        "  var t = \"continued on the next line but you forgot the +\";\n" +
        "  x = foo;\n" +
        "  foo();; // probably just a stray-semicolon. Doesn't hurt to check though\n" +
        "  var a = [1, 2, 3];\n" +
        "}\n" +
        "\n" +
        "function bar() {\n" +
        "  function baz() {\n" +
        "    for (var i = 0; i < 10; i++) {\n" +
        "      s = \"some string\";\n" +
        "    }\n" +
        "  }\n" +
        "  baz();\n" +
        "}";

    String actualCode = NodePrinter.print(compiler.getSourceOutput());
    assertTrue(actualCode.equals(expectedCode));
  }
}