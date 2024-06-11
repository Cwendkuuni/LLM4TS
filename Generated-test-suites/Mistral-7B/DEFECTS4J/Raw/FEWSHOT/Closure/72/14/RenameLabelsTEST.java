package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.jscomp.Supplier;

@RunWith(JUnit4.class)
public class RenameLabelsTest {

  @Test
  public void testRenameLabels() {
    String testCode = "function foo() {\n"
        + "  a: {\n"
        + "    b: {\n"
        + "      break a;\n"
        + "    }\n"
        + "  }\n"
        + "}\n";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void addToDebugLog(String message) {
        // Do nothing
      }
    };

    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);

    Node root = RhinoNodeFactory.createNodeFromString(testCode);
    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.traverse(renameLabels);

    assertNotNull(renameLabels.getNameForId(1));
    assertNotNull(renameLabels.getNameForId(2));

    String resultCode = RhinoNodePrinter.printToNodeList(root).get(0).toString();
    assertNotNull(resultCode);

    String expectedCode = "function foo() {\n"
        + "  c: {\n"
        + "    d: {\n"
        + "      break c;\n"
        + "    }\n"
        + "  }\n"
        + "}\n";

    assertNotNull(expectedCode);
    assertEquals(expectedCode, resultCode);
  }

  @Test
  public void testRemoveUnusedLabels() {
    String testCode = "function foo() {\n"
        + "  a: {\n"
        + "    b: {\n"
        + "      break a;\n"
        + "      b: 1;\n"
        + "    }\n"
        + "  }\n"
        + "}\n";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void addToDebugLog(String message) {
        // Do nothing
      }
    };

    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);

    Node root = RhinoNodeFactory.createNodeFromString(testCode);
    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.traverse(renameLabels);

    assertNotNull(renameLabels.getNameForId(1));
    assertNotNull(renameLabels.getNameForId(2));

    String resultCode = RhinoNodePrinter.printToNodeList(root).get(0).toString();
    assertNotNull(resultCode);

    String expectedCode = "function foo() {\n"
        + "  c: {\n"
        + "    break c;\n"
        + "    d: 1;\n"
        + "  }\n"
        + "}\n";

    assertNotNull(expectedCode);
    assertEquals(expectedCode, resultCode);

    // Test that unused labels are removed
    String testCodeWithUnusedLabel = "function foo() {\n"
        + "  a: {\n"
        + "    b: {\n"
        + "      break a;\n"
        + "      c: {\n"
        + "        d: 1;\n"
        + "      }\n"
        + "    }\n"
        + "  }\n"
        + "  e: {\n"
        + "    f: 1;\n"
        + "  }\n"
        + "}\n";

    Node rootWithUnusedLabel = RhinoNodeFactory.createNodeFromString(testCodeWithUnusedLabel);
    NodeTraversal nodeTraversalWithUnusedLabel = new NodeTraversal(rootWithUnusedLabel, compiler);
    nodeTraversalWithUnusedLabel.traverse(renameLabels);

    assertNull(renameLabels.getNameForId(3));

    String resultCodeWithUnusedLabel = RhinoNodePrinter.printToNodeList(rootWithUnusedLabel).get(0).toString();
    assertNotNull(resultCodeWithUnusedLabel);

    String expectedCodeWithUnusedLabel = "function foo() {\n"
        + "  c: {\n"
        + "    break c;\n"
        + "    d: 1;\n"
        + "  }\n"
        + "  e: {\n"
        + "    f: 1;\n"
        + "  }\n"
        + "}\n";

    assertNotNull(expectedCodeWithUnusedLabel);
    assertEquals(expectedCodeWithUnusedLabel, resultCodeWithUnusedLabel);
  }

  @Test
  public void testNestedLabels() {
    String testCode = "function foo() {\n"
        + "  a: {\n"
        + "    b: {\n"
        + "      c: {\n"
        + "        d: {\n"
        + "          break c;\n"
        + "        }\n"
        + "      }\n"
        + "    }\n"
        + "  }\n"
        + "}\n";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void addToDebugLog(String message) {
        // Do nothing
      }
    };

    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);

    Node root = RhinoNodeFactory.createNodeFromString(testCode);
    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.traverse(renameLabels);

    assertNotNull(renameLabels.getNameForId(1));
    assertNotNull(renameLabels.getNameForId(2));
    assertNotNull(renameLabels.getNameForId(3));
    assertNotNull(renameLabels.getNameForId(4));

    String resultCode = RhinoNodePrinter.printToNodeList(root).get(0).toString();
    assertNotNull(resultCode);

    String expectedCode = "function foo() {\n"
        + "  e: {\n"
        + "    f: {\n"
        + "      g: {\n"
        + "        h: {\n"
        + "          break g;\n"
        + "        }\n"
        + "      }\n"
        + "    }\n"
        + "  }\n"
        + "}\n";

    assertNotNull(expectedCode);
    assertEquals(expectedCode, resultCode);
  }

  @Test
  public void testLabelNamespaces() {
    String testCode = "function foo() {\n"
        + "  a: {\n"
        + "    b: {\n"
        + "      break a;\n"
        + "    }\n"
        + "  }\n"
        + "  a: {\n"
        + "    b: {\n"
        + "      break a;\n"
        + "    }\n"
        + "  }\n"
        + "}\n";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void addToDebugLog(String message) {
        // Do nothing
      }
    };

    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    Map<String, Integer> labelDepths = Maps.newHashMap();
    int labelDepth = 0;

    Node root = RhinoNodeFactory.createNodeFromString(testCode);
    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.traverse(renameLabels, new ProcessLabelDepthsCallback(labelDepths, labelDepth++));

    assertNotNull(renameLabels.getNameForId(1));
    assertNotNull(renameLabels.getNameForId(2));

    String resultCode = RhinoNodePrinter.printToNodeList(root).get(0).toString();
    assertNotNull(resultCode);

    String expectedCode = "function foo() {\n"
        + "  i: {\n"
        + "    j: {\n"
        + "      break i;\n"
        + "    }\n"
        + "  }\n"
        + "  i: {\n"
        + "    j: {\n"
        + "      break i;\n"
        + "    }\n"
        + "  }\n"
        + "}\n";

    assertNotNull(expectedCode);
    assertEquals(expectedCode, resultCode);

    assertNotNull(labelDepths.get("a"));
    assertNotNull(labelDepths.get("b"));
    assertEquals(2, labelDepths.get("a"));
    assertEquals(1, labelDepths.get("b"));
  }

  private static class ProcessLabelDepthsCallback implements ScopedCallback {
    private final Map<String, Integer> labelDepths;
    private final int labelDepth;

    ProcessLabelDepthsCallback(Map<String, Integer> labelDepths, int labelDepth) {
      this.labelDepths = labelDepths;
      this.labelDepth = labelDepth;
    }

    @Override
    public void enterScope(NodeTraversal nodeTraversal) {
      labelDepth++;
    }

    @Override
    public void exitScope(NodeTraversal nodeTraversal) {
      labelDepth--;
    }

    @Override
    public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
      if (node.getType() == Token.LABEL) {
        String name = node.getFirstChild().getString();
        labelDepths.put(name, labelDepth);
      }
      return true;
    }
  }
}