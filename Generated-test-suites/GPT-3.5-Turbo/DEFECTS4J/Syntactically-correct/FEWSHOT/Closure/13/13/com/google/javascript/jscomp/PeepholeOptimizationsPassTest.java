package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PeepholeOptimizationsPassTest {

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        AbstractPeepholeOptimization optimization1 = new AbstractPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                return null;
            }
        };

        AbstractPeepholeOptimization optimization2 = new AbstractPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                return null;
            }
        };

        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);

        Node externs = new Node(Node.EXTERN_STATEMENT);
        Node root = new Node(Node.SCRIPT);

        pass.process(externs, root);
    }

    @Test
    public void testShouldVisit() {
        PeepholeOptimizationsPass.ScopeState previous = new PeepholeOptimizationsPass.ScopeState();
        previous.traverseChildScopes = true;

        PeepholeOptimizationsPass.ScopeState state = new PeepholeOptimizationsPass.ScopeState();
        state.traverseChildScopes = true;

        PeepholeOptimizationsPass.StateStack traversalState = new PeepholeOptimizationsPass.StateStack();
        traversalState.states.add(previous);
        traversalState.states.add(state);

        Node node = new Node(Node.SCRIPT);
        node.setIsFunction(true);

        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null);

        assertTrue(pass.shouldVisit(node));

        state.traverseChildScopes = false;

        assertFalse(pass.shouldVisit(node));
    }

    @Test
    public void testShouldRetraverse() {
        PeepholeOptimizationsPass.ScopeState state = new PeepholeOptimizationsPass.ScopeState();
        state.changed = true;

        PeepholeOptimizationsPass.StateStack traversalState = new PeepholeOptimizationsPass.StateStack();
        traversalState.states.add(state);

        Node node = new Node(Node.SCRIPT);
        node.setIsFunction(true);
        node.setParent(new Node(Node.SCRIPT));

        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null);

        assertTrue(pass.shouldRetraverse(node));

        state.changed = false;

        assertFalse(pass.shouldRetraverse(node));
    }

    @Test
    public void testTraverse() {
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null);
        pass.traversalState = new PeepholeOptimizationsPass.StateStack();

        Node node = new Node(Node.SCRIPT);
        node.addChildToFront(new Node(Node.EXPR_RESULT));
        node.addChildToFront(new Node(Node.EXPR_RESULT));

        pass.traverse(node);
    }
}