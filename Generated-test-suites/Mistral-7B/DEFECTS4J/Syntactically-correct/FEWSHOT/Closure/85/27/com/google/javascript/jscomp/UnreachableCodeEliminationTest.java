package org.apache.commons.cli.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

public class UnreachableCodeEliminationTest {

    @Test
    public void testRemoveDeadExprStatementSafely() {
        Node root = Node.newBlock(2);
        Node n1 = Node.newLiteral(1);
        Node n2 = Node.newReturn(Node.newLiteral(2));
        Node n3 = Node.newLiteral(3);
        Node n4 = Node.newReturn(Node.newLiteral(3));
        Node n5 = Node.newLiteral(4);

        root.setFirstChild(n1);
        root.setLastChild(n5);
        root.setNext(n2);

        NodeTraversal nodeTraversal = NodeTraversal.create(null, null, null);
        UnreachableCodeElimination unreachableCodeElimination = new UnreachableCodeElimination(null, false);
        unreachableCodeElimination.process(null, root);

        NodeTraversal.traverse(null, root, new NodeVisitor() {
            @Override
            public void visit(NodeTraversal traversal, Node node, Node parent) {
                if (node.getParent() == root) {
                    assertNull(node.getNext());
                    assertNull(node.getLastChild());
                    if (node.getType() == Token.RETURN) {
                        assertNull(node.getFirstChild());
                    } else {
                        assertTrue(node.hasChildren());
                    }
                }
            }
        });

        // Test unreachable node that has no side effects
        Node unreachableNode = Node.newLiteral(0);
        root.setLastChild(unreachableNode);
        NodeTraversal.traverse(null, root, unreachableCodeElimination);
        assertFalse(NodeTraversal.traverse(null, root, new NodePredicate() {
            @Override
            public boolean matches(Node node, Node parent) {
                return node.getValue().equals(unreachableNode);
            }
        }).hasNext());

        // Test unreachable node that has side effects and remove it safely
        Node sideEffectNode = Node.newAssignment(Node.newLocal("x"), Node.newLiteral(5));
        root.setLastChild(sideEffectNode);
        NodeTraversal.traverse(null, root, unreachableCodeElimination);
        assertFalse(NodeTraversal.traverse(null, root, new NodePredicate() {
            @Override
            public boolean matches(Node node, Node parent) {
                return node.getValue().equals(sideEffectNode);
            }
        }).hasNext());

        // Test unreachable statement with local variable
        Node localVariableNode = Node.newVar("x");
        Node statementNode = Node.newAssignment(localVariableNode, Node.newLiteral(5));
        Node statementBlockNode = Node.newBlock(1);
        statementBlockNode.setFirstChild(statementNode);
        root.setLastChild(statementBlockNode);
        NodeTraversal.traverse(null, root, unreachableCodeElimination);
        assertFalse(NodeTraversal.traverse(null, root, new NodePredicate() {
            @Override
            public boolean matches(Node node, Node parent) {
                return node.getValue().equals(statementBlockNode);
            }
        }).hasNext());
        assertFalse(NodeTraversal.traverse(null, root, new NodePredicate() {
            @Override
            public boolean matches(Node node, Node parent) {
                return node.getValue().equals(statementNode);
            }
        }).hasNext());
    }
}

class NodeVisitor extends AbstractNodeVisitor<Void> {
    // Implement NodeVisitor interface methods here
}

class NodePredicate extends AbstractNodePredicate<Node> {
    // Implement NodePredicate interface methods here
}