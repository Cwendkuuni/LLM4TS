package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testCreateEntryLattice() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        null, null, null);
    MustBeReachingVariableDef.MustDef entryLattice = mustBeReachingVariableDef.createEntryLattice();

    // Check that all variables in the scope are present in the entry lattice.
    for (Var var : mustBeReachingVariableDef.jsScope.getVars()) {
      assertTrue(entryLattice.reachingDef.containsKey(var));
      assertEquals(var.scope.getRootNode(), entryLattice.reachingDef.get(var).node);
    }
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        null, null, null);
    MustBeReachingVariableDef.MustDef initialEstimateLattice = mustBeReachingVariableDef.createInitialEstimateLattice();

    // Check that all variables have TOP as their lattice value.
    for (Var var : mustBeReachingVariableDef.jsScope.getVars()) {
      assertEquals(null, initialEstimateLattice.reachingDef.get(var));
    }
  }

  @Test
  public void testFlowThrough() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        null, null, null);
    Node assignmentNode = Node.newAssignment(Token.ASSIGN, null, Node.newName("x"), Node.newName("y"));

    MustBeReachingVariableDef.MustDef inputLattice = new MustBeReachingVariableDef.MustDef();
    inputLattice.reachingDef.put(mustBeReachingVariableDef.jsScope.getVar("x"), new MustBeReachingVariableDef.Definition(Node.newName("assignmentNode")));

    MustBeReachingVariableDef.MustDef outputLattice = mustBeReachingVariableDef.flowThrough(assignmentNode, inputLattice);

    // Check that the definition of 'x' in the output lattice is the same as
    // the definition in the input lattice.
    assertEquals(inputLattice.reachingDef.get(mustBeReachingVariableDef.jsScope.getVar("x")), outputLattice.reachingDef.get(mustBeReachingVariableDef.jsScope.getVar("x")));
  }

  @Test(expected = AssertionError.class)
  public void testFlowThroughWithInvalidNodeType() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        null, null, null);

    Node invalidNode = Node.newUndefined();
    MustBeReachingVariableDef.MustDef inputLattice = new MustBeReachingVariableDef.MustDef();

    try {
      mustBeReachingVariableDef.flowThrough(invalidNode, inputLattice);
    } catch (RuntimeException e) {
      fail("Expected AssertionError, but got " + e.getClass().getName());
    }
  }

  @Test
  public void testComputeMustDefForAssignment() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        null, null, null);

    Node assignmentNode = Node.newAssignment(Token.ASSIGN, null, Node.newName("x"), Node.newName("y"));
    Node definitionNode = Node.newName("definitionNode");

    MustBeReachingVariableDef.MustDef inputLattice = new MustBeReachingVariableDef.MustDef();
    inputLattice.reachingDef.put(mustBeReachingVariableDef.jsScope.getVar("x"), new MustBeReachingVariableDef.Definition(definitionNode));

    MustBeReachingVariableDef.MustDef outputLattice = mustBeReachingVariableDef.computeMustDef(assignmentNode, definitionNode, null, false);

    // Check that the definition of 'x' in the output lattice is the same as
    // the definition in the input lattice.
    assertEquals(inputLattice.reachingDef.get(mustBeReachingVariableDef.jsScope.getVar("x")), outputLattice.reachingDef.get(mustBeReachingVariableDef.jsScope.getVar("x")));
  }

  @Test(expected = AssertionError.class)
  public void testComputeMustDefForInvalidNodeType() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        null, null, null);

    Node invalidNode = Node.newUndefined();
    Node definitionNode = Node.newName("definitionNode");

    MustBeReachingVariableDef.MustDef inputLattice = new MustBeReachingVariableDef.MustDef();

    try {
      mustBeReachingVariableDef.computeMustDef(invalidNode, definitionNode, null, false);
    } catch (RuntimeException e) {
      fail("Expected AssertionError, but got " + e.getClass().getName());
    }
  }

  @Test
  public void testComputeMustDefForWhileLoop() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        null, null, null);

    Node conditionNode = Node.newBinaryOp(Token.WHILE, Token.LT,
        Node.newName("x"), Node.newNumber(10));
    Node assignmentNode = Node.newAssignment(Token.ASSIGN, null, Node.newName("x"), Node.newNumber(5));
    Node loopNode = Node.newBlock();
    loopNode.addChild(conditionNode);
    loopNode.addChild(assignmentNode);
    loopNode.addChild(Node.newReturn(Node.newName("x")));

    MustBeReachingVariableDef.MustDef inputLattice = new MustBeReachingVariableDef.MustDef();
    inputLattice.reachingDef.put(mustBeReachingVariableDef.jsScope.getVar("x"), new MustBeReachingVariableDef.Definition(Node.newName("assignmentNodeInsideLoop")));

    MustBeReachingVariableDef.MustDef outputLattice = mustBeReachingVariableDef.computeMustDef(loopNode, null, null, false);

    // Check that the definition of 'x' in the output lattice is the same as
    // the definition inside the loop.
    Definition definitionNodeInsideLoop = inputLattice.reachingDef.get(mustBeReachingVariableDef.jsScope.getVar("x"));
    Definition definitionNodeInOutputLattice = outputLattice.reachingDef.get(mustBeReachingVariableDef.jsScope.getVar("x"));
    assertEquals(definitionNodeInsideLoop, definitionNodeInOutputLattice);
  }
}