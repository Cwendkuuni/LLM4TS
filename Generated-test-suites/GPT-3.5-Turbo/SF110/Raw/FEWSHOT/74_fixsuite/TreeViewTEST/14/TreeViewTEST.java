import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTree;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Component;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.util.List;

import org.fixsuite.message.info.DictionaryInfo;
import org.fixsuite.message.info.FixInfo;
import org.fixsuite.message.info.MessageInfo;
import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.Library;
import org.fixsuite.message.view.TreeView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TreeViewTest {

    private Library mockLibrary;
    private TreeView treeView;
    private FixInfoTreeNode rootNode;

    @Before
    public void setUp() {
        mockLibrary = mock(Library.class);
        treeView = new TreeView(mockLibrary);
        rootNode = (FixInfoTreeNode) treeView.loadFIXTree();
    }

    @Test
    public void testInitGUI() {
        assertEquals(BorderLayout.class, treeView.getLayout().getClass());
        assertTrue(treeView.getComponents()[0] instanceof JScrollPane);
        assertEquals(FixInfoCellRenderer.class, treeView.fixTree.getCellRenderer().getClass());
    }

    @Test
    public void testLoadFIXTree() {
        List<DictionaryInfo> mockDictionaries = mock(List.class);
        Mockito.when(mockLibrary.getDictionaries()).thenReturn(mockDictionaries);

        FixInfoTreeNode dictionaryNode = (FixInfoTreeNode) rootNode.getChildAt(0);

        DictionaryInfo mockDictionary = mock(DictionaryInfo.class);
        Mockito.when(mockDictionary.getVersion()).thenReturn("4.4");
        Mockito.when(mockDictionaries.get(0)).thenReturn(mockDictionary);

        FixInfoTreeNode versionNode = (FixInfoTreeNode) dictionaryNode.getChildAt(0);
        assertEquals("4.4", versionNode.getUserObject());
    }

    @Test
    public void testLoadMessages() {
        DictionaryInfo mockDictionary = mock(DictionaryInfo.class);
        List<MessageInfo> mockMessages = mock(List.class);
        Mockito.when(mockDictionary.getMessages()).thenReturn(mockMessages);

        FixInfoTreeNode versionNode = new FixInfoTreeNode("4.4");
        treeView.loadMessages(versionNode, mockDictionary);

        assertEquals(versionNode.getChildCount(), 0);

        MessageInfo mockMessage = mock(MessageInfo.class);
        Mockito.when(mockMessage.getName()).thenReturn("TestMessage");
        Mockito.when(mockMessage.getMessageType()).thenReturn("TEST");
        Mockito.when(mockMessages.get(0)).thenReturn(mockMessage);

        FixInfoTreeNode messageNode = (FixInfoTreeNode) versionNode.getChildAt(0);
        assertEquals("TestMessage", messageNode.getUserObject());
    }

    @Test
    public void testLoadItems() {
        CompositeFixInfo mockLeafNode = mock(CompositeFixInfo.class);
        List<FixInfo> mockItems = mock(List.class);
        Mockito.when(mockLeafNode.getItems()).thenReturn(mockItems);

        FixInfoTreeNode branchNode = new FixInfoTreeNode("TestNode");
        treeView.loadItems(branchNode, mockLeafNode, 0);

        assertEquals(branchNode.getChildCount(), 0);

        FixInfo mockItem = mock(FixInfo.class);
        Mockito.when(mockItems.get(0)).thenReturn(mockItem);

        FixInfoTreeNode itemNode = (FixInfoTreeNode) branchNode.getChildAt(0);
        assertEquals(mockItem, itemNode.getUserObject());
    }

    @Test
    public void testExpandAll() {
        JTree mockTree = mock(JTree.class);
        JScrollPane mockScrollPane = mock(JScrollPane.class);
        Mockito.when(mockScrollPane.getViewport().getView()).thenReturn(mockTree);
        treeView.mainScrollPane = mockScrollPane;

        treeView.expandAll();

        Mockito.verify(mockTree, Mockito.times(1)).expandRow(Mockito.anyInt());
    }

    @Test
    public void testCollapseAll() {
        JTree mockTree = mock(JTree.class);
        JScrollPane mockScrollPane = mock(JScrollPane.class);
        Mockito.when(mockScrollPane.getViewport().getView()).thenReturn(mockTree);
        treeView.mainScrollPane = mockScrollPane;

        treeView.collapseAll();

        Mockito.verify(mockTree, Mockito.times(1)).collapseRow(Mockito.anyInt());
    }

    @Test
    public void testValueChagned() {
        DefaultMutableTreeNode mockNode = mock(DefaultMutableTreeNode.class);
        Mockito.when(mockTree.getLastSelectedPathComponent()).thenReturn(mockNode);

        treeView.valueChanged(Mockito.mock(TreeSelectionEvent.class));

        Mockito.verify(mockNode, Mockito.times(4)).getUserObject();
        Mockito.verify(mockNode, Mockito.times(2)).getParent();
    }

    @Test
    public void testActionPerformed_expandButton() {
        treeView.expandButton.doClick();

        Mockito.verify(treeView, Mockito.times(1)).expandAll();
    }

    @Test
    public void testActionPerformed_collapseButton() {
        treeView.collapseButton.doClick();

        Mockito.verify(treeView, Mockito.times(1)).collapseAll();
    }

    @Test
    public void testClear() {
        treeView.clear((FixInfoTreeNode) rootNode.getChildAt(0));

        FixInfoTreeNode versionNode = (FixInfoTreeNode) rootNode.getChildAt(0);
        assertFalse(versionNode.isHighlighted());

        FixInfoTreeNode messageNode = (FixInfoTreeNode) versionNode.getChildAt(0);
        assertFalse(messageNode.isHighlighted());
    }

    @Test
    public void testSearch() {
        FixInfoTreeNode versionNode = new FixInfoTreeNode("4.4");
        FixInfoTreeNode messageNode = new FixInfoTreeNode("TestMessage");
        versionNode.add(messageNode);

        FixInfoTreeNode parentNode = new FixInfoTreeNode("Parent");
        parentNode.add(versionNode);

        treeView.search(parentNode, "Test");
        assertTrue(messageNode.isHighlighted());
        assertTrue(versionNode.isHighlighted());

        treeView.search(parentNode, "NotMatching");
        assertFalse(messageNode.isHighlighted());
        assertFalse(versionNode.isHighlighted());
    }

    @Test
    public void testFixInfoTreeNode() {
        FixInfoTreeNode node = treeView.new FixInfoTreeNode("TestNode");
        assertEquals("TestNode", node.getUserObject());
        assertFalse(node.isHighlighted());
    }

    @Test
    public void testFixInfoCellRenderer() {
        FixInfoTreeNode node = treeView.new FixInfoTreeNode("TestNode");
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();

        JLabel label = (JLabel) renderer.getTreeCellRendererComponent(treeView.fixTree, node, false, false, false, 0, false);

        assertEquals(label.getText(), node.getUserObject().toString());
        assertEquals(label.getStyle().getClass(), Font.class);
        assertFalse(node.isHighlighted());

        node.setHighlighted(true);
        label = (JLabel) renderer.getTreeCellRendererComponent(treeView.fixTree, node, false, false, false, 0, false);
        assertEquals(Color.BLUE, label.getForeground());
    }
}