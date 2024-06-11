package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static com.google.common.truth.Truth.assertThat;
import static com.google.javascript.jscomp.InlineObjectLiterals.VAR_PREFIX;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineSimpleObjectLiterals() {
    String jsCode = "function foo() {" +
        "var obj = {" +
        " x: 1," +
        " y: 2," +
        "};" +
        "return obj.x + obj.y;" +
        "}";

    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "test_var_");
    CompilerOptions options = new CompilerOptions(null);
    options.setPrintWarning = false;
    options.setPrintProgress = false;

    Node root = Parser.parse(new StringReader(jsCode), null, options);
    pass.process(null, root);
    String result = Printer.print(root, 1);

    assertThat(result)
        .isEqualTo("function foo() {" +
        " var x = JSCompiler_object_inline_x_test_var_;\n" +
        " var y = JSCompiler_object_inline_y_test_var_;\n" +
        " return x + y;" +
        "}");
  }

  @Test
  public void testInlineComplexObjectLiterals() {
    String jsCode = "function foo() {" +
        "var obj = {" +
        " x: {" +
        " a: 1," +
        " b: {" +
        " c: 2," +
        " d: 3" +
        " }," +
        " y: 2" +
        "};" +
        "return obj.x.b.c + obj.y;" +
        "}";

    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "test_var_");
    CompilerOptions options = new CompilerOptions(null);
    options.setPrintWarning = false;
    options.setPrintProgress = false;

    Node root = Parser.parse(new StringReader(jsCode), null, options);
    pass.process(null, root);
    String result = Printer.print(root, 1);

    Map<String, String> expectedVarMap = ImmutableMap.of(
        "a", "JSCompiler_object_inline_a_test_var_",
        "c", "JSCompiler_object_inline_c_test_var_",
        "d", "JSCompiler_object_inline_d_test_var_",
        "x", "JSCompiler_object_inline_x_test_var_"
    );

    assertThat(result)
        .isEqualTo("function foo() {" +
        " var a = JSCompiler_object_inline_a_test_var_;\n" +
        " var c = JSCompiler_object_inline_c_test_var_;\n" +
        " var d = JSCompiler_object_inline_d_test_var_;\n" +
        " var x = JSCompiler_object_inline_x_test_var_;\n" +
        " return x.c + JSCompiler_object_inline_y_test_var_;" +
        "}");

    // Test that all variables were properly inlined and renamed
    for (Map.Entry<String, String> entry : expectedVarMap.entrySet()) {
      String variableName = entry.getValue();
      String variableUsage = result.substring(result.indexOf(variableName), result.lastIndexOf(";"));
      String expectedVariableUsage = "JSCompiler_object_inline_" + entry.getKey() + "_test_var_";
      assertThat(variableUsage)
          .isEqualTo(expectedVariableUsage);
    }
  }

  @Test
  public void testNoInlineObjectLiterals() {
    String jsCode = "function foo() {" +
        "var obj = {" +
        " x: 1," +
        " y: 2," +
        "};" +
        "function bar() {" +
        " return obj.x + obj.y;" +
        "}" +
        "return bar();" +
        "}";

    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "test_var_");
    CompilerOptions options = new CompilerOptions(null);
    options.setPrintWarning = false;
    options.setPrintProgress = false;

    Node root = Parser.parse(new StringReader(jsCode), null, options);
    pass.process(null, root);
    String result = Printer.print(root, 1);

    // Object literals should not be inlined if they are part of a function
    assertThat(result)
        .isEqualTo("function foo() {" +
        " var obj = {" +
        " x: 1," +
        " y: 2," +
        "};" +
        " function bar() {" +
        " return obj.x + obj.y;" +
        " }" +
        " return bar();" +
        "}");
  }

  @Test
  public void testInlineGlobalObjectLiterals() {
    String jsCode = "var obj = {" +
        " x: 1," +
        " y: 2," +
        "};" +
        "function foo() {" +
        " return obj.x + obj.y;" +
        "}";

    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> "test_var_");
    CompilerOptions options = new CompilerOptions(null);
    options.setPrintWarning = false;
    options.setPrintProgress = false;

    Node root = Parser.parse(new StringReader(jsCode), null, options);
    pass.process(null, root);
    String result = Printer.print(root, 1);

    // Global variables should not be inlined
    assertThat(result)
        .isEqualTo("var obj = {" +
        " x: 1," +
        " y: 2," +
        "};" +
        "function foo() {" +
        " var x = JSCompiler_object_inline_x_test_var_;\n" +
        " var y = JSCompiler_object_inline_y_test_var_;\n" +
        " return x + y;" +
        "}");
  }
}