package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

public class RenameLabelsTest {

  @Test
  public void testRenameLabels() {
    String input = "function foo() {" +
                   "  a: {" +
                   "    b: {" +
                   "      c: break a;" +
                   "    }" +
                   "  }" +
                   "  a: {" +
                   "    b: break a;" +
                   "  }" +
                   "}";

    Node node = AbstractCompiler.parseTree(input, null, null);

    RenameLabels renameLabels = new RenameLabels(null);
    renameLabels.process(null, node);

    NodeTraversal nodeTraversal = new NodeTraversal(node, null);
    ProcessLabels processLabels = new ProcessLabels();
    nodeTraversal.traverse(null, node, processLabels);

    int depth = 1;
    String expectedLabelName = "a";
    String actualLabelName = null;

    Node currentNode = node;
    while (currentNode != null) {
      if (currentNode.getType() == Token.LABEL) {
        Node nameNode = currentNode.getFirstChild();
        actualLabelName = nameNode.getString();
        assertEquals("Label name at depth " + depth + " should be: ", expectedLabelName, actualLabelName);
        break;
      }
      currentNode = currentNode.getPreviousSibling();
      depth++;
    }

    expectedLabelName = "b";
    currentNode = node;
    boolean labelFound = false;
    while (currentNode != null) {
      if (currentNode.getType() == Token.BREAK || currentNode.getType() == Token.CONTINUE) {
        Node nameNode = currentNode.getFirstChild();
        if (nameNode != null) {
          actualLabelName = nameNode.getString();
          if (actualLabelName.equals(expectedLabelName)) {
            labelFound = true;
            break;
          }
        }
      }
      currentNode = currentNode.getNextSibling();
    }

    assertTrue("Label " + expectedLabelName + " not found", labelFound);

    // Test label removal
    input = "function foo() {" +
           "  a: {" +
           "    b: break a;" +
           "  }" +
           "  a: {" +
           "  }" +
           "}";

    node = AbstractCompiler.parseTree(input, null, null);
    renameLabels = new RenameLabels(null);
    renameLabels.removeUnused = false;
    renameLabels.process(null, node);

    nodeTraversal = new NodeTraversal(node, null);
    processLabels = new ProcessLabels();
    nodeTraversal.traverse(null, node, processLabels);

    currentNode = node;
    labelFound = false;
    while (currentNode != null) {
      if (currentNode.getType() == Token.LABEL) {
        Node nameNode = currentNode.getFirstChild();
        if (nameNode != null && nameNode.getString().equals("a")) {
          // The second label "a" should have been removed
          assertNull("Unexpected label 'a' found", currentNode);
          return;
        }
        break;
      }
      currentNode = currentNode.getPreviousSibling();
    }
  }

  private static boolean containsValue(Map<String, Object> map, Object value) {
    return map.values().contains(value);
  }

  private static void assertLabelMapEquals(Map<String, LabelInfo> expected, Map<String, LabelInfo> actual) {
    assertEquals("Unexpected number of labels", expected.size(), actual.size());
    for (Map.Entry<String, LabelInfo> entry : expected.entrySet()) {
      String labelName = entry.getKey();
      LabelInfo expectedLabelInfo = entry.getValue();
      LabelInfo actualLabelInfo = actual.get(labelName);
      assertEquals("Unexpected label info for label '" + labelName + "'",
          expectedLabelInfo.id, actualLabelInfo.id);
      assertEquals("Unexpected label 'referenced' status for label '" + labelName + "'",
          expectedLabelInfo.referenced, actualLabelInfo.referenced);
    }
  }

  @Test
  public void testLabelNamespaces() {
    String input = "function foo() {" +
                   "  a: {" +
                   "    b: {" +
                   "      c: break a;" +
                   "    }" +
                   "  }" +
                   "  a: {" +
                   "    b: break a;" +
                   "  }" +
                   "}";

    Node node = AbstractCompiler.parseTree(input, null, null);

    RenameLabels renameLabels = new RenameLabels(null);
    renameLabels.process(null, node);

    NodeTraversal nodeTraversal = new NodeTraversal(node, null);
    ProcessLabels processLabels = new ProcessLabels();
    nodeTraversal.traverse(null, node, processLabels);

    Map<String, LabelInfo> expectedLabelMap = Maps.newHashMap();
    LabelInfo labelInfo = new LabelInfo(1);
    expectedLabelMap.put("a", labelInfo);
    LabelInfo labelInfo2 = new LabelInfo(2);
    expectedLabelMap.put("b", labelInfo2);

    LabelNamespace labelNamespace = processLabels.namespaceStack.peek();
    Map<String, LabelInfo> actualLabelMap = labelNamespace.renameMap;
    assertLabelMapEquals(expectedLabelMap, actualLabelMap);

    // Test label removal
    input = "function foo() {" +
           "  a: {" +
           "    b: break a;" +
           "  }" +
           "  a: {" +
           "  }" +
           "}";

    node = AbstractCompiler.parseTree(input, null, null);
    renameLabels = new RenameLabels(null);
    renameLabels.removeUnused = true;
    renameLabels.process(null, node);

    nodeTraversal = new NodeTraversal(node, null);
    processLabels = new ProcessLabels();
    nodeTraversal.traverse(null, node, processLabels);

    labelNamespace = processLabels.namespaceStack.peek();
    actualLabelMap = labelNamespace.renameMap;
    assertLabelMapEquals(Maps.newHashMap(), actualLabelMap);
  }
}