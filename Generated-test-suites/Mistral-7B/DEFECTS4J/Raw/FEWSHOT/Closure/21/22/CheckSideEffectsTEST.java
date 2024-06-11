package com.google.javascript.jscomp.test;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Nodeops;

import java.util.List;

/**
 * Test cases for {@link CheckSideEffects} class.
 */
public class CheckSideEffectsTest {

  private static AbstractCompiler createCompiler() {
    return new CompilerOptions()
        .setPrintPreamble(false)
        .setPrintErrorMessages(true)
        .setPrintWarningMessages(true)
        .setOutputFile("out", "test_checkSideEffects.js")
        .compile("test_checkSideEffects.js");
  }

  @Test
  public void testCheckSideEffects() {
    AbstractCompiler compiler = createCompiler();
    CompilerInput input = compiler.getInput();

    String code = "function foo() {\n" +
        "  return 42;\n" +
        "}\n" +
        "\n" +
        "function test() {\n" +
        "  var a = foo();\n" +
        "  var b = foo();\n" +
        "  var c = 5;\n" +
        "  var d = a + b;\n" +
        "  var e = c + d;\n" +
        "  var f = a + b + c;\n" +
        "  var g = a * b;\n" +
        "  var h = a % b;\n" +
        "  var i = a << b;\n" +
        "  var j = a >> b;\n" +
        "  var k = a >>> b;\n" +
        "  var l = a + 'string';\n" +
        "  var m = a - 'string';\n" +
        "  var n = a * 'string';\n" +
        "  var o = a / 'string';\n" +
        "  var p = a % 'string';\n" +
        "  var q = a << 'string';\n" +
        "  var r = a >> 'string';\n" +
        "  var s = a >>> 'string';\n" +
        "  var t = a + true;\n" +
        "  var u = a + false;\n" +
        "  var v = a - true;\n" +
        "  var w = a - false;\n" +
        "  var x = a * true;\n" +
        "  var y = a * false;\n" +
        "  var z = a / true;\n" +
        "  var A = a / false;\n" +
        "  var B = a % true;\n" +
        "  var C = a % false;\n" +
        "  var D = a << true;\n" +
        "  var E = a << false;\n" +
        "  var F = a >> true;\n" +
        "  var G = a >> false;\n" +
        "  var H = a >>> true;\n" +
        "  var I = a >>> false;\n" +
        "  var J = a++;\n" +
        "  var K = a--;\n" +
        "  var L = ++a;\n" +
        "  var M = --a;\n" +
        "  var N = a + null;\n" +
        "  var O = a + undefined;\n" +
        "  var P = a + 0;\n" +
        "  var Q = a + new Object();\n" +
        "  var R = a + [1, 2, 3];\n" +
        "  var S = a + function() {};\n" +
        "  var T = a + /foo/;\n" +
        "  var U = a + {};\n" +
        "  var V = a + [];\n" +
        "  var W = a + new Number(1);\n" +
        "  var X = a + new Boolean(true);\n" +
        "  var Y = a + new Boolean(false);\n" +
        "  var Z = a + null;\n" +
        "  var AA = a + undefined;\n" +
        "  var AB = a + 0;\n" +
        "  var AC = a + new Object();\n" +
        "  var AD = a + [1, 2, 3];\n" +
        "  var AE = a + function() {};\n" +
        "  var AF = a + /foo/;\n" +
        "  var AG = a + {};\n" +
        "  var AH = a + [];\n" +
        "  var AI = a + new Number(1);\n" +
        "  var AJ = a + new Boolean(true);\n" +
        "  var AK = a + new Boolean(false);\n" +
        "}\n" +
        "\n" +
        "test();";

    input.setCode(code);

    CheckLevel level = CheckLevel.WARNING;
    CheckSideEffects checker = new CheckSideEffects(compiler, level, false);
    Node root = input.getAst();
    NodeTraversal.traverse(checker, root, null);

    List<Node> problemNodes = checker.problemNodes;
    assertTrue("Problem nodes should not be empty", problemNodes.isEmpty());

    NodeTraversal.traverse(compiler, root, new StripProtection(compiler));
    compiler.processInput();

    String output = compiler.getOutput();
    String expectedOutput = "function test() {\n" +
        "  var a = 42;\n" +
        "  var b = 42;\n" +
        "  var c = 5;\n" +
        "  var d = 84;\n" +
        "  var e = 91;\n" +
        "  var f = 126;\n" +
        "  var g = 0;\n" +
        "  var h = 0;\n" +
        "  var i = 0;\n" +
        "  var j = 0;\n" +
        "  var k = 0;\n" +
        "  var l = 'string42';\n" +
        "  var m = 'string42';\n" +
        "  var n = 0;\n" +
        "  var o = NaN;\n" +
        "  var p = NaN;\n" +
        "  var q = NaN;\n" +
        "  var r = NaN;\n" +
        "  var s = '42string';\n" +
        "  var t = 43;\n" +
        "  var u = 41;\n" +
        "  var v = 41;\n" +
        "  var w = 40;\n" +
        "  var x = 1;\n" +
        "  var y = 0;\n" +
        "  var z = 0;\n" +
        "  var A = 0;\n" +
        "  var B = 0;\n" +
        "  var C = 0;\n" +
        "  var D = 0;\n" +
        "  var E = 1;\n" +
        "  var F = 0;\n" +
        "  var G = 0;\n" +
        "  var H = 0;\n" +
        "  var I = 0;\n" +
        "  var J = 43;\n" +
        "  var K = 41;\n" +
        "  var L = 43;\n" +
        "  var M = 41;\n" +
        "  var N = null;\n" +
        "  var O = undefined;\n" +
        "  var P = 0;\n" +
        "  var Q = Object.create(null);\n" +
        "  var R = [1, 2, 3];\n" +
        "  var S = function test() {}\n" +
        "  var T = /foo/;\n" +
        "  var U = {}\n" +
        "  var V = []\n" +
        "  var W = new Number(1);\n" +
        "  var X = new Boolean(true);\n" +
        "  var Y = new Boolean(false);\n" +
        "  var Z = null;\n" +
        "  var AA = undefined;\n" +
        "  var AB = 0;\n" +
        "  var AC = Object.create(null);\n" +
        "  var AD = [1, 2, 3];\n" +
        "  var AE = function test() {}\n" +
        "  var AF = /foo/;\n" +
        "  var AG = {}\n" +
        "  var AH = []\n" +
        "  var AI = new Number(1);\n" +
        "  var AJ = new Boolean(true);\n" +
        "  var AK = new Boolean(false)\n" +
        "}\n";

    assertEquals("Unexpected output from compiler", expectedOutput, output);
  }

  static class NodeUtilTest extends NodeUtil {
  }

  static class StripProtection extends AbstractPostOrderCallback implements CompilerPass {

    private final AbstractCompiler compiler;

    StripProtection(AbstractCompiler compiler) {
      this.compiler = compiler;
    }

    @Override
    public void process(Node externs, Node root) {
      NodeTraversal.traverse(compiler, root, this);
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall()) {
        Node target = n.getFirstChild();
        if (target.isName() && target.getString().equals("JSCOMPILER_PROTECT")) {
          Node expr = n.getLastChild();
          n.detachChildren();
          parent.replaceChild(n, expr);
        }
      }
    }
  }
}