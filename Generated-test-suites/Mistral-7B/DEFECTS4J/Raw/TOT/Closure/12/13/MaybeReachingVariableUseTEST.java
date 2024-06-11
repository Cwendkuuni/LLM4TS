import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.graph.GraphNode;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;

  @Before
  public void setUp() {
    // Initialize the control flow graph and scope for testing.
    // Replace this with the actual initialization of the CFG and scope.
    cfg = new ControlFlowGraph<>();
    jsScope = new Scope();
  }

  @Test
  public void testSimpleCase() {
    // Prepare the Java class for testing.
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    // Add nodes to the control flow graph for testing.
    Node entryNode = Node.createNode(new Token(Token.BLOCK, 0));
    Node assignmentNode = Node.createNode(new Token(Token.ASSIGN, 1));
    Node printNode = Node.createNode(new Token(Token.PRINT, 2));
    Node exitNode = Node.createNode(new Token(Token.RETURN, 3));

    // Connect the nodes in the control flow graph.
    analysis.getGraph().addEdge(new DiGraphEdge(entryNode, assignmentNode));
    analysis.getGraph().addEdge(new DiGraphEdge(assignmentNode, printNode));
    analysis.getGraph().addEdge(new DiGraphEdge(printNode, exitNode));

    // Set the JavaScript scope for the variables used in the test.
    jsScope.declareVar("x", VarType.NUM);

    // Run the analysis.
    analysis.analyze();

    // Check the results of the analysis.
    GraphNode<Node, Branch> assignmentNodeGraph = analysis.getGraph().getNode(assignmentNode);
    FlowState<MaybeReachingVariableUse.ReachingUses> state = assignmentNodeGraph.getAnnotation();

    // Check that the variable "x" is reported as possibly reaching the use at the print statement.
    Collection<Node> uses = analysis.getUses("x", assignmentNode);
    assertNotNull(uses);
    assertEquals(1, uses.size());
    assertEquals(printNode, uses.iterator().next());
  }

  @Test
  public void testConditionalBranch() {
    // Prepare the Java class for testing.
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    // Add nodes to the control flow graph for testing.
    Node entryNode = Node.createNode(new Token(Token.BLOCK, 0));
    Node conditionNode = Node.createNode(new Token(Token.IF, 1));
    Node assignmentNode1 = Node.createNode(new Token(Token.ASSIGN, 2));
    Node printNode1 = Node.createNode(new Token(Token.PRINT, 3));
    Node assignmentNode2 = Node.createNode(new Token(Token.ASSIGN, 4));
    Node printNode2 = Node.createNode(new Token(Token.PRINT, 5));
    Node exitNode = Node.createNode(new Token(Token.RETURN, 6));

    // Connect the nodes in the control flow graph.
    analysis.getGraph().addEdge(new DiGraphEdge(entryNode, conditionNode));
    analysis.getGraph().addEdge(new DiGraphEdge(conditionNode, assignmentNode1));
    analysis.getGraph().addEdge(new DiGraphEdge(conditionNode, assignmentNode2));
    analysis.getGraph().addEdge(new DiGraphEdge(assignmentNode1, printNode1));
    analysis.getGraph().addEdge(new DiGraphEdge(assignmentNode2, printNode2));
    analysis.getGraph().addEdge(new DiGraphEdge(printNode1, exitNode));
    analysis.getGraph().addEdge(new DiGraphEdge(printNode2, exitNode));

    // Set the JavaScript scope for the variables used in the test.
    jsScope.declareVar("x", VarType.NUM);

    // Run the analysis.
    analysis.analyze();

    // Check the results of the analysis.
    GraphNode<Node, Branch> conditionNodeGraph = analysis.getGraph().getNode(conditionNode);
    FlowState<MaybeReachingVariableUse.ReachingUses> state1 = conditionNodeGraph.getAnnotation();
    Collection<Node> uses1 = analysis.getUses("x", conditionNode);
    GraphNode<Node, Branch> assignmentNode1Graph = analysis.getGraph().getNode(assignmentNode1);
    FlowState<MaybeReachingVariableUse.ReachingUses> state2 = assignmentNode1Graph.getAnnotation();

    // Check that the variable "x" is reported as possibly reaching the use at both
    // the print statements, but only the first one has an actual use.
    assertNotNull(uses1);
    assertEquals(1, uses1.size());
    assertEquals(printNode1, uses1.iterator().next());
    assertNotNull(state1.getOut());
    assertEquals(2, state1.getOut().mayUseMap.size());
    assertTrue(state1.getOut().mayUseMap.containsKey(jsScope.getVar("x")));
    assertEquals(2, state2.getOut().mayUseMap.size());
    assertTrue(state2.getOut().mayUseMap.containsKey(jsScope.getVar("x")));
  }

  @Test
  public void testEscapedVariable() {
    // Prepare the Java class for testing.
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);

    // Add nodes to the control flow graph for testing.
    Node entryNode = Node.createNode(new Token(Token.BLOCK, 0));
    Node assignmentNode = Node.createNode(new Token(Token.ASSIGN, 1));
    Node printNode = Node.createNode(new Token(Token.PRINT, 2));
    Node returnNode = Node.createNode(new Token(Token.RETURN, 3));
    Node escapeNode = Node.createNode(new Token(Token.NAME, "escape"));

    // Connect the nodes in the control flow graph.
    analysis.getGraph().addEdge(new DiGraphEdge(entryNode, assignmentNode));
    analysis.getGraph().addEdge(new DiGraphEdge(assignmentNode, printNode));
    analysis.getGraph().addEdge(new DiGraphEdge(printNode, escapeNode));
    analysis.getGraph().addEdge(new DiGraphEdge(escapeNode, returnNode));

    // Set the JavaScript scope for the variables used in the test.
    jsScope.declareVar("x", VarType.NUM);
    jsScope.declareVar("escape", VarType.GLOBAL);

    // Run the analysis.
    analysis.analyze();

    // Check the results of the analysis.
    GraphNode<Node, Branch> assignmentNodeGraph = analysis.getGraph().getNode(assignmentNode);
    FlowState<MaybeReachingVariableUse.ReachingUses> state = assignmentNodeGraph.getAnnotation();

    // The escaped variable should not be reported as possibly reaching the use.
    Collection<Node> uses = analysis.getUses("x", assignmentNode);
    assertNotNull(uses);
    assertEquals(1, uses.size());
    assertEquals(printNode, uses.iterator().next());
    assertNotNull(state.getOut());
    assertFalse(state.getOut().mayUseMap.containsKey(jsScope.getVar("x")));
    assertFalse(state.getOut().mayUseMap.containsKey(jsScope.getVar("escape")));
  }
}