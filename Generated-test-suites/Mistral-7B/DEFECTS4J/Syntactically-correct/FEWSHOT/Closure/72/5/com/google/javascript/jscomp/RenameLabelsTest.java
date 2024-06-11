package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RenameLabelsTest {
  @Test
  public void testProcessLabelsEnterAndExitScope() {
    RenameLabels renameLabels = new RenameLabels(null);
    ProcessLabels processLabels = new ProcessLabels();

    // Test entering and exiting a scope.
    processLabels.enterScope(null);
    processLabels.exitScope(null);

    Map<String, RenameLabels.LabelInfo> labelInfoMap = processLabels.namespaceStack.peek().renameMap;
    assertNull(labelInfoMap);
  }

  @Test
  public void testProcessLabelsEnterAndExitScopeMultipleTimes() {
    RenameLabels renameLabels = new RenameLabels(null);
    ProcessLabels processLabels = new ProcessLabels();

    // Test entering and exiting a scope multiple times.
    for (int i = 0; i < 10; i++) {
      processLabels.enterScope(null);
      processLabels.exitScope(null);
    }

    Map<String, RenameLabels.LabelInfo> labelInfoMap = processLabels.namespaceStack.peek().renameMap;
    assertNull(labelInfoMap);
  }

  @Test
  public void testProcessLabelsDetermineNewNameForLabel() {
    RenameLabels renameLabels = new RenameLabels(null);
    ProcessLabels processLabels = new ProcessLabels();

    // Test determining a new name for a label.
    String labelName = "label1";
    processLabels.enterScope(null);
    processLabels.shouldTraverse(null, null, null);
    RenameLabels.LabelInfo labelInfo = processLabels.namespaceStack.peek().renameMap.get(labelName);
    assertSame(labelName, labelInfo.name);
    assertNull(labelInfo.newName);

    String newLabelName = processLabels.getNameForId(1);
    assertTrue(newLabelName.length() > 0);
    assertFalse(newLabelName.equals(labelName));
  }

  @Test
  public void testProcessLabelsDetermineNewNameForLabelReferencedInSameScope() {
    RenameLabels renameLabels = new RenameLabels(null);
    ProcessLabels processLabels = new ProcessLabels();

    // Test determining a new name for a label that is referenced in the same scope.
    String labelName1 = "label1";
    String labelName2 = "label2";
    processLabels.enterScope(null);
    processLabels.shouldTraverse(null, null, null);
    processLabels.shouldTraverse(null, null, null);
    RenameLabels.LabelInfo labelInfo1 = processLabels.namespaceStack.peek().renameMap.get(labelName1);
    assertSame(labelName1, labelInfo1.name);
    assertNull(labelInfo1.newName);

    String newLabelName1 = processLabels.getNameForId(1);
    assertTrue(newLabelName1.length() > 0);
    assertFalse(newLabelName1.equals(labelName1));

    RenameLabels.LabelInfo labelInfo2 = processLabels.namespaceStack.peek().renameMap.get(labelName2);
    assertSame(labelName2, labelInfo2.name);
    assertNull(labelInfo2.newName);

    String newLabelName2 = processLabels.getNameForId(2);
    assertTrue(newLabelName2.length() > 0);
    assertFalse(newLabelName2.equals(labelName2));

    processLabels.exitScope(null);
  }

  @Test
  public void testProcessLabelsDetermineNewNameForLabelReferencedInOuterScope() {
    RenameLabels renameLabels = new RenameLabels(null);
    ProcessLabels processLabels = new ProcessLabels();

    // Test determining a new name for a label that is referenced in an outer scope.
    String labelName1 = "label1";
    String labelName2 = "label2";
    processLabels.enterScope(null);
    processLabels.shouldTraverse(null, null, null);
    processLabels.enterScope(null);
    processLabels.shouldTraverse(null, null, null);
    RenameLabels.LabelInfo labelInfo1 = processLabels.namespaceStack.peek().renameMap.get(labelName1);
    assertSame(labelName1, labelInfo1.name);
    assertNull(labelInfo1.newName);

    String newLabelName1 = processLabels.getNameForId(1);
    assertTrue(newLabelName1.length() > 0);
    assertFalse(newLabelName1.equals(labelName1));

    processLabels.exitScope(null);
    processLabels.shouldTraverse(null, null, null);
    RenameLabels.LabelInfo labelInfo2 = processLabels.namespaceStack.peek().renameMap.get(labelName2);
    assertSame(labelName2, labelInfo2.name);
    assertNull(labelInfo2.newName);

    String newLabelName2 = processLabels.getNameForId(2);
    assertTrue(newLabelName2.length() > 0);
    assertFalse(newLabelName2.equals(labelName2));
  }

  @Test
  public void testProcessLabelsRenameLabel() {
    RenameLabels renameLabels = new RenameLabels(null);
    ProcessLabels processLabels = new ProcessLabels();

    // Test renaming a label.
    String labelName = "label1";
    processLabels.enterScope(null);
    processLabels.shouldTraverse(null, null, null);
    RenameLabels.LabelInfo labelInfo = processLabels.namespaceStack.peek().renameMap.get(labelName);
    assertSame(labelName, labelInfo.name);
    assertNull(labelInfo.newName);

    String newLabelName = processLabels.getNameForId(1);
    assertTrue(newLabelName.length() > 0);
    assertFalse(newLabelName.equals(labelName));

    processLabels.exitScope(null);

    processLabels.enterScope(null);
    processLabels.shouldTraverse(null, null, null);
    nodeTraversal -> {
      Node labelNode = new Node(Token.LABEL, null, Lists.newArrayList(new Node(Token.IDENT, null, newNode("label1"))));
      processLabels.visit(nodeTraversal, labelNode, null);
      Node nameNode = labelNode.getFirstChild();
      assertSame(newLabelName, nameNode.getString());
    };
  }

  @Test
  public void testProcessLabelsRemoveUnusedLabel() {
    RenameLabels renameLabels = new RenameLabels(null, new DefaultNameSupplier(), false);
    ProcessLabels processLabels = new ProcessLabels();

    // Test removing an unused label.
    String labelName = "label1";
    processLabels.enterScope(null);
    processLabels.shouldTraverse(null, null, null);
    RenameLabels.LabelInfo labelInfo = processLabels.namespaceStack.peek().renameMap.get(labelName);
    assertSame(labelName, labelInfo.name);
    assertNull(labelInfo.newName);

    String newLabelName = processLabels.getNameForId(1);
    assertTrue(newLabelName.length() > 0);
    assertFalse(newLabelName.equals(labelName));

    processLabels.exitScope(null);

    processLabels.enterScope(null);
    processLabels.shouldTraverse(null, null, null);
    nodeTraversal -> {
      Node labelNode = new Node(Token.LABEL, null, Lists.newArrayList(new Node(Token.IDENT, null, newNode("label1"))));
      processLabels.visit(nodeTraversal, labelNode, null);
      assertTrue(processLabels.namespaceStack.peek().renameMap.isEmpty());
    };
  }

  @Test
  public void testProcessLabelsRemoveUnusedLabelAndRenameOthers() {
    RenameLabels renameLabels = new RenameLabels(null, new DefaultNameSupplier(), true);
    ProcessLabels processLabels = new ProcessLabels();

    // Test removing an unused label and renaming others.
    String labelName1 = "label1";
    String labelName2 = "label2";
    processLabels.enterScope(null);
    processLabels.shouldTraverse(null, null, null);
    processLabels.shouldTraverse(null, null, null);

    RenameLabels.LabelInfo labelInfo1 = processLabels.namespaceStack.peek().renameMap.get(labelName1);
    assertSame(labelName1, labelInfo1.name);
    assertNull(labelInfo1.newName);

    String newLabelName1 = processLabels.getNameForId(1);
    assertTrue(newLabelName1.length() > 0);
    assertFalse(newLabelName1.equals(labelName1));

    RenameLabels.LabelInfo labelInfo2 = processLabels.namespaceStack.peek().renameMap.get(labelName2);
    assertSame(labelName2, labelInfo2.name);
    assertNull(labelInfo2.newName);

    String newLabelName2 = processLabels.getNameForId(2);
    assertTrue(newLabelName2.length() > 0);
    assertFalse(newLabelName2.equals(labelName2));

    processLabels.exitScope(null);

    processLabels.enterScope(null);
    processLabels.shouldTraverse(null, null, null);
    nodeTraversal -> {
      Node labelNode1 = new Node(Token.LABEL, null, Lists.newArrayList(new Node(Token.IDENT, null, newNode(newLabelName1))));
      processLabels.visit(nodeTraversal, labelNode1, null);
      Node nameNode1 = labelNode1.getFirstChild();
      assertSame(newLabelName1, nameNode1.getString());

      Node labelNode2 = new Node(Token.LABEL, null, Lists.newArrayList(new Node(Token.IDENT, null, newNode(labelName2))));
      processLabels.visit(nodeTraversal, labelNode2, null);
      Node nameNode2 = labelNode2.getFirstChild();
      assertSame(labelName2, nameNode2.getString());

      processLabels.exitScope(null);

      processLabels.enterScope(null);
      processLabels.shouldTraverse(null, null, null);
      RenameLabels.LabelInfo labelInfo3 = processLabels.namespaceStack.peek().renameMap.get(labelName1);
      assertNull(labelInfo3);
    };
  }

  @Test
  public void testProcessLabelsRenameLabelWithChildren() {
    RenameLabels renameLabels = new RenameLabels(null);
    ProcessLabels processLabels = new ProcessLabels();

    // Test renaming a label with children.
    String labelName = "label1";
    processLabels.enterScope(null);
    processLabels.shouldTraverse(null, null, null);
    RenameLabels.LabelInfo labelInfo = processLabels.namespaceStack.peek().renameMap.get(labelName);
    assertSame(labelName, labelInfo.name);
    assertNull(labelInfo.newName);

    String newLabelName = processLabels.getNameForId(1);
    assertTrue(newLabelName.length() > 0);
    assertFalse(newLabelName.equals(labelName));

    processLabels.exitScope(null);

    processLabels.enterScope(null);
    processLabels.shouldTraverse(null, null, null);
    nodeTraversal -> {
      Node labelNode = new Node(Token.LABEL, null, Lists.newArrayList(new Node(Token.IDENT, null, newNode("label1")), newNode(Token.BLOCK, null, Lists.newArrayList())));
      processLabels.visit(nodeTraversal, labelNode, null);
      Node nameNode = labelNode.getFirstChild();
      assertSame(newLabelName, nameNode.getString());

      Node blockNode = labelNode.getLastChild();
      processLabels.visitLabel(blockNode, null);
      Node labelNodeInBlock = blockNode.getFirstChild();
      assertSame(newLabelName, labelNodeInBlock.getString());
    };
  }
}