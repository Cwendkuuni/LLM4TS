package org.apache.commons.cli.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.NodeUtil;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testTryRemoveAssignment() {
    Node root = createTestNode("test", null);
    AbstractCompiler compiler = new TestCompiler();
    Scope scope = new TestScope(root);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(root);
    LiveVariablesAnalysis liveness = new LiveVariablesAnalysis(cfg, scope, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node aNode = createTestNode("a", null);
    Node bNode = createTestNode("b", null);
    Node cNode = createTestNode("c", null);

    Node assignmentNode1 = createAssignmentNode("a", aNode, createNumberNode(1));
    Node assignmentNode2 = createAssignmentNode("a", aNode, bNode);
    Node assignmentNode3 = createAssignmentNode("c", cNode, createNumberNode(2));

    root.addChild(assignmentNode1);
    root.addChild(assignmentNode2);
    root.addChild(assignmentNode3);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.setVisitor(deadAssignmentsElimination);
    nodeTraversal.traverse();

    assertEquals(0, getChildCount(assignmentNode2));
    assertNull(assignmentNode2.getParent());

    assertEquals(Token.VOID, assignmentNode3.getType());
  }

  @Test
  public void testTryRemoveAssignmentWithCondition() {
    Node root = createTestNode("test", null);
    AbstractCompiler compiler = new TestCompiler();
    Scope scope = new TestScope(root);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(root);
    LiveVariablesAnalysis liveness = new LiveVariablesAnalysis(cfg, scope, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node aNode = createTestNode("a", null);
    Node bNode = createTestNode("b", null);
    Node cNode = createTestNode("c", null);

    Node conditionNode = createBooleanNode(true);
    Node conditionalNode = createConditionalNode(conditionNode, null, bNode, createNumberNode(1), root);
    Node assignmentNode1 = createAssignmentNode("a", aNode, createNumberNode(1));
    Node assignmentNode2 = createAssignmentNode("c", cNode, createNumberNode(2));

    root.addChild(assignmentNode1);
    root.addChild(conditionalNode);
    root.addChild(assignmentNode2);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.setVisitor(deadAssignmentsElimination);
    nodeTraversal.traverse();

    assertEquals(0, getChildCount(assignmentNode2));
    assertNull(assignmentNode2.getParent());

    assertEquals(Token.VOID, assignmentNode3.getType());
  }

  @Test
  public void testTryRemoveAssignmentWithReturn() {
    Node root = createTestNode("test", null);
    AbstractCompiler compiler = new TestCompiler();
    Scope scope = new TestScope(root);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(root);
    LiveVariablesAnalysis liveness = new LiveVariablesAnalysis(cfg, scope, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node aNode = createTestNode("a", null);
    Node bNode = createTestNode("b", null);
    Node cNode = createTestNode("c", null);

    Node returnNode = createReturnNode(createNumberNode(1));
    Node assignmentNode1 = createAssignmentNode("a", aNode, createNumberNode(1));
    Node assignmentNode2 = createAssignmentNode("c", cNode, createNumberNode(2));

    Node functionNode = createFunctionNode("testFunction", null, null, root);
    Node functionBodyNode = createBlockNode();
    functionBodyNode.addChild(assignmentNode1);
    functionBodyNode.addChild(returnNode);
    functionNode.setBody(functionBodyNode);
    root.addChild(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.setVisitor(deadAssignmentsElimination);
    nodeTraversal.traverse();

    assertEquals(0, getChildCount(assignmentNode2));
    assertNull(assignmentNode2.getParent());

    assertEquals(Token.VOID, assignmentNode3.getType());
  }

  @Test
  public void testTryRemoveAssignmentWithFor() {
    Node root = createTestNode("test", null);
    AbstractCompiler compiler = new TestCompiler();
    Scope scope = new TestScope(root);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(root);
    LiveVariablesAnalysis liveness = new LiveVariablesAnalysis(cfg, scope, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node aNode = createTestNode("a", null);
    Node bNode = createTestNode("b", null);
    Node cNode = createTestNode("c", null);

    Node forNode = createForNode(createNumberNode(1), createNumberNode(5), null);
    Node assignmentNode1 = createAssignmentNode("a", aNode, createNumberNode(1));
    Node assignmentNode2 = createAssignmentNode("c", cNode, createNumberNode(2));

    Node bodyNode = createBlockNode();
    bodyNode.addChild(assignmentNode1);
    bodyNode.addChild(assignmentNode2);
    forNode.setBody(bodyNode);
    root.addChild(forNode);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.setVisitor(deadAssignmentsElimination);
    nodeTraversal.traverse();

    assertEquals(0, getChildCount(assignmentNode2));
    assertNull(assignmentNode2.getParent());

    assertEquals(Token.VOID, assignmentNode3.getType());
  }

  @Test
  public void testTryRemoveAssignmentWithIf() {
    Node root = createTestNode("test", null);
    AbstractCompiler compiler = new TestCompiler();
    Scope scope = new TestScope(root);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(root);
    LiveVariablesAnalysis liveness = new LiveVariablesAnalysis(cfg, scope, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node aNode = createTestNode("a", null);
    Node bNode = createTestNode("b", null);
    Node cNode = createTestNode("c", null);

    Node conditionNode = createBooleanNode(true);
    Node ifNode = createIfNode(conditionNode, null, cNode, createNumberNode(2), root);
    Node assignmentNode1 = createAssignmentNode("a", aNode, createNumberNode(1));

    Node bodyNode = createBlockNode();
    bodyNode.addChild(assignmentNode1);
    ifNode.setBody(bodyNode);
    root.addChild(ifNode);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.setVisitor(deadAssignmentsElimination);
    nodeTraversal.traverse();

    assertEquals(0, getChildCount(assignmentNode1));
    assertNull(assignmentNode1.getParent());

    assertEquals(Token.VOID, assignmentNode3.getType());
  }

  @Test
  public void testTryRemoveAssignmentWithSwitch() {
    Node root = createTestNode("test", null);
    AbstractCompiler compiler = new TestCompiler();
    Scope scope = new TestScope(root);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(root);
    LiveVariablesAnalysis liveness = new LiveVariablesAnalysis(cfg, scope, compiler);
    DeadAssignmentsElimination deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node aNode = createTestNode("a", null);
    Node bNode = createTestNode("b", null);
    Node cNode = createTestNode("c", null);

    Node switchNode = createSwitchNode(createNumberNode(1), null, root);
    Node caseNode = createCaseNode(createNumberNode(1), null, cNode, createNumberNode(2), root);
    Node assignmentNode1 = createAssignmentNode("a", aNode, createNumberNode(1));

    Node bodyNode = createBlockNode();
    bodyNode.addChild(assignmentNode1);
    caseNode.setBody(bodyNode);
    switchNode.addChild(caseNode);
    root.addChild(switchNode);

    NodeTraversal nodeTraversal = new NodeTraversal(root, compiler);
    nodeTraversal.setVisitor(deadAssignmentsElimination);
    nodeTraversal.traverse();

    assertEquals(0, getChildCount(assignmentNode1));
    assertNull(assignmentNode1.getParent());

    assertEquals(Token.VOID, assignmentNode3.getType());
  }

  private Node createTestNode(String name, Node parent) {
    return new Node(Token.NAME, new Text(name), parent);
  }

  private Node createNumberNode(double value) {
    return new Node(Token.NUMBER, new Text(Double.toString(value)));
  }

  private Node createBooleanNode(boolean value) {
    return new Node(Token.BOOLEAN, new Text(Boolean.toString(value)));
  }

  private Node createAssignmentNode(String name, Node lhs, Node rhs) {
    return new Node(Token.ASSIGN, lhs, rhs);
  }

  private Node createFunctionNode(String name, Node[] parameters, Node body, Node parent) {
    Node functionNode = new Node(Token.FUNCTION, new Text(name), null);
    functionNode.setParameters(parameters);
    functionNode.setBody(body);
    functionNode.setParent(parent);
    return functionNode;
  }

  private Node createConditionalNode(Node condition, Node trueCase, Node falseCase, Node consequence, Node parent) {
    Node conditionalNode = new Node(Token.IF, condition, consequence, parent);
    if (trueCase != null) {
      conditionalNode.addChild(trueCase);
    }
    if (falseCase != null) {
      Node falseBranch = new Node(Token.ELSE);
      falseBranch.setBody(createBlockNode());
      falseBranch.addChild(falseCase);
      conditionalNode.addChild(falseBranch);
    }
    return conditionalNode;
  }

  private Node createReturnNode(Node value) {
    return new Node(Token.RETURN, value, null);
  }

  private Node createForNode(Node start, Node end, Node step) {
    Node forNode = new Node(Token.FOR, null, null, null);
    forNode.setInitialization(createBlockNode());
    forNode.setCondition(createComparisonNode(Token.LE, start, end, null));
    forNode.setIncrement(createAssignmentNode("", null, createIncrementNode(step, null, null)));
    return forNode;
  }

  private Node createSwitchNode(Node expression, Node defaultNode, Node parent) {
    Node switchNode = new Node(Token.SWITCH, expression, null, parent);
    switchNode.setDefault(defaultNode);
    return switchNode;
  }

  private Node createCaseNode(Node value, Node body, Node consequence, Node label, Node parent) {
    Node caseNode = new Node(Token.CASE, value, null, null);
    caseNode.setBody(body);
    caseNode.setConsequence(consequence);
    caseNode.setLabel(label);
    return caseNode;
  }

  private Node createBlockNode() {
    return new Node(Token.BLOCK, null, null);
  }

  private Node createIncrementNode(Node expr, Node unary, Node incrementBy) {
    Node incrementNode = new Node(Token.INC, expr, null);
    incrementNode.setIncrementBy(incrementBy);
    return incrementNode;
  }

  private Node createComparisonNode(int op, Node left, Node right, Node inExpr) {
    Node comparisonNode = new Node(Token.BINOP, left, right, null);
    comparisonNode.setType(op);
    comparisonNode.setInExpression(inExpr);
    return comparisonNode;
  }

  private int getChildCount(Node node) {
    int count = 0;
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      count++;
    }
    return count;
  }

  private class TestCompiler extends Compiler {
    @Override
    public DiGraph<Node, Branch> getControlFlowGraph(Node externalDocs, Node root) {
      return new ControlFlowGraph<Node>(root);
    }
  }

  private class TestScope extends Scope {
    private TestScope(Node root) {
      super(root);
    }

    @Override
    public Var getVar(String name) {
      return new Var(name, Var.VAR_TYPE_LOCAL, null);
    }
  }
}