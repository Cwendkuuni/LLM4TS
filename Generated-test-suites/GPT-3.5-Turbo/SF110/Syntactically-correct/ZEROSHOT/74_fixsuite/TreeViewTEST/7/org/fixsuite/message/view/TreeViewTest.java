import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;
import org.fixsuite.message.view.TreeView;
import org.fixsuite.message.info.*;
import javax.swing.event.*;
import org.fixsuite.message.*;
import java.awt.event.*;
import javax.swing.tree.*;
import java.awt.*;

public class TreeViewTest {

    @Test
    public void testConstructor() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        assertNotNull(treeView);
        assertNotNull(treeView.mainScrollPane);
        assertNotNull(treeView.fixTree);
        assertNotNull(treeView.searchLabel);
        assertNotNull(treeView.searchTextField);
        assertNotNull(treeView.searchButton);
        assertNotNull(treeView.clearButton);
        assertNotNull(treeView.expandButton);
        assertNotNull(treeView.collapseButton);
        assertEquals(library, treeView.library);
    }

    @Test
    public void testInitGUI() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.initGUI();
        assertNotNull(treeView.getLayout());
        assertNotNull(treeView.mainScrollPane);
        assertNotNull(treeView.fixTree);
        assertNotNull(treeView.searchLabel);
        assertNotNull(treeView.searchTextField);
        assertNotNull(treeView.searchButton);
        assertNotNull(treeView.clearButton);
        assertNotNull(treeView.expandButton);
        assertNotNull(treeView.collapseButton);
        assertNotNull(treeView.getComponent(0));
    }

    @Test
    public void testLoadFIXTree() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode result = treeView.loadFIXTree();
        assertNotNull(result);
        assertNotNull(result.getUserObject());
        assertEquals("Dictionaries", result.getUserObject());
        assertEquals(0, result.getChildCount());
    }

    @Test
    public void testLoadMessages() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode versionNode = new FixInfoTreeNode("2.3");
        DictionaryInfo dictionary = new DictionaryInfo();
        treeView.loadMessages(versionNode, dictionary);
        assertEquals(0, versionNode.getChildCount());
    }

    @Test
    public void testLoadItems() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
        CompositeFixInfo leafNode = new CompositeFixInfo();
        int level = 0;
        treeView.loadItems(branchNode, leafNode, level);
        assertEquals(0, branchNode.getChildCount());
    }

    @Test
    public void testExpandAll() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.expandAll();
    }

    @Test
    public void testCollapseAll() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.collapseAll();
    }

    @Test
    public void testSearch() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode root = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();
        treeView.search(root, "searchString");
    }

    @Test
    public void testClear() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode root = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();
        treeView.clear(root);
    }

    @Test
    public void testValueChanged() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        TreeSelectionEvent event = new TreeSelectionEvent(treeView.fixTree, null, true, null, null);
        treeView.valueChanged(event);
    }

    @Test
    public void testActionPerformed() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        ActionEvent event = new ActionEvent(new JButton(), 1, null);
        treeView.actionPerformed(event);
    }

    @Test
    public void testKeyPressed() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        KeyEvent event = new KeyEvent(new JButton(), 1, 1, 1, 1);
        treeView.keyPressed(event);
    }

}