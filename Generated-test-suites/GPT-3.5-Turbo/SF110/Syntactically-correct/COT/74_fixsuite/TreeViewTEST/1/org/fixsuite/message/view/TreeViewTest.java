import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.FixInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.Library;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;

public class TreeViewTest {

    private TreeView treeView;

    @Before
    public void setup() {
        Library library = new Library();
        treeView = new TreeView(library);
    }

    @Test
    public void testInitGUI() {
        Assert.assertNotNull(treeView.getLayout());

        JScrollPane mainScrollPane = (JScrollPane) treeView.getComponent(0);
        Assert.assertEquals(new Dimension(300, 400), mainScrollPane.getPreferredSize());

        JTree fixTree = (JTree) mainScrollPane.getViewport().getView();
        Assert.assertNotNull(fixTree.getCellRenderer());
    }

    @Test
    public void testLoadFIXTree() {
        FixInfoTreeNode root = treeView.loadFIXTree();

        Assert.assertEquals("Dictionaries", root.getUserObject());

        Enumeration<?> children = root.children();
        while (children.hasMoreElements()) {
            FixInfoTreeNode child = (FixInfoTreeNode) children.nextElement();
            Assert.assertTrue(child.getUserObject() instanceof DictionaryInfo);

            DictionaryInfo dictionary = (DictionaryInfo) child.getUserObject();
            Assert.assertTrue(dictionary.getVersion() instanceof String);
        }
    }

    @Test
    public void testLoadMessages() {
        DictionaryInfo dictionary = new DictionaryInfo();
        FixInfoTreeNode versionNode = new FixInfoTreeNode(dictionary.getVersion());
        treeView.loadMessages(versionNode, dictionary);

        Enumeration<?> children = versionNode.children();
        while (children.hasMoreElements()) {
            FixInfoTreeNode child = (FixInfoTreeNode) children.nextElement();
            Assert.assertTrue(child.getUserObject() instanceof MessageInfo);

            MessageInfo message = (MessageInfo) child.getUserObject();
            Assert.assertTrue(message.getName() instanceof String);
            Assert.assertTrue(message.getMessageType() instanceof String);
        }
    }

    @Test
    public void testLoadItems() {
        CompositeFixInfo leafNode = new CompositeFixInfo();
        FixInfoTreeNode branchNode = new FixInfoTreeNode(leafNode);

        treeView.loadItems(branchNode, leafNode, 0);

        Enumeration<?> children = branchNode.children();
        while (children.hasMoreElements()) {
            FixInfoTreeNode child = (FixInfoTreeNode) children.nextElement();
            Assert.assertTrue(child.getUserObject() instanceof FixInfo);
        }
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();

        JTree fixTree = treeView.getFixTree();
        for (int row = 0; row < fixTree.getRowCount(); row++) {
            Assert.assertTrue(fixTree.isExpanded(row));
        }
    }

    @Test
    public void testCollapseAll() {
        treeView.collapseAll();

        JTree fixTree = treeView.getFixTree();
        for (int row = 0; row < fixTree.getRowCount(); row++) {
            Assert.assertFalse(fixTree.isExpanded(row));
        }
    }

    @Test
    public void testSearch() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.search(root, "test");

        Enumeration<?> enumeration = root.depthFirstEnumeration();
        while (enumeration.hasMoreElements()) {
            FixInfoTreeNode node = (FixInfoTreeNode) enumeration.nextElement();
            if (node.getUserObject() instanceof FixInfo) {
                Assert.assertTrue(node.isHighlighted());
            }
        }
    }

    @Test
    public void testClear() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.clear(root);

        Enumeration<?> enumeration = root.depthFirstEnumeration();
        while (enumeration.hasMoreElements()) {
            FixInfoTreeNode node = (FixInfoTreeNode) enumeration.nextElement();
            Assert.assertFalse(node.isHighlighted());
        }
    }

    @Test
    public void testValueChanged() {
        FixInfoTreeNode node = new FixInfoTreeNode(new FieldInfo());

        TreeSelectionEvent event = new TreeSelectionEvent(new JTree(), new TreePath(node.getPath()), true, node, node);
        treeView.valueChanged(event);

        Assert.assertEquals(node, treeView.getLastSelectedNode());
    }

    @Test
    public void testActionPerformed_collapseButton() {
        JButton collapseButton = treeView.getCollapseButton();
        fireEvent(collapseButton);

        JTree fixTree = treeView.getFixTree();
        for (int row = 0; row < fixTree.getRowCount(); row++) {
            Assert.assertFalse(fixTree.isExpanded(row));
        }
    }

    @Test
    public void testActionPerformed_expandButton() {
        JButton expandButton = treeView.getExpandButton();
        fireEvent(expandButton);

        JTree fixTree = treeView.getFixTree();
        for (int row = 0; row < fixTree.getRowCount(); row++) {
            Assert.assertTrue(fixTree.isExpanded(row));
        }
    }

    @Test
    public void testActionPerformed_searchButton() {
        treeView.getSearchTextField().setText("test");
        JButton searchButton = treeView.getSearchButton();
        fireEvent(searchButton);

        Enumeration<?> enumeration = treeView.loadFIXTree().depthFirstEnumeration();
        while (enumeration.hasMoreElements()) {
            FixInfoTreeNode node = (FixInfoTreeNode) enumeration.nextElement();
            if (node.getUserObject() instanceof FixInfo) {
                Assert.assertTrue(node.isHighlighted());
            }
        }
    }

    @Test
    public void testActionPerformed_clearButton() {
        JButton clearButton = treeView.getClearButton();
        fireEvent(clearButton);

        Enumeration<?> enumeration = treeView.loadFIXTree().depthFirstEnumeration();
        while (enumeration.hasMoreElements()) {
            FixInfoTreeNode node = (FixInfoTreeNode) enumeration.nextElement();
            Assert.assertFalse(node.isHighlighted());
        }
    }

    @Test
    public void testKeyPressed_enterKey() {
        KeyEvent enterEvent = new KeyEvent(treeView, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        treeView.keyPressed(enterEvent);

        JButton searchButton = treeView.getSearchButton();
        Assert.assertTrue(searchButton.getModel().isPressed());
    }

    private void fireEvent(JButton button) {
        ActionListener[] listeners = button.getActionListeners();
        for (ActionListener listener : listeners) {
            ActionEvent event = new ActionEvent(button, ActionEvent.ACTION_PERFORMED, button.getText());
            listener.actionPerformed(event);
        }
    }
}