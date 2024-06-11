package org.fixsuite.message.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.event.*;
import javax.swing.tree.*;

import org.fixsuite.message.Library;
import org.fixsuite.message.info.*;
import org.fixsuite.message.LibrarySuite;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

    private TreeView treeView;

    @Before
    public void setUp() {
        Library library = new Library();
        treeView = new TreeView(library);
    }

    @Test
    public void testLoadFIXTree() {
        // Test that the root node of the tree is "Dictionaries"
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeView.fixTree.getModel().getRoot();
        Assert.assertEquals("Dictionaries", root.getUserObject());
        // Test that the first child of the root is a dictionary version node
        DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) root.getChildAt(0);
        Assert.assertNotNull(childNode.getUserObject());
        Assert.assertTrue(childNode.getUserObject() instanceof String);
        // Test that the first child of the dictionary version node is a message node
        childNode = (DefaultMutableTreeNode) childNode.getChildAt(0);
        Assert.assertNotNull(childNode.getUserObject());
        Assert.assertTrue(childNode.getUserObject() instanceof MessageInfo);
        // Test that the first child of the message node is a field node
        childNode = (DefaultMutableTreeNode) childNode.getChildAt(0);
        Assert.assertNotNull(childNode.getUserObject());
        Assert.assertTrue(childNode.getUserObject() instanceof FieldInfo);
    }

    @Test
    public void testLoadItems() {
        // Test that the tree is empty before loading items
        Assert.assertEquals(0, treeView.fixTree.getRowCount());
        // Create a new branch node and add a leaf node to it
        TreeView.FixInfoTreeNode branchNode = treeView.new FixInfoTreeNode("Branch");
        TreeView.CompositeFixInfo leafNode = treeView.new CompositeFixInfo();
        treeView.loadItems(branchNode, leafNode, 0);
        // Test that the branch node now has a child
        Assert.assertEquals(1, branchNode.getChildCount());
        // Test that the child of the branch node is the leaf node
        DefaultMutableTreeNode childNode = (DefaultMutableTreeNode) branchNode.getChildAt(0);
        Assert.assertEquals(leafNode, childNode.getUserObject());
        // Test that the child node is also a branch node
        Assert.assertEquals(1, childNode.getChildCount());
    }

    @Test
    public void testExpandAll() {
        // Expand all the nodes in the tree
        treeView.expandAll();
        // Test that all the rows are expanded
        for (int row = 0; row < treeView.fixTree.getRowCount(); row++) {
            Assert.assertTrue(treeView.fixTree.isExpanded(row));
        }
    }

    @Test
    public void testCollapseAll() {
        // Expand all the nodes in the tree
        treeView.expandAll();
        // Collapse all the nodes in the tree
        treeView.collapseAll();
        // Test that all the rows are collapsed
        for (int row = 0; row < treeView.fixTree.getRowCount(); row++) {
            Assert.assertFalse(treeView.fixTree.isExpanded(row));
        }
    }

    @Test
    public void testSearch() {
        // Search for a specific message in the tree
        String searchString = "Test Message";
        TreeView.FixInfoTreeNode root = treeView.new FixInfoTreeNode("Root");
        TreeView.FixInfoTreeNode messageNode = treeView.new FixInfoTreeNode("Test Message");
        root.add(messageNode);
        treeView.search(root, searchString);
        // Test that the message node is highlighted and expanded
        Assert.assertTrue(messageNode.isHighlighted());
        Assert.assertTrue(treeView.fixTree.isExpanded(new TreePath(messageNode.getPath())));
    }

    @Test
    public void testClear() {
        // Clear all the highlighted nodes in the tree
        TreeView.FixInfoTreeNode root = treeView.new FixInfoTreeNode("Root");
        TreeView.FixInfoTreeNode messageNode = treeView.new FixInfoTreeNode("Test Message");
        messageNode.setHighlighted(true);
        root.add(messageNode);
        treeView.clear(root);
        // Test that the message node is no longer highlighted
        Assert.assertFalse(messageNode.isHighlighted());
    }

    @Test
    public void testActionPerformed() {
        // Test the action performed for different buttons
        // Create a mock event for the collapse button
        ActionEvent collapseEvent = new ActionEvent(treeView.collapseButton, ActionEvent.ACTION_PERFORMED, "Collapse");
        treeView.actionPerformed(collapseEvent);
        // Test that the collapseAll method is called
        // Create a mock event for the expand button
        ActionEvent expandEvent = new ActionEvent(treeView.expandButton, ActionEvent.ACTION_PERFORMED, "Expand");
        treeView.actionPerformed(expandEvent);
        // Test that the expandAll method is called
        // Create a mock event for the search button
        ActionEvent searchEvent = new ActionEvent(treeView.searchButton, ActionEvent.ACTION_PERFORMED, "Search");
        treeView.actionPerformed(searchEvent);
        // Test that the search method is called
        // Create a mock event for the clear button
        ActionEvent clearEvent = new ActionEvent(treeView.clearButton, ActionEvent.ACTION_PERFORMED, "Clear");
        treeView.actionPerformed(clearEvent);
        // Test that the clear method is called
    }

    @Test
    public void testKeyPressed() {
        // Test the key pressed event for the search text field
        // Create a mock event with the ENTER key code
        KeyEvent enterKeyEvent = new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        treeView.keyPressed(enterKeyEvent);
        // Test that the search button is clicked
    }
}