package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.HashSet;
import java.util.Set;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustDefJoin() {
    Set<Var> vars = Sets.newHashSet(
        new Var("x", new Scope.Root()),
        new Var("y", new Scope.Root()),
        new Var("z", new Scope.Root())
    );

    MustDef joinA = new MustDef(vars);
    MustDef joinB = new MustDef();

    MustDef result = MustDefJoin.apply(joinA, joinB);
    assertEquals(joinA, result);

    vars.add(new Var("w", new Scope.Root()));

    MustDef joinC = new MustDef(vars);
    result = MustDefJoin.apply(joinA, joinC);
    assertEquals(joinC, result);

    result = MustDefJoin.apply(joinB, joinC);
    MustDef expected = new MustDef();
    expected.reachingDef.put(new Var("x"), null);
    expected.reachingDef.put(new Var("y"), null);
    expected.reachingDef.put(new Var("z"), null);
    expected.reachingDef.put(new Var("w"), null);
    assertEquals(expected, result);
  }

  @Test
  public void testMustDefForward() {
    Scope scope = new Scope(new Scope.Root());
    scope.declareVar("x", Node.INT_LITERAL, null);
    scope.declareVar("y", Node.INT_LITERAL, null);
    scope.declareVar("z", Node.INT_LITERAL, null);

    ControlFlowGraph cfg = new ControlFlowGraph();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, scope, new AbstractCompiler());

    Node rootNode = Node.BLOCK();

    Node assignmentX = Node.ASSIGNMENT(Node.IDENT("x"), Node.INT_LITERAL(1));
    Node assignmentY = Node.ASSIGNMENT(Node.IDENT("y"), Node.INT_LITERAL(2));
    Node assignmentZ = Node.ASSIGNMENT(Node.IDENT("z"), Node.INT_LITERAL(3));

    rootNode.addChild(assignmentX);
    rootNode.addChild(assignmentY);
    rootNode.addChild(assignmentZ);

    Node useX = Node.IDENT("x");
    Node useY = Node.IDENT("y");
    Node useZ = Node.IDENT("z");

    Node block = Node.BLOCK();
    block.addChild(useX);
    block.addChild(useY);
    block.addChild(useZ);

    rootNode.addChild(block);

    MustDef input = analysis.createEntryLattice();
    MustDef output = analysis.flowThrough(rootNode, input);

    Node expectedXDef = Node.IDENT("x");
    Node expectedYDef = Node.IDENT("y");
    Node expectedZDef = Node.IDENT("z");

    assertEquals(expectedXDef, analysis.getDef("x", useX));
    assertEquals(expectedYDef, analysis.getDef("y", useY));
    assertEquals(expectedZDef, analysis.getDef("z", useZ));

    assertNull(analysis.getDef("x", Node.IDENT("w")));
    assertNull(analysis.getDef("y", Node.IDENT("w")));
    assertNull(analysis.getDef("z", Node.IDENT("w")));

    assertEquals(expectedXDef, analysis.getDef("x", Node.IDENT("x")));
    assertEquals(expectedYDef, analysis.getDef("y", Node.IDENT("y")));
    assertEquals(expectedZDef, analysis.getDef("z", Node.IDENT("z")));

    assertTrue(analysis.dependsOnOuterScopeVars("x", useX));
    assertFalse(analysis.dependsOnOuterScopeVars("x", useX));
    assertFalse(analysis.dependsOnOuterScopeVars("y", useX));
    assertFalse(analysis.dependsOnOuterScopeVars("z", useX));
  }

  @Test
  public void testMustDefConditional() {
    Scope scope = new Scope(new Scope.Root());
    scope.declareVar("x", Node.INT_LITERAL, null);
    scope.declareVar("y", Node.INT_LITERAL, null);
    scope.declareVar("z", Node.INT_LITERAL, null);

    ControlFlowGraph cfg = new ControlFlowGraph();
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        cfg, scope, new AbstractCompiler());

    Node rootNode = Node.BLOCK();

    Node ifNode = Node.IF(Node.IDENT("x"), Node.BLOCK(), Node.BLOCK());

    Node assignmentX = Node.ASSIGNMENT(Node.IDENT("x"), Node.INT_LITERAL(1));
    Node assignmentY = Node.ASSIGNMENT(Node.IDENT("y"), Node.INT_LITERAL(2));
    Node assignmentZ = Node.ASSIGNMENT(Node.IDENT("z"), Node.INT_LITERAL(3));

    rootNode.addChild(assignmentX);
    rootNode.addChild(ifNode);

    Node blockIfTrue = Node.BLOCK();
    Node assignmentXIfTrue = Node.ASSIGNMENT(Node.IDENT("x"), Node.INT_LITERAL(4));
    blockIfTrue.addChild(assignmentXIfTrue);

    Node blockIfFalse = Node.BLOCK();
    Node assignmentXIfFalse = Node.ASSIGNMENT(Node.IDENT("x"), Node.INT_LITERAL(5));
    blockIfFalse.addChild(assignmentXIfFalse);

    ifNode.setNextChild(blockIfTrue);
    ifNode.addChild(blockIfFalse);

    Node useX = Node.IDENT("x");

    rootNode.addChild(useX);

    MustDef input = analysis.createEntryLattice();
    MustDef output = analysis.flowThrough(rootNode, input);

    Node expectedXDefIfTrue = Node.IDENT("x");
    Node expectedXDefIfFalse = Node.IDENT("x");

    assertEquals(expectedXDefIfTrue, analysis.getDef("x", useX));

    analysis.reset();

    MustDef input2 = analysis.createEntryLattice();
    MustDef output2 = analysis.flowThrough(rootNode, input2);

    assertEquals(expectedXDefIfFalse, analysis.getDef("x", useX));
  }
}