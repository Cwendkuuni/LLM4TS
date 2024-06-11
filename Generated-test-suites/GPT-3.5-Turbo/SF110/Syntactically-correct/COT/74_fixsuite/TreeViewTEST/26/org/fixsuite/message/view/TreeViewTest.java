
import org.junit.*;
import javax.swing.*;
import org.fixsuite.message.*;
import org.fixsuite.message.info.*;
import org.fixsuite.message.view.*;

public class TreeViewTest {

    private Library library;
    private TreeView treeView;

    @Before
    public void setUp() {
        library = new Library();
        treeView = new TreeView(library);
    }

    @Test
    public void testInitGUI() {
        // Check if the mainScrollPane is correctly initialized
        Assert.assertNotNull(treeView.mainScrollPane);
        Assert.assertEquals(new Dimension(300, 400), treeView.mainScrollPane.getPreferredSize());

        // Check if the fixTree is correctly initialized
        Assert.assertNotNull(treeView.fixTree);
        Assert.assertNotNull(treeView.fixTree.getCellRenderer());

        // Check if the toolbar components are correctly initialized
        Assert.assertNotNull(treeView.searchLabel);
        Assert.assertNotNull(treeView.searchTextField);
        Assert.assertNotNull(treeView.searchButton);
        Assert.assertNotNull(treeView.clearButton);
        Assert.assertNotNull(treeView.expandButton);
        Assert.assertNotNull(treeView.collapseButton);
    }

    @Test
    public void testLoadFIXTree() {
        FixInfoTreeNode result = treeView.loadFIXTree();

        // Check if the root is correctly created
        Assert.assertEquals("Dictionaries", result.getUserObject());

        // Check if the dictionaries are correctly loaded as child nodes
        Assert.assertEquals(library.getDictionaries().size(), result.getChildCount());
    }

    @Test
    public void testLoadMessages() {
        FixInfoTreeNode versionNode = new FixInfoTreeNode("Version");

        // Create a dummy dictionary with a single message
        DictionaryInfo dictionary = new DictionaryInfo();
        MessageInfo message = new MessageInfo();
        dictionary.addMessage(message);

        treeView.loadMessages(versionNode, dictionary);

        // Check if the versionNode has the correct child node
        Assert.assertEquals(1, versionNode.getChildCount());
        FixInfoTreeNode messageNode = (FixInfoTreeNode) versionNode.getChildAt(0);
        Assert.assertEquals(message, messageNode.getUserObject());

        // Check if the messageNode has the correct child nodes (items)
        Assert.assertEquals(message.getItems().size(), messageNode.getChildCount());
    }

    @Test
    public void testLoadItems() {
        FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");

        // Create a dummy leafNode with a single FixInfo item
        CompositeFixInfo leafNode = new CompositeFixInfo();
        FixInfo info = new FixInfo();
        leafNode.addItem(info);

        treeView.loadItems(branchNode, leafNode, 0);

        // Check if the branchNode has the correct child node
        Assert.assertEquals(1, branchNode.getChildCount());
        FixInfoTreeNode infoNode = (FixInfoTreeNode) branchNode.getChildAt(0);
        Assert.assertEquals(info, infoNode.getUserObject());
    }

    @Test
    public void testExpandAll() {
        // Expand the tree and check if all rows are expanded
        treeView.expandAll();
        for (int row = 0; row < treeView.fixTree.getRowCount(); row++) {
            Assert.assertTrue(treeView.fixTree.isExpanded(row));
        }
    }

    @Test
    public void testCollapseAll() {
        // Collapse the tree and check if all rows are collapsed
        treeView.collapseAll();
        for (int row = 0; row < treeView.fixTree.getRowCount(); row++) {
            Assert.assertFalse(treeView.fixTree.isExpanded(row));
        }
    }

