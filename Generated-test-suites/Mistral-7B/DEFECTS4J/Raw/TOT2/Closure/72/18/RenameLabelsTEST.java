import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Test;

public class RenameLabelsTest {
  @Test
  public void testRenameLabels() {
    // Test case for a simple label renaming.
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    String input = "function foo() {" +
        "  a: {" +
        "    b: break a;" +
        "  }" +
        "}";

    Node root = NodeFactory.compile(input, options);
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    ProcessLabels processLabels = new ProcessLabels();
    nodeTraversal.setScopedCallback(processLabels);
    nodeTraversal.traverse();

    Node expectedOutput = NodeFactory.compile(
        "function foo() {" +
        "  " + processLabels.names.get(0) + ": {" +
        "    " + processLabels.names.get(0) + "_1: break " + processLabels.names.get(0) + ";" +
        "  }" +
        "}", options).getFirstChild();

    Node actualOutput = NodeUtil.deepClone(root);
    NodeUtil.tryMergeBlock(actualOutput);

    assertEquals(expectedOutput.toString(), actualOutput.toString());
  }

  @Test
  public void testRenameLabelsWithNestedLabels() {
    // Test case for label renaming with nested labels.
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    String input = "function foo() {" +
        "  a: {" +
        "    b: {" +
        "      c: break b;" +
        "    }" +
        "  }" +
        "}";

    Node root = NodeFactory.compile(input, options);
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    ProcessLabels processLabels = new ProcessLabels();
    nodeTraversal.setScopedCallback(processLabels);
    nodeTraversal.traverse();

    Node expectedOutput = NodeFactory.compile(
        "function foo() {" +
        "  " + processLabels.names.get(0) + ": {" +
        "    " + processLabels.names.get(0) + "_1: {" +
        "      " + processLabels.names.get(0) + "_2: break " + processLabels.names.get(0) + "_1;" +
        "    }" +
        "  }" +
        "}", options).getFirstChild();

    Node actualOutput = NodeUtil.deepClone(root);
    NodeUtil.tryMergeBlock(actualOutput);

    assertEquals(expectedOutput.toString(), actualOutput.toString());
  }

  @Test(expected = AssertionError.class)
  public void testRenameLabelsWithNameCollision() {
    // Test case for label renaming with name collision.
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    String input = "function foo() {" +
        "  a: {" +
        "    label a;" + // Duplicate label
        "  }" +
        "}";

    Node root = NodeFactory.compile(input, options);
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    ProcessLabels processLabels = new ProcessLabels();
    nodeTraversal.setScopedCallback(processLabels);
    nodeTraversal.traverse();

    // The test should fail due to the name collision.
  }

  @Test(expected = RuntimeException.class)
  public void testRenameLabelsWithUnreachableLabel() {
    // Test case for label renaming with an unreachable label.
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    String input = "function foo() {" +
        "  a: {" +
        "  }" + // Unreachable label
        "}";

    Node root = NodeFactory.compile(input, options);
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    ProcessLabels processLabels = new ProcessLabels();
    nodeTraversal.setScopedCallback(processLabels);
    nodeTraversal.traverse();

    // The test should fail due to the unreachable label.
  }

  @Test
  public void testRenameLabelsWithRemoveUnused() {
    // Test case for label renaming with removeUnused flag set to true.
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    String input = "function foo() {" +
        "  a: {" +
        "    b: break a;" +
        "  }" +
        "  a: {" +
        "  }" + // Unused label
        "}";

    Node root = NodeFactory.compile(input, options);
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), false); // Disable removing unused labels
    renameLabels.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    ProcessLabels processLabels = new ProcessLabels();
    nodeTraversal.setScopedCallback(processLabels);
    nodeTraversal.traverse();

    assertEquals(3, processLabels.names.size()); // Check that both labels are renamed and unused one is not removed

    RenameLabels renameLabelsWithRemoveUnused = new RenameLabels(compiler, new DefaultNameSupplier(), true); // Enable removing unused labels
    root = NodeUtil.deepClone(root);
    renameLabelsWithRemoveUnused.process(null, root);

    nodeTraversal = new NodeTraversal(root, compiler);
    processLabels = new ProcessLabels();
    nodeTraversal.setScopedCallback(processLabels);
    nodeTraversal.traverse();

    assertEquals(2, processLabels.names.size()); // Check that unused label is removed
  }

  @Test
  public void testRenameLabelsWithCustomNameSupplier() {
    // Test case for label renaming with a custom NameSupplier.
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    String input = "function foo() {" +
        "  a: {" +
        "  }" +
        "}";

    Node root = NodeFactory.compile(input, options);
    RenameLabels renameLabels = new RenameLabels(compiler, new CustomNameSupplier(), false);
    renameLabels.process(null, root);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    ProcessLabels processLabels = new ProcessLabels();
    nodeTraversal.setScopedCallback(processLabels);
    nodeTraversal.traverse();

    String customLabelName = processLabels.names.get(0);
    assertEquals("customLabelName", customLabelName);
  }

  // CustomNameSupplier class implementation goes here...
}