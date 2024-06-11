package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphNode;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {
    private static final String TEST_SOURCE =
            "function foo() {" +
                    "var x = 1;" +
                    "var y;" +
                    "if (x < 2) {" +
                    "  y = x;" +
                    "} else {" +
                    "  x = 3;" +
                    "  y = 4;" +
                    "}" +
                    "return y;" +
                    "}";

    @Test
    public void testMaybeReachingVariableUse() {
        Scope jsScope = new Scope();
        AbstractCompiler compiler = new AbstractCompiler();
        ControlFlowGraph<Node> controlFlowGraph =
                new ControlFlowGraph<>(compiler, jsScope, null, null);
        MaybeReachingVariableUse maybeReachingVariableUse =
                new MaybeReachingVariableUse(controlFlowGraph, jsScope, compiler);
        Node defNode = NodeUtil.getFirstDefNode(controlFlowGraph.getEntryNode());
        ReachingUses expectedUses = new ReachingUses();
        addToUseIfLocal("x", defNode, expectedUses);
        addToUseIfLocal("y", NodeUtil.getSecondDefNode(defNode), expectedUses);

        Collection<Node> actualUses = maybeReachingVariableUse.getUses("x", defNode);
        assertThat(actualUses).containsExactly(defNode);
        assertNull(maybeReachingVariableUse.getUses("z", defNode));

        expectedUses = new ReachingUses();
        addToUseIfLocal("y", defNode, expectedUses);
        addToUseIfLocal("x", NodeUtil.getAssignmentNode(defNode, 3), expectedUses);

        actualUses = maybeReachingVariableUse.getUses("y", defNode);
        assertThat(actualUses).containsExactly(NodeUtil.getAssignmentNode(defNode, 3));
        assertNull(maybeReachingVariableUse.getUses("z", defNode));

        expectedUses = new ReachingUses();
        addToUseIfLocal("x", NodeUtil.getAssignmentNode(defNode, 3), expectedUses);
        addToUseIfLocal("y", NodeUtil.getSecondDefNode(defNode), expectedUses);

        actualUses = maybeReachingVariableUse.getUses("x", defNode);
        assertNull(actualUses);
        assertNull(maybeReachingVariableUse.getUses("z", defNode));

        expectedUses = new ReachingUses();
        addToUseIfLocal("y", NodeUtil.getSecondDefNode(defNode), expectedUses);

        actualUses = maybeReachingVariableUse.getUses("y", defNode);
        assertThat(actualUses).containsExactly(NodeUtil.getSecondDefNode(defNode));
        assertNull(maybeReachingVariableUse.getUses("z", defNode));
    }
}