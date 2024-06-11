package org.fixsuite.message.view;
import org.fixsuite.message.Library;
import org.fixsuite.message.LibrarySuite;
import org.fixsuite.message.info.ComponentInfo;
import org.fixsuite.message.info.FieldInfo;
import org.fixsuite.message.info.MessageInfo;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import java.awt.event.KeyEvent;
import static org.junit.Assert.*;

public class TreeViewTest {

	private TreeView treeView;

	@Before
	public void setUp() {
		Library library = new Library();
		treeView = new TreeView(library);

		// Create a sample tree for testing
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
		DefaultMutableTreeNode node1 = new DefaultMutableTreeNode("Node 1");
		DefaultMutableTreeNode node2 = new DefaultMutableTreeNode("Node 2");
		DefaultMutableTreeNode node3 = new DefaultMutableTreeNode("Node 3");
		root.add(node1);
		root.add(node2);
		node2.add(node3);

		treeView.fixTree = new JTree(root);
		treeView.fixTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	@Test
	public void testTreeView() {
		assertNotNull(treeView);
	}

	@Test
	public void testValueChanged() {
		// select a tree node
		TreeView.FixInfoTreeNode node = new TreeView.FixInfoTreeNode(new Object());
		treeView.fixTree.setSelectionPath(new TreePath(node.getPath()));

		// test the valueChanged method
		treeView.valueChanged(null); // pass null for the TreeSelectionEvent parameter

		// verify that the DetailsPanel was updated
		assertNotNull(LibrarySuite.getInstance().getDetailsPanel());
		//assertNull(LibrarySuite.getInstance().getDetailsPanel().getDisplay());

	}

	@Test
	public void testValueChanged2() {
		TreeView.FixInfoTreeNode node = new TreeView.FixInfoTreeNode(new FieldInfo());
		treeView.fixTree.setSelectionPath(new TreePath(node.getPath()));
		treeView.valueChanged(null);
		//assertNull(LibrarySuite.getInstance().getDetailsPanel().getDisplay());

		node = new TreeView.FixInfoTreeNode(new ComponentInfo());
		treeView.fixTree.setSelectionPath(new TreePath(node.getPath()));
		treeView.valueChanged(null);
		//assertNotNull(LibrarySuite.getInstance().getDetailsPanel().getDisplay());

		node = new TreeView.FixInfoTreeNode(new MessageInfo());
		treeView.fixTree.setSelectionPath(new TreePath(node.getPath()));
		treeView.valueChanged(null);
		//assertNotNull(LibrarySuite.getInstance().getDetailsPanel().getDisplay());
	}

	@Test
	public void testActionPerformed() {
		// Test collapse button
		treeView.expandAll();
		treeView.collapseButton.doClick();
		assertFalse(treeView.fixTree.isExpanded(0));

		// Test expand button
		treeView.collapseAll();
		treeView.expandButton.doClick();
		assertTrue(treeView.fixTree.isExpanded(0));

		// Test search button
		treeView.searchTextField.setText("Node 3");
		treeView.searchButton.doClick();
		assertFalse(treeView.fixTree.isCollapsed(1));
		assertTrue(treeView.fixTree.isCollapsed(0));

		// Test clear button
		treeView.clearButton.doClick();
		assertTrue(treeView.fixTree.isCollapsed(1));
		assertTrue(treeView.fixTree.isCollapsed(0));
	}

	@Test
	public void testTreeViewKeyReleased() {
		KeyEvent event = new KeyEvent(treeView, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
		treeView.keyReleased(event);
	}

	@Test
	public void testTreeViewKeyTyped() {
		KeyEvent event = new KeyEvent(treeView, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);
		treeView.keyTyped(event);
	}

}