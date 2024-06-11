package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.Token;

/**
 * Test class for DeadAssignmentsElimination class.
 */
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    // Test case 1: Simple assignment elimination
    AbstractCompiler compiler = new AbstractCompiler();
    String input = "function foo() {" + "  var x = 1;" + "  x = 2;" + "  return x;" + "}";
    Node root = AbstractCompiler.compile(compiler, input);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    assertTrue(NodeUtil.getChildrenCount(root.getLastChild()) == 1);
    assertTrue(NodeUtil.getString(root.getLastChild().getFirstChild()).equals("2"));

    // Test case 2: Increment/decrement operator elimination
    input = "function foo() {" + "  var x = 0;" + "  x++;" + "  return x;" + "}";
    root = AbstractCompiler.compile(compiler, input);

    eliminator.process(null, root);

    assertTrue(NodeUtil.getChildrenCount(root.getLastChild()) == 1);
    assertTrue(NodeUtil.getString(root.getLastChild().getFirstChild()).equals("1"));

    input = "function foo() {" + "  var x = 0;" + "  ++x;" + "  return x;" + "}";

    root = AbstractCompiler.compile(compiler, input);

    eliminator.process(null, root);

    assertTrue(NodeUtil.getChildrenCount(root.getLastChild()) == 1);
    assertTrue(NodeUtil.getString(root.getLastChild().getFirstChild()).equals("1"));

    // Test case 3: Dead assignment elimination with control flow structures
    input = "function foo() {" + "  if (true) {" + "    var x = 1;" + "  } else {" + "    var x = 2;" + "  }" + "  return x;" + "}";
    root = AbstractCompiler.compile(compiler, input);

    eliminator.process(null, root);

    assertTrue(NodeUtil.getChildrenCount(root.getLastChild()) == 1);
    assertTrue(NodeUtil.getString(root.getLastChild().getFirstChild()).equals("1"));

    input = "function foo() {" + "  if (true) {" + "    var x = 1;" + "  } else {" + "    var x = 2;" + "    return x;" + "  }" + "}";

    root = AbstractCompiler.compile(compiler, input);

    eliminator.process(null, root);

    assertTrue(NodeUtil.getChildrenCount(root.getLastChild()) == 0);

    // Test case 4: Dead assignment elimination with loops
    input = "function foo() {" + "  for (var i = 0; i < 10; i++) {" + "    var j = i;" + "  }" + "  return i;" + "}";
    root = AbstractCompiler.compile(compiler, input);

    eliminator.process(null, root);

    assertTrue(NodeUtil.getChildrenCount(root.getLastChild()) == 1);
    assertTrue(NodeUtil.getString(root.getLastChild().getFirstChild()).equals("10"));

    input = "function foo() {" + "  for (var i = 0; i < 10; i++) {" + "    var j = i;" + "    j = i + 1;" + "  }" + "  return i;" + "}";

    root = AbstractCompiler.compile(compiler, input);

    eliminator.process(null, root);

    assertTrue(NodeUtil.getChildrenCount(root.getLastChild()) == 1);
    assertTrue(NodeUtil.getString(root.getLastChild().getFirstChild()).equals("10"));

    // Test case 5: Dead assignment elimination with multiple assignments
    input = "function foo() {" + "  var x = 1, y = 1;" + "  x = 2, y = 3;" + "  return x + y;" + "}";
    root = AbstractCompiler.compile(compiler, input);

    eliminator.process(null, root);

    assertTrue(NodeUtil.getChildrenCount(root.getLastChild()) == 2);
    assertTrue(NodeUtil.getString(root.getLastChild().getChildAt(0)).equals("2"));
    assertTrue(NodeUtil.getString(root.getLastChild().getChildAt(1)).equals("3"));

    // Test case 6: Dead assignment elimination with multiple conditions
    input = "function foo() {" + "  if (true) {" + "    var x = 1;" + "  } else if (true) {" + "    var x = 2;" + "  } else {" + "    var x = 3;" + "  }" + "  return x;" + "}";
    root = AbstractCompiler.compile(compiler, input);

    eliminator.process(null, root);

    assertTrue(NodeUtil.getChildrenCount(root.getLastChild()) == 1);
    assertTrue(NodeUtil.getString(root.getLastChild().getFirstChild()).equals("1"));

    // Test case 7: Dead assignment elimination with nested conditions
    input = "function foo() {" + "  if (true) {" + "    if (true) {" + "      var x = 1;" + "    } else {" + "      var x = 2;" + "    }" + "  } else {" + "    var x = 3;" + "  }" + "  return x;" + "}";
    root = AbstractCompiler.compile(compiler, input);

    eliminator.process(null, root);

    assertTrue(NodeUtil.getChildrenCount(root.getLastChild()) == 1);
    assertTrue(NodeUtil.getString(root.getLastChild().getFirstChild()).equals("1"));

    // Test case 8: Dead assignment elimination with function calls
    input = "function foo() {" + "  var x = 1;" + "  x = bar();" + "  return x;" + "}";
    root = AbstractCompiler.compile(compiler, input);

    eliminator.process(null, root);

    assertTrue(NodeUtil.getChildrenCount(root.getLastChild()) == 1);
    assertTrue(NodeUtil.getString(root.getLastChild().getFirstChild()).equals("1")); // Assumption: bar() returns 1

    // Test case 9: Dead assignment elimination with complex expressions
    input = "function foo() {" + "  var x = 1;" + "  x = ((y = 2) + 3) * 2;" + "  return x;" + "}";
    root = AbstractCompiler.compile(compiler, input);

    eliminator.process(null, root);

    assertTrue(NodeUtil.getChildrenCount(root.getLastChild()) == 1);
    assertTrue(NodeUtil.getString(root.getLastChild().getFirstChild()).equals("19")); // Assumption: y is not dead
  }

  @Test
  public void testDeadAssignmentsEliminationWithLoopsAndFunctions() {
    // Test case: Dead assignment elimination with loops and functions
    AbstractCompiler compiler = new AbstractCompiler();
    String input = "function foo() {" + "  var x = 0;" + "  for (var i = 0; i < 10; i++) {" + "    x = increment(i)" + "  }" + "  return x;" + "}";
    Node root = AbstractCompiler.compile(compiler, input);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    // Assumption: increment function returns the input + 1
    assertTrue(NodeUtil.getChildrenCount(root.getLastChild()) == 11);
    for (int i = 0; i < 10; i++) {
      assertTrue(NodeUtil.getString(root.getLastChild().getChildAt(i + 1)).equals(String.valueOf(i + 1)));
    }
    assertTrue(NodeUtil.getString(root.getLastChild().getLastChild()).equals("10"));
  }

  @Test
  public void testDeadAssignmentsEliminationWithNestedFunctionCalls() {
    // Test case: Dead assignment elimination with nested function calls
    AbstractCompiler compiler = new AbstractCompiler();
    String input = "function foo() {" + "  function bar() {" + "    var x = 1;" + "    x = baz()" + "  }" + "  var x = 0;" + "  bar()" + "  return x;" + "}";
    Node root = AbstractCompiler.compile(compiler, input);

    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);
    eliminator.process(null, root);

    // Assumption: baz function returns 1
    assertTrue(NodeUtil.getChildrenCount(root.getLastChild()) == 2);

    Node barFunction = root.getChildAt(0);
    assertTrue(NodeUtil.getChildrenCount(barFunction) == 2);
    assertTrue(NodeUtil.getString(barFunction.getChildAt(0)).equals("1")); // Assumption: x is initialized to 1 in the bar function
    assertTrue(NodeUtil.getChildrenCount(barFunction.getChildAt(1)) == 1);
    assertTrue(NodeUtil.getString(barFunction.getChildAt(1).getFirstChild()).equals("1")); // Assumption: baz function returns 1

    assertTrue(NodeUtil.getChildrenCount(root.getLastChild()) == 1);
    assertTrue(NodeUtil.getString(root.getLastChild().getFirstChild()).equals("1")); // Assumption: x is initialized to 0 in the foo function
  }
}