import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the RenameLabels CompilerPass.
 */
public class RenameLabelsTest {

  @org.junit.Test
  public void testRenameLabels() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(Compiler.WarningLevel.QUIET);
    String sourceCode =
        "function test() {" +
            "  a: {" +
            "    b: break a;" +
            "  }" +
            "  c: {" +
            "    d: continue c;" +
            "  }" +
            "  e: {" +
            "    a: break e;" +
            "  }" +
            "  f: {" +
            "    a: continue f;" +
            "  }" +
            "}";

    Node externs = NodeFactory.compile(sourceCode);
    Node root = NodeFactory.parseTemplate(
        "function test() { test(); }", null);
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    renameLabels.process(externs, root);

    // Check the renamed labels in the test function.
    Node testFunction = NodeUtil.getChildren(root, 0, 1)[0];
    assertEquals("test", testFunction.getIdentifier().getName());
    Node testFunctionBody = NodeUtil.getChildren(testFunction, 0)[0];
    Node labelA = NodeUtil.getChildren(testFunctionBody, 0, 1)[0];
    assertEquals("a", labelA.getFirstChild().getString());
    Node labelB = NodeUtil.getChildren(labelA, 0, 1)[0];
    assertEquals("b", labelB.getFirstChild().getString());
    Node labelE = NodeUtil.getChildren(testFunctionBody, 2, 3)[0];
    assertEquals("e", labelE.getFirstChild().getString());

    // Check the renamed labels in the test function's body.
    Node statements = NodeUtil.getChildren(testFunctionBody, 1)[0];
    assertEquals("test", NodeUtil.getChildren(statements, 0, 1)[0].getIdentifier().getName());
    Node labelC = NodeUtil.getChildren(NodeUtil.getChildren(statements, 1)[0], 0, 1)[0];
    assertEquals("c", labelC.getFirstChild().getString());
    Node labelD = NodeUtil.getChildren(NodeUtil.getChildren(NodeUtil.getChildren(statements, 1)[0], 1), 0, 1)[0];
    assertEquals("d", labelD.getFirstChild().getString());
    Node labelF = NodeUtil.getChildren(NodeUtil.getChildren(NodeUtil.getChildren(statements, 2)[0], 0), 0, 1)[0];
    assertEquals("f", labelF.getFirstChild().getString());

    // Test the removal of unreferenced labels.
    sourceCode =
        "function test() {" +
            "  a: {" +
            "  }" +
            "  b: {" +
            "    a: break b;" +
            "  }" +
            "}";

    externs = NodeFactory.compile(sourceCode);
    root = NodeFactory.parseTemplate(
        "function test() { test(); }", null);
    renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    renameLabels.process(externs, root);

