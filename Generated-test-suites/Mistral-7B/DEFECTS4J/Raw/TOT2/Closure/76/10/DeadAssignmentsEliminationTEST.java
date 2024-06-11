import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.jscomp.ControlFlowGraph.*;

import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Graph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeTraversal;

import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    // Test case for a simple variable assignment
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "  return x;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);

    Node root = parseCompilableJs(jsCode);
    pass.process(null, root);

    ControlFlowGraph<Node> cfg = NodeTraversal.getControlFlowGraph(root);
    DataFlowAnalysis<FlowState<LiveVariableLattice>> liveness = new LiveVariablesAnalysis(
        cfg, null, compiler);
    liveness.analyze();

    // Check if the dead assignment has been removed
    Node xAssign = getAssignmentNodeByVariableName(cfg, "x");
    assertNull(" Dead assignment should be removed", xAssign);

    // Test case for a variable assignment within a conditional branch
    jsCode = "function foo() {" +
        "  var x = 1;" +
        "  if (true) {" +
        "    x = 2;" +
        "  } else {" +
        "    x = 3;" +
        "  }" +
        "  return x;" +
        "}";

    root = parseCompilableJs(jsCode);
    pass.process(null, root);

    cfg = NodeTraversal.getControlFlowGraph(root);

    // Check if the dead assignment within the true branch has been removed
    xAssign = getAssignmentNodeByVariableName(cfg, "x");
    assertTrue(" Dead assignment within the true branch should be removed",
        xAssign == null || xAssign.getParent() == null);

    // Test case for a variable assignment within a loop
    jsCode = "function foo() {" +
        "  var x = 1;" +
        "  for (var i = 0; i < 10; i++) {" +
        "    x = i;" +
        "  }" +
        "  return x;" +
        "}";

    root = parseCompilableJs(jsCode);
    pass.process(null, root);

    cfg = NodeTraversal.getControlFlowGraph(root);

    // Check if the dead assignments within the loop have been removed
    for (Node n = root; n != null; n = n.getNext()) {
      if (n.getType() == Token.FOR && !isForIn(n)) {
        Node init = n.getFirstChild();
        if (init != null && init.getType() == Token.VAR) {
          String variable = init.getLastChild().getString();
          Node xAssign = getAssignmentNodeByVariableName(cfg, variable);
          if (xAssign != null && xAssign.getParent() != null) {
            Node loop = xAssign.getParent();
            Node loopCondition = loop.getFirstChild();
            assertFalse(" Dead assignments within the loop should not be removed",
                loopCondition.getLastChild().getType() == Token.ASSIGN);
          }
        }
        break;
      }
    }

    // Test case for a dead assignment within a return statement
    jsCode = "function foo() {" +
        "  var x = 1;" +
        "  return x = 2;" +
        "}";

    root = parseCompilableJs(jsCode);
    pass.process(null, root);

    xAssign = getAssignmentNodeByVariableName(cfg, "x");
    assertTrue(" Dead assignment within a return statement should be removed",
        xAssign != null && xAssign.getParent() == null);

    // Test case for a dead assignment within a function call
    jsCode = "function foo() {" +
        "  function bar() {" +
        "    var x = 1;" +
        "    x = 2;" +
        "  }" +
        "  bar();" +
        "  return 1;" +
        "}";

    root = parseCompilableJs(jsCode);
    pass.process(null, root);

    xAssign = getAssignmentNodeByVariableName(cfg, "x");
    assertNull(" Dead assignment within a function call should be removed", xAssign);

    // Test case for a dead assignment within a switch statement
    jsCode = "function foo() {" +
        "  var x = 1;" +
        "  switch (1) {" +
        "    case 1:" +
        "      x = 2;" +
        "      break;" +
        "    default:" +
        "      break;" +
        "  }" +
        "  return x;" +
        "}";

    root = parseCompilableJs(jsCode);
    pass.process(null, root);

    xAssign = getAssignmentNodeByVariableName(cfg, "x");
    assertNull(" Dead assignment within a switch statement should be removed", xAssign);

    // Test case for a dead assignment within a try-catch-finally block
    jsCode = "function foo() {" +
        "  try {" +
        "    var x = 1;" +
        "    x = 2;" +
        "  } catch (e) {" +
        "  } finally {" +
        "    x = 3;" +
        "  }" +
        "  return x;" +
        "}";

    root = parseCompilableJs(jsCode);
    pass.process(null, root);

    xAssign = getAssignmentNodeByVariableName(cfg, "x");
    assertNull(" Dead assignment within a try-catch-finally block should be removed", xAssign);

    // Test case for a dead assignment within a variable declaration
    jsCode = "function foo() {" +
        "  var x, y;" +
        "  x = 1;" +
        "  y = 2;" +
        "  x = 3;" +
        "  return x;" +
        "}";

    root = parseCompilableJs(jsCode);
    pass.process(null, root);

    xAssign = getAssignmentNodeByVariableName(cfg, "x");
    assertNull(" Dead assignment within a variable declaration should be removed", xAssign);

    // Test case for a dead assignment within a function definition
    jsCode = "function foo(x) {" +
        "  var y;" +
        "  y = 1;" +
        "  function bar(a) {" +
        "    y = 2;" +
        "  }" +
        "  return bar;" +
        "}";

    root = parseCompilableJs(jsCode);
    pass.process(null, root);

    xAssign = getAssignmentNodeByVariableName(cfg, "y");
    assertNull(" Dead assignment within a function definition should be removed", xAssign);

    // Test case for a dead assignment within a var statement
    jsCode = "function foo() {" +
        "  var x, y;" +
        "  x = 1;" +
        "  y = 2;" +
        "  var z = x;" +
        "  x = 3;" +
        "  return z;" +
        "}";

    root = parseCompilableJs(jsCode);
    pass.process(null, root);

    xAssign = getAssignmentNodeByVariableName(cfg, "x");
    assertNull(" Dead assignment within a var statement should be removed", xAssign);

    // Test case for a dead assignment within a switch case statement
    jsCode = "function foo() {" +
        "  switch (1) {" +
        "    case 1:" +
        "      var x = 1;" +
        "      x = 2;" +
        "      break;" +
        "  }" +
        "  return 1;" +
        "}";

    root = parseCompilableJs(jsCode);
    pass.process(null, root);

    xAssign = getAssignmentNodeByVariableName(cfg, "x");
    assertNull(" Dead assignment within a switch case statement should be removed", xAssign);

    // Test case for a dead assignment within a for-in loop
    jsCode = "function foo() {" +
        "  var x = {};" +
        "  for (var key in x) {" +
        "    x[key] = 1;" +
        "  }" +
        "  return x;" +
        "}";

    root = parseCompilableJs(jsCode);
    pass.process(null, root);

    xAssign = getAssignmentNodeByVariableName(cfg, "x");
    assertNotNull(" Dead assignments within a for-in loop should not be removed", xAssign);

    // Test case for a dead assignment within a catch block
    jsCode = "function foo() {" +
        "  try {" +
        "    throw 1;" +
        "  } catch (e) {" +
        "    var x = 1;" +
        "    x = 2;" +
        "  }" +
        "  return 1;" +
        "}";

    root = parseCompilableJs(jsCode);
    pass.process(null, root);

    xAssign = getAssignmentNodeByVariableName(cfg, "x");
    assertNull(" Dead assignment within a catch block should be removed", xAssign);

    // Test case for a dead assignment within a finally block
    jsCode = "function foo() {" +
        "  try {" +
        "    throw 1;" +
        "  } finally {" +
        "    var x = 1;" +
        "    x = 2;" +
        "  }" +
        "  return 1;" +
        "}";

    root = parseCompilableJs(jsCode);
    pass.process(null, root);

    xAssign = getAssignmentNodeByVariableName(cfg, "x");
    assertNull(" Dead assignment within a finally block should be removed", xAssign);
  }

  private Node getAssignmentNodeByVariableName(ControlFlowGraph<Node> cfg,
      String variable) {
    for (DiGraphNode<Node, Branch> node : cfg.getDirectedGraphNodes()) {
      if (node.getValue() != null && NodeUtil.isName(node.getValue()) &&
          node.getValue().getString().equals(variable)) {
        if (node.getValue().getNext() != null &&
            NodeUtil.isAssignmentOp(node.getValue().getNext())) {
          return node.getValue().getNext();
        }
      }
    }
    return null;
  }
}