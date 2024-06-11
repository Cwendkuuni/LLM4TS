package com.google.javascript.jscomp;

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class RenameLabelsTest {

  @Test
  public void testRenameLabels() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    RenameLabels renameLabels = new RenameLabels(compiler);

    String input = "function foo() {\n" +
        "  a: {\n" +
        "    b: break a;\n" +
        "  }\n" +
        "  c: {\n" +
        "    d: break c;\n" +
        "  }\n" +
        "}";

    Node externs = null;
    Node root = NodePrinter.print(new Parser().parse(input, null, null));

    renameLabels.process(externs, root);

    String output = NodePrinter.print(root);

    assertEquals("function foo() {\n" +
        "  a: {\n" +
        "    x: break x;\n" +
        "  }\n" +
        "  c: {\n" +
        "    y: break y;\n" +
        "  }\n" +
        "}", output);

    // Check that labels have been renamed within blocks
    assertEquals("function foo() {\n" +
        "  a: {\n" +
        "    x: {\n" +
        "      b: break x;\n" +
        "    }\n" +
        "  }\n" +
        "  c: {\n" +
        "    y: {\n" +
        "      d: break y;\n" +
        "    }\n" +
        "  }\n" +
        "}", NodePrinter.print(NodeUtil.deepClone(root)));

    // Labels are not renamed if they are not used
    String unusedLabelInput = "function foo() {\n" +
        "  a: {}\n" +
        "}";

    Node unusedLabelRoot = NodePrinter.print(new Parser().parse(unusedLabelInput, null, null));

    renameLabels.process(externs, unusedLabelRoot);

    String unusedLabelOutput = NodePrinter.print(unusedLabelRoot);

    assertEquals("function foo() {\n" +
        "  a: {}\n" +
        "}", unusedLabelOutput);

    // Check that labels are removed if they are not used
    String unusedLabelInputWithUse = "function foo() {\n" +
        "  a: {}\n" +
        "  b: break a;\n" +
        "}";

    Node unusedLabelRootWithUse = NodePrinter.print(new Parser().parse(unusedLabelInputWithUse, null, null));

    renameLabels.process(externs, unusedLabelRootWithUse);

    String unusedLabelOutputWithUse = NodePrinter.print(unusedLabelRootWithUse);

    assertEquals("function foo() {\n" +
        "}", unusedLabelOutputWithUse);

    // Check that labels are renamed even if they are in a deep nesting level
    String deepNestingLabelInput = "function foo() {\n" +
        "  a: {\n" +
        "    b: {\n" +
        "      c: {\n" +
        "        d: {\n" +
        "          e: {\n" +
        "            f: {\n" +
        "              break e;\n" +
        "            }\n" +
        "          }\n" +
        "        }\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}";

    Node deepNestingLabelRoot = NodePrinter.print(new Parser().parse(deepNestingLabelInput, null, null));

    renameLabels.process(externs, deepNestingLabelRoot);

    String deepNestingLabelOutput = NodePrinter.print(deepNestingLabelRoot);

    assertEquals("function foo() {\n" +
        "  a: {\n" +
        "    b: {\n" +
        "      c: {\n" +
        "        d: {\n" +
        "          x: {\n" +
        "            e: {\n" +
        "              break x;\n" +
        "            }\n" +
        "          }\n" +
        "        }\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}", deepNestingLabelOutput);

    // Check that labels are renamed even if they have the same name as a keyword
    String keywordLabelInput = "function foo() {\n" +
        "  label: {\n" +
        "    if (true) {\n" +
        "      break label;\n" +
        "    }\n" +
        "  }\n" +
        "}";

    Node keywordLabelRoot = NodePrinter.print(new Parser().parse(keywordLabelInput, null, null));

    renameLabels.process(externs, keywordLabelRoot);

    String keywordLabelOutput = NodePrinter.print(keywordLabelRoot);

    assertEquals("function foo() {\n" +
        "  i: {\n" +
        "    if (true) {\n" +
        "      break i;\n" +
        "    }\n" +
        "  }\n" +
        "}", keywordLabelOutput);

    // Check that labels are renamed even if they are in a loop
    String loopLabelInput = "function foo() {\n" +
        "  a: {\n" +
        "    while (true) {\n" +
        "      b: break a;\n" +
        "    }\n" +
        "  }\n" +
        "}";

    Node loopLabelRoot = NodePrinter.print(new Parser().parse(loopLabelInput, null, null));

    renameLabels.process(externs, loopLabelRoot);

    String loopLabelOutput = NodePrinter.print(loopLabelRoot);

    assertEquals("function foo() {\n" +
        "  a: {\n" +
        "    while (true) {\n" +
        "      x: break x;\n" +
        "    }\n" +
        "  }\n" +
        "}", loopLabelOutput);

    // Check that labels are renamed even if they are in a try-catch block
    String tryCatchLabelInput = "function foo() {\n" +
        "  try {\n" +
        "    a: {\n" +
        "      throw new Error();\n" +
        "    }\n" +
        "  } catch (e) {\n" +
        "    b: {\n" +
        "      break b;\n" +
        "    }\n" +
        "  }\n" +
        "}";

    Node tryCatchLabelRoot = NodePrinter.print(new Parser().parse(tryCatchLabelInput, null, null));

    renameLabels.process(externs, tryCatchLabelRoot);

    String tryCatchLabelOutput = NodePrinter.print(tryCatchLabelRoot);

    assertEquals("function foo() {\n" +
        "  try {\n" +
        "    a: {\n" +
        "      throw new Error();\n" +
        "    }\n" +
        "  } catch (e) {\n" +
        "    y: {\n" +
        "      break y;\n" +
        "    }\n" +
        "  }\n" +
        "}", tryCatchLabelOutput);
  }

  @Test
  public void testRemoveUnusedLabels() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), false);

    String input = "function foo() {\n" +
        "  a:\n" +
        "}";

    Node externs = null;
    Node root = NodePrinter.print(new Parser().parse(input, null, null));

    renameLabels.process(externs, root);

    String output = NodePrinter.print(root);

    assertNull(output);
  }

  @Test
  public void testRemoveUnusedLabelsWithReferences() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), false);

    String input = "function foo() {\n" +
        "  a: {\n" +
        "    break a;\n" +
        "  }\n" +
        "}";

    Node externs = null;
    Node root = NodePrinter.print(new Parser().parse(input, null, null));

    renameLabels.process(externs, root);

    String output = NodePrinter.print(root);

    assertEquals("function foo() {\n" +
        "}", output);
  }
}