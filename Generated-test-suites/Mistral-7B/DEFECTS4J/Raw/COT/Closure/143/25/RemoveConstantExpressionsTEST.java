import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.TRAVERSAL_ORDER_POST;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.jscomp.RemoveConstantRValuesCallback;
import com.google.javascript.jscomp.ReportCodeHasChangedListener;
import com.google.javascript.jscomp.Result;
import com.google.javascript.jscomp.ast.AstChangeProxy;
import com.google.javascript.jscomp.test.CompilerTestCase;
import com.google.javascript.rhino.Token;

import java.util.List;

public class RemoveConstantExpressionsTest extends CompilerTestCase {

  private AbstractCompiler compiler;
  private RemoveConstantExpressions pass;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    compiler = newCompiler();
    pass = new RemoveConstantExpressions(compiler);
  }

  @Override
  public CompilerPass getProcessor() {
    return pass;
  }

  /**
   * Test method for RemoveConstantExpressions class.
   */
  @org.junit.Test
  public void testRemoveConstantExpressions() {
    String input = "function foo() { return 1; } function bar() { return 2; } "
        + "1 + foo() + bar();";
    String expectedOutput = "foo();bar();";

    Node root = parseTree(input);
    pass.process(null, root);
    assertEquals(expectedOutput, getSource(root));
  }

  /**
   * Test method for RemoveConstantExpressions class with side effect expressions.
   */
  @org.junit.Test
  public void testRemoveConstantExpressionsWithSideEffects() {
    String input = "function foo() { document.write('1'); return 1; } function bar() {"
        + "document.write('2'); return 2; } 1 + foo() + bar();";
    String expectedOutput = "foo();bar();";

    Node root = parseTree(input);
    pass.process(null, root);
    assertEquals(expectedOutput, getSource(root));
  }

  /**
   * Test method for RemoveConstantExpressions class with multiple side effect expressions.
   */
  @org.junit.Test
  public void testRemoveConstantExpressionsWithMultipleSideEffects() {
    String input = "function foo() { document.write('1'); return 1; } function bar() {"
        + "document.write('2'); return 2; } function baz() { document.write('3'); return 3; } "
        + "1 + foo() + bar() + baz();";
    String expectedOutput = "foo();bar();baz();";

    Node root = parseTree(input);
    pass.process(null, root);
    assertEquals(expectedOutput, getSource(root));
  }

  /**
   * Test method for RemoveConstantExpressions class with no side effect expressions.
   */
  @org.junit.Test
  public void testRemoveConstantExpressionsNoSideEffects() {
    String input = "1 + 2 + 3;";
    String expectedOutput = "1 + 2 + 3;";

    Node root = parseTree(input);
    pass.process(null, root);
    assertEquals(expectedOutput, getSource(root));
  }

  /**
   * Test method for RemoveConstantExpressions class with constant expressions.
   */
  @org.junit.Test
  public void testRemoveConstantExpressionsConstants() {
    String input = "1 + 1 + 1;";
    String expectedOutput = "1 + 1 + 1;";

    Node root = parseTree(input);
    pass.process(null, root);
    assertEquals(expectedOutput, getSource(root));
  }

  /**
   * Test method for RemoveConstantExpressions class with non-constant expressions.
   */
  @org.junit.Test
  public void testRemoveConstantExpressionsNonConstants() {
    String input = "function foo() { return 1 + 1; } 1 + foo();";
    String expectedOutput = "1 + foo();";

    Node root = parseTree(input);
    pass.process(null, root);
    assertEquals(expectedOutput, getSource(root));
  }

  /**
   * Test method for RemoveConstantExpressions class with complex expressions.
   */
  @org.junit.Test
  public void testRemoveConstantExpressionsComplexExpressions() {
    String input = "function foo() { return 1 + 2 * 3; } function bar() { return 4 * 5; } "
        + "1 + foo() * bar();";
    String expectedOutput = "foo();bar();";

    Node root = parseTree(input);
    pass.process(null, root);
    assertEquals(expectedOutput, getSource(root));
  }

  /**
   * Test method for RemoveConstantExpressions class with no expressions.
   */
  @org.junit.Test
  public void testRemoveConstantExpressionsNoExpressions() {
    String input = "";
    String expectedOutput = "";

    Node root = parseTree(input);
    pass.process(null, root);
    assertEquals(expectedOutput, getSource(root));
  }

  /**
   * Test method for RemoveConstantExpressions class with exceptions.
   */
  @org.junit.Test(expected = AssertionError.class)
  public void testRemoveConstantExpressionsExceptions() {
    String input = "throw new Error('Test Error');";
    Node root = parseTree(input);
    pass.process(null, root);
  }

  /**
   * Test method for RemoveConstantExpressions class with parallel pass.
   */
  @org.junit.Test
  public void testRemoveConstantExpressionsParallel() {
    String input = "function foo() { return 1; } function bar() { return 2; } "
        + "1 + foo() + bar();";

    CompilerPass pass1 = new RemoveConstantExpressions(compiler);
    CompilerPass pass2 = new RemoveConstantExpressions(compiler);

    ParallelCompilerPass parallelPass = new ParallelCompilerPass();
    parallelPass.addPass(pass1);
    parallelPass.addPass(pass2);

    Node root = parseTree(input);
    parallelPass.process(null, root);

    assertEquals("foo();bar();", getSource(root));
  }

  /**
   * Test method for RemoveConstantExpressions class with edge case: parent is a side effect expression.
   */
  @org.junit.Test
  public void testRemoveConstantExpressionsParentSideEffect() {
    String input = "document.write('1'); 1 + 2;";
    String expectedOutput = "document.write('1');";

    Node root = parseTree(input);
    pass.process(null, root);
    assertEquals(expectedOutput, getSource(root));
  }

  /**
   * Test method for RemoveConstantExpressions class with edge case: child is a side effect expression.
   */
  @org.junit.Test
  public void testRemoveConstantExpressionsChildSideEffect() {
    String input = "1 + document.write('2');";
    String expectedOutput = "document.write('2');";

    Node root = parseTree(input);
    pass.process(null, root);
    assertEquals(expectedOutput, getSource(root));
  }

  /**
   * Test method for RemoveConstantExpressions class with edge case: multiple children are side effect expressions.
   */
  @org.junit.Test
  public void testRemoveConstantExpressionsMultipleChildrenSideEffect() {
    String input = "1 + document.write('2') + document.write('3');";
    String expectedOutput = "document.write('2') + document.write('3');";

    Node root = parseTree(input);
    pass.process(null, root);
    assertEquals(expectedOutput, getSource(root));
  }

  /** Remove Constant R-values callback tests */

  /**
   * Test method for RemoveConstantRValuesCallback class with empty list of subexpressions.
   */
  @org.junit.Test
  public void testGetSideEffectSubexpressionsEmptyList() {
    Node node = new Node(Token.EXPR_RESULT, null);
    RemoveConstantRValuesCallback callback = new RemoveConstantRValuesCallback();
    List<Node> subexpressions = Lists.newArrayList();

    NodeTraversal.traverse(null, node, new GatherSideEffectSubexpressionsCallback(
        null, new GatherSideEffectSubexpressionsCallback.CopySideEffectSubexpressions(
            null, subexpressions)));

    assertEquals(0, subexpressions.size());
  }

  /**
   * Test method for RemoveConstantRValuesCallback class with one subexpression.
   */
  @org.junit.Test
  public void testGetSideEffectSubexpressionsOneSubexpression() {
    Node node = new Node(Token.EXPR_RESULT, null);
    Node subexpression = new Node(Token.IDENT, "foo");
    RemoveConstantRValuesCallback callback = new RemoveConstantRValuesCallback();
    List<Node> subexpressions = Lists.newArrayList();

    NodeTraversal.traverse(null, node, new GatherSideEffectSubexpressionsCallback(
        null, new GatherSideEffectSubexpressionsCallback.CopySideEffectSubexpressions(
            null, subexpressions)));

    assertEquals(1, subexpressions.size());
    assertEquals(subexpression, subexpressions.get(0));
  }

  /**
   * Test method for RemoveConstantRValuesCallback class with multiple subexpressions.
   */
  @org.junit.Test
  public void testGetSideEffectSubexpressionsMultipleSubexpressions() {
    Node node = new Node(Token.EXPR_RESULT, null);
    Node subexpression1 = new Node(Token.IDENT, "foo");
    Node subexpression2 = new Node(Token.IDENT, "bar");
    Node subexpression3 = new Node(Token.IDENT, "baz");

    RemoveConstantRValuesCallback callback = new RemoveConstantRValuesCallback();
    List<Node> subexpressions = Lists.newArrayList();

    NodeTraversal.traverse(null, node, new GatherSideEffectSubexpressionsCallback(
        null, new GatherSideEffectSubexpressionsCallback.CopySideEffectSubexpressions(
            null, subexpressions)), TRAVERSAL_ORDER_POST);

    assertEquals(3, subexpressions.size());
    assertTrue(subexpressions.contains(subexpression1));
    assertTrue(subexpressions.contains(subexpression2));
    assertTrue(subexpressions.contains(subexpression3));
  }
}

