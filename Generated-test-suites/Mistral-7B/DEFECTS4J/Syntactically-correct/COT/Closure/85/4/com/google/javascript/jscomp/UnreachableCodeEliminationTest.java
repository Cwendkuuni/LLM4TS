import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractVisitor.TRAVERSE;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.ControlFlowAnalysis;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class UnreachableCodeEliminationTest {

  @Test
  public void testUnreachableCodeElimination() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node rootNode = Node.newBlock("function foo() {}");
    NodeTraversal traversal = new NodeTraversal(rootNode, elimination);
    TRAVERSE.traverse(compiler, rootNode, traversal);

    // Basic functionality test
    assertEquals(0, rootNode.getChildCount());

    // Edge cases and exception handling tests

    // Test if return statement with no children is removed
    Node returnNodeWithNoChildren = Node.newReturn(null);
    Node testRootNodeWithReturn = Node.newBlock("function test() {" + returnNodeWithNoChildren.toString() + "}");
    NodeTraversal testTraversalWithReturn = new NodeTraversal(testRootNodeWithReturn, elimination);
    TRAVERSE.traverse(compiler, testRootNodeWithReturn, testTraversalWithReturn);
    assertEquals(0, testRootNodeWithReturn.getChildCount());

    // Test if return statement with children is not removed
    Node returnNodeWithChildren = Node.newBlock("function test() {" + returnNodeWithNoChildren.toString() + " return 42; }");
    NodeTraversal testTraversalWithReturnAndChildren = new NodeTraversal(returnNodeWithChildren, elimination);
    TRAVERSE.traverse(compiler, returnNodeWithChildren, testTraversalWithReturnAndChildren);
    assertNotNull(returnNodeWithChildren.getFirstChild());
    assertEquals(1, returnNodeWithChildren.getChildCount());

    // Test if statement with no side effects is removed
    Node noOpStatement = Node.newExpressionStatement(Node.newName("x.y.z.prototype.propertyName"));
    Node testRootNodeWithNoOpStatement = Node.newBlock("function test() {" + noOpStatement.toString() + " }");
    NodeTraversal testTraversalWithNoOpStatement = new NodeTraversal(testRootNodeWithNoOpStatement, elimination);
    TRAVERSE.traverse(compiler, testRootNodeWithNoOpStatement, testTraversalWithNoOpStatement);
    assertEquals(0, testRootNodeWithNoOpStatement.getChildCount());

    // Test if statement with side effects is not removed
    Node noOpStatementWithSideEffects = Node.newExpressionStatement(Node.newName("x++"));
    Node testRootNodeWithNoOpStatementWithSideEffects = Node.newBlock("function test() {" + noOpStatementWithSideEffects.toString() + " }");
    NodeTraversal testTraversalWithNoOpStatementWithSideEffects = new NodeTraversal(testRootNodeWithNoOpStatementWithSideEffects, elimination);
    TRAVERSE.traverse(compiler, testRootNodeWithNoOpStatementWithSideEffects, testTraversalWithNoOpStatementWithSideEffects);
    assertNotNull(testRootNodeWithNoOpStatementWithSideEffects.getFirstChild());
    assertEquals(1, testRootRootNodeWithNoOpStatementWithSideEffects.getChildCount());

    // Test if unreachable control flow graph node is removed
    Node unreachableNode = Node.newExpressionStatement(Node.newName("x.y.z.prototype.propertyName"));
    Node testRootNodeWithUnreachableNode = Node.newBlock("function test() {" + unreachableNode.toString() + " return 42; }");
    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, false);
    ControlFlowGraph<Node> cfg = cfa.process(null, testRootNodeWithUnreachableNode).getCfg();
    GraphReachability<Node, ControlFlowGraph.Branch> reachability = new GraphReachability<>(cfg);
    reachability.compute(cfg.getEntry().getValue());
    DiGraphNode<Node, Branch> unreachableNodeGraphNode = cfg.getDirectedGraphNode(unreachableNode);
    assertFalse(reachability.isReachable(unreachableNodeGraphNode));
    NodeTraversal testTraversalWithUnreachableNode = new NodeTraversal(testRootNodeWithUnreachableNode, elimination);
    TRAVERSE.traverse(compiler, testRootNodeWithUnreachableNode, testTraversalWithUnreachableNode);
    assertEquals(0, testRootNodeWithUnreachableNode.getChildCount());

    // Test if reachable control flow graph node is not removed
    Node reachableNode = Node.newExpressionStatement(Node.newName("x++"));
    Node testRootNodeWithReachableNode = Node.newBlock("function test() {" + unreachableNode.toString() + " " + reachableNode.toString() + " return 42; }");
    ControlFlowAnalysis cfaForReachableNode = new ControlFlowAnalysis(compiler, false, false);
    ControlFlowGraph<Node> cfgForReachableNode = cfaForReachableNode.process(null, testRootNodeWithReachableNode).getCfg();
    GraphReachability<Node, ControlFlowGraph.Branch> reachabilityForReachableNode = new GraphReachability<>(cfgForReachableNode);
    reachabilityForReachableNode.compute(cfgForReachableNode.getEntry().getValue());
    DiGraphNode<Node, Branch> reachableNodeGraphNode = cfgForReachableNode.getDirectedGraphNode(reachableNode);
    assertTrue(reachabilityForReachableNode.isReachable(reachableNodeGraphNode));
    NodeTraversal testTraversalWithReachableNode = new NodeTraversal(testRootNodeWithReachableNode, elimination);
    TRAVERSE.traverse(compiler, testRootNodeWithReachableNode, testTraversalWithReachableNode);
    assertNotNull(testRootNodeWithReachableNode.getFirstChild());
    assertEquals(2, testRootNodeWithReachableNode.getChildCount());

    // Test if unconditional branch is removed when target is the same
    Node breakNode = Node.newBreak(null);
    Node continueNode = Node.newContinue(null);
    Node returnNode = Node.newReturn(null);
    Node testRootNodeWithUnconditionalBranch =
        Node.newBlock("function test() {" +
                      "if (true) {" +
                      "  switch (x) {" +
                      "    case 1:" +
                      "      break;" +
                      "    case 2:" +
                      "      break;" +
                      "  }" +
                      "  " + breakNode.toString() +
                      "  " + breakNode.toString() +
                      "  " + breakNode.toString() +
                      "  " + returnNode.toString() +
                      "}"
                     );
    NodeTraversal testTraversalWithUnconditionalBranch = new NodeTraversal(testRootNodeWithUnconditionalBranch, elimination);
    TRAVERSE.traverse(compiler, testRootNodeWithUnconditionalBranch, testTraversalWithUnconditionalBranch);
    assertEquals(0, testRootNodeWithUnconditionalBranch.getChildCount());

    // Test if dead statement is removed from loop
    Node deadStatementInLoop = Node.newExpressionStatement(Node.newName("x++"));
    Node testRootNodeWithDeadStatementInLoop =
        Node.newBlock("function test() {" +
                      "for (int i = 0; i < 10; i++) {" +
                      "  " + deadStatementInLoop.toString() +
                      "}" +
                      "}"
                     );
    NodeTraversal testTraversalWithDeadStatementInLoop = new NodeTraversal(testRootNodeWithDeadStatementInLoop, elimination);
    TRAVERSE.traverse(compiler, testRootNodeWithDeadStatementInLoop, testTraversalWithDeadStatementInLoop);
    assertEquals(1, testRootNodeWithDeadStatementInLoop.getChildCount());
    assertFalse(NodeUtil.hasChildWithType(testRootNodeWithDeadStatementInLoop, EXPRESSION_STATEMENT));

    // Test if dead statement is removed from try-catch block
    Node deadStatementInTryCatch = Node.newExpressionStatement(Node.newName("x++"));
    Node testRootNodeWithDeadStatementInTryCatch =
        Node.newBlock("function test() {" +
                      "try {" +
                      "  " + deadStatementInTryCatch.toString() +
                      "  throw new Error();" +
                      "} catch (e) {" +
                      "  // empty" +
                      "}" +
                      "}"
                     );
    NodeTraversal testTraversalWithDeadStatementInTryCatch = new NodeTraversal(testRootNodeWithDeadStatementInTryCatch, elimination);
    TRAVERSE.traverse(compiler, testRootNodeWithDeadStatementInTryCatch, testTraversalWithDeadStatementInTryCatch);
    assertEquals(1, testRootNodeWithDeadStatementInTryCatch.getChildCount());
    assertFalse(NodeUtil.hasChildWithType(testRootNodeWithDeadStatementInTryCatch, EXPRESSION_STATEMENT));

    // Test if dead statement is removed from switch statement
    Node deadStatementInSwitch = Node.newExpressionStatement(Node.newName("x++"));
    Node testRootNodeWithDeadStatementInSwitch =
        Node.newBlock("function test() {" +
                      "switch (x) {" +
                      "  case 1:" +
                      "    " + deadStatementInSwitch.toString() +
                      "    break;" +
                      "  case 2:" +
                      "    break;" +
                      "}" +
                      "}"
                     );
    NodeTraversal testTraversalWithDeadStatementInSwitch = new NodeTraversal(testRootNodeWithDeadStatementInSwitch, elimination);
    TRAVERSE.traverse(compiler, testRootNodeWithDeadStatementInSwitch, testTraversalWithDeadStatementInSwitch);
    assertEquals(1, testRootNodeWithDeadStatementInSwitch.getChildCount());
    assertFalse(NodeUtil.hasChildWithType(testRootNodeWithDeadStatementInSwitch, EXPRESSION_STATEMENT));
  }

  @Test(expected = NullPointerException.class)
  public void testNullInput() {
    AbstractCompiler compiler = new AbstractCompiler();
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);
    elimination.process(null, null);
  }
}