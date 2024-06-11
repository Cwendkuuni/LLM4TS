package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Set;

public class MustBeReachingVariableDefTest {

  @Test
  public void testGetDef() {
    // Create a simple test case where we have the following code:
    // function foo(x) {
    //   var y = x;
    //   return y;
    // }

    Scope scope = new Scope();
    scope.addVar("x", new Var(Token.VAR, null, null, null, null, null, null, null, null, null, null, null));
    Scope jsScope = new Scope(scope, null);

    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(jsScope.getRootNode(), jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node entryNode = jsScope.getRootNode();
    Node assignmentNode = NodeUtil.getFirstChildAssuming(entryNode, Token.ASSIGN);
    Node returnNode = NodeUtil.getFirstChildAssuming(assignmentNode, Token.RETURN);

    // We are testing the method getDef with the following arguments:
    // name = "y"
    // useNode = returnNode

    String name = "y";
    Node useNode = returnNode;

    Node expectedDefNode = NodeUtil.getFirstChildAssuming(assignmentNode, Token.VAR);

    Node actualDefNode = analysis.getDef(name, useNode);

    assertEquals(expectedDefNode, actualDefNode);
  }

  @Test
  public void testGetDef_Undefined() {
    // Create a simple test case where we have the following code:
    // function foo() {
    //   return x; // x is not defined in this function
    // }

    Scope scope = new Scope();
    Scope jsScope = new Scope(scope, null);

    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(jsScope.getRootNode(), jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node entryNode = jsScope.getRootNode();
    Node returnNode = NodeUtil.getFirstChildAssuming(entryNode, Token.RETURN);

    String name = "x";
    Node useNode = returnNode;

    assertNull(analysis.getDef(name, useNode));
  }

  @Test
  public void testGetDef_Escaped() {
    // Create a simple test case where we have the following code:
    // function foo() {
    //   var x = 0;
    //   function bar() {
    //     return x; // x is an inner function name, not a local variable
    //   }
    //   return bar;
    // }

    Scope scope = new Scope();
    Scope jsScope = new Scope(scope, null);

    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(jsScope.getRootNode(), jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node entryNode = jsScope.getRootNode();

    Node functionNode = NodeUtil.getFirstChildAssuming(entryNode, Token.FUNCTION);
    Node functionNameNode = NodeUtil.getFirstChildAssuming(functionNode, Token.IDENT);
    Node returnNode = NodeUtil.getFirstChildAssuming(NodeUtil.getLastChild(functionNode), Token.RETURN);

    String name = functionNameNode.getString();
    Node useNode = returnNode;

    assertNull(analysis.getDef(name, useNode));
  }

  @Test
  public void testGetDef_Catch() {
    // Create a simple test case where we have the following code:
    // function foo() {
    //   try {
    //     throw new Error();
    //   } catch (e) {
    //     return e.message;
    //   }
    // }

    Scope scope = new Scope();
    Scope jsScope = new Scope(scope, null);

    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(jsScope.getRootNode(), jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node entryNode = jsScope.getRootNode();
    Node tryNode = NodeUtil.getFirstChildAssuming(entryNode, Token.TRY);
    Node catchNode = NodeUtil.getFirstChildAssuming(tryNode, Token.CATCH);
    Node returnNode = NodeUtil.getFirstChildAssuming(NodeUtil.getLastChild(catchNode), Token.RETURN);

    String name = "e";
    Node useNode = returnNode;

    assertNull(analysis.getDef(name, useNode));
  }

  @Test
  public void testGetDef_MultipleDefinitions() {
    // Create a simple test case where we have the following code:
    // function foo() {
    //   var x = 0;
    //   var x = 1;
    //   return x;
    // }

    Scope scope = new Scope();
    Scope jsScope = new Scope(scope, null);

    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(jsScope.getRootNode(), jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node entryNode = jsScope.getRootNode();
    Node firstAssignmentNode = NodeUtil.getFirstChildAssuming(entryNode, Token.ASSIGN);
    Node secondAssignmentNode = NodeUtil.getFirstChildAssuming(NodeUtil.getNextSibling(firstAssignmentNode), Token.ASSIGN);
    Node returnNode = NodeUtil.getFirstChildAssuming(NodeUtil.getLastChild(entryNode), Token.RETURN);

    String name = "x";
    Node useNode = returnNode;

    Node expectedDefNode = secondAssignmentNode;

    Node actualDefNode = analysis.getDef(name, useNode);

    assertEquals(expectedDefNode, actualDefNode);
  }

  @Test
  public void testDependsOnOuterScopeVars() {
    // Create a simple test case where we have the following code:
    // function foo() {
    //   var x = 1;
    //   function bar(y) {
    //     return x;
    //   }
    //   return bar;
    // }

    Scope scope = new Scope();
    Scope jsScope = new Scope(scope, null);

    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph cfg = new ControlFlowGraph(jsScope.getRootNode(), jsScope, compiler);

    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(cfg, jsScope, compiler);

    Node entryNode = jsScope.getRootNode();

    Node functionNode = NodeUtil.getFirstChildAssuming(entryNode, Token.FUNCTION);
    Node functionNameNode = NodeUtil.getFirstChildAssuming(functionNode, Token.IDENT);
    Node returnNode = NodeUtil.getFirstChildAssuming(NodeUtil.getLastChild(functionNode), Token.RETURN);

    Node varXNode = NodeUtil.getFirstChildAssuming(entryNode, Token.VAR);

    String name = functionNameNode.getString();
    Node useNode = returnNode;

    assertFalse(analysis.dependsOnOuterScopeVars(name, useNode));

    Node outerScopeVarNode = NodeUtil.getFirstChildAssuming(NodeUtil.getAncestor(varXNode, Token.VAR), Token.VAR);

    assertTrue(analysis.dependsOnOuterScopeVars(name, useNode));
  }
}