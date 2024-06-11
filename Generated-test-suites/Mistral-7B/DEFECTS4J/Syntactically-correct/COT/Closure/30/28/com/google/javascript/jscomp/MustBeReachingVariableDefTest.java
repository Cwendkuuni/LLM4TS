package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

public class MustBeReachingVariableDefTest {

  private MustBeReachingVariableDef mustBeReachingVariableDef;
  private Node rootNode;
  private Scope jsScope;
  private ControlFlowGraph<Node> cfg;

  @Before
  public void setup() {
    // Initialize test data
    rootNode = Node.newBlock(0);
    jsScope = new Scope();
    jsScope.setRootNode(rootNode);

    cfg = new ControlFlowGraph<Node>(rootNode, new AbstractCompiler());

    mustBeReachingVariableDef = new MustBeReachingVariableDef(cfg, jsScope, new AbstractCompiler());
  }

  // Test basic functionality of each public method

  @Test
  public void testCreateEntryLattice() {
    MustDef entryLattice = mustBeReachingVariableDef.createEntryLattice();
    assertNotNull(entryLattice);
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MustDef initialEstimateLattice = mustBeReachingVariableDef.createInitialEstimateLattice();
    assertNotNull(initialEstimateLattice);
  }

  @Test
  public void testFlowThrough() {
    Node testNode = Node.newName("testNode");
    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(testNode, input);
    assertEquals(input, output);
  }

  // Test edge cases and exception handling scenarios

  @Test(expected = IllegalStateException.class)
  public void testIsForwardWithFalse() {
    MustBeReachingVariableDef mustBeReachingVariableDefWithFalse =
        new MustBeReachingVariableDef(cfg, jsScope, new AbstractCompiler()) {
          @Override
          boolean isForward() {
            return false;
          }
        };
    mustBeReachingVariableDefWithFalse.flowThrough(rootNode, new MustDef());
  }

  @Test
  public void testFlowThroughWithConditional() {
    Node conditionNode = Node.newIf(Node.newBool(true), Node.newName("conditionNodeTrue"), Node.newName("conditionNodeFalse"));
    Node thenNode = Node.newName("thenNode");
    Node elseNode = Node.newName("elseNode");

    rootNode.addChild(conditionNode);
    rootNode.addChild(thenNode);
    rootNode.addChild(elseNode);

    MustDef input = new MustDef();
    MustDef expectedOutput = new MustDef();
    mustBeReachingVariableDef.computeMustDef(conditionNode, conditionNode, expectedOutput, true);
    mustBeReachingVariableDef.computeMustDef(thenNode, conditionNode, expectedOutput, false);
    mustBeReachingVariableDef.computeMustDef(elseNode, conditionNode, expectedOutput, false);

    MustDef output = mustBeReachingVariableDef.flowThrough(conditionNode, input);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testFlowThroughWithLoop() {
    Node loopConditionNode = Node.newWhile(Node.newBool(true), Node.newName("loopConditionNode"));
    Node loopBodyNode = Node.newName("loopBodyNode");

    rootNode.addChild(loopConditionNode);
    rootNode.addChild(loopBodyNode);

    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(loopConditionNode, input);
    assertEquals(input, output);

    MustDef expectedOutput = new MustDef();
    mustBeReachingVariableDef.computeMustDef(loopConditionNode, loopConditionNode, expectedOutput, false);
    mustBeReachingVariableDef.computeMustDef(loopBodyNode, loopConditionNode, expectedOutput, false);

    output = mustBeReachingVariableDef.flowThrough(loopBodyNode, input);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testFlowThroughWithFunction() {
    Node functionNode = Node.newFunction(Node.newName("functionName"), new Node[]{}, new Node[]{});
    Node functionReturnNode = Node.newName("functionReturnNode");

    rootNode.addChild(functionNode);
    rootNode.addChild(functionReturnNode);

    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(functionNode, input);
    assertEquals(input, output);

    MustDef expectedOutput = new MustDef();
    mustBeReachingVariableDef.computeMustDef(functionNode, functionNode, expectedOutput, false);
    mustBeReachingVariableDef.computeMustDef(functionReturnNode, functionNode, expectedOutput, false);

    output = mustBeReachingVariableDef.flowThrough(functionReturnNode, input);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testFlowThroughWithAssignment() {
    Node assignNode = Node.newAss(Node.newName("assignNodeLeft"), Node.newName("assignNodeRight"), Node.newName("assignNodeRight"));

    rootNode.addChild(assignNode);

    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(assignNode, input);

    MustDef expectedOutput = new MustDef();
    Definition assignNodeRightDefinition = new Definition(assignNode.getFirstChild());
    expectedOutput.reachingDef.put(jsScope.getVar(assignNode.getFirstChild().getString()), assignNodeRightDefinition);
    mustBeReachingVariableDef.computeMustDef(assignNode, assignNode, expectedOutput, false);
    mustBeReachingVariableDef.computeMustDef(assignNode.getLastChild(), assignNode, expectedOutput, false);

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testFlowThroughWithGet() {
    Node getNode = Node.newGetProp(Node.newName("getNode"), new Node[]{Node.newString("getProperty")});

    rootNode.addChild(getNode);

    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(getNode, input);

    MustDef expectedOutput = new MustDef();
    Definition getNodePropertyDefinition = new Definition(getNode.getFirstChild().getFirstChild());
    expectedOutput.reachingDef.put(jsScope.getVar(getNode.getFirstChild().getFirstChild().getString()), getNodePropertyDefinition);
    mustBeReachingVariableDef.computeMustDef(getNode, getNode, expectedOutput, false);

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testFlowThroughWithVar() {
    Node varNode = Node.newVar("varNode");

    rootNode.addChild(varNode);

    MustDef input = new MustDef();
    MustDef output = mustBeReachingVariableDef.flowThrough(varNode, input);

    MustDef expectedOutput = new MustDef();
    Definition varNodeDefinition = new Definition(varNode);
    expectedOutput.reachingDef.put(jsScope.getVar("varNode"), varNodeDefinition);
    mustBeReachingVariableDef.computeMustDef(varNode, varNode, expectedOutput, false);

    assertEquals(expectedOutput, output);
  }

  @Test
  public void testGetDef() {
    Node useNode = Node.newGetProp(Node.newName("useNode"), new Node[]{Node.newString("getProperty")});
    String name = "getProperty";

    rootNode.addChild(useNode);
    Node definitionNode = Node.newAssign(Node.newName("definitionNodeLeft"), Node.newName("definitionNodeRight"), Node.newNumber(1));
    rootNode.addChild(definitionNode);

    MustDef mustDef = new MustDef();
    Node def = mustBeReachingVariableDef.getDef(name, useNode);

    assertEquals(definitionNode, def);
  }

  @Test
  public void testGetDefWithOuterScopeVariable() {
    Node useNode = Node.newGetProp(Node.newName("useNode"), new Node[]{Node.newString("getProperty")});
    String name = "outerScopeVariable";

    rootNode.addChild(useNode);
    Node definitionNode = Node.newAssign(Node.newName("definitionNodeLeft"), Node.newName("outerScopeVariable"), Node.newNumber(1));
    Node outerScopeNode = Node.newBlock(0);
    outerScopeNode.addChild(definitionNode);
    rootNode.addChild(outerScopeNode);

    MustDef mustDef = new MustDef();
    Node def = mustBeReachingVariableDef.getDef(name, useNode);

    assertNull(def);
    assertTrue(mustBeReachingVariableDef.dependsOnOuterScopeVars(name, useNode));
  }
}