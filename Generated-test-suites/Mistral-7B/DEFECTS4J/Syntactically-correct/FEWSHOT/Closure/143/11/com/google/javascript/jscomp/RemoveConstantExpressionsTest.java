package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class RemoveConstantExpressionsTest {
    @Test
    public void testRemoveConstantExpressions() {
        RemoveConstantExpressions pass = new RemoveConstantExpressions(null);

        Node root = NodeFactory.createProgram();
        NodeTraversal.traverse(null, root, pass);

        Node expr1 = NodeFactory.createBinaryOp(
                Token.OP_PLUS,
                NodeFactory.createNumberLiteral(1),
                NodeFactory.createIdentifier("foo")
        );
        Node expr2 = NodeFactory.createBinaryOp(
                Token.OP_PLUS,
                expr1,
                NodeFactory.createIdentifier("bar")
        );

        Node expected = NodeFactory.createBinaryOp(
                Token.OP_PLUS,
                NodeFactory.createIdentifier("foo"),
                NodeFactory.createIdentifier("bar")
        );

        Node actual = findNode(root, expr2);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testRemoveConstantExpressionsWithSideEffects() {
        RemoveConstantExpressions pass = new RemoveConstantExpressions(null);

        Node root = NodeFactory.createProgram();
        NodeTraversal.traverse(null, root, pass);

        Node expr1 = NodeFactory.createIdentifier("foo");
        Node expr2 = NodeFactory.createBinaryOp(
                Token.OP_PLUS,
                NodeFactory.createIdentifier("x"),
                expr1
        );
        Node expr3 = NodeFactory.createBinaryOp(
                Token.OP_PLUS,
                expr2,
                NodeFactory.createIdentifier("bar")
        );

        Node expected = NodeFactory.createSequence(expr1, NodeFactory.createIdentifier("bar"));

        Node actual = findNode(root, expr3);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testRemoveConstantExpressionsWithNestedSideEffects() {
        RemoveConstantExpressions pass = new RemoveConstantExpressions(null);

        Node root = NodeFactory.createProgram();
        NodeTraversal.traverse(null, root, pass);

        Node expr1 = NodeFactory.createIdentifier("foo");
        Node expr2 = NodeFactory.createBinaryOp(
                Token.OP_PLUS,
                NodeFactory.createIdentifier("x"),
                expr1
        );
        Node expr3 = NodeFactory.createBinaryOp(
                Token.OP_PLUS,
                NodeFactory.createIdentifier("y"),
                expr2
        );
        Node expr4 = NodeFactory.createBinaryOp(
                Token.OP_PLUS,
                expr3,
                NodeFactory.createIdentifier("bar")
        );

        Node expected = NodeFactory.createSequence(
                NodeFactory.createSequence(
                    NodeFactory.createIdentifier("foo"),
                    NodeFactory.createIdentifier("x")
                ),
                NodeFactory.createSequence(
                    NodeFactory.createIdentifier("y"),
                    NodeFactory.createIdentifier("bar")
                )
        );

        Node actual = findNode(root, expr4);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void testRemoveConstantExpressionsWithNoSideEffects() {
        RemoveConstantExpressions pass = new RemoveConstantExpressions(null);

        Node root = NodeFactory.createProgram();
        NodeTraversal.traverse(null, root, pass);

        Node expr1 = NodeFactory.createBinaryOp(
                Token.OP_PLUS,
                NodeFactory.createNumberLiteral(1),
                NodeFactory.createNumberLiteral(2)
        );
        Node expr2 = NodeFactory.createBinaryOp(
                Token.OP_PLUS,
                expr1,
                NodeFactory.createNumberLiteral(3)
        );

        Node expected = NodeFactory.createProgram();
        Node actual = findNode(root, expr2);
        assertNull(actual);
    }

    private Node findNode(Node node, Node target) {
        if (node.equals(target)) {
            return node;
        }

        if (node.getFirstChild() != null) {
            return findNode(node.getFirstChild(), target);
        }

        return null;
    }
}