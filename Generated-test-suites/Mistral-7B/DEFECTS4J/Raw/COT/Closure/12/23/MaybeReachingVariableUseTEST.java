package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class MaybeReachingVariableUseTest {
  @Test
  public void testMaybeReachingVariableUse() {
    // Given
    Scope jsScope = Scope.create();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<Node>(compiler);

    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  if (true) {" +
        "    x = 2;" +
        "  }" +
        "  return x;" +
        "}";

    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
        controlFlowGraph, jsScope, compiler);

    Node entryNode = controlFlowGraph.getEntryNode();
    maybeReachingVariableUse.process(entryNode, null, null);

    // When
    Multimap<Var, Node> uses = maybeReachingVariableUse.getUses("x", entryNode);

    // Then
    assertNotNull(uses);
    assertEquals(Sets.newHashSet(controlFlowGraph.getNode(2)), uses.keys());
    assertEquals(Sets.newHashSet(controlFlowGraph.getNode(0), controlFlowGraph.getNode(2)), uses.values());
  }

  @Test
  public void testEmptySourceCode() {
    // Given
    Scope jsScope = Scope.create();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<Node>(compiler);

    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
        controlFlowGraph, jsScope, compiler);

    Node entryNode = controlFlowGraph.getEntryNode();
    maybeReachingVariableUse.process(entryNode, null, null);

    // Then
    Multimap<Var, Node> uses = maybeReachingVariableUse.getUses("x", entryNode);
    assertNull(uses);
  }

  @Test
  public void testEscapedVariable() {
    // Given
    Scope jsScope = Scope.create();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<Node>(compiler);

    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  try {" +
        "    x = 2;" +
        "  } finally {" +
        "    x = 3;" +
        "  }" +
        "  return x;" +
        "}" +
        "function bar() {" +
        "  x = 1;" +
        "}";

    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
        controlFlowGraph, jsScope, compiler);

    Node entryNode = controlFlowGraph.getEntryNode();
    Node fooNode = controlFlowGraph.addNode(Node.createTree("foo"));
    Node barNode = controlFlowGraph.addNode(Node.createTree("bar"));

    Node fooCallNode = controlFlowGraph.addNode(Node.createCall(Node.createName("foo"), null));
    controlFlowGraph.addEdge(entryNode, fooCallNode, null);
    controlFlowGraph.addEdge(fooCallNode, fooNode, null);
    controlFlowGraph.addEdge(fooNode, controlFlowGraph.getNode(2), null);

    controlFlowGraph.addEdge(entryNode, barNode, null);

    maybeReachingVariableUse.process(entryNode, null, null);

    // When
    Multimap<Var, Node> uses = maybeReachingVariableUse.getUses("x", entryNode);

    // Then
    assertNotNull(uses);
    assertEquals(Sets.newHashSet(controlFlowGraph.getNode(2)), uses.keys());
    assertEquals(Sets.newHashSet(entryNode, fooNode, controlFlowGraph.getNode(2)), uses.values());

    // Given
    MaybeReachingVariableUse maybeReachingVariableUseEscaped = new MaybeReachingVariableUse(
        controlFlowGraph, jsScope, compiler);
    maybeReachingVariableUseEscaped.escaped.add(jsScope.getVar("x"));

    // When
    Multimap<Var, Node> usesEscaped = maybeReachingVariableUseEscaped.getUses("x", entryNode);

    // Then
    assertNull(usesEscaped);
  }

  @Test
  public void testConditionalStatement() {
    // Given
    Scope jsScope = Scope.create();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<Node>(compiler);

    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  if (false) {" +
        "    x = 2;" +
        "  }" +
        "  return x;" +
        "}";

    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
        controlFlowGraph, jsScope, compiler);

    Node entryNode = controlFlowGraph.getEntryNode();
    Node fooNode = controlFlowGraph.addNode(Node.createTree("foo"));

    Node ifNode = controlFlowGraph.addNode(Node.createIfStatement(Node.createBooleanLiteral(false), null));
    controlFlowGraph.addEdge(fooNode, ifNode, null);
    controlFlowGraph.addEdge(ifNode, controlFlowGraph.getNode(1), null);
    controlFlowGraph.addEdge(controlFlowGraph.getNode(1), fooNode, new DiGraphEdge(NodeUtil.getConditionExpression(ifNode), false));

    Node returnNode = controlFlowGraph.addNode(Node.createReturnStatement(Node.createIntLiteral(1), null));
    controlFlowGraph.addEdge(ifNode, returnNode, null);

    maybeReachingVariableUse.process(entryNode, null, null);

    // When
    Multimap<Var, Node> uses = maybeReachingVariableUse.getUsages();

    // Then
    assertNotNull(uses);
    assertEquals(Sets.newHashSet(controlFlowGraph.getNode(0)), uses.keys());
    assertEquals(Sets.newHashSet(controlFlowGraph.getNode(0)), uses.values());
  }

  @Test
  public void testWhileStatement() {
    // Given
    Scope jsScope = Scope.create();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<Node>(compiler);

    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  while (true) {" +
        "    x = 2;" +
        "  }" +
        "  return x;" +
        "}";

    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
        controlFlowGraph, jsScope, compiler);

    Node entryNode = controlFlowGraph.getEntryNode();
    Node fooNode = controlFlowGraph.addNode(Node.createTree("foo"));

    Node whileNode = controlFlowGraph.addNode(Node.createWhileStatement(Node.createBooleanLiteral(true), null));
    controlFlowGraph.addEdge(fooNode, whileNode, null);
    controlFlowGraph.addEdge(whileNode, controlFlowGraph.getNode(1), null);
    controlFlowGraph.addEdge(controlFlowGraph.getNode(1), whileNode, new DiGraphEdge(NodeUtil.getConditionExpression(whileNode), false));

    Node returnNode = controlFlowGraph.addNode(Node.createReturnStatement(Node.createIntLiteral(1), null));
    controlFlowGraph.addEdge(whileNode, returnNode, null);

    maybeReachingVariableUse.process(entryNode, null, null);

    // When
    Multimap<Var, Node> uses = maybeReachingVariableUse.getUsages();

    // Then
    assertNotNull(uses);
    assertEquals(Sets.newHashSet(controlFlowGraph.getNode(0)), uses.keys());
    assertEquals(Sets.newHashSet(controlFlowGraph.getNode(1), controlFlowGraph.getNode(0)), uses.values());
  }

  @Test
  public void testForStatement() {
    // Given
    Scope jsScope = Scope.create();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<Node>(compiler);

    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  for (var i = 0; i < 5; i++) {" +
        "    x = i;" +
        "  }" +
        "  return x;" +
        "}";

    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
        controlFlowGraph, jsScope, compiler);

    Node entryNode = controlFlowGraph.getEntryNode();
    Node fooNode = controlFlowGraph.addNode(Node.createTree("foo"));

    Node forNode = controlFlowGraph.addNode(Node.createForStatement(
        Node.createVarDeclaration(Node.createName("i"), null, Node.createIntLiteral(0), null, null),
        Node.createBooleanExpression(Node.createBinaryOperator(Node.createName("i"), Token.LT, Node.createIntLiteral(5), null), null),
        Node.createAssignStatement(Node.createName("i"), Node.createBinaryOperator(Node.createName("i"), Token.ADD, Node.createIntLiteral(1), null), null),
        null));
    controlFlowGraph.addEdge(fooNode, forNode, null);

    Node forInitNode = forNode.getFirstChild();
    Node forUpdateNode = forInitNode.getNext();

    Node bodyNode = forNode.getLastChild();
    controlFlowGraph.addEdge(forNode, bodyNode, null);
    controlFlowGraph.addEdge(bodyNode, forUpdateNode, null);
    controlFlowGraph.addEdge(forUpdateNode, forNode, new DiGraphEdge(forUpdateNode, true));

    Node returnNode = controlFlowGraph.addNode(Node.createReturnStatement(Node.createName("x"), null));
    controlFlowGraph.addEdge(bodyNode, returnNode, null);

    maybeReachingVariableUse.process(entryNode, null, null);

    // When
    Multimap<Var, Node> uses = maybeReachingVariableUse.getUsages();

    // Then
    assertNotNull(uses);
    assertEquals(Sets.newHashSet(controlFlowGraph.getNode(0), controlFlowGraph.getNode(2), controlFlowGraph.getNode(3), controlFlowGraph.getNode(4)), uses.keys());
    assertEquals(Sets.newHashSet(controlFlowGraph.getNode(0), controlFlowGraph.getNode(2), controlFlowGraph.getNode(3), controlFlowGraph.getNode(4), controlFlowGraph.getNode(5)), uses.values());
  }

  @Test
  public void testForInStatement() {
    // Given
    Scope jsScope = Scope.create();
    AbstractCompiler compiler = new AbstractCompiler();
    ControlFlowGraph<Node> controlFlowGraph = new ControlFlowGraph<Node>(compiler);

    String sourceCode = "function foo() {" +
        "  var x = 1;" +
        "  for (var i in [1, 2, 3]) {" +
        "    x = i;" +
        "  }" +
        "  return x;" +
        "}";

    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
        controlFlowGraph, jsScope, compiler);

    Node entryNode = controlFlowGraph.getEntryNode();
    Node fooNode = controlFlowGraph.addNode(Node.createTree("foo"));

    Node forNode = controlFlowGraph.addNode(Node.createForStatement(
        Node.createVarDeclaration(Node.createName("i"), null, null, Node.createArrayLiteral(Node.createIntLiteral(1), Node.createIntLiteral(2), Node.createIntLiteral(3), null), null),
        null, null,
        null));
    controlFlowGraph.addEdge(fooNode, forNode, null);

    Node forInitNode = forNode.getFirstChild();
    Node forUpdateNode = forInitNode.getNext();

    Node bodyNode = forNode.getLastChild();
    controlFlowGraph.addEdge(forNode, bodyNode, null);
    controlFlowGraph.addEdge(bodyNode, forUpdateNode, null);
    controlFlowGraph.addEdge(forUpdateNode, forNode, new DiGraphEdge(forUpdateNode, true));

    Node returnNode = controlFlowGraph.addNode(Node.createReturnStatement(Node.createName("x"), null));
    controlFlowGraph.addEdge(bodyNode, returnNode, null);

    maybeReachingVariableUse.process(entryNode, null, null);

    // When
    Multimap<Var, Node> uses = maybeReachingVariableUse.getUsages();

    // Then
    assertNotNull(uses);
    assertEquals(Sets.newHashSet(controlFlowGraph.getNode(0), controlFlowGraph.getNode(2), controlFlowGraph.getNode(3), controlFlowGraph.getNode(4), controlFlowGraph.getNode(5)), uses.keys());
    assertEquals(Sets.newHashSet(controlFlowGraph.getNode(0), controlFlowGraph.getNode(2), controlFlowGraph.getNode(3), controlFlowGraph.getNode(4), controlFlowGraph.getNode(5), controlFlowGraph.getNode(6), controlFlowGraph.getNode(7), controlFlowGraph.getNode(8), controlFlowGraph.getNode(9)), uses.values());
  }
}