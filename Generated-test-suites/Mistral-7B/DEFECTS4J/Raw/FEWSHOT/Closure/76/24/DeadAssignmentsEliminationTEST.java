package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    // Test case 1: Simple assignment
    AbstractCompiler compiler = new AbstractCompiler();
    String testCode = "function foo() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "}";
    Node root = AbstractNodeParser.parse(testCode);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(1, nodeTraversal.getNumChildren(root));
    assertNull(NodeUtil.findChildWithName(root, "x"));

    // Test case 2: Assignment with condition
    testCode = "function foo() {" +
        "  var x = 1;" +
        "  if (true) {" +
        "    x = 2;" +
        "  }" +
        "}";
    root = AbstractNodeParser.parse(testCode);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);
    nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(1, nodeTraversal.getNumChildren(root));
    assertNull(NodeUtil.findChildWithName(root, "x"));

    // Test case 3: Assignment within a loop
    testCode = "function foo() {" +
        "  for (var i = 0; i < 10; i++) {" +
        "    var x = i;" +
        "    x = i + 1;" +
        "  }" +
        "}";
    root = AbstractNodeParser.parse(testCode);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);
    nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(1, nodeTraversal.getNumChildren(root));
    assertNull(NodeUtil.findChildWithName(root, "x"));

    // Test case 4: Assignment with multiple conditions
    testCode = "function foo() {" +
        "  var x = 1;" +
        "  if (true) {" +
        "    if (true) {" +
        "      x = 2;" +
        "    }" +
        "  }" +
        "}";
    root = AbstractNodeParser.parse(testCode);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);
    nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(1, nodeTraversal.getNumChildren(root));
    assertNull(NodeUtil.findChildWithName(root, "x"));

    // Test case 5: Assignment with multiple statements
    testCode = "function foo() {" +
        "  var x = 1;" +
        "  x = 2;" +
        "  var y = 3;" +
        "  x = 3;" +
        "}";
    root = AbstractNodeParser.parse(testCode);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);
    nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(2, nodeTraversal.getNumChildren(root));
    assertNull(NodeUtil.findChildWithName(root, "x"));
    assertEquals(1, nodeTraversal.getNumChildren(NodeUtil.findChildWithName(root, "y")));

    // Test case 6: Assignment with function call
    testCode = "function foo() {" +
        "  var x = 1;" +
        "  x = func()" +
        "  function func() {" +
        "    return 2;" +
        "  }" +
        "}";
    root = AbstractNodeParser.parse(testCode);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);
    nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(3, nodeTraversal.getNumChildren(root));
    assertNull(NodeUtil.findChildWithName(root, "x"));
    assertEquals(1, nodeTraversal.getNumChildren(NodeUtil.findChildWithName(root, "func")));
    assertEquals(1, nodeTraversal.getNumChildren(NodeUtil.findChildWithName(root, "func").getLastChild()));

    // Test case 7: Assignment with increment/decrement operator
    testCode = "function foo() {" +
        "  var x = 1;" +
        "  x++;" +
        "}";
    root = AbstractNodeParser.parse(testCode);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);
    nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(1, nodeTraversal.getNumChildren(root));
    assertNull(NodeUtil.findChildWithName(root, "x"));

    // Test case 8: Assignment with multiple increment/decrement operators
    testCode = "function foo() {" +
        "  var x = 1;" +
        "  x++;" +
        "  x--;" +
        "}";
    root = AbstractNodeParser.parse(testCode);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);
    nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(1, nodeTraversal.getNumChildren(root));
    assertNull(NodeUtil.findChildWithName(root, "x"));

    // Test case 9: Assignment with multiple variables
    testCode = "function foo() {" +
        "  var x = 1, y = 2;" +
        "  x = 2, y = 3;" +
        "}";
    root = AbstractNodeParser.parse(testCode);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);
    nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(2, nodeTraversal.getNumChildren(root));
    assertNull(NodeUtil.findChildWithName(root, "x"));
    assertEquals(1, nodeTraversal.getNumChildren(NodeUtil.findChildWithName(root, "y")));

    // Test case 10: Assignment with multiple conditions and multiple variables
    testCode = "function foo() {" +
        "  var x = 1, y = 2;" +
        "  if (true) {" +
        "    if (true) {" +
        "      x = 2;" +
        "    }" +
        "    if (true) {" +
        "      y = 3;" +
        "    }" +
        "  }" +
        "}";
    root = AbstractNodeParser.parse(testCode);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);
    nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse(compiler, root, deadAssignmentsElimination);
    assertEquals(2, nodeTraversal.getNumChildren(root));
    assertNull(NodeUtil.findChildWithName(root, "x"));
    assertEquals(1, nodeTraversal.getNumChildren(NodeUtil.findChildWithName(root, "y")));
  }
}