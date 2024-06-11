package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RenameLabelsTest {
    private static final String[] EMPTY_STRING_ARRAY = {};

    private RenameLabels renameLabels;

    @Before
    public void setUp() {
        renameLabels = new RenameLabels(null);
    }

    @Test
    public void testProcess() {
        // setup
        Node node = new Node(Token.LABEL);
        Node parent = new Node();
        parent.addChildToFront(node);
        node.addChildToFront(new Node("labelName"));
        renameLabels.namespaceStack.push(renameLabels.new LabelNamespace());

        // invoke
        renameLabels.visit(null, node, parent);

        // assert
        assertNull(node.getFirstChild());
        assertTrue(renameLabels.namespaceStack.peek().renameMap.isEmpty());
    }

    @Test
    public void testGetLabelInfo_withExistingLabelInfo_shouldReturnLabelInfo() {
        // setup
        renameLabels.namespaceStack.push(renameLabels.new LabelNamespace());
        LabelInfo li = renameLabels.new LabelInfo(1);
        renameLabels.namespaceStack.peek().renameMap.put("labelName", li);

        // invoke and assert
        assertEquals(li, renameLabels.getLabelInfo("labelName"));
    }

    @Test
    public void testGetLabelInfo_withNonExistingLabelInfo_shouldReturnNull() {
        // setup
        renameLabels.namespaceStack.push(renameLabels.new LabelNamespace());

        // invoke and assert
        assertNull(renameLabels.getLabelInfo("labelName"));
    }

    @Test
    public void testGetNameForId_withValidId_shouldReturnShortName() {
        // setup
        renameLabels.names.add("a");

        // invoke and assert
        assertEquals("a", renameLabels.getNameForId(1));
    }

    @Test
    public void testShouldTraverse_withLabelNode_shouldRenameLabelAndStoreContext() {
        // setup
        NodeTraversal nodeTraversal = new NodeTraversal(null);
        Node node = new Node(Token.LABEL);
        node.addChildToFront(new Node("labelName"));
        Node parent = new Node();
        parent.addChildToFront(node);
        renameLabels.namespaceStack.push(renameLabels.new LabelNamespace());

        // invoke
        boolean result = renameLabels.shouldTraverse(nodeTraversal, node, parent);

        // assert
        assertTrue(result);
        assertTrue(renameLabels.namespaceStack.peek().renameMap.containsKey("labelName"));
    }

    @Test
    public void testShouldTraverse_withoutLabelNode_shouldReturnTrue() {
        // setup
        NodeTraversal nodeTraversal = new NodeTraversal(null);
        Node node = new Node(Token.EMPTY);
        Node parent = new Node();

        // invoke and assert
        assertTrue(renameLabels.shouldTraverse(nodeTraversal, node, parent));
    }

    @Test
    public void testEnterScope() {
        // setup
        LabelNamespace labelNamespace = renameLabels.new LabelNamespace();
        renameLabels.namespaceStack.push(labelNamespace);

        // invoke
        renameLabels.enterScope(null);

        // assert
        assertEquals(labelNamespace, renameLabels.namespaceStack.peek());
    }

    @Test
    public void testExitScope() {
        // setup
        LabelNamespace labelNamespace = renameLabels.new LabelNamespace();
        renameLabels.namespaceStack.push(labelNamespace);

        // invoke
        renameLabels.exitScope(null);

        // assert
        assertTrue(renameLabels.namespaceStack.isEmpty());
    }

    @Test
    public void testVisit_withLabelNodeAndReferencedLabel_shouldRenameLabel() {
        // setup
        Node node = new Node(Token.LABEL);
        Node parent = new Node();
        parent.addChildToFront(node);
        node.addChildToFront(new Node("labelName"));
        LabelNamespace labelNamespace = renameLabels.new LabelNamespace();
        renameLabels.namespaceStack.push(labelNamespace);
        labelNamespace.renameMap.put("labelName", renameLabels.new LabelInfo(1));

        // invoke
        renameLabels.visit(null, node, parent);

        // assert
        assertEquals("a", node.getFirstChild().getString());
    }

    @Test
    public void testVisit_withLabelNodeAndUnreferencedLabel_shouldRemoveLabel() {
        // setup
        Node node = new Node(Token.LABEL);
        Node child = new Node();
        Node parent = new Node();
        parent.addChildToFront(node);
        node.addChildToFront(new Node("labelName"));
        node.addChildToFront(child);
        LabelNamespace labelNamespace = renameLabels.new LabelNamespace();
        renameLabels.namespaceStack.push(labelNamespace);

        // invoke
        renameLabels.visit(null, node, parent);

        // assert
        assertEquals(child, parent.getFirstChild());
        assertTrue(renameLabels.namespaceStack.peek().renameMap.isEmpty());
    }

    @Test
    public void testVisit_withoutLabelNode_shouldDoNothing() {
        // setup
        Node node = new Node(Token.EMPTY);
        Node parent = new Node();

        // invoke
        renameLabels.visit(null, node, parent);

        // assert
        assertTrue(renameLabels.namespaceStack.isEmpty());
    }

    @Test
    public void testVisitBreakOrContinue_withNamedBreakOrContinueAndRenamedLabel_shouldRenameLabel() {
        // setup
        Node node = new Node(Token.BREAK);
        Node parent = new Node();
        parent.addChildToFront(node);
        node.addChildToFront(new Node("labelName"));
        LabelNamespace labelNamespace = renameLabels.new LabelNamespace();
        renameLabels.namespaceStack.push(labelNamespace);
        labelNamespace.renameMap.put("labelName", renameLabels.new LabelInfo(1));

        // invoke
        renameLabels.visitBreakOrContinue(node);

        // assert
        assertEquals("a", node.getFirstChild().getString());
    }

    @Test
    public void testVisitBreakOrContinue_withNamedBreakOrContinueAndUnrenamedLabel_shouldNotRenameLabel() {
        // setup
        Node node = new Node(Token.BREAK);
        Node parent = new Node();
        parent.addChildToFront(node);
        node.addChildToFront(new Node("labelName"));
        LabelNamespace labelNamespace = renameLabels.new LabelNamespace();
        renameLabels.namespaceStack.push(labelNamespace);
        labelNamespace.renameMap.put("labelName", renameLabels.new LabelInfo(1));

        // invoke
        renameLabels.visitBreakOrContinue(node);

        // assert
        assertEquals("labelName", node.getFirstChild().getString());
    }

    @Test
    public void testVisitBreakOrContinue_withoutNamedBreakOrContinue_shouldDoNothing() {
        // setup
        Node node = new Node(Token.BREAK);

        // invoke
        renameLabels.visitBreakOrContinue(node);

        // assert
        assertTrue(renameLabels.namespaceStack.isEmpty());
    }
}