import org.junit.*;
import static org.junit.Assert.*;

public class TreeViewTest {

    @Test
    public void testConstructor() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        assertNotNull(treeView);
    }
    
    @Test
    public void testInitGUI() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.initGUI();
        assertNotNull(treeView.getMainScrollPane());
        assertNotNull(treeView.getFixTree());
        assertNotNull(treeView.getSearchLabel());
        assertNotNull(treeView.getSearchTextField());
        assertNotNull(treeView.getSearchButton());
        assertNotNull(treeView.getClearButton());
        assertNotNull(treeView.getExpandButton());
        assertNotNull(treeView.getCollapseButton());
    }
    
    @Test
    public void testLoadFIXTree() {
        Library library = new Library();
        DictionaryInfo dictionary = new DictionaryInfo();
        library.addDictionary(dictionary);
        MessageInfo message = new MessageInfo();
        dictionary.addMessage(message);
        
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode root = treeView.loadFIXTree();
        
        assertNotNull(root);
        assertEquals(1, root.getChildCount());
        FixInfoTreeNode node = (FixInfoTreeNode) root.getChildAt(0);
        assertNotNull(node);
        assertEquals(dictionary.getVersion(), node.getUserObject());
        assertEquals(1, node.getChildCount());
        FixInfoTreeNode messageNode = (FixInfoTreeNode) node.getChildAt(0);
        assertNotNull(messageNode);
        assertEquals(message, messageNode.getUserObject());
    }
    
    @Test
    public void testLoadMessages() {
        Library library = new Library();
        DictionaryInfo dictionary = new DictionaryInfo();
        library.addDictionary(dictionary);
        MessageInfo message = new MessageInfo();
        dictionary.addMessage(message);
        
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode versionNode = new FixInfoTreeNode(dictionary.getVersion());
        treeView.loadMessages(versionNode, dictionary);
        
        assertEquals(1, versionNode.getChildCount());
        FixInfoTreeNode messageNode = (FixInfoTreeNode) versionNode.getChildAt(0);
        assertEquals(message, messageNode.getUserObject());
    }
    
    @Test
    public void testLoadItems() {
        Library library = new Library();
        DictionaryInfo dictionary = new DictionaryInfo();
        library.addDictionary(dictionary);
        MessageInfo message = new MessageInfo();
        dictionary.addMessage(message);
        CompositeFixInfo compositeFixInfo = new CompositeFixInfo();
        message.addItem(compositeFixInfo);
        compositeFixInfo.addItem(new FixInfo());
        
        TreeView treeView = new TreeView(library);
        FixInfoTreeNode branchNode = new FixInfoTreeNode(message);
        treeView.loadItems(branchNode, compositeFixInfo, 0);
        
        assertEquals(1, branchNode.getChildCount());
        FixInfoTreeNode fixInfoNode = (FixInfoTreeNode) branchNode.getChildAt(0);
        assertTrue(fixInfoNode.getUserObject() instanceof FixInfo);
    }
    
    @Test
    public void testExpandAll() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.expandAll();
        
        // Verify that all rows in the tree are expanded
        // Need to find a way to get the count of rows in the tree
    }
    
    @Test
    public void testCollapseAll() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        treeView.collapseAll();
        
        // Verify that all rows in the tree are collapsed
        // Need to find a way to get the count of rows in the tree
    }
    
    @Test
    public void testSearch() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        
        FixInfoTreeNode root = new FixInfoTreeNode("Dictionaries");
        FixInfoTreeNode node1 = new FixInfoTreeNode("Dictionary1");
        FixInfoTreeNode node2 = new FixInfoTreeNode("Dictionary2");
        FixInfoTreeNode node3 = new FixInfoTreeNode(new MessageInfo("Message1"));
        FixInfoTreeNode node4 = new FixInfoTreeNode(new MessageInfo("Message2"));
        root.add(node1);
        root.add(node2);
        node1.add(node3);
        node2.add(node4);
        
        treeView.search(node3, "Message1");
        assertTrue(node3.isHighlighted());
        assertFalse(node4.isHighlighted());
    }
    
    @Test
    public void testClear() {
        Library library = new Library();
        TreeView treeView = new TreeView(library);
        
        FixInfoTreeNode root = new FixInfoTreeNode("Dictionaries");
        FixInfoTreeNode node1 = new FixInfoTreeNode("Dictionary1");
        FixInfoTreeNode node2 = new FixInfoTreeNode("Dictionary2");
        FixInfoTreeNode node3 = new FixInfoTreeNode(new MessageInfo("Message1"));
        FixInfoTreeNode node4 = new FixInfoTreeNode(new MessageInfo("Message2"));
        root.add(node1);
        root.add(node2);
        node1.add(node3);
        node2.add(node4);
        node3.setHighlighted(true);
        node4.setHighlighted(true);
        
        treeView.clear(root);
        assertFalse(node3.isHighlighted());
        assertFalse(node4.isHighlighted());
    }
    
    @Test
    public void testValueChanged() {
        // Need to find a way to test this method
    }
    
    @Test
    public void testActionPerformed() {
        // Need to find a way to test this method
    }
    
    @Test
    public void testKeyPressed() {
        // Need to find a way to test this method
    }
    
    @Test
    public void testKeyReleased() {
        // Need to find a way to test this method
    }
    
    @Test
    public void testKeyTyped() {
        // Need to find a way to test this method
    }
    
}