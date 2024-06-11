import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import org.fixsuite.message.info.*;
import javax.swing.event.*;
import org.fixsuite.message.*;
import java.awt.event.*;
import javax.swing.tree.*;
import java.awt.*;

public class TreeViewTest {

    private TreeView treeView;
    private Library library;

    @Before
    public void setup() {
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
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        assertNotNull(rootNode);
        assertEquals("Dictionaries", rootNode.getUserObject());

        int dictionaryCount = library.getDictionaries().size();
        assertEquals(dictionaryCount, rootNode.getChildCount());

        for (int i = 0; i < dictionaryCount; i++) {
            FixInfoTreeNode dictionaryNode = (FixInfoTreeNode) rootNode.getChildAt(i);
            DictionaryInfo dictionary = library.getDictionaries().get(i);
            assertEquals(dictionary.getVersion(), dictionaryNode.getUserObject());

            int messageCount = dictionary.getMessages().size();
            assertEquals(messageCount, dictionaryNode.getChildCount());

            for (int j = 0; j < messageCount; j++) {
                FixInfoTreeNode messageNode = (FixInfoTreeNode) dictionaryNode.getChildAt(j);
                MessageInfo message = dictionary.getMessages().get(j);
                assertEquals(message, messageNode.getUserObject());
            }
        }
    }

    @Test
    public void testLoadItems() {
        FixInfoTreeNode branchNode = new FixInfoTreeNode("BranchNode");
        CompositeFixInfo leafNode = new CompositeFixInfo();
        leafNode.getItems().add(new FixInfo());

        treeView.loadItems(branchNode, leafNode, 0);
        assertEquals(1, branchNode.getChildCount());
    }

    @Test
    public void testExpandAll() {
        FixInfoTreeNode rootNode = treeView.loadFIXTree();

        int rowCountBefore = treeView.fixTree.getRowCount();
        treeView.expandAll();
        int rowCountAfter = treeView.fixTree.getRowCount();

        assertTrue(rowCountAfter > rowCountBefore);
    }

    @Test
    public void testCollapseAll() {
        FixInfoTreeNode rootNode = treeView.loadFIXTree();
        treeView.expandAll();

        int rowCountBefore = treeView.fixTree.getRowCount();
        treeView.collapseAll();
        int rowCountAfter = treeView.fixTree.getRowCount();

        assertTrue(rowCountAfter < rowCountBefore);
    }

    @Test
    public void testSearch() {
        FixInfoTreeNode rootNode = treeView.loadFIXTree();

        FixInfoTreeNode node = new FixInfoTreeNode("Node");
        node.setHighlighted(true);
        rootNode.add(node);

        treeView.search(node, "SearchString");
        assertFalse(node.isHighlighted());

        node = new FixInfoTreeNode("Node");
        node.setHighlighted(true);
        FixInfoTreeNode childNode = new FixInfoTreeNode("ChildNode");
        node.add(childNode);
        rootNode.add(node);

        treeView.search(node, "SearchString");
        assertFalse(node.isHighlighted());
        assertFalse(childNode.isHighlighted());

        node = new FixInfoTreeNode(new MessageInfo("Message", "MessageType"));
        node.setHighlighted(true);
        rootNode.add(node);

        treeView.search(node, "Message");
        assertTrue(node.isHighlighted());
    }

    @Test
    public void testClear() {
        FixInfoTreeNode rootNode = treeView.loadFIXTree();

        FixInfoTreeNode node = new FixInfoTreeNode("Node");
        node.setHighlighted(true);
        rootNode.add(node);

        treeView.clear(node);
        assertFalse(node.isHighlighted());

        node = new FixInfoTreeNode("Node");
        node.setHighlighted(true);
        FixInfoTreeNode childNode = new FixInfoTreeNode("ChildNode");
        childNode.setHighlighted(true);
        node.add(childNode);
        rootNode.add(node);

        treeView.clear(node);
        assertFalse(node.isHighlighted());
        assertFalse(childNode.isHighlighted());
    }

    @Test
    public void testValueChanged() {
        FixInfoTreeNode node = new FixInfoTreeNode("Node");
        treeView.fixTree.setSelectionPath(new TreePath(node.getPath()));
        treeView.valueChanged(null);

        // Not asserting anything since it just invokes methods from another class
    }

    @Test
    public void testActionPerformed() {
        treeView.collapseButton.doClick();
        treeView.expandButton.doClick();
        treeView.searchButton.doClick();
        treeView.clearButton.doClick();

        // Not asserting anything since it just invokes methods from another class
    }

    @Test
    public void testKeyPressed() {
        KeyEvent event = new KeyEvent(treeView, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER);
        treeView.keyPressed(event);

        // Not asserting anything since it just invokes methods from another class
    }
}