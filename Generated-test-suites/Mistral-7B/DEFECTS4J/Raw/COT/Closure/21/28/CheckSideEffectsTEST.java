import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckSideEffectsTest {

  @Test
  public void testCheckSideEffects() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNINGS, ALL);
    Compiler compiler = new Compiler(options);
    Node source = NodeFactory.createRoot();

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNINGS, true);
    checkSideEffects.process(null, source);

    NodeTraversal.traverse(compiler, source, new StripProtection(compiler));

    assertEquals(0, compiler.getDiagnostics().length());
  }

  @Test
  public void testCheckSideEffectsWithUselessCode() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNINGS, ALL);
    Compiler compiler = new Compiler(options);
    Node source = NodeFactory.createText("var s = \"this string is \";\nvar x = foo();");

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNINGS, true);
    checkSideEffects.process(null, source);

    NodeTraversal.traverse(compiler, source, new StripProtection(compiler));

    DiagnosticInfo diagnosticInfo = compiler.getDiagnostics()[0];
    assertEquals("JSC_USELESS_CODE", diagnosticInfo.getType().name());
    assertEquals("Suspicious code. This code lacks side-effects. Is there a bug?", diagnosticInfo.getMessage());
  }

  @Test
  public void testCheckSideEffectsWithMissingPlus() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNINGS, ALL);
    Compiler compiler = new Compiler(options);
    Node source = NodeFactory.createText("var s = \"this string is \";\nvar s = \"continued on the next line but you forgot the +\"");

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNINGS, true);
    checkSideEffects.process(null, source);

    NodeTraversal.traverse(compiler, source, new StripProtection(compiler));

    DiagnosticInfo diagnosticInfo = compiler.getDiagnostics()[0];
    assertEquals("JSC_USELESS_CODE", diagnosticInfo.getType().name());
    assertEquals("Is there a missing '+' on the previous line?", diagnosticInfo.getMessage());
  }

  @Test(expected = AssertionError.class)
  public void testCheckSideEffectsWithSideEffectCall() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNINGS, ALL);
    Compiler compiler = new Compiler(options);
    Node source = NodeFactory.createText("function foo() { console.log('foo'); } foo();");

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNINGS, true);
    checkSideEffects.process(null, source);

    NodeTraversal.traverse(compiler, source, new StripProtection(compiler));

    assertEquals(0, compiler.getDiagnostics().length());
  }

  @Test
  public void testCheckSideEffectsWithSimpleOp() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNINGS, ALL);
    Compiler compiler = new Compiler(options);
    Node source = NodeFactory.createText("var x = 1 + 2;");

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNINGS, true);
    checkSideEffects.process(null, source);

    NodeTraversal.traverse(compiler, source, new StripProtection(compiler));

    DiagnosticInfo diagnosticInfo = compiler.getDiagnostics()[0];
    assertEquals("JSC_USELESS_CODE", diagnosticInfo.getType().name());
    assertEquals("The result of the '+' operator is not being used.", diagnosticInfo.getMessage());
  }

  @Test
  public void testCheckSideEffectsWithComplexExpression() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNINGS, ALL);
    Compiler compiler = new Compiler(options);
    Node source = NodeFactory.createText("var x = 1 + 2 * 3;");

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNINGS, true);
    checkSideEffects.process(null, source);

    NodeTraversal.traverse(compiler, source, new StripProtection(compiler));

    assertEquals(0, compiler.getDiagnostics().length());
  }

  @Test
  public void testCheckSideEffectsWithVariableDeclaration() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNINGS, ALL);
    Compiler compiler = new Compiler(options);
    Node source = NodeFactory.createText("var x = 1;");

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNINGS, true);
    checkSideEffects.process(null, source);

    NodeTraversal.traverse(compiler, source, new StripProtection(compiler));

    assertEquals(0, compiler.getDiagnostics().length());
  }

  @Test
  public void testCheckSideEffectsWithExpressionStatement() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNINGS, ALL);
    Compiler compiler = new Compiler(options);
    Node source = NodeFactory.createText("1 + 2;");

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNINGS, true);
    checkSideEffects.process(null, source);

    NodeTraversal.traverse(compiler, source, new StripProtection(compiler));

    assertEquals(0, compiler.getDiagnostics().length());
  }

  @Test
  public void testCheckSideEffectsWithEmptyStatement() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNINGS, ALL);
    Compiler compiler = new Compiler(options);
    Node source = NodeFactory.createText(";");

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNINGS, true);
    checkSideEffects.process(null, source);

    NodeTraversal.traverse(compiler, source, new StripProtection(compiler));

    assertEquals(0, compiler.getDiagnostics().length());
  }

  @Test
  public void testCheckSideEffectsWithCommaExpression() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNINGS, ALL);
    Compiler compiler = new Compiler(options);
    Node source = NodeFactory.createText("1, 2;");

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNINGS, true);
    checkSideEffects.process(null, source);

    NodeTraversal.traverse(compiler, source, new StripProtection(compiler));

    assertEquals(0, compiler.getDiagnostics().length());
  }

  @Test
  public void testCheckSideEffectsWithForStatement() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(WARNINGS, ALL);
    Compiler compiler = new Compiler(options);
    Node source = NodeFactory.createText("for (var i = 0; i < 5; i++) {}");

    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, WARNINGS, true);
    checkSideEffects.process(null, source);

    NodeTraversal.traverse(compiler, source, new StripProtection(compiler));

    assertEquals(0, compiler.getDiagnostics().length());
  }

}