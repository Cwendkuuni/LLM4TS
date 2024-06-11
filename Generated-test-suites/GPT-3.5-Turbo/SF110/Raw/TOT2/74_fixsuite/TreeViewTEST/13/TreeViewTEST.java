import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.fixsuite.message.*;
import org.fixsuite.message.info.*;

public class TreeViewTest {

    private TreeView treeView;
    private Library library;

    @Before
    public void setUp() {
        library = new Library();
        treeView = new TreeView(library);
    }

    @Test
    public void testLoadFIXTree() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        assertNotNull(root);
        assertEquals("Dictionaries", root.getUserObject());
    }

    @Test
    public void testLoadMessages() {
        FixInfoTreeNode versionNode = new FixInfoTreeNode("Test Version");
        DictionaryInfo dictionaryInfo = new DictionaryInfo("Test Dictionary");
        MessageInfo messageInfo = new MessageInfo("Test Message", "Test Type");
        CompositeFixInfo compositeFixInfo = new CompositeFixInfo("Test Composite");
        dictionaryInfo.getMessages().add(messageInfo);
        messageInfo.getItems().add(compositeFixInfo);

        treeView.loadMessages(versionNode, dictionaryInfo);

        assertEquals(1, versionNode.getChildCount());
        assertEquals(messageInfo, ((FixInfoTreeNode) versionNode.getChildAt(0)).getUserObject());
    }

    @Test
    public void testLoadItems() {
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
        CompositeFixInfo compositeFixInfo = new CompositeFixInfo("Test Composite");
        compositeFixInfo.getItems().add(new FieldInfo(1, "Test Field"));

        treeView.loadItems(branchNode, compositeFixInfo, 0);

        assertEquals(1, branchNode.getChildCount());
        assertEquals(compositeFixInfo, ((FixInfoTreeNode) branchNode.getChildAt(0)).getUserObject());
    }

    // Add more test cases for other methods

}