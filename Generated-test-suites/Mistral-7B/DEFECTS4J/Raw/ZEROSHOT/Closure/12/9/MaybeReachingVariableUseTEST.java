import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.HashMultimap;
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

  @org.junit.Before
  public void setup() {
    // Setup mocks
    Scope jsScope = mock(Scope.class);
    AbstractCompiler compiler = mock(AbstractCompiler.class);

    ControlFlowGraph<Node> cfg = mock(ControlFlowGraph.class);

    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
  }

  @org.junit.Test
  public void testCreateEntryLattice() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    ReachingUses reachingUses = maybeReachingVariableUse.createEntryLattice();
    assertNotNull(reachingUses);
    assertEquals(Sets.newHashSet(), reachingUses.mayUseMap.keys());
  }

  @org.junit.Test
  public void testCreateInitialEstimateLattice() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    ReachingUses reachingUses = maybeReachingVariableUse.createInitialEstimateLattice();
    assertNotNull(reachingUses);
    assertEquals(Sets.newHashSet(), reachingUses.mayUseMap.keys());
  }

  @org.junit.Test
  public void testFlowThroughSimpleNode() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    ReachingUses input = new ReachingUses();
    ReachingUses output = maybeReachingVariableUse.flowThrough(Node.createIntLiteral(1), input);
    assertEquals(input, output);
  }

  @org.junit.Test
  public void testFlowThroughVariableAssignment() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    ReachingUses input = new ReachingUses();
    input.mayUseMap.put(new Var("x", null), Node.createIntLiteral(1));

    Node variable = Node.createName("x");
    Node assignment = Node.createAssign(variable, Node.createIntLiteral(2));
    ReachingUses output = maybeReachingVariableUse.flowThrough(assignment, input);

    Multimap<Var, Node> expectedUses = HashMultimap.create();
    expectedUses.put(new Var("x", null), Node.createIntLiteral(2));
    assertEquals(expectedUses, output.mayUseMap);
  }

  @org.junit.Test
  public void testFlowThroughVariableAssignmentWithConditional() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    ReachingUses input = new ReachingUses();
    input.mayUseMap.put(new Var("x", null), Node.createIntLiteral(1));

    Node variable = Node.createName("x");
    Node condition = Node.createBooleanLiteral(true);
    Node assignment = Node.createConditionalExpression(condition, Node.createAssign(variable, Node.createIntLiteral(2)), Node.createIntLiteral(1));
    ReachingUses output = maybeReachingVariableUse.flowThrough(assignment, input);

    Multimap<Var, Node> expectedUses = HashMultimap.create();
    expectedUses.put(new Var("x", null), Node.createIntLiteral(2));
    assertEquals(expectedUsuses, output.mayUseMap);
  }

  @org.junit.Test
  public void testFlowThroughSimpleConditional() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    ReachingUses input = new ReachingUses();
    ReachingUses output = maybeReachingVariableUse.flowThrough(Node.createBooleanLiteral(true), input);
    assertEquals(input, output);
  }

  @org.junit.Test
  public void testFlowThroughSimpleLoop() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    ReachingUses input = new ReachingUses();
    Node loop = Node.createWhileLoop(Node.createBooleanLiteral(true), Node.createEmptyStatement(), Node.createEmptyStatement());
    ReachingUses output = maybeReachingVariableUse.flowThrough(loop, input);
    assertEquals(input, output);
  }

  @org.junit.Test
  public void testFlowThroughComplexLoop() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    ReachingUses input = new ReachingUses();
    Node loop = Node.createWhileLoop(Node.createBooleanLiteral(true), Node.createEmptyStatement(), Node.createAssign(Node.createName("x"), Node.createIntLiteral(1)));
    ReachingUses output = maybeReachingVariableUse.flowThrough(loop, input);
    Multimap<Var, Node> expectedUses = HashMultimap.create();
    expectedUses.put(new Var("x", null), Node.createAssign(Node.createName("x"), Node.createIntLiteral(1)));
    assertEquals(expectedUses, output.mayUseMap);
  }

  @org.junit.Test
  public void testFlowThroughFunctionCall() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    ReachingUses input = new ReachingUses();
    Node functionCall = Node.createCall(Node.createName("testFunction"), Node.createList());
    ReachingUses output = maybeReachingVariableUse.flowThrough(functionCall, input);
    assertEquals(input, output);
  }

  @org.junit.Test
  public void testFlowThroughFunctionCallWithConditional() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    ReachingUses input = new ReachingUses();
    Node condition = Node.createBooleanLiteral(true);
    Node functionCall = Node.createConditionalExpression(condition, Node.createCall(Node.createName("testFunction"), Node.createList()), Node.createEmptyStatement());
    ReachingUses output = maybeReachingVariableUse.flowThrough(functionCall, input);
    assertEquals(input, output);
  }

  @org.junit.Test
  public void testFlowThroughForInLoop() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    ReachingUses input = new ReachingUses();
    Node forInLoop = Node.createForInStatement(Node.createName("x"), Node.createName("arr"), Node.createEmptyStatement(), Node.createEmptyStatement());
    ReachingUses output = maybeReachingVariableUse.flowThrough(forInLoop, input);
    assertEquals(input, output);
  }

  @org.junit.Test
  public void testFlowThroughForInLoopWithAssignment() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    ReachingUses input = new ReachingUses();
    Node forInLoop = Node.createForInStatement(Node.createName("x"), Node.createName("arr"), Node.createAssign(Node.createName("x"), Node.createName("x")), Node.createEmptyStatement());
    ReachingUses output = maybeReachingVariableUse.flowThrough(forInLoop, input);
    Multimap<Var, Node> expectedUses = HashMultimap.create();
    expectedUses.put(new Var("x", null), Node.createName("arr"));
    assertEquals(expectedUses, output.mayUseMap);
  }

  @org.junit.Test
  public void testJoinOperation() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    ReachingUses a = new ReachingUses();
    ReachingUses b = new ReachingUses();
    ReachingUses result = maybeReachingVariableUse.join(List.of(a, b));
    assertEquals(a, result);
  }

  @org.junit.Test
  public void testGetUses() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    Node defNode = Node.createName("x");
    ReachingUses reachingUses = new ReachingUses();
    reachingUses.mayUseMap.put(new Var("x", null), defNode);
    when(maybeReachingVariableUse.cfg.getNode(defNode)).thenReturn(GraphNode.createAnnotated(defNode, reachingUses));

    Collection<Node> uses = maybeReachingVariableUse.getUses("x", defNode);
    assertEquals(Sets.newHashSet(defNode), Sets.difference(Sets.newHashSet(uses), Sets.newHashSet()));
  }
}