package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {
    @Test
    public void testProcessLabels() {
        // Given a simple test case without label nesting
        String sourceCode = "function foo() {" +
                "label1: {" +
                "label2: return; " +
                "}" +
                "label3: return; " +
                "}";

        Node externs = NodeFactory.createExterns();
        Node root = NodeFactory.createProgram(NodeFactory.createScript(sourceCode));

        RenameLabels renameLabels = new RenameLabels(new Compiler());
        renameLabels.process(externs, root);

        // When processing the code, all labels should be renamed
        ProcessLabels processLabels = (ProcessLabels) renameLabels.getProcessCallback();
        Map<String, LabelInfo> labelMap = processLabels.namespaceStack.peek().renameMap;

        // Then the labels should be renamed according to their depth in the nesting
        assertEquals("label1", labelMap.get("label1").getName());
        assertEquals("label2", labelMap.get("label2").getName());
        assertNull(labelMap.get("label3")); // label3 was removed because it was unreferenced

        // And the short names should be unique and follow the expected naming scheme
        assertEquals("a", labelMap.get("label1").getName());
        assertEquals("b", labelMap.get("label2").getName());
    }

    @Test
    public void testProcessLabelsWithNestedLabels() {
        // Given a test case with nested labels
        String sourceCode = "function foo() {" +
                "label1: {" +
                "label2: {" +
                "label3: return; " +
                "}" +
                "label4: return; " +
                "}" +
                "label5: return; " +
                "}";

        Node externs = NodeFactory.createExterns();
        Node root = NodeFactory.createProgram(NodeFactory.createScript(sourceCode));

        RenameLabels renameLabels = new RenameLabels(new Compiler());
        renameLabels.process(externs, root);

        // When processing the code, all labels should be renamed
        ProcessLabels processLabels = (ProcessLabels) renameLabels.getProcessCallback();
        Map<String, LabelInfo> labelMap = processLabels.namespaceStack.peek().renameMap;

        // Then the labels should be renamed according to their depth in the nesting
        assertEquals("label1", labelMap.get("label1").getName());
        assertEquals("label2", labelMap.get("label2").getName());
        assertEquals("label3", labelMap.get("label3").getName());
        assertEquals("label4", labelMap.get("label4").getName());
        assertNull(labelMap.get("label5")); // label5 was removed because it was unreferenced

        // And the short names should be unique and follow the expected naming scheme
        assertEquals("a", labelMap.get("label1").getName());
        assertEquals("b", labelMap.get("label2").getName());
        assertEquals("c", labelMap.get("label3").getName());
        assertEquals("d", labelMap.get("label4").getName());
    }

    @Test
    public void testProcessLabelsWithReferencedUnusedLabel() {
        // Given a test case with a referenced but unused label
        String sourceCode = "function foo() {" +
                "label1: {" +
                "label2: return; " +
                "}" +
                "label3: {" +
                "label1: return; " + // unreferenced usage of label1
                "}" +
                "label4: return; " +
                "}";

        Node externs = NodeFactory.createExterns();
        Node root = NodeFactory.createProgram(NodeFactory.createScript(sourceCode));

        RenameLabels renameLabels = new RenameLabels(new Compiler());
        renameLabels.process(externs, root);

        // When processing the code, the unreferenced label should be removed
        ProcessLabels processLabels = (ProcessLabels) renameLabels.getProcessCallback();
        Map<String, LabelInfo> labelMap = processLabels.namespaceStack.peek().renameMap;

        // Then the labels should be renamed according to their depth in the nesting
        assertEquals("label1", labelMap.get("label1").getName());
        assertEquals("label2", labelMap.get("label2").getName());
        assertEquals("label4", labelMap.get("label4").getName());
        assertNull(labelMap.get("label3")); // label3 was removed because it was unreferenced
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testProcessLabelsWithRemoveUnusedFalse() {
        // Given a test case where removeUnused is set to false
        String sourceCode = "function foo() {" +
                "label1: {" +
                "label2: return; " +
                "}" +
                "label3: return; " +
                "}";

        Node externs = NodeFactory.createExterns();
        Node root = NodeFactory.createProgram(NodeFactory.createScript(sourceCode));

        RenameLabels renameLabels = new RenameLabels(new Compiler(), new DefaultNameSupplier(), false);
        renameLabels.process(externs, root);

        // When processing the code, an exception should be thrown because label3
        // is unreferenced and should not be removed
        ProcessLabels processLabels = (ProcessLabels) renameLabels.getProcessCallback();
        Map<String, LabelInfo> labelMap = processLabels.namespaceStack.peek().renameMap;

        // Then the labels should be renamed according to their depth in the nesting
        assertEquals("label1", labelMap.get("label1").getName());
        assertEquals("label2", labelMap.get("label2").getName());
        assertNotNull(labelMap.get("label3")); // label3 should not be null because it was not removed
    }
}

class NodeFactory {
    public static Node createExterns() {
        return Node.ObjectLiteral(NodeFactory.createObject());
    }

    public static Node createObject() {
        return Node.ObjectLiteral();
    }

    public static Node createScript(String script) {
        return Node.Script(script, null, null);
    }

    public static Node createProgram(Node script) {
        return Node.Program(Node.Script(script, null, null), null);
    }
}