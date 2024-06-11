package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeUtil.isAssign;
import static com.google.javascript.rhino.NodeUtil.isName;
import static com.google.javascript.rhino.NodeUtil.newNode;
import static com.google.javascript.rhino.NodeUtil.newNumber;
import static com.google.javascript.rhino.Token.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    // Given
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope scope = new Scope();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    // When
    Node root = newNode(AND, newNode(NAME, "x"), newNode(NAME, "y"));
    Node n1 = newNode(ASSIGN, new Node(NAME, "x"), newNumber(1));
    Node n2 = newNode(ASSIGN, new Node(NAME, "y"), newNumber(2));
    Node n3 = newNode(ASSIGN, new Node(NAME, "x"), newNode(NAME, "y"));
    Node n4 = newNode(ASSIGN, new Node(NAME, "x"), newNumber(3));

    root.addChild(n1);
    root.addChild(n2);
    root.addChild(n3);
    root.addChild(n4);

    deadAssignmentsElimination.process(null, root);

    // Then
    assertTrue(isAssign(n1));
    assertFalse(isAssign(n3));

    // Edge cases

    // Test for variable assignments within conditionals
    Node conditional1 = newNode(IF, newNode(NAME, "x"), newNode(NAME, "y"));
    Node assign1 = newNode(ASSIGN, new Node(NAME, "x"), newNumber(1));
    Node assign2 = newNode(ASSIGN, new Node(NAME, "x"), newNode(NAME, "y"));

    conditional1.addChild(assign1);
    conditional1.addChild(assign2);

    Node conditional2 = newNode(ELSE, conditional1);
    Node assign3 = newNode(ASSIGN, new Node(NAME, "x"), newNumber(2));

    conditional2.addChild(assign3);

    root.addChild(conditional1);
    root.addChild(conditional2);

    deadAssignmentsElimination.process(null, root);

    assertTrue(isAssign(assign1));
    assertFalse(isAssign(assign2));

    // Test for variable assignments within loops
    Node loop = newNode(FOR, newNode(NAME, "i"), newNode(NAME, "i"), null);
    Node assign4 = newNode(ASSIGN, new Node(NAME, "i"), newNumber(1));
    Node assign5 = newNode(ASSIGN, new Node(NAME, "i"), newNode(NAME, "i"));

    loop.addChild(assign4);
    loop.addChild(assign5);

    root.addChild(loop);

    deadAssignmentsElimination.process(null, root);

    assertTrue(isAssign(assign4));
    assertFalse(isAssign(assign5));

    // Test for variable assignments in function calls
    Node functionCall = newNode(NAME, "myFunction");
    Node functionBody = newNode(BLOCK, null);
    Node assign6 = newNode(ASSIGN, new Node(NAME, "x"), newNumber(1));

    functionBody.addChild(assign6);
    functionCall.addChild(functionBody);

    root.addChild(functionCall);

    deadAssignmentsElimination.process(null, root);

    assertTrue(isAssign(assign6));

    // Test for variable assignments in switch statements
    Node switchStatement = newNode(SWITCH, newNode(NAME, "x"), null);
    Node caseStatement = newNode(CASE, newNode(NUMBER, 1), null);
    Node assign7 = newNode(ASSIGN, new Node(NAME, "x"), newNumber(1));

    caseStatement.addChild(assign7);
    switchStatement.addChild(caseStatement);

    root.addChild(switchStatement);

    deadAssignmentsElimination.process(null, root);

    assertTrue(isAssign(assign7));

    // Test for variable assignments in return statements
    Node returnStatement = newNode(RETURN, newNode(NAME, "x"));
    Node assign8 = newNode(ASSIGN, new Node(NAME, "x"), newNumber(1));

    root.addChild(assign8);
    root.addChild(returnStatement);

    deadAssignmentsElimination.process(null, root);

    assertTrue(isAssign(assign8));
  }
}