import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckLevel.WARNING;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testSimpleFunction() {
    SourceFile source = new SourceFile("Test.js", "function test() { this.prop = 42; }");
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(source, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
    NodeTraversal traversal = new NodeTraversal(compiler, checker, ast);
    traversal.traverse(ast);
    compiler.reportDiagnostics();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithJsDoc() {
    SourceFile source = new SourceFile("Test.js", "/** @constructor */ function Test() { this.prop = 42; }");
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(source, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
    NodeTraversal traversal = new NodeTraversal(compiler, checker, ast);
    traversal.traverse(ast);
    compiler.reportDiagnostics();

    assertFalse(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testFunctionInsideObject() {
    SourceFile source = new SourceFile("Test.js", "var Test = { prop: 42, func: function() { this.prop = 43; } };");
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(source, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
    NodeTraversal traversal = new NodeTraversal(compiler, checker, ast);
    traversal.traverse(ast);
    compiler.reportDiagnostics();

    assertFalse(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testFunctionAsProperty() {
    SourceFile source = new SourceFile("Test.js", "var Test = { func: function() { this.prop = 42; } }; Test.func();");
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(source, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
    NodeTraversal traversal = new NodeTraversal(compiler, checker, ast);
    traversal.traverse(ast);
    compiler.reportDiagnostics();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testFunctionCallWithJsDoc() {
    SourceFile source = new SourceFile("Test.js", "/** @constructor */ function Test() { } Test.prototype.func = function() { this.prop = 42; }; Test.prototype.test = function() { this.func(); }; Test.test();");
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(source, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
    NodeTraversal traversal = new NodeTraversal(compiler, checker, ast);
    traversal.traverse(ast);
    compiler.reportDiagnostics();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testAssignment() {
    SourceFile source = new SourceFile("Test.js", "var a = this;");
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(source, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
    NodeTraversal traversal = new NodeTraversal(compiler, checker, ast);
    traversal.traverse(ast);
    compiler.reportDiagnostics();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testAssignmentWithJsDoc() {
    SourceFile source = new SourceFile("Test.js", "/** @this {Object} */ var a = this;");
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(source, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
    NodeTraversal traversal = new NodeTraversal(compiler, checker, ast);
    traversal.traverse(ast);
    compiler.reportDiagnostics();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithPrototype() {
    SourceFile source = new SourceFile("Test.js", "function Test() { } Test.prototype.func = function() { this.prop = 42; }; Test.prototype.test = function() { this.func(); }; Test.test();");
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(source, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
    NodeTraversal traversal = new NodeTraversal(compiler, checker, ast);
    traversal.traverse(ast);
    compiler.reportDiagnostics();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @org.junit.Test
  public void testFunctionWithPrototypeAndJsDoc() {
    SourceFile source = new SourceFile("Test.js", "/** @constructor */ function Test() { } Test.prototype.func = /** @this {Object} */ function() { this.prop = 42; }; Test.prototype.test = function() { this.func(); }; Test.test();");
    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new Compiler(options);
    Parser parser = new Parser(compiler);
    Node ast = parser.parse(source, null, null);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, new CheckLevel(WARNING));
    NodeTraversal traversal = new NodeTraversal(compiler, checker, ast);
    traversal.traverse(ast);
    compiler.reportDiagnostics();

    assertTrue(compiler.getDiagnostics().isEmpty());
  }
}