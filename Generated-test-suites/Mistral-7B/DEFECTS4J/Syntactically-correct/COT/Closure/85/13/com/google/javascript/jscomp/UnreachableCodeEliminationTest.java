package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.jscomp.graph.GraphReachability;
import com.google.javascript.rhino.NodeUtil;

import org.junit.Test;

public class UnreachableCodeEliminationTest {
  @Test
  public void testUnreachableCodeElimination() {
    // Given
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    UnreachableCodeElimination unreachableCodeElimination =
        new UnreachableCodeElimination(compiler, true);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node root = Node.newBlock(new Node(Token.SCRIPT));

    // When
    NodeTraversal.traverse(compiler, root, unreachableCodeElimination);

    // Then
    ControlFlowGraph<Node> controlFlowGraph = unreachableCodeElimination.curCfg;
    DiGraphNode<Node, Branch> entryNode = controlFlowGraph.getEntry();
    assertEquals(0, controlFlowGraph.getNodes().size());
    assertEquals(entryNode, controlFlowGraph.getNodes().get(0));

    // Basic test case for visit method
    Node testNode = Node.newExpressionStatement(new Node(Token.RETURN, null));
    DiGraphNode<Node, Branch> testNodeGraphNode = controlFlowGraph.getDirectedGraphNode(testNode);

    assertEquals(GraphReachability.UNREACHABLE, testNodeGraphNode.getAnnotation());

    // Test for edge cases and exception handling scenarios

    // Test for a node with no children
    Node nodeWithNoChildren = Node.newExpressionStatement(new Node(Token.VOID, null));
    DiGraphNode<Node, Branch> nodeWithNoChildrenGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithNoChildren);

    assertEquals(GraphReachability.REACHABLE, nodeWithNoChildrenGraphNode.getAnnotation());

    // Test for a node with side effects
    Node nodeWithSideEffects = Node.newExpressionStatement(new Node(Token.ASSIGN, null));
    DiGraphNode<Node, Branch> nodeWithSideEffectsGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithSideEffects);

    assertEquals(GraphReachability.REACHABLE, nodeWithSideEffectsGraphNode.getAnnotation());

    // Test for a node with a direct child
    Node nodeWithDirectChild = Node.newExpressionStatement(
        Node.newAssignment(new Node(Token.VAR, null), new Node(Token.NUMBER, 5)));
    DiGraphNode<Node, Branch> nodeWithDirectChildGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithDirectChild);

    assertEquals(GraphReachability.REACHABLE, nodeWithDirectChildGraphNode.getAnnotation());

    // Test for a node with a conditional branch
    Node nodeWithConditionalBranch = Node.newConditionalExpression(
        Node.newExpressionStatement(new Node(Token.RETURN, null)),
        Node.newExpressionStatement(new Node(Token.NUMBER, 5)),
        Node.newExpressionStatement(new Node(Token.NUMBER, 10)));
    DiGraphNode<Node, Branch> nodeWithConditionalBranchGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithConditionalBranch);

    assertEquals(GraphReachability.REACHABLE, nodeWithConditionalBranchGraphNode.getAnnotation());

    // Test for a node with a loop
    Node nodeWithLoop = Node.newExpressionStatement(
        Node.newDoWhileStatement(new Node(Token.FALSE), new Node(Token.ASSIGN, null)));
    DiGraphNode<Node, Branch> nodeWithLoopGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithLoop);

    assertEquals(GraphReachability.REACHABLE, nodeWithLoopGraphNode.getAnnotation());

    // Test for a node with a switch statement
    Node nodeWithSwitchStatement = Node.newExpressionStatement(
        Node.newSwitchStatement(new Node(Token.NUMBER, 5), new Node(Token.BLOCK), null, null));
    DiGraphNode<Node, Branch> nodeWithSwitchStatementGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithSwitchStatement);

    assertEquals(GraphReachability.REACHABLE, nodeWithSwitchStatementGraphNode.getAnnotation());

    // Test for a node with a try statement
    Node nodeWithTryStatement = Node.newExpressionStatement(
        Node.newTryStatement(new Node(Token.BLOCK), null, new Node(Token.BLOCK), null));
    DiGraphNode<Node, Branch> nodeWithTryStatementGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithTryStatement);

    assertEquals(GraphReachability.REACHABLE, nodeWithTryStatementGraphNode.getAnnotation());

    // Test for a node with a return statement
    Node nodeWithReturnStatement = Node.newReturnStatement(new Node(Token.NUMBER, 5));
    DiGraphNode<Node, Branch> nodeWithReturnStatementGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithReturnStatement);

    assertEquals(GraphReachability.UNREACHABLE, nodeWithReturnStatementGraphNode.getAnnotation());

    // Test for a node with a break statement
    Node nodeWithBreakStatement = Node.newExpressionStatement(new Node(Token.BREAK));
    DiGraphNode<Node, Branch> nodeWithBreakStatementGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithBreakStatement);

    assertEquals(GraphReachability.UNREACHABLE, nodeWithBreakStatementGraphNode.getAnnotation());

    // Test for a node with a continue statement
    Node nodeWithContinueStatement = Node.newExpressionStatement(new Node(Token.CONTINUE));
    DiGraphNode<Node, Branch> nodeWithContinueStatementGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithContinueStatement);

    assertEquals(GraphReachability.UNREACHABLE, nodeWithContinueStatementGraphNode.getAnnotation());

    // Test for a node with a throw statement
    Node nodeWithThrowStatement = Node.newExpressionStatement(new Node(Token.THROW, null));
    DiGraphNode<Node, Branch> nodeWithThrowStatementGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithThrowStatement);

    assertEquals(GraphReachability.UNREACHABLE, nodeWithThrowStatementGraphNode.getAnnotation());

    // Test for a node with a label statement
    Node nodeWithLabelStatement = Node.newLabelStatement("label");
    DiGraphNode<Node, Branch> nodeWithLabelStatementGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithLabelStatement);

    assertEquals(GraphReachability.UNREACHABLE, nodeWithLabelStatementGraphNode.getAnnotation());

    // Test for a node with an empty statement
    Node nodeWithEmptyStatement = Node.newExpressionStatement(new Node(Token.VOID, null));
    DiGraphNode<Node, Branch> nodeWithEmptyStatementGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithEmptyStatement);

    assertEquals(GraphReachability.UNREACHABLE, nodeWithEmptyStatementGraphNode.getAnnotation());

    // Test for a node with a function statement
    Node nodeWithFunctionStatement = Node.newFunctionDeclaration(
        "testFunction", Node.newBlock(new Node(Token.BLOCK)), null, null);
    DiGraphNode<Node, Branch> nodeWithFunctionStatementGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithFunctionStatement);

    assertEquals(GraphReachability.UNREACHABLE, nodeWithFunctionStatementGraphNode.getAnnotation());

    // Test for a node with a script statement
    Node nodeWithScriptStatement = Node.newScript(new Node(Token.SCRIPT), null);
    DiGraphNode<Node, Branch> nodeWithScriptStatementGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithScriptStatement);

    assertEquals(GraphReachability.UNREACHABLE, nodeWithScriptStatementGraphNode.getAnnotation());

    // Test for a node with a comment
    Node nodeWithComment = Node.newComment("This is a comment");
    DiGraphNode<Node, Branch> nodeWithCommentGraphNode =
        controlFlowGraph.getDirectedGraphNode(nodeWithComment);

    assertEquals(GraphReachability.UNREACHABLE, nodeWithCommentGraphNode.getAnnotation());
  }
}