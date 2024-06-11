import org.junit.*;
import javax.swing.*;
import java.util.*;
import org.fixsuite.message.info.*;
import javax.swing.event.*;
import org.fixsuite.message.*;
import java.awt.event.*;
import javax.swing.tree.*;
import java.awt.*;

public class TreeViewTest {

    @Test
    public void testInitGUI() {
        TreeView treeView = new TreeView(new Library());
        Assert.assertNotNull(treeView);
    }

    @Test
    public void testLoadFIXTree() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode root = treeView.loadFIXTree();
        Assert.assertNotNull(root);
    }

    @Test
    public void testLoadMessages() {
        Library library = new Library();
        DictionaryInfo dictionary = new DictionaryInfo("FIX.4.4");
        library.addDictionary(dictionary);
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode versionNode = new FixInfoTreeNode(dictionary.getVersion());
        treeView.loadMessages(versionNode, dictionary);
        Assert.assertEquals(versionNode.getChildCount(), dictionary.getMessages().size());
    }

    @Test
    public void testLoadItems() {
        Library library = new Library();
        DictionaryInfo dictionary = new DictionaryInfo("FIX.4.4");
        library.addDictionary(dictionary);
        MessageInfo message = new MessageInfo("TestMessage", "TEST");
        dictionary.addMessage(message);
        FieldInfo field = new FieldInfo("TestField", 1);
        message.addField(field);
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode branchNode = new FixInfoTreeNode(message);
        treeView.loadItems(branchNode, message, 0);
        Assert.assertEquals(branchNode.getChildCount(), message.getItems().size());
    }

    @Test
    public void testExpandAll() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.expandAll();
        // TODO: Assert expanded rows
    }

    @Test
    public void testCollapseAll() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.collapseAll();
        // TODO: Assert collapsed rows
    }

    @Test
    public void testSearch() {
        Library library = new Library();
        DictionaryInfo dictionary = new DictionaryInfo("FIX.4.4");
        library.addDictionary(dictionary);
        MessageInfo message = new MessageInfo("TestMessage", "TEST");
        dictionary.addMessage(message);
        FieldInfo field = new FieldInfo("TestField", 1);
        message.addField(field);
        TreeView treeView = new TreeView(library);
        treeView.search((FixInfoTreeNode)treeView.fixTree.getModel().getRoot(), "Test");
        // TODO: Assert highlighted nodes
    }

    @Test
    public void testClear() {
        Library library = new Library();
        DictionaryInfo dictionary = new DictionaryInfo("FIX.4.4");
        library.addDictionary(dictionary);
        MessageInfo message = new MessageInfo("TestMessage", "TEST");
        dictionary.addMessage(message);
        FieldInfo field = new FieldInfo("TestField", 1);
        message.addField(field);
        TreeView treeView = new TreeView(library);
        treeView.clear((FixInfoTreeNode)treeView.fixTree.getModel().getRoot());
        // TODO: Assert cleared nodes
    }

    @Test
    public void testValueChanged() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        TreeSelectionEvent event = new TreeSelectionEvent(treeView.fixTree, new TreePath(), false, null, null);
        treeView.valueChanged(event);
        // TODO: Assert details panel display
    }

    @Test
    public void testActionPerformed() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        ActionEvent event = new ActionEvent(treeView.collapseButton, ActionEvent.ACTION_PERFORMED, "collapse");
        treeView.actionPerformed(event);
        // TODO: Assert collapse all
        event = new ActionEvent(treeView.expandButton, ActionEvent.ACTION_PERFORMED, "expand");
        treeView.actionPerformed(event);
        // TODO: Assert expand all
        event = new ActionEvent(treeView.searchButton, ActionEvent.ACTION_PERFORMED, "search");
        treeView.actionPerformed(event);
        Assert.assertEquals(treeView.searchTextField.getText(), "");
        event = new ActionEvent(treeView.clearButton, ActionEvent.ACTION_PERFORMED, "clear");
        treeView.actionPerformed(event);
        // TODO: Assert clear all
    }

    @Test
    public void testKeyPressed() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        KeyEvent event = new KeyEvent(treeView.searchTextField, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
        treeView.keyPressed(event);
        // TODO: Assert searchButton pressed
    }

    @Test
    public void testKeyReleased() {
        // No functionality to test
    }

    @Test
    public void testKeyTyped() {
        // No functionality to test
    }
}