import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {
  private AbstractCompiler compiler;
  private VarCheck varCheck;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = NodeFactory.getInitial();
    varCheck = new VarCheck(compiler);
  }

  @Test
  public void testProcessExternsEmpty() {
    Node externs = Node.NULL();
    Node root = Node.NULL();
    varCheck.process(externs, root);
    // Nothing should happen since we're not running the checks in sanity mode.
    assertNull(varCheck.synthesizedExternsInput);
    assertNull(varCheck.synthesizedExternsRoot);
  }

  @Test
  public void testProcessExternsWithVars() {
    String[] varNames = {"foo", "bar"};
    Node externs = Node.newObject(Token.OBJECT, Lists.newArrayList(
        Node.newProperty(Token.IDENT, "foo"),
        Node.newProperty(Token.IDENT, "bar")
    ));
    Node root = Node.NULL();
    when(compiler.newExternInput("{SyntheticVarsDeclar}"))
        .thenReturn(new DependentCompilerInput(externs, null));

    varCheck.process(externs, root);
    CompilerInput synthesizedExterns = varCheck.getSynthesizedExternsInput();
    Node synthesizedExternsRoot = varCheck.getSynthesizedExternsRoot();

    assertTrue(synthesizedExterns != null);
    assertTrue(synthesizedExternsRoot != null);

    for (String varName : varNames) {
      Node varNode = synthesizedExternsRoot.getFirstChild();
      for (int i = 0; i < varNames.length; i++) {
        if (varNode.getString().equals(varName)) {
          assertTrue(varNode.getFirstChild().getType() == Token.VAR);
          break;
        }
        varNode = varNode.getNextSibling();
      }
    }
  }

  @Test
  public void testVisitUndefinedVar() {
    String varName = "foo";
    Node parent = NodeFactory.createVarStatement("", Lists.newArrayList(
        NodeFactory.createVar("foo")
    ));
    Node n = NodeFactory.createName("foo");

    Scope scope = NodeTraversal.createScope(null, null);
    when(compiler.getScope()).thenReturn(scope);

    NodeTraversal traversal = NodeTraversal.createTraversal(compiler, Node.NULL(), parent);
    varCheck.visit(traversal, n, parent);

    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
    assertTrue(scope.getVar(varName) == null);
  }

  @Test
  public void testVisitDeclaredVar() {
    String varName = "foo";
    Node parent = NodeFactory.createVarStatement("", Lists.newArrayList(
        NodeFactory.createVar("foo")
    ));
    Node n = NodeFactory.createName("foo");

    Scope scope = NodeTraversal.createScope(null, null);
    Scope.Var var = NodeTraversal.createVar(varName, NodeFactory.createName("foo"), null);
    scope.setVar(varName, var);

    when(compiler.getScope()).thenReturn(scope);

    NodeTraversal traversal = NodeTraversal.createTraversal(compiler, Node.NULL(), parent);
    varCheck.visit(traversal, n, parent);

    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
    assertTrue(scope.getVar(varName) != null);
  }

  @Test
  public void testVisitDuplicateVar() {
    String varName = "foo";
    Node parent1 = NodeFactory.createVarStatement("", Lists.newArrayList(
        NodeFactory.createVar("foo")
    ));
    Node parent2 = NodeFactory.createVarStatement("", Lists.newArrayList(
        NodeFactory.createVar("foo")
    ));
    Node n = NodeFactory.createName("foo");

    Scope scope = NodeTraversal.createScope(null, null);
    when(compiler.getScope()).thenReturn(scope);

    NodeTraversal traversal = NodeTraversal.createTraversal(compiler, Node.NULL(), parent1);
    varCheck.visit(traversal, n, parent1);
    varCheck.visit(traversal, n, parent2);

    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
    assertTrue(scope.getVar(varName) != null);
  }

  @Test
  public void testVisitUndefinedVarInFunction() {
    String varName = "foo";
    Node parent = NodeFactory.createFunctionStatement(Token.FUNCTION, null, Lists.newArrayList(
        NodeFactory.createName("function"), NodeFactory.createName(varName)
    ));
    Node n = NodeFactory.createName("foo");

    Scope scope = NodeTraversal.createScope(null, null);
    when(compiler.getScope()).thenReturn(scope);

    NodeTraversal traversal = NodeTraversal.createTraversal(compiler, Node.NULL(), parent);
    varCheck.visit(traversal, n, parent);

    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
    assertTrue(scope.getVar(varName) == null);
  }

  @Test(expected = IllegalStateException.class)
  public void testVisitUndefinedVarInGlobalScope() {
    String varName = "foo";
    Node n = NodeFactory.createName("foo");

    Scope scope = NodeTraversal.createScope(null, null);
    when(compiler.getScope()).thenReturn(scope);

    NodeTraversal traversal = NodeTraversal.createTraversal(compiler, Node.NULL(), Node.NULL());
    varCheck.visit(traversal, n, Node.NULL());
  }

  @Test
  public void testVisitModuleDependency() {
    String currModuleName = "moduleA";
    String varModuleName = "moduleB";
    String varName = "foo";

    Node currModuleRoot = NodeFactory.createModuleStatement(Token.MODULE, Token.IDENT, Lists.newArrayList(
        NodeFactory.createName(currModuleName)
    ));
    Node varModuleRoot = NodeFactory.createModuleStatement(Token.MODULE, Token.IDENT, Lists.newArrayList(
        NodeFactory.createName(varModuleName)
    ));
    Node parent = NodeFactory.createGetProp(Token.GETPROP, NodeFactory.createName(currModuleName), NodeFactory.createName("foo"));
    Node n = NodeFactory.createName(varName);

    JSModule currModule = NodeFactory.createModule(currModuleName, null, currModuleRoot);
    JSModule varModule = NodeFactory.createModule(varModuleName, null, varModuleRoot);
    CompilerInput currInput = new DependentCompilerInput(Node.NULL(), currModule);
    CompilerInput varInput = new DependentCompilerInput(Node.NULL(), varModule);
    when(compiler.getModuleGraph().dependsOn(currModule, varModule)).thenReturn(false);

    Scope scope = NodeTraversal.createScope(null, null);
    when(compiler.getScope()).thenReturn(scope);

    NodeTraversal traversal = NodeTraversal.createTraversal(compiler, Node.NULL(), parent);
    varCheck.visit(traversal, n, parent);

    assertFalse(varCheck.varsToDeclareInExterns.contains(varName));
    assertTrue(scope.getVar(varName) == null);
  }

  @Test
  public void testVisitModuleDependencyWithProperDeclaration() {
    String currModuleName = "moduleA";
    String varModuleName = "moduleB";
    String varName = "foo";

    Node currModuleRoot = NodeFactory.createModuleStatement(Token.MODULE, Token.IDENT, Lists.newArrayList(
        NodeFactory.createName(currModuleName),
        NodeFactory.createImportDeclaration(Token.IMPORT, Token.IDENT, NodeFactory.createName("moduleB"))
    ));
    Node varModuleRoot = NodeFactory.createModuleStatement(Token.MODULE, Token.IDENT, Lists.newArrayList(
        NodeFactory.createName(varModuleName)
    ));
    Node parent = NodeFactory.createGetProp(Token.GETPROP, NodeFactory.createName(currModuleName), NodeFactory.createName("foo"));
    Node n = NodeFactory.createName(varName);

    JSModule currModule = NodeFactory.createModule(currModuleName, null, currModuleRoot);
    JSModule varModule = NodeFactory.createModule(varModuleName, null, varModuleRoot);
    CompilerInput currInput = new DependentCompilerInput(Node.NULL(), currModule);
    CompilerInput varInput = new DependentCompilerInput(Node.NULL(), varModule);
    when(compiler.getModuleGraph().dependsOn(currModule, varModule)).thenReturn(true);

    Scope scope = NodeTraversal.createScope(null, null);
    when(compiler.getScope()).thenReturn(scope);

    NodeTraversal traversal = NodeTraversal.createTraversal(compiler, Node.NULL(), parent);
    varCheck.visit(traversal, n, parent);

    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
    assertTrue(scope.getVar(varName) == null);
  }

  @Test
  public void testVisitModuleDependencyWithViolatedDependency() {
    String currModuleName = "moduleA";
    String varModuleName = "moduleB";
    String varName = "foo";

    Node currModuleRoot = NodeFactory.createModuleStatement(Token.MODULE, Token.IDENT, Lists.newArrayList(
        NodeFactory.createName(currModuleName)
    ));
    Node varModuleRoot = NodeFactory.createModuleStatement(Token.MODULE, Token.IDENT, Lists.newArrayList(
        NodeFactory.createName(varModuleName)
    ));
    Node parent = NodeFactory.createGetProp(Token.GETPROP, NodeFactory.createName(currModuleName), NodeFactory.createName("foo"));
    Node n = NodeFactory.createName(varName);

    JSModule currModule = NodeFactory.createModule(currModuleName, null, currModuleRoot);
    JSModule varModule = NodeFactory.createModule(varModuleName, null, varModuleRoot);
    CompilerInput currInput = new DependentCompilerInput(Node.NULL(), currModule);
    CompilerInput varInput = new DependentCompilerInput(Node.NULL(), varModule);
    when(compiler.getModuleGraph().dependsOn(currModule, varModule)).thenReturn(false);

    Scope scope = NodeTraversal.createScope(null, null);
    when(compiler.getScope()).thenReturn(scope);

    NodeTraversal traversal = NodeTraversal.createTraversal(compiler, Node.NULL(), parent);
    varCheck.visit(traversal, n, parent);

    assertFalse(varCheck.varsToDeclareInExterns.contains(varName));

    when(compiler.getErrorLevel(UNDEFINED_EXTERN_VAR_ERROR)).thenReturn(CheckLevel.WARNING);

    NodeTraversal traversal2 = NodeTraversal.createTraversal(compiler, Node.NULL(), parent);
    varCheck.visit(traversal2, n, parent);

    assertTrue(varCheck.varsToDeclareInExterns.isEmpty());
    assertTrue(scope.getVar(varName) == null);
    assertTrue(compiler.getDiagnostics().getMessages().isEmpty());
  }
}