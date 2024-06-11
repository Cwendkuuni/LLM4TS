package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.CompilationLevel;
import com.google.javascript.jscomp.CompilerOptions.Hints;
import com.google.javascript.jscomp.GraphTestUtil;
import com.google.javascript.rhino.Node;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    String source = "function f() {" +
        "  var x = 1, y = 1;" +
        "  x = y;" +
        "  y = 2;" +
        "  print(x);" +
        "}";

    CompilerOptions options = new CompilerOptions();
    options.setSourceFile("DeadAssignmentsEliminationTest.js");
    options.setOutputFile("DeadAssignmentsEliminationTest-compiled.js");
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    options.setCompilationLevel(CompilationLevel.SIMPLE);
    options.setHint(Hints.FORCE_UNDEF, "print");

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(new Compiler(options));

    Node root = GraphTestUtil.parseAndTraverse(source, deadAssignmentsElimination);

    // Basic functionality test
    assertTrue(deadAssignmentsElimination.tryRemoveDeadAssignments(null, null));

    // Test for dead assignments when there is at least one inner function
    String innerFunctionSource = "function g() {" +
        "  var z = 1;" +
        "  function h() {" +
        "    z = 2;" +
        "  }" +
        "  h();" +
        "}";

    Node innerFunctionNode = GraphTestUtil.parseAndTraverse(innerFunctionSource, null);
    Node functionBlock = new Node(Token.BLOCK, null, null);
    functionBlock.appendChild(new Node(Token.FUNCTION,
        Node.newName("g"), functionBlock, null));
    root.appendChild(functionBlock);

    deadAssignmentsElimination.tryRemoveDeadAssignments(null, null);

    // Test for dead assignments when there are no assigns to eliminate
    String noAssignmentsSource = "function f() {" +
        "  return 1;" +
        "}";

    Node noAssignmentsNode = GraphTestUtil.parseAndTraverse(noAssignmentsSource, null);
    root.replaceChild(root.getFirstChild(), noAssignmentsNode);

    deadAssignmentsElimination.tryRemoveDeadAssignments(null, null);

    // Test for dead assignments when there are no variables to eliminate
    String noVariablesSource = "function f() {" +
        "  return 1 + 1;" +
        "}";

    Node noVariablesNode = GraphTestUtil.parseAndTraverse(noVariablesSource, null);
    root.replaceChild(root.getFirstChild(), noVariablesNode);

    deadAssignmentsElimination.tryRemoveDeadAssignments(null, null);

    // Test for dead assignments with a global variable
    String globalVariableSource = "var x = 1;" +
        "function f() {" +
        "  x = 2;" +
        "}";

    Node globalVariableNode = GraphTestUtil.parseAndTraverse(globalVariableSource, null);
    root.replaceChild(root.getFirstChild(), globalVariableNode);

    deadAssignmentsElimination.tryRemoveDeadAssignments(null, null);

    // Test for dead assignments with a variable that is declared but not assigned
    String undeclaredVariableSource = "function f() {" +
        "  var x;" +
        "  x = 2;" +
        "}";

    Node undeclaredVariableNode = GraphTestUtil.parseAndTraverse(undeclaredVariableSource, null);
    root.replaceChild(root.getFirstChild(), undeclaredVariableNode);

    deadAssignmentsElimination.tryRemoveDeadAssignments(null, null);

    // Test for dead assignments with a variable that is already dead
    String deadVariableSource = "function f() {" +
        "  var x, y = 1;" +
        "  x = y;" +
        "  y = null;" +
        "  x = 2;" +
        "}";

    Node deadVariableNode = GraphTestUtil.parseAndTraverse(deadVariableSource, null);
    root.replaceChild(root.getFirstChild(), deadVariableNode);

    deadAssignmentsElimination.tryRemoveDeadAssignments(null, null);

    // Test for dead assignments with a variable that is read after being killed
    String readAfterKilledSource = "function f() {" +
        "  var x = 1, y = 1;" +
        "  x = y;" +
        "  y = 2;" +
        "  print(x);" +
        "}";

    Node readAfterKilledNode = GraphTestUtil.parseAndTraverse(readAfterKilledSource, null);
    root.replaceChild(root.getFirstChild(), readAfterKilledNode);

    deadAssignmentsElimination.tryRemoveDeadAssignments(null, null);

    // Test for dead assignments with a variable that is both read and killed
    String readAndKilledSource = "function f() {" +
        "  var x = 1, y = 1;" +
        "  x = y;" +
        "  y = 2;" +
        "  print(x);" +
        "  x = 3;" +
        "}";

    Node readAndKilledNode = GraphTestUtil.parseAndTraverse(readAndKilledSource, null);
    root.replaceChild(root.getFirstChild(), readAndKilledNode);

    deadAssignmentsElimination.tryRemoveDeadAssignments(null, null);
  }

}