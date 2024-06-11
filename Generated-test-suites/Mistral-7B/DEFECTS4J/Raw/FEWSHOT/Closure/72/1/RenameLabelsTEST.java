package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessFlags;
import com.google.javascript.jscomp.RenameLabels;
import com.google.javascript.rhino.Token;

public class RenameLabelsTest {

  @Test
  public void testRenameLabels() {
    String jsCode = "function bar() {\n"
        + "  a: {\n"
        + "    b: function() {\n"
        + "    };\n"
        + "  }\n"
        + "  a: {\n"
        + "    break a;\n"
        + "  }\n"
        + "}";

    ProcessFlags processFlags = new ProcessFlags();
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);

    Node externs = Node.externalCall("jsComp", null, null);
    Node root = Node.parse(jsCode);

    renameLabels.process(externs, root);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, root, new ProcessLabels());
    nodeTraversal.traverse();

    assertEquals("function bar() {\n"
        + "  a: {\n"
        + "    b: function() {}\n"
        + "  }\n"
        + "  c: {\n"
        + "    break c;\n"
        + "  }\n"
        + "}", Node.toString(root));
  }

  @Test
  public void testRenameLabelsWithUnusedLabel() {
    String jsCode = "function bar() {\n"
        + "  a: {\n"
        + "    b: function() {\n"
        + "    };\n"
        + "  }\n"
        + "  a: {}\n"
        + "}";

    ProcessFlags processFlags = new ProcessFlags();
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);

    Node externs = Node.externalCall("jsComp", null, null);
    Node root = Node.parse(jsCode);

    renameLabels.process(externs, root);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, root, new ProcessLabels());
    nodeTraversal.traverse();

    assertEquals("function bar() {\n"
        + "  a: {\n"
        + "    b: function() {}\n"
        + "  }\n"
        + "  c: {}\n"
        + "}", Node.toString(root));

    // Check that unused label was removed
    Map<String, LabelInfo> renameMap = Maps.newHashMap(renameLabels.processLabels.namespaceStack.peek().renameMap);
    assertNull(renameMap.get("a"));
  }

  @Test
  public void testRenameLabelsWithNestedLabels() {
    String jsCode = "function bar() {\n"
        + "  a: {\n"
        + "    b: {\n"
        + "      c: function() {\n"
        + "      };\n"
        + "    }\n"
        + "  }\n"
        + "  a: {\n"
        + "    break a;\n"
        + "  }\n"
        + "}";

    ProcessFlags processFlags = new ProcessFlags();
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);

    Node externs = Node.externalCall("jsComp", null, null);
    Node root = Node.parse(jsCode);

    renameLabels.process(externs, root);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, root, new ProcessLabels());
    nodeTraversal.traverse();

    assertEquals("function bar() {\n"
        + "  a: {\n"
        + "    b: {\n"
        + "      c: function() {}\n"
        + "    }\n"
        + "  }\n"
        + "  d: {\n"
        + "    break d;\n"
        + "  }\n"
        + "}", Node.toString(root));
  }

  @Test
  public void testRenameLabelsWithNestedBreaks() {
    String jsCode = "function bar() {\n"
        + "  a: {\n"
        + "    b: function() {\n"
        + "      break a;\n"
        + "    }\n"
        + "  }\n"
        + "  a: {\n"
        + "    break a;\n"
        + "  }\n"
        + "}";

    ProcessFlags processFlags = new ProcessFlags();
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);

    Node externs = Node.externalCall("jsComp", null, null);
    Node root = Node.parse(jsCode);

    renameLabels.process(externs, root);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, root, new ProcessLabels());
    nodeTraversal.traverse();

    assertEquals("function bar() {\n"
        + "  a: {\n"
        + "    b: function() {}\n"
        + "  }\n"
        + "  e: {\n"
        + "    break e;\n"
        + "  }\n"
        + "}", Node.toString(root));
  }

  @Test
  public void testRenameLabelsWithNestedBreaksAndContinues() {
    String jsCode = "function bar() {\n"
        + "  a: {\n"
        + "    b: function() {\n"
        + "      break a;\n"
        + "    }\n"
        + "  }\n"
        + "  a: {\n"
        + "    continue a;\n"
        + "  }\n"
        + "  a: {\n"
        + "    break a;\n"
        + "  }\n"
        + "}";

    ProcessFlags processFlags = new ProcessFlags();
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);

    Node externs = Node.externalCall("jsComp", null, null);
    Node root = Node.parse(jsCode);

    renameLabels.process(externs, root);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, null, root, new ProcessLabels());
    nodeTraversal.traverse();

    assertEquals("function bar() {\n"
        + "  a: {\n"
        + "    b: function() {}\n"
        + "  }\n"
        + "  f: {\n"
        + "    continue f;\n"
        + "  }\n"
        + "  g: {\n"
        + "    break g;\n"
        + "  }\n"
        + "}", Node.toString(root));
  }

  @Test
  public void testRenameLabelsWithKeywordLabel() {
    String jsCode = "function bar() {\n"
        + "  function break() {}\n"
        + "  a: {\n"
        + "    break:\n"
        + "  }\n"
        + "}";

    ProcessFlags processFlags = new ProcessFlags();
    AbstractCompiler compiler = new AbstractCompiler();
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);

    Node externs = Node.externalCall("jsComp", null, null);
    Node root = Node.parse(jsCode);

    try {
      renameLabels.process(externs, root);
      fail("Expected runtime error when trying to rename a keyword label.");
    } catch (RuntimeException e) {
      // Expected
    }

    assertEquals("function bar() {\n"
        + "  function break() {}\n"
        + "  a: {\n"
        + "    label:\n"
        + "  }\n"
        + "}", Node.toString(root));
  }
}

class DefaultNameSupplier implements Supplier<String> {
  final NameGenerator nameGenerator = new NameGenerator(new HashSet<String>(), "", null);

  @Override
  public String get() {
    return nameGenerator.generateNextName();
  }
}