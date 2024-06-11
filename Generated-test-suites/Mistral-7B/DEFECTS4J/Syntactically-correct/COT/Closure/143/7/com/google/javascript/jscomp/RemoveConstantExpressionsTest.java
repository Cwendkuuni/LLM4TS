package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompiler;
import com.google.javascript.jscomp.RemoveConstantExpressions;
import com.google.javascript.jscomp.test_util.CodeTest;
import com.google.javascript.rhino.NodeFactory;

import java.util.List;

/**
 * Test class for RemoveConstantExpressions.
 */
public class RemoveConstantExpressionsTest {

  private static Node compileAndTraverse(String jsCode) {
    Compiler compiler = new Compiler();
    Node ast = compiler.parse(jsCode, null);
    ParallelCompiler.runPasses(new RemoveConstantExpressions(compiler), ast);
    return ast;
  }

  @org.junit.Test
  public void testRemoveConstantExpression() {
    Node input = NodeFactory.createText("1 + foo() + bar()");
    Node expectedOutput = NodeFactory.createList(
        NodeFactory.createName("foo"),
        NodeFactory.createName("bar")
    );
    Node output = compileAndTraverse(input.getText());
    NodeTraversal.traverse(null, output, new AssertNodeEqualCallback(expectedOutput));
  }

  @org.junit.Test
  public void testRemoveConstantExpressionWithSideEffects() {
    Node input = NodeFactory.createText("1 + foo() * bar()");
    Node expectedOutput = NodeFactory.createList(
        NodeFactory.createName("foo"),
        NodeFactory.createOperator("*"),
        NodeFactory.createName("bar")
    );
    Node output = compileAndTraverse(input.getText());
    NodeTraversal.traverse(null, output, new AssertNodeEqualCallback(expectedOutput));
  }

  @org.junit.Test
  public void testDoNotRemoveSideEffectFreeExpression() {
    Node input = NodeFactory.createText("Math.random() + foo()");
    Node expectedOutput = NodeFactory.createList(
        NodeFactory.createName("Math.random"),
        NodeFactory.createName("foo")
    );
    Node output = compileAndTraverse(input.getText());
    NodeTraversal.traverse(null, output, new AssertNodeEqualCallback(expectedOutput));
  }

  @org.junit.Test
  public void testRemoveConstantExpressionWithParentSideEffect() {
    Node input = NodeFactory.createText("if (Math.random() < 0.5) { 1 + foo() }");
    Node expectedOutput = NodeFactory.createList(
        NodeFactory.createName("if"),
        NodeFactory.createName("Math.random"),
        NodeFactory.createOperator("<"),
        NodeFactory.createDouble(0.5),
        NodeFactory.createBlock(
            NodeFactory.createList(
                NodeFactory.createNumber(1),
                NodeFactory.createName("foo")
            )
        )
    );
    Node output = compileAndTraverse(input.getText());
    NodeTraversal.traverse(null, output, new AssertNodeEqualCallback(expectedOutput));
  }

  @org.junit.Test
  public void testRemoveConstantExpressionWithCyclicDependencies() {
    Node input = NodeFactory.createText(
        "function foo() { return 1 + bar(); } function bar() { return 1 + foo(); } 1 + foo() + bar()");
    Node expectedOutput = NodeFactory.createList(
        NodeFactory.createName("foo"),
        NodeFactory.createName("bar")
    );
    Node output = compileAndTraverse(input.getText());
    NodeTraversal.traverse(null, output, new AssertNodeEqualCallback(expectedOutput));
  }

  @org.junit.Test
  public void testDoNotChangeCodeWithNoConstantExpressions() {
    Node input = NodeFactory.createText("function foo() { return 1 + 2; }");
    Node expectedOutput = NodeFactory.createText(input.getText());
    Node output = compileAndTraverse(input.getText());
    assertTrue(CodeTest.areNodesEqual(expectedOutput, output));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testRemoveConstantExpressionWithException() {
    Node input = NodeFactory.createText("throw new Error(); 1 + foo() + bar()");
    Node expectedOutput = NodeFactory.createList(
        NodeFactory.createKeyword("throw"),
        NodeFactory.createNew("Error")
    );
    Node output = compileAndTraverse(input.getText());
    NodeTraversal.traverse(null, output, new AssertNodeEqualCallback(expectedOutput));
  }

  private static class AssertNodeEqualCallback extends AbstractPostOrderCallback {
    private Node expectedNode;

    public AssertNodeEqualCallback(Node expectedNode) {
      this.expectedNode = expectedNode;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      if (NodeUtil.nodesAreEqual(node, expectedNode)) {
        assertTrue(true);
      } else {
        fail("Expected node: " + expectedNode + "\nActual node: " + node);
      }
    }
  }
}