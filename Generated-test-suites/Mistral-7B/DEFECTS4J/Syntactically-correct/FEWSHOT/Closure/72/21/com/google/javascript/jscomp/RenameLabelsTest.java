package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class RenameLabelsTest {
  @Test
  public void testRenameLabels() {
    String input = "function foo() {\n" +
        "  a: {\n" +
        "    b: {\n" +
        "      break a;\n" +
        "      c: {\n" +
        "        break a;\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}";

    AbstractCompiler compiler = new Compiler();
    RenameLabels renameLabels = new RenameLabels(compiler);

    Node externs = NodeUtil.createTopLevelNode(input);
    Node root = NodeUtil.getRootNode(externs);

    renameLabels.process(externs, root);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, root, null);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    // Test label names
    assertEquals("a1", processLabels.namespaceStack.peek().renameMap.get("a").id.toString());
    assertEquals("b1", processLabels.namespaceStack.peek().renameMap.get("b").id.toString());

    // Test renamed labels
    assertEquals("a1", NodeUtil.getText(root.getFirstChild().getFirstChild().getFirstChild()));
    assertEquals("b1", NodeUtil.getText(root.getFirstChild().getFirstChild().getLastChild().getLastChild().getLastChild()));
    assertEquals("a1", NodeUtil.getText(root.getFirstChild().getLastChild().getFirstChild()));

    // Test removed labels
    assertNull(NodeUtil.getChild(root.getLastChild().getLastChild(), 0));

    // Test renamed break and continue statements
    assertEquals("a1", NodeUtil.getText(root.getFirstChild().getLastChild().getLastChild().getLastChild().getFirstChild()));
    assertEquals("a1", NodeUtil.getText(root.getFirstChild().getLastChild().getLastChild().getLastChild().getLastChild().getLastChild()));
  }

  @Test
  public void testRemoveUnusedLabels() {
    String input = "function foo() {\n" +
        "  a: {\n" +
        "    b: break a;\n" +
        "  }\n" +
        "}";

    AbstractCompiler compiler = new Compiler();
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), false);

    Node externs = NodeUtil.createTopLevelNode(input);
    Node root = NodeUtil.getRootNode(externs);

    renameLabels.process(externs, root);

    NodeTraversal nodeTraversal = NodeTraversal.create(compiler, root, null);
    ProcessLabels processLabels = new ProcessLabels();
    NodeTraversal.traverse(compiler, root, processLabels);

    // Test removed label
    assertNull(NodeUtil.getChild(root.getFirstChild(), 0));
  }
}

class Compiler extends AbstractCompiler {
  @Override
  public void reportCodeChange() {
    // Do nothing
  }
}

class DefaultNameSupplier implements Supplier<String> {
  final NameGenerator nameGenerator = new NameGenerator(new HashSet<String>(), "", null);

  @Override
  public String get() {
    return nameGenerator.generateNextName();
  }
}

class NameGenerator {
  private final Set<String> usedNames;
  private final String prefix;
  private final String namespace;

  NameGenerator(Set<String> usedNames, String prefix, String namespace) {
    this.usedNames = usedNames;
    this.prefix = prefix;
    this.namespace = namespace;
  }

  String generateNextName() {
    String name = prefix;
    int index = 0;
    while (usedNames.contains(name)) {
      name = prefix + index;
      index++;
    }
    usedNames.add(name);
    return namespace != null ? namespace + name : name;
  }
}

class ProcessLabels extends RenameLabels.ProcessLabels {
  @Override
  public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
    return super.shouldTraverse(nodeTraversal, node, parent) || node.getType() == Token.LABEL;
  }
}