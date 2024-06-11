import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  @org.junit.Test
  public void testRenameLabels() {
    // Set up test compiler
    Compiler compiler = new Compiler();

    // Create test class with labels
    String testCode = "function test() {" +
        "  a: {" +
        "    b: { break a; }" +
        "  }" +
        "  a: { continue a; }" +
        "  c: { break c; }" +
        "}";

    // Compile test class
    Node externs = Node.importNode(compiler.compile(testCode, null, null).tree);
    Node root = externs.getFirstChild();

    // Run RenameLabels pass
    new RenameLabels(compiler).process(externs, root);

    // Check renamed labels
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    ProcessLabels processLabels = new ProcessLabels();
    nodeTraversal.traverse(compiler, root, processLabels);

    Map<String, String> expectedLabels = new HashMap<>();
    expectedLabels.put("a", "label0");
    expectedLabels.put("b", "label1");

    processLabels.names.forEach(name -> expectedLabels.put(name, name));

    // Check renamed labels against expected labels
    for (LabelInfo labelInfo : processLabels.namespaceStack.peek().renameMap.values()) {
      String labelName = labelInfo.id > 0 ? String.format("label%d", labelInfo.id - 1) : "global";
      String labelResult = labelInfo.referenced ? processLabels.names.get(labelInfo.id) : "";
      assertEquals(expectedLabels.get(labelName), labelResult);
    }
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testRenameLabelsWithKeywordLabel() {
    // Set up test compiler
    Compiler compiler = new Compiler();

    // Create test class with a label keyword
    String testCode = "function test() {" +
        "  if (true) break test;" +
        "}";

    // Compile test class
    Node externs = Node.importNode(compiler.compile(testCode, null, null).tree);
    Node root = externs.getFirstChild();

    // Run RenameLabels pass
    new RenameLabels(compiler).process(externs, root);

    // Assert that an error is thrown because of the use of a reserved keyword as a label
    try {
      new RenameLabels(compiler).process(externs, root);
      fail("Expected an AssertionError to be thrown.");
    } catch (AssertionError e) {
      // Expected error
    }
  }

  @org.junit.Test
  public void testRenameLabelsWithUnusedLabel() {
    // Set up test compiler
    Compiler compiler = new Compiler();

    // Create test class with an unused label
    String testCode = "function test() {" +
        "  a: {}" +
        "  b: { break a; }" +
        "}";

    // Compile test class
    Node externs = Node.importNode(compiler.compile(testCode, null, null).tree);
    Node root = externs.getFirstChild();

    // Run RenameLabels pass
    new RenameLabels(compiler).process(externs, root);

    // Check that the unused label is removed
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    ProcessLabels processLabels = new ProcessLabels();
    nodeTraversal.traverse(compiler, root, processLabels);

    LabelInfo unusedLabelInfo = processLabels.namespaceStack.peek().renameMap.get("a");
    assertNull(unusedLabelInfo);
  }

  @org.junit.Test
  public void testRenameLabelsWithNestedLabels() {
    // Set up test compiler
    Compiler compiler = new Compiler();

    // Create test class with nested labels
    String testCode = "function test() {" +
        "  a: {" +
        "    b: {" +
        "      c: { break c; }" +
        "    }" +
        "    d: { break d; }" +
        "  }" +
        "  a: { continue a; }" +
        "}";

    // Compile test class
    Node externs = Node.importNode(compiler.compile(testCode, null, null).tree);
    Node root = externs.getFirstChild();

    // Run RenameLabels pass
    new RenameLabels(compiler).process(externs, root);

    // Check renamed labels
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    ProcessLabels processLabels = new ProcessLabels();
    nodeTraversal.traverse(compiler, root, processLabels);

    Map<String, String> expectedLabels = new HashMap<>();
    expectedLabels.put("a", "label0");
    expectedLabels.put("b", "label1");
    expectedLabels.put("c", "label2");

    processLabels.names.forEach(name -> expectedLabels.put(name, name));

    // Check renamed labels against expected labels
    for (LabelInfo labelInfo : processLabels.namespaceStack.peek().renameMap.values()) {
      String labelName = labelInfo.id > 0 ? String.format("label%d", labelInfo.id - 1) : "global";
      String labelResult = labelInfo.referenced ? processLabels.names.get(labelInfo.id) : "";
      assertEquals(expectedLabels.get(labelName), labelResult);
    }
  }
}