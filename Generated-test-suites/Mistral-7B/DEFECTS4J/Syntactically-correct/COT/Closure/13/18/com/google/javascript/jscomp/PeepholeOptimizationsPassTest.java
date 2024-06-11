package com.google.javascript.jscomp;

import com.google.common.base.Preconditions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PeepholeOptimizationsPassTest {

  @Mock
  private AbstractCompiler compiler;

  private PeepholeOptimizationsPass peepholeOptimizationsPass;
  private ArrayList<AbstractPeepholeOptimization> optimizations;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    optimizations = new ArrayList<>();
    peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
  }

  @Test
  public void testConstructor() {
    peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
    Preconditions.checkState(peepholeOptimizationsPass.peepholeOptimizations.length == 0,
        "Expected empty peephole optimizations array");
  }

  @Test
  public void testProcess_EmptyNode() {
    peepholeOptimizationsPass.process(null, null);
    verify(compiler, Mockito.never()).addChangeHandler(Mockito.any());
    verify(compiler, Mockito.never()).removeChangeHandler(Mockito.any());
  }

  @Test
  public void testProcess_NullCompiler() {
    peepholeOptimizationsPass = new PeepholeOptimizationsPass(null, optimizations.toArray(new AbstractPeepholeOptimization[0]));
    assert (peepholeOptimizationsPass == null);
  }

  @Test(expected = NullPointerException.class)
  public void testProcess_NullOptimizations() {
    peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, null);
  }

  @Test
  public void testProcess_EmptyAST() {
    Node root = Node.NULL();
    peepholeOptimizationsPass.process(Node.NULL(), root);
    verify(compiler, Mockito.never()).addChangeHandler(Mockito.any());
    verify(compiler, Mockito.never()).removeChangeHandler(Mockito.any());
  }

  @Test
  public void testProcess_NonEmptyAST() {
    Node root = Node.objectLiteral();
    peepholeOptimizationsPass.process(Node.NULL(), root);
    verify(compiler, Mockito.atLeastOnce()).addChangeHandler(Mockito.any());
    verify(compiler, Mockito.atLeastOnce()).removeChangeHandler(Mockito.any());
  }

  @Test
  public void testVisit() {
    Node node = Node.literal(1);
    peepholeOptimizationsPass.visit(node);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testVisit_NullNode() {
    peepholeOptimizationsPass.visit(null);
  }

  @Test
  public void testShouldVisit_FunctionNode() {
    Node functionNode = Node.functionDeclaration("func", null, Node.NULL(), Lists.newArrayList(Node.NULL()), Node.NULL(), Node.NULL());
    boolean shouldVisit = peepholeOptimizationsPass.shouldVisit(functionNode);
    Preconditions.checkState(shouldVisit, "Expected true");
  }

  @Test
  public void testShouldVisit_ScriptNode() {
    Node scriptNode = Node.script(Node.stringLiteral(""));
    boolean shouldVisit = peepholeOptimizationsPass.shouldVisit(scriptNode);
    Preconditions.checkState(shouldVisit, "Expected true");
  }

  @Test
  public void testShouldRetraverse_NonFunctionOrScriptNode() {
    Node node = Node.literal(1);
    boolean shouldRetraverse = peepholeOptimizationsPass.shouldRetraverse(node);
    Preconditions.checkState(shouldRetraverse == false, "Expected false");
  }

  @Test
  public void testShouldRetraverse_FunctionNode() {
    Node functionNode = Node.functionDeclaration("func", null, Node.NULL(), Lists.newArrayList(Node.NULL()), Node.NULL(), Node.NULL());
    boolean previousTraverseChildScopes = true;
    traversalStatePreparation(peepholeOptimizationsPass, functionNode, previousTraverseChildScopes);
    boolean shouldRetraverse = peepholeOptimizationsPass.shouldRetraverse(functionNode);
    Preconditions.checkState(shouldRetraverse, "Expected true");
  }

  @Test
  public void testShouldRetraverse_FunctionNodeWithChangedScope() {
    Node functionNode = Node.functionDeclaration("func", null, Node.NULL(), Lists.newArrayList(Node.NULL()), Node.NULL(), Node.NULL());
    boolean previousTraverseChildScopes = true;
    boolean changed = true;
    traversalStatePreparation(peepholeOptimizationsPass, functionNode, previousTraverseChildScopes);
    peepholeOptimizationsPass.traversalState.peek().changed = changed;
    boolean shouldRetraverse = peepholeOptimizationsPass.shouldRetraverse(functionNode);
    Preconditions.checkState(shouldRetraverse, "Expected true");
  }

  @Test
  public void testShouldRetraverse_ScriptNode() {
    Node scriptNode = Node.script(Node.stringLiteral(""));
    boolean shouldRetraverse = peepholeOptimizationsPass.shouldRetraverse(scriptNode);
    Preconditions.checkState(shouldRetraverse == false, "Expected false");
  }

  @Test
  public void testExitNode_FunctionNode() {
    Node functionNode = Node.functionDeclaration("func", null, Node.NULL(), Lists.newArrayList(Node.NULL()), Node.NULL(), Node.NULL());
    peepholeOptimizationsPass.exitNode(functionNode);
  }

  @Test
  public void testExitNode_ScriptNode() {
    Node scriptNode = Node.script(Node.stringLiteral(""));
    peepholeOptimizationsPass.exitNode(scriptNode);
  }

  @Test
  public void testBeginTraversal() {
    peepholeOptimizationsPass.beginTraversal();
    for (AbstractPeepholeOptimization optimization : optimizations) {
      verify(optimization).beginTraversal(compiler);
    }
  }

  @Test
  public void testEndTraversal() {
    peepholeOptimizationsPass.endTraversal();
    for (AbstractPeepholeOptimization optimization : optimizations) {
      verify(optimization).endTraversal(compiler);
    }
  }

  private void traversalStatePreparation(PeepholeOptimizationsPass pass, Node node, boolean previousTraverseChildScopes) {
    pass.traversalState.push();
    pass.traversalState.peek().traverseChildScopes = previousTraverseChildScopes;
  }
}