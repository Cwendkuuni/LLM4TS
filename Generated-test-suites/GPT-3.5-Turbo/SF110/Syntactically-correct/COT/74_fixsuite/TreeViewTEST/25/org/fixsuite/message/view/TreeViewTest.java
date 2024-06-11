
import static org.junit.Assert.*;
import org.fixsuite.message.*;
import org.fixsuite.message.info.*;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.util.*;

public class TreeViewTest {
  
  private TreeView treeView;
  private Library library;
  
  @Before
  public void setUp() {
    library = new Library();
    treeView = new TreeView(library);
  }
  
  @Test
  public void testTreeViewConstructor() {
    assertNotNull(treeView);
  }
  
  @Test
  public void testInitGUI() {
    treeView.initGUI();
  
    assertNotNull(treeView.mainScrollPane);
    assertNotNull(treeView.fixTree);
    assertNotNull(treeView.searchLabel);
    assertNotNull(treeView.searchTextField);
    assertNotNull(treeView.searchButton);
    assertNotNull(treeView.clearButton);
    assertNotNull(treeView.expandButton);
    assertNotNull(treeView.collapseButton);
  }
  
  @Test
  public void testLoadFIXTree() {
    FixInfoTreeNode root = treeView.loadFIXTree();
  
    assertNotNull(root);
    assertEquals("Dictionaries", root.getUserObject());
  }
  
  @Test
  public void testLoadMessages() {
    FixInfoTreeNode versionNode = new FixInfoTreeNode("Version 1.0");
    DictionaryInfo dictionary = new DictionaryInfo();
    MessageInfo message = new MessageInfo();
    dictionary.addMessage(message);
  
    treeView.loadMessages(versionNode, dictionary);
  
    assertEquals(1, versionNode.getChildCount());
  }
  
  @Test
  public void testLoadItems() {
    FixInfoTreeNode branchNode = new FixInfoTreeNode("Branch");
    CompositeFixInfo leafNode = new CompositeFixInfo();
    FixInfo info = new FixInfo();
    leafNode.addItem(info);
  
    treeView.loadItems(branchNode, leafNode, 0);
  
    assertEquals(1, branchNode.getChildCount());
  }
  
  @Test
  public void testExpandAll() {
    treeView.expandAll();
  }
  
  @Test
  public void testCollapseAll() {
    treeView.collapseAll();
  }
  
  @Test
  public void testSearch() {
    FixInfoTreeNode node = new FixInfoTreeNode("Node");
    String searchString = "Search";
  
    treeView.search(node, searchString);
  
    assertFalse(node.isHighlighted());
  }
  
  @Test
  public void testClear() {
    FixInfoTreeNode node = new FixInfoTreeNode("Node");
  
    treeView.clear(node);
  
    assertFalse(node.isHighlighted());
  }
  
  @Test
  public void testValueChanged() {
    JTree tree = new JTree();
    FixInfoTreeNode node = new FixInfoTreeNode("Node");
    tree.setLastSelectedPathComponent(node);
  
    treeView.valueChanged(new TreeSelectionEvent(tree, new TreePath(node.getPath()), true, null, null));
  }
  
  @Test
  public void testActionPerformed() {
    ActionEvent event = new ActionEvent(treeView.collapseButton, ActionEvent.ACTION_PERFORMED, "");
  
    treeView.actionPerformed(event);
  }
  
  @Test
  public void testKeyPressed() {
    KeyEvent event = new KeyEvent(treeView, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, ' ');
  
    treeView.keyPressed(event);
  }
  
  @Test
  public void testKeyReleased() {
    KeyEvent event = new KeyEvent(treeView, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, ' ');
  
    treeView.keyReleased(event);
  }
  
  @Test
  public void testKeyTyped() {
    KeyEvent event = new KeyEvent(treeView, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, ' ');
  
    treeView.keyTyped(event);
  }
}
