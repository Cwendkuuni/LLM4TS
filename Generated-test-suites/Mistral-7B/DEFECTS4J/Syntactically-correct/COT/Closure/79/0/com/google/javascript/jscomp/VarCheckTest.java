import static com.google.javascript.jscomp.DiagnosticType.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore({"com.google.common.base.*", "com.google.javascript.rhino.*"})
public class VarCheckTest {

  private AbstractCompiler compiler;
  private VarCheck varCheck;

  @Before
  public void setUp() {
    compiler = PowerMockito.mock(AbstractCompiler.class);
    varCheck = new VarCheck(compiler);
  }

  @Test
  public void testProcessExterns_noErrors() {
    Node externs = Node.newText("var a = 1;");
    Node root = Node.newText("var b = a;");

    varCheck.process(externs, root);

    assertNotNull(varCheck.getSynthesizedExternsInput());
    assertNotNull(varCheck.getSynthesizedExternsRoot());

    ImmutableList<DiagnosticType> diagnostics = varCheck.getDiagnostics();
    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testProcessExterns_undefinedVariable() {
    Node externs = Node.newText("var a = 1;");
    Node root = Node.newText("var b = a;"); // 'b' is not defined in externs

    varCheck.process(externs, root);

    ImmutableList<DiagnosticType> diagnostics = varCheck.getDiagnostics();
    assertFalse(diagnostics.isEmpty());
    assertTrue(diagnostics.get(0) instanceof UNDEFINED_VAR_ERROR);
  }

  @Test
  public void testProcessExterns_missingModuleDependency() {
    Node externsA = Node.newText("var a = 1;");
    Node rootA = Node.newText("var b = a;");

    Node externsB = Node.newText("var b = 1;");
    Node rootB = Node.newText("var a = b;");

    varCheck.process(externsA, rootA);
    varCheck.process(externsB, rootB);

    ImmutableList<DiagnosticType> diagnostics = varCheck.getDiagnostics();
    ImmutableList<DiagnosticType> expectedDiagnostics = ImmutableList.of(
        new MISSING_MODULE_DEP_ERROR("ModuleA", "ModuleB", "a")
    );

    assertTrue(diagnostics.equals(expectedDiagnostics));
  }

  @Test
  public void testProcessExterns_stricModuleDependency() {
    Node externsA = Node.newText("var a = 1;");
    Node rootA = Node.newText("var b = a;"); // 'b' is defined in ModuleB

    Node externsB = Node.newText("var b = 1;");
    Node rootB = Node.newText("var a = b;");

    varCheck = new VarCheck(compiler, true);

    varCheck.process(externsA, rootA);
    varCheck.process(externsB, rootB);

    ImmutableList<DiagnosticType> diagnostics = varCheck.getDiagnostics();
    ImmutableList<DiagnosticType> expectedDiagnostics = ImmutableList.of(
        new STRICT_MODULE_DEP_ERROR("ModuleA", "ModuleB", "a")
    );

    assertTrue(diagnostics.equals(expectedDiagnostics));
  }

  @Test
  public void testProcess_undefinedVariable() {
    Node root = Node.newText("var b = a;"); // 'a' is not defined in the file

    varCheck.process(null, root);

    ImmutableList<DiagnosticType> diagnostics = varCheck.getDiagnostics();
    assertTrue(diagnostics.size() > 0);
    assertTrue(diagnostics.get(0) instanceof UNDEFINED_VAR_ERROR);
  }

  @Test
  public void testProcess_functionDeclarationWithEmptyName() {
    Node root = Node.newText("function(){} var a = 1; function foo(){}");

    varCheck.process(null, root);

    ImmutableList<DiagnosticType> diagnostics = varCheck.getDiagnostics();
    ImmutableList<DiagnosticType> expectedDiagnostics = ImmutableList.of(
        new INVALID_FUNCTION_DECL()
    );

    assertTrue(diagnostics.equals(expectedDiagnostics));
  }

  @Test
  public void testProcess_variableWithSameNameInDifferentScopes() {
    Node root = Node.newText(
        "function A(){" +
        "  var a = 1;" +
        "}" +
        "function B(){" +
        "  var a = 2;" +
        "}"
    );

    varCheck.process(null, root);

    ImmutableList<DiagnosticType> diagnostics = varCheck.getDiagnostics();
    ImmutableList<DiagnosticType> expectedDiagnostics = ImmutableList.of(
        new UNDEFINED_VAR_ERROR("a"),
        new UNDEFINED_VAR_ERROR("a")
    );

    assertTrue(diagnostics.size() == 2);
    assertTrue(diagnostics.get(0).getMessage().contains("FunctionA"));
    assertTrue(diagnostics.get(1).getMessage().contains("FunctionB"));
  }

  @Test
  public void testProcess_variableWithSameNameInSameScope() {
    Node root = Node.newText(
        "function A(){" +
        "  var a = 1;" +
        "  var a = 2;" +
        "}"
    );

    varCheck.process(null, root);

    ImmutableList<DiagnosticType> diagnostics = varCheck.getDiagnostics();
    ImmutableList<DiagnosticType> expectedDiagnostics = ImmutableList.of();

    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testProcess_variableWithSameNameInParentChildRelationship() {
    Node root = Node.newText(
        "function A(){" +
        "  var a = 1;" +
        "  function B(){" +
        "    var a = 2;" +
        "  }" +
        "}"
    );

    varCheck.process(null, root);

    ImmutableList<DiagnosticType> diagnostics = varCheck.getDiagnostics();
    ImmutableList<DiagnosticType> expectedDiagnostics = ImmutableList.of();

    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testProcess_globalVariable() {
    Node root = Node.newText("var a = 1;");

    varCheck.process(null, root);

    ImmutableList<DiagnosticType> diagnostics = varCheck.getDiagnostics();
    ImmutableList<DiagnosticType> expectedDiagnostics = ImmutableList.of();

    assertTrue(diagnostics.isEmpty());
  }

  @Test
  public void testProcess_duplicateVariableDeclaration() {
    Node root = Node.newText(
        "function A(){" +
        "  var a = 1;" +
        "  var a = 2;" +
        "}"
    );

    varCheck.process(null, root);

    ImmutableList<DiagnosticType> diagnostics = varCheck.getDiagnostics();
    ImmutableList<DiagnosticType> expectedDiagnostics = ImmutableList.of(
        new UNDEFINED_VAR_ERROR("a")
    );

    assertTrue(diagnostics.size() == 1);
    assertTrue(diagnostics.get(0).getMessage().contains("FunctionA"));
  }
}