import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PeepholeOptimizationsPassTest {

    private AbstractCompiler compiler;
    private AbstractPeepholeOptimization[] peepholeOptimizations;
    private PeepholeOptimizationsPass pass;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        peepholeOptimizations = new AbstractPeepholeOptimization[1];
        peepholeOptimizations[0] = mock(AbstractPeepholeOptimization.class);
        pass = new PeepholeOptimizationsPass(compiler, peepholeOptimizations);
    }

    @Test
    public void testProcess_withExternalAndRootNodes_callsMethodsInCorrectOrder() {
        Node externsNode = mock(Node.class);
        Node rootNode = mock(Node.class);
        PeepholeChangeHandler handler = mock(PeepholeChangeHandler.class);
        pass.compiler.addChangeHandler(handler);

        pass.beginTraversal();

        pass.process(externsNode, rootNode);

        pass.endTraversal();

        verify(peepholeOptimizations[0], times(1)).beginTraversal(compiler);
        verify(peepholeOptimizations[0], times(1)).endTraversal(compiler);
        verify(compiler, times(1)).addChangeHandler(handler);
        verify(compiler, times(1)).removeChangeHandler(handler);
    }

    @Test
    public void testProcess_withExternalAndRootNodes_callsTraverseMethod() {
        Node externsNode = mock(Node.class);
        Node rootNode = mock(Node.class);

        pass.traverse(rootNode);

        verify(pass).traverse(rootNode);
    }

    @Test
    public void testProcess_withShouldRetraverse_returnsTrueForFunctionNode() {
        Node functionNode = mock(Node.class);
        when(functionNode.getParent()).thenReturn(mock(Node.class));
        when(functionNode.isFunction()).thenReturn(true);

        boolean result = pass.shouldRetraverse(functionNode);

        Assert.assertTrue(result);
    }

    @Test
    public void testProcess_withShouldRetraverse_returnsFalseForFunctionNode() {
        Node functionNode = mock(Node.class);
        when(functionNode.getParent()).thenReturn(mock(Node.class));
        when(functionNode.isFunction()).thenReturn(false);

        boolean result = pass.shouldRetraverse(functionNode);

        Assert.assertFalse(result);
    }

    @Test
    public void testProcess_withShouldRetraverse_returnsFalseForNullCurrentNode() {
        Node node = mock(Node.class);
        when(node.getParent()).thenReturn(null);

        boolean result = pass.shouldRetraverse(node);

        Assert.assertFalse(result);
    }

    @Test
    public void testProcess_withShouldVisit_returnsTrueForFunctionNode() {
        Node functionNode = mock(Node.class);
        when(functionNode.isFunction()).thenReturn(true);
        ScopeState previous = mock(ScopeState.class);
        when(pass.traversalState.peek()).thenReturn(previous);
        when(previous.traverseChildScopes).thenReturn(true);

        boolean result = pass.shouldVisit(functionNode);

        Assert.assertTrue(result);
        verify(pass.traversalState, times(1)).push();
    }

    @Test
    public void testProcess_withShouldVisit_returnsFalseForFunctionNode() {
        Node functionNode = mock(Node.class);
        when(functionNode.isFunction()).thenReturn(true);
        ScopeState previous = mock(ScopeState.class);
        when(pass.traversalState.peek()).thenReturn(previous);
        when(previous.traverseChildScopes).thenReturn(false);

        boolean result = pass.shouldVisit(functionNode);

        Assert.assertFalse(result);
        verify(pass.traversalState, never()).push();
    }

    @Test
    public void testProcess_withExitNode_callsPopMethodForFunctionNode() {
        Node functionNode = mock(Node.class);
        when(functionNode.isFunction()).thenReturn(true);

        pass.exitNode(functionNode);

        verify(pass.traversalState, times(1)).pop();
    }

    @Test
    public void testVisit_callsOptimizeSubtreeMethodForEachOptimization() {
        Node node = mock(Node.class);
        AbstractPeepholeOptimization optimization1 = mock(AbstractPeepholeOptimization.class);
        AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);
        pass.peepholeOptimizations[0] = optimization1;
        pass.peepholeOptimizations[1] = optimization2;

        pass.visit(node);

        verify(optimization1, times(1)).optimizeSubtree(node);
        verify(optimization2, times(1)).optimizeSubtree(node);
    }

    @Test
    public void testVisit_stopsLoopWhenNoChangeInNode() {
        Node node = mock(Node.class);
        AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);
        pass.peepholeOptimizations[0] = optimization;
        when(optimization.optimizeSubtree(node)).thenReturn(node);

        pass.visit(node);

        verify(optimization, times(1)).optimizeSubtree(node);
    }

    @Test
    public void testVisit_stopsLoopWhenChangeInNode() {
        Node node = mock(Node.class);
        Node changedNode = mock(Node.class);
        AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);
        pass.peepholeOptimizations[0] = optimization;
        when(optimization.optimizeSubtree(node)).thenReturn(changedNode, node);

        pass.visit(node);

        verify(optimization, times(2)).optimizeSubtree(node);
    }

}