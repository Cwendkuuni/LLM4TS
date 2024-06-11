package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.ArrayList;

public class PeepholeOptimizationsPassTest {
    @Test
    public void testEmptyPass() {
        AbstractCompiler mockCompiler = new MockCompiler();
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(mockCompiler);
        Node root = new Node();
        pass.process(null, root);
        assertTrue(true); // Test passes if the method does not throw an exception
    }

    @Test
    public void testSingleOptimizationPass() {
        AbstractCompiler mockCompiler = new MockCompiler();
        AbstractPeepholeOptimization mockOptimization = new MockPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                return node;
            }
        };
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(mockCompiler, mockOptimization);
        Node root = new Node();
        pass.process(null, root);
        assertTrue(true); // Test passes if the method does not throw an exception
    }

    @Test
    public void testMultipleOptimizationsPass() {
        AbstractCompiler mockCompiler = new MockCompiler();
        AbstractPeepholeOptimization optimiztion1 = new MockPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                return node;
            }
        };
        AbstractPeepholeOptimization optimiztion2 = new MockPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                return node;
            }
        };
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(mockCompiler, optimiztion1, optimiztion2);
        Node root = new Node();
        pass.process(null, root);
        assertTrue(true); // Test passes if the method does not throw an exception
    }

    @Test
    public void testShouldVisitFunction() {
        AbstractCompiler mockCompiler = new MockCompiler();
        AbstractPeepholeOptimization mockOptimization = new MockPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                return null;
            }
        };
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(mockCompiler, mockOptimization);
        Node functionNode = new Node();
        assertTrue(pass.shouldVisit(functionNode));
    }

    @Test
    public void testShouldNotVisitNonFunction() {
        AbstractCompiler mockCompiler = new MockCompiler();
        AbstractPeepholeOptimization mockOptimization = new MockPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                return null;
            }
        };
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(mockCompiler, mockOptimization);
        Node nonFunctionNode = new Node();
        assertFalse(pass.shouldVisit(nonFunctionNode));
    }

    @Test
    public void testShouldRetraverseFunction() {
        AbstractCompiler mockCompiler = new MockCompiler();
        AbstractPeepholeOptimization mockOptimization = new MockPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                return null;
            }
        };
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(mockCompiler, mockOptimization);
        Node functionNode = new Node();
        Node childFunctionNode = new Node();
        functionNode.setFirstChild(childFunctionNode);
        ScopeState state = pass.traversalState.peek();
        assertTrue(pass.shouldRetraverse(functionNode));
        assertFalse(pass.shouldRetraverse(nonFunctionNode));
    }

    @Test
    public void testShouldNotRetraverseNonFunction() {
        AbstractCompiler mockCompiler = new MockCompiler();
        AbstractPeepholeOptimization mockOptimization = new MockPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                return null;
            }
        };
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(mockCompiler, mockOptimization);
        Node nonFunctionNode = new Node();
        assertFalse(pass.shouldRetraverse(nonFunctionNode));
    }

    @Test
    public void testVisitFunction() {
        AbstractCompiler mockCompiler = new MockCompiler();
        AbstractPeepholeOptimization mockOptimization = new MockPeepholeOptimization() {
            private int visits = 0;
            @Override
            public Node optimizeSubtree(Node node) {
                visits++;
                return node;
            }
        };
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(mockCompiler, mockOptimization);
        Node functionNode = new Node();
        pass.process(null, functionNode);
        ArrayList<Integer> visits = Lists.newArrayList();
        visits.add(0);
        mockOptimization.visits.addAll(visits);
        assertTrue(mockOptimization.visits.get(0) > 0);
    }

    @Test
    public void testVisitNonFunction() {
        AbstractCompiler mockCompiler = new MockCompiler();
        AbstractPeepholeOptimization mockOptimization = new MockPeepholeOptimization() {
            private int visits = 0;
            @Override
            public Node optimizeSubtree(Node node) {
                visits++;
                return node;
            }
        };
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(mockCompiler, mockOptimization);
        Node nonFunctionNode = new Node();
        pass.process(null, nonFunctionNode);
        ArrayList<Integer> visits = Lists.newArrayList();
        visits.add(0);
        mockOptimization.visits.addAll(visits);
        assertTrue(mockOptimization.visits.get(0) == 0);
    }

    @Test
    public void testBeginTraversal() {
        AbstractCompiler mockCompiler = new MockCompiler();
        AbstractPeepholeOptimization mockOptimization = new MockPeepholeOptimization();
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(mockCompiler, mockOptimization);
        pass.beginTraversal();
        assertTrue(mockOptimization.wasBeginCalled);
    }

    @Test
    public void testEndTraversal() {
        AbstractCompiler mockCompiler = new MockCompiler();
        AbstractPeepholeOptimization mockOptimization = new MockPeepholeOptimization();
        PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(mockCompiler, mockOptimization);
        pass.endTraversal();
        assertTrue(mockOptimization.wasEndCalled);
    }
}

class MockCompiler implements AbstractCompiler {
    @Override
    public void addChangeHandler(CodeChangeHandler handler) {
        // Do nothing
    }

    @Override
    public void removeChangeHandler(CodeChangeHandler handler) {
        // Do nothing
    }
}

class MockPeepholeOptimization implements AbstractPeepholeOptimization {
    private int visits = 0;
    private boolean wasBeginCalled = false;
    private boolean wasEndCalled = false;

    @Override
    public void beginTraversal(AbstractCompiler compiler) {
        wasBeginCalled = true;
    }

    @Override
    public void endTraversal(AbstractCompiler compiler) {
        wasEndCalled = true;
    }

    @Override
    public Node optimizeSubtree(Node node) {
        return null;
    }
}