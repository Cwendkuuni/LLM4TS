package com.google.javascript.jscomp;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.graph.GraphReachability;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphNode;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {

    @Test
    public void testEnterScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        NodeTraversal t = new NodeTraversal(compiler, uce);
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        uce.enterScope(t);
    }

    @Test
    public void testExitScope() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        NodeTraversal t = new NodeTraversal(compiler, uce);
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        uce.exitScope(t);
    }

    @Test
    public void testProcess() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        Node externs = new Node(Token.EXTERN);
        Node root = new Node(Token.ROOT);
        uce.process(externs, root);
    }

    @Test
    public void testVisit() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        NodeTraversal t = new NodeTraversal(compiler, uce);
        Node n = new Node(Token.EMPTY);
        Node parent = new Node(Token.SCRIPT);
        uce.visit(t, n, parent);
    }

    @Test
    public void testTryRemoveUnconditionalBranching() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        Node n = new Node(Token.EMPTY);
        Node result = uce.tryRemoveUnconditionalBranching(n);
    }

    @Test
    public void testComputeFollowing() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        Node n = new Node(Token.EMPTY);
        Node result = uce.computeFollowing(n);
    }

    @Test
    public void testRemoveDeadExprStatementSafely() {
        AbstractCompiler compiler = new AbstractCompiler();
        boolean removeNoOpStatements = true;
        UnreachableCodeElimination uce = new UnreachableCodeElimination(compiler, removeNoOpStatements);
        Node n = new Node(Token.EMPTY);
        uce.removeDeadExprStatementSafely(n);
    }
}