package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MustBeReachingVariableDefTest {

  private Scope jsScope;
  private MustBeReachingVariableDef mustBeReachingVariableDef;

  @Before
  public void setUp() {
    // Initialize test data
    // Replace this with actual setup code for your test
  }

  @Test
  public void testSimpleMethod() {
    // Test a simple method
    Node simpleMethodNode = Node.newNode(Token.FUNCTION, "testSimpleMethod", null, null, null);
    mustBeReachingVariableDef = new MustBeReachingVariableDef(null, jsScope, null);

    Node useNode = Node.newName("a");
    simpleMethodNode.addChild(useNode);

    Node definitionNode = Node.newName("a");
    simpleMethodNode.addChild(definitionNode);

    mustBeReachingVariableDef.flowThrough(definitionNode, mustBeReachingVariableDef.createInitialEstimateLattice());
    Node expectedResult = definitionNode;
    Node actualResult = mustBeReachingVariableDef.getDef("a", useNode);

    assertEquals("Method 'testSimpleMethod' should return 'a' as the definition for variable 'a'", expectedResult, actualResult);
  }

  @Test
  public void testMethodWithAssignment() {
    // Test a method with assignment
    Node methodWithAssignmentNode = Node.newNode(Token.FUNCTION, "testMethodWithAssignment", null, null, null);
    mustBeReachingVariableDef = new MustBeReachingVariableDef(null, jsScope, null);

    Node aNode = Node.newName("a");
    methodWithAssignmentNode.addChild(aNode);

    Node assignmentNode = Node.newAssignment(Token.ASSIGN, aNode, Node.newNumber(-1));
    methodWithAssignmentNode.addChild(assignmentNode);

    Node useNode = Node.newName("a");
    methodWithAssignmentNode.addChild(useNode);

    mustBeReachingVariableDef.flowThrough(definitionNode, mustBeReachingVariableDef.createInitialEstimateLattice());
    Node expectedResult = assignmentNode;
    Node actualResult = mustBeReachingVariableDef.getDef("a", useNode);

    assertEquals("Method 'testMethodWithAssignment' should return assignment node as the definition for variable 'a'", expectedResult, actualResult);
  }

  @Test
  public void testMethodWithConditional() {
    // Test a method with a conditional statement
    Node methodWithConditionalNode = Node.newNode(Token.FUNCTION, "testMethodWithConditional", null, null, null);
    mustBeReachingVariableDef = new MustBeReachingVariableDef(null, jsScope, null);

    Node varA = Node.newName("a");
    methodWithConditionalNode.addChild(varA);

    Node conditionNode = Node.newCondition(Token.IF, Node.newBool(true));
    methodWithConditionalNode.addChild(conditionNode);

    Node definitionNode1 = Node.newName("a");
    methodWithConditionalNode.addChild(definitionNode1);

    Node assignmentNode1 = Node.newAssignment(Token.ASSIGN, varA, Node.newNumber(1));
    methodWithConditionalNode.addChild(assignmentNode1);

    Node conditionNode2 = Node.newCondition(Token.ELSE);
    methodWithConditionalNode.addChild(conditionNode2);

    Node definitionNode2 = Node.newName("a");
    methodWithConditionalNode.addChild(definitionNode2);

    Node useNode = Node.newName("a");
    methodWithConditionalNode.addChild(useNode);

    mustBeReachingVariableDef.flowThrough(definitionNode1, mustBeReachingVariableDef.createInitialEstimateLattice());
    if (mustBeReachingVariableDef.getDef("a", useNode) != definitionNode1) {
      fail("Method 'testMethodWithConditional' should return definitionNode1 as the definition for variable 'a' when the condition is true");
    }

    mustBeReachingVariableDef.flowThrough(definitionNode2, mustBeReachingVariableDef.getDef("a", useNode));
    Node expectedResult = definitionNode2;
    Node actualResult = mustBeReachingVariableDef.getDef("a", useNode);

    assertEquals("Method 'testMethodWithConditional' should return definitionNode2 as the definition for variable 'a' when the condition is false", expectedResult, actualResult);
  }

  @Test
  public void testMethodWithCycle() {
    // Test a method with a cycle
    Node methodWithCycleNode = Node.newNode(Token.FUNCTION, "testMethodWithCycle", null, null, null);
    mustBeReachingVariableDef = new MustBeReachingVariableDef(null, jsScope, null);

    Node varA = Node.newName("a");
    methodWithCycleNode.addChild(varA);

    Node assignmentNode1 = Node.newAssignment(Token.ASSIGN, varA, varA);
    methodWithCycleNode.addChild(assignmentNode1);

    Node useNode = Node.newName("a");
    methodWithCycleNode.addChild(useNode);

    mustBeReachingVariableDef.flowThrough(assignmentNode1, mustBeReachingVariableDef.createInitialEstimateLattice());
    Node expectedResult = null; // Since it's a cycle, there is no definite definition
    Node actualResult = mustBeReachingVariableDef.getDef("a", useNode);

    assertEquals("Method 'testMethodWithCycle' should return null as the definition for variable 'a' since there is a cycle", expectedResult, actualResult);
  }

  @Test
  public void testMethodWithFunctionCall() {
    // Test a method that calls another method
    Node methodWithFunctionCallNode = Node.newNode(Token.FUNCTION, "testMethodWithFunctionCall", null, null, null);
    mustBeReachingVariableDef = new MustBeReachingVariableDef(null, jsScope, null);

    Node varA = Node.newName("a");
    methodWithFunctionCallNode.addChild(varA);

    Node callNode = Node.newCall(Token.CALL, null, Node.newName("testMethodCallingA"));
    methodWithFunctionCallNode.addChild(callNode);

    Node useNode = Node.newName("a");
    methodWithFunctionCallNode.addChild(useNode);

    // Replace 'testMethodCallingA' with a test method that assigns a value to 'a'
    MustBeReachingVariableDef testMethodCallingA = new MustBeReachingVariableDef(null, jsScope, null);
    Node definitionNode = Node.newName("a");
    testMethodCallingA.flowThrough(definitionNode, testMethodCallingA.createInitialEstimateLattice());

    Node expectedResult = definitionNode;
    Node actualResult = mustBeReachingVariableDef.getDef("a", useNode);

    assertEquals("Method 'testMethodWithFunctionCall' should return the definition of 'a' from the called method", expectedResult, actualResult);
  }

  @Test
  public void testMethodWithEscapeVariable() {
    // Test a method that uses an escaped variable
    Node methodWithEscapeVariableNode = Node.newNode(Token.FUNCTION, "testMethodWithEscapeVariable", null, null, null);
    mustBeReachingVariableDef = new MustBeReachingVariableDef(null, jsScope, null);

    Node varA = Node.newName("a");
    methodWithEscapeVariableNode.addChild(varA);

    Node useEscapedVariableNode = Node.newName("escaped");
    methodWithEscapeVariableNode.addChild(useEscapedVariableNode);

    mustBeReachingVariableDef.flowThrough(useEscapedVariableNode, mustBeReachingVariableDef.createInitialEstimateLattice());
    Node expectedResult = null; // Since 'escaped' is an escaped variable, it has no definite definition
    Node actualResult = mustBeReachingVariableDef.getDef("a", useNode);

    assertEquals("Method 'testMethodWithEscapeVariable' should return null as the definition for variable 'a' since 'escaped' is an escaped variable", expectedResult, actualResult);
  }
}