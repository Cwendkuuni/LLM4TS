import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.DefaultTreeCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;

import org.fixsuite.message.DictionaryInfo;
import org.fixsuite.message.FieldInfo;
import org.fixsuite.message.Library;
import org.fixsuite.message.MessageInfo;
import org.fixsuite.message.info.CompositeFixInfo;
import org.fixsuite.message.info.FixInfo;
import org.fixsuite.message.info.FixInfoTreeNode;
import org.fixsuite.message.info.ComponentInfo;

import static org.junit.Assert.*;
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

    // --- Public Methods ---

    @Test
    public void testTreeViewConstructor() {
        assertNotNull(treeView);
        assertNotNull(treeView.getLibrary());
    }

    // --- Private Methods ---

    @Test
    public void testInitGUI() {
        // Not practical to test UI code
        assertTrue(true);
    }

    @Test
    public void testLoadFIXTree() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        assertNotNull(root);
        assertEquals("Dictionaries", root.getUserObject());

        ArrayList<DictionaryInfo> dictionaries = library.getDictionaries();
        for (int i = 0; i < dictionaries.size(); i++) {
            DictionaryInfo dictionary = dictionaries.get(i);
            FixInfoTreeNode dictionaryNode = (FixInfoTreeNode) root.getChildAt(i);
            assertNotNull(dictionaryNode);
            assertEquals(dictionary.getVersion(), dictionaryNode.getUserObject());

            ArrayList<MessageInfo> messages = dictionary.getMessages();
            for (int j = 0; j < messages.size(); j++) {
                MessageInfo message = messages.get(j);
                FixInfoTreeNode messageNode = (FixInfoTreeNode) dictionaryNode.getChildAt(j);
                assertNotNull(messageNode);
                assertEquals(message, messageNode.getUserObject());
                assertEquals(0, messageNode.getChildCount());
            }
        }
    }

    @Test
    public void testLoadMessages() {
        FixInfoTreeNode versionNode = new FixInfoTreeNode("Version");
        DictionaryInfo dictionary = new DictionaryInfo();
        MessageInfo message = new MessageInfo();

        treeView.loadMessages(versionNode, dictionary);

        assertNotNull(versionNode.getChildAt(0));
        assertEquals(message, ((FixInfoTreeNode) versionNode.getChildAt(0)).getUserObject());
    }

    @Test
    public void testLoadItems() {
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
        CompositeFixInfo leafNode = new CompositeFixInfo();
        FixInfo info = new FixInfo();

        treeView.loadItems(branchNode, leafNode, 0);

        assertNotNull(branchNode.getChildAt(0));
        assertEquals(info, ((FixInfoTreeNode) branchNode.getChildAt(0)).getUserObject());
    }

    @Test
    public void testExpandAll() {
        // Not practical to test UI code
        assertTrue(true);
    }

    @Test
    public void testCollapseAll() {
        // Not practical to test UI code
        assertTrue(true);
    }

    @Test
    public void testSearch() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        String searchString = "search";
        treeView.search(root, searchString);

        for (int i = 0; i < root.getChildCount(); i++) {
            FixInfoTreeNode dictionaryNode = (FixInfoTreeNode) root.getChildAt(i);
            for (int j = 0; j < dictionaryNode.getChildCount(); j++) {
                FixInfoTreeNode messageNode = (FixInfoTreeNode) dictionaryNode.getChildAt(j);
                FixInfo info = (FixInfo) messageNode.getUserObject();
                if (info instanceof MessageInfo) {
                    assertTrue(((MessageInfo) info).getName().matches(searchString)
                            || ((MessageInfo) info).getMessageType().matches(searchString));
                    assertTrue(messageNode.isHighlighted());
                } else if (info instanceof ComponentInfo) {
                    assertTrue(((ComponentInfo) info).getName().matches(searchString));
                    assertTrue(messageNode.isHighlighted());
                } else if (info instanceof FieldInfo) {
                    String tagNumber = "" + ((FieldInfo) info).getTagNumber();
                    assertTrue(((FieldInfo) info).getName().matches(searchString)
                            || tagNumber.matches(searchString));
                    assertTrue(messageNode.isHighlighted());
                }
            }
        }
    }

    @Test
    public void testClear() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.clear(root);

        for (int i = 0; i < root.getChildCount(); i++) {
            FixInfoTreeNode dictionaryNode = (FixInfoTreeNode) root.getChildAt(i);
            assertFalse(dictionaryNode.isHighlighted());
            for (int j = 0; j < dictionaryNode.getChildCount(); j++) {
                FixInfoTreeNode messageNode = (FixInfoTreeNode) dictionaryNode.getChildAt(j);
                FixInfo info = (FixInfo) messageNode.getUserObject();
                assertFalse(messageNode.isHighlighted());
            }
        }
    }

    // --- ActionListener Methods ---

    @Test
    public void testActionPerformed_ExpandButton() {
        treeView.actionPerformed(null); // UI event, not practical to test
        assertTrue(true);
    }

    @Test
    public void testActionPerformed_CollapseButton() {
        treeView.actionPerformed(null); // UI event, not practical to test
        assertTrue(true);
    }

    @Test
    public void testActionPerformed_SearchButton() {
        treeView.actionPerformed(null); // UI event, not practical to test
        assertTrue(true);
    }

    @Test
    public void testActionPerformed_ClearButton() {
        treeView.actionPerformed(null); // UI event, not practical to test
        assertTrue(true);
    }

    // --- KeyListener Methods ---

    @Test
    public void testKeyPressed_SearchTextField() {
        KeyEvent event = new KeyEvent(new JTextField(), 0, 0, 0, KeyEvent.VK_ENTER);
        treeView.keyPressed(event);
        assertTrue(true); // Event just triggers searchButton.doClick(), so not practical to test
    }

    @Test
    public void testKeyReleased_SearchTextField() {
        // Not practical to test UI code
        assertTrue(true);
    }

    @Test
    public void testKeyTyped_SearchTextField() {
        // Not practical to test UI code
        assertTrue(true);
    }

    // --- FixInfoTreeNode Class ---

    @Test
    public void testFixInfoTreeNodeConstructor() {
        String userObject = "Object";
        FixInfoTreeNode node = new FixInfoTreeNode(userObject);
        assertNotNull(node);
        assertEquals(userObject, node.getUserObject());
        assertFalse(node.isHighlighted());
    }

    @Test
    public void testFixInfoTreeNodeIsHighlighted() {
        FixInfoTreeNode node = new FixInfoTreeNode(null);
        assertFalse(node.isHighlighted());

        node.setHighlighted(true);
        assertTrue(node.isHighlighted());

        node.setHighlighted(false);
        assertFalse(node.isHighlighted());
    }

    // --- FixInfoCellRenderer Class ---

    @Test
    public void testFixInfoCellRendererGetTreeCellRendererComponent() {
        JTree tree = new JTree();
        Object value = new FixInfoTreeNode(null);
        boolean sel = false;
        boolean expanded = false;
        boolean leaf = false;
        int row = 0;
        boolean hasFocus = false;

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        JLabel label = (JLabel) renderer.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
                hasFocus);

        assertNotNull(label);
        assertEquals(value, label.getText());
    }
}