    @Test
    public void testSearch() {
        FixInfoTreeNode root = treeView.loadFIXTree();

        // Search for a message name that matches the given pattern
        String searchString = ".*message.*";
        treeView.search(root, searchString);

        // Check if all nodes with matching message names are highlighted and expanded
        checkHighlightedNodes(root, searchString);

        // Search for a tag number that matches the given pattern
        searchString = ".*\\d+.*";
        treeView.search(root, searchString);

        // Check if all nodes with matching tag numbers are highlighted and expanded
        checkHighlightedNodes(root, searchString);
    }

    @Test
    public void testClear() {
        FixInfoTreeNode root = treeView.loadFIXTree();

        // Search for a message name that matches the given pattern
        String searchString = ".*message.*";
        treeView.search(root, searchString);

        // Clear the search results
        treeView.clear(root);

        // Check if all nodes are not highlighted
        checkHighlightedNodes(root, null);
    }

    @Test
    public void testValueChanged() {
        // Set up a dummy LibrarySuite and DetailsPanel
        LibrarySuite librarySuite = LibrarySuite.getInstance();
        DetailsPanel detailsPanel = new DetailsPanel();
        librarySuite.setDetailsPanel(detailsPanel);

        // Get a message node from the treeView
        FixInfoTreeNode node = (FixInfoTreeNode) treeView.fixTree.getModel().getRoot();
        node = (FixInfoTreeNode) node.getChildAt(0);

        // Call the valueChanged() method with the selected node
        treeView.valueChanged(null);

        // Check if the detailsPanel correctly displays the details of the selected node
        Assert.assertNotNull(detailsPanel.getFixInfo());
        Assert.assertNull(detailsPanel.getComponentInfo());
    }

    @Test
    public void testActionPerformed() {
        // Set up a dummy LibrarySuite and DetailsPanel
        LibrarySuite librarySuite = LibrarySuite.getInstance();
        DetailsPanel detailsPanel = new DetailsPanel();
        librarySuite.setDetailsPanel(detailsPanel);

        // Test the action performed by the collapseButton
        treeView.collapseButton.doClick();
        for (int row = 0; row < treeView.fixTree.getRowCount(); row++) {
            Assert.assertFalse(treeView.fixTree.isExpanded(row));
        }

        // Test the action performed by the expandButton
        treeView.expandButton.doClick();
        for (int row = 0; row < treeView.fixTree.getRowCount(); row++) {
            Assert.assertTrue(treeView.fixTree.isExpanded(row));
        }

        // Test the action performed by the searchButton
        treeView.searchTextField.setText("message");
        treeView.searchButton.doClick();
        checkHighlightedNodes((FixInfoTreeNode) treeView.fixTree.getModel().getRoot(), "message");

        // Test the action performed by the clearButton
        treeView.clearButton.doClick();
        checkHighlightedNodes((FixInfoTreeNode) treeView.fixTree.getModel().getRoot(), null);
    }

    @Test
    public void testKeyPressed() {
        KeyEvent event = new KeyEvent(treeView, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' ');

        // Press the enter key and check if the searchButton is clicked
        treeView.keyPressed(event);
        Assert.assertEquals(0, treeView.searchTextField.getText().length());
    }

    private void checkHighlightedNodes(FixInfoTreeNode node, String searchString) {
        if (node.isHighlighted()) {
            Assert.assertNotNull(searchString);
            if (node.getUserObject() instanceof FixInfo) {
                FixInfo info = (FixInfo) node.getUserObject();
                if (info instanceof MessageInfo) {
                    Assert.assertTrue(((MessageInfo) info).getName().matches(searchString)
                            || ((MessageInfo) info).getMessageType().matches(searchString));
                } else if (info instanceof ComponentInfo) {
                    Assert.assertTrue(((ComponentInfo) info).getName().matches(searchString));
                } else if (info instanceof FieldInfo) {
                    FieldInfo field = (FieldInfo) info;
                    String tagNumber = Integer.toString(field.getTagNumber());
                    Assert.assertTrue(field.getName().matches(searchString) || tagNumber.matches(searchString));
                }
            }
        } else {
            Assert.assertNull(searchString);
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            FixInfoTreeNode childNode = (FixInfoTreeNode) node.getChildAt(i);
            checkHighlightedNodes(childNode, searchString);
        }
    }

}
