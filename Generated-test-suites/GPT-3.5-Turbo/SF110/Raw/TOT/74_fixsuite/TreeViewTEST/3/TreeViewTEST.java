import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import static org.junit.Assert.*;

public class TreeViewTest {

    private TreeView treeView;

    @Before
    public void setup() {
        Library library = new Library();
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

        assertNotNull(treeView.searchTextField.getToolTipText());
        assertNotNull(treeView.searchButton.getToolTipText());

        assertTrue(treeView.mainScrollPane.getPreferredSize().equals(new Dimension(300, 400)));

        assertTrue(treeView.clearButton.getActionListeners().length > 0);
        assertTrue(treeView.expandButton.getActionListeners().length > 0);
        assertTrue(treeView.collapseButton.getActionListeners().length > 0);
    }

    @Test
    public void testLoadFIXTree() {
        FixInfoTreeNode fixInfoTreeNode = treeView.loadFIXTree();

        assertNotNull(fixInfoTreeNode);
        assertNotNull(fixInfoTreeNode.getUserObject());
        assertTrue(fixInfoTreeNode.getUserObject().equals("Dictionaries"));

        assertNotNull(treeView.library.getDictionaries());
        assertFalse(treeView.library.getDictionaries().isEmpty());

        for (DictionaryInfo dictionary : treeView.library.getDictionaries()) {
            FixInfoTreeNode dictionaryNode = new FixInfoTreeNode(dictionary.getVersion());

            assertNotNull(dictionaryNode);
            assertNotNull(dictionaryNode.getUserObject());
            assertTrue(dictionaryNode.getUserObject().equals(dictionary.getVersion()));

            assertNotNull(dictionary.getMessages());
            assertFalse(dictionary.getMessages().isEmpty());

            for (MessageInfo message : dictionary.getMessages()) {
                FixInfoTreeNode messageNode = new FixInfoTreeNode(message);

                assertNotNull(messageNode);
                assertNotNull(messageNode.getUserObject());
                assertTrue(messageNode.getUserObject().equals(message));

                treeView.loadItems(messageNode, message, 0);

                assertTrue(messageNode.getChildCount() > 0);
                assertTrue(messageNode.getChildAt(0) instanceof FixInfoTreeNode);

                dictionaryNode.add(messageNode);
            }

            fixInfoTreeNode.add(dictionaryNode);
        }

        assertTrue(fixInfoTreeNode.getChildCount() > 0);
        assertTrue(fixInfoTreeNode.getChildAt(0) instanceof FixInfoTreeNode);
    }

    @Test
    public void testLoadMessages() {
        FixInfoTreeNode versionNode = new FixInfoTreeNode("Version");
        DictionaryInfo dictionary = new DictionaryInfo();
        dictionary.setVersion("Version");

        treeView.loadMessages(versionNode, dictionary);

        assertNotNull(versionNode);
        assertNotNull(versionNode.getUserObject());
        assertTrue(versionNode.getUserObject().equals("Version"));

        assertNotNull(dictionary.getMessages());
        assertFalse(dictionary.getMessages().isEmpty());

        for (MessageInfo message : dictionary.getMessages()) {
            FixInfoTreeNode messageNode = new FixInfoTreeNode(message);

            assertNotNull(messageNode);
            assertNotNull(messageNode.getUserObject());
            assertTrue(messageNode.getUserObject().equals(message));

            treeView.loadItems(messageNode, message, 0);

            assertTrue(messageNode.getChildCount() > 0);
            assertTrue(messageNode.getChildAt(0) instanceof FixInfoTreeNode);

            versionNode.add(messageNode);
        }

        assertTrue(versionNode.getChildCount() > 0);
        assertTrue(versionNode.getChildAt(0) instanceof FixInfoTreeNode);
    }

