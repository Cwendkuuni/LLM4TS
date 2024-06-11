import static org.junit.Assert.*;
import static com.google.javascript.jscomp.RenameLabels.LabelInfo.*;
import static com.google.javascript.jscomp.RenameLabels.LabelNamespace.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {

  @SuppressWarnings("unchecked")
  private static <T> T createCompiler() {
    return (T) new Compiler();
  }

  @SuppressWarnings("unchecked")
  private static <T extends NodeTraversal> T createTraversal(Compiler compiler) {
    return (T) compiler.traverse(new RenameLabels(compiler));
  }

  @SuppressWarnings("unchecked")
  private static <T> T createProcessLabels(NodeTraversal traversal) {
    return (T) traversal.getCallback(RenameLabels.ProcessLabels.class);
  }

  @Test
  public void testRenameLabelsEmptyCode() throws Exception {
    Compiler compiler = createCompiler();
    Node root = NodeUtil.createRoot();
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testRenameLabelsSingleLabel() throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("a: { }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    assertEquals("a", root.getFirstChild().getString());
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testRenameLabelsLabelWithSameNameAtDifferentDepth() throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("function foo() { a: { b: {} } } a: { c: {} }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    assertEquals("b", root.getChild(1).getChild(0).getFirstChild().getString());
    assertEquals("c", root.getChild(3).getChild(0).getFirstChild().getString());
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testRenameLabelsLabelWithSameNameAtSameDepth() throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("function foo() { a: { a: {} } a: { a: {} } }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    assertEquals("a1", root.getChild(1).getChild(0).getFirstChild().getString());
    assertEquals("a2", root.getChild(2).getChild(0).getFirstChild().getString());
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testRenameLabelsLabelWithKeywordName() throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("function foo() { var break = 5; break: {} }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    fail("Expected an AssertionError due to keyword 'break'");
  }

  @Test(expected = AssertionError.class)
  public void testRenameLabelsLabelWithSameNameInDifferentScopes() throws Exception {
    Compiler compiler = createCompiler();
    Node root =
        Node.newBlock("function foo() { function bar() { a: { a: {} } } a: { a: {} } }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    fail("Expected an AssertionError due to label 'a' in different scopes");
  }

  @Test
  public void testRenameLabelsLabelWithBreakOrContinue() throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("a: { break a; } a: { continue a; }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    assertEquals("a1", root.getChild(0).getChild(0).getFirstChild().getString());
    assertEquals("a2", root.getChild(1).getChild(0).getFirstChild().getString());
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testRenameLabelsLabelWithBreakOrContinueReferencingDifferentLabels()
      throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("a: { break b; } a: { continue c; }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    assertEquals("a1", root.getChild(0).getChild(0).getFirstChild().getString());
    assertEquals("a2", root.getChild(1).getChild(0).getFirstChild().getString());
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testRenameLabelsLabelWithDuplicateName() throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("a: { a: {} a: {} }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    fail("Expected an AssertionError due to duplicate label name 'a'");
  }

  @Test
  public void testRenameLabelsUnusedLabel() throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("a: {}");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    // Unused labels are removed
    assertNull(root.getChild(0));
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testRenameLabelsLabelWithOneChildStatement() throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("a: { b: {} }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    assertEquals("a", root.getChild(0).getFirstChild().getString());
    assertEquals("b", root.getChild(0).getChild(0).getFirstChild().getString());
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testRenameLabelsLabelWithMultipleChildStatements() throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("a: { b: {}, c: {} }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    assertEquals("a", root.getChild(0).getFirstChild().getString());
    assertEquals("b", root.getChild(0).getChild(0).getFirstChild().getString());
    assertEquals("c", root.getChild(0).getChild(1).getFirstChild().getString());
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testRenameLabelsLabelWithEmptyBlock() throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("a: {}");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    assertEquals("a", root.getChild(0).getFirstChild().getString());
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testRenameLabelsLabelWithNestedBlock() throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("a: { b: { c: {} } }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    assertEquals("a", root.getChild(0).getFirstChild().getString());
    assertEquals("b", root.getChild(0).getChild(0).getFirstChild().getString());
    assertEquals("c", root.getChild(0).getChild(0).getChild(0).getFirstChild().getString());
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testRenameLabelsLabelWithReferencedLabelInsideNestedBlock()
      throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("function foo() { a: { b: break a; } }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    assertEquals("a1", root.getChild(0).getFirstChild().getString());
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testRenameLabelsLabelWithUnreferencedLabelInsideNestedBlock()
      throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("function foo() { a: { b: {} } }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    // Unused labels inside nested blocks are removed
    assertNull(root.getChild(0).getChild(0));
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testRenameLabelsLabelWithBreakOrContinueInsideNestedBlock()
      throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("function foo() { a: { b: { break a; } } }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    assertEquals("a1", root.getChild(0).getFirstChild().getString());
    assertEquals("b1", root.getChild(0).getChild(0).getChild(0).getFirstChild().getString());
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testRenameLabelsLabelWithBreakOrContinueOutsideNestedBlock()
      throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("function foo() { break a; a: {} }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    assertEquals("a1", root.getChild(0).getFirstChild().getString());
    assertTrue(compiler.getDiagnostics().isEmpty());
  }

  @Test
  public void testRenameLabelsLabelWithBreakOrContinueOutsideNestedBlockAndUnusedLabel()
      throws Exception {
    Compiler compiler = createCompiler();
    Node root = Node.newBlock("function foo() { break a; a: {} }");
    RenameLabels renameLabels = new RenameLabels(compiler);
    renameLabels.process(NodeUtil.createExterns(), root);
    // Unused labels outside nested blocks are removed
    assertNull(root.getChild(0));
    assertTrue(compiler.getDiagnostics().isEmpty());
  }
}