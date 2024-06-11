import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerContext;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.HashMap;
import java.util.Map;

public class RenameLabelsTest {
  private static final String TEST_CASE_NAME = "RenameLabelsTest";
  private static final String TEST_CLASS_NAME = "com.google.javascript.jscomp.RenameLabels";
  private AbstractCompiler compiler;
  private CompilerOptions options;
  private NodeFactory nodeFactory;
  private CompilerContext compilerContext;
  private RenameLabels renameLabels;

  @org.junit.Before
  public void setUp() throws Exception {
    compiler = mock(AbstractCompiler.class);
    options = mock(CompilerOptions.class);
    nodeFactory = mock(NodeFactory.class);
    compilerContext = mock(CompilerContext.class);
    when(compiler.getContext()).thenReturn(compilerContext);
    when(compilerContext.getOptions()).thenReturn(options);
    when(compiler.getFactory()).thenReturn(nodeFactory);
    renameLabels = new RenameLabels(compiler);
  }

  @org.junit.Test
  public void testBasicFunctionWithLabel() throws Exception {
    String sourceCode = "function bar() { a: {}}";
    Node externs = Node.parseString(sourceCode, null, null);
    Node root = Node.parseString("function bar() {}", null, null);

    renameLabels.process(externs, root);

    Node labelNode = root.findFirstDescendant("a:");
    assertEquals(labelNode.getString(), TEST_CASE_NAME + "_LABEL_0");
  }

  @org.junit.Test
  public void testLabelWithinLabel() throws Exception {
    String sourceCode = "function bar() { a: { b: {}}}";
    Node externs = Node.parseString(sourceCode, null, null);
    Node root = Node.parseString("function bar() {}", null, null);

    renameLabels.process(externs, root);

    Node labelNodeB = root.findFirstDescendant("b:");
    assertEquals(labelNodeB.getString(), TEST_CASE_NAME + "_LABEL_1");

    Node labelNodeA = root.findFirstDescendant("a:");
    assertEquals(labelNodeA.getString(), TEST_CASE_NAME + "_LABEL_0");
  }

  @org.junit.Test
  public void testLabelInsideFunctionCall() throws Exception {
    String sourceCode = "function foo() { a: { b: bar(); }}}";
    Node externs = Node.parseString(sourceCode, null, null);
    Node root = Node.parseString("function foo() {}", null, null);

    renameLabels.process(externs, root);

    Node labelNodeB = root.findFirstDescendant("b:");
    assertEquals(labelNodeB.getString(), TEST_CASE_NAME + "_LABEL_2");

    Node labelNodeA = root.findFirstDescendant("a:");
    assertEquals(labelNodeA.getString(), TEST_CASE_NAME + "_LABEL_1");
  }

  @org.junit.Test
  public void testLabelInsideSwitchStatement() throws Exception {
    String sourceCode = "function bar() { switch (x) { case 0: a: {}} }";
    Node externs = Node.parseString(sourceCode, null, null);
    Node root = Node.parseString("function bar() {}", null, null);

    renameLabels.process(externs, root);

    Node labelNode = root.findFirstDescendant("a:");
    assertEquals(labelNode.getString(), TEST_CASE_NAME + "_LABEL_3");
  }

  @org.junit.Test
  public void testLabelWithSameNameInDifferentScopes() throws Exception {
    String sourceCode = "function outer() { a: { b: {}} } function inner() { a: {}}";
    Node externs = Node.parseString(sourceCode, null, null);
    Node root = Node.parseString("", null, null);

    renameLabels.process(externs, root);

    Node labelNodeOuterA = root.findFirstDescendant("outer.a:");
    assertEquals(labelNodeOuterA.getString(), TEST_CASE_NAME + "_LABEL_4");

    Node labelNodeInnerA = root.findFirstDescendant("inner.a:");
    assertEquals(labelNodeInnerA.getString(), TEST_CASE_NAME + "_LABEL_5");
  }

  @org.junit.Test
  public void testLabelRemoval() throws Exception {
    String sourceCode = "function bar() { a: {}}";
    Node externs = Node.parseString(sourceCode, null, null);
    Node root = Node.parseString("function bar() {}", null, null);

    renameLabels = new RenameLabels(compiler, new UnusedNameSupplier(), false);
    renameLabels.process(externs, root);

    Node labelNode = root.findFirstDescendant("a:");
    assert labelNode == null;
  }

  private static class UnusedNameSupplier implements Supplier<String> {
    @Override
    public String get() {
      return "unusedName";
    }
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testLabelWithKeyword() throws Exception {
    String sourceCode = "function bar() { return a: {}; }";
    Node externs = Node.parseString(sourceCode, null, null);
    Node root = Node.parseString("function bar() {}", null, null);

    renameLabels.process(externs, root);

    throw new RuntimeException("Test case does not handle label names being keywords.");
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testLabelWithEmptyName() throws Exception {
    String sourceCode = "function bar() { a: {} }";
    Node externs = Node.parseString(sourceCode, null, null);
    Node root = Node.parseString("function bar() {}", null, null);

    renameLabels.process(externs, root);

    throw new RuntimeException("Test case does not handle label names being empty strings.");
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testLabelWithLongName() throws Exception {
    String sourceCode = "function bar() { a: { b: { c: { d: { e: { f: { g: { h: { i: { j: { k: { l: { m: { n: { o: { p: { q: { r: { s: { t: { u: { v: { w: { x: { y: { z: { AA: {}}}}}}}}}}}}}}}}}}}}}}}}}}}}";
    Node externs = Node.parseString(sourceCode, null, null);
    Node root = Node.parseString("function bar() {}", null, null);

    renameLabels.process(externs, root);

    throw new RuntimeException("Test case does not handle label names longer than 255 characters.");
  }
}