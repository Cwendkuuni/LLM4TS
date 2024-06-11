package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class RenameLabelsTest {

    @Test
    public void testRenameLabels() {
        // Given a RenameLabels instance
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        // When processing a simple Node tree
        Node externs = Node.newBlock("function foo() {}");
        Node root = Node.newBlock("foo();");
        renameLabels.process(externs, root);

        // Then the label names should have been renamed
        List<String> names = new ArrayList<>();
        ProcessLabels processLabels = new ProcessLabels();
        NodeTraversal.traverse(compiler, root, processLabels);
        for (Map.Entry<String, LabelInfo> entry : processLabels.namespaceStack.peek().renameMap.entrySet()) {
            names.add(entry.getKey());
        }
        assertSame("a", names.get(0));
        assertSame("a", processLabels.getNameForId(1));
    }

    @Test
    public void testRenameLabelsWithReferences() {
        // Given a RenameLabels instance
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        // Given a Node tree with labeled breaks and continues
        Node externs = Node.newBlock("function foo() {\n" +
                "  a: {\n" +
                "    break a;\n" +
                "  }\n" +
                "  b: {\n" +
                "    continue b;\n" +
                "  }\n" +
                "}");
        Node root = externs;
        renameLabels.process(externs, root);

        // When processing the Node tree
        ProcessLabels processLabels = new ProcessLabels();
        NodeTraversal.traverse(compiler, root, processLabels);

        // Then the label names should have been renamed and label references updated
        List<String> names = new ArrayList<>();
        for (Map.Entry<String, LabelInfo> entry : processLabels.namespaceStack.peek().renameMap.entrySet()) {
            names.add(entry.getKey());
        }
        assertSame("a", names.get(0));
        assertSame("a", processLabels.getNameForId(1));
        assertSame("b", names.get(1));
        assertSame("b", processLabels.getNameForId(2));

        // And the references to the labels should have been updated
        Node node = root.getFirstChild();
        assertSame("a", node.getLastChild().getString());
        node = node.getNextSibling();
        assertSame("b", node.getLastChild().getString());
    }

    @Test
    public void testRenameLabelsWithUnusedLabel() {
        // Given a RenameLabels instance
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler);

        // Given a Node tree with an unused label
        Node externs = Node.newBlock("function foo() {\n" +
                "  a: {}\n" +
                "}");
        Node root = externs;
        renameLabels.process(externs, root);

        // When processing the Node tree
        ProcessLabels processLabels = new ProcessLabels();
        NodeTraversal.traverse(compiler, root, processLabels);

        // Then the unused label should have been removed
        assertNull(processLabels.namespaceStack.peek().renameMap.get("a"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRenameLabelsWithInvalidSupplier() {
        // Given a RenameLabels instance with an invalid Supplier
        AbstractCompiler compiler = new AbstractCompiler();
        RenameLabels renameLabels = new RenameLabels(compiler, () -> null, true);
    }
}