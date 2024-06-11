import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.InlineObjectLiterals;
import org.junit.Assert;
import org.junit.Test;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_var_" + Math.random());
    CompilerResult result = compiler.process(new Node("source code"), pass);

    Assert.assertNotNull("Compilation should not fail", result.errors);
    Assert.assertEquals(0, result.errors.size());

    String output = result.code.toString();
    // Check if the object literal has been inlined
    Assert.assertTrue("Object literal should have been inlined", output.contains("JSCompiler_object_inline_"));
  }

  @Test
  public void testEmptyObjectLiterals() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_var_" + Math.random());

    String sourceCode = "var obj = {}";
    CompilerResult result = compiler.process(new Node(sourceCode), pass);

    Assert.assertNotNull("Compilation should not fail", result.errors);
    Assert.assertEquals(0, result.errors.size());

    String output = result.code.toString();
    // Check if the empty object literal has been inlined
    Assert.assertTrue("Empty object literal should have been inlined", output.contains("JSCompiler_object_inline_"));
  }

  @Test
  public void testNestedObjectLiterals() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_var_" + Math.random());

    String sourceCode = "var obj1 = {a: {b: 1}};";
    CompilerResult result = compiler.process(new Node(sourceCode), pass);

    Assert.assertNotNull("Compilation should not fail", result.errors);
    Assert.assertEquals(0, result.errors.size());

    String output = result.code.toString();
    // Check if the nested object literal has been inlined
    Assert.assertTrue("Nested object literal should have been inlined", output.contains("JSCompiler_object_inline_a_"));
    Assert.assertTrue("Nested object literal should have been inlined", output.contains("JSCompiler_object_inline_b_"));
  }

  @Test
  public void testSelfReferentialObjectLiterals() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_var_" + Math.random());

    String sourceCode = "var obj = {a: obj};";
    CompilerResult result = compiler.process(new Node(sourceCode), pass);

    Assert.assertNotNull("Compilation should not fail", result.errors);
    Assert.assertEquals(1, result.errors.size());
    Assert.assertEquals(
        "Self-referential object literals are not supported",
        result.errors.get(0).message,
        result.errors.get(0).toString());
  }

  @Test
  public void testCircularObjectLiterals() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_var_" + Math.random());

    String sourceCode = "var obj1 = {a: obj2};\n" +
            "var obj2 = {a: obj1};";
    CompilerResult result = compiler.process(new Node(sourceCode), pass);

    Assert.assertNotNull("Compilation should not fail", result.errors);
    Assert.assertEquals(1, result.errors.size());
    Assert.assertEquals(
        "Circular object literals are not supported",
        result.errors.get(0).message,
        result.errors.get(0).toString());
  }

  @Test
  public void testObjectLiteralsWithFunctions() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_var_" + Math.random());

    String sourceCode = "var obj = {" +
        "  x: function() { return 1; },\n" +
        "  y: 2,\n" +
        "  z: { a: 1 }";
    CompilerResult result = compiler.process(new Node(sourceCode), pass);

    Assert.assertNotNull("Compilation should not fail", result.errors);
    Assert.assertEquals(0, result.errors.size());

    String output = result.code.toString();
    // Check if the object literal with functions and nested object literal have been inlined
    Assert.assertTrue("Object literal with functions and nested object literal should have been inlined", output.contains("JSCompiler_object_inline_"));
    Assert.assertTrue("Object literal with functions and nested object literal should have been inlined", output.contains("JSCompiler_object_inline_z_"));
  }

  @Test
  public void testObjectLiteralsWithArrays() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "test_var_" + Math.random());

    String sourceCode = "var obj = {" +
        "  x: [1, 2, 3],\n" +
        "  y: 2,\n" +
        "  z: { a: 1 }";
    CompilerResult result = compiler.process(new Node(sourceCode), pass);

    Assert.assertNotNull("Compilation should not fail", result.errors);
    Assert.assertEquals(0, result.errors.size());

    String output = result.code.toString();
    // Check if the object literal with arrays and nested object literal have been inlined
    Assert.assertTrue("Object literal with arrays and nested object literal should have been inlined", output.contains("JSCompiler_object_inline_"));
    Assert.assertTrue("Object literal with arrays and nested object literal should have been inlined", output.contains("JSCompiler_object_inline_z_"));
  }
}