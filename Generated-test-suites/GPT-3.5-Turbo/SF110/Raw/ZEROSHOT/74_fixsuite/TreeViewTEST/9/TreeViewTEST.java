import org.fixsuite.message.view.*;
import org.fixsuite.message.info.*;
import org.fixsuite.message.Library;
import org.fixsuite.message.FixInfo;
import org.fixsuite.message.info.FixInfoTreeNode;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.FieldInfo;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import java.awt.*;
import java.awt.event.*;

import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;

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
        assertNotNull(treeView);
        assertNotNull(treeView.mainScrollPane);
        assertNotNull(treeView.fixTree);
        assertNotNull(treeView.searchLabel);
        assertNotNull(treeView.searchTextField);
        assertNotNull(treeView.searchButton);
        assertNotNull(treeView.clearButton);
        assertNotNull(treeView.expandButton);
        assertNotNull(treeView.collapseButton);
        
        assertNotNull(treeView.fixTree.getCellRenderer());
        assertNotNull(treeView.fixTree.getTreeSelectionListeners());
        
        assertNotNull(treeView.searchTextField.getKeyListeners());
        assertNotNull(treeView.searchButton.getActionListeners());
        assertNotNull(treeView.clearButton.getActionListeners());
        assertNotNull(treeView.expandButton.getActionListeners());
        assertNotNull(treeView.collapseButton.getActionListeners());
        
        assertNotNull(treeView.getComponent(0));
    }
    
    @Test
    public void testLoadFIXTree() {
        FixInfoTreeNode treeNode = treeView.loadFIXTree();
        assertNotNull(treeNode);
        
        Enumeration<FixInfoTreeNode> rootChildren = treeNode.children();
        while (rootChildren.hasMoreElements()) {
            FixInfoTreeNode rootChild = rootChildren.nextElement();
            assertNotNull(rootChild.getUserObject());
            
            Enumeration<FixInfoTreeNode> versionChildren = rootChild.children();
            while (versionChildren.hasMoreElements()) {
                FixInfoTreeNode versionChild = versionChildren.nextElement();
                assertNotNull(versionChild.getUserObject());
                
                Enumeration<FixInfoTreeNode> messageChildren = versionChild.children();
                while (messageChildren.hasMoreElements()) {
                    FixInfoTreeNode messageChild = messageChildren.nextElement();
                    assertNotNull(messageChild.getUserObject());
                    
                    Enumeration<FixInfoTreeNode> itemChildren = messageChild.children();
                    while (itemChildren.hasMoreElements()) {
                        FixInfoTreeNode itemChild = itemChildren.nextElement();
                        assertNotNull(itemChild.getUserObject());
                    }
                }
            }
        }
    }
    
    @Test
    public void testLoadMessages() {
        FixInfoTreeNode versionNode = new FixInfoTreeNode("version");
        DictionaryInfo dictionary = new DictionaryInfo();
        treeView.loadMessages(versionNode, dictionary);
        
        Enumeration<FixInfoTreeNode> messageChildren = versionNode.children();
        while (messageChildren.hasMoreElements()) {
            FixInfoTreeNode messageChild = messageChildren.nextElement();
            assertNotNull(messageChild.getUserObject());
            
            Enumeration<FixInfoTreeNode> itemChildren = messageChild.children();
            while (itemChildren.hasMoreElements()) {
                FixInfoTreeNode itemChild = itemChildren.nextElement();
                assertNotNull(itemChild.getUserObject());
            }
        }
    }
    
    @Test
    public void testLoadItems() {
        FixInfoTreeNode branchNode = new FixInfoTreeNode("branch");
        CompositeFixInfo leafNode = new CompositeFixInfo();
        treeView.loadItems(branchNode, leafNode, 0);
        
        Enumeration<FixInfoTreeNode> itemChildren = branchNode.children();
        while (itemChildren.hasMoreElements()) {
            FixInfoTreeNode itemChild = itemChildren.nextElement();
            assertNotNull(itemChild.getUserObject());
            
            if (itemChild.getUserObject() instanceof CompositeFixInfo) {
                CompositeFixInfo compositeFixInfo = (CompositeFixInfo) itemChild.getUserObject();
                treeView.loadItems(itemChild, compositeFixInfo, 1);
            }
        }
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
        FixInfoTreeNode rootNode = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();
        treeView.search(rootNode, "searchString");
    }
    
    @Test
    public void testClear() {
        FixInfoTreeNode rootNode = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();
        treeView.clear(rootNode);
    }
    
    @Test
    public void testValueChanged() {
        TreeSelectionEvent event = new TreeSelectionEvent(treeView.fixTree, null, false, null, null);
        treeView.valueChanged(event);
    }
    
    @Test
    public void testActionPerformed() {
        ActionEvent event = new ActionEvent(treeView.expandButton, ActionEvent.ACTION_PERFORMED, "command");
        treeView.actionPerformed(event);
    }
    
    @Test
    public void testKeyPressed() {
        KeyEvent event = new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_ENTER);
        treeView.keyPressed(event);
    }
    
    @Test
    public void testKeyReleased() {
        KeyEvent event = new KeyEvent(treeView.searchTextField, KeyEvent.KEY_RELEASED, 0, 0);
        treeView.keyReleased(event);
    }
    
    @Test
    public void testKeyTyped() {
        KeyEvent event = new KeyEvent(treeView.searchTextField, KeyEvent.KEY_TYPED, 0, 0);
        treeView.keyTyped(event);
    }
    
    @Test
    public void testFixInfoTreeNode() {
        Object userObject = new Object();
        FixInfoTreeNode node = new FixInfoTreeNode(userObject);
        
        assertNotNull(node.getUserObject());
        assertFalse(node.isHighlighted());
    }
    
    @Test
    public void testFixInfoCellRenderer() {
        FixInfoTreeNode treeNode = new FixInfoTreeNode(new FieldInfo());
        JTree tree = new JTree();
        Object value = treeNode;
        boolean isSelected = false;
        boolean isExpanded = false;
        boolean isLeaf = false;
        int row = 0;
        boolean hasFocus = false;
        
        DefaultTreeCellRenderer renderer = new TreeView.FixInfoCellRenderer();
        Component component = renderer.getTreeCellRendererComponent(tree, value, isSelected, isExpanded, isLeaf, row, hasFocus);
        
        assertNotNull(component);
        assertTrue(component instanceof JLabel);
    }
}