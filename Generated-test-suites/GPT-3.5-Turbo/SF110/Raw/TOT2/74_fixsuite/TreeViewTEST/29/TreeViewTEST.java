import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {
    
    private TreeView treeView;
    
    @Before
    public void setUp() {
        treeView = new TreeView(new Library());
    }
    
    @Test
    public void testLoadFIXTree() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        assertNotNull("Root node should not be null", root);
        assertEquals("Root node should have 'Dictionaries' as user object", "Dictionaries", root.getUserObject());
    }
    
    @Test
    public void testLoadMessages() {
        FixInfoTreeNode versionNode = new FixInfoTreeNode("Version");
        DictionaryInfo dictionary = new DictionaryInfo("Dictionary");
        treeView.loadMessages(versionNode, dictionary);
        assertEquals("Version node should have dictionary as child node", 1, versionNode.getChildCount());
    }
    
    @Test
    public void testLoadItems() {
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
        CompositeFixInfo leafNode = new CompositeFixInfo();
        treeView.loadItems(branchNode, leafNode, 0);
        assertEquals("Branch node should have items from leaf node", leafNode.getItems().size(), branchNode.getChildCount());
    }
    
    @Test
    public void testExpandAll() {
        treeView.expandAll();
        assertEquals("All nodes should be expanded", treeView.fixTree.getRowCount(), treeView.fixTree.getExpandedDescendants(new TreePath(treeView.fixTree.getModel().getRoot())).length);
    }
    
    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        assertEquals("All nodes should be collapsed", 0, treeView.fixTree.getExpandedDescendants(new TreePath(treeView.fixTree.getModel().getRoot())).length);
    }
    
    @Test
    public void testSearch() {
        FixInfoTreeNode rootNode = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();
        treeView.search(rootNode, "Test");
        assertEquals("All matching nodes should be highlighted", 0, countHighlightedNodes(rootNode));
    }
    
    @Test
    public void testClear() {
        FixInfoTreeNode rootNode = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();
        treeView.clear(rootNode);
        assertEquals("All nodes should be unhighlighted", 0, countHighlightedNodes(rootNode));
    }
    
    @Test
    public void testActionPerformed() {
        treeView.clearButton.doClick();
        assertEquals("Clear button should unhighlight all nodes", 0, countHighlightedNodes((FixInfoTreeNode)treeView.fixTree.getModel().getRoot()));
    }
    
    private int countHighlightedNodes(FixInfoTreeNode node) {
        int count = 0;
        if (node.isHighlighted()) {
            count++;
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            FixInfoTreeNode childNode = (FixInfoTreeNode) node.getChildAt(i);
            count += countHighlightedNodes(childNode);
        }
        return count;
    }
}