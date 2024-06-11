package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.Deque;
import java.util.LinkedList;

public class UnreachableCodeEliminationTest {
    private static final Node EMPTY_NODE = new Node(Token.END, 0, null);
    private static AbstractCompiler compiler;
    private static Node rootNode;

    @org.junit.BeforeClass
    public static void setUpBeforeClass() throws Exception {
        compiler = new AbstractCompiler();
        rootNode = new Node(Token.SCRIPT, 1, null);
    }

    @org.junit.AfterClass
    public static void tearDownAfterClass() throws Exception {
        compiler = null;
        rootNode = null;
    }

    @org.junit.Test
    public void testUnreachableCodeElimination() {
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
        NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
        elimination.process(null, rootNode);
        NodeTraversal.traverse(compiler, rootNode, elimination);

        checkControlFlowGraph(elimination.curCfg, rootNode);
    }

    @org.junit.Test
    public void testRemoveNoOpStatements() {
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
        NodeTraversal traversal = new NodeTraversal(compiler, rootNode, null);
        elimination.process(null, rootNode);
        NodeTraversal.traverse(compiler, rootNode, elimination);

        checkControlFlowGraph(elimination.curCfg, rootNode);
    }

    @org.junit.Test
    public void testRemoveDeadCodeFollowingReturn() {
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
        Node root = createReturnStatementNode();
        NodeTraversal traversal = new NodeTraversal(compiler, root, null);
        elimination.process(null, root);
        NodeTraversal.traverse(compiler, root, elimination);

        Node result = elimination.curCfg.getEntry().getValue();
        assertNull(result.getFirstChild());
        assertFalse(result.hasChildren());
    }

    @org.junit.Test
    public void testRemoveDeadCodeWithSideEffects() {
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
        Node root = createSideEffectNode();
        NodeTraversal traversal = new NodeTraversal(compiler, root, null);
        elimination.process(null, root);
        NodeTraversal.traverse(compiler, root, elimination);

        Node result = elimination.curCfg.getEntry().getValue();
        assertNotNull(result);
        assertTrue(result.hasChildren());
    }

    @org.junit.Test
    public void testTryRemoveUnconditionalBranching() {
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
        Node root = createBranchStatementNode();
        NodeTraversal traversal = new NodeTraversal(compiler, root, null);
        elimination.process(null, root);
        NodeTraversal.traverse(compiler, root, elimination);

        Node result = elimination.curCfg.getEntry().getValue();
        assertNotNull(result);
        assertTrue(result.hasChildren());
    }

    @org.junit.Test
    public void testTryRemoveUnconditionalBranchingWithDeadTarget() {
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
        Node root = createBranchWithDeadTargetNode();
        NodeTraversal traversal = new NodeTraversal(compiler, root, null);
        elimination.process(null, root);
        NodeTraversal.traverse(compiler, root, elimination);

        Node result = elimination.curCfg.getEntry().getValue();
        assertNotNull(result);
        assertTrue(result.hasChildren());
        Node target = result.getFirstChild();
        assertNull(target);
    }

    @org.junit.Test
    public void testTryRemoveUnconditionalBranchingWithDeadSource() {
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
        Node root = createDeadSourceBranchNode();
        NodeTraversal traversal = new NodeTraversal(compiler, root, null);
        elimination.process(null, root);
        NodeTraversal.traverse(compiler, root, elimination);

        Node result = elimination.curCfg.getEntry().getValue();
        assertNotNull(result);
        assertTrue(result.hasChildren());
        Node source = result.getLastChild();
        assertNull(source);
    }

    @org.junit.Test
    public void testTryRemoveUnconditionalBranchingWithDeadSourceAndTarget() {
        UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
        Node root = createDeadSourceAndTargetBranchNode();
        NodeTraversal traversal = new NodeTraversal(compiler, root, null);
        elimination.process(null, root);
        NodeTraversal.traverse(compiler, root, elimination);

        Node result = elimination.curCfg.getEntry().getValue();
        assertNotNull(result);
        assertTrue(result.hasChildren());
        Node source = result.getLastChild();
        Node target = result.getFirstChild();
        assertNull(source);
        assertNull(target);
    }

    private static Node createReturnStatementNode() {
        Node root = new Node(Token.SCRIPT, 1, null);
        root.addChild(new Node(Token.IF, 2, root));
        Node condition = new Node(Token.IDENT, "condition", 1, root);
        root.addChild(condition);
        root.addChild(new Node(Token.RETURN, 1, root));
        return root;
    }

    private static Node createSideEffectNode() {
        Node root = new Node(Token.SCRIPT, 1, null);
        root.addChild(new Node(Token.ASSIGN, 1, root, new Node(Token.NUMBER, 5, null), null));
        return root;
    }

    private static Node createBranchStatementNode() {
        Node root = new Node(Token.SCRIPT, 1, null);
        root.addChild(new Node(Token.IF, 2, root));
        Node condition = new Node(Token.IDENT, "condition", 1, root);
        root.addChild(condition);
        root.addChild(new Node(Token.BREAK, 1, root));
        return root;
    }

    private static Node createBranchWithDeadTargetNode() {
        Node root = new Node(Token.SCRIPT, 1, null);
        root.addChild(new Node(Token.IF, 2, root));
        Node condition = new Node(Token.IDENT, "condition", 1, root);
        root.addChild(condition);
        root.addChild(new Node(Token.RETURN, 1, null));
        return root;
    }

    private static Node createDeadSourceBranchNode() {
        Node root = new Node(Token.SCRIPT, 1, null);
        root.addChild(new Node(Token.IF, 2, root));
        Node condition = new Node(Token.IDENT, "condition", 1, root);
        root.addChild(condition);
        root.addChild(new Node(Token.RETURN, 1, root));
        root.addChild(new Node(Token.IDENT, "dead", 1, root));
        return root;
    }

    private static Node createDeadSourceAndTargetBranchNode() {
        Node root = new Node(Token.SCRIPT, 1, null);
        root.addChild(new Node(Token.IF, 2, root));
        Node condition = new Node(Token.IDENT, "condition", 1, root);
        root.addChild(condition);
        root.addChild(new Node(Token.RETURN, 1, null));
        root.addChild(new Node(Token.IDENT, "deadSource", 1, root));
        root.addChild(new Node(Token.IDENT, "deadTarget", 1, root));
        return root;
    }

    private static void checkControlFlowGraph(ControlFlowGraph<Node> cfg, Node node) {
        Deque<Node> queue = new LinkedList<Node>();
        queue.add(node);
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            DiGraphNode<Node, Branch> gNode = cfg.getDirectedGraphNode(current);
            List<DiGraphEdge<Node, Branch>> outEdges = gNode.getOutEdges();
            for (DiGraphEdge<Node, Branch> edge : outEdges) {
                Node next = edge.getDestination().getValue();
                queue.add(next);
            }
            assertNotNull(gNode);
        }
    }

    private static void assertNotNull(Node node) {
        assertTrue(node != null);
    }

    private static void assertNull(Node node) {
        assertTrue(node == null);
    }
}