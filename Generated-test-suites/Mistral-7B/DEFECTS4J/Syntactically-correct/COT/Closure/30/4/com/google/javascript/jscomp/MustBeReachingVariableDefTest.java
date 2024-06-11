package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.MustBeReachingVariableDef.*;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDef() {
    // Test basic functionality
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(), new Node());
    MustBeReachingVariableDef mbrvd = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node node1 = new Node_Block();
    Node node2 = new Node_Assign(Token.ASSIGN, node1, new Node_Name("a"));
    Node node3 = new Node_Assign(Token.ASSIGN, new Node_Name("a"), new Node_Num(1));
    Node node4 = new Node_Return(node3);

    jsScope.declareVar("a", Node.SC_VAR, Node.ACCESS_VAR, node1);

    MustDef expectedOutput = new MustDef();
    MustDef resultOutput = mbrvd.flowThrough(node4, expectedOutput);

    assertEquals(expectedOutput, resultOutput);

    // Test edge cases and exception handling

    // Test null input
    MustDef nullInput = null;
    MustDef resultNullInput = mbrvd.flowThrough(node4, nullInput);
    assertNotNull(resultNullInput);

    // Test empty input
    MustDef emptyInput = new MustDef();
    MustDef resultEmptyInput = mbrvd.flowThrough(node4, emptyInput);
    assertNotSame(emptyInput, resultEmptyInput);

    // Test node with no definition
    Node nodeNoDef = new Node_Return(new Node_Num(42));
    MustDef resultNodeNoDef = mbrvd.flowThrough(nodeNoDef, expectedOutput);
    assertNull(resultNodeNoDef.reachingDef.get("a"));

    // Test node with multiple definitions
    Node nodeMultiDef = new Node_Block();
    Node multiDefAssign1 = new Node_Assign(Token.ASSIGN, node1, new Node_Name("a"));
    Node multiDefAssign2 = new Node_Assign(Token.ASSIGN, node1, new Node_Name("a"));
    Node multiDefReturn = new Node_Return(node1);

    jsScope.declareVar("a", Node.SC_VAR, Node.ACCESS_VAR, node1);

    MustDef resultMultiDef = mbrvd.flowThrough(multiDefReturn, expectedOutput);
    Map<Var, Definition> multiDefResultMap = resultMultiDef.reachingDef;
    assertEquals(2, multiDefResultMap.size());
    assertTrue(multiDefResultMap.containsKey(jsScope.getVar("a")));

    // Test node with loop
    Node nodeWithLoop = new Node_While(Token.WHILE, new Node_Name("i"), node1);
    Node loopAssign = new Node_Assign(Token.ASSIGN, new Node_Name("i"), new Node_Num(1));
    Node loopReturn = new Node_Return(node1);

    Scope loopScope = new Scope();
    Var loopVar = loopScope.declareVar("i", Node.SC_VAR, Node.ACCESS_VAR, nodeWithLoop);

    MustDef resultNodeWithLoop = mbrvd.flowThrough(loopReturn, expectedOutput);
    Definition resultNodeWithLoopDefinition = resultNodeWithLoop.reachingDef.get(loopVar);
    assertEquals(nodeWithLoop, resultNodeWithLoopDefinition.node);

    // Test node with function call
    Node nodeWithFunctionCall = new Node_Call(Token.IDENTIFIER, new Node_Name("foo"));
    Node functionCallReturn = new Node_Return(nodeWithFunctionCall);

    Scope functionScope = new Scope();
    functionScope.declareVar("x", Node.SC_VAR, Node.ACCESS_VAR, node1);
    functionScope.declareVar("y", Node.SC_VAR, Node.ACCESS_VAR, node1);

    MustDef resultNodeWithFunctionCall = mbrvd.flowThrough(functionCallReturn, expectedOutput);
    Map<Var, Definition> resultNodeWithFunctionCallMap = resultNodeWithFunctionCall.reachingDef;
    // Assuming 'foo' function has local variables 'x' and 'y'
    assertEquals(2, resultNodeWithFunctionCallMap.size());
    assertTrue(resultNodeWithFunctionCallMap.containsKey(functionScope.getVar("x")));
    assertTrue(resultNodeWithFunctionCallMap.containsKey(functionScope.getVar("y")));

    // Test node with assignment to a function parameter
    Node nodeWithFunctionParameterAssign = new Node_Block();
    Node funcParamAssign = new Node_Assign(Token.ASSIGN, new Node_Name("p"), new Node_Num(42));
    Node funcParamAssignAssign = new Node_Assign(Token.ASSIGN, new Node_Name("a"), funcParamAssign);
    Node funcParamAssignReturn = new Node_Return(node1);

    Scope funcParamScope = new Scope();
    funcParamScope.declareVar("p", Node.SC_VAR, Node.ACCESS_VAR, nodeWithFunctionParameterAssign);
    funcParamScope.declareVar("a", Node.SC_VAR, Node.ACCESS_VAR, node1);

    MustDef resultNodeWithFunctionParameterAssign = mbrvd.flowThrough(funcParamAssignReturn, expectedOutput);
    Definition resultNodeWithFunctionParameterAssignDefinition = resultNodeWithFunctionParameterAssign.reachingDef.get(funcParamScope.getVar("p"));
    assertEquals(funcParamAssign, resultNodeWithFunctionParameterAssignDefinition.node);
  }
}