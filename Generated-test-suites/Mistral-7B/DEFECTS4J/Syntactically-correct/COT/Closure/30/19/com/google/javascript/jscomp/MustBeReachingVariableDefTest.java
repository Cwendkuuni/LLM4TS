package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.ControlFlowGraph;

@RunWith(JUnit4.class)
public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDefConstructor() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    assertNotNull(mustBeReachingVariableDef);
  }

  @Test
  public void testDefinitionClassConstructor() {
    Node node = Node.newIntLiteral(1);
    Definition definition = new Definition(node);
    assertEquals(node, definition.node);
    assertNotNull(definition.depends);
  }

  @Test
  public void testMustDefClass() {
    MustDef mustDef = new MustDef();
    assertNotNull(mustDef.reachingDef);
  }

  @Test
  public void testMustDefJoinClass() {
    MustDef a = new MustDef();
    MustDef b = new MustDef();
    MustDefJoin mustDefJoin = new MustDefJoin();
    MustDef result = mustDefJoin.apply(a, b);
    assertNotNull(result.reachingDef);
  }

  @Test
  public void testIsForwardMethod() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        null, null, null);
    assertTrue(mustBeReachingVariableDef.isForward());
  }

  @Test
  public void testCreateEntryLatticeMethod() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        null, null, null);
    MustDef entryLattice = mustBeReachingVariableDef.createEntryLattice();
    assertNotNull(entryLattice.reachingDef);
  }

  @Test
  public void testCreateInitialEstimateLatticeMethod() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        null, null, null);
    MustDef initialEstimateLattice = mustBeReachingVariableDef.createInitialEstimateLattice();
    assertNotNull(initialEstimateLattice.reachingDef);
  }

  @Test
  public void testFlowThroughMethod() {
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        null, null, null);
    Node node = Node.newIntLiteral(1);
    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(node, input);
    assertNotNull(output.reachingDef);
  }

  @Test
  public void testComputeMustDefMethodWithSimpleExpression() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);
    Node n = Node.newAssignment(Node.newName("x"), Node.newName("y"));
    Node rootNode = Node.newBlock();
    Node cfgNode = Node.newBlock();
    mustBeReachingVariableDef.computeMustDef(n, cfgNode, new MustDef(), false);

    Node useNode = Node.newName("x");
    Node def = mustBeReachingVariableDef.getDef("y", useNode);
    assertEquals(cfgNode, def);
  }

  @Test
  public void testComputeMustDefMethodWithIfStatement() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);

    Node ifCondition = Node.newBinaryOp(Token.IF, Node.newName("x"), Node.newIntLiteral(0));
    Node ifBody = Node.newBlock();
    Node elseBody = Node.newBlock();
    Node rootNode = Node.newBlock();
    Node cfgNode = Node.newBlock();

    Node assignX = Node.newAssignment(Node.newName("x"), Node.newName("y"));
    Node assignY = Node.newAssignment(Node.newName("y"), Node.newIntLiteral(1));
    ifBody.addChild(assignX);
    ifBody.addChild(assignY);
    elseBody.addChild(Node.newAssignment(Node.newName("y"), Node.newIntLiteral(2)));

    rootNode.addChild(ifCondition);
    rootNode.addChild(ifBody);
    rootNode.addChild(elseBody);

    cfg.addNode(cfgNode);
    cfg.addEdge( cfgNode, NodeUtil.makeBlock(ifCondition), Token.ON_TRUE );
    cfg.addEdge( cfgNode, NodeUtil.makeBlock(elseBody), Token.ON_FALSE );

    mustBeReachingVariableDef.computeMustDef(ifCondition, cfgNode, new MustDef(), false);

    Node useNodeX = Node.newName("x");
    Node useNodeY = Node.newName("y");

    Node defX = mustBeReachingVariableDef.getDef("y", useNodeX);
    assertEquals(cfgNode, defX);

    Node defYIf = mustBeReachingVariableDef.getDef("y", useNodeY);
    assertEquals(NodeUtil.makeBlock(assignY), defYIf);

    Node defYElse = mustBeReachingVariableDef.getDef("y", useNodeY);
    assertEquals(NodeUtil.makeBlock(assignY), defYElse);
  }

  @Test
  public void testComputeMustDefMethodWithWhileLoop() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);

    Node whileCondition = Node.newBinaryOp(Token.WHILE, Node.newName("x"), Node.newIntLiteral(0));
    Node whileBody = Node.newBlock();
    Node whileLoop = Node.newBlock();
    Node rootNode = Node.newBlock();

    Node assignX = Node.newAssignment(Node.newName("x"), Node.newIntLiteral(1));
    Node assignY = Node.newAssignment(Node.newName("y"), Node.newIntLiteral(1));
    whileBody.addChild(assignX);
    whileBody.addChild(assignY);
    whileLoop.addChild(whileCondition);
    whileLoop.addChild(whileBody);
    rootNode.addChild(whileLoop);

    cfg.addNode(cfg.getEntryNode());
    cfg.addEdge( cfg.getEntryNode(), whileLoop, Token.ON_TRUE );

    mustBeReachingVariableDef.computeMustDef(whileCondition, cfg.getEntryNode(), new MustDef(), false);

    Node useNodeX = Node.newName("x");
    Node useNodeY = Node.newName("y");

    Node defX = mustBeReachingVariableDef.getDef("x", useNodeX);
    assertEquals(cfg.getEntryNode(), defX);

    Node defY = mustBeReachingVariableDef.getDef("y", useNodeY);
    assertEquals(NodeUtil.makeBlock(assignY), defY);
  }

  @Test
  public void testComputeMustDefMethodWithConditionalAssignment() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);

    Node condition = Node.newBinaryOp(Token.IF, Node.newName("x"), Node.newIntLiteral(0));
    Node assignA = Node.newAssignment(Node.newName("a"), Node.newIntLiteral(1));
    Node assignB = Node.newAssignment(Node.newName("b"), Node.newIntLiteral(2));
    Node rootNode = Node.newBlock();

    Node ifTrue = Node.newBlock();
    ifTrue.addChild(condition);
    ifTrue.addChild(assignA);
    rootNode.addChild(ifTrue);

    Node ifFalse = Node.newBlock();
    ifFalse.addChild(assignB);
    rootNode.addChild(ifFalse);

    cfg.addNode(cfg.getEntryNode());
    cfg.addEdge( cfg.getEntryNode(), ifTrue, Token.ON_TRUE );
    cfg.addEdge( cfg.getEntryNode(), ifFalse, Token.ON_FALSE );

    mustBeReachingVariableDef.computeMustDef(condition, cfg.getEntryNode(), new MustDef(), false);

    Node useNodeA = Node.newName("a");
    Node useNodeB = Node.newName("b");

    Node defA = mustBeReachingVariableDef.getDef("a", useNodeA);
    assertEquals(cfg.getEntryNode(), defA);

    Node defB = mustBeReachingVariableDef.getDef("b", useNodeB);
    assertEquals(cfg.getEntryNode(), defB);
  }

  @Test
  public void testGetDefMethodWithVariableOutsideScope() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);

    Node rootNode = Node.newBlock();
    Node useNode = Node.newName("outerScopeVar");
    rootNode.addChild(useNode);

    cfg.addNode(cfg.getEntryNode());
    cfg.addEdge(cfg.getEntryNode(), rootNode, Token.ON_TRUE);

    MustDef mustDef = mustBeReachingVariableDef.flowThrough(useNode, new MustDef());

    assertNull(mustDef.reachingDef.get("outerScopeVar"));
  }

  @Test
  public void testDependsonOuterScopeVarsMethod() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        cfg, jsScope, compiler);

    Node rootNode = Node.newBlock();
    Node useNode = Node.newName("outerScopeVar");
    Node varNode = Node.newName("localVar");
    rootNode.addChild(useNode);
    rootNode.addChild(varNode);

    cfg.addNode(cfg.getEntryNode());
    cfg.addEdge(cfg.getEntryNode(), rootNode, Token.ON_TRUE);

    MustDef mustDef = mustBeReachingVariableDef.flowThrough(varNode, new MustDef());

    assertTrue(mustBeReachingVariableDef.dependsOnOuterScopeVars("outerScopeVar", useNode));
  }
}