package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPasses;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeChangeHandler;
import com.google.javascript.jscomp.PeepholeOptimization;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.ScopeStack;
import com.google.javascript.rhino.NodeOps;

public class PeepholeOptimizationsPassTest {
  private CompilerPassManager passManager;
  private CompilerPass compilerPass;
  private PeepholeOptimizationsPass peepholePass;
  private Node rootNode;

  @Before
  public void setUp() {
    passManager = new CompilerPassManager();
    passManager.addPass(CompilerPasses.getOptimizationPasses());
    peepholePass = new PeepholeOptimizationsPass(mock(AbstractCompiler.class),
        new SimpleConstantFolding(), new SimpleFunctionInlining());
    compilerPass = peepholePass;

    rootNode = NodeOps.newNode(NodeOps.SCOPE);
    passManager.process(null, rootNode);
  }

  @Test
  public void testProcessWithEmptyAST() {
    peepholePass.process(null, null);
    verify(compilerPass, never()).process(any(Node.class));
  }

  @Test
  public void testProcessWithValidAST() {
    peepholePass.process(null, rootNode);
    verify(compilerPass, times(1)).process(eq(rootNode));
  }

  @Test
  public void testVisitNullNode() {
    when(compilerPass.getCompiler()).thenReturn(mock(AbstractCompiler.class));
    peepholePass.visit(null);
  }

  @Test
  public void testVisitFunctionNode() {
    Scope scope = new Scope();
    when(compilerPass.getCompiler()).thenReturn(mock(AbstractCompiler.class));
    when(compilerPass.getCompiler().getCurrentScope()).thenReturn(scope);

    Node functionNode = NodeOps.newNode(NodeOps.FUNCTION, "testFunction");
    Node bodyNode = NodeOps.newNode(NodeOps.BLOCK, null);
    functionNode.setBody(bodyNode);

    peepholePass.visit(functionNode);

    verify(compilerPass, times(1)).visit(eq(functionNode));
    verify(compilerPass, atLeastOnce()).beginTraversal(any(AbstractCompiler.class));
    verify(compilerPass, atLeastOnce()).endTraversal(any(AbstractCompiler.class));
  }

  @Test
  public void testVisitSimpleStatementNode() {
    Node statementNode = NodeOps.newNode(NodeOps.ASSIGN, null, null, NodeOps.createDouble(1.0));

    peepholePass.visit(statementNode);

    verify(compilerPass, times(1)).visit(eq(statementNode));
    verify(compilerPass, never()).optimizeSubtree(eq(statementNode));
  }

  @Test
  public void testVisitComplexStatementNode() {
    Node statementNode = NodeOps.newNode(NodeOps.RETURN, null, NodeOps.createList(
        NodeOps.newNode(NodeOps.ASSIGN, null, NodeOps.createIdent("x"), NodeOps.createDouble(1.0)),
        NodeOps.newNode(NodeOps.ASSIGN, null, NodeOps.createIdent("y"), NodeOps.createDouble(2.0))));

    Node expectedNewStatementNode = NodeOps.newNode(NodeOps.ASSIGN, null, NodeOps.createIdent("x"), NodeOps.createDouble(1.0));
    expectedNewStatementNode.setNext(NodeOps.newNode(NodeOps.ASSIGN, null, NodeOps.createIdent("y"), NodeOps.createDouble(2.0)));

    when(compilerPass.getCompiler()).thenReturn(mock(AbstractCompiler.class));
    when(compilerPass.getCompiler().getCurrentScope()).thenReturn(mock(Scope.class));
    when(compilerPass.getCompiler().getCurrentScope().getParent()).thenReturn(mock(Scope.class));
    when(compilerPass.getCompiler().getCurrentScope().getParent().getParent()).thenReturn(mock(Scope.class));

    Scope scope1 = mock(Scope.class);
    Scope scope2 = mock(Scope.class);
    Scope scope3 = mock(Scope.class);

    when(compilerPass.getCompiler().getCurrentScope()).thenReturn(scope1);
    when(scope1.getParent()).thenReturn(scope2);
    when(scope2.getParent()).thenReturn(scope3);

    when(compilerPass.optimizeSubtree(statementNode)).thenReturn(expectedNewStatementNode);

    Node newStatementNode = peepholePass.visit(statementNode);

    verify(compilerPass, times(1)).visit(eq(statementNode));
    verify(compilerPass, times(1)).optimizeSubtree(eq(statementNode));
    verify(compilerPass, times(1)).reportChange();
    assertEquals(expectedNewStatementNode, newStatementNode);
  }

  @Test
  public void testShouldVisitFunctionNodeWhenTraverseChildScopesIsTrue() {
    Node functionNode = NodeOps.newNode(NodeOps.FUNCTION, "testFunction");
    assertTrue(peepholePass.shouldVisit(functionNode));
  }

  @Test
  public void testShouldNotVisitFunctionNodeWhenTraverseChildScopesIsFalse() {
    Node functionNode = NodeOps.newNode(NodeOps.FUNCTION, "testFunction");
    ScopeState state = new ScopeState();
    traversalState.push();
    state = traversalState.peek();
    state.traverseChildScopes = false;

    assertFalse(peepholePass.shouldVisit(functionNode));
  }

