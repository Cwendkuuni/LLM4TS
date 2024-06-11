package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testBasicFunctions() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new Node("f")) {
          @Override
          public Node getEntryNode() {
            return this.getNode(0);
          }
        }, new Scope(new Node("f")), new AbstractCompiler());

    Node def = analysis.getDef("x", new Node("f"));
    assertNull(def);
  }

  @Test
  public void testAssignments() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new Node("f")) {
          @Override
          public Node getEntryNode() {
            return this.getNode(0);
          }
        }, new Scope(new Node("f")), new AbstractCompiler());

    analysis.getCFG().addNode(new Node("x = 1"));
    analysis.getCFG().addEdge(analysis.getCFG().getNode(0), analysis.getCFG().getNode(1));

    Node def = analysis.getDef("x", new Node("x = 1"));
    assertEquals(analysis.getCFG().getNode(1), def);
  }

  @Test
  public void testConditionalAssignments() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new Node("f")) {
          @Override
          public Node getEntryNode() {
            return this.getNode(0);
          }
        }, new Scope(new Node("f")), new AbstractCompiler());

    Node conditionalNode = new Node("if (x > 0)", Token.IF, null);
    Node assignNode = new Node("x = 1", Token.ASSIGN, conditionalNode);
    analysis.getCFG().addNode(conditionalNode);
    analysis.getCFG().addNode(assignNode);
    analysis.getCFG().addEdge(analysis.getCFG().getNode(0), conditionalNode);
    analysis.getCFG().addEdge(conditionalNode, assignNode);

    Node def = analysis.getDef("x", new Node("if (x > 0)", Token.IF, null));
    assertNull(def);

    def = analysis.getDef("x", new Node("x = 1", Token.ASSIGN, null));
    assertEquals(analysis.getCFG().getNode(2), def);
  }

  @Test
  public void testFunctionCalls() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new Node("f")) {
          @Override
          public Node getEntryNode() {
            return this.getNode(0);
          }
        }, new Scope(new Node("f")), new AbstractCompiler());

    Node funcNode = new Node("g()", Token.CALL, null);
    analysis.getCFG().addNode(funcNode);
    analysis.getCFG().addEdge(analysis.getCFG().getNode(0), funcNode);

    Node def = analysis.getDef("x", new Node("g()", Token.CALL, null));
    assertNull(def);
  }

  @Test
  public void testFunctionCallsWithArguments() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new Node("f")) {
          @Override
          public Node getEntryNode() {
            return this.getNode(0);
          }
        }, new Scope(new Node("f")), new AbstractCompiler());

    Node funcNode = new Node("g(x)", Token.CALL, null);
    analysis.getCFG().addNode(funcNode);
    analysis.getCFG().addEdge(analysis.getCFG().getNode(0), funcNode);

    Node argNode = new Node("x = 1");
    analysis.getCFG().addNode(argNode);
    analysis.getCFG().addEdge(funcNode, argNode);

    Node def = analysis.getDef("x", new Node("g(x)", Token.CALL, null));
    assertEquals(analysis.getCFG().getNode(2), def);
  }

  @Test
  public void testComputedGoTo() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new Node("f")) {
          @Override
          public Node getEntryNode() {
            return this.getNode(0);
          }
        }, new Scope(new Node("f")), new AbstractCompiler());

    Node ifNode = new Node("if (x > 0)", Token.IF, null);
    Node elseNode = new Node("else", Token.ELSE, ifNode);
    Node assignNode = new Node("x = 1", Token.ASSIGN, elseNode);

    analysis.getCFG().addNode(ifNode);
    analysis.getCFG().addNode(elseNode);
    analysis.getCFG().addNode(assignNode);

    analysis.getCFG().addEdge(analysis.getCFG().getNode(0), ifNode);
    analysis.getCFG().addEdge(ifNode, assignNode);

    Node def = analysis.getDef("x", new Node("if (x > 0)", Token.IF, null));
    assertNull(def);
  }

  @Test
  public void testComputedGoToWithEscape() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new Node("f")) {
          @Override
          public Node getEntryNode() {
            return this.getNode(0);
          }
        }, new Scope(new Node("f")), new AbstractCompiler());

    Node argNode = new Node("arguments", Token.GET_PROP, null);

    Node ifNode = new Node("if (arguments[0] > 0)", Token.IF, null);
    Node elseNode = new Node("else", Token.ELSE, ifNode);
    Node assignNode = new Node("arguments[0] = 1", Token.ASSIGN, elseNode);

    analysis.getCFG().addNode(ifNode);
    analysis.getCFG().addNode(elseNode);
    analysis.getCFG().addNode(assignNode);

    analysis.getCFG().addEdge(analysis.getCFG().getNode(0), ifNode);
    analysis.getCFG().addEdge(ifNode, assignNode);

    analysis.escaped.add(analysis.jsScope.getVar("arguments"));

    Node def = analysis.getDef("arguments[0]", new Node("if (arguments[0] > 0)", Token.IF, null));
    assertNull(def);
  }

  @Test
  public void testFunctionCallWithEscape() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new Node("f")) {
          @Override
          public Node getEntryNode() {
            return this.getNode(0);
          }
        }, new Scope(new Node("f")), new AbstractCompiler());

    Node funcNode = new Node("g()", Token.CALL, null);
    analysis.getCFG().addNode(funcNode);
    analysis.getCFG().addEdge(analysis.getCFG().getNode(0), funcNode);

    analysis.escaped.add(analysis.jsScope.getVar("arguments"));

    Node def = analysis.getDef("arguments", new Node("g()", Token.CALL, null));
    assertNull(def);
  }

  @Test
  public void testFunctionCallWithEscapeAndArguments() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new Node("f")) {
          @Override
          public Node getEntryNode() {
            return this.getNode(0);
          }
        }, new Scope(new Node("f")), new AbstractCompiler());

    Node funcNode = new Node("g(x)", Token.CALL, null);
    analysis.getCFG().addNode(funcNode);
    analysis.getCFG().addEdge(analysis.getCFG().getNode(0), funcNode);

    Node argNode = new Node("x = 1");
    analysis.getCFG().addNode(argNode);
    analysis.getCFG().addEdge(funcNode, argNode);

    analysis.escaped.add(analysis.jsScope.getVar("arguments"));
    analysis.escaped.add(analysis.jsScope.getVar("x"));

    Node def = analysis.getDef("x", new Node("g(x)", Token.CALL, null));
    assertNull(def);
  }

  @Test
  public void testFunctionCallWithEscapeAndArgumentsInsideFunction() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new Node("f")) {
          @Override
          public Node getEntryNode() {
            return this.getNode(0);
          }
        }, new Scope(new Node("f")), new AbstractCompiler());

    Node funcNode = new Node("g(x)", Token.CALL, null);
    Node ifNode = new Node("if (x > 0)", Token.IF, null);
    Node assignNode = new Node("x = 1", Token.ASSIGN, ifNode);

    analysis.getCFG().addNode(funcNode);
    analysis.getCFG().addNode(ifNode);
    analysis.getCFG().addNode(assignNode);

    analysis.getCFG().addEdge(analysis.getCFG().getNode(0), funcNode);
    analysis.getCFG().addEdge(funcNode, ifNode);
    analysis.getCFG().addEdge(ifNode, assignNode);

    analysis.escaped.add(analysis.jsScope.getVar("arguments"));
    analysis.escaped.add(analysis.jsScope.getVar("x"));

    Node def = analysis.getDef("x", new Node("g(x)", Token.CALL, null));
    assertNull(def);
  }

  @Test
  public void testFunctionCallWithEscapeAndArgumentsAndDependency() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new Node("f")) {
          @Override
          public Node getEntryNode() {
            return this.getNode(0);
          }
        }, new Scope(new Node("f")), new AbstractCompiler());

    Node funcNode = new Node("g(x)", Token.CALL, null);
    Node ifNode = new Node("if (x > 0)", Token.IF, null);
    Node assignNode = new Node("x = 1", Token.ASSIGN, ifNode);
    Node dependencyNode = new Node("y = 1");

    analysis.getCFG().addNode(funcNode);
    analysis.getCFG().addNode(ifNode);
    analysis.getCFG().addNode(assignNode);
    analysis.getCFG().addNode(dependencyNode);

    analysis.getCFG().addEdge(analysis.getCFG().getNode(0), funcNode);
    analysis.getCFG().addEdge(funcNode, ifNode);
    analysis.getCFG().addEdge(ifNode, assignNode);
    analysis.getCFG().addEdge(funcNode, dependencyNode);

    analysis.escaped.add(analysis.jsScope.getVar("arguments"));
    analysis.escaped.add(analysis.jsScope.getVar("x"));
    analysis.escaped.add(analysis.jsScope.getVar("y"));

    Node def = analysis.getDef("x", new Node("g(x)", Token.CALL, null));
    assertNull(def);
  }

  @Test
  public void testFunctionCallWithEscapeAndArgumentsAndDependentOnOuterScope() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new Node("f")) {
          @Override
          public Node getEntryNode() {
            return this.getNode(0);
          }
        }, new Scope(new Node("f")), new AbstractCompiler());

    Node funcNode = new Node("g(x)", Token.CALL, null);
    Node varNode = new Node("x = 1");
    Node outerScopeNode = new Node("z = 1");
    Node ifNode = new Node("if (x > 0)", Token.IF, null);

    analysis.getCFG().addNode(funcNode);
    analysis.getCFG().addNode(ifNode);
    analysis.getCFG().addNode(varNode);
    analysis.getCFG().addNode(outerScopeNode);

    analysis.getCFG().addEdge(analysis.getCFG().getNode(0), funcNode);
    analysis.getCFG().addEdge(funcNode, ifNode);
    analysis.getCFG().addEdge(varNode, funcNode);
    analysis.getCFG().addEdge(outerScopeNode, ifNode);
    analysis.getCFG().addEdge(ifNode, varNode);

    analysis.escaped.add(analysis.jsScope.getVar("arguments"));
    analysis.escaped.add(analysis.jsScope.getVar("x"));
    analysis.escaped.add(analysis.jsScope.getVar("z"));

    boolean dependsOnOuterScope = analysis.dependsOnOuterScopeVars("x", funcNode);
    assertTrue(dependsOnOuterScope);

    Node def = analysis.getDef("x", new Node("g(x)", Token.CALL, null));
    assertNull(def);
  }

  @Test
  public void testFunctionCallWithEscapeAndArgumentsAndDependentOnOuterScopeButNotModified() {
    MustBeReachingVariableDef analysis = new MustBeReachingVariableDef(
        new ControlFlowGraph<>(new Node("f")) {
          @Override
          public Node getEntryNode() {
            return this.getNode(0);
          }
        }, new Scope(new Node("f")), new AbstractCompiler());

    Node funcNode = new Node("g(x)", Token.CALL, null);
    Node varNode = new Node("x = 1");
    Node outerScopeNode = new Node("z = 1");
    Node ifNode = new Node("if (x > 0)", Token.IF, null);

    analysis.getCFG().addNode(funcNode);
    analysis.getCFG().addNode(ifNode);
    analysis.getCFG().addNode(varNode);
    analysis.getCFG().addNode(outerScopeNode);

    analysis.getCFG().addEdge(analysis.getCFG().getNode(0), funcNode);
    analysis.getCFG().addEdge(funcNode, ifNode);
    analysis.getCFG().addEdge(outerScopeNode, ifNode);
    analysis.getCFG().addEdge(ifNode, varNode);

    analysis.escaped.add(analysis.jsScope.getVar("arguments"));
    analysis.escaped.add(analysis.jsScope.getVar("x"));
    analysis.escaped.add(analysis.jsScope.getVar("z"));

    boolean dependsOnOuterScope = analysis.dependsOnOuterScopeVars("x", funcNode);
    assertTrue(dependsOnOuterScope);

    Node def = analysis.getDef("x", new Node("g(x)", Token.CALL, null));
    assertNull(def);
  }
}