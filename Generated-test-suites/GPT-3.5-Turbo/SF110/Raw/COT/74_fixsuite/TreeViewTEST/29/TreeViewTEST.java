import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.fixsuite.message.view.TreeView;
import org.fixsuite.message.info.Library;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.FieldInfo;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;

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
        assertNotNull(treeView);
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
        DictionaryInfo dictionary = new DictionaryInfo();
        library.getDictionaries().add(dictionary);
        
        treeView.loadFIXTree();
        
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeView.getFixTree().getModel().getRoot();
        assertEquals(1, root.getChildCount());
        assertEquals("Dictionaries", root.getUserObject().toString());
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(0);
        assertEquals(0, node.getChildCount());
        assertEquals(dictionary.getVersion(), node.getUserObject().toString());
    }
    
    @Test
    public void testLoadMessages() {
        DictionaryInfo dictionary = new DictionaryInfo();
        library.getDictionaries().add(dictionary);
        
        MessageInfo message = new MessageInfo();
        dictionary.getMessages().add(message);
        
        treeView.loadMessages(new TreeView.FixInfoTreeNode((Object)"VersionNode"), dictionary);
        
        DefaultMutableTreeNode versionNode = new TreeView.FixInfoTreeNode((Object)"VersionNode");
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) versionNode.getChildAt(0);
        assertEquals(0, node.getChildCount());
        assertEquals(message, node.getUserObject());
    }
    
    @Test
    public void testLoadItems() {
        CompositeFixInfo leafNode = new CompositeFixInfo();
        FixInfo info = new FixInfo();
        leafNode.getItems().add(info);
        
        treeView.loadItems(new TreeView.FixInfoTreeNode((Object)"BranchNode"), leafNode, 0);
        
        DefaultMutableTreeNode branchNode = new TreeView.FixInfoTreeNode((Object)"BranchNode");
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) branchNode.getChildAt(0);
        assertEquals(0, node.getChildCount());
        assertEquals(info, node.getUserObject());
    }
    
    @Test
    public void testExpandAll() {
        treeView.expandAll();
        
        for (int row = 0; row < treeView.getFixTree().getRowCount(); ++row) {
            assertTrue(treeView.getFixTree().isExpanded(row));
        }
    }
    
    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        
        for (int row = 0; row < treeView.getFixTree().getRowCount(); ++row) {
            assertFalse(treeView.getFixTree().isExpanded(row));
        }
    }
    
    @Test
    public void testSearch() {
        FixInfoTreeNode root = new FixInfoTreeNode((Object)"Root");
        FixInfoTreeNode node1 = new FixInfoTreeNode((Object)"Node1");
        FixInfoTreeNode node2 = new FixInfoTreeNode((Object)"Node2");
        root.add(node1);
        root.add(node2);
        
        treeView.search(node1, "Node1");
        
        assertTrue(node1.isHighlighted());
        assertFalse(node2.isHighlighted());
    }
    
    @Test
    public void testClear() {
        FixInfoTreeNode root = new FixInfoTreeNode((Object)"Root");
        FixInfoTreeNode node1 = new FixInfoTreeNode((Object)"Node1");
        FixInfoTreeNode node2 = new FixInfoTreeNode((Object)"Node2");
        root.add(node1);
        root.add(node2);
        
        node1.setHighlighted(true);
        node2.setHighlighted(true);
        
        treeView.clear(root);
        
        assertFalse(node1.isHighlighted());
        assertFalse(node2.isHighlighted());
    }
    
    @Test
    public void testValueChanged() {
        treeView.valueChanged(null);
        
        // Test if the method is properly called without any errors
    }
    
    @Test
    public void testActionPerformed() {
        treeView.actionPerformed(null);
        
        // Test if the method is properly called without any errors
    }
    
    @Test
    public void testKeyPressed() {
        treeView.keyPressed(null);
        
        // Test if the method is properly called without any errors
    }
}