import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TraversalRootsCallback;
import com.google.javascript.jscomp.TraversalRootsCallback.TraversalRoots;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class VarCheckTest {

  private AbstractCompiler compiler;
  private VarCheck varCheck;
  private Node synthesizedExternsRoot;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    synthesizedExternsRoot = Node.object();
    when(compiler.newExternInput("{SyntheticVarsDeclar}"))
        .thenReturn(new CompilerInput(synthesizedExternsRoot, ""));
    varCheck = new VarCheck(compiler);
  }

  @Test
  public void testEmptyNameFunctionDeclaration() {
    Node root = Node.function("foo", null);
    Node functionNode = Node.newFunction(
        Token.FUNCTION, Node.newList(Node.newName("")), null, null);
    root.addChildToBack(functionNode);

    TraversalRoots traversalRoots = mock(TraversalRoots.class);
    when(traversalRoots.getRoots()).thenReturn(new TraversalRoots[]{root});

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getTraversalRootsCallback()).thenReturn(traversalRoots);

    varCheck.process(null, null);
    varCheck.visit(ArgumentMatchers.eq(compiler), ArgumentMatchers.any(Node.class),
        ArgumentMatchers.any(Node.class));
  }

  @Test
  public void testUndefinedVariable() {
    Node root = Node.object();
    Node varNode = Node.newName("x");
    root.addChildToBack(varNode);

    Scope scope = mock(Scope.class);
    when(compiler.getScope()).thenReturn(scope);

    TraversalRoots traversalRoots = mock(TraversalRoots.class);
    when(traversalRoots.getRoots()).thenReturn(new TraversalRoots[]{root});

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getScope()).thenReturn(scope);
    when(nodeTraversal.getTraversalRootsCallback()).thenReturn(traversalRoots);

    varCheck.process(null, null);
    varCheck.visit(ArgumentMatchers.eq(compiler), ArgumentMatchers.any(Node.class),
        ArgumentMatchers.any(Node.class));

    Scope.Var var = mock(Scope.Var.class);
    when(scope.getVar("x")).thenReturn(var);
    when(var.input).thenReturn(null);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getInput()).thenReturn(null);
    when(traversal.report(ArgumentMatchers.any(Node.class),
        ArgumentMatchers.any(DiagnosticType.class), ArgumentMatchers.any(String.class)))
        .thenCallRealMethod();

    varCheck.visit(ArgumentMatchers.eq(compiler), ArgumentMatchers.any(NodeTraversal.class),
        ArgumentMatchers.eq(Node.class));

    assertTrue(varCheck.hasDiagnostics());
  }

  @Test
  public void testUndefinedVariableWithInput() {
    Node root = Node.object();
    Node varNode = Node.newName("x");
    root.addChildToBack(varNode);

    CompilerInput input = new CompilerInput(Node.object(), "test.js");
    when(compiler.getInput()).thenReturn(input);

    Scope scope = mock(Scope.class);
    when(compiler.getScope()).thenReturn(scope);

    TraversalRoots traversalRoots = mock(TraversalRoots.class);
    when(traversalRoots.getRoots()).thenReturn(new TraversalRoots[]{root});

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getScope()).thenReturn(scope);
    when(nodeTraversal.getTraversalRootsCallback()).thenReturn(traversalRoots);

    varCheck.process(null, null);
    varCheck.visit(ArgumentMatchers.eq(compiler), ArgumentMatchers.any(Node.class),
        ArgumentMatchers.any(Node.class));

    Scope.Var var = mock(Scope.Var.class);
    when(scope.getVar("x")).thenReturn(var);
    when(var.input).thenReturn(input);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getInput()).thenReturn(input);
    when(traversal.report(ArgumentMatchers.any(Node.class),
        ArgumentMatchers.any(DiagnosticType.class), ArgumentMatchers.any(String.class)))
        .thenCallRealMethod();

    varCheck.visit(ArgumentMatchers.eq(compiler), ArgumentMatchers.any(NodeTraversal.class),
        ArgumentMatchers.eq(Node.class));

    assertFalse(varCheck.hasDiagnostics());
  }

  @Test
  public void testMissingModuleDependency() {
    Node root = Node.object();
    Node varNode = Node.newName("x");
    root.addChildToBack(varNode);

    CompilerInput input1 = new CompilerInput(Node.object(), "module1.js");
    CompilerInput input2 = new CompilerInput(Node.object(), "module2.js");

    when(compiler.getInput()).thenReturn(input1);
    when(compiler.getExternInputForFile("module2.js")).thenReturn(input2);

    Scope scope = mock(Scope.class);
    when(compiler.getScope()).thenReturn(scope);

    TraversalRoots traversalRoots = mock(TraversalRoots.class);
    when(traversalRoots.getRoots()).thenReturn(new TraversalRoots[]{root});

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getScope()).thenReturn(scope);
    when(nodeTraversal.getTraversalRootsCallback()).thenReturn(traversalRoots);

    varCheck.process(null, null);
    varCheck.visit(ArgumentMatchers.eq(compiler), ArgumentMatchers.any(Node.class),
        ArgumentMatchers.any(Node.class));

    JSModule module1 = mock(JSModule.class);
    JSModule module2 = mock(JSModule.class);
    when(input1.getModule()).thenReturn(module1);
    when(input2.getModule()).thenReturn(module2);

    when(compiler.getModuleGraph().dependsOn(module1, module2)).thenReturn(false);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getInput()).thenReturn(input1);
    when(traversal.report(ArgumentMatchers.any(Node.class),
        ArgumentMatchers.any(DiagnosticType.class), ArgumentMatchers.any(String.class)))
        .thenCallRealMethod();

    varCheck.visit(ArgumentMatchers.eq(compiler), ArgumentMatchers.any(NodeTraversal.class),
        ArgumentMatchers.eq(Node.class));

    assertFalse(varCheck.hasDiagnostics());

    when(compiler.getModuleGraph().dependsOn(module2, module1)).thenReturn(true);

    varCheck.visit(ArgumentMatchers.eq(compiler), ArgumentMatchers.any(NodeTraversal.class),
        ArgumentMatchers.eq(Node.class));

    assertTrue(varCheck.hasDiagnostics());
  }

  @Test
  public void testStrictModuleDependency() {
    Node root = Node.object();
    Node varNode = Node.newName("x");
    root.addChildToBack(varNode);

    CompilerInput input1 = new CompilerInput(Node.object(), "module1.js");
    CompilerInput input2 = new CompilerInput(Node.object(), "module2.js");

    when(compiler.getInput()).thenReturn(input1);
    when(compiler.getExternInputForFile("module2.js")).thenReturn(input2);

    Scope scope = mock(Scope.class);
    when(compiler.getScope()).thenReturn(scope);

    TraversalRoots traversalRoots = mock(TraversalRoots.class);
    when(traversalRoots.getRoots()).thenReturn(new TraversalRoots[]{root});

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getScope()).thenReturn(scope);
    when(nodeTraversal.getTraversalRootsCallback()).thenReturn(traversalRoots);

    varCheck = new VarCheck(compiler, true);

    varCheck.process(null, null);
    varCheck.visit(ArgumentMatchers.eq(compiler), ArgumentMatchers.any(Node.class),
        ArgumentMatchers.any(Node.class));

    JSModule module1 = mock(JSModule.class);
    JSModule module2 = mock(JSModule.class);
    when(input1.getModule()).thenReturn(module1);
    when(input2.getModule()).thenReturn(module2);

    when(compiler.getModuleGraph().dependsOn(module1, module2)).thenReturn(false);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getInput()).thenReturn(input1);
    when(traversal.report(ArgumentMatchers.any(Node.class),
        ArgumentMatchers.any(DiagnosticType.class), ArgumentMatchers.any(String.class)))
        .thenCallRealMethod();

    varCheck.visit(ArgumentMatchers.eq(compiler), ArgumentMatchers.any(NodeTraversal.class),
        ArgumentMatchers.eq(Node.class));

    assertFalse(varCheck.hasDiagnostics());

    when(compiler.getModuleGraph().dependsOn(module2, module1)).thenReturn(true);

    varCheck.visit(ArgumentMatchers.eq(compiler), ArgumentMatchers.any(NodeTraversal.class),
        ArgumentMatchers.eq(Node.class));

    assertNull(varCheck.getLastDiagnostic());
  }

  @Test
  public void testUndefinedExternalVariable() {
    Node root = Node.object();
    Node varNode = Node.newName("x");
    root.addChildToBack(varNode);

    CompilerInput input = new CompilerInput(Node.object(), "test.js");
    when(compiler.getInput()).thenReturn(input);

    Scope scope = mock(Scope.class);
    when(compiler.getScope()).thenReturn(scope);

    TraversalRoots traversalRoots = mock(TraversalRoots.class);
    when(traversalRoots.getRoots()).thenReturn(new TraversalRoots[]{root});

    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    when(nodeTraversal.getScope()).thenReturn(scope);
    when(nodeTraversal.getTraversalRootsCallback()).thenReturn(traversalRoots);

    varCheck.process(null, null);
    varCheck.visit(ArgumentMatchers.eq(compiler), ArgumentMatchers.any(Node.class),
        ArgumentMatchers.any(Node.class));

    Scope.Var var = mock(Scope.Var.class);
    when(scope.getVar("x")).thenReturn(var);
    when(var.input).thenReturn(null);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getInput()).thenReturn(input);
    when(traversal.report(ArgumentMatchers.any(Node.class),
        ArgumentMatchers.any(DiagnosticType.class), ArgumentMatchers.any(String.class)))
        .thenCallRealMethod();

    varCheck.visit(ArgumentMatchers.eq(compiler), ArgumentMatchers.any(NodeTraversal.class),
        ArgumentMatchers.eq(Node.class));

    assertTrue(varCheck.hasDiagnostics());

    when(var.input).thenReturn(input);

    varCheck.visit(ArgumentMatchers.eq(compiler), ArgumentMatchers.any(NodeTraversal.class),
        ArgumentMatchers.eq(Node.class));

    assertFalse(varCheck.hasDiagnostics());
  }
}