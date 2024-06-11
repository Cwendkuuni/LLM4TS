import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompilerTestCase;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SortedDependencies.CircularDependencyException;
import com.google.javascript.jscomp.testing.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class VarCheckTest extends AbstractCompilerTestCase {

  private VarCheck varCheckPass;
  private Compiler compiler;

  @Override
  protected CompilerPass getProcessor(Compiler compiler) {
    return varCheckPass;
  }

  @Before
  public void setUp() throws Exception {
    varCheckPass = new VarCheck(compiler);
    compiler = createCompiler();
  }

  @Test
  public void testUndefinedVarError() throws Exception {
    test(
        "function foo() { return bar; }",
        VarCheck.UNDEFINED_VAR_ERROR);
  }

  @Test
  public void testViolatedModuleDepError() throws Exception {
    List<SourceFile> inputs = ImmutableList.of(
        SourceFile.fromCode("input1", "var x;"),
        SourceFile.fromCode("input2", "var y = x;"));
    test(
        inputs,
        VarCheck.VIOLATED_MODULE_DEP_ERROR);
  }

  @Test
  public void testMissingModuleDepWarning() throws Exception {
    List<SourceFile> inputs = ImmutableList.of(
        SourceFile.fromCode("input1", "var x;"),
        SourceFile.fromCode("input2", "var y = x;"));
    test(
        inputs,
        VarCheck.MISSING_MODULE_DEP_WARNING);
  }

  @Test
  public void testStrictModuleDepError() throws Exception {
    List<SourceFile> inputs = ImmutableList.of(
        SourceFile.fromCode("input1", "var x;"),
        SourceFile.fromCode("input2", "var y = x;"));
    test(
        inputs,
        VarCheck.STRICT_MODULE_DEP_ERROR);
  }

  @Test
  public void testNameReferenceInExternsWarning() throws Exception {
    test(
        "function foo() { bar(); }",
        VarCheck.NAME_REFERENCE_IN_EXTERNS_WARNING);
  }

  @Test
  public void testUndefinedExternVarWarning() throws Exception {
    test(
        "externVar.prop;",
        VarCheck.UNDEFINED_EXTERN_VAR_WARNING);
  }

  @Test
  public void testInvalidFunctionDeclError() throws Exception {
    test("function() {}", VarCheck.INVALID_FUNCTION_DECL);
  }

  private void test(String input, DiagnosticType diagnostic) {
    test(CompilerTestCase.createSourceFiles(),
        CompilerTestCase.createModules(input),
        diagnostic);
  }

  private void test(List<SourceFile> inputs, DiagnosticType diagnostic) {
    test(inputs, Collections.emptyList(), diagnostic);
  }

  private void test(List<SourceFile> inputs, List<String> externs, DiagnosticType diagnostic) {
    try {
      testExterns(externs);

      test(inputs, diagnostic, createWarningLevel(diagnostic));

      if (diagnostic.equals(VarCheck.UNDEFINED_EXTERN_VAR_ERROR)) {
        assertEquals(externs.size(), lastCompiler.getWarnings().size());
        return;
      }

      assertEquals(1, lastCompiler.getWarnings().size());
      assertEquals(diagnostic, lastCompiler.getWarnings().get(0).getType());
    } catch (IOException | IllegalArgumentException | RuntimeException e) {
      throw new RuntimeException("Untested " + diagnostic
          + " with inputs: " + inputs);
    }
  }

  private void testExterns(List<String> externs) {
    if (!externs.isEmpty()) {
      externs = externs.stream()
          .map(extern -> String.format("var %s;", extern))
          .collect(Collectors.toList());
      compiler.compile(CompilerTestCase.createSourceFiles(),
          CompilerTestCase.createSourceModules(externs));
    }
  }

  private CheckLevel createWarningLevel(DiagnosticType diagnostic) {
    return diagnostic.equals(VarCheck.MISSING_MODULE_DEP_WARNING)
        || diagnostic.equals(VarCheck.NAME_REFERENCE_IN_EXTERNS_WARNING)
        || diagnostic.equals(VarCheck.UNDEFINED_EXTERN_VAR_WARNING)
        ? CheckLevel.WARNING
        : CheckLevel.ERROR;
  }

}