package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Node;

import java.util.HashSet;
import java.util.Set;

public class LiveVariablesAnalysisTest {
    private Scope jsScope;
    private ControlFlowGraph<Node> cfg;
    private LiveVariablesAnalysis liveVariablesAnalysis;

    @Before
    public void setUp() {
        // Create a simple Scope for testing
        jsScope = new Scope();
        jsScope.declareLocal("a");
        jsScope.declareLocal("b");
        jsScope.declareLocal("c");

        // Create a ControlFlowGraph for testing
        Node rootNode = new Node(Token.SCRIPT);
        Node n1 = new Node(Token.VAR, "a");
        Node n2 = new Node(Token.ASSIGN, null, n1, new Node(Token.INT_LITERAL, 10));
        Node n3 = new Node(Token.ASSIGN, null, new Node(Token.NAME, "b"), n2);
        Node n4 = new Node(Token.VAR, "c");
        Node n5 = new Node(Token.ASSIGN, null, n4, n3);
        Node n6 = new Node(Token.RETURN, null, n5);
        rootNode.addChild(n1);
        rootNode.addChild(n2);
        rootNode.addChild(n3);
        rootNode.addChild(n4);
        rootNode.addChild(n5);
        rootNode.addChild(n6);
        cfg = new ControlFlowGraph<>(rootNode);

        liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, null);
    }

    @Test
    public void testLiveVariablesAnalysis() {
        Set<Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
        assertTrue(escapedLocals.isEmpty());

        LiveVariableLattice lattice = liveVariablesAnalysis.computeFinalResult();
        assertTrue(lattice.isLive("a"));
        assertTrue(lattice.isLive("b"));
        assertTrue(lattice.isLive("c"));

        Node lp = jsScope.getRootNode().getFirstChild().getNext();
        for(Node arg = lp.getFirstChild(); arg != null; arg = arg.getNext()) {
            Var argVar = jsScope.getVar(arg.getString());
            assertFalse(lattice.isLive(argVar));
        }
    }
}

class ControlFlowGraph<T extends Node> implements DiGraph<T, Branch> {
    private DiGraph<T, Branch> graph;

    ControlFlowGraph(T rootNode) {
        this.graph = new DiGraph<>();
        addEdge(rootNode, null);
    }

    @Override
    public Set<T> getVertices() {
        return new HashSet<>(graph.getVertices());
    }

    @Override
    public List<DiGraphEdge<T, Branch>> getOutEdges(T vertex) {
        return graph.getOutEdges(vertex);
    }

    @Override
    public void addEdge(T from, Branch condition) {
        graph.addEdge(from, condition, null);
    }
}

class NodeUtil {
    static boolean isName(Node node) {
        return node.getType() == Token.NAME;
    }

    static boolean isVar(Node node) {
        return node.getType() == Token.VAR;
    }

    static boolean isAssignmentOp(Node node) {
        return node.getType() == Token.ASSIGN;
    }

    static boolean isAssign(Node node) {
        return node.getFirstChild().getType() == Token.ASSIGN;
    }

    static Node getConditionExpression(Node node) {
        if (node.getType() == Token.IF) {
            return node.getFirstChild();
        } else if (node.getType() == Token.WHILE) {
            return node.getFirstChild().getFirstChild();
        } else if (node.getType() == Token.DO) {
            return node.getFirstChild().getLastChild();
        } else if (node.getType() == Token.FOR) {
            return NodeUtil.getConditionExpression(NodeUtil.getConditionExpression(node));
        }
        return null;
    }
}