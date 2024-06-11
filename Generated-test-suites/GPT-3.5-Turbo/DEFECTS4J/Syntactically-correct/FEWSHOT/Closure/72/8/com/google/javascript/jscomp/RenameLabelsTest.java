package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {
    private AbstractCompiler compiler;
    private RenameLabels renameLabels;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler();
        renameLabels = new RenameLabels(compiler);
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EMPTY);
        Node root = new Node(Token.EMPTY);
        renameLabels.process(externs, root);

        // TODO: Add more specific assertions for the expected outcome of the renameLabels.process() method
    }

    @Test
    public void testDefaultNameSupplier() {
        RenameLabels.DefaultNameSupplier defaultNameSupplier = new RenameLabels.DefaultNameSupplier();
        assertEquals("a", defaultNameSupplier.get());
        assertEquals("b", defaultNameSupplier.get());
        assertEquals("c", defaultNameSupplier.get());
        assertEquals("d", defaultNameSupplier.get());
        assertEquals("e", defaultNameSupplier.get());
    }

    @Test
    public void testEnterScope() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        assertEquals(1, processLabels.namespaceStack.size());
        assertEquals(0, processLabels.namespaceStack.peek().renameMap.size());

        processLabels.enterScope(null);

        assertEquals(2, processLabels.namespaceStack.size());
        assertEquals(0, processLabels.namespaceStack.peek().renameMap.size());
    }

    @Test
    public void testExitScope() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.enterScope(null);
        processLabels.enterScope(null);

        assertEquals(3, processLabels.namespaceStack.size());

        processLabels.exitScope(null);

        assertEquals(2, processLabels.namespaceStack.size());
    }

    @Test
    public void testShouldTraverse() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.namespaceStack.push(processLabels.new LabelNamespace());

        assertFalse(processLabels.shouldTraverse(null, new Node(Token.FUNCTION), null));

        assertTrue(processLabels.shouldTraverse(null, new Node(Token.LABEL), null));

        assertEquals(1, processLabels.namespaceStack.peek().renameMap.size());
        assertTrue(processLabels.namespaceStack.peek().renameMap.containsKey("label1"));
    }

    @Test
    public void testVisit() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.namespaceStack.push(processLabels.new LabelNamespace());

        Node labelNode = new Node(Token.LABEL);
        labelNode.addChildToFront(new Node(Token.STRING, "label1"));

        processLabels.visit(null, labelNode, null);

        assertEquals(0, processLabels.namespaceStack.peek().renameMap.size());
    }

    @Test
    public void testVisitLabel_referenced() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.namespaceStack.push(processLabels.new LabelNamespace());
        processLabels.names.add("shortName");

        Node labelNode = new Node(Token.LABEL);
        labelNode.addChildToFront(new Node(Token.STRING, "label1"));
        labelNode.addChildToFront(new Node(Token.BLOCK));

        processLabels.namespaceStack.peek().renameMap.put("label1", processLabels.new LabelInfo(1));

        processLabels.visitLabel(labelNode, null);

        assertEquals("shortName", labelNode.getFirstChild().getString());
    }

    @Test
    public void testVisitLabel_notReferenced() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.namespaceStack.push(processLabels.new LabelNamespace());
        processLabels.names.add("shortName");

        Node labelNode = new Node(Token.LABEL);
        labelNode.addChildToFront(new Node(Token.STRING, "label1"));
        labelNode.addChildToFront(new Node(Token.BLOCK));

        processLabels.visitLabel(labelNode, null);

        assertNull(labelNode.getParent());
    }

    @Test
    public void testVisitBreakOrContinue_referenced() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.namespaceStack.push(processLabels.new LabelNamespace());
        processLabels.names.add("shortName");

        Node breakNode = new Node(Token.BREAK);
        breakNode.addChildToFront(new Node(Token.STRING, "label1"));

        processLabels.namespaceStack.peek().renameMap.put("label1", processLabels.new LabelInfo(1));

        processLabels.visitBreakOrContinue(breakNode);

        assertEquals("shortName", breakNode.getFirstChild().getString());
    }

    @Test
    public void testVisitBreakOrContinue_notReferenced() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.namespaceStack.push(processLabels.new LabelNamespace());
        processLabels.names.add("shortName");

        Node breakNode = new Node(Token.BREAK);
        breakNode.addChildToFront(new Node(Token.STRING, "label1"));

        processLabels.visitBreakOrContinue(breakNode);

        assertEquals("label1", breakNode.getFirstChild().getString());
    }

    @Test
    public void testGetNameForId() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.names.add("a");
        processLabels.names.add("b");
        processLabels.names.add("c");

        assertEquals("a", processLabels.getNameForId(1));
        assertEquals("b", processLabels.getNameForId(2));
        assertEquals("c", processLabels.getNameForId(3));
    }

    @Test
    public void testGetLabelInfo() {
        RenameLabels.ProcessLabels processLabels = renameLabels.new ProcessLabels();
        processLabels.namespaceStack.push(processLabels.new LabelNamespace());
        processLabels.namespaceStack.peek().renameMap.put("label1", processLabels.new LabelInfo(1));

        assertEquals(1, processLabels.getLabelInfo("label1").id);
    }
}