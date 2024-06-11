import org.fixsuite.message.view.*;
import org.fixsuite.message.info.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import junit.framework.TestCase;

public class TreeViewTest extends TestCase {

    private TreeView treeView;
    private Library library;

    protected void setUp() {
        library = new Library();
        treeView = new TreeView(library);
    }

    protected void tearDown() {
        library = null;
        treeView = null;
    }

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

        assertTrue(treeView.getLayout() instanceof BorderLayout);
        assertTrue(treeView.getMainScrollPane().getPreferredSize().equals(new Dimension(300, 400)));

        assertNotNull(treeView.getFixTree().getCellRenderer());
        assertNotNull(treeView.getFixTree().getSelectionModel());
        assertNotNull(treeView.getFixTree().getModel());

        assertEquals(FixInfoCellRenderer.class, treeView.getFixTree().getCellRenderer().getClass());
    }

    public void testLoadFIXTree() {
        FixInfoTreeNode root = treeView.loadFIXTree();

        assertNotNull(root);
        assertEquals("Dictionaries", root.getUserObject());

        Enumeration<FixInfoTreeNode> rootChildren = root.children();
        while(rootChildren.hasMoreElements()) {
            FixInfoTreeNode child = rootChildren.nextElement();
            assertNotNull(child.getUserObject());

            if(child.getUserObject() instanceof String) {
                DictionaryInfo dummyDictionary = new DictionaryInfo();
                dummyDictionary.setVersion((String)child.getUserObject());

                TreeViewTestHelper.loadMessages(child, dummyDictionary);

                assertNotNull(child.children());
            }
        }
    }

    public void testLoadMessages() {
        DictionaryInfo dictionary = new DictionaryInfo();
        FixInfoTreeNode versionNode = new FixInfoTreeNode("Version");
        
        treeView.loadMessages(versionNode, dictionary);
        
        assertNotNull(versionNode.children());
        assertTrue(versionNode.children().hasMoreElements());

        FixInfoTreeNode child = versionNode.children().nextElement();
        assertNotNull(child);
        assertNotNull(child.getUserObject());
        assertEquals(MessageInfo.class, child.getUserObject().getClass());

        assertEquals(0, child.getChildCount());
    }
    
    public void testLoadItems() {
        CompositeFixInfo leafNode = new MessageInfo();
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
        int level = 0;

        TreeViewTestHelper.loadItems(treeView, branchNode, leafNode, level);

        assertNotNull(branchNode.children());
        assertTrue(branchNode.children().hasMoreElements());

        FixInfoTreeNode child = branchNode.children().nextElement();
        assertNotNull(child);
        assertNotNull(child.getUserObject());
        assertEquals(FixInfo.class, child.getUserObject().getClass());

        if(child.getUserObject() instanceof CompositeFixInfo) {
        	assertTrue(child.getChildCount() < 5);
        }
    }

    public void testExpandAll() {
        treeView.loadFIXTree();

        treeView.expandAll();

        for(int i = 0; i < treeView.getFixTree().getRowCount(); i++) {
            assertTrue(treeView.getFixTree().isExpanded(i));
        }
    }

    public void testCollapseAll() {
        treeView.loadFIXTree();

        treeView.collapseAll();

        for(int i = 0; i < treeView.getFixTree().getRowCount(); i++) {
            assertFalse(treeView.getFixTree().isExpanded(i));
        }
    }

    public void testSearch() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.collapseAll();

        treeView.search(root, "SomeSearchString");

        Enumeration<?> enumeration = root.preorderEnumeration();
        while(enumeration.hasMoreElements()) {
            FixInfoTreeNode node = (FixInfoTreeNode) enumeration.nextElement();
            if(node.getUserObject() instanceof FixInfo) {
                assertTrue(node.isHighlighted());
            }
        }
    }

    public void testClear() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.collapseAll();
        treeView.search(root, "SomeSearchString");

        treeView.clear(root);

        Enumeration<?> enumeration = root.preorderEnumeration();
        while(enumeration.hasMoreElements()) {
            FixInfoTreeNode node = (FixInfoTreeNode) enumeration.nextElement();
            assertFalse(node.isHighlighted());
        }
    }

    public void testValueChanged() {
        FixInfoTreeNode node = new FixInfoTreeNode(new FieldInfo());
        treeView.getFixTree().setLastSelectedPathComponent(node);
        treeView.valueChanged(null);

        // test for specific behavior
    }

    public void testActionPerformed() {
        ActionEvent event = new ActionEvent(treeView.getClearButton(), 0, "");
        treeView.actionPerformed(event);

        ActionEvent event2 = new ActionEvent(treeView.getExpandButton(), 0, "");
        treeView.actionPerformed(event2);

        ActionEvent event3 = new ActionEvent(treeView.getSearchButton(), 0, "");
        treeView.actionPerformed(event3);

        ActionEvent event4 = new ActionEvent(treeView.getClearButton(), 0, "");
        treeView.actionPerformed(event4);

        // test for specific behavior
    }

    public void testkeyPressed() {
        KeyEvent event = new KeyEvent(new JLabel(), 0, 0, 10, 0); // Enter key

        treeView.keyPressed(event);

        // test for specific behavior
    }

    public void testkeyReleased() {
        KeyEvent event = new KeyEvent(new JLabel(), 0, 0, 10, 0); // Enter key

        treeView.keyReleased(event);

        // test for specific behavior
    }

    public void testkeyTyped() {
        KeyEvent event = new KeyEvent(new JLabel(), 0, 0, 10, 0); // Enter key

        treeView.keyTyped(event);

        // test for specific behavior
    }

    private class TreeViewTestHelper {
        static void loadMessages(FixInfoTreeNode versionNode, DictionaryInfo dictionary) {
            MessageInfo message = new MessageInfo();
            dictionary.addMessage(message);

            FixInfoTreeNode node = new FixInfoTreeNode(message);
            versionNode.add(node);
        }
        
        static void loadItems(TreeView treeView, FixInfoTreeNode branchNode, CompositeFixInfo leafNode, int level) {
        	level++;
        	FieldInfo field = new FieldInfo();
        	
            if (leafNode.getItems().size() > 0) {
                FixInfoTreeNode node = new FixInfoTreeNode(field);

                if (field instanceof CompositeFixInfo && level < 5) {
                    loadItems(treeView, node, (CompositeFixInfo)field, level);
                }
                branchNode.add(node);
            }
        }
    }
}