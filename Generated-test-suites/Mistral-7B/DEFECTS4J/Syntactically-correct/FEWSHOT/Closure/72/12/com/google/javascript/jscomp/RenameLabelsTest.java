package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.rhino.Token;

public class RenameLabelsTest {
  private static final String TEST_CODE = "function foo() {" +
      "  a: { break a; }}" +
      "  b: { continue a; }" +
      "  c: { break c; }" +
      "  d: { continue d; }" +
      "  e: { break e; }" +
      "  f: { continue f; }" +
      "}";

  @Test
  public void testRenameLabels() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = ParserTestUtil.parse(TEST_CODE);
    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);

    renameLabels.process(null, root);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traverseAndCheckRenamedLabels(traversal);
  }

  private void traverseAndCheckRenamedLabels(NodeTraversal nodeTraversal) {
    Node currentNode = nodeTraversal.current();
    while (currentNode != null) {
      switch (currentNode.getType()) {
        case Token.LABEL:
          LabelInfo labelInfo = getLabelInfo(currentNode);
          if (labelInfo != null) {
            String newLabelName = getNameForId(labelInfo.id);
            String oldLabelName = currentNode.getFirstChild().getString();
            assertEquals(newLabelName, oldLabelName);
            assertFalse(labelInfo.referenced);
          }
          break;
        case Token.BREAK:
        case Token.CONTINUE:
          Node child = currentNode.getFirstChild();
          if (child != null) {
            LabelInfo labelInfo = getLabelInfo(child.getString());
            if (labelInfo != null) {
              String newLabelName = getNameForId(labelInfo.id);
              String oldLabelName = child.getString();
              assertEquals(newLabelName, oldLabelName);
              assertTrue(labelInfo.referenced);
            }
          }
          break;
      }
      nodeTraversal.traverseDown();
      currentNode = nodeTraversal.current();
    }
  }

  private LabelInfo getLabelInfo(Node node) {
    Node parent = node.getParent();
    while (parent != null) {
      if (parent.getType() == Token.BLOCK) {
        Block block = (Block) parent;
        LabelNamespace labelNamespace = block.labelNamespace;
        return labelNamespace.renameMap.get(node.getFirstChild().getString());
      }
      parent = parent.getParent();
    }
    return null;
  }

  private String getNameForId(int id) {
    RenameLabels renameLabels = new RenameLabels(null);
    return renameLabels.names.get(id - 1);
  }

  @Test
  public void testRemoveUnusedLabels() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), false);
    Node root = ParserTestUtil.parse(TEST_CODE);
    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);

    renameLabels.process(null, root);
    NodeTraversal traversal = new NodeTraversal(root, compiler);
    traverseAndCheckRenamedLabels(traversal);

    // Remove unused labels.
    renameLabels.process(root, null);
    Node newRoot = nodeTraversal.current();
    assertTrue(newRoot.getChildCount() < root.getChildCount());
  }

  private static class DefaultNameSupplier implements RenameLabels.Supplier<String> {
    private final NameGenerator nameGenerator = new NameGenerator(Sets.newHashSet(), "", null);

    @Override
    public String get() {
      return nameGenerator.generateNextName();
    }
  }

  private static class NameGenerator {
    private final Set<String> usedNames;
    private final String prefix;
    private final int depth;

    NameGenerator(Set<String> usedNames, String prefix, int depth) {
      this.usedNames = usedNames;
      this.prefix = prefix;
      this.depth = depth;
    }

    String generateNextName() {
      String name = prefix + Integer.toString(depth++);
      while (usedNames.contains(name)) {
        name += "_";
      }
      usedNames.add(name);
      return name;
    }
  }
}