    testFunction = NodeUtil.getChildren(root, 0, 1)[0];
    testFunctionBody = NodeUtil.getChildren(testFunction, 0)[0];
    labelA = NodeUtil.getChildren(testFunctionBody, 0, 1)[0];
    assertTrue(labelA.getParentNode() == null);
    labelB = NodeUtil.getChildren(testFunctionBody, 1, 2)[0];
    assertEquals("b", labelB.getFirstChild().getString());
  }

  @org.junit.Test
  public void testRenameLabelsWithReusedLabel() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(Compiler.WarningLevel.QUIET);
    String sourceCode =
        "function test() {" +
            "  a: {" +
            "    b: {" +
            "      a: break a;" +
            "    }" +
            "  }" +
            "}";

    Node externs = NodeFactory.compile(sourceCode);
    Node root = NodeFactory.parseTemplate("function test() { test(); }", null);
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    renameLabels.process(externs, root);

    // Check that 'a' is renamed to 'a0' in the outer scope and 'a1' in the inner scope.
    Node testFunction = NodeUtil.getChildren(root, 0, 1)[0];
    Node testFunctionBody = NodeUtil.getChildren(testFunction, 0)[0];
    Node labelAOuter = NodeUtil.getChildren(testFunctionBody, 0, 1)[0];
    assertEquals("a0", labelAOuter.getFirstChild().getString());
    Node labelBAInner = NodeUtil.getChildren(labelAOuter, 0, 1)[0];
    assertEquals("b", labelBAInner.getFirstChild().getString());
    Node labelAInner = NodeUtil.getChildren(NodeUtil.getChildren(labelBAInner, 0), 0)[0];
    assertEquals("a1", labelAInner.getFirstChild().getString());
  }

  @org.junit.Test
  public void testRenameLabelsWithKeywordLabel() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(Compiler.WarningLevel.QUIET);
    String sourceCode =
        "function test() {" +
            "  break: {" +
            "  }" +
            "}";

    Node externs = NodeFactory.compile(sourceCode);
    Node root = NodeFactory.parseTemplate("function test() { test(); }", null);
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    try {
      renameLabels.process(externs, root);
      fail("Expected compilation error for label name 'break'.");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Label name 'break' is a reserved keyword."));
    }
  }

  @org.junit.Test
  public void testRenameLabelsWithEmptyLabel() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(Compiler.WarningLevel.QUIET);
    String sourceCode =
        "function test() {" +
            "  {}" +
            "}";

    Node externs = NodeFactory.compile(sourceCode);
    Node root = NodeFactory.parseTemplate("function test() { test(); }", null);
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    try {
      renameLabels.process(externs, root);
      fail("Expected compilation error for empty label.");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Empty label names are not allowed."));
    }
  }

  @org.junit.Test
  public void testRenameLabelsWithDuplicateLabelName() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(Compiler.WarningLevel.QUIET);
    String sourceCode =
        "function test() {" +
            "  a: {" +
            "  }" +
            "  a: {" +
            "  }" +
            "}";

    Node externs = NodeFactory.compile(sourceCode);
    Node root = NodeFactory.parseTemplate("function test() { test(); }", null);
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    try {
      renameLabels.process(externs, root);
      fail("Expected compilation error for duplicate label name 'a'.");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Duplicate label name 'a'."));
    }
  }

  @org.junit.Test
  public void testRenameLabelsWithLabelInConditionalStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(Compiler.WarningLevel.QUIET);
    String sourceCode =
        "function test() {" +
            "  a: {" +
            "    if (a) {" +
            "      break a;" +
            "    }" +
            "  }" +
            "}";

    Node externs = NodeFactory.compile(sourceCode);
    Node root = NodeFactory.parseTemplate("function test() { test(); }", null);
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    try {
      renameLabels.process(externs, root);
      fail("Expected compilation error for label name 'a' in conditional statement.");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Label names are not allowed in conditional statements."));
    }
  }

  @org.junit.Test
  public void testRenameLabelsWithLabelInLoopStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(Compiler.WarningLevel.QUIET);
    String sourceCode =
        "function test() {" +
            "  a: {" +
            "    for (var i = 0; i < 10; i++) {" +
            "      break a;" +
            "    }" +
            "  }" +
            "}";

    Node externs = NodeFactory.compile(sourceCode);
    Node root = NodeFactory.parseTemplate("function test() { test(); }", null);
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    try {
      renameLabels.process(externs, root);
      fail("Expected compilation error for label name 'a' in loop statement.");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Label names are not allowed in loop statements."));
    }
  }

  @org.junit.Test
  public void testRenameLabelsWithLabelInSwitchStatement() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(null);
    options.setWarningLevel(Compiler.WarningLevel.QUIET);
    String sourceCode =
        "function test() {" +
            "  a: {" +
            "    switch (1) {" +
            "      case 1:" +
            "        break a;" +
            "      default:" +
            "        break;" +
            "    }" +
            "  }" +
            "}";

    Node externs = NodeFactory.compile(sourceCode);
    Node root = NodeFactory.parseTemplate("function test() { test(); }", null);
    RenameLabels renameLabels = new RenameLabels(compiler, new DefaultNameSupplier(), true);
    try {
      renameLabels.process(externs, root);
      fail("Expected compilation error for label name 'a' in switch statement.");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Label names are not allowed in switch statements."));
    }
  }
}