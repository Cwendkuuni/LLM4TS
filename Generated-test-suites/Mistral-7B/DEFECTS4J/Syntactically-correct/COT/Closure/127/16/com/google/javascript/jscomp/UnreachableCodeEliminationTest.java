import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import java.util.List;

public class UnreachableCodeEliminationTest {

  @SuppressWarnings("unchecked")
  private static <T> T newInstance(Class<T> clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("deprecation")
  private static ControlFlowAnalysis newControlFlowAnalysis(AbstractCompiler compiler) {
    return (ControlFlowAnalysis) newInstance(ControlFlowAnalysis.class);
  }

  @SuppressWarnings("deprecation")
  private static GraphReachability newGraphReachability(ControlFlowGraph<Node> cfg) {
    return new GraphReachability<Node, ControlFlowGraph.Branch>(cfg);
  }

  @SuppressWarnings("deprecation")
  private static ControlFlowGraph<Node> newControlFlowGraph() {
    return (ControlFlowGraph<Node>) newInstance(ControlFlowGraph.class);
  }

  @SuppressWarnings("deprecation")
  private static DiGraphNode<Node, Branch> newDiGraphNode(Node node) {
    return (DiGraphNode<Node, Branch>) newInstance(DiGraphNode.class);
  }

  @SuppressWarnings("deprecation")
  private static DiGraphEdge<Node, Branch> newDiGraphEdge(Node from, Node to, Branch branch) {
    return (DiGraphEdge<Node, Branch>) newInstance(DiGraphEdge.class);
  }

  private AbstractCompiler newCompiler() {
    return newInstance(AbstractCompiler.class);
  }

  private UnreachableCodeElimination newUnreachableCodeElimination(AbstractCompiler compiler, boolean removeNoOpStatements) {
    return new UnreachableCodeElimination(compiler, removeNoOpStatements);
  }

  // Test method for UnreachableCodeElimination constructor
  @org.junit.Test
  public void testConstructor() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);
  }

  // Test method for process()
  @org.junit.Test
  public void testProcess() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);
    Node toplevel = Node.root();
    elimination.process(Node.root(), toplevel);
  }

  // Test method for visit() with a null node
  @org.junit.Test
  public void testVisitNullNode() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);
    Node parent = Node.root();
    Node n = null;
    elimination.visit(NodeTraversal.getTraverser(compiler), parent, n);
  }

  // Test method for visit() with a Node having no side effects
  @org.junit.Test
  public void testVisitNoOpStatement() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);

    Node parent = Node.newBlock("test");
    Node n = Node.newAssignment(parent, "x", Node.newNumberLiteral(1));

    elimination.visit(NodeTraversal.getTraverser(compiler), parent, n);
    assertTrue(parent.hasChild(n));
  }

  // Test method for visit() with a Node that has side effects
  @org.junit.Test
  public void testVisitStatementWithSideEffects() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);

    Node parent = Node.newBlock("test");
    Node n = Node.newAssignment(parent, "x", Node.newAssignment(Node.newVariable("y"), Node.newNumberLiteral(1)));

    elimination.visit(NodeTraversal.getTraverser(compiler), parent, n);
    assertTrue(parent.hasChild(n));
  }

  // Test method for visit() with a Node that is a function
  @org.junit.Test
  public void testVisitFunction() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);

    Node parent = Node.newBlock("test");
    Node functionDeclaration = Node.newFunction("testFunction", Node.newList(), Node.newBlock("testFunction"));
    Node functionCall = Node.newCall(Node.newVariable("testFunction"));
    parent.addChild(functionDeclaration);
    parent.addChild(functionCall);

    elimination.visit(NodeTraversal.getTraverser(compiler), parent, functionCall);
    assertTrue(parent.hasChild(functionDeclaration));
    assertTrue(parent.hasChild(functionCall));
  }

  // Test method for visit() with a Node that is a return statement
  @org.junit.Test
  public void testVisitReturnStatement() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);

    Node parent = Node.newBlock("test");
    Node returnStatement = Node.newReturnStatement();
    parent.addChild(returnStatement);

    elimination.visit(NodeTraversal.getTraverser(compiler), parent, returnStatement);
    assertFalse(parent.hasChild(returnStatement));
  }

  // Test method for visit() with a Node that is a break statement
  @org.junit.Test
  public void testVisitBreakStatement() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);

    Node parent = Node.newBlock("test");
    Node whileStatement = Node.newWhileStatement(Node.newExpressionStatement(Node.newVariable("x").set("1")), Node.newExpressionStatement(Node.newReturnStatement()));
    parent.addChild(whileStatement);

    Node breakStatement = Node.newBreakStatement();
    whileStatement.addChild(breakStatement);

    elimination.visit(NodeTraversal.getTraverser(compiler), parent, breakStatement);
    assertFalse(parent.hasChild(breakStatement));
    assertFalse(whileStatement.hasChild(breakStatement));
  }

  // Test method for visit() with a Node that is a continue statement
  @org.junit.Test
  public void testVisitContinueStatement() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);

    Node parent = Node.newBlock("test");
    Node forStatement = Node.newForStatement(Node.newVariable("x"), Node.newExpressionStatement(Node.newReturnStatement()), Node.newBlock("forBody"), Node.newExpressionStatement(Node.newReturnStatement()));
    parent.addChild(forStatement);

    Node continueStatement = Node.newContinueStatement();
    forStatement.addChild(continueStatement);

    elimination.visit(NodeTraversal.getTraverser(compiler), parent, continueStatement);
    assertFalse(parent.hasChild(continueStatement));
    assertFalse(forStatement.hasChild(continueStatement));
  }

  // Test method for removeDeadExprStatementSafely() with a Node having no side effects
  @org.junit.Test
  public void testRemoveDeadExprStatementSafelyNoSideEffects() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);

    Node parent = Node.newBlock("test");
    Node n = Node.newExpressionStatement(Node.newAssignment(Node.newVariable("x"), Node.newNumberLiteral(1)));
    parent.addChild(n);

    elimination.removeDeadExprStatementSafely(n);
    assertFalse(parent.hasChild(n));
  }

  // Test method for removeDeadExprStatementSafely() with a Node having side effects
  @org.junit.Test
  public void testRemoveDeadExprStatementSafelyWithSideEffects() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);

    Node parent = Node.newBlock("test");
    Node n = Node.newExpressionStatement(Node.newAssignment(Node.newVariable("x"), Node.newAssignment(Node.newVariable("y"), Node.newNumberLiteral(1))));
    parent.addChild(n);

    elimination.removeDeadExprStatementSafely(n);
    assertTrue(parent.hasChild(n));
  }

  // Test method for removeDeadExprStatementSafely() with a FOR-IN header
  @org.junit.Test
  public void testRemoveDeadExprStatementSafelyWithFORINHeader() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);

    Node parent = Node.newBlock("test");
    Node forInHeader = Node.newForInStatement(Node.newVariable("x"), Node.newList(Node.newIdentifier("Array")), Node.newBlock("forBody"));
    Node n = Node.newExpressionStatement(Node.newReturnStatement());
    forInHeader.addChild(n);
    parent.addChild(forInHeader);

    elimination.removeDeadExprStatementSafely(n);
    assertTrue(parent.hasChild(forInHeader));
    assertFalse(forInHeader.hasChild(n));
  }

  // Test method for tryRemoveUnconditionalBranching() with a break statement
  @org.junit.Test
  public void testTryRemoveUnconditionalBranchingBreakStatement() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);

    Node parent = Node.newBlock("test");
    Node breakStatement = Node.newBreakStatement();
    Node target = Node.newVariable("x");
    Node conditionBranch = Node.newIfStatement(Node.newExpressionStatement(Node.newReturnStatement()), breakStatement, Node.newBlock("elsePart"));
    Node targetBlock = Node.newBlock("targetBlock");
    targetBlock.addChild(target);
    targetBlock.addChild(conditionBranch);
    parent.addChild(targetBlock);

    elimination.tryRemoveUnconditionalBranching(breakStatement);
    assertTrue(parent.hasChild(targetBlock));
    assertFalse(targetBlock.hasChild(breakStatement));
  }

  // Test method for tryRemoveUnconditionalBranching() with a continue statement
  @org.junit.Test
  public void testTryRemoveUnconditionalBranchingContinueStatement() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);

    Node parent = Node.newBlock("test");
    Node continueStatement = Node.newContinueStatement();
    Node target = Node.newVariable("x");
    Node conditionBranch = Node.newIfStatement(Node.newExpressionStatement(Node.newReturnStatement()), continueStatement, Node.newBlock("elsePart"));
    Node targetBlock = Node.newBlock("targetBlock");
    targetBlock.addChild(target);
    targetBlock.addChild(conditionBranch);
    parent.addChild(targetBlock);

    elimination.tryRemoveUnconditionalBranching(continueStatement);
    assertTrue(parent.hasChild(targetBlock));
    assertFalse(targetBlock.hasChild(continueStatement));
  }

  // Test method for tryRemoveUnconditionalBranching() with a return statement
  @org.junit.Test
  public void testTryRemoveUnconditionalBranchingReturnStatement() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);

    Node parent = Node.newBlock("test");
    Node returnStatement = Node.newReturnStatement();
    Node target = Node.newVariable("x");
    Node conditionBranch = Node.newIfStatement(Node.newExpressionStatement(Node.newReturnStatement()), returnStatement, Node.newBlock("elsePart"));
    Node targetBlock = Node.newBlock("targetBlock");
    targetBlock.addChild(target);
    targetBlock.addChild(conditionBranch);
    parent.addChild(targetBlock);

    elimination.tryRemoveUnconditionalBranching(returnStatement);
    assertTrue(parent.hasChild(targetBlock));
    assertFalse(targetBlock.hasChild(returnStatement));
  }

  // Test method for tryRemoveUnconditionalBranching() with a conditional branch
  @org.junit.Test
  public void testTryRemoveUnconditionalBranchingConditionalBranch() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);

    Node parent = Node.newBlock("test");
    Node conditionBranch = Node.newIfStatement(Node.newExpressionStatement(Node.newReturnStatement()), Node.newBlock("trueBlock"), Node.newBlock("falseBlock"));
    Node target = Node.newVariable("x");
    Node targetBlock = Node.newBlock("targetBlock");
    targetBlock.addChild(target);
    targetBlock.addChild(conditionBranch);
    parent.addChild(targetBlock);

    elimination.tryRemoveUnconditionalBranching(conditionBranch);
    assertTrue(parent.hasChild(targetBlock));
    assertFalse(targetBlock.hasChild(conditionBranch));
  }

  // Test method for tryRemoveUnconditionalBranching() with a loop
  @org.junit.Test
  public void testTryRemoveUnconditionalBranchingLoop() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);

    Node parent = Node.newBlock("test");
    Node loop = Node.newForStatement(Node.newVariable("x"), Node.newList(Node.newNumberLiteral(1)), Node.newBlock("loopBody"));
    Node target = Node.newVariable("x");
    Node targetBlock = Node.newBlock("targetBlock");
    targetBlock.addChild(target);
    targetBlock.addChild(loop);
    parent.addChild(targetBlock);

    elimination.tryRemoveUnconditionalBranching(loop);
    assertTrue(parent.hasChild(targetBlock));
    assertFalse(targetBlock.hasChild(loop));
  }

  // Test method for removeNode()
  @org.junit.Test
  public void testRemoveNode() {
    AbstractCompiler compiler = newCompiler();
    UnreachableCodeElimination elimination = newUnreachableCodeElimination(compiler, true);

    Node parent = Node.newBlock("test");
    Node n = Node.newVariable("x");
    parent.addChild(n);

    elimination.removeNode(n);
    assertFalse(parent.hasChild(n));
  }
}