package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PeepholeOptimizationsPassTest {
    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);

        Node externs = new Node();
        Node root = new Node();

        pass.process(externs, root);

        // Assertions
        assertEquals(compiler, pass.getCompiler());
    }

    @Test
    public void testShouldRetraverse_withChangedScope() {
        PeepholeOptimizationsPass.ScopeState state = new PeepholeOptimizationsPass.ScopeState();
        state.changed = true;

        PeepholeOptimizationsPass traversal = new PeepholeOptimizationsPass();
        traversal.traversalState.push(state);

        Node node = new Node();
        node.setParent(new Node());

        assertTrue(traversal.shouldRetraverse(node));
    }

    @Test
    public void testShouldRetraverse_withUnchangedScope() {
        PeepholeOptimizationsPass.ScopeState state = new PeepholeOptimizationsPass.ScopeState();
        state.changed = false;

        PeepholeOptimizationsPass traversal = new PeepholeOptimizationsPass();
        traversal.traversalState.push(state);

        Node node = new Node();
        node.setParent(new Node());

        assertFalse(traversal.shouldRetraverse(node));
    }

    @Test
    public void testShouldRetraverse_withNoParent() {
        PeepholeOptimizationsPass.ScopeState state = new PeepholeOptimizationsPass.ScopeState();
        state.changed = true;

        PeepholeOptimizationsPass traversal = new PeepholeOptimizationsPass();
        traversal.traversalState.push(state);

        Node node = new Node();
        node.setParent(null);

        assertFalse(traversal.shouldRetraverse(node));
    }

    @Test
    public void testShouldRetraverse_withFunctionParent() {
        PeepholeOptimizationsPass.ScopeState state = new PeepholeOptimizationsPass.ScopeState();
        state.changed = true;

        PeepholeOptimizationsPass traversal = new PeepholeOptimizationsPass();
        traversal.traversalState.push(state);

        Node node = new Node();
        node.setParent(new Node(Node.FUNCTION));

        assertTrue(traversal.shouldRetraverse(node));
    }

    @Test
    public void testShouldRetraverse_withScriptParent() {
        PeepholeOptimizationsPass.ScopeState state = new PeepholeOptimizationsPass.ScopeState();
        state.changed = true;

        PeepholeOptimizationsPass traversal = new PeepholeOptimizationsPass();
        traversal.traversalState.push(state);

        Node node = new Node();
        node.setParent(new Node(Node.SCRIPT));

        assertTrue(traversal.shouldRetraverse(node));
    }

    @Test
    public void testShouldVisit_withFunctionParentAndTraverseChildScopesTrue() {
        PeepholeOptimizationsPass.ScopeState state = new PeepholeOptimizationsPass.ScopeState();
        state.traverseChildScopes = true;

        PeepholeOptimizationsPass traversal = new PeepholeOptimizationsPass();
        traversal.traversalState.push(state);

        Node node = new Node();
        node.setParent(new Node(Node.FUNCTION));

        assertTrue(traversal.shouldVisit(node));
    }

    @Test
    public void testShouldVisit_withFunctionParentAndTraverseChildScopesFalse() {
        PeepholeOptimizationsPass.ScopeState state = new PeepholeOptimizationsPass.ScopeState();
        state.traverseChildScopes = false;

        PeepholeOptimizationsPass traversal = new PeepholeOptimizationsPass();
        traversal.traversalState.push(state);

        Node node = new Node();
        node.setParent(new Node(Node.FUNCTION));

        assertFalse(traversal.shouldVisit(node));
    }

    @Test
    public void testShouldVisit_withNonFunctionParent() {
        PeepholeOptimizationsPass.ScopeState state = new PeepholeOptimizationsPass.ScopeState();
        state.traverseChildScopes = true;

        PeepholeOptimizationsPass traversal = new PeepholeOptimizationsPass();
        traversal.traversalState.push(state);

        Node node = new Node();
        node.setParent(new Node(Node.SCRIPT));

        assertTrue(traversal.shouldVisit(node));
    }

    @Test
    public void testShouldVisit_withNonFunctionParentAndTraverseChildScopesFalse() {
        PeepholeOptimizationsPass.ScopeState state = new PeepholeOptimizationsPass.ScopeState();
        state.traverseChildScopes = false;

        PeepholeOptimizationsPass traversal = new PeepholeOptimizationsPass();
        traversal.traversalState.push(state);

        Node node = new Node();
        node.setParent(new Node(Node.SCRIPT));

        assertTrue(traversal.shouldVisit(node));
    }

    @Test
    public void testExitNode_withFunctionOrScriptParent() {
        PeepholeOptimizationsPass traversal = new PeepholeOptimizationsPass();
        traversal.traversalState.push(new PeepholeOptimizationsPass.ScopeState());

        Node node = new Node();
        node.setParent(new Node(Node.FUNCTION));

        traversal.exitNode(node);

        assertEquals(0, traversal.traversalState.currentDepth);
    }

    @Test
    public void testExitNode_withoutFunctionOrScriptParent() {
        PeepholeOptimizationsPass traversal = new PeepholeOptimizationsPass();
        traversal.traversalState.push(new PeepholeOptimizationsPass.ScopeState());

        Node node = new Node();
        node.setParent(new Node(Node.SCRIPT));

        traversal.exitNode(node);

        assertEquals(0, traversal.traversalState.currentDepth);
    }

    @Test
    public void testVisit() {
        AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                return null;
            }
        };

        PeepholeOptimizationsPass traversal = new PeepholeOptimizationsPass();
        traversal.peepholeOptimizations = new AbstractPeepholeOptimization[]{optimization};

        Node node = new Node();
        traversal.visit(node);
    }

    @Test
    public void testBeginTraversal() {
        AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
            @Override
            public void beginTraversal(AbstractCompiler compiler) {}
        };

        PeepholeOptimizationsPass traversal = new PeepholeOptimizationsPass();
        traversal.peepholeOptimizations = new AbstractPeepholeOptimization[]{optimization};

        traversal.beginTraversal();
    }

    @Test
    public void testEndTraversal() {
        AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
            @Override
            public void endTraversal(AbstractCompiler compiler) {}
        };

        PeepholeOptimizationsPass traversal = new PeepholeOptimizationsPass();
        traversal.peepholeOptimizations = new AbstractPeepholeOptimization[]{optimization};

        traversal.endTraversal();
    }
}