class GatherSideEffectSubexpressionsCallback extends AbstractPostOrderCallback {
  private final ReportCodeHasChangedListener listener;
  private final CopySideEffectSubexpressions copySideEffectSubexpressions;

  public GatherSideEffectSubexpressionsCallback(
      ReportCodeHasChangedListener listener,
      CopySideEffectSubexpressions copySideEffectSubexpressions) {
    this.listener = listener;
    this.copySideEffectSubexpressions = copySideEffectSubexpressions;
  }

  @Override
  public void visit(NodeTraversal traversal, Node node, Node parent) {
    if (NodeUtil.nodeTypeMayHaveSideEffects(node)
        && (parent == null || NodeUtil.nodeTypeMayHaveSideEffects(parent))) {
      copySideEffectSubexpressions.collect(node);
    }

    listener.nodeVisited(node);
  }
}

class CopySideEffectSubexpressions implements AstChangeProxy.ChangeListener {
  private final ReportCodeHasChangedListener listener;
  private final List<Node> subexpressions;

  CopySideEffectSubexpressions(ReportCodeHasChangedListener listener, List<Node> subexpressions) {
    this.listener = listener;
    this.subexpressions = subexpressions;
  }

  void collect(Node node) {
    subexpressions.add(node);
  }

  @Override
  public void nodeAdded(Node node) {
    listener.nodeAdded(node);
  }

  @Override
  public void nodeRemoved(Node node) {
    listener.nodeRemoved(node);
  }

  @Override
  public void nodeVisited(Node node) {
  }
}