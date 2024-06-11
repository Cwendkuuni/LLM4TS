package org.fixsuite.message.view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.FixInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.Library;
import org.fixsuite.message.LibrarySuite;
import org.junit.Before;
import org.junit.Test;

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
        assertEquals("Dictionaries", root.getUserObject());
        assertEquals(0, root.getChildCount());

        DictionaryInfo dictionary = new DictionaryInfo();
        dictionary.setVersion("FIX.4.4");
        dictionary.setMessages(new ArrayList<>());
        library.getDictionaries().add(dictionary);

        root = treeView.loadFIXTree();
        assertNotNull(root);
        assertEquals("Dictionaries", root.getUserObject());
        assertEquals(1, root.getChildCount());

        FixInfoTreeNode node = (FixInfoTreeNode) root.getChildAt(0);
        assertNotNull(node);
        assertEquals("FIX.4.4", node.getUserObject());
        assertEquals(0, node.getChildCount());

        MessageInfo message = new MessageInfo();
        message.setName("Message");
        message.setMessageType("MT");
        dictionary.getMessages().add(message);

        root = treeView.loadFIXTree();
        assertNotNull(root);
        assertEquals("Dictionaries", root.getUserObject());
        assertEquals(1, root.getChildCount());

        node = (FixInfoTreeNode) root.getChildAt(0);
        assertNotNull(node);
        assertEquals("FIX.4.4", node.getUserObject());
        assertEquals(1, node.getChildCount());

        FixInfoTreeNode childNode = (FixInfoTreeNode) node.getChildAt(0);
        assertNotNull(childNode);
        assertEquals(message, childNode.getUserObject());
        assertEquals(0, childNode.getChildCount());
    }

    @Test
    public void testLoadMessages() {
        DictionaryInfo dictionary = new DictionaryInfo();
        treeView.loadMessages(null, dictionary);
        assertEquals(0, library.getDictionaries().size());

        FixInfoTreeNode versionNode = new FixInfoTreeNode("FIX.4.4");
        treeView.loadMessages(versionNode, null);
        assertEquals(0, versionNode.getChildCount());

        dictionary.setMessages(new ArrayList<>());
        treeView.loadMessages(versionNode, dictionary);
        assertEquals(0, versionNode.getChildCount());

        MessageInfo message = new MessageInfo();
        message.setName("Message");
        message.setMessageType("MT");
        dictionary.getMessages().add(message);

        treeView.loadMessages(versionNode, dictionary);
        assertEquals(1, versionNode.getChildCount());

        FixInfoTreeNode childNode = (FixInfoTreeNode) versionNode.getChildAt(0);
        assertNotNull(childNode);
        assertEquals(message, childNode.getUserObject());
        assertEquals(0, childNode.getChildCount());
    }

    @Test
    public void testLoadItems() {
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
        CompositeFixInfo leafNode = new CompositeFixInfo();
        treeView.loadItems(branchNode, leafNode, 0);
        assertEquals(0, branchNode.getChildCount());

        FixInfo info = new FixInfo();
        leafNode.getItems().add(info);

        treeView.loadItems(branchNode, leafNode, 0);
        assertEquals(1, branchNode.getChildCount());

        FixInfoTreeNode childNode = (FixInfoTreeNode) branchNode.getChildAt(0);
        assertNotNull(childNode);
        assertEquals(info, childNode.getUserObject());
        assertEquals(0, childNode.getChildCount());
    }

    @Test
    public void testExpandAll() {
        assertTrue(treeView.fixTree.getRowCount() > 0);
        treeView.expandAll();
        for (int row = 0; row < treeView.fixTree.getRowCount(); row++) {
            assertTrue(treeView.fixTree.isExpanded(row));
        }
    }

    @Test
    public void testCollapseAll() {
        assertTrue(treeView.fixTree.getRowCount() > 0);
        treeView.collapseAll();
        for (int row = 0; row < treeView.fixTree.getRowCount(); row++) {
            assertFalse(treeView.fixTree.isExpanded(row));
        }
    }

    @Test
    public void testSearch() {
        FixInfoTreeNode node = new FixInfoTreeNode("Node");
        treeView.search(node, "searchString");
        assertFalse(node.isHighlighted());

        FixInfoTreeNode childNode = new FixInfoTreeNode("ChildNode");
        node.add(childNode);
        treeView.search(childNode, "searchString");
        assertTrue(childNode.isHighlighted());

        treeView.search(childNode, "Child");
        assertFalse(childNode.isHighlighted());
    }

    @Test
    public void testClear() {
        FixInfoTreeNode node = new FixInfoTreeNode("Node");
        treeView.clear(node);
        assertFalse(node.isHighlighted());

        FixInfoTreeNode childNode = new FixInfoTreeNode("ChildNode");
        node.add(childNode);
        childNode.setHighlighted(true);
        treeView.clear(node);
        assertFalse(childNode.isHighlighted());
    }

    // Add more test cases for the remaining methods

}