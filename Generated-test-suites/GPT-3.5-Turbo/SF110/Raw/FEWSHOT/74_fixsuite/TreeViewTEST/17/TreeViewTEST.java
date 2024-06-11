
package org.fixsuite.message.view;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TreeViewTest {
  
  private TreeView treeView;
  
  @Before
  public void setUp() {
    treeView = new TreeView(null);
  }
  
  @Test
  public void testInitGUI() {
    assertNull(treeView.getLayout());
    assertNull(treeView.getMainScrollPane());
    assertNull(treeView.getFixTree());
    assertNull(treeView.getSearchLabel());
    assertNull(treeView.getSearchTextField());
    assertNull(treeView.getSearchButton());
    assertNull(treeView.getClearButton());
    assertNull(treeView.getExpandButton());
    assertNull(treeView.getCollapseButton());
    assertNull(treeView.getLibrary());
  }
  
  @Test
  public void testLoadFIXTree() {
    assertNotNull(treeView.loadFIXTree());
  }
  
  @Test
  public void testLoadMessages() {
    assertNotNull(treeView.loadMessages(null, null));
  }
  
  @Test
  public void testLoadItems() {
    assertNotNull(treeView.loadItems(null, null, 0));
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
    treeView.search(null, "");
  }
  
  @Test
  public void testClear() {
    treeView.clear(null);
  }
  
  @Test
  public void testValueChanged() {
    treeView.valueChanged(null);
  }
  
  @Test
  public void testActionPerformed() {
    treeView.actionPerformed(null);
  }
  
  @Test
  public void testKeyPressed() {
    treeView.keyPressed(null);
  }
  
  @Test
  public void testKeyReleased() {
    treeView.keyReleased(null);
  }
  
  @Test
  public void testKeyTyped() {
    treeView.keyTyped(null);
  }
}