    @Test
    public void testLoadItems() {
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
        CompositeFixInfo leafNode = new CompositeFixInfo();
        leafNode.setItems(new ArrayList<>());

        treeView.loadItems(branchNode, leafNode, 0);

        assertNotNull(branchNode);
        assertNotNull(branchNode.getUserObject());
        assertTrue(branchNode.getUserObject().equals("Branch"));

        assertNotNull(leafNode.getItems());
        assertFalse(leafNode.getItems().isEmpty());

        int level = 1;
        for (FixInfo info : leafNode.getItems()) {
            FixInfoTreeNode node = new FixInfoTreeNode(info);

            assertNotNull(node);
            assertNotNull(node.getUserObject());
            assertTrue(node.getUserObject().equals(info));

            if (info instanceof CompositeFixInfo && level < 5) {
                treeView.loadItems(node, (CompositeFixInfo) info, level + 1);
            }

            branchNode.add(node);
        }

        assertTrue(branchNode.getChildCount() > 0);
        assertTrue(branchNode.getChildAt(0) instanceof FixInfoTreeNode);
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();

        int rowCount = treeView.fixTree.getRowCount();

        for (int row = 0; row < rowCount; row++) {
            assertTrue(treeView.fixTree.isExpanded(row));
        }
    }

    @Test
    public void testCollapseAll() {
        treeView.collapseAll();

        int rowCount = treeView.fixTree.getRowCount();

        for (int row = 0; row < rowCount; row++) {
            assertFalse(treeView.fixTree.isExpanded(row));
        }
    }

    @Test
    public void testSearch() {
        FixInfoTreeNode rootNode = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();
        String searchString = "SearchString";

        treeView.clear(rootNode);
        treeView.search(rootNode, searchString);

        int count = 0;
        while (count < rootNode.getChildCount()) {
            FixInfoTreeNode childNode = (FixInfoTreeNode) rootNode.getChildAt(count);

            treeView.search(childNode, searchString);

            assertTrue(childNode.isHighlighted());

            count++;
        }
    }

    @Test
    public void testClear() {
        FixInfoTreeNode rootNode = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();

        treeView.clear(rootNode);

        int count = 0;
        while (count < rootNode.getChildCount()) {
            FixInfoTreeNode childNode = (FixInfoTreeNode) rootNode.getChildAt(count);

            assertFalse(childNode.isHighlighted());

            count++;
        }
    }

    @Test
    public void testValueChange() {
        treeView.fixTree.setSelectionPath(new TreePath(treeView.fixTree.getPathForRow(0)));

        treeView.valueChanged(null);

        /*
        Implement Assertions according to the functionality of valueChanged method and its dependencies on other classes.
        */
    }

    @Test
    public void testActionPerformed() {
        ActionEvent collapseButtonEvent = new ActionEvent(treeView.collapseButton, ActionEvent.ACTION_PERFORMED, "CollapseButton");
        ActionEvent expandButtonEvent = new ActionEvent(treeView.expandButton, ActionEvent.ACTION_PERFORMED, "ExpandButton");
        ActionEvent searchButtonEvent = new ActionEvent(treeView.searchButton, ActionEvent.ACTION_PERFORMED, "SearchButton");
        ActionEvent clearButtonEvent = new ActionEvent(treeView.clearButton, ActionEvent.ACTION_PERFORMED, "ClearButton");

        treeView.actionPerformed(collapseButtonEvent);
        treeView.actionPerformed(expandButtonEvent);
        treeView.actionPerformed(searchButtonEvent);
        treeView.actionPerformed(clearButtonEvent);

        /*
        Implement Assertions according to the functionality of actionPerformed method and its dependencies on other classes.
        */
    }

    @Test
    public void testKeyPressed() {
        KeyEvent enterKeyEvent = new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);

        treeView.keyPressed(enterKeyEvent);

        /*
        Implement Assertions according to the functionality of keyPressed method and its dependencies on other classes.
        */
    }

    @Test
    public void testGetTreeCellRendererComponent() {
        JTree tree = new JTree();
        Object value = "Value";
        boolean sel = true;
        boolean expanded = true;
        boolean leaf = true;
        int row = 0;
        boolean hasFocus = true;

        Component component = treeView.fixTreeCellRenderer.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        assertNotNull(component);

        /*
        Implement Assertions according to the functionality of getTreeCellRendererComponent method and its dependencies on other classes.
        */
    }
}