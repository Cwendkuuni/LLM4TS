import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;
import java.util.ArrayList;

public class PeepholeOptimizationsPassTest {
    @Mock
    private AbstractCompiler compiler;
    private PeepholeOptimizationsPass pass;

    @Before
    public void setUp() {
        pass = new PeepholeOptimizationsPass(compiler);
    }

    @Test
    public void testProcess() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);
        
        PeepholeChangeHandler handler = mock(PeepholeChangeHandler.class);
        when(compiler.getChangeHandler()).thenReturn(handler);
        
        pass.process(externs, root);

        verify(compiler).addChangeHandler(handler);
        verify(compiler).traverse(root);
        verify(compiler).removeChangeHandler(handler);
    }

    @Test
    public void testGetCompiler() {
        assertEquals(compiler, pass.getCompiler());
    }

    @Test
    public void testShouldRetraverse_withFunction() {
        Node parent = mock(Node.class);
        Node node = mock(Node.class);
        when(node.getParent()).thenReturn(parent);
        when(node.isFunction()).thenReturn(true);
        
        boolean shouldRetraverse = pass.shouldRetraverse(node);

        assertTrue(shouldRetraverse);
        verify(compiler, never()).addChangeHandler(any(PeepholeChangeHandler.class));
    }

    @Test
    public void testShouldRetraverse_withScript() {
        Node parent = mock(Node.class);
        Node node = mock(Node.class);
        when(node.getParent()).thenReturn(parent);
        when(node.isFunction()).thenReturn(false);
        when(node.isScript()).thenReturn(true);
        
        boolean shouldRetraverse = pass.shouldRetraverse(node);

        assertTrue(shouldRetraverse);
        verify(compiler, never()).addChangeHandler(any(PeepholeChangeHandler.class));
    }

    @Test
    public void testShouldRetraverse_notInFunctionOrScript() {
        Node parent = mock(Node.class);
        Node node = mock(Node.class);
        when(node.getParent()).thenReturn(parent);
        when(node.isFunction()).thenReturn(false);
        when(node.isScript()).thenReturn(false);
        
        boolean shouldRetraverse = pass.shouldRetraverse(node);

        assertFalse(shouldRetraverse);
        verify(compiler, never()).addChangeHandler(any(PeepholeChangeHandler.class));
    }

    @Test
    public void testShouldVisit_inFunction() {
        Node node = mock(Node.class);
        when(node.isFunction()).thenReturn(true);
        
        boolean shouldVisit = pass.shouldVisit(node);

        assertTrue(shouldVisit);
        verify(compiler).addChangeHandler(any(PeepholeChangeHandler.class));
    }

    @Test
    public void testShouldVisit_notInFunction() {
        Node node = mock(Node.class);
        when(node.isFunction()).thenReturn(false);
        
        boolean shouldVisit = pass.shouldVisit(node);

        assertTrue(shouldVisit);
        verify(compiler, never()).addChangeHandler(any(PeepholeChangeHandler.class));
    }

    @Test
    public void testExitNode() {
        Node node = mock(Node.class);

        pass.exitNode(node);

        verify(compiler).removeChangeHandler(any(PeepholeChangeHandler.class));
    }

    @Test
    public void testVisit_noOptimization() {
        Node node = mock(Node.class);
        AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
        pass = new PeepholeOptimizationsPass(compiler, optimizations);

        pass.visit(node);

        verify(node, never()).getFirstChild();
        verifyZeroInteractions(node);
    }

    @Test
    public void testVisit_singleOptimization_noChange() {
        Node node = mock(Node.class);
        AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);
        AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[]{optimization};
        pass = new PeepholeOptimizationsPass(compiler, optimizations);
        when(optimization.optimizeSubtree(node)).thenReturn(node);

        pass.visit(node);

        verify(node).getFirstChild();
        verify(optimization).optimizeSubtree(node);
        verifyZeroInteractions(node);
    }

    @Test
    public void testVisit_singleOptimization_withChange() {
        Node node = mock(Node.class);
        Node newNode = mock(Node.class);
        AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);
        AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[]{optimization};
        pass = new PeepholeOptimizationsPass(compiler, optimizations);
        when(optimization.optimizeSubtree(node)).thenReturn(newNode);

        pass.visit(node);

        verify(node).getFirstChild();
        verify(optimization).optimizeSubtree(node);
        verify(node).replaceWith(newNode);
    }

    @Test
    public void testVisit_multipleOptimizations_noChange() {
        Node node = mock(Node.class);
        AbstractPeepholeOptimization optimization1 = mock(AbstractPeepholeOptimization.class);
        AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);
        AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[]{optimization1, optimization2};
        pass = new PeepholeOptimizationsPass(compiler, optimizations);
        when(optimization1.optimizeSubtree(node)).thenReturn(node);
        when(optimization2.optimizeSubtree(node)).thenReturn(node);

        pass.visit(node);

        verify(node, times(2)).getFirstChild();
        verify(optimization1).optimizeSubtree(node);
        verify(optimization2).optimizeSubtree(node);
        verifyZeroInteractions(node);
    }

    @Test
    public void testVisit_multipleOptimizations_withChange() {
        Node node = mock(Node.class);
        Node newNode = mock(Node.class);
        AbstractPeepholeOptimization optimization1 = mock(AbstractPeepholeOptimization.class);
        AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);
        AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[]{optimization1, optimization2};
        pass = new PeepholeOptimizationsPass(compiler, optimizations);
        when(optimization1.optimizeSubtree(node)).thenReturn(newNode);
        when(optimization2.optimizeSubtree(newNode)).thenReturn(newNode);

        pass.visit(node);

        verify(node, times(2)).getFirstChild();
        verify(optimization1).optimizeSubtree(node);
        verify(optimization2).optimizeSubtree(newNode);
        verify(node).replaceWith(newNode);
    }

    @Test
    public void testBeginTraversal() {
        AbstractPeepholeOptimization optimization1 = mock(AbstractPeepholeOptimization.class);
        AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);
        AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[]{optimization1, optimization2};
        pass = new PeepholeOptimizationsPass(compiler, optimizations);

        pass.beginTraversal();

        verify(optimization1).beginTraversal(compiler);
        verify(optimization2).beginTraversal(compiler);
    }

    @Test
    public void testEndTraversal() {
        AbstractPeepholeOptimization optimization1 = mock(AbstractPeepholeOptimization.class);
        AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);
        AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[]{optimization1, optimization2};
        pass = new PeepholeOptimizationsPass(compiler, optimizations);

        pass.endTraversal();

        verify(optimization1).endTraversal(compiler);
        verify(optimization2).endTraversal(compiler);
    }

  private class PeepholeChangeHandler implements CodeChangeHandler {
    @Override
    public void reportChange() {
      pass.traversalState.peek().changed = true;
    }
  }
}