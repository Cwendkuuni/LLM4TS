package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RenameLabelsTest {
  @Test
  public void testRenameLabels() {
    RenameLabels renameLabels = new RenameLabels(null);

    String input = "function foo() {\n" +
        "  a: {\n" +
        "    b: {\n" +
        "      break a;\n" +
        "    }\n" +
        "  }\n" +
        "  a: {\n" +
        "    b: break a;\n" +
        "  }\n" +
        "}";

    Node externs = NodeFactory.createRootNode(input);
    Node root = NodeUtil.getFunctionNode(externs, "foo");

    renameLabels.process(externs, root);

    assertEquals("function foo() {\n" +
        "  a: {\n" +
        "    b: {\n" +
        "      continue 0;\n" +
        "    }\n" +
        "  }\n" +
        "  a: {\n" +
        "    continue 0;\n" +
        "  }\n" +
        "}", NodePrinter.printNode(root));

    String inputWithDuplicateLabel = "function foo() {\n" +
        "  a: {\n" +
        "    b: {\n" +
        "      break a;\n" +
        "    }\n" +
        "  }\n" +
        "  a: {\n" +
        "    b: break a;\n" +
        "  }\n" +
        "  a: {\n" +
        "    b: break a;\n" +
        "  }\n" +
        "}";

    Node externsWithDuplicateLabel = NodeFactory.createRootNode(inputWithDuplicateLabel);
    Node rootWithDuplicateLabel = NodeUtil.getFunctionNode(externsWithDuplicateLabel, "foo");

    renameLabels.process(externsWithDuplicateLabel, rootWithDuplicateLabel);

    assertEquals("function foo() {\n" +
        "  a: {\n" +
        "    b: continue 1;\n" +
        "  }\n" +
        "  a: {\n" +
        "    b: continue 2;\n" +
        "  }\n" +
        "  a: {\n" +
        "    b: continue 2;\n" +
        "  }\n" +
        "}", NodePrinter.printNode(rootWithDuplicateLabel));

    String inputWithUnreferencedLabel = "function foo() {\n" +
        "  a: {\n" +
        "  }\n" +
        "  a: {\n" +
        "    break a;\n" +
        "  }\n" +
        "}";

    Node externsWithUnreferencedLabel = NodeFactory.createRootNode(inputWithUnreferencedLabel);
    Node rootWithUnreferencedLabel = NodeUtil.getFunctionNode(externsWithUnreferencedLabel, "foo");

    renameLabels.process(externsWithUnreferencedLabel, rootWithUnreferencedLabel);

    assertEquals("function foo() {\n" +
        "}", NodePrinter.printNode(rootWithUnreferencedLabel));
  }

  @Test
  public void testRenameLabelsWithNullCompiler() {
    Supplier<String> nameSupplier = new DefaultNameSupplier();
    RenameLabels renameLabels = new RenameLabels(null, nameSupplier, true);

    String input = "function foo() {\n" +
        "  a: {\n" +
        "    b: {\n" +
        "      break a;\n" +
        "    }\n" +
        "  }\n" +
        "  a: {\n" +
        "    b: break a;\n" +
        "  }\n" +
        "}";

    Node externs = NodeFactory.createRootNode(input);
    Node root = NodeUtil.getFunctionNode(externs, "foo");

    renameLabels.process(externs, root);

    assertNull(renameLabels);
  }

  @Test
  public void testRenameLabelsWithRemoveUnusedFalse() {
    RenameLabels renameLabels = new RenameLabels(null);

    String input = "function foo() {\n" +
        "  a: {\n" +
        "    b: {\n" +
        "      break a;\n" +
        "    }\n" +
        "  }\n" +
        "  a: {\n" +
        "    b: break a;\n" +
        "  }\n" +
        "}";

    Node externs = NodeFactory.createRootNode(input);
    Node root = NodeUtil.getFunctionNode(externs, "foo");

    renameLabels = new RenameLabels(null, null, false);
    renameLabels.process(externs, root);

    assertEquals("function foo() {\n" +
        "  a: {\n" +
        "    b: break a;\n" +
        "  }\n" +
        "  a: {\n" +
        "    b: break a;\n" +
        "  }\n" +
        "}", NodePrinter.printNode(root));
  }
}