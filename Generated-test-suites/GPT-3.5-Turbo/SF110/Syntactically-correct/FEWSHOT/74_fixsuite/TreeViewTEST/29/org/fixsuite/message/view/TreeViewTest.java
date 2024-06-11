package org.fixsuite.message.view;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {
    private Library library;
    private TreeView treeView;
    
    @Before
    public void setUp() {
        library = new Library();
        treeView = new TreeView(library);
    }
    
    @Test
    public void testInitGUI() {
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
        assertEquals("Dictionaries", root.getUserObject());
        assertEquals(0, root.getChildCount());
        
        DictionaryInfo dictionary = new DictionaryInfo("FIX.4.4");
        library.addDictionary(dictionary);
        
        root = treeView.loadFIXTree();
        assertNotNull(root);
        assertEquals("Dictionaries", root.getUserObject());
        assertEquals(1, root.getChildCount());
        
        FixInfoTreeNode dictionaryNode = (FixInfoTreeNode) root.getChildAt(0);
        assertEquals("FIX.4.4", dictionaryNode.getUserObject());
        assertEquals(0, dictionaryNode.getChildCount());
        
        MessageInfo message = new MessageInfo("ExecReport");
        dictionary.addMessage(message);
        
        root = treeView.loadFIXTree();
        assertNotNull(root);
        assertEquals("Dictionaries", root.getUserObject());
        assertEquals(1, root.getChildCount());
        
        dictionaryNode = (FixInfoTreeNode) root.getChildAt(0);
        assertEquals("FIX.4.4", dictionaryNode.getUserObject());
        assertEquals(1, dictionaryNode.getChildCount());
        
        FixInfoTreeNode messageNode = (FixInfoTreeNode) dictionaryNode.getChildAt(0);
        assertEquals(message, messageNode.getUserObject());
        assertEquals(0, messageNode.getChildCount());
    }
    
    @Test
    public void testLoadMessages() {
        DictionaryInfo dictionary = new DictionaryInfo("FIX.4.4");
        library.addDictionary(dictionary);
        
        FixInfoTreeNode versionNode = new FixInfoTreeNode("FIX.4.4");
        treeView.loadMessages(versionNode, dictionary);
        
        assertEquals(0, versionNode.getChildCount());
        
        MessageInfo message = new MessageInfo("ExecReport");
        dictionary.addMessage(message);
        
        versionNode = new FixInfoTreeNode("FIX.4.4");
        treeView.loadMessages(versionNode, dictionary);
        
        assertEquals(1, versionNode.getChildCount());
        
        FixInfoTreeNode messageNode = (FixInfoTreeNode) versionNode.getChildAt(0);
        assertEquals(message, messageNode.getUserObject());
        assertEquals(0, messageNode.getChildCount());
    }
    
    @Test
    public void testLoadItems() {
        MessageInfo message = new MessageInfo("ExecReport");
        CompositeFixInfo compositeFixInfo = new CompositeFixInfo("Group");
        compositeFixInfo.addItem(new FieldInfo("Field1", 100));
        message.addComponent(compositeFixInfo);
        
        FixInfoTreeNode branchNode = new FixInfoTreeNode(message);
        int level = 0;
        treeView.loadItems(branchNode, message, level);
        
        assertEquals(1, branchNode.getChildCount());
        
        FixInfoTreeNode compositeNode = (FixInfoTreeNode) branchNode.getChildAt(0);
        assertEquals(compositeFixInfo, compositeNode.getUserObject());
        assertEquals(1, compositeNode.getChildCount());
        
        FixInfoTreeNode fieldNode = (FixInfoTreeNode) compositeNode.getChildAt(0);
        assertEquals("Field1 (100)", fieldNode.getUserObject());
        assertEquals(0, fieldNode.getChildCount());
    }
    
    @Test
    public void testExpandAll() {
        // Cannot test since JTree and JScrollPane are not mocked
    }
    
    @Test
    public void testCollapseAll() {
        // Cannot test since JTree and JScrollPane are not mocked
    }
    
    @Test
    public void testSearch() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.search(root, "ExecReport");
        
        int count = 0;
        while (count < root.getChildCount()) {
            FixInfoTreeNode childNode = (FixInfoTreeNode) root.getChildAt(count);
            if (childNode.getUserObject() instanceof MessageInfo) {
                assertTrue(childNode.isHighlighted());
            } else {
                assertFalse(childNode.isHighlighted());
            }
            count++;
        }
        
        count = 0;
        while (count < root.getChildCount()) {
            FixInfoTreeNode childNode = (FixInfoTreeNode) root.getChildAt(count);
            assertFalse(childNode.isHighlighted());
            int innerCount = 0;
            while (innerCount < childNode.getChildCount()) {
                FixInfoTreeNode innerChildNode = (FixInfoTreeNode) childNode.getChildAt(innerCount);
                assertFalse(innerChildNode.isHighlighted());
                innerCount++;
            }
            count++;
        }
    }
    
    @Test
    public void testClear() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.search(root, "ExecReport");
        
        int count = 0;
        while (count < root.getChildCount()) {
            FixInfoTreeNode childNode = (FixInfoTreeNode) root.getChildAt(count);
            assertEquals(true, childNode.isHighlighted());
            treeView.clear(childNode);
            assertEquals(false, childNode.isHighlighted());
            
            int innerCount = 0;
            while (innerCount < childNode.getChildCount()) {
                FixInfoTreeNode innerChildNode = (FixInfoTreeNode) childNode.getChildAt(innerCount);
                assertEquals(false, innerChildNode.isHighlighted());
                innerCount++;
            }
            
            count++;
        }
    }
    
    // Additional Test Cases
}