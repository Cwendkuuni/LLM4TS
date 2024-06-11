import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class UnreachableCodeEliminationTest {

  private UnreachableCodeElimination testee;
  private AbstractCompiler compilerMock;
  private ControlFlowAnalysis cfaMock;
  private Scope scopeMock;
  private Node rootNode;
  private NodeTraversal traversalMock;

  @org.junit.Before
  public void setUp() {
    compilerMock = mock(AbstractCompiler.class);
    cfaMock = mock(ControlFlowAnalysis.class);
    scopeMock = mock(Scope.class);
    when(scopeMock.getRootNode()).thenReturn(rootNode);
    traversalMock = mock(NodeTraversal.class);

    testee = new UnreachableCodeElimination(compilerMock, true);
  }

  @org.junit.Test
  public void testEnterScope() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getScope()).thenReturn(scopeMock);

    testee.enterScope(traversal);

    assertSame("Expected curCfg to be set.", testee.curCfg, cfaMock.getCfg());
  }

  @org.junit.Test
  public void testExitScope() {
    testee.exitScope(traversalMock);

    assertNull("Expected curCfg to be null.", testee.curCfg);
  }

  @org.junit.Test
  public void testProcess() {
    NodeFactory nodeFactory = NodeFactory.getFactory();
    rootNode = nodeFactory.parseTree("function test() {}");

    testee.process(null, rootNode);

    // Calling process method should not have caused any changes.
    assertSame("Unexpected root after process.", rootNode, compilerMock.getSourceRoot());
  }

  @org.junit.Test
  public void testVisitNullNode() {
    testee.visit(traversalMock, null, null);

    // No-op.
  }

  @org.junit.Test
  public void testVisitFunctionNode() {
    testee.visit(traversalMock, createFunctionNode("function test() {}"), null);

    // No-op.
  }

  @org.junit.Test
  public void testVisitScriptNode() {
    testee.visit(traversalMock, createScriptNode("function test() {}"), null);

    // No-op.
  }

  @org.junit.Test
  public void testVisitReturnStatement() {
    Node returnStmt = createReturnStatement();
    Node parent = createBlockNode();
    parent.addChildToEnd(returnStmt);

    testee.visit(traversalMock, returnStmt, parent);

    // No-op.
  }

  @org.junit.Test
  public void testVisitBlockNodeWithEmptyStatement() {
    Node blockNode = createBlockNode();

    testee.visit(traversalMock, blockNode, null);

    assertFalse("Expected node to not be removed.", NodeUtil.containsNode(rootNode, blockNode));
  }

  @org.junit.Test
  public void testVisitBlockNodeWithChildren() {
    Node blockNode = createBlockNode();
    Node child = createExpressionNode(1);
    blockNode.addChildToEnd(child);

    testee.visit(traversalMock, blockNode, null);

    assertFalse("Expected node to not be removed.", NodeUtil.containsNode(rootNode, blockNode));
  }

  @org.junit.Test
  public void testVisitBreakStatement() {
    Node breakStmt = createBreakStatement();
    Node parent = createBlockNode();
    parent.addChildToEnd(breakStmt);

    Node following = createExpressionNode(2);
    Node nextBlockNode = createBlockNode();
    nextBlockNode.addChildToEnd(following);
    parent.addChildToEnd(nextBlockNode);

    testee.visit(traversalMock, breakStmt, parent);

    // Break statement should be removed and control should be transferred to the
    // following statement.
    assertFalse("Expected break statement to be removed.", NodeUtil.containsNode(rootNode, breakStmt));
    assertSame("Expected control to be transferred to the following statement.", following, parent.getNext());
  }

  @org.junit.Test
  public void testVisitContinueStatement() {
    Node continueStmt = createContinueStatement();
    Node parent = createBlockNode();
    parent.addChildToEnd(continueStmt);

    Node following = createExpressionNode(2);
    Node nextBlockNode = createBlockNode();
    nextBlockNode.addChildToEnd(following);
    parent.addChildToEnd(nextBlockNode);

    testee.visit(traversalMock, continueStmt, parent);

    // Continue statement should be removed and control should be transferred to the
    // next iteration of the loop.
    assertFalse("Expected continue statement to be removed.", NodeUtil.containsNode(rootNode, continueStmt));
    assertSame("Expected control to be transferred to the next iteration.", parent, nextBlockNode.getParent());
  }

  @org.junit.Test
  public void testVisitExpressionNodeWithSideEffects() {
    Node expressionNode = createExpressionNode(1);

    testee.visit(traversalMock, expressionNode, null);

    // Expression node should not be removed.
    assertSame("Expected node to not be removed.", expressionNode, NodeUtil.findNode(rootNode, expressionNode));
  }

  @org.junit.Test
  public void testVisitExpressionNodeWithoutSideEffects() {
    Node expressionNode = createExpressionNode(1);

    testee.setRemoveNoOpStatements(false);

    testee.visit(traversalMock, expressionNode, null);

    // Expression node should not be removed even if removeNoOpStatements is false.
    assertSame("Expected node to not be removed.", expressionNode, NodeUtil.findNode(rootNode, expressionNode));
  }

  @org.junit.Test
  public void testVisitExpressionNodeWithoutSideEffectsWithRemoveNoOpStatements() {
    Node expressionNode = createExpressionNode(1);

    testee.visit(traversalMock, expressionNode, null);

    // Expression node should be removed if it has no side effects and removeNoOpStatements is true.
    assertFalse("Expected node to be removed.", NodeUtil.containsNode(rootNode, expressionNode));
  }

  @org.junit.Test
  public void testTryRemoveUnconditionalBranching() {
    Node breakStmt = createBreakStatement();
    Node following = createExpressionNode(2);
    Node breakTarget = createExpressionNode(3);
    Node nextBlockNode = createBlockNode();
    nextBlockNode.addChildToEnd(following);
    breakTarget.addChildToEnd(nextBlockNode);
    breakStmt.addChildToEnd(breakTarget);

    Node result = testee.tryRemoveUnconditionalBranching(breakStmt);

    // Control should be transferred to the following statement.
    assertSame("Expected result to be the following statement.", following, result);
  }

  @org.junit.Test
  public void testTryRemoveUnconditionalBranchingWithOneNodeTarget() {
    Node breakStmt = createBreakStatement();
    Node following = createExpressionNode(2);
    Node nextBlockNode = createBlockNode();
    nextBlockNode.addChildToEnd(following);
    breakStmt.addChildToEnd(nextBlockNode);

    Node result = testee.tryRemoveUnconditionalBranching(following);

    // Following statement should not be removed because the branch is not unconditional.
    assertSame("Unexpected result.", following, result);
  }

  @org.junit.Test
  public void testTryRemoveUnconditionalBranchingWithMultipleTargets() {
    Node breakStmt = createBreakStatement();
    Node following1 = createExpressionNode(2);
    Node nextBlockNode1 = createBlockNode();
    nextBlockNode1.addChildToEnd(following1);
    Node following2 = createExpressionNode(3);
    Node nextBlockNode2 = createBlockNode();
    nextBlockNode2.addChildToEnd(following2);
    breakStmt.addChildToEnd(nextBlockNode1);
    breakStmt.addChildToEnd(nextBlockNode2);

    Node result = testee.tryRemoveUnconditionalBranching(following1);

    // Following statement should be removed because it is the first target
    // of an unconditional branch and the control flow remains the same.
    assertSame("Expected result to be the first following statement.", following1, result);
  }

  @org.junit.Test
  public void testTryRemoveUnconditionalBranchingWithMultipleTargetsAndComplexControlFlow() {
    Node breakStmt = createBreakStatement();
    Node following1 = createExpressionNode(2);
    Node nextBlockNode1 = createBlockNode();
    nextBlockNode1.addChildToEnd(following1);
    Node continueStmt = createContinueStatement();
    Node nextBlockNode2 = createBlockNode();
    nextBlockNode2.addChildToEnd(continueStmt);
    Node returnStmt = createReturnStatement();
    Node nextBlockNode3 = createBlockNode();
    nextBlockNode3.addChildToEnd(returnStmt);
    breakStmt.addChildToEnd(nextBlockNode1);
    breakStmt.addChildToEnd(nextBlockNode2);
    breakStmt.addChildToEnd(nextBlockNode3);

    Node result = testee.tryRemoveUnconditionalBranching(following1);

    // Following statement should be removed because it is the first target
    // of an unconditional branch and the control flow remains the same.
    assertSame("Expected result to be the first following statement.", following1, result);
  }

  private Node createBlockNode() {
    return createNode(Token.BLOCK, new ArrayList<>());
  }

  private Node createFunctionNode(String code) {
    Node function = createNode(Token.FUNCTION, null, code);
    function.setName(code);
    return function;
  }

  private Node createScriptNode(String code) {
    return createNode(Token.SCRIPT, null, code);
  }

  private Node createBreakStatement() {
    return createNode(Token.BREAK, null);
  }

  private Node createContinueStatement() {
    return createNode(Token.CONTINUE, null);
  }

  private Node createNode(int type, Node parent, String code) {
    Node node = NodeFactory.createNode(type, NodeFactory.createText(code));
    if (parent != null) {
      parent.addChildToEnd(node);
    }
    return node;
  }

  private Node createNode(int type, List<Node> children) {
    Node node = NodeFactory.createNode(type, children.toArray(new Node[children.size()]));
    return node;
  }

  private Node createExpressionNode(int value) {
    return createNode(Token.NUMBER, null, Integer.toString(value));
  }

  private Node createReturnStatement() {
    return createNode(Token.RETURN, null);
  }

  private void testeeSetRemoveNoOpStatements(boolean removeNoOpStatements) {
    testee.setRemoveNoOpStatements(removeNoOpStatements);
  }
}