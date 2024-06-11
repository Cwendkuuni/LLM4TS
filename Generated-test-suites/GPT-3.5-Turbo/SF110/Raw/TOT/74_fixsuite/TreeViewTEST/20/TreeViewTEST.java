import org.junit.Before;
import org.junit.Test;
import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.FixInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.view.TreeView;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

public class TreeViewTest {

    private TreeView treeView;
    private DefaultMutableTreeNode root;

    @Before
    public void setup() {
        JFrame frame = new JFrame();
        Library library = new Library();
        dictionary = new DictionaryInfo("Version1");
        message = new MessageInfo("Message1");
        component = new ComponentInfo("Component1");
        field = new FieldInfo("Field1", 1);
        message.addField(field);
        component.addComponent(message);
        dictionary.addMessage(message);
        library.addDictionary(dictionary);
        treeView = new TreeView(library);

        try {
            SwingUtilities.invokeAndWait(() -> frame.add(treeView));
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
        }

        root = (DefaultMutableTreeNode) treeView.fixTree.getModel().getRoot();
    }

    @Test
    public void testLoadFIXTree() {
        assertNotNull(root);
        assertEquals("Dictionaries", root.getUserObject());
        assertEquals(1, root.getChildCount());

        DefaultMutableTreeNode dictionaryNode = (DefaultMutableTreeNode) root.getChildAt(0);
        assertEquals("Version1", dictionaryNode.getUserObject());

        DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) dictionaryNode.getChildAt(0);
        assertEquals("Message1", messageNode.getUserObject());

        DefaultMutableTreeNode componentNode = (DefaultMutableTreeNode) messageNode.getChildAt(0);
        assertEquals("Component1", componentNode.getUserObject());

        DefaultMutableTreeNode fieldNode = (DefaultMutableTreeNode) componentNode.getChildAt(0);
        assertEquals("Field1 (1)", fieldNode.getUserObject());
    }

    @Test
    public void testLoadMessages() {
        DefaultMutableTreeNode versionNode = new DefaultMutableTreeNode("Version");
        TreeView.loadMessages(versionNode, dictionary);

        assertEquals(1, versionNode.getChildCount());

        DefaultMutableTreeNode messageNode = (DefaultMutableTreeNode) versionNode.getChildAt(0);
        assertEquals("Message1", messageNode.getUserObject());

        DefaultMutableTreeNode componentNode = (DefaultMutableTreeNode) messageNode.getChildAt(0);
        assertEquals("Component1", componentNode.getUserObject());

        DefaultMutableTreeNode fieldNode = (DefaultMutableTreeNode) componentNode.getChildAt(0);
        assertEquals("Field1 (1)", fieldNode.getUserObject());
    }

    @Test
    public void testLoadItems() {
        DefaultMutableTreeNode branchNode = new DefaultMutableTreeNode("Branch");
        CompositeFixInfo leafNode = new CompositeFixInfo();
        FixInfo info = new FixInfo();
        leafNode.addItem(info);
        TreeView.loadItems(branchNode, leafNode, 0);

        assertEquals(1, branchNode.getChildCount());

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) branchNode.getChildAt(0);
        assertEquals(info, node.getUserObject());
    }

    @Test
    public void testExpandAll() {
        JButton expandButton = treeView.expandButton;
        expandButton.doClick();

        for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
            assertTrue(treeView.fixTree.isExpanded(i));
        }
    }

    @Test
    public void testCollapseAll() {
        JButton expandButton = treeView.expandButton;
        expandButton.doClick();

        JButton collapseButton = treeView.collapseButton;
        collapseButton.doClick();

        for (int i = 0; i < treeView.fixTree.getRowCount(); i++) {
            assertFalse(treeView.fixTree.isExpanded(i));
        }
    }

    @Test
    public void testSearch() {
        JButton searchButton = treeView.searchButton;
        JTextField searchTextField = treeView.searchTextField;
        searchTextField.setText("Message1");
        searchButton.doClick();

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(0);
        assertTrue(node.isHighlighted());

        node = (DefaultMutableTreeNode) node.getChildAt(0);
        assertTrue(node.isHighlighted());
    }

    @Test
    public void testClear() {
        JButton clearButton = treeView.clearButton;
        clearButton.doClick();

        assertFalse(root.isHighlighted());

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) root.getChildAt(0);
        assertFalse(node.isHighlighted());
    }

    @Test
    public void testValueChanged() {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode("Node");

        treeView.fixTree.setSelectionPath(new TreePath(node.getPath()));
        treeView.valueChanged(null);
        
        // Test assertions for displayDetails methods
    }

    @Test
    public void testActionPerformed() {
        JButton collapseButton = treeView.collapseButton;
        collapseButton.doClick();

        JButton expandButton = treeView.expandButton;
        expandButton.doClick();

        JButton searchButton = treeView.searchButton;
        searchButton.doClick();

        JButton clearButton = treeView.clearButton;
        clearButton.doClick();

        // Test assertions for respective buttons
    }

    @Test
    public void testKeyPressed() {
        KeyEvent event = new KeyEvent(
            treeView, 
            KeyEvent.KEY_PRESSED, 
            System.currentTimeMillis(), 
            KeyEvent.VK_ENTER, 
            KeyEvent.CHAR_UNDEFINED
        );

        treeView.keyPressed(event);

        // Test assertions for searchButton.doClick()
    }

    @Test
    public void testGetTreeCellRendererComponent() {
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) treeView.fixTree.getCellRenderer();
        JLabel label = (JLabel) renderer.getTreeCellRendererComponent(
            treeView.fixTree,
            field,
            true,
            true,
            false,
            0,
            true
        );

        assertEquals("Field1 (1)", label.getText());
        assertEquals(Color.BLUE, label.getForeground());

        label = (JLabel) renderer.getTreeCellRendererComponent(
            treeView.fixTree,
            message,
            true,
            true,
            false,
            0,
            true
        );

        assertEquals("Message1 (null)", label.getText());
        assertEquals(Color.BLUE, label.getForeground());

        label = (JLabel) renderer.getTreeCellRendererComponent(
            treeView.fixTree,
            component,
            true,
            true,
            false,
            0,
            true
        );

        assertEquals("[Component1]", label.getText());
        assertEquals(Color.BLUE, label.getForeground());
    }
}