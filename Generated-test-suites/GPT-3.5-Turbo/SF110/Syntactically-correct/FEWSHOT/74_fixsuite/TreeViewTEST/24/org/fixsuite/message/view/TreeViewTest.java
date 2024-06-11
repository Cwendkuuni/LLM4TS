package org.fixsuite.message.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;

import org.fixsuite.message.ComponentInfo;
import org.fixsuite.message.DictionaryInfo;
import org.fixsuite.message.FieldInfo;
import org.fixsuite.message.FixInfo;
import org.fixsuite.message.Library;
import org.fixsuite.message.MessageInfo;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {
    
    private TreeView treeView;
    private FixInfoTreeNode rootNode;
    private FixInfoTreeNode childNode;
    private DictionaryInfo dictionary;
    private MessageInfo message;
    private FieldInfo field;
    
    @Before
    public void setUp() {
        dictionary = new DictionaryInfo("Test Dictionary");
        message = new MessageInfo("Test Message", "Test Type");
        field = new FieldInfo("Test Field", 123);
        rootNode = new FixInfoTreeNode("Dictionaries");
        childNode = new FixInfoTreeNode(message);
        treeView = new TreeView(new Library());
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(treeView.fixTree);
        assertNotNull(treeView.mainScrollPane);
        assertNotNull(treeView.searchLabel);
        assertNotNull(treeView.searchTextField);
        assertNotNull(treeView.searchButton);
        assertNotNull(treeView.clearButton);
        assertNotNull(treeView.expandButton);
        assertNotNull(treeView.collapseButton);
        assertNotNull(treeView.library);
    }
    
    @Test
    public void testInitGUI() {
        treeView.initGUI();
        assertNotNull(treeView.getLayout());
        assertNotNull(treeView.mainScrollPane.getPreferredSize());
        assertNotNull(treeView.fixTree.getCellRenderer());
        assertNotNull(treeView.fixTree.getTreeSelectionListeners());
        assertNotNull(treeView.searchTextField.getToolTipText());
        assertNotNull(treeView.searchTextField.getKeyListeners());
        assertNotNull(treeView.searchButton.getActionListeners());
        assertNotNull(treeView.clearButton.getActionListeners());
        assertNotNull(treeView.expandButton.getActionListeners());
        assertNotNull(treeView.collapseButton.getActionListeners());
        assertNotNull(treeView.getComponent(0));
    }
    
    @Test
    public void testLoadFIXTree() {
        dictionary.addMessage(message);
        treeView.library.addDictionary(dictionary);
        FixInfoTreeNode node = treeView.loadFIXTree();
        assertNotNull(node);
        assertEquals(1, node.getChildCount());
        assertNotNull(node.getChildAt(0));
        assertNotNull(node.getChildAt(0).getUserObject());
        assertEquals("Test Message", ((FixInfoTreeNode) node.getChildAt(0)).getUserObject());
    }
    
    @Test
    public void testLoadMessages() {
        dictionary.addMessage(message);
        treeView.loadMessages(rootNode, dictionary);
        assertEquals(1, rootNode.getChildCount());
        assertNotNull(rootNode.getChildAt(0));
        assertNotNull(rootNode.getChildAt(0).getUserObject());
        assertEquals("Test Message", ((FixInfoTreeNode) rootNode.getChildAt(0)).getUserObject());
    }
    
    @Test
    public void testLoadItems() {
        message.addItem(field);
        treeView.loadItems(childNode, message, 0);
        assertEquals(1, childNode.getChildCount());
        assertNotNull(childNode.getChildAt(0));
        assertNotNull(childNode.getChildAt(0).getUserObject());
        assertEquals("Test Field", ((FixInfoTreeNode) childNode.getChildAt(0)).getUserObject());
    }
    
    @Test
    public void testExpandAll() {
        JTree testTree = new JTree(rootNode);
        treeView.fixTree = testTree;
        treeView.expandAll();
        for (int row = 0; row < testTree.getRowCount(); row++) {
            assertEquals(true, treeView.fixTree.isExpanded(row));
        }
    }
    
    @Test
    public void testCollapseAll() {
        JTree testTree = new JTree(rootNode);
        treeView.fixTree = testTree;
        treeView.collapseAll();
        for (int row = 0; row < testTree.getRowCount(); row++) {
            assertEquals(false, treeView.fixTree.isExpanded(row));
        }
    }
    
    @Test
    public void testValueChange() {
        treeView.fixTree = new JTree(rootNode);
        treeView.valueChanged(new TreeSelectionEvent(treeView.fixTree, null, null, null, null));
        // assert details panel display
    }
    
    @Test
    public void testActionPerformed() {
        treeView.fixTree = new JTree(rootNode);
        
        // test clearButton
        treeView.actionPerformed(null);
        assertNull(((FixInfoTreeNode) treeView.fixTree.getModel().getRoot()).isHighlighted());
        
        // test collapseButton
        treeView.actionPerformed(null);
        for (int row = 0; row < treeView.fixTree.getRowCount(); row++) {
            assertEquals(false, treeView.fixTree.isExpanded(row));
        }
        
        // test expandButton
        treeView.actionPerformed(null);
        for (int row = 0; row < treeView.fixTree.getRowCount(); row++) {
            assertEquals(true, treeView.fixTree.isExpanded(row));
        }
        
        // test searchButton
        treeView.searchTextField.setText("Test Message");
        treeView.actionPerformed(null);
        assertEquals(true, rootNode.isHighlighted());
    }
    
    @Test
    public void testKeyPressed() {
        treeView.searchButton = new JButton();
        treeView.searchButton.addActionListener(treeView);
        treeView.keyPressed(null);
        // assert searchButton clicked
    }
    
}