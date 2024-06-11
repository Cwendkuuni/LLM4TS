import org.junit.*;
import static org.junit.Assert.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.*;

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
    }

    @Test
    public void testLoadFIXTree() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        assertNotNull(root);
        assertEquals("Dictionaries", root.getUserObject());
        assertEquals(3, root.getChildCount());

        FixInfoTreeNode dictionaryNode = (FixInfoTreeNode) root.getChildAt(0);
        assertNotNull(dictionaryNode.getUserObject());
        assertTrue(dictionaryNode.getUserObject() instanceof DictionaryInfo);

        FixInfoTreeNode messageNode = (FixInfoTreeNode) dictionaryNode.getChildAt(0);
        assertNotNull(messageNode.getUserObject());
        assertTrue(messageNode.getUserObject() instanceof MessageInfo);

        FixInfoTreeNode fieldNode = (FixInfoTreeNode) messageNode.getChildAt(0);
        assertNotNull(fieldNode.getUserObject());
        assertTrue(fieldNode.getUserObject() instanceof FieldInfo);
    }

    @Test
    public void testLoadMessages() {
        FixInfoTreeNode versionNode = new FixInfoTreeNode("FIX.4.4");
        DictionaryInfo dictionary = new DictionaryInfo();
        treeView.loadMessages(versionNode, dictionary);
        assertEquals(0, versionNode.getChildCount());

        MessageInfo message = new MessageInfo();
        dictionary.getMessages().add(message);
        treeView.loadMessages(versionNode, dictionary);
        assertEquals(1, versionNode.getChildCount());

        FixInfoTreeNode messageNode = (FixInfoTreeNode) versionNode.getChildAt(0);
        assertNotNull(messageNode.getUserObject());
        assertEquals(message, messageNode.getUserObject());
    }

    @Test
    public void testLoadItems() {
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
        CompositeFixInfo leafNode = new CompositeFixInfo();
        treeView.loadItems(branchNode, leafNode, 0);
        assertEquals(0, branchNode.getChildCount());

        FixInfo fixInfo = new FixInfo();
        leafNode.getItems().add(fixInfo);
        treeView.loadItems(branchNode, leafNode, 0);
        assertEquals(1, branchNode.getChildCount());

        FixInfoTreeNode fixInfoNode = (FixInfoTreeNode) branchNode.getChildAt(0);
        assertNotNull(fixInfoNode.getUserObject());
        assertEquals(fixInfo, fixInfoNode.getUserObject());
    }

    @Test
    public void testExpandAll() {
        treeView.expandAll();
        assertEquals(treeView.fixTree.getRowCount(), treeView.fixTree.getExpandedRowCount());
    }

    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        assertEquals(0, treeView.fixTree.getExpandedRowCount());
    }

    @Test
    public void testSearch() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.search(root, "Message");
        assertTrue(treeView.fixTree.isExpanded(new TreePath(root.getPath())));

        treeView.clear(root);
        treeView.search(root, "(?i)field\\d+");
        assertFalse(treeView.fixTree.isExpanded(new TreePath(root.getPath())));
    }

    @Test
    public void testClear() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.search(root, "Message");
        treeView.clear(root);
        assertFalse(treeView.fixTree.isExpanded(new TreePath(root.getPath())));
    }

    @Test
    public void testValueChanged() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.fixTree.setSelectionRow(0);
        assertEquals(null, LibrarySuite.getInstance().getDetailsPanel().getDisplay());

        treeView.fixTree.setSelectionRow(2);
        assertNotNull(LibrarySuite.getInstance().getDetailsPanel().getDisplay());
    }

    @Test
    public void testActionPerformed() {
        ActionEvent collapseButtonEvent = new ActionEvent(treeView.collapseButton, ActionEvent.ACTION_PERFORMED, "");
        treeView.actionPerformed(collapseButtonEvent);
        verifyCollapseAll();

        ActionEvent expandButtonEvent = new ActionEvent(treeView.expandButton, ActionEvent.ACTION_PERFORMED, "");
        treeView.actionPerformed(expandButtonEvent);
        verifyExpandAll();

        ActionEvent searchButtonEvent = new ActionEvent(treeView.searchButton, ActionEvent.ACTION_PERFORMED, "");
        treeView.searchTextField.setText("Message");
        treeView.actionPerformed(searchButtonEvent);
        verifySearch();

        ActionEvent clearButtonEvent = new ActionEvent(treeView.clearButton, ActionEvent.ACTION_PERFORMED, "");
        treeView.actionPerformed(clearButtonEvent);
        verifyClear();
    }

    @Test
    public void testKeyPressed() {
        KeyEvent enterKeyEvent = new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER);
        treeView.keyPressed(enterKeyEvent);
        verifySearch();
    }

    @Test
    public void testKeyReleased() {
        KeyEvent keyEvent = new KeyEvent(treeView.searchTextField, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_A);
        treeView.keyReleased(keyEvent);
        // No action to be tested
    }

    @Test
    public void testKeyTyped() {
        KeyEvent keyEvent = new KeyEvent(treeView.searchTextField, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_A);
        treeView.keyTyped(keyEvent);
        // No action to be tested
    }

    private void verifyCollapseAll() {
        assertFalse(treeView.fixTree.isExpanded(new TreePath(treeView.fixTree.getModel().getRoot())));
    }

    private void verifyExpandAll() {
        assertTrue(treeView.fixTree.isExpanded(new TreePath(treeView.fixTree.getModel().getRoot())));
    }

    private void verifySearch() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        assertTrue(treeView.fixTree.isExpanded(new TreePath(root.getPath())));
    }

    private void verifyClear() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        assertFalse(treeView.fixTree.isExpanded(new TreePath(root.getPath())));
    }
}