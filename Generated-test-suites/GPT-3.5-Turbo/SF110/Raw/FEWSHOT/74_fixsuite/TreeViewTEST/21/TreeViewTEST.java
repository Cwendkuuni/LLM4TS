package org.fixsuite.message.view;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TreeViewTest {

    private TreeView treeView;
    private Library library;

    @Before
    public void setUp() {
        library = new Library();
        treeView = new TreeView(library);
    }

    @Test
    public void testInitGUI() {
        assertNotNull(treeView.getMainScrollPane());
        assertNotNull(treeView.getFixTree());
        assertNotNull(treeView.getSearchLabel());
        assertNotNull(treeView.getSearchTextField());
        assertNotNull(treeView.getSearchButton());
        assertNotNull(treeView.getClearButton());
        assertNotNull(treeView.getExpandButton());
        assertNotNull(treeView.getCollapseButton());
    }

    @Test
    public void testLoadFIXTree() {
        FixInfoTreeNode rootNode = treeView.loadFIXTree();

        assertNotNull(rootNode);
        assertEquals("Dictionaries", rootNode.getUserObject());

        Enumeration<FixInfoTreeNode> children = rootNode.children();
        while (children.hasMoreElements()) {
            FixInfoTreeNode child = children.nextElement();
            assertTrue(child.getUserObject() instanceof String);

            Enumeration<FixInfoTreeNode> subChildren = child.children();
            while (subChildren.hasMoreElements()) {
                FixInfoTreeNode subChild = subChildren.nextElement();
                assertTrue(subChild.getUserObject() instanceof MessageInfo);
                MessageInfo messageInfo = (MessageInfo) subChild.getUserObject();
                assertNotNull(messageInfo.getName());
                assertNotNull(messageInfo.getMessageType());

                Enumeration<FixInfoTreeNode> subSubChildren = subChild.children();
                while (subSubChildren.hasMoreElements()) {
                    FixInfoTreeNode subSubChild = subSubChildren.nextElement();
                    assertTrue(subSubChild.getUserObject() instanceof FixInfo);
                }
            }
        }
    }

    @Test
    public void testLoadMessages() {
        FixInfoTreeNode versionNode = new FixInfoTreeNode("Version");
        DictionaryInfo dictionary = new DictionaryInfo();
        treeView.loadMessages(versionNode, dictionary);

        Enumeration<FixInfoTreeNode> children = versionNode.children();
        while (children.hasMoreElements()) {
            FixInfoTreeNode child = children.nextElement();
            assertTrue(child.getUserObject() instanceof MessageInfo);
            MessageInfo messageInfo = (MessageInfo) child.getUserObject();
            assertNotNull(messageInfo.getName());
            assertNotNull(messageInfo.getMessageType());

            Enumeration<FixInfoTreeNode> subChildren = child.children();
            while (subChildren.hasMoreElements()) {
                FixInfoTreeNode subChild = subChildren.nextElement();
                assertTrue(subChild.getUserObject() instanceof FixInfo);
            }
        }
    }

    @Test
    public void testLoadItems() {
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
        CompositeFixInfo leafNode = new CompositeFixInfo();
        treeView.loadItems(branchNode, leafNode, 0);

        Enumeration<FixInfoTreeNode> children = branchNode.children();
        while (children.hasMoreElements()) {
            FixInfoTreeNode child = children.nextElement();
            assertTrue(child.getUserObject() instanceof FixInfo);
        }
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();

        assertEquals(treeView.getFixTree().getRowCount(), treeView.getFixTree().getExpandedRowCount());
    }

    @Test
    public void testCollapseAll() {
        treeView.collapseAll();

        assertEquals(0, treeView.getFixTree().getExpandedRowCount());
    }

    @Test
    public void testSearch() {
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        treeView.search(rootNode, "searchString");

        Enumeration<FixInfoTreeNode> descendants = rootNode.depthFirstEnumeration();
        while (descendants.hasMoreElements()) {
            FixInfoTreeNode node = descendants.nextElement();
            assertTrue(node.isHighlighted());
        }
    }

    @Test
    public void testClear() {
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        treeView.clear(rootNode);

        Enumeration<FixInfoTreeNode> descendants = rootNode.depthFirstEnumeration();
        while (descendants.hasMoreElements()) {
            FixInfoTreeNode node = descendants.nextElement();
            assertFalse(node.isHighlighted());
        }
    }

    @Test
    public void testValueChanged() {
        TreeSelectionEvent event = new TreeSelectionEvent(treeView.getFixTree(), null, true, null, null);

        treeView.valueChanged(event);
    }

    @Test
    public void testActionPerformed() {
        ActionEvent event = new ActionEvent(treeView.getSearchButton(), 1, "");

        treeView.actionPerformed(event);
    }

    @Test
    public void testKeyPressed() {
        KeyEvent event = new KeyEvent(treeView.getSearchTextField(), 1, 1, 1, KeyEvent.VK_ENTER, 'a');

        treeView.keyPressed(event);
    }

    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(treeView.getSearchTextField(), 1, 1, 1, KeyEvent.VK_ENTER, 'a');

        treeView.keyReleased(event);
    }

    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(treeView.getSearchTextField(), 1, 1, 1, KeyEvent.VK_ENTER, 'a');

        treeView.keyTyped(event);
    }
}