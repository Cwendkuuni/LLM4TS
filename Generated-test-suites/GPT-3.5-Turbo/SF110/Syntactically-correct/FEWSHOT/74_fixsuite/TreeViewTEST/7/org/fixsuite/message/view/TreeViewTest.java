
package org.fixsuite.message.view;

import org.fixsuite.message.*;
import org.fixsuite.message.info.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

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
        assertNotNull(treeView);
        assertNotNull(treeView.mainScrollPane);
        assertNotNull(treeView.fixTree);
        assertNotNull(treeView.searchLabel);
        assertNotNull(treeView.searchTextField);
        assertNotNull(treeView.searchButton);
        assertNotNull(treeView.clearButton);
        assertNotNull(treeView.expandButton);
        assertNotNull(treeView.collapseButton);
    }
    
    @Test
    public void testLoadFIXTree() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        assertNotNull(root);
        
        List<DictionaryInfo> dictionaries = library.getDictionaries();
        assertEquals(dictionaries.size(), root.getChildCount());
        
        for (int i = 0; i < root.getChildCount(); i++) {
            FixInfoTreeNode dictionaryNode = (FixInfoTreeNode) root.getChildAt(i);
            DictionaryInfo dictionary = dictionaries.get(i);
            assertEquals(dictionary.getVersion(), dictionaryNode.getUserObject());
            
            List<MessageInfo> messages = dictionary.getMessages();
            assertEquals(messages.size(), dictionaryNode.getChildCount());
            
            for (int j = 0; j < dictionaryNode.getChildCount(); j++) {
                FixInfoTreeNode messageNode = (FixInfoTreeNode) dictionaryNode.getChildAt(j);
                MessageInfo message = messages.get(j);
                assertEquals(message, messageNode.getUserObject());
                
                List<FixInfo> items = message.getItems();
                assertEquals(items.size(), messageNode.getChildCount());
                
                for (int k = 0; k < messageNode.getChildCount(); k++) {
                    FixInfoTreeNode itemNode = (FixInfoTreeNode) messageNode.getChildAt(k);
                    FixInfo item = items.get(k);
                    assertEquals(item, itemNode.getUserObject());
                }
            }
        }
    }
    
    @Test
    public void testExpandAll() {
        treeView.expandAll();
        for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
            assertTrue(treeView.fixTree.isExpanded(i));
        }
    }
    
    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
            assertFalse(treeView.fixTree.isExpanded(i));
        }
    }
    
    @Test
    public void testSearch() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.clear(root);
        treeView.collapseAll();
        
        final String searchString = "foo";
        treeView.search(root, searchString);
        
        LinkedList<FixInfoTreeNode> queue = new LinkedList<>();
        queue.add(root);
        
        while (!queue.isEmpty()) {
            FixInfoTreeNode node = queue.poll();
            
            assertTrue(node.getUserObject() instanceof FixInfo);
            
            FixInfo info = (FixInfo) node.getUserObject();
            if (info instanceof MessageInfo) {
                assertTrue(((MessageInfo) info).getName().matches(searchString) ||
                        ((MessageInfo) info).getMessageType().matches(searchString));
            } else if (info instanceof ComponentInfo) {
                assertTrue(((ComponentInfo) info).getName().matches(searchString));
            } else if (info instanceof FieldInfo) {
                assertTrue(((FieldInfo) info).getName().matches(searchString) ||
                        String.valueOf(((FieldInfo) info).getTagNumber()).matches(searchString));
            }
            
            Enumeration<FixInfoTreeNode> children = node.children();
            while (children.hasMoreElements()) {
                queue.add(children.nextElement());
            }
        }
    }
    
    @Test
    public void testClear() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.clear(root);
        
        LinkedList<FixInfoTreeNode> queue = new LinkedList<>();
        queue.add(root);
        
        while (!queue.isEmpty()) {
            FixInfoTreeNode node = queue.poll();
            
            assertFalse(node.isHighlighted());
            
            Enumeration<FixInfoTreeNode> children = node.children();
            while (children.hasMoreElements()) {
                queue.add(children.nextElement());
            }
        }
    }
    
    // Add more test cases for the remaining methods...
    
    
    
}
