
package com.google.javascript.jscomp;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class PeepholeOptimizationsPassTest {

    private AbstractCompiler compiler;
    private AbstractPeepholeOptimization optimization;

    @Before
    public void setup() {
        compiler = mock(AbstractCompiler.class);
        optimization = mock(AbstractPeepholeOptimization.class);
    }

    @Test
    public void testProcess() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);
        PeepholeChangeHandler handler = mock(PeepholeChangeHandler.class);

        when(compiler.getChangeHandlers()).thenReturn(Lists.newArrayList(handler));

        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
        pass.process(externs, root);

        verify(handler).reportChange();
        verify(compiler).removeChangeHandler(handler);
    }

    @Test
    public void testShouldRetraverse_WithParentIsFunctionOrScriptAndStateChanged_ReturnsTrue() {
        Node parent = mock(Node.class);
        Node node = mock(Node.class);

        when(node.getParent()).thenReturn(parent);
        when(node.isFunction()).thenReturn(true);

        ScopeState state = new ScopeState();
        state.changed = true;
        state.traverseChildScopes = true;
        when(traversalState.peek()).thenReturn(state);

        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);

        boolean result = pass.shouldRetraverse(node);

        assertTrue(result);
    }

    @Test
    public void testShouldRetraverse_WithParentIsFunctionOrScriptAndStateChanged_ReturnsFalse() {
        Node parent = mock(Node.class);
        Node node = mock(Node.class);

        when(node.getParent()).thenReturn(parent);
        when(node.isFunction()).thenReturn(true);

        ScopeState state = new ScopeState();
        state.changed = false;
        state.traverseChildScopes = true;
        when(traversalState.peek()).thenReturn(state);

        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);

        boolean result = pass.shouldRetraverse(node);

        assertFalse(result);
    }

    @Test
    public void testShouldRetraverse_WithParentNotFunctionOrScript_ReturnsFalse() {
        Node parent = mock(Node.class);
        Node node = mock(Node.class);

        when(node.getParent()).thenReturn(parent);
        when(node.isFunction()).thenReturn(false);

        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);

        boolean result = pass.shouldRetraverse(node);

        assertFalse(result);
    }

    @Test
    public void testShouldVisit_WithNotFunctionOrScript_ReturnsTrue() {
        Node node = mock(Node.class);

        boolean result = pass.shouldVisit(node);

        assertTrue(result);
    }

    @Test
    public void testShouldVisit_WithFunctionOrScriptAndTraverseChildScopes_ReturnTrue() {
        Node node = mock(Node.class);
        ScopeState previous = mock(ScopeState.class);

        when(node.isFunction()).thenReturn(true);
        when(traversalState.peek()).thenReturn(previous);
        when(previous.traverseChildScopes).thenReturn(true);

        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);

        boolean result = pass.shouldVisit(node);

        assertTrue(result);
    }

    @Test
    public void testShouldVisit_WithFunctionOrScriptAndNotTraverseChildScopes_ReturnFalse() {
        Node node = mock(Node.class);
        ScopeState previous = mock(ScopeState.class);

        when(node.isFunction()).thenReturn(true);
        when(traversalState.peek()).thenReturn(previous);
        when(previous.traverseChildScopes).thenReturn(false);

        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);

        boolean result = pass.shouldVisit(node);

        assertFalse(result);
    }

    @Test
    public void testExitNode_WithFunctionOrScript_PopStateStack() {
        Node node = mock(Node.class);

        pass.exitNode(node);

        verify(traversalState).pop();
    }

    @Test
    public void testVisit_WithOptimizationReturningDifferentNode_ChangeHandlerReportChange() {
        Node node = mock(Node.class);
        Node newNode = mock(Node.class);

        when(optimization.optimizeSubtree(node)).thenReturn(newNode);

        pass.visit(node);

        verify(handler).reportChange();
    }

    @Test
    public void testVisit_WithOptimizationReturningSameNode_ChangeHandlerNotReportChange() {
        Node node = mock(Node.class);

        when(optimization.optimizeSubtree(node)).thenReturn(node);

        pass.visit(node);

        verify(handler, never()).reportChange();
    }

    @Test
    public void testVisit_WithNullNode_Return() {
        Node node = null;

        pass.visit(node);

        verify(handler, never()).reportChange();
    }

    @Test
    public void testBeginTraversal_CallsBeginTraversalOnOptimizations() {
        AbstractPeepholeOptimization[] optimizations = {optimization};
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

        pass.beginTraversal();

        verify(optimization).beginTraversal(compiler);
    }

    @Test
    public void testEndTraversal_CallsEndTraversalOnOptimizations() {
        AbstractPeepholeOptimization[] optimizations = {optimization};
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

        pass.endTraversal();

        verify(optimization).endTraversal(compiler);
    }
}
