package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.rhino.NodeUtil;

@RunWith(JUnit4.class)
public class RenameLabelsTest {

  @Test
  public void testRenameLabels() {
    String input = "function foo() {" +
        "  a: {" +
        "    b: {" +
        "      break a;" +
        "    }" +
        "  }" +
        "  a: {" +
        "    b: break a;" +
        "  }" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // Intentionally empty.
      }
    };

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = AbstractSyntaxTree.getAbstractSyntaxTree(input);
    renameLabels.process(null, root);

    String expectedOutput = "function foo() {" +
        "  " + renameLabels.names.get(0) + ": {" +
        "    " + renameLabels.names.get(1) + ": {" +
        "      break " + renameLabels.names.get(0) + ";" +
        "    }" +
        "  }" +
        "  " + renameLabels.names.get(0) + ": {" +
        "    " + renameLabels.names.get(1) + ": break " + renameLabels.names.get(0) + ";" +
        "  }" +
        "}";

    assertEquals(expectedOutput, AbstractSyntaxTree.getSource(root));
  }

  @Test
  public void testRemoveUnusedLabels() {
    String input = "function foo() {" +
        "  a: {" +
        "    b: break a;" +
        "  }" +
        "  a: {" +
        "    c: {" +
        "      break a;" +
        "    }" +
        "  }" +
        "  b: function bar() {" +
        "    break a;" +
        "  }" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // Intentionally empty.
      }
    };

    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), false);
    Node root = AbstractSyntaxTree.getAbstractSyntaxTree(input);
    renameLabels.process(null, root);

    String expectedOutput = "function foo() {" +
        "  a: {" +
        "    b: break a;" +
        "  }" +
        "  a: {" +
        "    c: {" +
        "      break a;" +
        "    }" +
        "  }" +
        "  b: function bar() {" +
        "  }" +
        "}";

    assertEquals(expectedOutput, AbstractSyntaxTree.getSource(root));

    RenameLabels renameLabels2 = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    Node root2 = AbstractSyntaxTree.getAbstractSyntaxTree(input);
    renameLabels2.process(null, root2);

    String expectedOutput2 = "function foo() {" +
        "  b: {" +
        "    break b;" +
        "  }" +
        "  c: {" +
        "    break b;" +
        "  }" +
        "  b: function bar() {" +
        "  }" +
        "}";

    assertEquals(expectedOutput2, AbstractSyntaxTree.getSource(root2));
  }

  @Test
  public void testNestedLabels() {
    String input = "function foo() {" +
        "  a: {" +
        "    b: {" +
        "      c: {" +
        "        break a;" +
        "      }" +
        "    }" +
        "  }" +
        "  a: {" +
        "    b: break a;" +
        "  }" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // Intentionally empty.
      }
    };

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = AbstractSyntaxTree.getAbstractSyntaxTree(input);
    renameLabels.process(null, root);

    String expectedOutput = "function foo() {" +
        "  " + renameLabels.names.get(0) + ": {" +
        "    " + renameLabels.names.get(1) + ": {" +
        "      " + renameLabels.names.get(2) + ": {" +
        "        break " + renameLabels.names.get(0) + ";" +
        "      }" +
        "    }" +
        "  }" +
        "  " + renameLabels.names.get(0) + ": {" +
        "    " + renameLabels.names.get(1) + ": break " + renameLabels.names.get(0) + ";" +
        "  }" +
        "}";

    assertEquals(expectedOutput, AbstractSyntaxTree.getSource(root));
  }

  @Test
  public void testLabelNamespaces() {
    String input = "function foo() {" +
        "  var a = function a() { a: {" +
        "      break a;" +
        "    }}" +
        "  }" +
        "  function bar() {" +
        "    a: {" +
        "      break a;" +
        "    }" +
        "  }" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // Intentionally empty.
      }
    };

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = AbstractSyntaxTree.getAbstractSyntaxTree(input);
    renameLabels.process(null, root);

    String expectedOutput = "function foo() {" +
        "  var a = function a() {" +
        "    " + renameLabels.names.get(0) + ": {" +
        "        break " + renameLabels.names.get(0) + ";" +
        "      }" +
        "  }" +
        "  function bar() {" +
        "    " + renameLabels.names.get(1) + ": {" +
        "        break " + renameLabels.names.get(1) + ";" +
        "      }" +
        "  }" +
        "}";

    assertEquals(expectedOutput, AbstractSyntaxTree.getSource(root));
  }

  @Test
  public void testEmptyLabelNamespace() {
    String input = "function foo() {}";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // Intentionally empty.
      }
    };

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = AbstractSyntaxTree.getAbstractSyntaxTree(input);
    renameLabels.process(null, root);

    String expectedOutput = "function foo() {}";

    assertEquals(expectedOutput, AbstractSyntaxTree.getSource(root));
  }

  @Test
  public void testBreakAndContinueLabels() {
    String input = "function foo() {" +
        "  a: {" +
        "    b: break a;" +
        "    continue a;" +
        "  }" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // Intentionally empty.
      }
    };

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = AbstractSyntaxTree.getAbstractSyntaxTree(input);
    renameLabels.process(null, root);

    String expectedOutput = "function foo() {" +
        "  " + renameLabels.names.get(0) + ": {" +
        "    " + renameLabels.names.get(1) + ": break " + renameLabels.names.get(0) + ";" +
        "    continue " + renameLabels.names.get(0) + ";" +
        "  }" +
        "}";

    assertEquals(expectedOutput, AbstractSyntaxTree.getSource(root));
  }

  @Test
  public void testRemoveBlockWithEmptyLabel() {
    String input = "function foo() {" +
        "  a: {" +
        "  }" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // Intentionally empty.
      }
    };

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = AbstractSyntaxTree.getAbstractSyntaxTree(input);
    renameLabels.process(null, root);

    String expectedOutput = "function foo() {}";

    assertEquals(expectedOutput, AbstractSyntaxTree.getSource(root));
  }

  @Test
  public void testRemoveBlockWithEmptyLabelAndStatements() {
    String input = "function foo() {" +
        "  a: {" +
        "    x = 42;" +
        "  }" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // Intentionally empty.
      }
    };

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = AbstractSyntaxTree.getAbstractSyntaxTree(input);
    renameLabels.process(null, root);

    String expectedOutput = "function foo() {" +
        "}";

    assertEquals(expectedOutput, AbstractSyntaxTree.getSource(root));
  }

  @Test
  public void testRemoveBlockWithEmptyLabelAndStatementsAndStatementsInside() {
    String input = "function foo() {" +
        "  a: {" +
        "    x = 42;" +
        "    y = 21;" +
        "  }" +
        "  a: {" +
        "    z = 13;" +
        "  }" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // Intentionally empty.
      }
    };

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = AbstractSyntaxTree.getAbstractSyntaxTree(input);
    renameLabels.process(null, root);

    String expectedOutput = "function foo() {}";

    assertEquals(expectedOutput, AbstractSyntaxTree.getSource(root));
  }

  @Test
  public void testMergeBlocks() {
    String input = "function foo() {" +
        "  a: {" +
        "    x = 42;" +
        "    y = 21;" +
        "  }" +
        "  a: {" +
        "    z = 13;" +
        "  }" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler() {
      @Override
      public void reportCodeChange() {
        // Intentionally empty.
      }
    };

    RenameLabels renameLabels = new RenameLabels(compiler);
    Node root = AbstractSyntaxTree.getAbstractSyntaxTree(input);
    renameLabels.process(null, root);

    String expectedOutput = "function foo() {" +
        "  x = 42;" +
        "  y = 21;" +
        "  z = 13;" +
        "}";

    Node result = AbstractSyntaxTree.getAbstractSyntaxTree(AbstractSyntaxTree.getSource(root));
    NodeUtil.tryMergeBlock(result.getFirstChild().getLastChild());
    assertEquals(expectedOutput, AbstractSyntaxTree.getSource(result));
  }
}

