import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.DeadAssignmentsElimination.*;

import org.junit.Test;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.GraphNode;
import com.google.javascript.jscomp.ControlFlowGraph;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String jsCode = "function foo() {" +
                    "  var x = 1;" +
                  "  if (x) {" +
                  "    var y = 2;" +
                  "    x = 3;" +
                  "  } else {" +
                  "    x = 4;" +
                  "  }" +
                  "  return x;" +
                  "}";

    Node ast = parser(compiler, jsCode);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, ast);

    ControlFlowGraph<Node> controlFlowGraph = deadAssignmentsElimination.compiler.getLatestCFG();
    GraphNode xNode = controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue();
    assertFalse("x should not be dead", xNode.isDead());

    GraphNode yNode = controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue().getNext().getValue();
    assertTrue("y should be dead", yNode.isDead());

    Node result = (Node) controlFlowGraph.getEntryNode().getValue().getLastChild();
    assertEquals("Expected result to be 3", new Node(Token.NUMBER, 3.0), result);
  }

  @Test
  public void testDeadAssignmentsInLoop() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String jsCode = "function foo() {" +
                    "  for (var i = 0; i < 10; i++) {" +
                    "    var x = i;" +
                    "  }" +
                    "  return x;" +
                    "}";

    Node ast = parser(compiler, jsCode);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, ast);

    ControlFlowGraph<Node> controlFlowGraph = deadAssignmentsElimination.compiler.getLatestCFG();
    GraphNode xNode = controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue();
    assertTrue("x should be dead", xNode.isDead());

    Node result = (Node) controlFlowGraph.getEntryNode().getValue().getLastChild();
    assertEquals("Expected result to be 10", new Node(Token.NUMBER, 10.0), result);
  }

  @Test
  public void testDeadAssignmentsInIf() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String jsCode = "function foo() {" +
                    "  if (true) {" +
                    "    var x = 1;" +
                    "  } else {" +
                    "    var x = 2;" +
                    "  }" +
                    "  return x;" +
                    "}";

    Node ast = parser(compiler, jsCode);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, ast);

    ControlFlowGraph<Node> controlFlowGraph = deadAssignmentsElimination.compiler.getLatestCFG();
    GraphNode xNode = controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue();
    assertTrue("x should be dead", xNode.isDead());

    Node result = (Node) controlFlowGraph.getEntryNode().getValue().getLastChild();
    assertEquals("Expected result to be 1", new Node(Token.NUMBER, 1.0), result);
  }

  @Test
  public void testDeadAssignmentsInConditionalExpression() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String jsCode = "function foo() {" +
                    "  var x = 1;" +
                    "  return (x > 0) ? (x = 2) : x;" +
                    "}";

    Node ast = parser(compiler, jsCode);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, ast);

    ControlFlowGraph<Node> controlFlowGraph = deadAssignmentsElimination.compiler.getLatestCFG();
    GraphNode xNode = controlFlowGraph.getDirectedGraphNodes().iterator().next().getValue();
    assertTrue("x should be dead", xNode.isDead());

    Node result = (Node) controlFlowGraph.getEntryNode().getValue().getLastChild();
    assertEquals("Expected result to be 1", new Node(Token.NUMBER, 1.0), result);
  }

  @Test(expected = RuntimeException.class)
  public void testDeadAssignmentsInFunctionCall() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String jsCode = "function foo() {" +
                    "  var x = 1;" +
                    "  return Math.sin(x = 2);" +
                    "}";

    Node ast = parser(compiler, jsCode);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, ast);

    // The test should throw a RuntimeException because the code change
    // made by DeadAssignmentsElimination is not valid JavaScript.
  }

  @Test(expected = RuntimeException.class)
  public void testDeadAssignmentsInWhile() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String jsCode = "function foo() {" +
                    "  var x = 1;" +
                    "  while (x) {" +
                    "    x = 2;" +
                    "  }" +
                    "  return x;" +
                    "}";

    Node ast = parser(compiler, jsCode);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, ast);

    // The test should throw a RuntimeException because the code change
    // made by DeadAssignmentsElimination is not valid JavaScript.
  }

  @Test(expected = RuntimeException.class)
  public void testDeadAssignmentsInForIn() {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);

    String jsCode = "function foo() {" +
                    "  var x = {'a': 1, 'b': 2};" +
                    "  for (var key in x) {" +
                    "    x[key] = 3;" +
                    "  }" +
                    "  return x.a;" +
                    "}";

    Node ast = parser(compiler, jsCode);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, ast);

    // The test should throw a RuntimeException because the code change
    // made by DeadAssignmentsElimination is not valid JavaScript.
  }
}