
package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PeepholeOptimizationsPassTest {
    private PeepholeOptimizationsPass pass;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        pass = new PeepholeOptimizationsPass(compiler);
    }

    @Test
    public visit_AllOptimizationsCalled() {
        MockPeepholeOptimization[] optimizations = new MockPeepholeOptimization[5];
        for (int i = 0; i < 5; i++) {
            optimizations[i] = new MockPeepholeOptimization();
        }

        pass = new PeepholeOptimizationsPass(compiler, optimizations);

        Node node = new Node(Token.FUNCTION);
        pass.visit(node);

        for (MockPeepholeOptimization optimization : optimizations) {
            assertEquals(1, optimization.optimizeSubtreeCallCount());
        }
    }

    // TODO: Write test cases for other methods in PeepholeOptimizationsPass

    private class MockPeepholeOptimization extends AbstractPeepholeOptimization {
        private int optimizeSubtreeCallCount = 0;

        @Override
        Node optimizeSubtree(Node node) {
            optimizeSubtreeCallCount++;
            return node;
        }

        public int optimizeSubtreeCallCount() {
            return optimizeSubtreeCallCount;
        }
    }
}
