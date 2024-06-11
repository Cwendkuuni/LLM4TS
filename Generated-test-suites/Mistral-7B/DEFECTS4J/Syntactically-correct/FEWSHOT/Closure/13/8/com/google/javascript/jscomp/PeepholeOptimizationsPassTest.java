package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class PeepholeOptimizationsPassTest {
    private AbstractCompiler compiler;
    private AbstractPeepholeOptimization optimization1;
    private AbstractPeepholeOptimization optimization2;
    private PeepholeOptimizationsPass pass;

    @Before
    public void setup() {
        compiler = new Compiler();
        optimization1 = new DummyPeepholeOptimization();
        optimization2 = new DummyPeepholeOptimization();
        pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
    }

    @Test
    public void testProcess() {
        Node root = new Node_Function();
        pass.process(null, root);
    }

    @Test
    public void testVisitFunctionOrScript() {
        Node root = new Node_Function();
        pass.traverse(root);
        assertTrue(pass.traversalState.peek().traverseChildScopes);
        pass.traversalState.push();
        Node child = new Node_ExpressionStatement();
        pass.traverse(child);
        assertFalse(pass.traversalState.peek().traverseChildScopes);
        pass.traversalState.pop();
    }

    @Test
    public void testShouldVisit() {
        Node root = new Node_Function();
        assertTrue(pass.shouldVisit(root));

        Node child = new Node_ExpressionStatement();
        assertTrue(pass.shouldVisit(child));
    }

    @Test
    public void testShouldRetraverse() {
        Node root = new Node_Function();
        assertFalse(pass.shouldRetraverse(root));

        Node child1 = new Node_ExpressionStatement();
        Node child2 = new Node_ExpressionStatement();
        Node grandChild = new Node_ExpressionStatement();

        pass.traverse(root);
        pass.traversalState.push();
        pass.traverse(child1);

        // Change the child1 node
        Node newChild1 = new Node_ExpressionStatement();
        pass.visit(child1);
        assertSame(newChild1, child1);

        pass.traversalState.pop();
        assertTrue(pass.shouldRetraverse(child1));

        pass.traversalState.push();
        pass.traverse(child2);
        assertFalse(pass.shouldRetraverse(child2));

        pass.traversalState.push();
        pass.traverse(grandChild);

        // Change the root node
        Node newRoot = new Node_Function();
        pass.visit(root);
        assertSame(newRoot, root);

        assertTrue(pass.shouldRetraverse(root));
    }

    @Test
    public void testTraverse() {
        Node root = new Node_Function();
        Node child1 = new Node_ExpressionStatement();
        Node child2 = new Node_ExpressionStatement();
        Node grandChild = new Node_ExpressionStatement();

        root.setFirstChild(child1);
        child1.setNext(child2);
        child2.setNext(grandChild);

        pass.traverse(root);
        pass.traversalState.push();
        pass.traverse(child1);
        assertNull(pass.traversalState.peek().changed);

        // Change the child1 node
        Node newChild1 = new Node_ExpressionStatement();
        pass.visit(child1);
        assertSame(newChild1, child1);

        assertTrue(pass.traversalState.peek().changed);
        assertTrue(pass.traversalState.peek().traverseChildScopes);

        pass.traversalState.pop();
        assertNull(pass.traversalState.peek().changed);

        pass.traversalState.push();
        pass.traverse(child2);
        assertNull(pass.traversalState.peek().changed);

        pass.traversalState.pop();
        assertNull(pass.traversalState.peek().changed);

        pass.traversalState.push();
        pass.traverse(grandChild);
        assertNull(pass.traversalState.peek().changed);

        pass.traversalState.pop();
        assertNull(pass.traversalState.peek().changed);
    }

    @Test
    public void testBeginAndEndTraversal() {
        AbstractPeepholeOptimization optimization1Spy = spy(optimization1);
        AbstractPeepholeOptimization optimization2Spy = spy(optimization2);

        pass.beginTraversal();
        pass.endTraversal();

        verify(optimization1Spy).beginTraversal(pass.getCompiler());
        verify(optimization2Spy).beginTraversal(pass.getCompiler());
        verifyNoMoreInteractions(optimization1Spy);
        verifyNoMoreInteractions(optimization2Spy);
    }
}

class DummyPeepholeOptimization implements AbstractPeepholeOptimization {
    @Override
    public Node optimizeSubtree(Node node) {
        return node;
    }

    @Override
    public void beginTraversal(AbstractCompiler compiler) {
    }

    @Override
    public void endTraversal(AbstractCompiler compiler) {
    }
}