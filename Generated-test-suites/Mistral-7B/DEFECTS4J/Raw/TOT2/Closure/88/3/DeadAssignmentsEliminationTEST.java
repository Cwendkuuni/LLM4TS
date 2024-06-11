import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeOperations;

import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    String sourceCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var c = a;\n" +
        "  a = 3;\n" +
        "  b = c;\n" +
        "  return a + b;\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = NodeOperations.parseJsonToRootNode(sourceCode);

    DeadAssignmentsElimination deadAssignmentsEliminator =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsEliminator.process(null, root);

    ControlFlowGraph<Node> controlFlowGraph =
        NodeTraversal.getOrCreateControlFlowGraph(root);
    assertTrue(deadAssignmentsEliminator.tryRemoveDeadAssignments(
        NodeTraversal.getEmptyNodeTraversal(compiler), controlFlowGraph));

    String expectedResult = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  a = 3;\n" +
        "  // dead code elimination: b = c;\n" +
        "  return a + b;\n" +
        "}";

    String actualResult = NodeOperations.getSourceCode(root);
    assertTrue(expectedResult.equals(actualResult));
  }

  @Test
  public void testDeadAssignmentsEliminationWithComplexControlFlow() {
    String sourceCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  if (true) {\n" +
        "    a = 3;\n" +
        "  } else {\n" +
        "    b = a;\n" +
        "  }\n" +
        "  return a + b;\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = NodeOperations.parseJsonToRootNode(sourceCode);

    DeadAssignmentsElimination deadAssignmentsEliminator =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsEliminator.process(null, root);

    ControlFlowGraph<Node> controlFlowGraph =
        NodeTraversal.getOrCreateControlFlowGraph(root);
    assertTrue(deadAssignmentsEliminator.tryRemoveDeadAssignments(
        NodeTraversal.getEmptyNodeTraversal(compiler), controlFlowGraph));

    String expectedResult = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  if (true) {\n" +
        "    a = 3;\n" +
        "  } else {\n" +
        "    // dead code elimination: b = a;\n" +
        "  }\n" +
        "  return a + b;\n" +
        "}";

    String actualResult = NodeOperations.getSourceCode(root);
    assertTrue(expectedResult.equals(actualResult));
  }

  @Test
  public void testDeadAssignmentsEliminationWithNoDeadAssignments() {
    String sourceCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var c = a + b;\n" +
        "  return c;\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = NodeOperations.parseJsonToRootNode(sourceCode);

    DeadAssignmentsElimination deadAssignmentsEliminator =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsEliminator.process(null, root);

    ControlFlowGraph<Node> controlFlowGraph =
        NodeTraversal.getOrCreateControlFlowGraph(root);
    assertTrue(deadAssignmentsEliminator.tryRemoveDeadAssignments(
        NodeTraversal.getEmptyNodeTraversal(compiler), controlFlowGraph));

    String expectedResult = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var c = a + b;\n" +
        "  return c;\n" +
        "}";

    String actualResult = NodeOperations.getSourceCode(root);
    assertTrue(expectedResult.equals(actualResult));
  }

  @Test
  public void testDeadAssignmentsEliminationWithMissingLivenessAnalysis() {
    String sourceCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  a = b;\n" + // No liveness analysis done for this assignment
        "  return a + b;\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = NodeOperations.parseJsonToRootNode(sourceCode);

    DeadAssignmentsElimination deadAssignmentsEliminator =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsEliminator.process(null, root);

    ControlFlowGraph<Node> controlFlowGraph =
        NodeTraversal.getOrCreateControlFlowGraph(root);
    assertFalse(deadAssignmentsEliminator.tryRemoveDeadAssignments(
        NodeTraversal.getEmptyNodeTraversal(compiler), controlFlowGraph));

    String expectedResult = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  a = b;\n" + // Dead code elimination should not remove this
        "  return a + b;\n" +
        "}";

    String actualResult = NodeOperations.getSourceCode(root);
    assertTrue(expectedResult.equals(actualResult));
  }

  @Test
  public void testDeadAssignmentsEliminationWithMultipleDeadAssignments() {
    String sourceCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  var c = a;\n" +
        "  a = 3;\n" +
        "  b = c;\n" +
        "  return a + b;\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = NodeOperations.parseJsonToRootNode(sourceCode);

    DeadAssignmentsElimination deadAssignmentsEliminator =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsEliminator.process(null, root);

    ControlFlowGraph<Node> controlFlowGraph =
        NodeTraversal.getOrCreateControlFlowGraph(root);
    assertTrue(deadAssignmentsEliminator.tryRemoveDeadAssignments(
        NodeTraversal.getEmptyNodeTraversal(compiler), controlFlowGraph));

    String expectedResult = "function foo() {\n" +
        "  var a = 1;\n" +
        "  var b = 2;\n" +
        "  a = 3;\n" + // Dead code elimination: b = c;
        "  return a + b;\n" +
        "}";

    String actualResult = NodeOperations.getSourceCode(root);
    assertTrue(expectedResult.equals(actualResult));
  }

  @Test
  public void testDeadAssignmentsEliminationWithIncrementOperator() {
    String sourceCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  a++;\n" +
        "  a++;\n" +
        "  return a;\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = NodeOperations.parseJsonToRootNode(sourceCode);

    DeadAssignmentsElimination deadAssignmentsEliminator =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsEliminator.process(null, root);

    ControlFlowGraph<Node> controlFlowGraph =
        NodeTraversal.getOrCreateControlFlowGraph(root);
    assertTrue(deadAssignmentsEliminator.tryRemoveDeadAssignments(
        NodeTraversal.getEmptyNodeTraversal(compiler), controlFlowGraph));

    String expectedResult = "function foo() {\n" +
        "  var a = 1;\n" +
        "  a++;\n" + // Dead code elimination: a++;
        "  return a;\n" +
        "}";

    String actualResult = NodeOperations.getSourceCode(root);
    assertTrue(expectedResult.equals(actualResult));
  }

  @Test
  public void testDeadAssignmentsEliminationWithDecrementOperator() {
    String sourceCode = "function foo() {\n" +
        "  var a = 1;\n" +
        "  a--;\n" +
        "  a--;\n" +
        "  return a;\n" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    Node root = NodeOperations.parseJsonToRootNode(sourceCode);

    DeadAssignmentsElimination deadAssignmentsEliminator =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsEliminator.process(null, root);

    ControlFlowGraph<Node> controlFlowGraph =
        NodeTraversal.getOrCreateControlFlowGraph(root);
    assertTrue(deadAssignmentsEliminator.tryRemoveDeadAssignments(
        NodeTraversal.getEmptyNodeTraversal(compiler), controlFlowGraph));

    String expectedResult = "function foo() {\n" +
        "  var a = 1;\n" +
        "  a--;\n" + // Dead code elimination: a--;
        "  return a;\n" +
        "}";

    String actualResult = NodeOperations.getSourceCode(root);
    assertTrue(expectedResult.equals(actualResult));
  }
}