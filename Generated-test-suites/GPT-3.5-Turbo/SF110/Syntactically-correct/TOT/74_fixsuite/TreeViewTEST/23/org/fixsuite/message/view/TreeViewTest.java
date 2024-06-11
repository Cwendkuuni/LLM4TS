import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FixInfo;
import org.fixsuite.message.info.FixInfoTreeNode;
import org.fixsuite.message.view.TreeView;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TreeViewTest {

    private TreeView treeView;
    private FixInfoTreeNode root;

    @Before
    public void setUp() {
        DictionaryInfo dictionary1 = new DictionaryInfo("FIX.4.2");
        DictionaryInfo dictionary2 = new DictionaryInfo("FIX.4.4");
        List<DictionaryInfo> dictionaries = new ArrayList<>();
        dictionaries.add(dictionary1);
        dictionaries.add(dictionary2);
        root = new FixInfoTreeNode("Dictionaries");

        for (DictionaryInfo dictionary : dictionaries) {
            FixInfoTreeNode node = new FixInfoTreeNode(dictionary.getVersion());
            root.add(node);
        }

        treeView = new TreeView(root);
    }

    @Test
    public void testLoadFIXTree() {
        FixInfoTreeNode fixTree = treeView.loadFIXTree();
        assertNotNull(fixTree);
        assertEquals(root, fixTree);
        assertEquals(2, fixTree.getChildCount());
    }

    @Test
    public void testLoadMessages() {
        FixInfoTreeNode fixTree = treeView.loadFIXTree();
        FixInfoTreeNode versionNode = (FixInfoTreeNode) fixTree.getChildAt(0);
        treeView.loadMessages(versionNode, new DictionaryInfo("FIX.4.2"));

        assertEquals(1, versionNode.getChildCount());
    }

    @Test
    public void testLoadItems() {
        FixInfoTreeNode fixTree = treeView.loadFIXTree();
        FixInfoTreeNode versionNode = (FixInfoTreeNode) fixTree.getChildAt(0);
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Message");
        treeView.loadItems(branchNode, new FixInfo(), 0);

        assertEquals(1, branchNode.getChildCount());
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();
        for (int row = 0; row < treeView.fixTree.getRowCount(); row++) {
            assertTrue(treeView.fixTree.isExpanded(row));
        }
    }

    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        for (int row = 0; row < treeView.fixTree.getRowCount(); row++) {
            assertFalse(treeView.fixTree.isExpanded(row));
        }
    }

    @Test
    public void testSearch() {
        treeView.search(root, "FIX.4.2");
        assertTrue(treeView.fixTree.isExpanded(new TreePath(root.getPath())));
    }

    @Test
    public void testClear() {
        treeView.search(root, "FIX.4.2");
        treeView.clear(root);
        assertFalse(root.isHighlighted());
    }

    @Test
    public void testActionPerformed() {
        JButton collapseButton = new JButton();
        JButton expandButton = new JButton();
        JButton searchButton = new JButton();
        JButton clearButton = new JButton();
        treeView.collapseButton = collapseButton;
        treeView.expandButton = expandButton;
        treeView.searchButton = searchButton;
        treeView.clearButton = clearButton;

        collapseButton.doClick();
        assertFalse(treeView.fixTree.isExpanded(new TreePath(root.getPath())));

        expandButton.doClick();
        assertTrue(treeView.fixTree.isExpanded(new TreePath(root.getPath())));

        searchButton.doClick();
        assertFalse(root.isHighlighted());

        clearButton.doClick();
        assertFalse(root.isHighlighted());
    }

    @Test
    public void testKeyPressed() {
        KeyEvent event = new KeyEvent(treeView.fixTree, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, '\n');
        treeView.keyPressed(event);
        assertFalse(root.isHighlighted());
    }

    @Test
    public void testGetTreeCellRendererComponent() {
        JTree tree = new JTree();
        FixInfoTreeNode node = new FixInfoTreeNode(new FixInfo());
        treeView.fixTree = tree;

        JLabel label = (JLabel)treeView.new FixInfoCellRenderer().getTreeCellRendererComponent(tree, node, false, false, true, 0, false);
        assertNotNull(label);
    }

    @Test
    public void testFixInfoTreeNode() {
        FixInfoTreeNode node = new FixInfoTreeNode("Node");
        assertNotNull(node);
        assertFalse(node.isHighlighted());
    }

    @Test
    public void testFixInfoTreeNodeIsHighlighted() {
        FixInfoTreeNode node = new FixInfoTreeNode("Node");
        assertFalse(node.isHighlighted());
        node.setHighlighted(true);
        assertTrue(node.isHighlighted());
    }

}