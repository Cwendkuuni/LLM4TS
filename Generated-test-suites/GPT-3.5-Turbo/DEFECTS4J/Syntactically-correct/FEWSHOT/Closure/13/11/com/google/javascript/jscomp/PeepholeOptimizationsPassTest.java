package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class PeepholeOptimizationsPassTest {
    private AbstractCompiler compiler;
    private AbstractPeepholeOptimization[] peepholeOptimizations;
    private PeepholeOptimizationsPass peepholeOptimizationsPass;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        peepholeOptimizations = new AbstractPeepholeOptimization[2];
        peepholeOptimizations[0] = new TestPeepholeOptimization();
        peepholeOptimizations[1] = new TestPeepholeOptimization();
        peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, peepholeOptimizations);
    }

    @Test
    public void testGetCompiler() {
        assertEquals(compiler, peepholeOptimizationsPass.getCompiler());
    }

    @Test
    public void testProcess() {
        Node externs = new Node();
        Node root = new Node();

        PeepholeChangeHandler handler = new PeepholeChangeHandler();
        compiler.addChangeHandler(handler);

        peepholeOptimizationsPass.process(externs, root);

        compiler.removeChangeHandler(handler);
    }

    private static class TestPeepholeOptimization extends AbstractPeepholeOptimization {
        @Override
        Node optimizeSubtree(Node node) {
            return null;
        }

        @Override
        void beginTraversal(AbstractCompiler compiler) {
        }

        @Override
        void endTraversal(AbstractCompiler compiler) {
        }
    }

    private class PeepholeChangeHandler implements CodeChangeHandler {
        @Override
        public void reportChange() {
        }
    }
}