import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.NodeFactory;

import java.util.Set;

public class VarCheckTest {

  private CompilerInput input;
  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private CompilerPassManager manager;
  private VarCheck varCheck;
  private Node root;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    manager = mock(CompilerPassManager.class);
    input = new CompilerInput("test.js", null, null, null, null, null);
    root = new Node(Token.PROGRAM, null);
    when(compiler.newCompilerPassManager()).thenReturn(manager);
    when(manager.getRoot()).thenReturn(root);
    when(manager.getCompiler()).thenReturn(compiler);
    varCheck = new VarCheck(compiler);
  }

  @org.junit.Test
  public void testUndefinedVariable() {
    Node variableNode = Node.newString(Token.NAME, "foo");
    Node parentNode = new Node(Token.BLOCK, null);
    parentNode.addChildToBack(variableNode);
    root.addChildToBack(parentNode);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    traversal.traverse(varCheck);

    Scope scope = traversal.getScope();
    assertEquals(null, scope.getVar("foo"));
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(
        "variable foo is undefined",
        traversal.getDiagnostics().get(0).getMessage());
  }

  @org.junit.Test
  public void testDuplicateVariable() {
    Node variableNode1 = Node.newString(Token.NAME, "foo");
    Node variableNode2 = Node.newString(Token.NAME, "foo");

    Node functionNode = new Node(Token.FUNCTION, null);
    functionNode.addChildToBack(variableNode1);
    Node parentNode = new Node(Token.BLOCK, null);
    parentNode.addChildToBack(functionNode);
    parentNode.addChildToBack(variableNode2);
    root.addChildToBack(parentNode);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    traversal.traverse(varCheck);

    Scope scope = traversal.getScope();
    assertEquals(null, scope.getVar("foo"));
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(
        "variable foo is undefined",
        traversal.getDiagnostics().get(0).getMessage());
  }

  @org.junit.Test
  public void testValidVariable() {
    Node variableNode = Node.newString(Token.NAME, "foo");
    Node parentNode = new Node(Token.BLOCK, null);
    parentNode.addChildToBack(variableNode);
    root.addChildToBack(parentNode);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    traversal.traverse(varCheck);

    Scope scope = traversal.getScope();
    Scope.Var var = scope.getVar("foo");
    assertEquals(variableNode, var.node);
    assertEquals(0, traversal.getDiagnostics().size());
  }

  @org.junit.Test
  public void testUndefinedVariableInFunction() {
    Node functionNode = new Node(Token.FUNCTION, null);
    Node variableNode = Node.newString(Token.NAME, "foo");
    functionNode.addChildToBack(variableNode);
    Node parentNode = new Node(Token.BLOCK, null);
    parentNode.addChildToBack(functionNode);
    root.addChildToBack(parentNode);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    traversal.traverse(varCheck);

    Scope functionScope = traversal.getScope();
    Scope globalScope = traversal.getScope().getGlobalScope();
    assertEquals(null, functionScope.getVar("foo"));
    assertEquals(null, globalScope.getVar("foo"));
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("variable foo is undefined", traversal.getDiagnostics().get(0).getMessage());
  }

  @org.junit.Test
  public void testVariableInExterns() {
    Node variableNode = Node.newString(Token.NAME, "foo");
    Node externsNode = new Node(Token.PROGRAM, null);
    externsNode.addChildToBack(variableNode);
    root.addChildToBack(externsNode);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    traversal.traverse(varCheck);

    Scope scope = traversal.getScope();
    assertEquals(scope.getVar("foo"), traversal.getScope().getGlobalScope().getVar("foo"));
    assertEquals(0, traversal.getDiagnostics().size());
  }

  @org.junit.Test
  public void testVariableInExternsWithDeclaredVariable() {
    Node variableNode = Node.newString(Token.NAME, "foo");
    Node externsNode = new Node(Token.PROGRAM, null);
    externsNode.addChildToBack(variableNode);

    Node functionNode = new Node(Token.FUNCTION, null);
    Node localVariableNode = Node.newString(Token.NAME, "foo");
    functionNode.addChildToBack(localVariableNode);
    Node parentNode = new Node(Token.BLOCK, null);
    parentNode.addChildToBack(functionNode);
    root.addChildToBack(parentNode);

    NodeTraversal traversal = new NodeTraversal(compiler, root, null);
    traversal.traverse(varCheck);

    Scope scope = traversal.getScope();
    assertEquals(scope.getVar("foo"), traversal.getScope().getGlobalScope().getVar("foo"));
    assertEquals(0, traversal.getDiagnostics().size());
  }

  @org.junit.Test
  public void testModuleDependency() {
    Node module = new Node(Token.PROGRAM, null);
    module.putBooleanProp(Node.IS_MODULE, true);
    module.putStringProp(Node.MODULE_NAME, "module1");

    Node variableNode = Node.newString(Token.NAME, "foo");
    Node assignNode = new Node(Token.ASSIGN, null);
    assignNode.addChildToBack(variableNode);
    Node functionNode = new Node(Token.FUNCTION, null);
    functionNode.addChildToBack(assignNode);
    module.addChildToBack(functionNode);

    Node dependencyModule = new Node(Token.PROGRAM, null);
    dependencyModule.putBooleanProp(Node.IS_MODULE, true);
    dependencyModule.putStringProp(Node.MODULE_NAME, "module2");

    Node dependencyVariableNode = Node.newString(Token.NAME, "foo");
    Node dependencyFunctionNode = new Node(Token.FUNCTION, null);
    dependencyFunctionNode.addChildToBack(dependencyVariableNode);
    dependencyModule.addChildToBack(dependencyFunctionNode);

    Node rootNode = new Node(Token.PROGRAM, null);
    rootNode.addChildToBack(module);
    rootNode.addChildToBack(dependencyModule);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
    traversal.traverse(varCheck);

    JSModuleGraph moduleGraph = compiler.getModuleGraph();
    assertEquals(true, moduleGraph.dependsOn(module, dependencyModule));
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(
        "module module1 cannot reference foo, defined in module module2",
        traversal.getDiagnostics().get(0).getMessage());
  }

  @org.junit.Test
  public void testMissingModuleDependency() {
    Node module = new Node(Token.PROGRAM, null);
    module.putBooleanProp(Node.IS_MODULE, true);
    module.putStringProp(Node.MODULE_NAME, "module1");

    Node variableNode = Node.newString(Token.NAME, "foo");
    Node assignNode = new Node(Token.ASSIGN, null);
    assignNode.addChildToBack(variableNode);
    Node functionNode = new Node(Token.FUNCTION, null);
    functionNode.addChildToBack(assignNode);
    module.addChildToBack(functionNode);

    Node rootNode = new Node(Token.PROGRAM, null);
    rootNode.addChildToBack(module);

    Node dependencyModule = new Node(Token.PROGRAM, null);
    dependencyModule.putBooleanProp(Node.IS_MODULE, true);
    dependencyModule.putStringProp(Node.MODULE_NAME, "module2");

    Node dependencyVariableNode = Node.newString(Token.NAME, "foo");
    Node dependencyFunctionNode = new Node(Token.FUNCTION, null);
    dependencyFunctionNode.addChildToBack(dependencyVariableNode);
    dependencyModule.addChildToBack(dependencyFunctionNode);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
    traversal.traverse(varCheck);

    JSModuleGraph moduleGraph = compiler.getModuleGraph();
    assertEquals(false, moduleGraph.dependsOn(module, dependencyModule));
    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals(
        "missing module dependency; module module1 should depend "
            + "on module module2 because it references foo",
        traversal.getDiagnostics().get(0).getMessage());
  }

  @org.junit.Test
  public void testStrictModuleDependency() {
    Node module = new Node(Token.PROGRAM, null);
    module.putBooleanProp(Node.IS_MODULE, true);
    module.putStringProp(Node.MODULE_NAME, "module1");

    Node variableNode = Node.newString(Token.NAME, "foo");
    Node assignNode = new Node(Token.ASSIGN, null);
    assignNode.addChildToBack(variableNode);
    Node functionNode = new Node(Token.FUNCTION, null);
    functionNode.addChildToBack(assignNode);
    module.addChildToBack(functionNode);

    Node dependencyModule = new Node(Token.PROGRAM, null);
    dependencyModule.putBooleanProp(Node.IS_MODULE, true);
    dependencyModule.putStringProp(Node.MODULE_NAME, "module2");

    Node dependencyVariableNode = Node.newString(Token.NAME, "foo");
    Node dependencyFunctionNode = new Node(Token.FUNCTION, null);
    dependencyFunctionNode.addChildToBack(dependencyVariableNode);
    dependencyModule.addChildToBack(dependencyFunctionNode);

    Node rootNode = new Node(Token.PROGRAM, null);
    rootNode.addChildToBack(module);
    rootNode.addChildToBack(dependencyModule);

    varCheck = new VarCheck(compiler, true);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
    traversal.traverse(varCheck);

    JSModuleGraph moduleGraph = compiler.getModuleGraph();
    assertEquals(false, moduleGraph.dependsOn(module, dependencyModule));
    assertEquals(0, traversal.getDiagnostics().size());
  }

  @org.junit.Test
  public void testNameReferenceInExterns() {
    Node externsNode = new Node(Token.PROGRAM, null);
    Node nameReferenceNode = Node.newString(Token.NAME, "foo");
    externsNode.addChildToBack(nameReferenceNode);
    Node rootNode = new Node(Token.PROGRAM, null);
    rootNode.addChildToBack(externsNode);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
    traversal.traverse(varCheck);

    assertEquals(1, traversal.getDiagnostics().size());
    assertEquals("accessing name foo in externs has no effect", traversal.getDiagnostics().get(0).getMessage());
  }
}