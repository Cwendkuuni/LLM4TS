import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class TreeViewTest {

    @Test
    public void testLoadFIXTree() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode root = treeView.loadFIXTree();

        assertNotNull(root);
        assertEquals("Dictionaries", root.getUserObject());
        assertEquals(0, root.getChildCount());
    }

    @Test
    public void testLoadMessages() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode versionNode = new FixInfoTreeNode("Version 1.0");
        DictionaryInfo dictionary = new DictionaryInfo();
        MessageInfo message = new MessageInfo();
        dictionary.addMessage(message);

        treeView.loadMessages(versionNode, dictionary);

        assertEquals(1, versionNode.getChildCount());
    }

    @Test
    public void testLoadItems() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch Node");
        CompositeFixInfo leafNode = new CompositeFixInfo();
        leafNode.addItem(new FixInfo("Item"));

        treeView.loadItems(branchNode, leafNode, 0);

        assertEquals(1, branchNode.getChildCount());
    }

    // Add more test cases for other methods as needed

}