
package org.fixsuite.message.view;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;

import org.fixsuite.message.DictionaryInfo;
import org.fixsuite.message.FixInfo;
import org.fixsuite.message.FixInfoTreeNode;
import org.fixsuite.message.Library;
import org.fixsuite.message.MessageInfo;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {

    private TreeView treeView;
    private Library mockLibrary;
    
    @Before
    public void setup() {
        mockLibrary = mock(Library.class);
        treeView = new TreeView(mockLibrary);
    }
    
    @Test
    public void testInitGUI() {
        treeView.initGUI();
        
        JPanel panel = (JPanel)treeView.getComponent(0);
        assertNotNull(panel);
        
        JScrollPane scrollPane = (JScrollPane)panel.getComponent(0);
        assertNotNull(scrollPane);
        
        JTree fixTree = (JTree)scrollPane.getViewport().getView();
        assertNotNull(fixTree);
        //  assert the FixTree's properties
        
        JToolBar toolbar = (JToolBar)panel.getComponent(1);
        assertNotNull(toolbar);
        //  assert the Toolbar's properties
    }
    
    @Test
    public void testLoadFIXTree() {
        DictionaryInfo mockDictionary = mock(DictionaryInfo.class);
        FixInfoTreeNode dictionaryNode = new FixInfoTreeNode((Object)mockDictionary.getVersion());
        when(mockLibrary.getDictionaries()).thenReturn(Arrays.asList(mockDictionary));
        
        FixInfoTreeNode root = treeView.loadFIXTree();
        
        assertEquals(dictionaryNode, root.getChildAt(0));
    }
    
    @Test
    public void testLoadMessages() {
        DictionaryInfo mockDictionary = mock(DictionaryInfo.class);
        when(mockDictionary.getMessages()).thenReturn(Arrays.asList(mock(MessageInfo.class)));
        FixInfoTreeNode versionNode = new FixInfoTreeNode((Object)mockDictionary.getVersion());
        
        treeView.loadMessages(versionNode, mockDictionary);
        
        assertNotNull(versionNode.getChildAt(0));
    }
    
    @Test
    public void testLoadItems() {
        CompositeFixInfo mockLeafNode = mock(CompositeFixInfo.class);
        when(mockLeafNode.getItems()).thenReturn(Arrays.asList(mock(FixInfo.class)));
        FixInfoTreeNode branchNode = new FixInfoTreeNode((Object)mockLeafNode);
        
        treeView.loadItems(branchNode, mockLeafNode, 0);
        
        assertNotNull(branchNode.getChildAt(0));
    }
    
    @Test
    public void testExpandAll() {
        JTree mockFixTree = mock(JTree.class);
        JScrollPane mockScrollPane = mock(JScrollPane.class);
        when(treeView.fixTree).thenReturn(mockFixTree);
        when(treeView.mainScrollPane).thenReturn(mockScrollPane);
        when(mockFixTree.getRowCount()).thenReturn(1);
        
        treeView.expandAll();
        
        verify(mockFixTree, times(1)).expandRow(0);
    }
    
    @Test
    public void testCollapseAll() {
        JTree mockFixTree = mock(JTree.class);
        JScrollPane mockScrollPane = mock(JScrollPane.class);
        when(treeView.fixTree).thenReturn(mockFixTree);
        when(treeView.mainScrollPane).thenReturn(mockScrollPane);
        when(mockFixTree.getRowCount()).thenReturn(1);
        
        treeView.collapseAll();
        
        verify(mockFixTree, times(1)).collapseRow(0);
    }
    
    @Test
    public void testSearch() {
        FixInfoTreeNode mockNode = mock(FixInfoTreeNode.class);
        when(mockNode.getUserObject()).thenReturn(mock(FixInfo.class));
        String searchString = "regex";
        
        treeView.search(mockNode, searchString);
        
        // Verify certain actions based on the properties of the user object
    }
    
    @Test
    public void testClear() {
        FixInfoTreeNode mockNode = mock(FixInfoTreeNode.class);
        when(mockNode.getUserObject()).thenReturn(mock(FixInfo.class));
        
        treeView.clear(mockNode);
        
        // Verify certain actions based on the properties of the user object
    }
    
    @Test
    public void testValueChanged() {
        JTree mockFixTree = mock(JTree.class);
        FixInfoTreeNode mockNode = mock(FixInfoTreeNode.class);
        TreeSelectionEvent mockEvent = mock(TreeSelectionEvent.class);
        when(treeView.fixTree).thenReturn(mockFixTree);
        when(mockFixTree.getLastSelectedPathComponent()).thenReturn(mockNode);
        
        treeView.valueChanged(mockEvent);
        
        // Verify certain actions based on the properties of the node and user object
    }
    
    @Test
    public void testActionPerformed_collapseButton() {
        JButton mockCollapseButton = mock(JButton.class);
        when(mockCollapseButton.equals(any())).thenReturn(true);
        when(treeView.collapseButton).thenReturn(mockCollapseButton);
        
        treeView.actionPerformed(null);
        
        // Verify certain actions based on the properties of the collapseButton
    }
    
    @Test
    public void testActionPerformed_expandButton() {
        JButton mockExpandButton = mock(JButton.class);
        when(mockExpandButton.equals(any())).thenReturn(true);
        when(treeView.expandButton).thenReturn(mockExpandButton);
        
        treeView.actionPerformed(null);
        
        // Verify certain actions based on the properties of the expandButton
    }
    
    @Test
    public void testActionPerformed_searchButton() {
        JButton mockSearchButton = mock(JButton.class);
        when(mockSearchButton.equals(any())).thenReturn(true);
        when(treeView.searchButton).thenReturn(mockSearchButton);
        
        JTextField mockSearchTextField = mock(JTextField.class);
        when(treeView.searchTextField).thenReturn(mockSearchTextField);
        
        treeView.actionPerformed(null);
        
        // Verify certain actions based on the properties of the searchButton and searchTextField
    }
    
    @Test
    public void testActionPerformed_clearButton() {
        JButton mockClearButton = mock(JButton.class);
        when(mockClearButton.equals(any())).thenReturn(true);
        when(treeView.clearButton).thenReturn(mockClearButton);
        
        treeView.actionPerformed(null);
        
        // Verify certain actions based on the properties of the clearButton
    }
    
    @Test
    public void testKeyPressed() {
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(10);
        
        JButton mockSearchButton = mock(JButton.class);
        when(mockSearchButton.doClick()).thenReturn(true);
        when(treeView.searchButton).thenReturn(mockSearchButton);
        
        treeView.keyPressed(mockKeyEvent);
        
        // Verify certain actions based on the properties of the mockKeyEvent and searchButton
    }
}
