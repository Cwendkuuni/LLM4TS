package org.apache.commons.cli.test; // Assuming you want to keep the test package name identical to the original one.

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphEdge;
import com.google.javascript.jscomp.graph.DiGraphNode;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private ControlFlowAnalysis controlFlowAnalysis;
  private ControlFlowGraph<Node> controlFlowGraph;

  @Before
  public void setup() {
    compiler = new AbstractCompiler() {};
    controlFlowAnalysis = new ControlFlowAnalysis(compiler, false, false);
    controlFlowGraph = controlFlowAnalysis.getCfg();
  }

  @Test
  public void testRemoveNoOpStatements() {
    Node root = createNodeWithChildren("return null;", "a.b.MyClass.prototype.propertyName;");
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    nodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, true));
    DiGraphNode<Node, Branch> rootNode = controlFlowGraph.getDirectedGraphNode(root);
    assertNull(rootNode.getAnnotation());
    DiGraphNode<Node, Branch> statementNode = rootNode.getChildNodes().get(0);
    assertThat(statementNode.getAnnotation()).isEqualTo(GraphReachability.UNREACHABLE);
  }

  @Test
  public void testRemoveDeadCodeFollowingReturnStatement() {
    Node root = createNodeWithChildren("return null;", "alert('unreachable');");
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    nodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, false));
    DiGraphNode<Node, Branch> rootNode = controlFlowGraph.getDirectedGraphNode(root);
    assertNull(rootNode.getChildNodes().get(1));
  }

  @Test
  public void testTryRemoveUnconditionalBranching() {
    Node root = createNodeWithChildren("if (true) { return; }", "break;", "continue;", "return;");
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    nodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, false));
    DiGraphNode<Node, Branch> rootNode = controlFlowGraph.getDirectedGraphNode(root);
    Node breakTarget = rootNode.getChildNodes().get(1).getValue();
    Node continueTarget = rootNode.getChildNodes().get(2).getValue();
    Node returnTarget = rootNode.getChildNodes().get(3).getValue();

    Node eliminatedBreak = tryRemoveUnconditionalBranching(breakTarget);
    Node eliminatedContinue = tryRemoveUnconditionalBranching(continueTarget);
    Node eliminatedReturn = tryRemoveUnconditionalBranching(returnTarget);

    assertSame(returnTarget, eliminatedBreak);
    assertSame(returnTarget, eliminatedContinue);
    assertSame(root, eliminatedReturn);
  }

  @Test
  public void testRemoveUnconditionalBreak() {
    Node root = createNodeWithChildren("if (true) { break; }", "return;");
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    nodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, false));
    DiGraphNode<Node, Branch> rootNode = controlFlowGraph.getDirectedGraphNode(root);
    Node breakTarget = rootNode.getChildNodes().get(0).getValue();
    Node returnTarget = rootNode.getChildNodes().get(1).getValue();

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);

    assertNull(controlFlowGraph.getDirectedGraphNode(breakTarget));
    assertSame(returnTarget, controlFlowGraph.getDirectedGraphNode(root).getValue());
  }

  @Test
  public void testRemoveUnconditionalContinue() {
    Node root = createNodeWithChildren("if (true) { continue; }", "return;");
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    nodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, false));
    DiGraphNode<Node, Branch> rootNode = controlFlowGraph.getDirectedGraphNode(root);
    Node continueTarget = rootNode.getChildNodes().get(0).getValue();
    Node returnTarget = rootNode.getChildNodes().get(1).getValue();

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);

    assertNull(controlFlowGraph.getDirectedGraphNode(continueTarget));
    assertSame(returnTarget, controlFlowGraph.getDirectedGraphNode(root).getValue());
  }

  @Test
  public void testRemoveUnconditionalReturn() {
    Node root = createNodeWithChildren("if (true) { return; }", "return;");
    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    nodeTraversal.traverse(compiler, root, new UnreachableCodeElimination(compiler, false));
    DiGraphNode<Node, Branch> rootNode = controlFlowGraph.getDirectedGraphNode(root);
    Node returnTarget1 = rootNode.getChildNodes().get(0).getValue();
    Node returnTarget2 = rootNode.getChildNodes().get(1).getValue();

    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.process(null, root);

    assertSame(returnTarget1, returnTarget2);
    assertNull(controlFlowGraph.getDirectedGraphNode(returnTarget2));
  }

  private Node createNodeWithChildren(String... children) {
    Node root = new Node(Token.BLOCK);
    for (String child : children) {
      root.addChild(new Node(createNodeFromToken(child)));
    }
    return root;
  }

  private Node createNodeFromToken(String tokenName) {
    return new Node(Token.getToken(tokenName));
  }

  private Node tryRemoveUnconditionalBranching(Node n) {
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, false);
    elimination.visit(null, n, null);
    return elimination.tryRemoveUnconditionalBranching(n);
  }
}