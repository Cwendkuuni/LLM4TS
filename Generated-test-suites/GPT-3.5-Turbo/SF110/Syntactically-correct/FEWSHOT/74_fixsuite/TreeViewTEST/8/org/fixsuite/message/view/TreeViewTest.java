package org.fixsuite.message.view;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.tree.*;

public class TreeViewTest {

    private Library library;
    private TreeView treeView;

    @Before
    public void setUp() {
        library = new Library();
        treeView = new TreeView(library);
    }

    @Test
    public void testTreeViewConstructor() {
        assertNotNull(treeView.mainScrollPane);
        assertNotNull(treeView.fixTree);
        assertNotNull(treeView.searchLabel);
        assertNotNull(treeView.searchTextField);
        assertNotNull(treeView.searchButton);
        assertNotNull(treeView.clearButton);
        assertNotNull(treeView.expandButton);
        assertNotNull(treeView.collapseButton);
        assertEquals(library, treeView.library);
    }

    @Test
    public void testInitGUI() {
        treeView.initGUI();
        assertEquals(BorderLayout.class, treeView.getLayout().getClass());
        assertEquals(JScrollPane.class, treeView.mainScrollPane.getClass());
        assertEquals(new Dimension(300, 400), treeView.mainScrollPane.getPreferredSize());
        assertEquals(JTree.class, treeView.fixTree.getClass());
        assertEquals(FixInfoCellRenderer.class, treeView.fixTree.getCellRenderer().getClass());
        assertEquals(JLabel.class, treeView.searchLabel.getClass());
        assertEquals(JTextField.class, treeView.searchTextField.getClass());
        assertEquals(JButton.class, treeView.searchButton.getClass());
        assertEquals(JButton.class, treeView.clearButton.getClass());
        assertEquals(JButton.class, treeView.expandButton.getClass());
        assertEquals(JButton.class, treeView.collapseButton.getClass());
        assertEquals(JToolBar.class, treeView.getComponent(1).getClass());
    }

    @Test
    public void testLoadFIXTree() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        assertEquals("Dictionaries", root.getUserObject());

        for (int i = 0; i < library.getDictionaries().size(); i++) {
            DictionaryInfo dictionary = library.getDictionaries().get(i);
            FixInfoTreeNode dictionaryNode = (FixInfoTreeNode) root.getChildAt(i);
            assertEquals(dictionary.getVersion(), dictionaryNode.getUserObject());

            for (int j = 0; j < dictionary.getMessages().size(); j++) {
                MessageInfo message = dictionary.getMessages().get(j);
                FixInfoTreeNode messageNode = (FixInfoTreeNode) dictionaryNode.getChildAt(j);
                assertEquals(message, messageNode.getUserObject());

                for (int k = 0; k < message.getItems().size(); k++) {
                    FixInfo info = message.getItems().get(k);
                    FixInfoTreeNode infoNode = (FixInfoTreeNode) messageNode.getChildAt(k);
                    assertEquals(info, infoNode.getUserObject());

                    if (info instanceof CompositeFixInfo) {
                        CompositeFixInfo compositeInfo = (CompositeFixInfo) info;
                        if (compositeInfo.getItems().size() > 0) {
                            FixInfoTreeNode childNode = (FixInfoTreeNode) infoNode.getChildAt(0);
                            assertEquals(compositeInfo.getItems().get(0), childNode.getUserObject());
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testLoadItems() {
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
        CompositeFixInfo leafNode = new CompositeFixInfo();
        leafNode.addItem(new FixInfo());

        treeView.loadItems(branchNode, leafNode, 0);

        assertEquals(1, branchNode.getChildCount());
        FixInfoTreeNode childNode = (FixInfoTreeNode) branchNode.getChildAt(0);
        assertEquals(1, childNode.getChildCount());
        assertEquals(FixInfo.class, childNode.getUserObject().getClass());
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();
        // Verify that all rows in JTree are expanded
        for (int row = 0; row < treeView.fixTree.getRowCount(); row++) {
            assertTrue(treeView.fixTree.isExpanded(row));
        }
    }

    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        // Verify that all rows in JTree are collapsed
        for (int row = 0; row < treeView.fixTree.getRowCount(); row++) {
            assertFalse(treeView.fixTree.isExpanded(row));
        }
    }

    @Test
    public void testSearch() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.search(root, "searchString");

        // Verify that nodes matching the search string are highlighted and expanded
        for (int i = 0; i < root.getChildCount(); i++) {
            FixInfoTreeNode dictionaryNode = (FixInfoTreeNode) root.getChildAt(i);

            for (int j = 0; j < dictionaryNode.getChildCount(); j++) {
                FixInfoTreeNode messageNode = (FixInfoTreeNode) dictionaryNode.getChildAt(j);

                for (int k = 0; k < messageNode.getChildCount(); k++) {
                    FixInfoTreeNode infoNode = (FixInfoTreeNode) messageNode.getChildAt(k);

                    if (infoNode.getUserObject() instanceof FixInfo) {
                        assertTrue(infoNode.isHighlighted());
                        assertTrue(treeView.fixTree.isExpanded(new TreePath(infoNode.getPath())));
                    }
                }
            }
        }
    }

    @Test
    public void testClear() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.search(root, "searchString");
        treeView.clear(root);

        // Verify that all nodes are cleared (not highlighted)
        for (int i = 0; i < root.getChildCount(); i++) {
            FixInfoTreeNode dictionaryNode = (FixInfoTreeNode) root.getChildAt(i);

            for (int j = 0; j < dictionaryNode.getChildCount(); j++) {
                FixInfoTreeNode messageNode = (FixInfoTreeNode) dictionaryNode.getChildAt(j);

                for (int k = 0; k < messageNode.getChildCount(); k++) {
                    FixInfoTreeNode infoNode = (FixInfoTreeNode) messageNode.getChildAt(k);

                    assertFalse(infoNode.isHighlighted());
                }
            }
        }
    }

    @Test
    public void testValueChanged() {
        //TODO: Implement test case for TreeView.valueChanged() method
    }

    @Test
    public void testActionPerformed() {
        //TODO: Implement test cases for TreeView.actionPerformed() method
    }

    @Test
    public void testKeyPressed() {
        //TODO: Implement test case for TreeView.keyPressed() method
    }
}