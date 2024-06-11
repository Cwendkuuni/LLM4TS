package com.google.javascript.jscomp.tests;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static com.google.javascript.jscomp.util.TreeMatcher.assertTreeEquals;
import static com.google.javascript.rhino.NodeOp.ASSIGN;
import static com.google.javascript.rhino.NodeOp.EXPR_STATEMENT;
import static com.google.javascript.rhino.NodeOp.GET_PROP;
import static com.google.javascript.rhino.NodeOp.NAME;
import static com.google.javascript.rhino.NodeOp.VAR;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineSimpleObjectLiteral() {
    String jsCode = "function foo() {" +
        "  var obj = {x: 123, y: {a: 456}};" +
        "  return obj.x + obj.y.a;" +
        "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "tmpVar");
    CompilerPass[] passes = {pass};

    Node ast = compiler.compile(jsCode, options, passes).getTree();

    Node expected = NodeUtil.parseJS(
        "function foo() {" +
        "  var x = JSCompiler_object_inline_x_, y_a = JSCompiler_object_inline_ya_;" +
        "  return x + y_a;" +
        "}"
    );

    assertTreeEquals(expected, ast);
  }

  @Test
  public void testInlineComplexObjectLiteral() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: 123," +
        "    b: {" +
        "      c: 456," +
        "      d: {" +
        "        e: 789," +
        "        f: function() {}" +
        "      }," +
        "      g: [{h: 123, i: 456}, {j: 789}]" +
        "    }," +
        "    h: function() {}" +
        "  };" +
        "  return obj.a.b.g[0].h;" +
        "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "tmpVar");
    CompilerPass[] passes = {pass};

    Node ast = compiler.compile(jsCode, options, passes).getTree();

    Node expected = NodeUtil.parseJS(
        "function foo() {" +
        "  var a = JSCompiler_object_inline_a_, b_c = JSCompiler_object_inline_bc_, " +
        "      b_d_e = JSCompiler_object_inline_bde_, " +
        "      b_d_g0_h = JSCompiler_object_inline_bdgh_, " +
        "      b_d_g1 = JSCompiler_object_inline_bdg1_," +
        "      b_d_g1_h = JSCompiler_object_inline_bdg1h_," +
        "      h_fn = JSCompiler_object_inline_hf_;" +
        "  return a.b.g[0].h;" +
        "}"
    );

    assertTreeEquals(expected, ast);
  }

  @Test
  public void testInlineObjectLiteralWithSelfReference() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: obj.a.a" +
        "    }," +
        "    c: 123" +
        "  }";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "tmpVar");
    CompilerPass[] passes = {pass};

    Node ast = compiler.compile(jsCode, options, passes).getTree();

    // Self-referential object literals are not supported for inlining
    Node expected = NodeUtil.parseJS("function foo() {var obj = {}; return obj.a.b;}") ;

    assertTreeEquals(expected, ast);
  }

  @Test
  public void testInlineObjectLiteralWithExportedVariable() {
    String jsCode = "var x = 123;" +
        "function foo() {" +
        "  var obj = {x: 456};" +
        "  return obj.x;" +
        "}"
    ;

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "tmpVar");
    CompilerPass[] passes = {pass};

    Node ast = compiler.compile(jsCode, options, passes).getTree();

    // Exported variables cannot be inlined
    Node expected = NodeUtil.parseJS("function foo() {return 456;}") ;

    assertTreeEquals(expected, ast);
  }

  @Test
  public void testInlineObjectLiteralWithExternVariable() {
    String jsCode = "function foo() {" +
        "  var obj = {EXPORT: 123};" +
        "  return obj.EXPORT;" +
        "}"
    ;

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "tmpVar");
    CompilerPass[] passes = {pass};

    Node ast = compiler.compile(jsCode, options, passes).getTree();

    // Extern variables cannot be inlined
    Node expected = NodeUtil.parseJS("function foo() {return 123;}") ;

    assertTreeEquals(expected, ast);
  }

  @Test
  public void testInlineObjectLiteralWithCircularReference() {
    String jsCode = "function foo() {" +
        "  var obj1 = {x: obj2};" +
        "  var obj2 = {x: obj1};" +
        "  return obj1.x;" +
        "}"
    ;

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "tmpVar");
    CompilerPass[] passes = {pass};

    try {
      Node ast = compiler.compile(jsCode, options, passes).getTree();
      fail("Expected a compilation error.");
    } catch (IllegalArgumentException e) {
      // Pass
    }

    Node expectedError = NodeUtil.parseJS("function foo() {") ;
    Node expectedErrorEnd = NodeUtil.newEndStatement();

    assertTreeEquals(expectedError, ast.getFirstChild().getFirstChild());
    assertTreeEquals(expectedErrorEnd, ast.getFirstChild().getLastChild());
  }

  @Test
  public void testInlineObjectLiteralWithMisplacedInitialValue() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    x: 123," +
        "    y: function() {return 456;}" +
        "  }" +
        "  return obj.x;" +
        "}"
    ;

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "tmpVar");
    CompilerPass[] passes = {pass};

    Node ast = compiler.compile(jsCode, options, passes).getTree();

    // If the initial value of an object literal is a function, it cannot be inlined
    Node expected = NodeUtil.parseJS("function foo() {return 123;}") ;

    assertTreeEquals(expected, ast);
  }

  @Test
  public void testInlineObjectLiteralWithFunctionWithBindingVariable() {
    String jsCode = "function foo(x) {" +
        "  var obj = {" +
        "    x: x," +
        "    y: function() {return x;}" +
        "  }" +
        "  return obj.x;" +
        "}"
    ;

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "tmpVar");
    CompilerPass[] passes = {pass};

    Node ast = compiler.compile(jsCode, options, passes).getTree();

    // Object literals with a function property that references a binding variable can be inlined
    Node expected = NodeUtil.parseJS("function foo(x) {return x;}") ;

    assertTreeEquals(expected, ast);
  }

  @Test
  public void testInlineObjectLiteralsWithNestedObjectLiterals() {
    String jsCode = "function foo() {" +
        "  var obj = {" +
        "    a: {" +
        "      b: 123," +
        "      c: {" +
        "        d: 456," +
        "        e: [" +
        "          1," +
        "          {f: 789}" +
        "        ]" +
        "      }" +
        "    }" +
        "  };" +
        "  return obj.a.c.e[1].f;" +
        "}"
    ;

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "tmpVar");
    CompilerPass[] passes = {pass};

    Node ast = compiler.compile(jsCode, options, passes).getTree();

    // Object literals with nested object literals can be inlined
    Node expected = NodeUtil.parseJS("function foo() {" +
        "  var a_b = JSCompiler_object_inline_ab_, a_c_d = JSCompiler_object_inline_acd_, " +
        "      a_c_e1 = JSCompiler_object_inline_ace1_, a_c_e1_f = JSCompiler_object_inline_ace1f_;" +
        "  return a_c_e1_f;" +
        "}") ;

    assertTreeEquals(expected, ast);
  }
}