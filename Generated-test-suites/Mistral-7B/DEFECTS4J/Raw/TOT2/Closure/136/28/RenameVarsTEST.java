import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.RenameVars;
import com.google.javascript.rhino.Node;

public class RenameVarsTest {

  @Test
  public void testRenameVars() {
    Compiler compiler = new Compiler();
    String inputCode = "function foo(a, b) {" +
                       "  var c = function(d, e) { return a; }" +
                       "  var fa = a;" +
                       "  var fb = b;" +
                       "  var fc = c;" +
                       "  return fc;" +
                       "}";

    Node rootNode = compiler.parseTree(inputCode, null);
    RenameVars renameVars = new RenameVars(compiler, "", false, true, true,
        null, null, null);
    renameVars.process(compiler.getExterns(), rootNode);

    String outputCode = compiler.getSource().getCode();

    String expectedCode = "function foo(a, b) {" +
                          "  var x = function(d, e) { return a; }" +
                          "  var fa = a;" +
                          "  var fb = b;" +
                          "  var xx = x;" +
                          "  return xx;" +
                          "}";

    assertEquals(expectedCode, outputCode);
  }

  @Test
  public void testRenameVarsWithLocalRenamingOnly() {
    Compiler compiler = new Compiler();
    String inputCode = "function foo(a, b) {" +
                       "  var c = function(d, e) { return a; }" +
                       "  var fa = a;" +
                       "  var fb = b;" +
                       "  var fc = c;" +
                       "  return fc;" +
                       "}";

    RenameVars renameVars = new RenameVars(compiler, "", true, true, true,
        null, null, null);
    renameVars.process(compiler.getExterns(), compiler.parseTree(inputCode, null));

    String outputCode = compiler.getSource().getCode();

    String expectedCode = "function foo(a, b) {" +
                          "  function x(d, e) { return a; }" +
                          "  var x = x;" +
                          "  return x;" +
                          "}";

    assertEquals(expectedCode, outputCode);
  }

  @Test(expected = AssertionError.class)
  public void testRenameVarsWithExternVariable() {
    Compiler compiler = new Compiler();
    String inputCode = "window.foo = 1; function bar(a) {" +
                       "  var b = a;" +
                       "  return b;" +
                       "}";

    RenameVars renameVars = new RenameVars(compiler, "", false, true, true,
        null, null, null);
    renameVars.process(compiler.getExterns(), compiler.parseTree(inputCode, null));

    String outputCode = compiler.getSource().getCode();

    // The renaming map should not rename the extern variable.
    String expectedCode = inputCode;

    assertEquals(expectedCode, outputCode);
  }

  @Test(expected = AssertionError.class)
  public void testRenameVarsWithReservedKeywords() {
    Compiler compiler = new Compiler();
    String inputCode = "var if = 1; function foo() {" +
                       "  if (true) {" +
                       "  }" +
                       "}";

    RenameVars renameVars = new RenameVars(compiler, "", false, true, true,
        null, null, null);
    renameVars.process(compiler.getExterns(), compiler.parseTree(inputCode, null));

    String outputCode = compiler.getSource().getCode();

    // The renaming map should not rename reserved keywords.
    String expectedCode = inputCode;

    assertEquals(expectedCode, outputCode);
  }

  @Test
  public void testRenameVarsWithPreserveAnonymousFunctionNames() {
    Compiler compiler = new Compiler();
    String inputCode = "function foo(a, b) {" +
                       "  var c = function(d, e) { return a; }" +
                       "  var fa = a;" +
                       "  var fb = b;" +
                       "  return c;" +
                       "}";

    RenameVars renameVars = new RenameVars(compiler, "", false, true, false,
        null, null, null);
    renameVars.process(compiler.getExterns(), compiler.parseTree(inputCode, null));

    String outputCode = compiler.getSource().getCode();

    String expectedCode = "function foo(a, b) {" +
                          "  function c(d, e) { return a; }" +
                          "  var fa = a;" +
                          "  var fb = b;" +
                          "  return c;" +
                          "}";

    assertEquals(expectedCode, outputCode);
  }

  @Test
  public void testRenameVarsWithGeneratePseudoNames() {
    Compiler compiler = new Compiler();
    String inputCode = "function foo(a, b) {" +
                       "  var c = function(d, e) { return a; }" +
                       "  var fa = a;" +
                       "  var fb = b;" +
                       "  return c;" +
                       "}";

    RenameVars renameVars = new RenameVars(compiler, "", false, true, true,
        null, null, null);
    renameVars.process(compiler.getExterns(), compiler.parseTree(inputCode, null));

    String outputCode = compiler.getSource().getCode();

    String expectedCode = "function foo(a, b) {" +
                          "  function $c(d, e) { return a; }" +
                          "  var fa = a;" +
                          "  var fb = b;" +
                          "  return $c;" +
                          "}";

    assertEquals(expectedCode, outputCode);
  }

  @Test
  public void testRenameVarsWithNoExterns() {
    Compiler compiler = new Compiler();
    String inputCode = "function foo(a, b) {" +
                       "  var c = function(d, e) { return a; }" +
                       "  var fa = a;" +
                       "  var fb = b;" +
                       "  return c;" +
                       "}";

    RenameVars renameVars = new RenameVars(compiler, "", false, true, true,
        null, null, null);
    renameVars.process(null, compiler.parseTree(inputCode, null));

    String outputCode = compiler.getSource().getCode();

    String expectedCode = "function foo(a, b) {" +
                          "  function $c(d, e) { return a; }" +
                          "  var fa = a;" +
                          "  var fb = b;" +
                          "  return $c;" +
                          "}";

    assertEquals(expectedCode, outputCode);
  }
}