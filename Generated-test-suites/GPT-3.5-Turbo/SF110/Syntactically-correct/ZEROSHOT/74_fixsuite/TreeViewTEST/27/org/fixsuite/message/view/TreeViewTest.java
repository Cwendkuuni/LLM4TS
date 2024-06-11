import static org.junit.Assert.*;
import org.junit.Test;

public class TreeViewTest {

    @Test
    public void testTreeView() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        assertNotNull(treeView);
    }

    @Test
    public void testLoadFIXTree() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode root = treeView.loadFIXTree();
        assertNotNull(root);
    }

    @Test
    public void testLoadMessages() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode versionNode = new FixInfoTreeNode("Version");
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
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
        CompositeFixInfo leafNode = new CompositeFixInfo();
        FixInfo info = new FixInfo();
        leafNode.addItem(info);
        treeView.loadItems(branchNode, leafNode, 0);
        assertEquals(1, branchNode.getChildCount());
    }

    @Test
    public void testExpandAll() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.expandAll();
    }

    @Test
    public void testCollapseAll() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.collapseAll();
    }

    @Test
    public void testSearch() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode node = new FixInfoTreeNode("Node");
        treeView.search(node, "searchString");
    }

    @Test
    public void testClear() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode node = new FixInfoTreeNode("Node");
        treeView.clear(node);
    }
    
    @Test
    public void testValueChange() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.valueChanged(null);
    }

    @Test
    public void testActionPerformed() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.actionPerformed(null);
    }

    @Test
    public void testKeyPressed() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.keyPressed(null);
    }

    @Test
    public void testKeyReleased() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.keyReleased(null);
    }

    @Test
    public void testKeyTyped() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.keyTyped(null);
    }

    @Test
    public void testFixInfoTreeNode() {
        Object userObject = new Object();
        FixInfoTreeNode node = new FixInfoTreeNode(userObject);
        assertEquals(userObject, node.getUserObject());
        assertFalse(node.isHighlighted());
    }

    @Test
    public void testFixInfoTreeNodeIsHighlighted() {
        FixInfoTreeNode node = new FixInfoTreeNode("Node");
        assertFalse(node.isHighlighted());
        node.setHighlighted(true);
        assertTrue(node.isHighlighted());
    }

    @Test
    public void testFixInfoCellRenderer() {
        TreeView.FixInfoCellRenderer renderer = new TreeView.FixInfoCellRenderer();
        JTree tree = new JTree();
        Object value = new Object();
        boolean sel = true;
        boolean expanded = true;
        boolean leaf = true;
        int row = 1;
        boolean hasFocus = true;
        assertNotNull(renderer.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus));
    }
}