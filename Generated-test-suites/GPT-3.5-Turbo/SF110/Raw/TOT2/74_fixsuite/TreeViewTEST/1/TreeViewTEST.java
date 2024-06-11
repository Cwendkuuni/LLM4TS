import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import java.awt.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class TreeViewTest {
    
    private Library library;
    private TreeView treeView;
    
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
        DictionaryInfo dictionary = new DictionaryInfo("Test Dictionary");
        treeView.loadMessages(versionNode, dictionary);
        assertEquals(1, versionNode.getChildCount());
    }
    
    @Test
    public void testLoadItems() {
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Test Branch");
        CompositeFixInfo leafNode = new CompositeFixInfo("Test Leaf");
        treeView.loadItems(branchNode, leafNode, 0);
        assertEquals(1, branchNode.getChildCount());
    }
    
    @Test
    public void testExpandAll() {
        treeView.expandAll();
        assertEquals(treeView.fixTree.getRowCount(), treeView.fixTree.getExpandedDescendants(new TreePath(treeView.fixTree.getModel().getRoot())));
    }
    
    @Test
    public void testCollapseAll() {
        treeView.collapseAll();
        assertEquals(0, treeView.fixTree.getExpandedDescendants(new TreePath(treeView.fixTree.getModel().getRoot())).length);
    }
    
    @Test
    public void testSearch() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.search(root, "Test");
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)root.getChildAt(0);
        assertEquals(true, ((FixInfoTreeNode)node.getChildAt(0)).isHighlighted());
    }
    
    @Test
    public void testClear() {
        FixInfoTreeNode root = treeView.loadFIXTree();
        treeView.search(root, "Test");
        treeView.clear(root);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)root.getChildAt(0);
        assertEquals(false, ((FixInfoTreeNode)node.getChildAt(0)).isHighlighted());
    }
    
    @Test
    public void testActionPerformed() {
        ActionEvent event = new ActionEvent(this, 0, null);
        treeView.actionPerformed(event);
        assertEquals(0, treeView.fixTree.getExpandedDescendants(new TreePath(treeView.fixTree.getModel().getRoot())).length);
        
    }
}