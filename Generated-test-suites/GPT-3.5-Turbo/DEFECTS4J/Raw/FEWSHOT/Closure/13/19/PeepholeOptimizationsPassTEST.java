package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class PeepholeOptimizationsPassTest {

    @Mock
    private AbstractCompiler compilerMock;

    @Mock
    private Node externsMock;

    @Mock
    private Node rootMock;

    private PeepholeOptimizationsPass pass;

    @Before
    public void setUp() {
        compilerMock = mock(AbstractCompiler.class);
        externsMock = mock(Node.class);
        rootMock = mock(Node.class);
        pass = new PeepholeOptimizationsPass(compilerMock);
    }

    @Test
    public void testProcess() {
        CodeChangeHandler handlerMock = mock(CodeChangeHandler.class);
        pass.process(externsMock, rootMock);
        verify(compilerMock).addChangeHandler(handlerMock);
        verify(compilerMock).removeChangeHandler(handlerMock);
    }

    @Test
    public void testShouldRetraverse() {
        Node nodeMock = mock(Node.class);
        when(nodeMock.getParent()).thenReturn(null);
        when(nodeMock.isFunction()).thenReturn(true);
        when(nodeMock.isScript()).thenReturn(true);
        ScopeState stateMock = mock(ScopeState.class);
        when(stateMock.changed).thenReturn(true);
        when(stateMock.traverseChildScopes).thenReturn(true);
        when(traversalState.peek()).thenReturn(stateMock);
        boolean result = pass.shouldRetraverse(nodeMock);
        assertEquals(false, result);
    }

    @Test
    public void testShouldVisit() {
        Node nodeMock = mock(Node.class);
        when(nodeMock.isFunction()).thenReturn(true);
        when(nodeMock.isScript()).thenReturn(true);
        ScopeState previousMock = mock(ScopeState.class);
        when(previousMock.traverseChildScopes).thenReturn(true);
        when(traversalState.peek()).thenReturn(previousMock);
        pass.shouldVisit(nodeMock);
        verify(traversalState, times(2)).push();
    }

    @Test
    public void testExitNode() {
        Node nodeMock = mock(Node.class);
        when(nodeMock.isFunction()).thenReturn(true);
        when(nodeMock.isScript()).thenReturn(true);
        pass.exitNode(nodeMock);
        verify(traversalState).pop();
    }

    @Test
    public void testVisit() {
        Node nodeMock = mock(Node.class);
        AbstractPeepholeOptimization optimizationMock = mock(AbstractPeepholeOptimization.class);
        Node newVersionOfNodeMock = mock(Node.class);
        when(optimizationMock.optimizeSubtree(nodeMock)).thenReturn(newVersionOfNodeMock);
        when(optimizationMock.optimizeSubtree(newVersionOfNodeMock)).thenReturn(newVersionOfNodeMock);
        pass.peepholeOptimizations = new AbstractPeepholeOptimization[]{optimizationMock};
        pass.visit(nodeMock);
        verify(optimizationMock, times(2)).optimizeSubtree(nodeMock);
    }

    @Test
    public void testBeginTraversal() {
        AbstractPeepholeOptimization optimizationMock = mock(AbstractPeepholeOptimization.class);
        pass.peepholeOptimizations = new AbstractPeepholeOptimization[]{optimizationMock};
        pass.beginTraversal();
        verify(optimizationMock).beginTraversal(compilerMock);
    }

    @Test
    public void testEndTraversal() {
        AbstractPeepholeOptimization optimizationMock = mock(AbstractPeepholeOptimization.class);
        pass.peepholeOptimizations = new AbstractPeepholeOptimization[]{optimizationMock};
        pass.endTraversal();
        verify(optimizationMock).endTraversal(compilerMock);
    }
}