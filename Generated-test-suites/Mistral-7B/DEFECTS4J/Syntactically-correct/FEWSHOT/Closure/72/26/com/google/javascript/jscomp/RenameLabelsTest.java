package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeTraversal;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {
    @Test
    public void testRenameLabels() {
        // Prepare test input
        AbstractCompiler compiler = new Compiler();
        RenameLabels renameLabels = new RenameLabels(compiler);
        Node root = NodeUtil.createBlock("function test() { a: { break a; } }", null);

        // Perform label renaming
        renameLabels.process(null, root);

        // Check that label "a" has been renamed and referenced
        Node labelNode = NodeUtil.findSingleLabelNode(root, "a");
        assertNotNull(labelNode);
        Map<String, String> labelRenamings = Maps.newHashMap();
        labelRenamings.put("a", "labelA");
        NodeUtil.renameNodes(root, labelRenamings);
        String expectedRenamedLabelName = labelRenamings.get("a");
        String actualRenamedLabelName = labelNode.getFirstChild().getString();
        assertEquals("Expected label 'a' to be renamed to 'labelA'. Actual: " + actualRenamedLabelName, expectedRenamedLabelName, actualRenamedLabelName);

        // Check that the 'break a' statement has been updated with the renamed label
        Node breakStatement = NodeUtil.findSingleBreakOrContinueStatementNode(root, "a");
        assertNotNull(breakStatement);
        String expectedRenamedBreakStatementLabelName = expectedRenamedLabelName;
        String actualRenamedBreakStatementLabelName = breakStatement.getFirstChild().getString();
        assertEquals("Expected 'break a' statement to reference label 'a'. Actual: " + actualRenamedBreakStatementLabelName, expectedRenamedBreakStatementLabelName, actualRenamedBreakStatementLabelName);
    }

    @Test
    public void testRemoveUnusedLabels() {
        // Prepare test input
        AbstractCompiler compiler = new Compiler();
        RenameLabels renameLabels = new RenameLabels(compiler, new RemoveUnusedLabelsNameSupplier(), true);
        Node root = NodeUtil.createBlock("function test() { a: {}; b: {}; c: { break a; } }", null);

        // Perform label renaming and removal of unused labels
        renameLabels.process(null, root);

        // Check that label "a" has been renamed and referenced
        Node labelNodeA = NodeUtil.findSingleLabelNode(root, "a");
        assertNotNull(labelNodeA);
        Map<String, String> labelRenamings = Maps.newHashMap();
        labelRenamings.put("a", "labelA");
        NodeUtil.renameNodes(root, labelRenamings);
        String expectedRenamedLabelNameA = labelRenamings.get("a");
        String actualRenamedLabelNameA = labelNodeA.getFirstChild().getString();
        assertEquals("Expected label 'a' to be renamed to 'labelA'. Actual: " + actualRenamedLabelNameA, expectedRenamedLabelNameA, actualRenamedLabelNameA);

        // Check that label "b" has been removed since it is not referenced
        Node labelNodeB = NodeUtil.findSingleLabelNode(root, "b");
        assertNull(labelNodeB);

        // Check that label "c" has been renamed and referenced
        Node labelNodeC = NodeUtil.findSingleLabelNode(root, "c");
        assertNotNull(labelNodeC);
        Map<String, String> labelRenamingsC = Maps.newHashMap();
        labelRenamingsC.put("c", "labelC");
        NodeUtil.renameNodes(root, labelRenamingsC);
        String expectedRenamedLabelNameC = labelRenamingsC.get("c");
        String actualRenamedLabelNameC = labelNodeC.getFirstChild().getString();
        assertEquals("Expected label 'c' to be renamed to 'labelC'. Actual: " + actualRenamedLabelNameC, expectedRenamedLabelNameC, actualRenamedLabelNameC);
    }
}

class RemoveUnusedLabelsNameSupplier implements Supplier<String> {
    private int counter = 0;

    @Override
    public String get() {
        String labelName = "label" + (counter++);
        return labelName;
    }
}