import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Token;

import java.util.Set;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private CompilerInput input;
  private Node root;
  private VarCheck varCheck;
  private Scope scope;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    passManager = mock(CompilerPassManager.class);
    input = mock(CompilerInput.class);
    root = mock(Node.class);
    scope = mock(Scope.class);

    when(compiler.newCompilerPassManager()).thenReturn(passManager);
    when(passManager.createPass(VarCheck.class)).thenReturn(varCheck = new VarCheck(compiler));
    when(input.getAstRoot(compiler)).thenReturn(root);
  }

  @org.junit.Test
  public void testUndefinedVarError() {
    when(scope.getVar("foo")).thenReturn(null);

    Node undeclaredVar = Node.newName("foo");

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, input, root);
    varCheck.process(input, root);
    nodeTraversal.traverse(input, root, varCheck);

    verify(compiler).report(eq(input), eq(undeclaredVar),
        eq(UNDEFINED_VAR_ERROR), anyString());
  }

  @org.junit.Test
  public void testDuplicateVarDeclaration() {
    String varName = "foo";
    Node firstVarDeclaration = Node.newName(varName);
    Node secondVarDeclaration = Node.newName(varName);

    when(scope.getVar(varName)).thenReturn(null);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, input, root);
    varCheck.process(input, root);
    nodeTraversal.traverse(input, root, varCheck);

    varCheck.visit(mock(NodeTraversal.class), firstVarDeclaration, root);
    varCheck.visit(mock(NodeTraversal.class), secondVarDeclaration, root);

    verify(scope, times(1)).declare(eq(varName), eq(firstVarDeclaration),
        any(), any());

    verify(firstVarDeclaration, times(1)).addSuppression("duplicate");
  }

  @org.junit.Test
  public void testInvalidFunctionDeclaration() {
    Node invalidFunctionDeclaration = Node.newFunctionDeclaration(null, null, null, null);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, input, root);
    varCheck.process(input, root);
    nodeTraversal.traverse(input, root, varCheck);

    verify(input, times(1)).report(eq(invalidFunctionDeclaration),
        eq(INVALID_FUNCTION_DECL));
  }

  @org.junit.Test
  public void testViolatedModuleDependency() {
    String moduleName1 = "module1";
    String moduleName2 = "module2";
    String varName = "foo";

    JSModule module1 = mock(JSModule.class);
    JSModule module2 = mock(JSModule.class);
    when(input.getModule()).thenReturn(module1);
    when(module1.getName()).thenReturn(moduleName1);
    when(compiler.getModuleGraph()).thenReturn(mock(JSModuleGraph.class));
    when(moduleGraph().dependsOn(module1, module2)).thenReturn(true);
    when(input.getInputFileName()).thenReturn("module1.js");

    Node varNode = Node.newName(varName);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, input, root);
    varCheck.process(input, root);
    nodeTraversal.traverse(input, root, varCheck);

    verify(input, times(1)).report(eq(varNode),
        eq(VIOLATED_MODULE_DEP_ERROR), eq(moduleName1), eq(moduleName2), eq(varName));
  }

  @org.junit.Test
  public void testMissingModuleDependency() {
    String moduleName1 = "module1";
    String moduleName2 = "module2";
    String varName = "foo";

    JSModule module1 = mock(JSModule.class);
    JSModule module2 = mock(JSModule.class);
    when(input.getModule()).thenReturn(module1);
    when(module1.getName()).thenReturn(moduleName1);
    when(module2.getName()).thenReturn(moduleName2);
    when(compiler.getModuleGraph()).thenReturn(mock(JSModuleGraph.class));
    when(moduleGraph().dependsOn(module2, module1)).thenReturn(false);

    Node varNode = Node.newName(varName);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, input, root);
    varCheck.process(input, root);
    nodeTraversal.traverse(input, root, varCheck);

    verify(input, times(1)).report(eq(varNode),
        eq(MISSING_MODULE_DEP_ERROR), eq(moduleName1), eq(moduleName2), eq(varName));
  }

  @org.junit.Test
  public void testStrictModuleDependencyError() {
    String moduleName1 = "module1";
    String moduleName2 = "module2";
    String varName = "foo";

    JSModule module1 = mock(JSModule.class);
    JSModule module2 = mock(JSModule.class);
    when(input.getModule()).thenReturn(module1);
    when(module1.getName()).thenReturn(moduleName1);
    when(module2.getName()).thenReturn(moduleName2);
    when(compiler.getModuleGraph()).thenReturn(mock(JSModuleGraph.class));
    when(moduleGraph().dependsOn(module2, module1)).thenReturn(false);

    Node varNode = Node.newName(varName);

    varCheck = spy(new VarCheck(compiler, true));

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, input, root);
    varCheck.process(input, root);
    nodeTraversal.traverse(input, root, varCheck);

    verify(input, times(1)).report(eq(varNode), eq(STRICT_MODULE_DEP_ERROR),
        eq(moduleName1), eq(moduleName2), eq(varName));
  }

  @org.junit.Test
  public void testNameReferenceInExternsError() {
    String varName = "foo";

    Node nameNode = Node.newName(varName);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, input, root);
    varCheck.process(input, root);
    nodeTraversal.traverse(input, root, varCheck);

    verify(input, times(1)).report(eq(nameNode),
        eq(NAME_REFERENCE_IN_EXTERNS_ERROR), eq(varName));
  }

  @org.junit.Test
  public void testUndefinedExternVarError() {
    String varName = "foo";

    when(scope.getVar(varName)).thenReturn(null);

    Node nameNode = Node.newName(varName);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, input, root);
    varCheck.process(input, root);
    nodeTraversal.traverse(input, root, varCheck);

    if (!varCheck.strictExternCheck) {
      verify(input, times(1)).report(eq(nameNode),
          eq(UNDEFINED_EXTERN_VAR_ERROR), eq(varName));
    }
  }

  private JSModuleGraph moduleGraph() {
    return mock(JSModuleGraph.class);
  }

  @org.junit.Test
  public void testCreateSynthesizedExternVar() {
    String varName = "foo";

    Node nameNode = Node.newName(varName);

    varCheck.createSynthesizedExternVar(varName);

    assertTrue(varCheck.getSynthesizedExternsRoot().hasChild(nameNode));
  }

  @org.junit.Test
  public void testNoExternsCheckDuringSanityCheck() {
    Node externs = Node.newEmptyStatementList();

    Node rootNode = Node.newEmptyStatementList();

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, input, root);
    varCheck = spy(new VarCheck(compiler, true));

    varCheck.process(input, externs);
    nodeTraversal.traverseRoots(compiler, Lists.newArrayList(externs, root), varCheck);

    verify(input, never()).report(anyNode(), any(DiagnosticType.class), anyString());
  }

  @org.junit.Test
  public void testExternsCheckDuringSanityCheck() {
    String varName = "foo";

    Node externsNode = Node.newName(varName);
    Node rootNode = new Node(Token.BLOCK, null);
    rootNode.addChildToBack(externsNode);

    Node nameNode = Node.newName(varName);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, input, root);
    varCheck = spy(new VarCheck(compiler, true));

    varCheck.process(input, root);
    nodeTraversal.traverseRoots(compiler, Lists.newArrayList(externs, root), varCheck);

    verify(input, times(1)).report(eq(nameNode),
        eq(UNDEFINED_EXTERN_VAR_ERROR), eq(varName));
  }

  @org.junit.Test
  public void testCreateSynthesizedExternsInput() {
    String varName = "foo";

    Node nameNode = Node.newName(varName);

    varCheck.getSynthesizedExternsInput();

    assertFalse(varCheck.getSynthesizedExternsInput().getAstRoot(compiler)
        .getChildCount() > 0);

    varCheck.createSynthesizedExternVar(varName);

    assertTrue(varCheck.getSynthesizedExternsInput().getAstRoot(compiler)
        .hasChild(nameNode));
  }

  @org.junit.Test
  public void testCreateSynthesizedExternsRoot() {
    String varName = "foo";

    Node nameNode = Node.newName(varName);

    varCheck.getSynthesizedExternsRoot();

    assertFalse(varCheck.getSynthesizedExternsRoot().hasChild(nameNode));

    varCheck.createSynthesizedExternVar(varName);

    assertTrue(varCheck.getSynthesizedExternsRoot().hasChild(nameNode));
  }
}