class DefaultNameSupplier implements com.google.common.base.Supplier<String> {
  private final NameGenerator nameGenerator = new NameGenerator(new HashSet<String>(), "", null);

  @Override
  public String get() {
    return nameGenerator.generateNextName();
  }
}

class NameGenerator {
  private final Multimap<String, String> usedNames = ArrayListMultimap.create();
  private final String prefix;
  private final String suffix;
  private int currentIndex;

  NameGenerator(Set<String> usedNames, String prefix, String suffix) {
    this.usedNames = usedNames;
    this.prefix = prefix;
    this.suffix = suffix;
    this.currentIndex = 0;
  }

  String generateNextName() {
    String name;
    if (prefix != null && suffix != null) {
      name = prefix + currentIndex++ + suffix;
    } else if (prefix != null) {
      name = prefix + currentIndex++;
    } else if (suffix != null) {
      name = currentIndex++ + suffix;
    } else {
      name = String.valueOf(currentIndex++);
    }

    if (usedNames.containsValue(name)) {
      String lastUsed = (String) usedNames.get(usedNames.size() - 1).getValue();
      int lastUsedIndex = Integer.parseInt(lastUsed.substring(lastUsed.length() - 1));
      currentIndex = lastUsedIndex + 1;
      name = generateNextName();
    }

    usedNames.put(name, name);
    return name;
  }
}