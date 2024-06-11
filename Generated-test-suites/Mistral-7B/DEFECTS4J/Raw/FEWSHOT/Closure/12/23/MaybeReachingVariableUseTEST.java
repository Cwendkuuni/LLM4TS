package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.jscomp.graph.LatticeElement;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse util;
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    jsScope = new Scope();
    compiler = new Compiler();

    Node rootNode = Node.createTree(
        "function foo() { var a = 1; var b = 2; if (true) { a = 3; } alert(a); }");
    cfg = new ControlFlowGraph<>(rootNode, jsScope.getRootScope());
    util = new MaybeReachingVariableUse(cfg, jsScope, compiler);
  }

  @Test
  public void testEmptyFunction() {
    Node rootNode = Node.createTree("function foo() { }");
    ControlFlowGraph<Node> emptyCfg = new ControlFlowGraph<>(rootNode, jsScope.getRootScope());
    MaybeReachingVariableUse emptyUtil = new MaybeReachingVariableUse(emptyCfg, jsScope, compiler);

    Collection<Node> uses = emptyUtil.getUses("a", rootNode);
    assertNull(uses);
  }

  @Test
  public void testSimpleAssignment() {
    Collection<Node> uses = util.getUses("a", util.getEntryNode());
    assertNull(uses);

    Node assignNode = Node.createTree("a = 1;");
    uses = util.getUses("a", assignNode);
    assertFalse(uses.isEmpty());
    assertEquals(1, uses.size());
    assertTrue(uses.contains(assignNode));

    uses = util.getUses("b", assignNode);
    assertNull(uses);
  }

  @Test
  public void testSimpleIfStatement() {
    Node ifNode = Node.createTree("if (true) { a = 1; } alert(a);");
    uses = util.getUses("a", ifNode);
    assertFalse(uses.isEmpty());
    assertEquals(1, uses.size());
    assertTrue(uses.contains(ifNode));

    uses = util.getUses("a", util.getEntryNode());
    assertNull(uses);
  }

  @Test
  public void testSimpleForLoop() {
    Node forNode = Node.createTree("for (var i = 0; i < 10; i++) { alert(i); }");
    uses = util.getUses("i", forNode);
    assertFalse(uses.isEmpty());
    assertEquals(1, uses.size());
    assertTrue(uses.contains(forNode));

    uses = util.getUses("i", util.getEntryNode());
    assertNull(uses);
  }

  @Test
  public void testNestedIfStatement() {
    Node nestedIfNode = Node.createTree(
        "if (true) {" +
        "  if (true) {" +
        "    a = 1;" +
        "  } alert(a);" +
        "}");
    uses = util.getUses("a", nestedIfNode);
    assertFalse(uses.isEmpty());
    assertEquals(1, uses.size());
    assertTrue(uses.contains(nestedIfNode));

    uses = util.getUses("a", util.getEntryNode());
    assertNull(uses);
  }

  @Test
  public void testNestedAssignment() {
    Node nestedAssignNode = Node.createTree(
        "a = 1;" +
        "if (true) {" +
        "  a = 2;" +
        "} alert(a);");
    uses = util.getUses("a", nestedAssignNode);
    assertFalse(uses.isEmpty());
    assertEquals(1, uses.size());
    assertTrue(uses.contains(nestedAssignNode));

    uses = util.getUses("a", util.getEntryNode());
    assertNull(uses);
  }

  @Test
  public void testMultipleAssignments() {
    Node multipleAssignmentsNode = Node.createTree(
        "a = 1;" +
        "b = 2;" +
        "a = 3;" +
        "alert(a);");
    uses = util.getUses("a", multipleAssignmentsNode);
    assertFalse(uses.isEmpty());
    assertEquals(2, uses.size());
    assertTrue(uses.contains(multipleAssignmentsNode));
    assertTrue(uses.contains(Node.createTree("a = 3;")));

    uses = util.getUses("b", multipleAssignmentsNode);
    assertNull(uses);

    uses = util.getUses("a", util.getEntryNode());
    assertNull(uses);
  }

  @Test
  public void testMultipleVariablesInOneStatement() {
    Node multipleVariablesNode = Node.createTree(
        "var a = 1, b = 2; alert(a);");
    uses = util.getUses("a", multipleVariablesNode);
    assertFalse(uses.isEmpty());
    assertEquals(1, uses.size());
    assertTrue(uses.contains(multipleVariablesNode));

    uses = util.getUses("b", multipleVariablesNode);
    assertFalse(uses.isEmpty());
    assertEquals(1, uses.size());
    assertTrue(uses.contains(multipleVariablesNode));

    uses = util.getUses("a", util.getEntryNode());
    assertNull(uses);

    uses = util.getUses("b", util.getEntryNode());
    assertNull(uses);
  }

  @Test
  public void testNestedVariableDeclaration() {
    Node nestedVariablesNode = Node.createTree(
        "var a = 1;" +
        "if (true) {" +
        "  var a = 2;" +
        "  alert(a);" +
        "}");
    uses = util.getUses("a", nestedVariablesNode);
    assertFalse(uses.isEmpty());
    assertEquals(1, uses.size());
    assertTrue(uses.contains(Node.createTree("var a = 2;")));

    uses = util.getUses("a", util.getEntryNode());
    assertNull(uses);
  }

  @Test
  public void testFunctionCall() {
    Node callNode = Node.createTree("alert(a);");
    uses = util.getUses("a", callNode);
    assertFalse(uses.isEmpty());
    assertEquals(1, uses.size());
    assertTrue(uses.contains(callNode));

    uses = util.getUses("a", util.getEntryNode());
    assertNull(uses);
  }

  @Test
  public void testFunctionCallInConditional() {
    Node callInConditionalNode = Node.createTree(
        "if (true) {" +
        "  alert(a);" +
        "}");
    uses = util.getUses("a", callInConditionalNode);
    assertFalse(uses.isEmpty());
    assertEquals(1, uses.size());
    assertTrue(uses.contains(callInConditionalNode));

    uses = util.getUses("a", util.getEntryNode());
    assertNull(uses);
  }

  @Test
  public void testFunctionDefinition() {
    Node functionNode = Node.createTree(
        "function foo() {" +
        "  a = 1;" +
        "}");
    uses = util.getUses("a", functionNode);
    assertFalse(uses.isEmpty());
    assertEquals(1, uses.size());
    assertTrue(uses.contains(functionNode));

    uses = util.getUses("a", util.getEntryNode());
    assertNull(uses);
  }

  @Test
  public void testFunctionDefinitionWithReturnStatement() {
    Node functionWithReturnNode = Node.createTree(
        "function foo() {" +
        "  a = 1;" +
        "  return a;" +
        "}");
    uses = util.getUses("a", functionWithReturnNode);
    assertFalse(uses.isEmpty());
    assertEquals(1, uses.size());
    assertTrue(uses.contains(functionWithReturnNode));

    uses = util.getUses("a", util.getEntryNode());
    assertNull(uses);
  }
}