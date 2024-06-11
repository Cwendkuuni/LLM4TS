import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.ParrotCompiler;
import com.google.javascript.rhino.JSDocInfo;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    Checker compiler = new ParrotChecker();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING, DiagnosticType.WARNING);

    String sourceCode = "function evil() { this.useful = undefined; }";

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node node = AbstractCompiler.parseTree(sourceCode, null, null, null, options);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, options);
    abstractCompiler.addCallback(checker);
    Node result = abstractCompiler.process(node, null);

    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        checker.compiler.getDiagnostics().get(0).getMessage(null));
  }

  @Test
  public void testCheckGlobalThis_NoDiagnostic() {
    Checker compiler = new ParrotChecker();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING, DiagnosticType.WARNING);

    String sourceCode = "function safe() { this.prototype.useful = undefined; }";

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node node = AbstractCompiler.parseTree(sourceCode, null, null, null, options);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, options);
    abstractCompiler.addCallback(checker);
    Node result = abstractCompiler.process(node, null);

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_Constructor() {
    Checker compiler = new ParrotChecker();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING, DiagnosticType.WARNING);

    String sourceCode = "function Person() { this.name = 'John Doe'; }";

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node node = AbstractCompiler.parseTree(sourceCode, null, null, null, options);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, options);
    abstractCompiler.addCallback(checker);
    Node result = abstractCompiler.process(node, null);

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_PrototypeMethod() {
    Checker compiler = new ParrotChecker();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING, DiagnosticType.WARNING);

    String sourceCode = "var Person = function() {}; Person.prototype.getName = function() { return this.name; }";

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node node = AbstractCompiler.parseTree(sourceCode, null, null, null, options);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, options);
    abstractCompiler.addCallback(checker);
    Node result = abstractCompiler.process(node, null);

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_AnnotatedFunction() {
    Checker compiler = new ParrotChecker();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING, DiagnosticType.WARNING);

    String sourceCode = "/** @this {MyObject} */ function myFunction() { }";

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node node = AbstractCompiler.parseTree(sourceCode, null, null, null, options);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, options);
    abstractCompiler.addCallback(checker);
    Node result = abstractCompiler.process(node, null);

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_NoAnnotatedFunction() {
    Checker compiler = new ParrotChecker();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING, DiagnosticType.WARNING);

    String sourceCode = "function noAnnotation() { this.name = 'John Doe'; }";

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node node = AbstractCompiler.parseTree(sourceCode, null, null, null, options);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, options);
    abstractCompiler.addCallback(checker);
    Node result = abstractCompiler.process(node, null);

    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        checker.compiler.getDiagnostics().get(0).getMessage(null));
  }

  @Test
  public void testCheckGlobalThis_PropertyAccess() {
    Checker compiler = new ParrotChecker();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING, DiagnosticType.WARNING);

    String sourceCode = "var obj = { prop: function() { this.name = 'John Doe'; } }; obj.prop();";

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node node = AbstractCompiler.parseTree(sourceCode, null, null, null, options);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, options);
    abstractCompiler.addCallback(checker);
    Node result = abstractCompiler.process(node, null);

    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        checker.compiler.getDiagnostics().get(0).getMessage(null));
  }

  @Test
  public void testCheckGlobalThis_PropertyAccess_Chain() {
    Checker compiler = new ParrotChecker();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING, DiagnosticType.WARNING);

    String sourceCode = "var obj = { prop1: { prop2: function() { this.name = 'John Doe'; } } }; obj.prop1.prop2();";

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node node = AbstractCompiler.parseTree(sourceCode, null, null, null, options);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, options);
    abstractCompiler.addCallback(checker);
    Node result = abstractCompiler.process(node, null);

    assertEquals(1, checker.compiler.getDiagnostics().size());
    assertEquals("dangerous use of the global 'this' object",
        checker.compiler.getDiagnostics().get(0).getMessage(null));
  }

  @Test
  public void testCheckGlobalThis_PropertyAccess_NoDiagnostic() {
    Checker compiler = new ParrotChecker();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING, DiagnosticType.WARNING);

    String sourceCode = "var obj = { prop: function() { this.prototype.name = 'John Doe'; } }; obj.prop();";

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node node = AbstractCompiler.parseTree(sourceCode, null, null, null, options);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, options);
    abstractCompiler.addCallback(checker);
    Node result = abstractCompiler.process(node, null);

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_PropertyAccess_PrototypeChain() {
    Checker compiler = new ParrotChecker();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNING, DiagnosticType.WARNING);

    String sourceCode = "var obj = { prop: { prop: function() { this.prototype.name = 'John Doe'; } } }; obj.prop.prop();";

    CheckGlobalThis checker = new CheckGlobalThis(compiler, WARNING);
    Node node = AbstractCompiler.parseTree(sourceCode, null, null, null, options);

    AbstractCompiler abstractCompiler = new AbstractCompiler(compiler, options);
    abstractCompiler.addCallback(checker);
    Node result = abstractCompiler.process(node, null);

    assertTrue(checker.compiler.getDiagnostics().isEmpty());
  }
}