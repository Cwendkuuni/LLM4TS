package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

@RunWith(MockitoJUnitRunner.class)
public class MustBeReachingVariableDefTest {

  private AbstractCompiler compiler;
  private Scope jsScope;
  private ControlFlowGraph<Node> cfg;
  private MustBeReachingVariableDef analysis;

  @Before
  public void setup() {
    compiler = Mockito.mock(AbstractCompiler.class);
    jsScope = Mockito.mock(Scope.class);
    cfg = Mockito.mock(ControlFlowGraph.class);
    analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);
  }

  @Test
  public void testBasicFunctionality() {
    // Test a simple function
    Node functionNode = Node.newFunction("testFunction", new Token(Token.FUNC, 0),
        new Node[0], new Node[0], null);
    analysis.flowThrough(functionNode, analysis.createInitialEstimateLattice());
    Node result = analysis.getDef("result", functionNode);
    assertEquals(functionNode, result);
  }

  @Test
  public void testFunctionWithReturn() {
    // Test a function with a return statement
    Node functionNode = Node.newFunction("testFunction", new Token(Token.FUNC, 0),
        new Node[0], new Node[0], null);
    Node returnNode = Node.newReturn(new Node[0]);
    analysis.flowThrough(returnNode, analysis.createInitialEstimateLattice());
    analysis.flowThrough(functionNode, analysis.createInitialEstimateLattice());
    Node result = analysis.getDef("result", functionNode);
    assertEquals(returnNode, result);
  }

  @Test
  public void testFunctionWithAssignment() {
    // Test a function with an assignment statement
    Node functionNode = Node.newFunction("testFunction", new Token(Token.FUNC, 0),
        new Node[0], new Node[0], null);
    Node assignNode = Node.newAssign(Node.newName("result"),
        Node.newNumber(1));
    analysis.flowThrough(assignNode, analysis.createInitialEstimateLattice());
    analysis.flowThrough(functionNode, analysis.createInitialEstimateLattice());
    Node result = analysis.getDef("result", functionNode);
    assertEquals(assignNode, result);
  }

  @Test
  public void testFunctionWithConditional() {
    // Test a function with a conditional statement
    Node functionNode = Node.newFunction("testFunction", new Token(Token.FUNC, 0),
        new Node[0], new Node[0], null);
    Node conditionNode = Node.newIf(Node.newBool(true), Node.newName("x"),
        Node.newName("y"), Node.newName("z"));
    analysis.flowThrough(conditionNode, analysis.createInitialEstimateLattice());
    analysis.flowThrough(functionNode, analysis.createInitialEstimateLattice());
    Node result = analysis.getDef("x", functionNode);
    assertEquals(conditionNode, result);
  }

  @Test
  public void testFunctionWithLoop() {
    // Test a function with a loop statement
    Node functionNode = Node.newFunction("testFunction", new Token(Token.FUNC, 0),
        new Node[0], new Node[0], null);
    Node loopNode = Node.newWhile(Node.newName("x"), Node.newName("x"),
        Node.newName("y"));
    analysis.flowThrough(loopNode, analysis.createInitialEstimateLattice());
    analysis.flowThrough(functionNode, analysis.createInitialEstimateLattice());
    Node result = analysis.getDef("x", functionNode);
    assertEquals(loopNode, result);
  }

  @Test
  public void testFunctionWithEscapeVariable() {
    // Test a function with an escaped variable
    Node functionNode = Node.newFunction("testFunction", new Token(Token.FUNC, 0),
        new Node[0], new Node[0], null);
    Node escapeVariableNode = Node.newName("__outer__");
    analysis.flowThrough(functionNode, analysis.createInitialEstimateLattice());
    Node result = analysis.getDef(escapeVariableNode.getString(), functionNode);
    assertNull(result);
  }

  @Test
  public void testFunctionWithDependencies() {
    // Test a function with dependencies between variables
    Node functionNode = Node.newFunction("testFunction", new Token(Token.FUNC, 0),
        new Node[0], new Node[0], null);
    Node assignANode = Node.newAssign(Node.newName("a"), Node.newName("b"));
    Node assignBNode = Node.newAssign(Node.newName("b"), Node.newNumber(1));
    analysis.flowThrough(assignBNode, analysis.createInitialEstimateLattice());
    analysis.flowThrough(assignANode, analysis.createInitialEstimateLattice());
    Node resultA = analysis.getDef("a", functionNode);
    assertEquals(assignANode, resultA);
    Node resultB = analysis.getDef("b", functionNode);
    assertEquals(assignBNode, resultB);
  }

  @Test
  public void testFunctionWithOuterScopeDependency() {
    // Test a function with a dependency on an outer scope variable
    Node functionNode = Node.newFunction("outerFunction", new Token(Token.FUNC, 0),
        new Node[0], new Node[0], null);
    Node innerFunctionNode = Node.newFunction("innerFunction",
        new Token(Token.FUNC, 0), new Node[0], new Node[0], functionNode);
    Node variableNode = Node.newName("outerVariable");
    Node assignmentNode = Node.newAssign(variableNode, Node.newNumber(1));
    analysis.flowThrough(assignmentNode, analysis.createInitialEstimateLattice());
    analysis.flowThrough(innerFunctionNode, analysis.createInitialEstimateLattice());
    Node result = analysis.getDef(variableNode.getString(), innerFunctionNode);
    assertEquals(assignmentNode, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullNode() {
    analysis.getDef(null, Node.newName("x"));
  }
}