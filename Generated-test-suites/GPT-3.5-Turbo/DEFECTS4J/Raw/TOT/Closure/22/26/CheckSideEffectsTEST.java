import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.testing.TestCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.Asserts;

public class CheckSideEffectsTest {

  private TestCompiler compiler;
  private CheckSideEffects checkSideEffects;

  @Before
  public void setUp() {
    compiler = new TestCompiler();
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
  }

  @After
  public void tearDown() {
    compiler = null;
    checkSideEffects = null;
  }

  @Test
  public void testProcess_noNonSideEffectingStatements_noWarnings() {
    String sourceCode = "var a = 5;";

    Node root = compiler.parse(sourceCode);
    checkSideEffects.process(null, root);

    assertEquals(0, compiler.getWarningCount());
  }

  @Test
  public void testProcess_nonSideEffectingStatement_warningGenerated() {
    String sourceCode = "var a = 5 + 3;";

    Node root = compiler.parse(sourceCode);
    checkSideEffects.process(null, root);

    assertEquals(1, compiler.getWarningCount());
  }

  @Test
  public void testVisit_sideEffectFreeCode_warning() {
    String sourceCode = "var a = 5;";

    Node root = compiler.parse(sourceCode);
    checkSideEffects.visit(compiler.makeSimpleTraversal(), root, null);

    assertEquals(1, compiler.getWarningCount());
  }

  @Test
  public void testVisit_noWarnings() {
    String sourceCode = "var a = 'hello';";

    Node root = compiler.parse(sourceCode);
    checkSideEffects.visit(compiler.makeSimpleTraversal(), root, null);

    assertEquals(0, compiler.getWarningCount());
  }

  @Test
  public void testHotSwapScript_withScriptRoot() {
    String sourceCode = "var a = 5 + 3;";

    Node scriptRoot = compiler.parse(sourceCode);
    Node originalRoot = compiler.parse(sourceCode);
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

    assertEquals(1, compiler.getWarningCount());
  }

  @Test
  public void testHotSwapScript_noWarnings() {
    String sourceCode = "var a = 'hello';";

    Node scriptRoot = compiler.parse(sourceCode);
    Node originalRoot = compiler.parse(sourceCode);
    checkSideEffects.hotSwapScript(scriptRoot, originalRoot);

    assertEquals(0, compiler.getWarningCount());
  }

  @Test
  public void testProtectSideEffects_noProblemNodes_noChange() {
    Node scriptRoot = compiler.parse("var a = 5;");

    checkSideEffects.process(null, scriptRoot);
    checkSideEffects.protectSideEffects();

    Asserts.assertNode(compiler.parse("var a = 5;"), scriptRoot);
  }

  @Test
  public void testProtectSideEffects_withProblemNodes_resultChanged() {
    Node scriptRoot = compiler.parse("var a = 5 + 3;");

    checkSideEffects.process(null, scriptRoot);
    checkSideEffects.protectSideEffects();

    Asserts.assertNode(
        compiler.parse("var a = JSCOMPILER_PRESERVE(5 + 3);"), scriptRoot);
  }
  
  @Test
  public void testStripProtection_noProtectedCode_noChange() {
    Node scriptRoot = compiler.parse("var a = 5;");

    CheckSideEffects.StripProtection stripProtection =
        new CheckSideEffects.StripProtection(compiler);
    stripProtection.process(null, scriptRoot);

    Asserts.assertNode(compiler.parse("var a = 5;"), scriptRoot);
  }
  
  @Test
  public void testStripProtection_withProtectedCode_resultChanged() {
    Node scriptRoot = compiler.parse("var a = JSCOMPILER_PRESERVE(5 + 3);");

    CheckSideEffects.StripProtection stripProtection =
        new CheckSideEffects.StripProtection(compiler);
    stripProtection.process(null, scriptRoot);

    Asserts.assertNode(compiler.parse("var a = (5 + 3);"), scriptRoot);
  }

}