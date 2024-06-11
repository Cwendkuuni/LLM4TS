package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {

    private RenameLabels renameLabels;

    @Before
    public void setUp() {
        renameLabels = new RenameLabels(new AbstractCompiler());
    }

    @After
    public void tearDown() {
        renameLabels = null;
    }

    @Test
    public void testProcess_withLabels_shouldRenameLabels() {
        String input = "function bar() { a: { b: { foo(); } } a: { b: break a; } }";
        Node root = CompilerTestUtils.parseScript(input);

        renameLabels.process(null, root);

        NodeTraversal.traverse(new AbstractCompiler(), root, new NodeTraversal.ScopedCallback() {
            @Override
            public void enterScope(NodeTraversal t) {
            }

            @Override
            public void exitScope(NodeTraversal t) {
            }

            @Override
            public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
                if (n.getType() == Token.LABEL) {
                    String name = n.getFirstChild().getString();
                    assertFalse(name.equals("a"));
                    assertFalse(name.equals("b"));
                }
                return true;
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
                if (n.getType() == Token.BREAK || n.getType() == Token.CONTINUE) {
                    String name = n.getFirstChild().getString();
                    assertFalse(name.equals("a"));
                    assertFalse(name.equals("b"));
                }
            }
        });
    }

    @Test
    public void testProcess_withLabels_unusedLabels_shouldRemoveLabels() {
        String input = "function bar() { a: { b: { foo(); } } a: { } }";
        Node root = CompilerTestUtils.parseScript(input);

        renameLabels.process(null, root);

        NodeTraversal.traverse(new AbstractCompiler(), root, new NodeTraversal.ScopedCallback() {
            @Override
            public void enterScope(NodeTraversal t) {
            }

            @Override
            public void exitScope(NodeTraversal t) {
            }

            @Override
            public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
                if (n.getType() == Token.LABEL) {
                    String name = n.getFirstChild().getString();
                    assertFalse(name.equals("a"));
                    assertFalse(name.equals("b"));
                }
                return true;
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
                if (n.getType() == Token.BREAK || n.getType() == Token.CONTINUE) {
                    String name = n.getFirstChild().getString();
                    assertFalse(name.equals("a"));
                    assertFalse(name.equals("b"));
                }
            }
        });
    }

    @Test
    public void testProcess_withBreakOrContinue_shouldRenameReferences() {
        String input = "function bar() { a: { b: { foo(); break b; } } }";
        Node root = CompilerTestUtils.parseScript(input);

        renameLabels.process(null, root);

        NodeTraversal.traverse(new AbstractCompiler(), root, new NodeTraversal.ScopedCallback() {
            @Override
            public void enterScope(NodeTraversal t) {
            }

            @Override
            public void exitScope(NodeTraversal t) {
            }

            @Override
            public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
                return true;
            }

            @Override
            public void visit(NodeTraversal t, Node n, Node parent) {
                if (n.getType() == Token.BREAK || n.getType() == Token.CONTINUE) {
                    String name = n.getFirstChild().getString();
                    assertFalse(name.equals("a"));
                    assertFalse(name.equals("b"));
                }
            }
        });
    }
}