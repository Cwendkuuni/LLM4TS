import static org.junit.Assert.assertThat;
import static com.google.common.collect.Sets.newHashSet;
import static com.google.javascript.jscomp.CheckerFactory.checkType;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Diagnostics;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class VarCheckTest {

  @Test
  public void testSimpleVarDeclaration() {
    String jsSource = "var x = 1;";
    CompilerPass pass = new VarCheck(new TestAbstractCompiler());

    CompilerInput input = new CompilerInput(jsSource, "test.js", null);
    Diagnostics diagnostics = new Diagnostics();

    pass.process(null, input.getAstRoot(new TestAbstractCompiler()));
    Node root = input.getAstRoot(new TestAbstractCompiler());

    NodeTraversal.traverse(new TestChecker(), root, null);

    assertDiagnosticsEmpty(diagnostics);
  }

  @Test
  public void testUndefinedVar() {
    String jsSource = "var x = y;";
    CompilerPass pass = new VarCheck(new TestAbstractCompiler());

    CompilerInput input = new CompilerInput(jsSource, "test.js", null);
    Diagnostics diagnostics = new Diagnostics();

    pass.process(null, input.getAstRoot(new TestAbstractCompiler()));
    Node root = input.getAstRoot(new TestAbstractCompiler());

    NodeTraversal.traverse(new TestChecker(), root, null);

    Set<String> expectedDiagnostics = newHashSet(
        UNDEFINED_VAR_ERROR.getDisplayName("y"));
    assertDiagnostics(expectedDiagnostics, diagnostics);
  }

  @Test
  public void testModuleDependency() {
    String depJsSource = "var x = 1;";
    String mainJsSource = "import y from 'dep.js'; var z = x;";

    CompilerPass pass = new VarCheck(new TestAbstractCompiler());

    CompilerInput depInput = new CompilerInput(depJsSource, "dep.js", null);
    CompilerInput mainInput = new CompilerInput(mainJsSource, "test.js", depInput);

    Diagnostics depDiagnostics = new Diagnostics();
    Diagnostics mainDiagnostics = new Diagnostics();

    pass.process(depInput.getAstRoot(new TestAbstractCompiler()), null);
    pass.process(mainInput.getAstRoot(new TestAbstractCompiler()), mainInput.getAstRoot(new TestAbstractCompiler()));

    NodeTraversal.traverse(new TestChecker(), mainInput.getAstRoot(new TestAbstractCompiler()), null);

    Set<String> expectedDepDiagnostics = newHashSet();
    Set<String> expectedMainDiagnostics = newHashSet(
        VIOLATED_MODULE_DEP_ERROR.getDisplayName("test.js", "dep.js", "x"));

    assertDiagnostics(expectedDepDiagnostics, depDiagnostics);
    assertDiagnostics(expectedMainDiagnostics, mainDiagnostics);
  }

  @Test
  public void testStrictModuleDependency() {
    String depJsSource = "var x = 1;";
    String mainJsSource = "import y from 'dep.js'; var z = x;";

    CompilerPass pass = new VarCheck(new TestAbstractCompiler(true));

    CompilerInput depInput = new CompilerInput(depJsSource, "dep.js", null);
    CompilerInput mainInput = new CompilerInput(mainJsSource, "test.js", depInput);

    Diagnostics depDiagnostics = new Diagnostics();
    Diagnostics mainDiagnostics = new Diagnostics();

    pass.process(depInput.getAstRoot(new TestAbstractCompiler()), null);
    pass.process(mainInput.getAstRoot(new TestAbstractCompiler()), mainInput.getAstRoot(new TestAbstractCompiler()));

    NodeTraversal.traverse(new TestChecker(), mainInput.getAstRoot(new TestAbstractCompiler()), null);

    Set<String> expectedDepDiagnostics = newHashSet();
    Set<String> expectedMainDiagnostics = newHashSet(
        STRICT_MODULE_DEP_ERROR.getDisplayName("test.js", "dep.js", "x"));

    assertDiagnostics(expectedDepDiagnostics, depDiagnostics);
    assertDiagnostics(expectedMainDiagnostics, mainDiagnostics);
  }

  @Test
  public void testMissingModuleDependency() {
    String mainJsSource = "import y from 'nonexistent.js'; var z = y;";

    CompilerPass pass = new VarCheck(new TestAbstractCompiler());

    CompilerInput mainInput = new CompilerInput(mainJsSource, "test.js", null);

    Diagnostics diagnostics = new Diagnostics();

    pass.process(null, mainInput.getAstRoot(new TestAbstractCompiler()));

    NodeTraversal.traverse(new TestChecker(), mainInput.getAstRoot(new TestAbstractCompiler()), null);

    Set<String> expectedDiagnostics = newHashSet(
        MISSING_MODULE_DEP_ERROR.getDisplayName("test.js", "nonexistent.js", "y"));

    assertDiagnostics(expectedDiagnostics, diagnostics);
  }

  @Test
  public void testFunctionDeclaration() {
    String jsSource = "function foo() {}; var x = foo();";

    CompilerPass pass = new VarCheck(new TestAbstractCompiler());

    CompilerInput input = new CompilerInput(jsSource, "test.js", null);
    Diagnostics diagnostics = new Diagnostics();

    pass.process(null, input.getAstRoot(new TestAbstractCompiler()));
    Node root = input.getAstRoot(new TestAbstractCompiler());

    NodeTraversal.traverse(new TestChecker(), root, null);

    assertDiagnosticsEmpty(diagnostics);
  }

  private static final DiagnosticType UNDEFINED_VAR_ERROR =
      DiagnosticType.error("JSC_UNDEFINED_VARIABLE",
          "variable {0} is undefined");

  private static final DiagnosticType VIOLATED_MODULE_DEP_ERROR =
      DiagnosticType.error("JSC_VIOLATED_MODULE_DEPENDENCY",
          "module {0} cannot reference {2}, defined in " +
          "module {1}, since {1} loads after {0}");

  private static final DiagnosticType STRICT_MODULE_DEP_ERROR =
      DiagnosticType.disabled("JSC_STRICT_MODULE_DEPENDENCY",
          "module {0} cannot reference {2}, defined in " +
          "module {1}");

  private static final DiagnosticType MISSING_MODULE_DEP_ERROR =
      DiagnosticType.warning("JSC_MISSING_MODULE_DEPENDENCY",
          "missing module dependency; module {0} should depend " +
          "on module {1} because it references {2}");

  private interface TestChecker extends NodeTraversal.AbstractVisitor {
    // empty
  }

  private static abstract class TestAbstractCompiler extends AbstractCompiler {
    protected TestAbstractCompiler() {
      this(false);
    }

    protected TestAbstractCompiler(boolean strictExternCheck) {
      super(strictExternCheck);
    }
  }

  private static void assertDiagnosticsEmpty(Diagnostics diagnostics) {
    assertDiagnostics(Sets.newHashSet(), diagnostics);
  }

  private static void assertDiagnostics(Set<String> expectedDiagnostics, Diagnostics diagnostics) {
    Set<String> actualDiagnostics = Sets.newHashSet();

    for (Diagnostics.Entry entry : diagnostics.getEntries()) {
      actualDiagnostics.add(entry.getMessage());
    }

    assertThat(actualDiagnostics).containsExactlyInAnyOrder(expectedDiagnostics.toArray());
  }
}