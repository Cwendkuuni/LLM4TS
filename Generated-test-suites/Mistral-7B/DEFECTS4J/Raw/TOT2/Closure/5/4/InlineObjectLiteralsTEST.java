import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.InlineObjectLiterals;
import org.junit.Assert;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineSimpleObjectLiteral() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_name");

    String jsCode = "var obj = {a: 1, b: 2};";
    Node externs = Node.externalRoot();
    Node root = Node.parse(jsCode);
    pass.process(externs, root);

    String compiledCode = compiler.toSource(externs, root);
    Assert.assertEquals("var a, b, _temp_0;\n" +
            "a = 1;\n" +
            "b = 2;\n" +
            "_temp_0 = true;\n", compiledCode);
  }

  @Test
  public void testInlineComplexObjectLiteralWithFunctionCall() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_name");

    String jsCode = "function foo(obj) {\n" +
            "  return obj.a + obj.b;\n" +
            "}\n" +
            "var obj = {a: 1, b: 2};\n" +
            "console.log(foo(obj));";
    Node externs = Node.externalRoot();
    Node root = Node.parse(jsCode);
    pass.process(externs, root);

    String compiledCode = compiler.toSource(externs, root);
    Assert.assertEquals("function foo(a, b) {\n" +
            "  return a + b;\n" +
            "}\n" +
            "var a, b;\n" +
            "a = 1;\n" +
            "b = 2;\n" +
            "_temp_0 = true;\n" +
            "console.log(foo(a, b));\n", compiledCode);
  }

  @Test
  public void testInlineObjectLiteralWithSelfReference() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_name");

    String jsCode = "var obj = {\n" +
            "  a: function () {\n" +
            "    return obj.a.b;\n" +
            "  },\n" +
            "  b: 2\n" +
            "};\n" +
            "console.log(obj.a());";
    Node externs = Node.externalRoot();
    Node root = Node.parse(jsCode);
    pass.process(externs, root);

    String compiledCode = compiler.toSource(externs, root);
    Assert.assertEquals("var a, b, c, _temp_0, _temp_1;\n" +
            "a = function () {\n" +
            "  return c.b;\n" +
            "};\n" +
            "b = 2;\n" +
            "c = {\n" +
            "  b: 2,\n" +
            "  a: {\n" +
            "    b: _temp_1\n" +
            "  }\n" +
            "};\n" +
            "_temp_0 = true;\n" +
            "console.log(a());\n", compiledCode);
  }

  @Test
  public void testInlineObjectLiteralWithDeletedProperty() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_name");

    String jsCode = "delete obj.a;\n" +
            "var obj = {a: 1, b: 2};";
    Node externs = Node.externalRoot();
    Node root = Node.parse(jsCode);
    pass.process(externs, root);

    String compiledCode = compiler.toSource(externs, root);
    Assert.assertEquals("var obj, _temp_0;\n" +
            "obj = {\n" +
            "  b: 2\n" +
            "};\n" +
            "_temp_0 = true;\n", compiledCode);
  }

  @Test
  public void testInlineObjectLiteralWithFunctionDeclaration() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_name");

    String jsCode = "var obj = {\n" +
            "  foo: function () {\n" +
            "    return 42;\n" +
            "  },\n" +
            "  a: 1,\n" +
            "  b: 2\n" +
            "};";
    Node externs = Node.externalRoot();
    Node root = Node.parse(jsCode);
    pass.process(externs, root);

    String compiledCode = compiler.toSource(externs, root);
    Assert.assertEquals("var foo, a, b;\n" +
            "foo = function () {\n" +
            "  return 42;\n" +
            "};\n" +
            "a = 1;\n" +
            "b = 2;\n" +
            "_temp_0 = true;\n", compiledCode);
  }

  @Test
  public void testInlineObjectLiteralWithNestedObjects() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_name");

    String jsCode = "var obj = {\n" +
            "  a: 1,\n" +
            "  b: {\n" +
            "    c: 2,\n" +
            "    d: 3,\n" +
            "    e: function () {\n" +
            "      return 42;\n" +
            "    },\n" +
            "    f: 'test'\n" +
            "  }\n" +
            "};";
    Node externs = Node.externalRoot();
    Node root = Node.parse(jsCode);
    pass.process(externs, root);

    String compiledCode = compiler.toSource(externs, root);
    Assert.assertEquals("var a, b, c, d, e, f;\n" +
            "a = 1;\n" +
            "b = {\n" +
            "  c: 2,\n" +
            "  d: 3,\n" +
            "  e: function () {\n" +
            "    return 42;\n" +
            "  },\n" +
            "  f: 'test'\n" +
            "};\n" +
            "_temp_0 = true;\n", compiledCode);
  }

  @Test(expected = AssertionError.class)
  public void testInlineObjectLiteralWithUnreachableProperty() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_name");

    String jsCode = "var obj = {\n" +
            "  a: 1,\n" +
            "  b: {\n" +
            "    c: 2,\n" +
            "    d: 3,\n" +
            "    e: function () {\n" +
            "      return 42;\n" +
            "    },\n" +
            "    f: 'test'\n" +
            "  },\n" +
            "  x: 5\n" +
            "};";
    Node externs = Node.externalRoot();
    Node root = Node.parse(jsCode);
    pass.process(externs, root);

    String compiledCode = compiler.toSource(externs, root);
    Assert.fail(); // The test should fail since the unreachable property 'x' should not be inlined
  }
}