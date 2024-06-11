package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.rhino.NodeUtilities;

import java.util.Deque;
import java.util.Map;
import java.util.Set;

public class RenameLabelsTest {
  @Test
  public void testRenameLabels() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Supplier<String> nameSupplier = spy(new RenameLabels.DefaultNameSupplier());

    // Test with no unreferenced labels removed.
    RenameLabels renameLabels = new RenameLabels(compiler, nameSupplier, false);

    Node root = createTestNode();
    renameLabels.process(null, root);
    assertLabelRenamed("label1", "label1_0");
    assertLabelRenamed("label2", "label2_0");
    assertLabelRenamed("label3", "label3_0");
    assertLabelRenamed("label4", "label4_0");
    assertLabelRenamed("label5", "label5_0");

    // Test with unreferenced labels removed.
    renameLabels = new RenameLabels(compiler, nameSupplier, true);
    root = createTestNode();
    renameLabels.process(null, root);
    assertLabelRenamed("label1", "label1_0");
    assertLabelRenamed("label2", "label2_1");
    assertLabelRenamed("label3", "label3_2");
    assertLabelRemoved("label4");
    assertLabelRemoved("label5");
  }

  private Node createTestNode() {
    Node root = NodeUtilities.createBlock();

    Node label1 = NodeUtilities.createLabelNode("label1");
    Node label1Ref = NodeUtilities.createBreak(label1);
    Node label2 = NodeUtilities.createLabelNode("label2");
    Node label2Ref = NodeUtilities.createBreak(label2);
    Node label3 = NodeUtilities.createLabelNode("label3");
    Node label3Ref = NodeUtilities.createBreak(label3);
    Node label4 = NodeUtilities.createLabelNode("label4");
    Node label5 = NodeUtilities.createLabelNode("label5");
    Node label5Ref = NodeUtilities.createBreak(label5);
    Node block = NodeUtilities.createBlock();
    block.addChild(label1);
    block.addChild(label1Ref);
    block.addChild(label2);
    block.addChild(label2Ref);
    block.addChild(label3);
    block.addChild(label3Ref);
    block.addChild(label4);
    block.addChild(label5);
    block.addChild(label5Ref);
    root.addChild(block);
    return root;
  }

  private void assertLabelRenamed(String oldLabelName, String newLabelName) {
    Node labelNode = findLabelNode("label" + oldLabelName);
    assertNotNull(labelNode);
    String labelName = labelNode.getFirstChild().getString();
    assertEquals(newLabelName, labelName);
  }

  private void assertLabelRemoved(String labelName) {
    Node labelNode = findLabelNode("label" + labelName);
    assertNull(labelNode);
  }

  private Node findLabelNode(String labelName) {
    Node root = NodeUtilities.createBlock();
    NodeTraversal nodeTraversal = new NodeTraversal(null, root, null);

    RenameLabels.ProcessLabels processLabels = new RenameLabels.ProcessLabels();
    nodeTraversal.setScopedCallback(processLabels);
    NodeUtilities.traverse(nodeTraversal, createTestNode());

    for (Map.Entry<String, LabelInfo> entry : processLabels.namespaceStack.peek().renameMap.entrySet()) {
      if (entry.getKey().equals(labelName)) {
        return entry.getValue().node;
      }
    }

    return null;
  }
}

class NameGenerator {
  private final Set<String> usedNames;
  private final String prefix;
  private final String separator;
  private int counter;

  NameGenerator(Set<String> usedNames, String prefix, String separator) {
    this.usedNames = usedNames;
    this.prefix = prefix;
    this.separator = separator;
    this.counter = 0;
  }

  String generateNextName() {
    while (true) {
      String name = prefix + separator + counter++;
      if (!usedNames.contains(name)) {
        usedNames.add(name);
        return name;
      }
    }
  }
}

class LabelInfo {
  boolean referenced;
  final int id;
  Node node;

  LabelInfo(int id) {
    this.id = id;
  }
}

class LabelNamespace {
  final Map<String, LabelInfo> renameMap = ArrayListMultimap.create();
}