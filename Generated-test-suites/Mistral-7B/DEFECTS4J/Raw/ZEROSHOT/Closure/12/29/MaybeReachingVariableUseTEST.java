import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.GraphNode;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  private static final Scope MOCK_JS_SCOPE = mock(Scope.class);

  @org.junit.Before
  public void setup() {
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);
    ControlFlowGraph<Node> mockControlFlowGraph = mock(ControlFlowGraph.class);

    MaybeReachingVariableUse maybeReachingVariableUse =
        new MaybeReachingVariableUse(mockControlFlowGraph, MOCK_JS_SCOPE, mockCompiler);
  }

  @org.junit.Test
  public void testSimpleFlowThrough() {
    Node n = Node.newLiteral(Token.INT_LITERAL, 1);
    ReachingUses input = new ReachingUses();
    input.mayUseMap.put(new Var("A", MOCK_JS_SCOPE), n);
    ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);
    assertEquals(input, output);
  }

  @org.junit.Test
  public void testFlowThroughAssignment() {
    Node definitionNode = Node.newName("A");
    Node useNode = Node.newName("A");
    ReachingUses input = new ReachingUses();
    input.mayUseMap.put(new Var("A", MOCK_JS_SCOPE), definitionNode);
    ReachingUses output = maybeReachingVariableUse.flowThrough(useNode, input);
    assertEquals(definitionNode, output.mayUseMap.get(new Var("A", MOCK_JS_SCOPE)).get(0));
  }

  @org.junit.Test
  public void testFlowThroughIfStatement() {
    Node conditionNode = Node.newBinaryOp(Token.LOGICAL_OR, Node.newLiteral(Token.BOOL_LITERAL, true), Node.newLiteral(Token.BOOL_LITERAL, false));
    Node definitionNode = Node.newName("A");
    Node useNode = Node.newName("A");
    ReachingUses input = new ReachingUses();
    input.mayUseMap.put(new Var("A", MOCK_JS_SCOPE), definitionNode);
    ReachingUses output = maybeReachingVariableUse.flowThrough(useNode, input);
    assertEquals(definitionNode, output.mayUseMap.get(new Var("A", MOCK_JS_SCOPE)).get(0));
  }

  @org.junit.Test
  public void testFlowThroughForLoop() {
    Node initNode = Node.newVar("x");
    Node conditionNode = Node.newBinaryOp(Token.LOGICAL_AND, Node.newName("y"), Node.newName("x"));
    Node assignmentNode = Node.newAssign(Node.newName("x"), Node.newLiteral(Token.INT_LITERAL, 1));
    Node useNode = Node.newName("x");
    ReachingUses input = new ReachingUsages();
    input.mayUseMap.put(new Var("x", MOCK_JS_SCOPE), Node.newName("y"));
    ReachingUses output = maybeReachingVariableUse.flowThrough(useNode, input);
    assertEquals(Node.newName("y"), output.mayUseMap.get(new Var("x", MOCK_JS_SCOPE)).get(0));
  }

  @org.junit.Test
  public void testComputeMayUseMethod() {
    Node n = Node.newName("A");
    Node definitionNode = Node.newAssign(n, Node.newName("B"));
    ReachingUses use = new ReachingUses();
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
        null, MOCK_JS_SCOPE, null);
    maybeReachingVariableUse.computeMayUse(definitionNode, definitionNode, use, false);
    assertEquals(definitionNode, use.mayUseMap.get(new Var("A", MOCK_JS_SCOPE)).get(0));
  }

  @org.junit.Test
  public void testGetUsesMethod() {
    Node definitionNode = Node.newName("A");
    ReachingUses use = new ReachingUses();
    use.mayUseMap.put(new Var("A", MOCK_JS_SCOPE), definitionNode);
    GraphNode<Node, Branch> graphNode = mock(GraphNode.class);
    when(graphNode.getAnnotation()).thenReturn(new FlowState<>(use));
    Collection<Node> uses = maybeReachingVariableUse.getUses("A", definitionNode);
    assertEquals(definitionNode, uses.iterator().next());
  }

  @org.junit.Test
  public void testRemoveFromUseIfLocalMethod() {
    Node n = Node.newName("A");
    ReachingUses use = new ReachingUses();
    use.mayUseMap.put(new Var("A", MOCK_JS_SCOPE), n);
    maybeReachingVariableUse.removeFromUseIfLocal("A", use);
    assertEquals(Sets.newHashSet(), use.mayUseMap.get(new Var("A", MOCK_JS_SCOPE)));
  }

  @org.junit.Test
  public void testAddToUseIfLocalMethod() {
    Node n = Node.newName("A");
    ReachingUses use = new ReachingUses();
    maybeReachingVariableUse.addToUseIfLocal("A", n, use);
    assertEquals(Sets.newHashSet(n), use.mayUseMap.get(new Var("A", MOCK_JS_SCOPE)));
  }
}