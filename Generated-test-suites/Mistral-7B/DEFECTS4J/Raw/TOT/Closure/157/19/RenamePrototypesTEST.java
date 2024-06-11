import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.CompilationLevel.ADVANCED_OPTIMIZATIONS;
import static com.google.javascript.jscomp.CompilerOptions.CompilationLevel.SIMPLE_OPTIMIZATIONS;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Configuration;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.RenamePrototypes;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.TokenStream;

import org.junit.Test;

/**
 * Test cases for the RenamePrototypes class
 */
public class RenamePrototypesTest {

  @Test
  public void testSimpleRename() {
    String jsCode = "function Foo() {}"
        + "Foo.prototype.bar = function() {};"
        + "var x = new Foo();"
        + "x.bar();";

    CompilerOptions options = new CompilerOptions(CompilerOptions.DEFAULTS);
    options.setCompilationLevel(SIMPLE_OPTIMIZATIONS);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    Configuration config = new Configuration(options, null);

    Node root = new TokenStream(jsCode, null).parse();
    Node externs = null;
    pass.process(externs, root);

    String output = compiler.getSourceFileContent();
    String expectedOutput = "function Foo() {}"
        + "Foo.prototype._bar = function() {};"
        + "var x = new Foo();"
        + "x._bar();";

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testNoRename() {
    String jsCode = "function Foo() {}"
        + "Foo.prototype.indexOf = Array.prototype.indexOf;"
        + "Foo.prototype.lastIndexOf = Array.prototype.lastIndexOf;"
        + "Foo.prototype.toString = Object.prototype.toString;"
        + "Foo.prototype.valueOf = Object.prototype.valueOf;"
        + "var x = new Foo();";

    CompilerOptions options = new CompilerOptions(CompilerOptions.DEFAULTS);
    options.setCompilationLevel(SIMPLE_OPTIMIZATIONS);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, false, null, null);
    Configuration config = new Configuration(options, null);

    Node root = new TokenStream(jsCode, null).parse();
    Node externs = null;
    pass.process(externs, root);

    String output = compiler.getSourceFileContent();
    String expectedOutput = "function Foo() {}"
        + "Foo.prototype.indexOf = Array.prototype.indexOf;"
        + "Foo.prototype.lastIndexOf = Array.prototype.lastIndexOf;"
        + "Foo.prototype.toString = Object.prototype.toString;"
        + "Foo.prototype.valueOf = Object.prototype.valueOf;"
        + "var x = new Foo();";

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testAggressiveRename() {
    String jsCode = "function Foo() {}"
        + "Foo.prototype.capitalize = function() {};"
        + "Foo.prototype.isFunction = Function.prototype.isFunction;"
        + "Foo.prototype.toString = Object.prototype.toString;"
        + "var x = new Foo();"
        + "x.capitalize();";

    CompilerOptions options = new CompilerOptions(CompilerOptions.DEFAULTS);
    options.setCompilationLevel(ADVANCED_OPTIMIZATIONS);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    Configuration config = new Configuration(options, null);

    Node root = new TokenStream(jsCode, null).parse();
    Node externs = null;
    pass.process(externs, root);

    String output = compiler.getSourceFileContent();
    String expectedOutput = "function Foo() {}"
        + "Foo.prototype._capitalize = function() {};"
        + "Foo.prototype._isFunction = Function.prototype.isFunction;"
        + "Foo.prototype._toString = Object.prototype.toString;"
        + "var x = new Foo();"
        + "x._capitalize();";

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testNoRenameWithObjLit() {
    String jsCode = "var obj = {"
        + "  foo: 1,"
        + "  bar: function() {}"
        + "};";

    CompilerOptions options = new CompilerOptions(CompilerOptions.DEFAULTS);
    options.setCompilationLevel(SIMPLE_OPTIMIZATIONS);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, false, null, null);
    Configuration config = new Configuration(options, null);

    Node root = new TokenStream(jsCode, null).parse();
    Node externs = null;
    pass.process(externs, root);

    String output = compiler.getSourceFileContent();
    String expectedOutput = "var obj = {"
        + "  foo: 1,"
        + "  bar: function() {}"
        + "};";

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testRenameWithObjLit() {
    String jsCode = "var obj = {"
        + "  foo: 1,"
        + "  _bar: function() {}"
        + "};";

    CompilerOptions options = new CompilerOptions(CompilerOptions.DEFAULTS);
    options.setCompilationLevel(SIMPLE_OPTIMIZATIONS);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    Configuration config = new Configuration(options, null);

    Node root = new TokenStream(jsCode, null).parse();
    Node externs = null;
    pass.process(externs, root);

    String output = compiler.getSourceFileContent();
    String expectedOutput = "var obj = {"
        + "  foo: 1,"
        + "  _bar: function() {}"
        + "};";

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testPrototypeAssignment() {
    String jsCode = "function Foo() {}"
        + "Foo.prototype = {foo: 1};";

    CompilerOptions options = new CompilerOptions(CompilerOptions.DEFAULTS);
    options.setCompilationLevel(SIMPLE_OPTIMIZATIONS);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, false, null, null);
    Configuration config = new Configuration(options, null);

    Node root = new TokenStream(jsCode, null).parse();
    Node externs = null;
    pass.process(externs, root);

    String output = compiler.getSourceFileContent();
    String expectedOutput = "function Foo() {}"
        + "Foo.prototype = {foo: 1};";

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testPrototypeAssignmentWithRename() {
    String jsCode = "function Foo() {}"
        + "Foo.prototype = {_foo: 1};";

    CompilerOptions options = new CompilerOptions(CompilerOptions.DEFAULTS);
    options.setCompilationLevel(SIMPLE_OPTIMIZATIONS);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, true, null, null);
    Configuration config = new Configuration(options, null);

    Node root = new TokenStream(jsCode, null).parse();
    Node externs = null;
    pass.process(externs, root);

    String output = compiler.getSourceFileContent();
    String expectedOutput = "function Foo() {}"
        + "Foo.prototype = {_foo: 1};";

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testPrototypeAssignmentWithExternalProperty() {
    String jsCode = "function Foo() {}"
        + "Foo.prototype.indexOf = Array.prototype.indexOf;"
        + "Foo.prototype = {foo: 1};";

    CompilerOptions options = new CompilerOptions(CompilerOptions.DEFAULTS);
    options.setCompilationLevel(SIMPLE_OPTIMIZATIONS);

    Compiler compiler = new Compiler();
    CompilerPass pass = new RenamePrototypes(compiler, false, null, null);
    Configuration config = new Configuration(options, null);

    Node root = new TokenStream(jsCode, null).parse();
    Node externs = null;
    pass.process(externs, root);

    String output = compiler.getSourceFileContent();
    String expectedOutput = "function Foo() {}"
        + "Foo.prototype.indexOf = Array.prototype.indexOf;"
        + "Foo.prototype = {foo: 1};";

    assertEquals(expectedOutput, output);
  }
}