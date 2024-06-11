import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.DeadAssignmentsElimination.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.GraphTraversal;
import com.google.javascript.rhino.Node;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  var y = 1;" +
        "  if (x === y) {" +
        "    x = 2;" +
        "  } else {" +
        "    y = 3;" +
        "  }" +
        "  return x;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass passes = new CompilerPass[1];
    passes[0] = new DeadAssignmentsElimination(compiler);

    Node root = parser(jsCode);
    passes[0].process(null, root);

    Node result = compiler.getOutput();
    assertEquals("function foo() {" +
        "  var x = 1;" +
        "  var y = 1;" +
        "  if (x === y) {" +
        "  } else {" +
        "    y = 3;" +
        "  }" +
        "  return x;" +
        "}", treeUtil.sourceString(root));

    // Check that eliminating dead assignments did not change control flow graph.
    ControlFlowGraph<Node> origCfG = new ControlFlowGraph<Node, Branch>(root);
    ControlFlowGraph<Node> newCfG = new ControlFlowGraph<Node, Branch>(result);

    GraphTraversal<Node> gt = new GraphTraversal<Node>(newCfG);
    gt.traverse();

    Iterable<DiGraphNode<Node, Branch>> origNodes = origCfG.getDirectedGraphNodes();
    Iterable<DiGraphNode<Node, Branch>> newNodes = newCfG.getDirectedGraphNodes();

    for (DiGraphNode<Node, Branch> origNode : origNodes) {
      DiGraphNode<Node, Branch> newNode = newNodes.iterator().next();
      assertEquals(origNode.getValue(), newNode.getValue());
      origNode.getNext().forEachRemaining(n -> assertEquals(n.getValue(), newNode.getNext().next().getValue()));
    }
  }

  @Test
  public void testDeadAssignmentsEliminationWithLoop() {
    String jsCode = "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    if (i === 5) {" +
        "      i = 6;" +
        "    }" +
        "  }" +
        "  return i;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass passes = new CompilerPass[1];
    passes[0] = new DeadAssignmentsElimination(compiler);

    Node root = parser(jsCode);
    passes[0].process(null, root);

    Node result = compiler.getOutput();
    assertEquals("function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "  }" +
        "  return i;" +
        "}", treeUtil.sourceString(root));

    // Check that eliminating dead assignments did not change control flow graph.
    ControlFlowGraph<Node> origCfG = new ControlFlowGraph<Node, Branch>(root);
    ControlFlowGraph<Node> newCfG = new ControlFlowGraph<Node, Branch>(result);

    GraphTraversal<Node> gt = new GraphTraversal<Node>(newCfG);
    gt.traverse();

    Iterable<DiGraphNode<Node, Branch>> origNodes = origCfG.getDirectedGraphNodes();
    Iterable<DiGraphNode<Node, Branch>> newNodes = newCfG.getDirectedGraphNodes();

    for (DiGraphNode<Node, Branch> origNode : origNodes) {
      DiGraphNode<Node, Branch> newNode = newNodes.iterator().next();
      assertEquals(origNode.getValue(), newNode.getValue());
      origNode.getNext().forEachRemaining(n -> assertEquals(n.getValue(), newNode.getNext().next().getValue()));
    }
  }

  @Test
  public void testDeadAssignmentsEliminationWithAssignmentInCondition() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  if (x++ === 2) {" +
        "    x = 3;" +
        "  }" +
        "  return x;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass passes = new CompilerPass[1];
    passes[0] = new DeadAssignmentsElimination(compiler);

    Node root = parser(jsCode);
    passes[0].process(null, root);

    Node result = compiler.getOutput();
    assertEquals("function foo() {" +
        "  var x = 1;" +
        "  if (x === 1) {" +
        "    x = 3;" +
        "  }" +
        "  return x;" +
        "}", treeUtil.sourceString(root));

  }

  @Test (expected = AssertionError.class)
  public void testDeadAssignmentsEliminationWithFunctionDeclaration() {
    String jsCode = "function foo() {" +
        "  function bar() {" +
        "  }" +
        "  bar = 1;" +
        "  return bar;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass passes = new CompilerPass[1];
    passes[0] = new DeadAssignmentsElimination(compiler);

    Node root = parser(jsCode);
    passes[0].process(null, root);

    compiler.compile(root);
  }

  @Test (expected = AssertionError.class)
  public void testDeadAssignmentsEliminationWithLhsInCondition() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  if (x = 2) {" +
        "  }" +
        "  return x;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass passes = new CompilerPass[1];
    passes[0] = new DeadAssignmentsElimination(compiler);

    Node root = parser(jsCode);
    passes[0].process(null, root);

    compiler.compile(root);
  }

  @Test (expected = AssertionError.class)
  public void testDeadAssignmentsEliminationWithAssignmentInLoopCondition() {
    String jsCode = "function foo() {" +
        "  var i = 0;" +
        "  for (; i++ === 10; ) {" +
        "  }" +
        "  return i;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass passes = new CompilerPass[1];
    passes[0] = new DeadAssignmentsElimination(compiler);

    Node root = parser(jsCode);
    passes[0].process(null, root);

    compiler.compile(root);
  }

  @Test (expected = AssertionError.class)
  public void testDeadAssignmentsEliminationWithVariableDeclarationInCondition() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  if (var y = 2) {" +
        "  }" +
        "  return x;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPass passes = new CompilerPass[1];
    passes[0] = new DeadAssignmentsElimination(compiler);

    Node root = parser(jsCode);
    passes[0].process(null, root);

    compiler.compile(root);
  }
}