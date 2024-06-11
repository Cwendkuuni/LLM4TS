package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private VarCheck varCheck;
  private Node root;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    varCheck = new VarCheck(compiler);
    root = Node.objectLiteral();
  }

  @Test
  public void testProcess_EmptyExternsAndRoot_NoErrors() {
    Node externs = Node.objectLiteral();
    varCheck.process(externs, root);
    assertNull(varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
  }

  @Test
  public void testProcess_ValidExternsAndRoot_NoErrors() {
    Node externs = Node.objectLiteral(
        Node.newProperty("foo", Node.newString("42")),
        Node.newProperty("bar", Node.newNumber(42.0))
    );
    varCheck.process(externs, root);
    assertNull(varCheck.getSynthesizedExternsInput());
    assertNull(varCheck.getSynthesizedExternsRoot());
  }

  @Test
  public void testProcess_UndefinedVarInExterns_DeclaredInCode() {
    String varName = "foo";
    Node externs = Node.objectLiteral(Node.newProperty(varName, Node.newString("42")));
    Node code = Node.newVariable("x", Node.newNumber(42.0));
    Node parent = Node.newVariable("y", code);
    root = Node.newVariable("z", parent);

    varCheck.process(externs, root);
    ArgumentCaptor<String> varNameCaptor = ArgumentCaptor.forClass(String.class);
    varCheck.varsToDeclareInExterns.remove(varName);
    varCheck.createSynthesizedExternVar(varName);
    assertSame(varName, varNameCaptor.getValue());
  }

  @Test(expected = IllegalStateException.class)
  public void testProcess_UndefinedVarInCode_SanityCheck() {
    String varName = "foo";
    varCheck = new VarCheck(compiler, true);
    Node root = Node.newVariable("x", Node.newString(varName));
    varCheck.process(Node.objectLiteral(), root);
  }

  @Test
  public void testVisit_UndefinedVarInCode_ErrorReported() {
    String varName = "foo";
    Node parent = Node.newVariable("x", Node.newString(varName));
    root = Node.newVariable("y", parent);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.newTraversal(root)).thenReturn(traversal);

    varCheck.visit(traversal, Node.newVariable("z", Node.newString(varName)), root);
    traversal.report(ArgumentCaptor.forClass(Node.class).capture());
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    assertSame(UNDEFINED_VAR_ERROR, diagnosticTypeCaptor.getValue());
  }

  @Test
  public void testVisit_DuplicateVarDeclarations_ErrorReported() {
    String varName = "foo";
    Node parent = Node.newVariable("x", Node.newVariable("y", Node.newString(varName)));
    root = Node.newVariable("z", parent);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.newTraversal(root)).thenReturn(traversal);

    varCheck.visit(traversal, Node.newVariable("w", Node.newString(varName)), root);
    traversal.report(ArgumentCaptor.forClass(Node.class).capture());
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    assertSame(UNDEFINED_VAR_ERROR, diagnosticTypeCaptor.getValue());
  }

  @Test
  public void testVisit_UndefinedVarCrossModuleBoundaries_ErrorReported() {
    String moduleAName = "ModuleA";
    String moduleBName = "ModuleB";
    String varName = "foo";

    Node moduleAInput = Node.objectLiteral();
    Node moduleAExterns = Node.newVariable("foo", Node.newString(varName));
    Node moduleARoot = Node.newVariable("x", moduleAExterns);

    Node moduleBInput = Node.objectLiteral();
    Node moduleBExterns = Node.newObject();
    Node moduleBRoot = Node.newVariable("y", Node.newVariable("z", Node.newString(varName)));

    JSModule moduleA = mock(JSModule.class);
    when(moduleA.getName()).thenReturn(moduleAName);
    when(compiler.getModuleGraph().getModule(moduleAName)).thenReturn(moduleA);
    when(compiler.getModuleGraph().getModule(moduleBName)).thenReturn(mock(JSModule.class));

    Node moduleAGraph = Node.objectLiteral();
    when(moduleA.getAstRoot(compiler)).thenReturn(moduleAGraph);

    Node moduleBGraph = Node.objectLiteral();
    when(compiler.getModuleGraph().getDependencies(moduleBName)).thenReturn(Sets.newHashSet(moduleAName));

    when(compiler.newExternInput(ArgumentCaptor.forClass(String.class).capture())).thenReturn(
        Node.objectLiteral()
    );

    varCheck = new VarCheck(compiler);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.newTraversal(root)).thenReturn(traversal);

    varCheck.process(moduleBExterns, moduleBRoot);

    Node moduleA = Node.newObject();
    Node moduleAExterns = Node.newObject();
    Node moduleARoot = Node.newVariable("x", Node.newString(moduleAName));
    when(compiler.newTraversal(moduleARoot)).thenReturn(traversal);
    when(compiler.getModuleGraph().getModule(moduleAName)).thenReturn(moduleA);
    when(moduleA.getAstRoot(compiler)).thenReturn(moduleAGraph);

    varCheck.visit(traversal, Node.newVariable("w", Node.newString(varName)), root);
    traversal.report(ArgumentCaptor.forClass(Node.class).capture());
    ArgumentCaptor<DiagnosticType> diagnosticTypeCaptor =
        ArgumentCaptor.forClass(DiagnosticType.class);
    assertSame(VIOLATED_MODULE_DEP_ERROR, diagnosticTypeCaptor.getValue());
  }

  private static final DiagnosticType UNDEFINED_VAR_ERROR =
      VarCheck.UNDEFINED_VAR_ERROR;
}