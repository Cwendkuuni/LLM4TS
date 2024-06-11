package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.jscomp.CompilerPassTestUtils;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeType;
import com.google.javascript.rhino.NodeOperations;

class PeepholeOptimizationsPassTest extends CompilerPassTestCase {

  private PeepholeOptimizationsPass pass;
  private CompilerPassTestUtils testUtils;
  private AbstractCompiler compiler;

  @Before
  public void setUp() throws Exception {
    compiler = mock(AbstractCompiler.class);
    testUtils = new CompilerPassTestUtils(compiler);
    pass = new PeepholeOptimizationsPass(compiler, new NoOpPeepholeOptimization(),
        new DeadCodeEliminationPeepholeOptimization());
  }

  @Test
  public void testDefaultConstructor() {
    assertNull(pass.getCompiler());
    assertNotNull(pass.peepholeOptimizations);
    assertFalse(pass.peepholeOptimizations.length > 0);
  }

  @Test
  public void testConstructorWithCompilerAndOptimizations() {
    assertNotNull(pass.getCompiler());
    assertNotNull(pass.peepholeOptimizations);
    assertFalse(pass.peepholeOptimizations.length > 0);
  }

  @Test
  public void testProcess_ExternsAndRootAreNull() {
    pass.process(null, null);
  }

  @Test
  public void testProcess_RootIsNull() {
    Node externs = testUtils.createTestFile("Test.java");
    pass.process(externs, null);
  }

  @Test
  public void testProcess_ExternsIsNotNull() {
    Node externs = testUtils.createTestFile("Test.java");
    Node root = testUtils.createTestFile("Test.java");
    pass.process(externs, root);
  }

  @Test
  public void testProcess_RootIsFunction() {
    Node externs = testUtils.createTestFile("Test.java");
    String functionSource = "function testFunction() { return 1; }";
    Node functionNode = NodeOperations.parseFunction(functionSource);
    Node root = NodeOperations.script(functionSource);
    root.setFirstChild(functionNode);
    pass.process(externs, root);
  }

  @Test
  public void testProcess_RootIsScript() {
    Node externs = testUtils.createTestFile("Test.java");
    String scriptSource = "1;";
    Node root = NodeOperations.script(scriptSource);
    pass.process(externs, root);
  }

