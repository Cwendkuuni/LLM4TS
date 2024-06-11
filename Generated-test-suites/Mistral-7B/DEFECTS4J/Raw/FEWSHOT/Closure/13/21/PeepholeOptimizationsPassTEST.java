package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PeepholeOptimizationsPassTest {
    private AbstractCompiler compiler;
    private PeepholeOptimizationsPass optimizationsPass;
    private Node rootNode;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        List<AbstractPeepholeOptimization> optimizations = Lists.newArrayList();
        optimizations.add(mock(AbstractPeepholeOptimization.class));
        optimizations.add(mock(AbstractPeepholeOptimization.class));
        optimizationsPass = new PeepholeOptimizationsPass(compiler, optimizations.toArray(new AbstractPeepholeOptimization[0]));
        rootNode = mock(Node.class);
    }

    @Test
    public void testProcess() {
        optimizationsPass.process(null, rootNode);
    }

    @Test
    public void testTraverse() {
        optimizationsPass.traverse(rootNode);
    }

    @Test
    public void testShouldVisit() {
        when(rootNode.isFunction()).thenReturn(false);
        assert(optimizationsPass.shouldVisit(rootNode));
    }

    @Test
    public void testShouldRetraverse() {
        when(rootNode.isFunction()).thenReturn(false);
        when(rootNode.getParent()).thenReturn(mock(Node.class));
        ScopeState state = optimizationsPass.traversalState.peek();
        state.changed = true;
        assert(optimizationsPass.shouldRetraverse(rootNode));
    }

    @Test
    public void testShouldVisitFunction() {
        when(rootNode.isFunction()).thenReturn(true);
        assert(optimizationsPass.shouldVisit(rootNode));
    }

    @Test
    public void testShouldRetraverseFunction() {
        when(rootNode.isFunction()).thenReturn(true);
        ScopeState state = optimizationsPass.traversalState.peek();
        state.changed = true;
        assert(!optimizationsPass.shouldRetraverse(rootNode));
    }

    @Test
    public void testVisit() {
        Node node = mock(Node.class);
        optimizationsPass.visit(node);
    }

    @Test
    public void testVisitNull() {
        optimizationsPass.visit(null);
    }

    @Test
    public void testBeginTraversal() {
        optimizationsPass.beginTraversal();
    }

    @Test
    public void testEndTraversal() {
        optimizationsPass.endTraversal();
    }

    @Test
    public void testProcessNodeChange() {
        when(rootNode.getFirstChild()).thenReturn(mock(Node.class));
        Node childNode = mock(Node.class);
        when(childNode.getFirstChild()).thenReturn(null);
        when(childNode.isFunction()).thenReturn(false);

        optimizationsPass.process(null, rootNode);

        optimizationsPass.traverse(rootNode);

        Node newNode = mock(Node.class);
        when(childNode.getFirstChild()).thenReturn(newNode);

        optimizationsPass.traverse(childNode);

        assertSame(newNode, optimizationsPass.traversalState.peek().changedNode);
        assertNull(optimizationsPass.traversalState.peek().changedNode);
    }
}