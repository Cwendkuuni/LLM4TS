import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class PeepholeOptimizationsPassTest {

    @Mock
    AbstractCompiler compiler;
    @Mock
    AbstractPeepholeOptimization optimization;

    @Test
    public void shouldReportChangeWhenOptimizationChangesNode() {
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
        PeepholeOptimizationsPass.StateStack stateStack = mock(PeepholeOptimizationsPass.StateStack.class);
        pass.traversalState = stateStack;
        Node node = mock(Node.class);
        Node newVersionOfNode = mock(Node.class);

        when(optimization.optimizeSubtree(node)).thenReturn(newVersionOfNode);
        when(stateStack.peek()).thenReturn(new PeepholeOptimizationsPass.ScopeState());
        pass.visit(node);

        verify(stateStack.peek()).changed = true;
    }

    @Test
    public void shouldNotReportChangeWhenOptimizationDoesNotChangeNode() {
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
        PeepholeOptimizationsPass.StateStack stateStack = mock(PeepholeOptimizationsPass.StateStack.class);
        pass.traversalState = stateStack;
        Node node = mock(Node.class);

        when(optimization.optimizeSubtree(node)).thenReturn(node);
        when(stateStack.peek()).thenReturn(new PeepholeOptimizationsPass.ScopeState());
        pass.visit(node);

        verify(stateStack.peek(), never()).changed = true;
    }

    @Test
    public void shouldStopVisitingNodeWhenOptimizationOptimizesToNull() {
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
        PeepholeOptimizationsPass.StateStack stateStack = mock(PeepholeOptimizationsPass.StateStack.class);
        pass.traversalState = stateStack;
        Node node = mock(Node.class);

        when(optimization.optimizeSubtree(node)).thenReturn(null);
        when(stateStack.peek()).thenReturn(new PeepholeOptimizationsPass.ScopeState());
        pass.visit(node);

        verify(stateStack, never()).changed = true;
    }

    @Test
    public void shouldPushStateWhenVisitingFunctionOrScriptNode() {
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
        PeepholeOptimizationsPass.StateStack stateStack = new PeepholeOptimizationsPass.StateStack();
        pass.traversalState = stateStack;
        Node functionNode = mock(Node.class);
        Node scriptNode = mock(Node.class);

        when(functionNode.isFunction()).thenReturn(true);
        when(scriptNode.isScript()).thenReturn(true);

        pass.shouldVisit(functionNode);
        pass.shouldVisit(scriptNode);

        assertEquals(3, stateStack.states.size());
    }

    @Test
    public void shouldNotPushStateWhenTraverseChildScopesIsFalse() {
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
        PeepholeOptimizationsPass.StateStack stateStack = new PeepholeOptimizationsPass.StateStack();
        pass.traversalState = stateStack;
        Node functionNode = mock(Node.class);

        when(functionNode.isFunction()).thenReturn(true);
        when(stateStack.peek().traverseChildScopes).thenReturn(false);

        pass.shouldVisit(functionNode);

        assertEquals(1, stateStack.states.size());
    }

    @Test
    public void shouldPopStateWhenExitingFunctionOrScriptNode() {
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
        PeepholeOptimizationsPass.StateStack stateStack = new PeepholeOptimizationsPass.StateStack();
        stateStack.push();
        pass.traversalState = stateStack;
        Node functionNode = mock(Node.class);
        Node scriptNode = mock(Node.class);

        when(functionNode.isFunction()).thenReturn(true);
        when(scriptNode.isScript()).thenReturn(true);

        pass.exitNode(functionNode);
        pass.exitNode(scriptNode);

        assertEquals(1, stateStack.states.size());
    }
}