import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    String sourceCode = "function evil() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler() {};
    Checker checker = new Checker(new CompilerOptions(), null, null, null, null, null, compiler);
    SourceFile source = new SourceFile("test.js", sourceCode);

    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Checker.typeCheck, CheckLevel.WARNING);
    checker.check(source, options);

    Node ast = source.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, options.getWarningLevel(Checker.typeCheck)));
    traversal.traverse(ast);

    int numDiagnostics = checker.getDiagnostics().size();
    assertTrue(numDiagnostics > 0);
    DiagnosticType diagnosticType = checker.getDiagnostics().get(0).getType();
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getName());
  }

  @Test
  public void testCheckGlobalThis_Constructor() {
    String sourceCode = "function EvilConstructor() {}";

    AbstractCompiler compiler = new AbstractCompiler() {};
    Checker checker = new Checker(new CompilerOptions(), null, null, null, null, null, compiler);
    SourceFile source = new SourceFile("test.js", sourceCode);

    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Checker.typeCheck, CheckLevel.WARNING);
    checker.check(source, options);

    Node ast = source.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, options.getWarningLevel(Checker.typeCheck)));
    traversal.traverse(ast);

    int numDiagnostics = checker.getDiagnostics().size();
    assertFalse(numDiagnostics > 0);
  }

  @Test
  public void testCheckGlobalThis_PrototypeMethod() {
    String sourceCode = "var evilPrototype = { evilMethod: function() { this.useful = undefined; } };";

    AbstractCompiler compiler = new AbstractCompiler() {};
    Checker checker = new Checker(new CompilerOptions(), null, null, null, null, null, compiler);
    SourceFile source = new SourceFile("test.js", sourceCode);

    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Checker.typeCheck, CheckLevel.WARNING);
    checker.check(source, options);

    Node ast = source.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, options.getWarningLevel(Checker.typeCheck)));
    traversal.traverse(ast);

    int numDiagnostics = checker.getDiagnostics().size();
    assertFalse(numDiagnostics > 0);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithThisAnnotation() {
    String sourceCode = "function evilWithThisAnnotation() { @this this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler() {};
    Checker checker = new Checker(new CompilerOptions(), null, null, null, null, null, compiler);
    SourceFile source = new SourceFile("test.js", sourceCode);

    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Checker.typeCheck, CheckLevel.WARNING);
    checker.check(source, options);

    Node ast = source.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, options.getWarningLevel(Checker.typeCheck)));
    traversal.traverse(ast);

    int numDiagnostics = checker.getDiagnostics().size();
    assertFalse(numDiagnostics > 0);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithoutAnnotation() {
    String sourceCode = "function evilNoAnnotation() { this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler() {};
    Checker checker = new Checker(new CompilerOptions(), null, null, null, null, null, compiler);
    SourceFile source = new SourceFile("test.js", sourceCode);

    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Checker.typeCheck, CheckLevel.WARNING);
    checker.check(source, options);

    Node ast = source.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, options.getWarningLevel(Checker.typeCheck)));
    traversal.traverse(ast);

    int numDiagnostics = checker.getDiagnostics().size();
    assertTrue(numDiagnostics > 0);
    DiagnosticType diagnosticType = checker.getDiagnostics().get(0).getType();
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getName());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithAssignLhsChild() {
    String sourceCode = "function evilWithAssignLhsChild() { var a = this; }";

    AbstractCompiler compiler = new AbstractCompiler() {};
    Checker checker = new Checker(new CompilerOptions(), null, null, null, null, null, compiler);
    SourceFile source = new SourceFile("test.js", sourceCode);

    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Checker.typeCheck, CheckLevel.WARNING);
    checker.check(source, options);

    Node ast = source.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, options.getWarningLevel(Checker.typeCheck)));
    traversal.traverse(ast);

    int numDiagnostics = checker.getDiagnostics().size();
    assertTrue(numDiagnostics > 0);
    DiagnosticType diagnosticType = checker.getDiagnostics().get(0).getType();
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getName());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithPrototypeProperty() {
    String sourceCode = "function evilWithPrototypeProperty() { this.prototype.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler() {};
    Checker checker = new Checker(new CompilerOptions(), null, null, null, null, null, compiler);
    SourceFile source = new SourceFile("test.js", sourceCode);

    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Checker.typeCheck, CheckLevel.WARNING);
    checker.check(source, options);

    Node ast = source.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, options.getWarningLevel(Checker.typeCheck)));
    traversal.traverse(ast);

    int numDiagnostics = checker.getDiagnostics().size();
    assertFalse(numDiagnostics > 0);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithPrototypePropertyChain() {
    String sourceCode = "function evilWithPrototypePropertyChain() { this.prototype.a.b.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler() {};
    Checker checker = new Checker(new CompilerOptions(), null, null, null, null, null, compiler);
    SourceFile source = new SourceFile("test.js", sourceCode);

    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Checker.typeCheck, CheckLevel.WARNING);
    checker.check(source, options);

    Node ast = source.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, options.getWarningLevel(Checker.typeCheck)));
    traversal.traverse(ast);

    int numDiagnostics = checker.getDiagnostics().size();
    assertFalse(numDiagnostics > 0);
  }

  @Test
  public void testCheckGlobalThis_FunctionWithAssign() {
    String sourceCode = "function evilWithAssign() { var a = this; this.useful = undefined; }";

    AbstractCompiler compiler = new AbstractCompiler() {};
    Checker checker = new Checker(new CompilerOptions(), null, null, null, null, null, compiler);
    SourceFile source = new SourceFile("test.js", sourceCode);

    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Checker.typeCheck, CheckLevel.WARNING);
    checker.check(source, options);

    Node ast = source.getAst();
    NodeTraversal traversal = new NodeTraversal(ast, new CheckGlobalThis(compiler, options.getWarningLevel(Checker.typeCheck)));
    traversal.traverse(ast);

    int numDiagnostics = checker.getDiagnostics().size();
    assertTrue(numDiagnostics > 0);
    DiagnosticType diagnosticType = checker.getDiagnostics().get(0).getType();
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticType.getName());
  }

  private void assertEquals(String expected, DiagnosticType actual) {
    if (expected == null) {
      assertNull(actual);
    } else {
      assertEquals(expected.getName(), actual.getName());
    }
  }
}