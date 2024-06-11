import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.rhino.NodeFactory;

public class RenameVarsTest {
  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private CompilerOptions options;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    nodeFactory = compiler.getContext().getFactory();
    options = new CompilerOptions();
  }

  @Test
  public void testRenameVars() {
    // Test basic functionality
    String originalCode = "function foo(a, b) { var c = a + b; return c; }";
    CompilerInput input = new CompilerInput("test.js", originalCode, null, null);
    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(input.getRoot(), input.getRoot());
    String output = compiler.toSource(input.getRoot());
    assertEquals("function foo(a, b) { var d = a + b; return d; }", output);

    // Test edge cases and exception handling
    // Test renaming of local variables
    originalCode = "function foo(a, b) { var c = a + b; var c = 5; return c; }";
    input = new CompilerInput("test.js", originalCode, null, null);
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    try {
      renameVars.process(input.getRoot(), input.getRoot());
      fail("Expected an exception to be thrown for name collision");
    } catch (RuntimeException e) {
      // Expected exception
    }

    // Test renaming of global variables
    originalCode = "var a = 1, b = 2, c = a + b;";
    input = new CompilerInput("test.js", originalCode, null, null);
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(nodeFactory.createNodeList(), input.getRoot());
    output = compiler.toSource(input.getRoot());
    assertEquals("var a_1 = 1, b = 2, c = a_1 + b;", output);

    // Test renaming of anonymous functions
    originalCode = "function foo() { var a = function(b) { return b; } };";
    input = new CompilerInput("test.js", originalCode, null, null);
    renameVars = new RenameVars(compiler, "", false, true, false, null, null, null);
    renameVars.process(input.getRoot(), input.getRoot());
    output = compiler.toSource(input.getRoot());
    assertEquals("function foo() { var a = function(b) { return b; } };\nfunction a(b) { return b; }", output);

    // Test renaming of externs
    originalCode = "function foo() { export var a = 1; }";
    input = new CompilerInput("test.js", originalCode, null, null);
    renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(input.getRoot(), input.getRoot());
    output = compiler.toSource(input.getRoot());
    assertEquals("function foo() { export var a_1 = 1; }", output);

    // Test preserving pseudo names
    originalCode = "function foo(a) { return a.$$foo; }";
    input = new CompilerInput("test.js", originalCode, null, null);
    renameVars = new RenameVars(compiler, "", false, false, true, null, null, null);
    renameVars.process(input.getRoot(), input.getRoot());
    output = compiler.toSource(input.getRoot());
    assertEquals("function foo(a) { return a.$$foo; }", output);
  }
}