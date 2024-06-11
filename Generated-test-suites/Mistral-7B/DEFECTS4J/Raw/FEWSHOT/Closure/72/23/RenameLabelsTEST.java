package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

    private static class TestCompilerPass extends TestCompiler {
        @Override
        public void process(Node externs, Node root) {
            new RenameLabels(this).process(externs, root);
        }
    }

    @Test
    public void testRenameLabels() {
        String testCode = "function foo() {" +
                "  a: { break a; }" +
                "  b: { break a; }" +
                "  a: { continue b; }" +
                "  c: { continue b; }" +
                "  d: { break a; }" +
                "}";

        TestCompiler compiler = new TestCompilerPass();
        Node root = compiler.parse(testCode, null).getChildNodes();

        new RenameLabels(compiler).process(compiler.getExterns(), root);

        Node labelA = findLabelNode("a", root);
        Node labelB = findLabelNode("b", root);

        assertSame(labelA.getFirstChild(), labelB.getFirstChild());
        assertFalse(labelA.hasChildNodes());
        assertFalse(labelB.hasChildNodes());
    }

    @Test
    public void testRenameLabelsWithUnusedLabel() {
        String testCode = "function foo() {" +
                "  a: {}" +
                "  b: { break a; }" +
                "}";

        TestCompiler compiler = new TestCompilerPass();
        Node root = compiler.parse(testCode, null).getChildNodes();

        new RenameLabels(compiler, new AlwaysNewNameSupplier(), false).process(compiler.getExterns(), root);

        Node labelA = findLabelNode("a", root);
        assertSame(labelA.getParent(), root);
        assertNull(labelA.getFirstChild());
    }

    @Test
    public void testRenameLabelsWithNestedLabels() {
        String testCode = "function foo() {" +
                "  a: { b: { c: { break c; } } }" +
                "}";

        TestCompiler compiler = new TestCompilerPass();
        Node root = compiler.parse(testCode, null).getChildNodes();

        new RenameLabels(compiler).process(compiler.getExterns(), root);

        Node labelC = findLabelNode("c", root);
        Node labelB = findLabelNode("b", root);
        Node labelA = findLabelNode("a", root);

        assertSame(labelC.getParent(), labelB);
        assertSame(labelB.getParent(), labelA);

        assertSame(labelC.getFirstChild(), labelB.getFirstChild());
        assertFalse(labelC.hasChildNodes());
        assertFalse(labelB.hasChildNodes());
        assertFalse(labelA.hasChildNodes());
    }

    @Test
    public void testRenameLabelsWithUnicodeLabel() {
        String testCode = "function foo() {" +
                "  \u0041:\u0042 { break \u0041:\u0042; }" +
                "}";

        TestCompiler compiler = new TestCompilerPass();
        Node root = compiler.parse(testCode, null).getChildNodes();

        new RenameLabels(compiler).process(compiler.getExterns(), root);

        Node label = findLabelNode("\u0041:\u0042", root);
        assertSame(label.getParent(), root);
        assertNull(label.getFirstChild());
    }

    private Node findLabelNode(String name, Node root) {
        NodeTraversal nodeTraversal = new NodeTraversal(root);
        Node labelNode = null;
        nodeTraversal.traverse(new ProcessLabels() {
            @Override
            public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
                if (node.getType() == Token.LABEL && node.getFirstChild().getString().equals(name)) {
                    labelNode = node;
                    return false;
                }
                return true;
            }
        });
        return labelNode;
    }

    private static class ProcessLabels extends NodeTraversal.ScopedCallback {
        @Override
        public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
            return true;
        }

        @Override
        public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
            if (node != null && node.getType() == Token.LABEL) {
                String name = node.getFirstChild().getString();
                System.out.printf("Label %s renamed to %s%n", name, getNameForId(node.getFirstChild().getInt32()));
            }
        }

        private int getNameForId(Node node) {
            int id = 0;
            for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                if (child.getType() == Token.IDENT) {
                    id = child.getInt32();
                }
            }
            return id;
        }
    }

    private static class AlwaysNewNameSupplier implements RenameLabels.Supplier<String> {
        @Override
        public String get() {
            return "new_label_name";
        }
    }
}