  @Test(expected = NullPointerException.class)
  public void testProcess_CompilerIsNull() {
    AbstractCompiler compiler = null;
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler,
        new NoOpPeepholeOptimization(), new DeadCodeEliminationPeepholeOptimization());
    Node externs = testUtils.createTestFile("Test.java");
    Node root = testUtils.createTestFile("Test.java");
    pass.process(externs, root);
  }

  @Test
  public void testShouldVisit_Function() {
    Node functionNode = NodeOperations.parseFunction("function testFunction() { return 1; }");
    ScopeState state = new ScopeState();
    assertTrue(pass.shouldVisit(functionNode, state));
  }

  @Test
  public void testShouldVisit_Script() {
    Node scriptNode = NodeOperations.script("1;");
    ScopeState state = new ScopeState();
    assertTrue(pass.shouldVisit(scriptNode, state));
  }

  @Test
  public void testShouldVisit_FunctionWithDisabledChildScopes() {
    Node functionNode = NodeOperations.parseFunction("function testFunction() { return 1; }");
    ScopeState state = new ScopeState();
    state.traverseChildScopes = false;
    assertFalse(pass.shouldVisit(functionNode, state));
  }

  @Test
  public void testShouldRetraverse_FunctionWithChangedParent() {
    Node functionNode = NodeOperations.parseFunction("function testFunction() { return 1; }");
    Node parentNode = NodeOperations.script("function testParentFunction() { testFunction(); }");
    Node root = NodeOperations.script("testParentFunction();");
    root.setFirstChild(parentNode);
    parentNode.setFirstChild(functionNode);

    traversalState.push();
    traversalState.peek().traverseChildScopes = false;
    assertFalse(pass.shouldRetraverse(functionNode));

    traversalState.pop();
    traversalState.peek().reset();
    assertTrue(pass.shouldRetraverse(functionNode));
  }

  @Test
  public void testShouldRetraverse_FunctionWithUnchangedParent() {
    Node functionNode = NodeOperations.parseFunction("function testFunction() { return 1; }");
    Node parentNode = NodeOperations.script("function testParentFunction() { testFunction(); }");
    Node root = NodeOperations.script("testParentFunction();");
    root.setFirstChild(parentNode);
    parentNode.setFirstChild(functionNode);

    traversalState.push();
    traversalState.peek().traverseChildScopes = true;
    assertTrue(pass.shouldRetraverse(functionNode));

    traversalState.pop();
    traversalState.peek().reset();
    assertTrue(pass.shouldRetraverse(functionNode));
  }

  @Test
  public void testShouldRetraverse_ScriptWithChangedParent() {
    Node scriptNode = NodeOperations.script("1;");
    Node parentNode = NodeOperations.script("1;");
    Node root = NodeOperations.script("1;");
    root.setFirstChild(parentNode);
    parentNode.setFirstChild(scriptNode);

    traversalState.push();
    traversalState.peek().traverseChildScopes = false;
    assertFalse(pass.shouldRetraverse(scriptNode));

    traversalState.pop();
    traversalState.peek().reset();
    assertTrue(pass.shouldRetravisit(scriptNode));
  }

  @Test
  public void testShouldRetraverse_ScriptWithUnchangedParent() {
    Node scriptNode = NodeOperations.script("1;");
    Node parentNode = NodeOperations.script("1;");
    Node root = NodeOperations.script("1;");
    root.setFirstChild(parentNode);
    parentNode.setFirstChild(scriptNode);

    traversalState.push();
    traversalState.peek().traverseChildScopes = true;
    assertTrue(pass.shouldRetraverse(scriptNode));

    traversalState.pop();
    traversalState.peek().reset();
    assertTrue(pass.shouldRetraverse(scriptNode));
  }

  @Test
  public void testVisit_NoChange() {
    Node node = NodeOperations.number(1);
    pass.visit(node);
  }

  @Test
  public void testVisit_Changed() {
    Node node = NodeOperations.number(1);
    Node newNode = NodeOperations.string("2");
    pass.visit(node);
    pass.visit(node);
    pass.visit(newNode);

    assertNotSame(node, pass.getCompiler().getLastChange().getNode());
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testVisit_ArrayIndexOutOfBounds() {
    Node node = NodeOperations.array(NodeOperations.number(1));
    pass.visit(node);
  }

  @Test
  public void testBeginTraversal() {
    pass.beginTraversal();

    for (AbstractPeepholeOptimization optimization : pass.peepholeOptimizations) {
      verify(optimization).beginTraversal(compiler);
    }
  }

  @Test
  public void testEndTraversal() {
    pass.beginTraversal();

    for (AbstractPeepholeOptimization optimization : pass.peepholeOptimizations) {
      verify(optimization).endTraversal(compiler);
    }

    pass.endTraversal();
  }

  @Test
  public void testExitNode_Function() {
    Node functionNode = NodeOperations.parseFunction("function testFunction() { return 1; }");
    pass.exitNode(functionNode);
  }

  @Test
  public void testExitNode_Script() {
    Node scriptNode = NodeOperations.script("1;");
    pass.exitNode(scriptNode);
  }

  private static class NoOpPeepholeOptimization implements AbstractPeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node node) {
      return node;
    }

    @Override
    public void beginTraversal(CompilerPass pass) {
    }

    @Override
    public void endTraversal(CompilerPass pass) {
    }
  }

  private static class DeadCodeEliminationPeepholeOptimization
      implements AbstractPeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node node) {
      if (node.isFunctionDeclaration() || node.isVariable()) {
        Scope scope = node.getParentScope();
        if (scope != null && scope.getType() == ScopeType.GLOBAL) {
          Node declaration = scope.getVariable(node.getId().getName());
          if (declaration != null && node.getFirstChild() == null) {
            declaration.setParent(null);
            declaration.setFirstChild(null);
            declaration.setNext(node.getNext());
            node.setParent(null);
            node.setFirstChild(null);
            node.setNext(null);
            return null;
          }
        }
      }

      return node;
    }

    @Override
    public void beginTraversal(CompilerPass pass) {
    }

    @Override
    public void endTraversal(CompilerPass pass) {
    }
  }
}