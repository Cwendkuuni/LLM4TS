package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class RenameLabelsTest {

  @Test
  public void testProcessLabels() {
    RenameLabels renameLabels = new RenameLabels(null);
    Node root = Node.newBlock("root");
    renameLabels.process(Node.newNull(), root);

    ScopedCallback callback = new RenameLabels.ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(root, callback);
    nodeTraversal.traverse();

    LabelNamespace labelNamespace = callback.namespaceStack.peek();
    assertNotNull(labelNamespace);
    assertTrue(labelNamespace.renameMap.isEmpty());

    // Add some labels and verify their names.
    root.addChild(Node.newLabel("a"));
    root.addChild(Node.newLabel("b"));
    root.addChild(Node.newLabel("c"));

    nodeTraversal = new NodeTraversal(root, callback);
    nodeTraversal.traverse();

    Map<String, LabelInfo> renameMap = labelNamespace.renameMap;
    assertEquals(3, renameMap.size());

    LabelInfo labelInfoA = renameMap.get("a");
    assertEquals(1, labelInfoA.id);

    LabelInfo labelInfoB = renameMap.get("b");
    assertEquals(2, labelInfoB.id);

    LabelInfo labelInfoC = renameMap.get("c");
    assertEquals(3, labelInfoC.id);
  }

  @Test
  public void testRenameLabel() {
    RenameLabels renameLabels = new RenameLabels(null);
    Node root = Node.newBlock("root");

    root.addChild(Node.newLabel("a"));
    root.addChild(Node.newLabel("a"));

    Node labelA = root.getChildAt(0);
    Node labelB = root.getChildAt(1);

    root.addChild(Node.newBreak(labelA));
    root.addChild(Node.newLabel("a"));
    root.addChild(Node.newLabel("b"));
    root.addChild(Node.newLabel("a"));
    root.addChild(Node.newBreak(labelB));

    root.addChild(Node.newBlock("block1", Node.NEW_EXPRS, Node.BLOCK));
    root.addChild(Node.newLabel("a"));
    root.addChild(Node.newBreak(Node.newLabel("a")));
    root.addChild(Node.newLabel("b"));
    root.addChild(Node.newLabel("a"));
    root.addChild(Node.newBreak(Node.newLabel("b")));
    root.addChild(Node.newBlock("block2", Node.NEW_EXPRS, Node.BLOCK));

    Node labelANew = Node.newLabel("newA");
    labelA.setFirstChild(labelANew);

    Node labelBNew = Node.newLabel("newB");
    labelB.setFirstChild(labelBNew);

    root.addChild(Node.newBreak(labelANew));
    root.addChild(Node.newLabel("newA"));
    root.addChild(Node.newLabel("newB"));
    root.addChild(Node.newLabel("newA"));
    root.addChild(Node.newBreak(labelBNew));

    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(root, processLabels);
    nodeTraversal.traverse();

    Map<String, LabelInfo> renameMap = processLabels.namespaceStack.peek().renameMap;
    LabelInfo labelInfoA = renameMap.get("newA");
    assertEquals(1, labelInfoA.id);

    LabelInfo labelInfoNewA = renameMap.get("newA");
    assertEquals(1, labelInfoNewA.id);

    LabelInfo labelInfoB = renameMap.get("newB");
    assertEquals(2, labelInfoB.id);

    LabelInfo labelInfoNewB = renameMap.get("newB");
    assertEquals(2, labelInfoNewB.id);
  }

  @Test
  public void testRemoveUnusedLabels() {
    RenameLabels renameLabels = new RenameLabels(null, null, false);
    Node root = Node.newBlock("root");

    root.addChild(Node.newLabel("a"));
    root.addChild(Node.newLabel("b"));
    root.addChild(Node.newLabel("c"));

    Node labelANew = Node.newLabel("newA");
    labelANew.setParent(root);

    Node labelBNew = Node.newLabel("newB");
    labelBNew.setParent(root);

    root.addChild(Node.newBreak(labelANew));
    root.addChild(Node.newLabel("newA"));
    root.addChild(Node.newLabel("newB"));
    root.addChild(Node.newLabel("newA"));
    root.addChild(Node.newBreak(labelBNew));

    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(root, processLabels);
    nodeTraversal.traverse();

    Map<String, LabelInfo> renameMap = processLabels.namespaceStack.peek().renameMap;
    LabelInfo labelInfoA = renameMap.get("newA");
    assertEquals(1, labelInfoA.id);

    LabelInfo labelInfoNewA = renameMap.get("newA");
    assertEquals(1, labelInfoNewA.id);

    LabelInfo labelInfoB = renameMap.get("newB");
    assertEquals(2, labelInfoB.id);

    LabelInfo labelInfoNewB = renameMap.get("newB");
    assertEquals(2, labelInfoNewB.id);

    // Remove label "newB" and verify it's removed.
    root.removeChild(labelBNew);
    root.addChild(Node.newLabel("newC"));

    processLabels = new RenameLabels.ProcessLabels();
    nodeTraversal = new NodeTraversal(root, processLabels);
    nodeTraversal.traverse();

    renameMap = processLabels.namespaceStack.peek().renameMap;
    assertFalse(renameMap.containsKey("newB"));
    assertNotNull(renameMap.get("newC"));
  }

  @Test
  public void testRenameConflictingLabels() {
    RenameLabels renameLabels = new RenameLabels(null);
    Node root = Node.newBlock("root");

    root.addChild(Node.newLabel("a"));
    root.addChild(Node.newLabel("a"));

    Node labelA = root.getChildAt(0);
    Node labelB = root.getChildAt(1);

    root.addChild(Node.newBreak(labelA));
    root.addChild(Node.newLabel("a"));
    root.addChild(Node.newLabel("a"));
    root.addChild(Node.newLabel("b"));
    root.addChild(Node.newBreak(labelB));

    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    NodeTraversal nodeTraversal = new NodeTraversal(root, processLabels);
    nodeTraversal.traverse();

    Map<String, LabelInfo> renameMap = processLabels.namespaceStack.peek().renameMap;
    LabelInfo labelInfoA1 = renameMap.get("a1");
    assertEquals(1, labelInfoA1.id);

    LabelInfo labelInfoA2 = renameMap.get("a2");
    assertEquals(2, labelInfoA2.id);

    LabelInfo labelInfoB = renameMap.get("b");
    assertEquals(3, labelInfoB.id);

    Node labelA1 = labelInfoA1.node;
    Node labelA2 = labelInfoA2.node;

    assertEquals("a1", labelA1.getFirstChild().getString());
    assertEquals("a2", labelA2.getFirstChild().getString());
  }

  private void assertEquals(int expected, int actual) {
    org.junit.Assert.assertEquals(expected, actual);
  }

  private void assertEquals(String expected, String actual) {
    org.junit.Assert.assertEquals(expected, actual);
  }

  private void assertFalse(boolean condition) {
    org.junit.Assert.assertFalse(condition);
  }

  private void assertNotNull(Object object) {
    org.junit.Assert.assertNotNull(object);
  }

  private void assertTrue(boolean condition) {
    org.junit.Assert.assertTrue(condition);
  }

  private Map<String, LabelInfo> getLabelNamespace(Node node) {
    return ((RenameLabels.ProcessLabels) node.getUserData(RenameLabels.class)).namespaceStack.peek().renameMap;
  }

  private String getNameForId(int id, Map<String, LabelInfo> labelNamespace) {
    return labelNamespace.get(String.valueOf(id)).node.getFirstChild().getString();
  }

  private LabelInfo getLabelInfo(String name, Map<String, LabelInfo> labelNamespace) {
    return labelNamespace.get(name);
  }

  private void setLabelInfoReferenced(String name, Map<String, LabelInfo> labelNamespace) {
    LabelInfo labelInfo = getLabelInfo(name, labelNamespace);
    labelInfo.referenced = true;
  }

  private void removeLabel(String name, Map<String, LabelInfo> labelNamespace) {
    LabelInfo labelInfo = getLabelInfo(name, labelNamespace);
    getLabelNamespace(labelInfo.node).renameMap.remove(labelInfo.node.getFirstChild().getString());
  }
}