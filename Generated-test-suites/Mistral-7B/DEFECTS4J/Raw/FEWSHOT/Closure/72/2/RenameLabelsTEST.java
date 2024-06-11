package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.Supplier;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {
    @Test
    public void testRenameLabels() {
        String input = "function foo() {\n" +
                "  a: {\n" +
                "    b: {\n" +
                "      break a;\n" +
                "    }\n" +
                "  }\n" +
                "}";

        Node externs = Node.externalRoot();
        Node root = Node.parse(input);

        RenameLabels renameLabels = new RenameLabels(new Compiler());
        renameLabels.process(externs, root);

        NodeTraversal nodeTraversal = NodeTraversal.create(root, new AbstractCompiler());
        ProcessLabels processLabels = new ProcessLabels();
        NodeTraversal.traverse(renameLabels.compiler, root, processLabels);

        // Check that all labels have been renamed
        Map<String, String> expectedLabelRenamings = Maps.newHashMap();
        expectedLabelRenamings.put("a", "a0");
        expectedLabelRenamings.put("b", "b0");

        Map<String, String> actualLabelRenamings = Maps.newHashMap();
        TraverseLabels(nodeTraversal, actualLabelRenamings);

        // Check that the labels have been renamed correctly
        assertThat(actualLabelRenamings).isEqualTo(expectedLabelRenamings);

        // Check that all uses of the labels have been updated
        TraverseUsesOfLabels(nodeTraversal, expectedLabelRenamings);
    }

    private void TraverseLabels(NodeTraversal nodeTraversal, Map<String, String> labelRenamings) {
        nodeTraversal.inOrderTraversal(new NodeVisitor() {
            @Override
            public void visit(NodeTraversal nodeTraversal, Node node) {
                if (node.getType() == Token.LABEL) {
                    String labelName = node.getFirstChild().getString();
                    String newLabelName = labelRenamings.get(labelName);
                    if (newLabelName != null) {
                        node.getFirstChild().setString(newLabelName);
                    }
                }
                super.visit(nodeTraversal, node);
            }
        });
    }

    private void TraverseUsesOfLabels(NodeTraversal nodeTraversal, Map<String, String> labelRenamings) {
        nodeTraversal.inOrderTraversal(new NodeVisitor() {
            @Override
            public void visit(NodeTraversal nodeTraversal, Node node) {
                if (node.getType() == Token.BREAK) {
                    Node labelNode = node.getFirstChild();
                    String labelName = labelNode.getString();
                    String newLabelName = labelRenamings.get(labelName);
                    if (newLabelName != null) {
                        labelNode.setString(newLabelName);
                        node.replaceChild(labelNode, node.getLastChild());
                    }
                }
                super.visit(nodeTraversal, node);
            }
        });
    }

    @Test
    public void testRenameLabelsWithUnusedLabels() {
        String input = "function foo() {\n" +
                "  a: {\n" +
                "    b: {\n" +
                "      break a;\n" +
                "    }\n" +
                "  }\n" +
                "  c: {}; // unused label\n" +
                "}";

        Node externs = Node.externalRoot();
        Node root = Node.parse(input);

        RenameLabels renameLabels = new RenameLabels(new Compiler(), new DefaultNameSupplier(), false);
        renameLabels.process(externs, root);

        NodeTraversal nodeTraversal = NodeTraversal.create(root, new AbstractCompiler());
        ProcessLabels processLabels = new ProcessLabels();
        NodeTraversal.traverse(renameLabels.compiler, root, processLabels);

        // Check that all labels have been renamed
        Map<String, String> expectedLabelRenamings = Maps.newHashMap();
        expectedLabelRenamings.put("a", "a0");
        expectedLabelRenamings.put("b", "b0");

        Map<String, String> actualLabelRenamings = Maps.newHashMap();
        TraverseLabels(nodeTraversal, actualLabelRenamings);

        // Check that the labels have been renamed correctly
        assertThat(actualLabelRenamings).isEqualTo(expectedLabelRenamings);

        // Check that the unused label has not been renamed
        Node unusedLabel = NodeUtil.findFirstChildByType(root, Token.LABEL, "c");
        assertNull(unusedLabel);
    }

    @Test
    public void testRenameLabelsWithNestedLabels() {
        String input = "function foo() {\n" +
                "  a: {\n" +
                "    b: {\n" +
                "      c: {\n" +
                "        break c;\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        Node externs = Node.externalRoot();
        Node root = Node.parse(input);

        RenameLabels renameLabels = new RenameLabels(new Compiler());
        renameLabels.process(externs, root);

        NodeTraversal nodeTraversal = NodeTraversal.create(root, new AbstractCompiler());
        ProcessLabels processLabels = new ProcessLabels();
        NodeTraversal.traverse(renameLabels.compiler, root, processLabels);

        // Check that all labels have been renamed
        Map<String, String> expectedLabelRenamings = Maps.newHashMap();
        expectedLabelRenamings.put("a", "a0");
        expectedLabelRenamings.put("b", "b0");
        expectedLabelRenamings.put("c", "c0");

        Map<String, String> actualLabelRenamings = Maps.newHashMap();
        TraverseLabels(nodeTraversal, actualLabelRenamings);

        // Check that the labels have been renamed correctly
        assertThat(actualLabelRenamings).isEqualTo(expectedLabelRenamings);

        // Check that all uses of the labels have been updated
        TraverseUsesOfLabels(nodeTraversal, expectedLabelRenamings);
    }
}

class ProcessLabels extends RenameLabels.ProcessLabels {
    @Override
    public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        // Allow traversal of all nodes, as we want to test the label renaming logic
        return true;
    }
}