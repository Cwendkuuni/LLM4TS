package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ScopeState;
import com.google.javascript.rhino.NodeOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

/**
 * Test cases for PeepholeOptimizationsPass class.
 *
 * @author <YourName>
 */
public class PeepholeOptimizationsPassTest {
  private PeepholeOptimizationsPass pass;
  private AbstractCompiler compilerMock;
  private List<AbstractPeepholeOptimization> optimizations;

  @Before
  public void setUp() {
    compilerMock = mock(AbstractCompiler.class);
    optimizations = Arrays.asList(mock(AbstractPeepholeOptimization.class), mock(AbstractPeepholeOptimization.class));
    pass = new PeepholeOptimizationsPass(compilerMock, optimizations.toArray(new AbstractPeepholeOptimization[0]));
  }

  @Test
  public void testProcess() {
    Node rootNode = NodeOperations.createRoot(compilerMock, "root");

    pass.process(null, rootNode);

    verify(compilerMock).addChangeHandler(pass.getPeepholeChangeHandler());
    verify(compilerMock).removeChangeHandler(pass.getPeepholeChangeHandler());
  }

  @Test
  public void testTraverseFunctionWithOutChildren() {
    Node functionNode = NodeOperations.createFunctionDeclaration(compilerMock, "functionName", null, null, null, null);

    pass.traverse(functionNode);

    // No visit call since the function has no children
    verify(compilerMock, never()).visit(functionNode);
  }

  @Test
  public void testTraverseFunctionWithChildren() {
    Node functionNode = NodeOperations.createFunctionDeclaration(compilerMock, "functionName", null, null, null, null);
    Node childNode1 = NodeOperations.createStatement(compilerMock, "statement1");
    Node childNode2 = NodeOperations.createStatement(compilerMock, "statement2");
    functionNode.addChildrenToEnd(childNode1, childNode2);

    pass.traverse(functionNode);

    verify(compilerMock).visit(functionNode);
  }

  @Test
  public void testShouldVisitFunctionWithOutChildren() {
    Node functionNode = NodeOperations.createFunctionDeclaration(compilerMock, "functionName", null, null, null, null);

    assertTrue(pass.shouldVisit(functionNode));
  }

  @Test
  public void testShouldVisitFunctionWithChildrenAndTraverseChildScopes() {
    Node functionNode = NodeOperations.createFunctionDeclaration(compilerMock, "functionName", null, null, null, null);
    Node childNode1 = NodeOperations.createStatement(compilerMock, "statement1");
    Node childNode2 = NodeOperations.createStatement(compilerMock, "statement2");
    functionNode.addChildrenToEnd(childNode1, childNode2);

    assertTrue(pass.shouldVisit(functionNode));

    pass.traversalState.push();
  }

  @Test
  public void testShouldVisitFunctionWithChildrenAndDontTraverseChildScopes() {
    Node functionNode = NodeOperations.createFunctionDeclaration(compilerMock, "functionName", null, null, null, null);
    Node childNode1 = NodeOperations.createStatement(compilerMock, "statement1");
    Node childNode2 = NodeOperations.createStatement(compilerMock, "statement2");
    functionNode.addChildrenToEnd(childNode1, childNode2);

    pass.traversalState.peek().traverseChildScopes = false;

    assertFalse(pass.shouldVisit(functionNode));
  }

  @Test
  public void testShouldRetransverseFunctionWithOutChangedScope() {
    Node functionNode = NodeOperations.createFunctionDeclaration(compilerMock, "functionName", null, null, null, null);
    Node childNode1 = NodeOperations.createStatement(compilerMock, "statement1");
    Node childNode2 = NodeOperations.createStatement(compilerMock, "statement2");
    functionNode.addChildrenToEnd(childNode1, childNode2);

    pass.traverse(functionNode);

    pass.traversalState.peek().changed = true;

    assertTrue(pass.shouldRetransverse(functionNode));
  }

  @Test
  public void testShouldRetransverseFunctionWithChangedScope() {
    Node functionNode = NodeOperations.createFunctionDeclaration(compilerMock, "functionName", null, null, null, null);
    Node childNode1 = NodeOperations.createStatement(compilerMock, "statement1");
    Node childNode2 = NodeOperations.createStatement(compilerMock, "statement2");
    functionNode.addChildrenToEnd(childNode1, childNode2);

    pass.traverse(functionNode);

    pass.traversalState.peek().changed = false;

    assertFalse(pass.shouldRetransverse(functionNode));
  }

  @Test
  public void testExitNodeFunction() {
    Node functionNode = NodeOperations.createFunctionDeclaration(compilerMock, "functionName", null, null, null, null);

    pass.exitNode(functionNode);

    pass.traversalState.pop();
  }

  @Test
  public void testExitNodeNotFunction() {
    Node statementNode = NodeOperations.createStatement(compilerMock, "statement");

    pass.exitNode(statementNode);

    // No pop call since the node is not a function
    verify(pass.traversalState, never()).pop();
  }

  @Test
  public void testVisitNoOptimization() {
    Node node = NodeOperations.createBinaryOperator(compilerMock, "=", NodeOperations.createIdentifier(compilerMock, "a"), NodeOperations.createNumber(compilerMock, 5));

    pass.visit(node);

    verify(compilerMock, never()).reportChange();
  }

  @Test
  public void testVisitWithOptimization() {
    Node node = NodeOperations.createBinaryOperator(compilerMock, "=", NodeOperations.createIdentifier(compilerMock, "a"), NodeOperations.createNumber(compilerMock, 5));
    AbstractPeepholeOptimization optimizationMock = mock(AbstractPeepholeOptimization.class);

    ArgumentCaptor<Node> capturedNode = ArgumentCaptor.forClass(Node.class);

    pass.visit(node);

    verify(optimizationMock).optimizeSubtree(capturedNode.capture());

    Node optimizedNode = capturedNode.getValue();

    verify(compilerMock).reportChange();
    assertEquals(optimizedNode, node);
  }

  @Test
  public void testBeginTraversal() {
    pass.beginTraversal();

    for (AbstractPeepholeOptimization optimization : optimizations) {
      verify(optimization).beginTraversal(compilerMock);
    }
  }

  @Test
  public void testEndTraversal() {
    pass.endTraversal();

    for (AbstractPeepholeOptimization optimization : optimizations) {
      verify(optimization).endTraversal(compilerMock);
    }
  }
}