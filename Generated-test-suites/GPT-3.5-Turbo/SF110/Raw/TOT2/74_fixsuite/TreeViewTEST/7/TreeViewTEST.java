import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TreeViewTest {
    private Library mockLibrary;

    @Before
    public void setUp() {
        mockLibrary = new Library();
    }

    @Test
    public void testTreeViewConstructor() {
        TreeView treeView = new TreeView(mockLibrary);
        assertNotNull(treeView);
    }

    @Test
    public void testLoadFIXTree() {
        TreeView treeView = new TreeView(mockLibrary);
        assertNotNull(treeView.loadFIXTree());
        assertEquals("Dictionaries", treeView.loadFIXTree().getUserObject());
    }

    @Test
    public void testLoadMessages() {
        TreeView treeView = new TreeView(mockLibrary);
        assertNotNull(treeView.loadFIXTree());
        treeView.loadMessages((FixInfoTreeNode) treeView.loadFIXTree().getChildAt(0), mockLibrary.getDictionaries().get(0));
        assertEquals(1, treeView.loadFIXTree().getChildAt(0).getChildCount());
    }

    @Test
    public void testLoadItems() {
        TreeView treeView = new TreeView(mockLibrary);
        FixInfoTreeNode versionNode = new FixInfoTreeNode("Version");
        assertNotNull(treeView.loadFIXTree());
        treeView.loadItems(versionNode, mockLibrary.getDictionaries().get(0).getMessages().get(0), 0);
        assertEquals(1, versionNode.getChildCount());
    }

    @Test
    public void testExpandAll() {
        TreeView treeView = new TreeView(mockLibrary);
        assertNotNull(treeView.loadFIXTree());
        treeView.expandAll();
    }

    @Test
    public void testCollapseAll() {
        TreeView treeView = new TreeView(mockLibrary);
        assertNotNull(treeView.loadFIXTree());
        treeView.collapseAll();
    }

    @Test
    public void testSearch() {
        TreeView treeView = new TreeView(mockLibrary);
        assertNotNull(treeView.loadFIXTree());
        treeView.search((FixInfoTreeNode) treeView.loadFIXTree().getChildAt(0), "messageName");
        // Add assertion here
    }

    @Test
    public void testClear() {
        TreeView treeView = new TreeView(mockLibrary);
        assertNotNull(treeView.loadFIXTree());
        treeView.clear((FixInfoTreeNode) treeView.loadFIXTree().getChildAt(0));
        // Add assertion here
    }
}