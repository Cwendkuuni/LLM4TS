package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RenameLabelsTest {

    @Test
    public void testProcessLabels() {
        // Test empty program
        Node root = NodeFactory.createRoot();
        RenameLabels renameLabels = new RenameLabels(AbstractCompilerFactory.getTestCompiler());
        renameLabels.process(NodeFactory.createExterns(), root);
        assertEquals(0, root.getChildCount());

        // Test simple label
        Node labelNode = NodeFactory.createLabel("a");
        Node root1 = NodeFactory.createRoot();
        root1.addChild(labelNode);
        renameLabels.process(NodeFactory.createExterns(), root1);
        assertEquals("a", labelNode.getString());

        // Test label with nested labels
        Node root2 = NodeFactory.createRoot();
        Node labelA = NodeFactory.createLabel("a");
        Node labelB = NodeFactory.createLabel("b");
        Node blockA = NodeFactory.createBlock();
        Node blockB = NodeFactory.createBlock();
        labelA.addChild(blockA);
        labelB.addChild(blockB);
        labelA.addChild(labelB);
        root2.addChild(labelA);
        renameLabels.process(NodeFactory.createExterns(), root2);
        assertEquals("a_b", labelB.getString());

        // Test label with multiple references
        Node root3 = NodeFactory.createRoot();
        Node labelA = NodeFactory.createLabel("a");
        Node labelA1 = NodeFactory.createLabel("a");
        Node block = NodeFactory.createBlock();
        labelA.addChild(block);
        labelA1.addChild(block);
        root3.addChild(labelA);
        root3.addChild(labelA1);
        renameLabels.process(NodeFactory.createExterns(), root3);
        assertEquals("a_1", labelA1.getString());

        // Test label with break statement
        Node root4 = NodeFactory.createRoot();
        Node labelA = NodeFactory.createLabel("a");
        Node breakNode = NodeFactory.createBreak();
        Node nameNode = NodeFactory.createName("a");
        breakNode.addChild(nameNode);
        labelA.addChild(breakNode);
        root4.addChild(labelA);
        renameLabels.process(NodeFactory.createExterns(), root4);
        assertEquals("a", nameNode.getString());

        // Test label with continue statement
        Node root5 = NodeFactory.createRoot();
        Node labelA = NodeFactory.createLabel("a");
        Node continueNode = NodeFactory.createContinue();
        Node nameNode = NodeFactory.createName("a");
        continueNode.addChild(nameNode);
        labelA.addChild(continueNode);
        root5.addChild(labelA);
        renameLabels.process(NodeFactory.createExterns(), root5);
        assertEquals("a", nameNode.getString());

        // Test label with break and continue statements
        Node root6 = NodeFactory.createRoot();
        Node labelA = NodeFactory.createLabel("a");
        Node breakNode = NodeFactory.createBreak();
        Node nameNode1 = NodeFactory.createName("a");
        breakNode.addChild(nameNode1);
        Node continueNode = NodeFactory.createContinue();
        Node nameNode2 = NodeFactory.createName("a");
        continueNode.addChild(nameNode2);
        labelA.addChild(breakNode);
        labelA.addChild(continueNode);
        root6.addChild(labelA);
        renameLabels.process(NodeFactory.createExterns(), root6);
        assertEquals("a", nameNode1.getString());
        assertEquals("a_1", nameNode2.getString());

        // Test label with self reference
        Node root7 = NodeFactory.createRoot();
        Node labelA = NodeFactory.createLabel("a");
        Node labelA1 = NodeFactory.createLabel("a");
        Node labelB = NodeFactory.createLabel("b");
        Node blockA = NodeFactory.createBlock();
        Node blockB = NodeFactory.createBlock();
        labelA.addChild(blockA);
        labelB.addChild(blockB);
        labelA.addChild(labelB);
        labelB.addChild(labelA1);
        root7.addChild(labelA);
        renameLabels.process(NodeFactory.createExterns(), root7);
        assertEquals("a_1", labelA1.getString());

        // Test label removal
        Node root8 = NodeFactory.createRoot();
        Node labelA = NodeFactory.createLabel("a");
        Node labelB = NodeFactory.createLabel("b");
        Node blockA = NodeFactory.createBlock();
        Node blockB = NodeFactory.createBlock();
        labelA.addChild(blockA);
        labelB.addChild(blockB);
        labelA.addChild(labelB);
        root8.addChild(labelA);
        renameLabels.process(NodeFactory.createExterns(), root8);
        assertNull(labelB.getFirstChild());

        // Test label removal with break statement
        Node root9 = NodeFactory.createRoot();
        Node labelA = NodeFactory.createLabel("a");
        Node labelB = NodeFactory.createLabel("b");
        Node breakNode = NodeFactory.createBreak();
        Node nameNode1 = NodeFactory.createName("a");
        breakNode.addChild(nameNode1);
        labelB.addChild(breakNode);
        labelA.addChild(labelB);
        root9.addChild(labelA);
        renameLabels.process(NodeFactory.createExterns(), root9);
        assertNull(labelB.getFirstChild());

        // Test label removal with continue statement
        Node root10 = NodeFactory.createRoot();
        Node labelA = NodeFactory.createLabel("a");
        Node labelB = NodeFactory.createLabel("b");
        Node continueNode = NodeFactory.createContinue();
        Node nameNode1 = NodeFactory.createName("a");
        continueNode.addChild(nameNode1);
        labelB.addChild(continueNode);
        labelA.addChild(labelB);
        root10.addChild(labelA);
        renameLabels.process(NodeFactory.createExterns(), root10);
        assertNull(labelB.getFirstChild());
    }
}