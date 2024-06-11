import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilationTestBase;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.testing.TopLevelTestRunner;

public class ScopedAliasesTest extends CompilationTestBase {

  @Test
  public void testScopedAliases() throws Exception {
    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var dom = goog.dom; " +
        "  var DIV = dom.TagName.DIV; " +
        "  goog.scope(function() {" +
        "    var DIV2 = dom.TagName.DIV; " +
        "    assert(DIV !== DIV2); " +
        "  }); " +
        "  dom.createElement(DIV); " +
        "});";

    Compiler compiler = newCompiler(new SourceFile("Test.java", sourceCode));
    compiler.addPass(new HotSwapCompilerPass.ScopedAliases(
        compiler, null, null));

    compileWithOptions(compiler, new String[]{"--warn_unused_require"});

    TopLevelTestRunner runner = new TopLevelTestRunner(getCompiler().getJsOutputFile(), getCompiler().getDiagnostics());
    runner.runTests();
  }

  @Test
  public void testImproperlyUsedGoogScope() throws Exception {
    String sourceCode = "" +
        "goog.scope(function() {" +
        "  goog.scope(function() {" +
        "  }); " +
        "});";

    Compiler compiler = newCompiler(new SourceFile("Test.java", sourceCode));
    compiler.addPass(new HotSwapCompilerPass.ScopedAliases(
        compiler, null, null));

    compileWithOptions(compiler, new String[]{"--warn_unused_require"});

    TopLevelTestRunner runner = new TopLevelTestRunner(getCompiler().getJsOutputFile(), getCompiler().getDiagnostics());
    runner.runTests();

    assertEquals("Expected 1 errors", 1, getCompiler().getDiagnostics().size());
    DiagnosticInfo diagnosticInfo = getCompiler().getDiagnostics().get(0);
    assertEquals(GOOG_SCOPE_USED_IMPROPERLY.getExternalName(), diagnosticInfo.getCode());
  }

  @Test
  public void testGoogScopeWithBadParameters() throws Exception {
    String sourceCode = "" +
        "goog.scope(function() {" +
        "  function foo() {} " +
        "  goog.scope(foo); " +
        "});";

    Compiler compiler = newCompiler(new SourceFile("Test.java", sourceCode));
    compiler.addPass(new HotSwapCompilerPass.ScopedAliases(
        compiler, null, null));

    compileWithOptions(compiler, new String[]{"--warn_unused_require"});

    TopLevelTestRunner runner = new TopLevelTestRunner(getCompiler().getJsOutputFile(), getCompiler().getDiagnostics());
    runner.runTests();

    assertEquals("Expected 1 errors", 1, getCompiler().getDiagnostics().size());
    DiagnosticInfo diagnosticInfo = getCompiler().getDiagnostics().get(0);
    assertEquals(GOOG_SCOPE_HAS_BAD_PARAMETERS.getExternalName(), diagnosticInfo.getCode());
  }

  @Test
  public void testGoogScopeRefersToThis() throws Exception {
    String sourceCode = "" +
        "goog.scope(function() {" +
        "  function foo() {" +
        "    return this; " +
        "  } " +
        "  goog.scope(foo); " +
        "});";

    Compiler compiler = newCompiler(new SourceFile("Test.java", sourceCode));
    compiler.addPass(new HotSwapCompilerPass.ScopedAliases(
        compiler, null, null));

    compileWithOptions(compiler, new String[]{"--warn_unused_require"});

    TopLevelTestRunner runner = new TopLevelTestRunner(getCompiler().getJsOutputFile(), getCompiler().getDiagnostics());
    runner.runTests();

    assertEquals("Expected 1 errors", 1, getCompiler().getDiagnostics().size());
    DiagnosticInfo diagnosticInfo = getCompiler().getDiagnostics().get(0);
    assertEquals(GOOG_SCOPE_REFERENCES_THIS.getExternalName(), diagnosticInfo.getCode());
  }

  @Test
  public void testGoogScopeUsesReturn() throws Exception {
    String sourceCode = "" +
        "goog.scope(function() {" +
        "  function foo() {" +
        "    return; " +
        "  } " +
        "  goog.scope(foo); " +
        "});";

    Compiler compiler = newCompiler(new SourceFile("Test.java", sourceCode));
    compiler.addPass(new HotSwapCompilerPass.ScopedAliases(
        compiler, null, null));

    compileWithOptions(compiler, new String[]{"--warn_unused_require"});

    TopLevelTestRunner runner = new TopLevelTestRunner(getCompiler().getJsOutputFile(), getCompiler().getDiagnostics());
    runner.runTests();

    assertEquals("Expected 1 errors", 1, getCompiler().getDiagnostics().size());
    DiagnosticInfo diagnosticInfo = getCompiler().getDiagnostics().get(0);
    assertEquals(GOOG_SCOPE_USES_RETURN.getExternalName(), diagnosticInfo.getCode());
  }

  @Test
  public void testGoogScopeUsesThrow() throws Exception {
    String sourceCode = "" +
        "goog.scope(function() {" +
        "  function foo() {" +
        "    throw new Error(); " +
        "  } " +
        "  goog.scope(foo); " +
        "});";

    Compiler compiler = newCompiler(new SourceFile("Test.java", sourceCode));
    compiler.addPass(new HotSwapAliases(
        compiler, null, null));

    compileWithOptions(compiler, new String[]{"--warn_unused_require"});

    TopLevelTestRunner runner = new TopLevelTestRunner(getCompiler().getJsOutputFile(), getCompiler().getDiagnostics());
    runner.runTests();

    assertEquals("Expected 1 errors", 1, getCompiler().getDiagnostics().size());
    DiagnosticInfo diagnosticInfo = getCompiler().getDiagnostics().get(0);
    assertEquals(GOOG_SCOPE_USES_THROW.getExternalName(), diagnosticInfo.getCode());
  }

  @Test
  public void testAliasRedefined() throws Exception {
    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var x = 0; " +
        "  var x = 1; " +
        "});";

    Compiler compiler = newCompiler(new SourceFile("Test.java", sourceCode));
    compiler.addPass(new HotSwapCompilerPass.ScopedAliases(
        compiler, null, null));

    compileWithOptions(compiler, new String[]{"--warn_unused_require"});

    TopLevelTestRunner runner = new TopLevelTestRunner(getCompiler().getJsOutputFile(), getCompiler().getDiagnostics());
    runner.runTests();

    assertEquals("Expected 1 errors", 1, getCompiler().getDiagnostics().size());
    DiagnosticInfo diagnosticInfo = getCompiler().getDiagnostics().get(0);
    assertEquals(GOOG_SCOPE_ALIAS_REDEFINED.getExternalName(), diagnosticInfo.getCode());
  }

  @Test
  public void testNonAliasLocal() throws Exception {
    String sourceCode = "" +
        "goog.scope(function() {" +
        "  var x = 0; " +
        "});";

    Compiler compiler = newCompiler(new SourceFile("Test.java", sourceCode));
    compiler.addPass(new HotSwapCompilerPass.ScopedAliases(
        compiler, null, null));

    compileWithOptions(compiler, new String[]{"--warn_unused_require"});

    TopLevelTestRunner runner = new TopLevelTestRunner(getCompiler().getJsOutputFile(), getCompiler().getDiagnostics());
    runner.runTests();

    assertEquals("Expected 1 errors", 1, getCompiler().getDiagnostics().size());
    DiagnosticInfo diagnosticInfo = getCompiler().getDiagnostics().get(0);
    assertEquals(GOOG_SCOPE_NON_ALIAS_LOCAL.getExternalName(), diagnosticInfo.getCode());
  }
}