package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.rhino.Node;

import java.util.ArrayList;

public class PeepholeOptimizationsPassTest {

    @Test
    public void testPeepholeOptimizationsPass() {
        // Test empty optimization list
        PeepholeOptimizationsPass emptyOptimizationsPass = new PeepholeOptimizationsPass(null);
        Node root = Node.newObjectLiteral();
        emptyOptimizationsPass.process(null, root);

        // Test optimization list with no optimizations
        AbstractPeepholeOptimization noOptimization = new AbstractPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                return node;
            }
        };
        PeepholeOptimizationsPass noOptimizationsPass = new PeepholeOptimizationsPass(null, noOptimization);
        Node rootForNoOptimizationsPass = Node.newObjectLiteral();
        noOptimizationsPass.process(null, rootForNoOptimizationsPass);

        // Test optimization list with one optimization
        AbstractPeepholeOptimization singleOptimization = new AbstractPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                if (node.isNumberLiteral()) {
                    return Node.newNumberLiteral(node.getInt() + 1);
                }
                return node;
            }
        };
        PeepholeOptimizationsPass singleOptimizationsPass = new PeepholeOptimizationsPass(null, singleOptimization);
        Node rootForSingleOptimizationsPass = Node.newObjectLiteral();
        rootForSingleOptimizationsPass.setLocal("x", Node.newNumberLiteral(1));
        singleOptimizationsPass.process(null, rootForSingleOptimizationsPass);
        Node x = rootForSingleOptimizationsPass.getLocal("x");
        assertTrue(x.isNumberLiteral());
        assertEquals(new Long(2), x.asNumber().longValue());

        // Test optimization list with multiple optimizations
        AbstractPeepholeOptimization firstOptimization = new AbstractPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                if (node.isNumberLiteral()) {
                    return Node.newNumberLiteral(node.getInt() + 1);
                }
                return node;
            }
        };
        AbstractPeepholeOptimization secondOptimization = new AbstractPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                if (node.isStringLiteral()) {
                    String str = node.asString();
                    return Node.newStringLiteral(str.substring(1, str.length() - 1));
                }
                return node;
            }
        };
        PeepholeOptimizationsPass multipleOptimizationsPass = new PeepholeOptimizationsPass(null, firstOptimization, secondOptimization);
        Node rootForMultipleOptimizationsPass = Node.newObjectLiteral();
        rootForMultipleOptimizationsPass.setLocal("x", Node.newNumberLiteral(1));
        rootForMultipleOptimizationsPass.setLocal("y", Node.newStringLiteral("hello world"));
        multipleOptimizationsPass.process(null, rootForMultipleOptimizationsPass);
        Node x = rootForMultipleOptimizationsPass.getLocal("x");
        assertTrue(x.isNumberLiteral());
        assertEquals(new Long(2), x.asNumber().longValue());
        Node y = rootForMultipleOptimizationsPass.getLocal("y");
        assertTrue(y.isStringLiteral());
        assertEquals("hello", y.asString());

        // Test optimization list with conflicting optimizations
        AbstractPeepholeOptimization firstConflictingOptimization = new AbstractPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                if (node.isNumberLiteral()) {
                    return Node.newNumberLiteral(node.getInt() + 1);
                }
                return node;
            }
        };
        AbstractPeepholeOptimization secondConflictingOptimization = new AbstractPeepholeOptimization() {
            @Override
            public Node optimizeSubtree(Node node) {
                if (node.isNumberLiteral()) {
                    return Node.newNumberLiteral(node.getInt() - 1);
                }
                return node;
            }
        };
        PeepholeOptimizationsPass conflictingOptimizationsPass = new PeepholeOptimizationsPass(null, firstConflictingOptimization, secondConflictingOptimization);
        Node rootForConflictingOptimizationsPass = Node.newObjectLiteral();
        rootForConflictingOptimizationsPass.setLocal("x", Node.newNumberLiteral(1));
        conflictingOptimizationsPass.process(null, rootForConflictingOptimizationsPass);
        Node x = rootForConflictingOptimizationsPass.getLocal("x");
        assertTrue(x.isNumberLiteral());
        assertEquals(new Long(0), x.asNumber().longValue());
    }

    @Test
    public void testTraversalState() {
        // Test resetting traversal state
        PeepholeOptimizationsPass peepholeOptimizationsPass = new PeepholeOptimizationsPass(null);
        ScopeState state = new ScopeState();
        state.reset();
        assertTrue(state.changed);
        assertTrue(state.traverseChildScopes);

        // Test pushing and popping traversal state
        peepholeOptimizationsPass = new PeepholeOptimizationsPass(null);
        state = new ScopeState();
        peepholeOptimizationsPass.traversalState.push();
        state = peepholeOptimizationsPass.traversalState.peek();
        assertFalse(state.changed);
        assertFalse(state.traverseChildScopes);
        peepholeOptimizationsPass.traversalState.pop();
        state = peepholeOptimizationsPass.traversalState.peek();
        assertTrue(state.changed);
        assertTrue(state.traverseChildScopes);
    }
}