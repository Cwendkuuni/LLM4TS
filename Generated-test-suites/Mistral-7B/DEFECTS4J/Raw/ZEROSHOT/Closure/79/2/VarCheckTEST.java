import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Token;

import java.util.Set;

public class VarCheckTest {

  @org.junit.Before
  public void setUp() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    VarCheck varCheck = new VarCheck(compiler);
  }

  @org.junit.Test
  public void testUndefinedVarError() {
    VarCheck varCheck = new VarCheck(null);
    Node node = Node.newString(Token.NAME, "undefinedVar");
    varCheck.visit(null, null, null, null, node);
    assertEquals(1, varCheck.compiler.getDiagnosticListener().getErrors().size());
    assertEquals("JSC_UNDEFINED_VARIABLE",
        varCheck.compiler.getDiagnosticListener().getErrors().get(0).getType());
  }

  @org.junit.Test
  public void testViolatedModuleDepError() {
    JSModule moduleA = mock(JSModule.class);
    JSModule moduleB = mock(JSModule.class);
    when(moduleA.getName()).thenReturn("moduleA");
    when(moduleB.getName()).thenReturn("moduleB");

    VarCheck varCheck = new VarCheck(null);
    Node node = Node.newString(Token.NAME, "moduleBVar");

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getScope()).thenReturn(new Scope());
    when(traversal.getInput()).thenReturn(new CompilerInput(moduleA));

    varCheck.visit(traversal, null, null, null, node);
    assertEquals(1, varCheck.compiler.getDiagnosticListener().getErrors().size());
    assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY",
        varCheck.compiler.getDiagnosticListener().getErrors().get(0).getType());
  }

  @org.junit.Test
  public void testMissingModuleDepWarning() {
    JSModule moduleA = mock(JSModule.class);
    JSModule moduleB = mock(JSModule.class);
    when(moduleA.getName()).thenReturn("moduleA");
    when(moduleB.getName()).thenReturn("moduleB");

    VarCheck varCheck = new VarCheck(null);
    Node node = Node.newString(Token.NAME, "moduleBVar");

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getScope()).thenReturn(new Scope());
    when(traversal.getInput()).thenReturn(new CompilerInput(moduleA));

    varCheck.visit(traversal, null, null, null, node);
    assertEquals(1, varCheck.compiler.getDiagnosticListener().getDiagnostics().size());
    assertEquals("JSC_MISSING_MODULE_DEPENDENCY",
        varCheck.compiler.getDiagnosticListener().getDiagnostics().get(0).getType());
  }

  @org.junit.Test
  public void testStrictModuleDepError() {
    JSModule moduleA = mock(JSModule.class);
    JSModule moduleB = mock(JSModule.class);
    when(moduleA.getName()).thenReturn("moduleA");
    when(moduleB.getName()).thenReturn("moduleB");

    VarCheck varCheck = new VarCheck(null, true);
    Node node = Node.newString(Token.NAME, "moduleBVar");

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getScope()).thenReturn(new Scope());
    when(traversal.getInput()).thenReturn(new CompilerInput(moduleB));

    varCheck.visit(traversal, null, null, null, node);
    assertEquals(1, varCheck.compiler.getDiagnosticListener().getDiagnostics().size());
    assertEquals("JSC_STRICT_MODULE_DEPENDENCY",
        varCheck.compiler.getDiagnosticListener().getDiagnostics().get(0).getType());
  }

  @org.junit.Test
  public void testNameReferenceInExternsWarning() {
    VarCheck varCheck = new VarCheck(null);
    Node node = Node.newString(Token.NAME, "nameInExterns");

    varCheck.visit(null, null, null, null, node);
    assertEquals(1, varCheck.compiler.getDiagnosticListener().getDiagnostics().size());
    assertEquals("JSC_NAME_REFERENCE_IN_EXTERNS",
        varCheck.compiler.getDiagnosticListener().getDiagnostics().get(0).getType());
  }

  @org.junit.Test
  public void testUndefinedExternVarWarning() {
    VarCheck varCheck = new VarCheck(null);
    Node node = Node.newString(Token.NAME, "undefinedExternVar");

    varCheck.visit(null, null, null, null, node);
    assertEquals(1, varCheck.compiler.getDiagnosticListener().getDiagnostics().size());
    assertEquals("JSC_UNDEFINED_EXTERN_VAR_ERROR",
        varCheck.compiler.getDiagnosticListener().getDiagnostics().get(0).getType());
  }

  @org.junit.Test
  public void testInvalidFunctionDeclError() {
    VarCheck varCheck = new VarCheck(null);
    Node node = Node.newFunction(Token.FUNCTION, null, null);

    varCheck.visit(null, null, null, null, node);
    assertEquals(1, varCheck.compiler.getDiagnosticListener().getErrors().size());
    assertEquals("JSC_INVALID_FUNCTION_DECL",
        varCheck.compiler.getDiagnosticListener().getErrors().get(0).getType());
  }
}