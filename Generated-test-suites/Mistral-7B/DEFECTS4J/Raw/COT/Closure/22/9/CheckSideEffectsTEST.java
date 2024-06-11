package com.google.javascript.jscomp.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class CheckSideEffectsTest {

  private AbstractCompiler compiler;

  @SuppressWarnings("unchecked")
  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions(new CompilerInput());
    compiler = new AbstractCompiler() {
      @Override
      public List<Checker> getCheckers() {
        return Lists.newArrayList(new CheckSideEffects(this, CheckLevel.WARNING, true));
      }
    };
    compiler.setOptions(options);
  }

  @Test
  public void testFunctionality() {
    Node root = NodeFactory.createProgram();
    NodeTraversal.traverse(compiler, root, new CheckSideEffects(compiler, CheckLevel.WARNING, true));
    assertTrue("Expected some nodes to be marked for removal",
        checkProblemNodesExist(root, 3));
  }

  @Test
  public void testEdgeCases() {
    Node root = NodeFactory.createProgram();
    Node forNode = NodeFactory.createFor();
    Node expr = NodeFactory.createName("x");
    Node assign = NodeFactory.createCall(NodeFactory.createName("foo"), NodeFactory.createList(expr));
    forNode.addChildToBack(assign);
    root.addChildToBack(forNode);

    NodeTraversal.traverse(compiler, root, new CheckSideEffects(compiler, CheckLevel.WARNING, true));
    assertFalse("Expected no nodes to be marked for removal",
        checkProblemNodesExist(root, 0));

    // This test case should trigger a warning because the assignment statement
    // has no side-effects even though it is inside a for loop.
    expr = NodeFactory.createName("s");
    Node stringConcat = NodeFactory.createCall(Token.ADD, expr, NodeFactory.createString(" continued on the next line but you forgot the +"));
    assign = NodeFactory.createAssign(Token.ASSIGN, NodeFactory.createName("x"), NodeFactory.createCall(Token.NAME, "foo"));
    forNode.addChildToBack(assign);
    forNode.addChildToBack(stringConcat);
    root.addChildToBack(forNode);

    NodeTraversal.traverse(compiler, root, new CheckSideEffects(compiler, CheckLevel.WARNING, true));
    assertTrue("Expected one node to be marked for removal",
        checkProblemNodesExist(root, 1));
  }

  @Test(expected = AssertionError.class)
  public void testExceptionHandling() {
    Node root = NodeFactory.createProgram();
    Node tryNode = NodeFactory.createBlock(
        NodeFactory.createTry(
            NodeFactory.createBlock(NodeFactory.createCall(Token.NAME, "someFunctionThatThrowsAnException"))),
        NodeFactory.createList());
    root.addChildToBack(tryNode);

    NodeTraversal.traverse(compiler, root, new CheckSideEffects(compiler, CheckLevel.WARNING, true));
  }

  private boolean checkProblemNodesExist(Node root, int expectedCount) {
    List<Node> problemNodes = Lists.newArrayList();
    NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void visit(NodeTraversal t, Node n, Node parent) {
        if (n.isMarked(Node.IS_PROBLEM)) {
          problemNodes.add(n);
        }
      }
    });
    return problemNodes.size() == expectedCount;
  }
}