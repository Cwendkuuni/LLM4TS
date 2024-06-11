import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.rhino.Node;

public class RenameVarsTest {

  @Test
  public void testRenameVars() {
    String sourceCode = "function foo(a, b) {\n" +
        "  var c = function(d, e) { return a; }\n" +
        "  var fa = a;\n" +
        "  return c;\n" +
        "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    Node root = compiler.parseTree(sourceCode, new CompilerInput("Test", "Test.js", 0));

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(compiler.getExterns(), root);

    String renamedCode = compiler.getPrintedOutput();
    String expectedCode = "function foo(a, b) {\n" +
        "  var _temp0 = function(_temp1, _temp2) { return a; }\n" +
        "  var fa = a;\n" +
        "  return _temp0;\n" +
        "}";

    assertEquals(expectedCode, renamedCode);
  }

  @Test
  public void testRenameGlobals() {
    String sourceCode = "var a = 1, b = 2, c = 3";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    Node root = compiler.parseTree(sourceCode, new CompilerInput("Test", "Test.js", 0));

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(compiler.getExterns(), root);

    String renamedCode = compiler.getPrintedOutput();
    String expectedCode = "var a__ = 1, b__ = 2, c__ = 3";

    assertEquals(expectedCode, renamedCode);
  }

  @Test
  public void testRenameLocals() {
    String sourceCode = "function foo(a, b) {\n" +
        "  var c = function(d, e) { return a; }\n" +
        "  var fa = a;\n" +
        "  var i = 0;\n" +
        "  for (var i = 0; i < 5; i++) {\n" +
        "    var j = i;\n" +
        "  }\n" +
        "  return c;\n" +
        "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    Node root = compiler.parseTree(sourceCode, new CompilerInput("Test", "Test.js", 0));

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(compiler.getExterns(), root);

    String renamedCode = compiler.getPrintedOutput();
    String expectedCode = "function foo(a, b) {\n" +
        "  var _temp0 = function(_temp1, _temp2) { return a; }\n" +
        "  var fa = a;\n" +
        "  var i_ = 0;\n" +
        "  for (var i_ = 0; i_ < 5; i_++) {\n" +
        "    var j_ = i_;\n" +
        "  }\n" +
        "  return _temp0;\n" +
        "}";

    assertEquals(expectedCode, renamedCode);
  }

  @Test
  public void testRenameVarsWithExceptions() {
    String sourceCode = "try {\n" +
        "  var a = 1;\n" +
        "} catch (e) {\n" +
        "  var a = 2;\n" +
        "}";

    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler();
    compiler.init(options);

    Node root = compiler.parseTree(sourceCode, new CompilerInput("Test", "Test.js", 0));

    RenameVars renameVars = new RenameVars(compiler, "", false, false, false, null, null, null);
    renameVars.process(compiler.getExterns(), root);

    try {
      String renamedCode = compiler.getPrintedOutput();
      fail("Expected a compilation error.");
    } catch (AssertionError e) {
      // Expected
    }
  }
}