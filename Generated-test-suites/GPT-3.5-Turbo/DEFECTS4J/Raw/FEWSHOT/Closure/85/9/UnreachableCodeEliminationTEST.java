package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.graph.GraphReachability;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphNode;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UnreachableCodeEliminationTest {

    @Mock
    private AbstractCompiler compiler;

    @Mock
    private ControlFlowAnalysis cfa;

    @InjectMocks
    private UnreachableCodeElimination unreachableCodeElimination;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        unreachableCodeElimination = spy(new UnreachableCodeElimination(compiler, true));
    }

    @Test
    public void testProcess() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);
        NodeTraversal.traverse(compiler, root, unreachableCodeElimination);
        verify(compiler, times(1)).reportCodeChange();
    }

    @Test
    public void testEnterScope() {
        NodeTraversal t = mock(NodeTraversal.class);
        Scope scope = mock(Scope.class);
        Node rootNode = mock(Node.class);
        when(t.getScope()).thenReturn(scope);
        when(scope.getRootNode()).thenReturn(rootNode);
        doNothing().when(cfa).process(null, rootNode);
        unreachableCodeElimination.enterScope(t);
        assertEquals(unreachableCodeElimination.cfgStack.peek(), unreachableCodeElimination.curCfg);
        verify(cfa, times(1)).process(null, rootNode);
    }

    @Test
    public void testExitScope() {
        NodeTraversal t = mock(NodeTraversal.class);
        unreachableCodeElimination.exitScope(t);
        assertEquals(unreachableCodeElimination.cfgStack.size(), 0);
    }

    @Test
    public void testVisit() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        DiGraphNode<Node, Branch> gNode = mock(DiGraphNode.class);
        unreachableCodeElimination.curCfg = mock(ControlFlowGraph.class);
        unreachableCodeElimination.curCfg.getDirectedGraphNode(any(Node.class));
        unreachableCodeElimination.removeDeadExprStatementSafely(n);
        unreachableCodeElimination.visit(t, n, parent);
        verify(unreachableCodeElimination, times(1)).removeDeadExprStatementSafely(n);
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        Node n = mock(Node.class);
        DiGraphNode<Node, Branch> gNode = mock(DiGraphNode.class);
        List<DiGraphEdge<Node,Branch>> outEdges = mock(List.class);
        Node firstChild = mock(Node.class);
        Node follow = mock(Node.class);
        Node target = mock(Node.class);
        Node next = mock(Node.class);
        unreachableCodeElimination.tryRemoveUnconditionalBranching(n);
        unreachableCodeElimination.tryRemoveUnconditionalBranching(null);
        unreachableCodeElimination.curCfg = mock(ControlFlowGraph.class);
        unreachableCodeElimination.curCfg.getDirectedGraphNode(any(Node.class));
        unreachableCodeElimination.computeFollowing(n);
        when(outEdges.size()).thenReturn(1);
        when(n.getType()).thenReturn(Token.BLOCK);
        when(n.hasChildren()).thenReturn(true);
        when(n.getFirstChild()).thenReturn(firstChild);
        when(firstChild.getType()).thenReturn(Token.BREAK);
        Node effectiveTarget = unreachableCodeElimination.tryRemoveUnconditionalBranching(n);
        assertEquals(effectiveTarget, target);
        when(gNode.getOutEdges()).thenReturn(outEdges);
        when(outEdges.get(0).getValue()).thenReturn(Branch.UNCOND);
        when(outEdges.get(0).getDestination().getValue()).thenReturn(target);
        when(n.getNext()).thenReturn(next);
        when(next.getType()).thenReturn(Token.FUNCTION);
        when(n.getType()).thenReturn(Token.RETURN);
        when(n.hasChildren()).thenReturn(false);
        when(follow.getType()).thenReturn(Token.RETURN);
        when(ControlFlowAnalysis.computeFollowNode(n)).thenReturn(follow);
        when(gNode.getAnnotation()).thenReturn(GraphReachability.REACHABLE);
        unreachableCodeElimination.tryRemoveUnconditionalBranching(n);
    }

    @Test
    public void testComputeFollowing() {
        Node n = mock(Node.class);
        unreachableCodeElimination.computeFollowing(n);
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        Node n = mock(Node.class);
        NodeUtil.redeclareVarsInsideBranch(n);
        unreachableCodeElimination.removeDeadExprStatementSafely(n);
        unreachableCodeElimination.removeDeadExprStatementSafely(n);
        verify(compiler, times(1)).reportCodeChange();
        verify(NodeUtil, times(1)).removeChild(n.getParent(), n);
    }
    
    
}