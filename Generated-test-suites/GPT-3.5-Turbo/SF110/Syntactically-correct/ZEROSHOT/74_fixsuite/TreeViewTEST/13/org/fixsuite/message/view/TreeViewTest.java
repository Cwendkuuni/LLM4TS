package org.fixsuite.message.view;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TreeViewTest {
    
    private TreeView treeView;
    private Library library;
    
    @Before
    public void setUp() {
        library = new Library();
        treeView = new TreeView(library);
    }
    
    @After
    public void tearDown() {
        library = null;
        treeView = null;
    }
    
    @Test
    public void testInitGUI() {
        treeView.initGUI();
        assertNotNull(treeView.getLayout());
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
        FixInfoTreeNode root = treeView.loadFIXTree();
        assertNotNull(root);
        assertNotNull(root.getUserObject());
        assertEquals("Dictionaries", root.getUserObject());
        assertEquals(1, root.getChildCount());
        
        FixInfoTreeNode childNode = (FixInfoTreeNode) root.getChildAt(0);
        assertNotNull(childNode);
        assertNotNull(childNode.getUserObject());
        assertTrue(childNode.getUserObject() instanceof DictionaryInfo);
        
        DictionaryInfo dictionary = (DictionaryInfo) childNode.getUserObject();
        assertEquals(dictionary.getVersion(), childNode.getUserObject());
        assertEquals(1, childNode.getChildCount());
        
        FixInfoTreeNode grandChildNode = (FixInfoTreeNode) childNode.getChildAt(0);
        assertNotNull(grandChildNode);
        assertNotNull(grandChildNode.getUserObject());
        assertTrue(grandChildNode.getUserObject() instanceof MessageInfo);
    }
    
    @Test
    public void testLoadMessages() {
        FixInfoTreeNode versionNode = new FixInfoTreeNode("Version");
        DictionaryInfo dictionary = new DictionaryInfo();
        treeView.loadMessages(versionNode, dictionary);
        assertNotNull(versionNode);
        assertEquals(1, versionNode.getChildCount());
        
        FixInfoTreeNode childNode = (FixInfoTreeNode) versionNode.getChildAt(0);
        assertNotNull(childNode);
        assertNotNull(childNode.getUserObject());
        assertTrue(childNode.getUserObject() instanceof MessageInfo);
        
        MessageInfo message = (MessageInfo) childNode.getUserObject();
        assertEquals(message.getName() + " (" + message.getMessageType() + ")", childNode.getUserObject());
        assertEquals(0, childNode.getChildCount());
    }
    
    @Test
    public void testLoadItems() {
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
        CompositeFixInfo leafNode = new CompositeFixInfo();
        int level = 0;
        treeView.loadItems(branchNode, leafNode, level);
        assertNotNull(branchNode);
        assertEquals(1, branchNode.getChildCount());
        
        FixInfoTreeNode childNode = (FixInfoTreeNode) branchNode.getChildAt(0);
        assertNotNull(childNode);
        assertNotNull(childNode.getUserObject());
        assertTrue(childNode.getUserObject() instanceof FixInfo);
        
        FixInfo info = (FixInfo) childNode.getUserObject();
        assertEquals(info.getName(), childNode.getUserObject());
        assertEquals(0, childNode.getChildCount());
    }
    
    @Test
    public void testExpandAll() {
        treeView.expandAll();
    }
    
    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
    }
    
    @Test
    public void testSearch() {
        String searchString = "Test";
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.search(root, searchString);
    }
    
    @Test
    public void testClear() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.clear(root);
    }
    
    @Test
    public void testValueChanged() {
        treeView.valueChanged(null);
    }
    
    @Test
    public void testActionPerformed() {
        treeView.actionPerformed(null);
    }
    
    @Test
    public void testKeyPressed() {
        treeView.keyPressed(null);
    }
    
    @Test
    public void testKeyReleased() {
        treeView.keyReleased(null);
    }
    
    @Test
    public void testKeyTyped() {
        treeView.keyTyped(null);
    }
}