  @Test
  public void testRetraverseFunctionNode() {
    Node functionNode = NodeOps.newNode(NodeOps.FUNCTION, "testFunction");
    Node newFunctionNode = NodeOps.newNode(NodeOps.FUNCTION, "newFunction");

    when(compilerPass.optimizeSubtree(functionNode)).thenReturn(newFunctionNode);

    peepholePass.traverse(functionNode);
    peepholePass.traverse(functionNode);

    verify(compilerPass, times(2)).optimizeSubtree(eq(functionNode));
    verify(compilerPass, times(1)).reportChange();
    assertEquals(newFunctionNode, functionNode.getFirstChild());
  }

  @Test
  public void testShouldRetraverseFunctionNodeWhenChangedFlagIsTrue() {
    Node functionNode = NodeOps.newNode(NodeOps.FUNCTION, "testFunction");
    Node newFunctionNode = NodeOps.newNode(NodeOps.FUNCTION, "newFunction");

    when(compilerPass.optimizeSubtree(functionNode)).thenReturn(newFunctionNode);

    ScopeState state = new ScopeState();
    traversalState.push();
    state = traversalState.peek();
    state.changed = true;

    assertTrue(peepholePass.shouldRetraverse(functionNode));
  }

  @Test
  public void testShouldRetraverseFunctionNodeWhenParentIsFunctionOrScript() {
    Node functionNode = NodeOps.newNode(NodeOps.FUNCTION, "testFunction");
    Node parentNode = NodeOps.newNode(NodeOps.FUNCTION, "parentFunction");
    functionNode.setParent(parentNode);

    ScopeState state = new ScopeState();
    traversalState.push();
    state = traversalState.peek();

    assertTrue(peepholePass.shouldRetraverse(functionNode));
  }

  @Test
  public void testShouldNotRetraverseFunctionNodeWhenParentIsNotFunctionOrScript() {
    Node functionNode = NodeOps.newNode(NodeOps.FUNCTION, "testFunction");
    Node parentNode = NodeOps.newNode(NodeOps.BLOCK, null);
    functionNode.setParent(parentNode);

    ScopeState state = new ScopeState();
    traversalState.push();
    state = traversalState.peek();

    assertFalse(peepholePass.shouldRetraverse(functionNode));
  }

  @Test
  public void testShouldVisitScriptNodeWhenTraverseChildScopesIsTrue() {
    Node scriptNode = NodeOps.newNode(NodeOps.SCRIPT, null);
    assertTrue(peepholePass.shouldVisit(scriptNode));
  }

  @Test
  public void testShouldNotVisitScriptNodeWhenTraverseChildScopesIsFalse() {
    Node scriptNode = NodeOps.newNode(NodeOps.SCRIPT, null);
    ScopeState state = new ScopeState();
    traversalState.push();
    state = traversalState.peek();
    state.traverseChildScopes = false;

    assertFalse(peepholePass.shouldVisit(scriptNode));
  }

  @Test
  public void testProcessWithFunctionAndScriptNodes() {
    Node functionNode = NodeOps.newNode(NodeOps.FUNCTION, "testFunction");
    Node scriptNode = NodeOps.newNode(NodeOps.SCRIPT, null);
    Node rootNode = NodeOps.newNode(NodeOps.SCOPE);
    rootNode.setBody(NodeOps.newNode(NodeOps.BLOCK, null));
    rootNode.getBody().appendChild(functionNode);
    rootNode.getBody().appendChild(scriptNode);

    peepholePass.process(null, rootNode);

    verify(compilerPass, times(2)).process(eq(rootNode));
  }

  @Test
  public void testShouldVisitFunctionOrScriptNodeWithChangedFlagWhenTrue() {
    Node functionOrScriptNode = NodeOps.newNode(NodeOps.FUNCTION, "testFunction");
    ScopeState state = new ScopeState();
    traversalState.push();
    state = traversalState.peek();
    state.changed = true;

    assertTrue(peepholePass.shouldVisit(functionOrScriptNode));
  }

  @Test
  public void testShouldNotVisitFunctionOrScriptNodeWithChangedFlagWhenFalse() {
    Node functionOrScriptNode = NodeOps.newNode(NodeOps.FUNCTION, "testFunction");
    ScopeState state = new ScopeState();
    traversalState.push();
    state = traversalState.peek();
    state.changed = false;

    assertFalse(peepholePass.shouldVisit(functionOrScriptNode));
  }

  @Test
  public void testBeginAndEndTraversal() {
    peepholePass.beginTraversal();
    peepholePass.endTraversal();

    verify(compilerPass, times(1)).beginTraversal(any(AbstractCompiler.class));
    verify(compilerPass, times(1)).endTraversal(any(AbstractCompiler.class));
  }

  private static class SimpleConstantFolding implements PeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node n) {
      return n;
    }
  }

  private static class SimpleFunctionInlining implements PeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node n) {
      return n;
    }
  }
}