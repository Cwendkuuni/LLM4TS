package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.Token;

import java.util.List;

public class RemoveConstantExpressionsTest {
    private Compiler compiler;

    @org.junit.Before
    public void setup() {
        compiler = new Compiler();
    }

    @Test
    public void testRemoveConstantExpressions() {
        RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
        Node node = new Node(Token.PROGRAM, null);
        node.setLineInfo(0, 0);
        node.addChild(new Node(Token.EXPR_STATEMENT, null));

        Node statement = node.getFirstChild();
        statement.setLineInfo(0, 1);
        statement.addChild(new Node(Token.PLUS, null));
        statement.addChild(
            new Node(Token.NUMBER, 1, "1")).setLineInfo(0, 1);
        statement.addChild(new Node(Token.IDENT, null, "foo"));
        statement.addChild(new Node(Token.PLUS, null));
        statement.addChild(new Node(Token.IDENT, null, "bar"));

        pass.process(null, node);

        AbstractPostOrderCallback callback = new AbstractPostOrderCallback() {
            @Override
            public void visit(NodeTraversal traversal, Node node, Node parent) {
                if (node.getFirstChild() != null && node.getFirstChild().getType() == Token.IDENT) {
                    assertEquals("foo", node.getFirstChild().toString());
                }
                if (node.getLastChild() != null && node.getLastChild().getType() == Token.IDENT) {
                    assertEquals("bar", node.getLastChild().toString());
                }
            }
        };

        NodeTraversal.traverse(null, node, callback);
    }

    @Test
    public void testRemoveConstantExpressionsWithSideEffects() {
        RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
        Node node = new Node(Token.PROGRAM, null);
        node.setLineInfo(0, 0);
        node.addChild(new Node(Token.EXPR_STATEMENT, null));

        Node statement = node.getFirstChild();
        statement.setLineInfo(0, 1);
        statement.addChild(new Node(Token.PLUS, null));
        statement.addChild(new Node(Token.NUMBER, 1, "1")).setLineInfo(0, 1);
        statement.addChild(new Node(Token.IDENT, null, "foo"));
        statement.addChild(new Node(Token.PLUS, null));
        statement.addChild(new Node(Token.IDENT, null, "bar"));
        statement.addChild(new Node(Token.ASSIGN, null, null, new Node(Token.IDENT, null, "x")).setLineInfo(0, 1));

        pass.process(null, node);

        AbstractPostOrderCallback callback = new AbstractPostOrderCallback() {
            @Override
            public void visit(NodeTraversal traversal, Node node, Node parent) {
                if (node.getFirstChild() != null && node.getFirstChild().getType() == Token.IDENT) {
                    assertEquals("foo", node.getFirstChild().toString());
                }
                if (node.getLastChild() != null && node.getLastChild().getType() == Token.IDENT) {
                    assertEquals("bar", node.getLastChild().toString());
                }
                if (node.getLastChildOfType(Token.ASSIGN) != null) {
                    assertNotNull(node.getLastChildOfType(Token.ASSIGN).getFirstChild());
                }
            }
        };

        NodeTraversal.traverse(null, node, callback);
    }

    @Test
    public void testRemoveConstantExpressionsWithEmptyStatement() {
        RemoveConstantExpressions pass = new RemoveConstantExpressions(compiler);
        Node node = new Node(Token.PROGRAM, null);
        node.setLineInfo(0, 0);
        node.addChild(new Node(Token.EXPR_STATEMENT, null));
        node.getFirstChild().setLineInfo(0, 1);
        node.getFirstChild().addChild(new Node(Token.VOID, null));

        pass.process(null, node);

        AbstractPostOrderCallback callback = new AbstractPostOrderCallback() {
            @Override
            public void visit(NodeTraversal traversal, Node node, Node parent) {
                assertNull(node.getFirstChild());
            }
        };

        NodeTraversal.traverse(null, node, callback);
    }
}