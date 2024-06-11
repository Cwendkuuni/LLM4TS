import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PeepholeOptimizationsPassTest {
    private PeepholeOptimizationsPass pass;
    private AbstractCompiler compiler;
    private AbstractPeepholeOptimization optimization;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        optimization = mock(AbstractPeepholeOptimization.class);
        pass = new PeepholeOptimizationsPass(compiler, optimization);
    }

    @Test
    public void testProcess_callsBeginTraversal() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);

        pass.process(externs, root);

        verify(optimization).beginTraversal(compiler);
    }

    @Test
    public void testProcess_callsTraverse() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);

        pass.process(externs, root);

        verify(pass).traverse(root);
    }

    @Test
    public void testProcess_callsEndTraversal() {
        Node externs = mock(Node.class);
        Node root = mock(Node.class);

        pass.process(externs, root);

        verify(optimization).endTraversal(compiler);
    }

    @Test
    public void testTraverse_doesNotVisitFunctionOrScript_whenChangedIsFalse() {
        Node node = mock(Node.class);
        when(node.isFunction()).thenReturn(true);
        when(node.isScript()).thenReturn(true);

        pass.traversalState.peek().changed = false;
        pass.traverse(node);

        verify(node, never()).getFirstChild();
    }

    @Test
    public void testTraverse_callsVisit() {
        Node node = mock(Node.class);
        when(node.isFunction()).thenReturn(false);
        when(node.isScript()).thenReturn(false);
        when(node.getFirstChild()).thenReturn(null);

        pass.traverse(node);

        verify(pass).visit(node);
    }

    @Test
    public void testShouldRetraverse_returnsFalse_whenParentIsNull() {
        Node node = mock(Node.class);
        when(node.getParent()).thenReturn(null);

        boolean shouldRetraverse = pass.shouldRetraverse(node);

        assertFalse(shouldRetraverse);
    }

    @Test
    public void testShouldRetraverse_returnsFalse_whenNodeIsNotFunctionOrScript() {
        Node node = mock(Node.class);
        when(node.getParent()).thenReturn(mock(Node.class));

        boolean shouldRetraverse = pass.shouldRetraverse(node);

        assertFalse(shouldRetraverse);
    }

    @Test
    public void testShouldRetraverse_returnsFalse_whenChangedIsFalse() {
        Node node = mock(Node.class);
        when(node.isFunction()).thenReturn(true);
        when(node.getParent()).thenReturn(mock(Node.class));

        boolean shouldRetraverse = pass.shouldRetraverse(node);

        assertFalse(shouldRetraverse);
    }

    @Test
    public void testShouldRetraverse_changesState_andReturnsTrue_whenChangedIsTrue() {
        Node node = mock(Node.class);
        when(node.isFunction()).thenReturn(true);
        when(node.getParent()).thenReturn(mock(Node.class));

        pass.shouldRetraverse(node);

        assertTrue(pass.traversalState.peek().changed);
        assertFalse(pass.traversalState.peek().traverseChildScopes);
    }

    @Test
    public void testShouldVisit_returnsFalse_whenFunctionScopeAndTraverseChildScopesIsFalse() {
        Node node = mock(Node.class);
        when(node.isFunction()).thenReturn(true);

        pass.traversalState.peek().traverseChildScopes = false;
        boolean shouldVisit = pass.shouldVisit(node);

        assertFalse(shouldVisit);
    }

    @Test
    public void testShouldVisit_pushesState_whenFunctionOrScriptScope() {
        Node node = mock(Node.class);
        when(node.isFunction()).thenReturn(true);

        pass.shouldVisit(node);

        assertEquals(2, pass.traversalState.states.size());
    }

    @Test
    public void testExitNode_popsState_whenFunctionOrScriptScope() {
        Node node = mock(Node.class);
        when(node.isFunction()).thenReturn(true);

        pass.exitNode(node);

        assertEquals(1, pass.traversalState.states.size());
    }

    @Test
    public void testVisit_callsOptimizeSubtree_forAllOptimizations() {
        Node node = mock(Node.class);
        Node currentVersionOfNode = node;

        pass.visit(node);

        verify(optimization).optimizeSubtree(currentVersionOfNode);
    }

    @Test
    public void testVisit_returnsWhenCurrentVersionOfNodeIsNull() {
        Node node = mock(Node.class);
        when(optimization.optimizeSubtree(any(Node.class))).thenReturn(null);

        pass.visit(node);

        verifyZeroInteractions(compiler); // compiler should not be called when currentVersionOfNode is null
    }

    @Test
    public void testBeginTraversal_callsBeginTraversal_forAllOptimizations() {
        pass.beginTraversal();

        verify(optimization).beginTraversal(compiler);
    }

    @Test
    public void testEndTraversal_callsEndTraversal_forAllOptimizations() {
        pass.endTraversal();

        verify(optimization).endTraversal(compiler);
    }
}