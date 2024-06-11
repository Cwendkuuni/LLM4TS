import static org.junit.Assert.*;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  @Test
  public void testBasicFunctionality() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    // Add your assertions here for testing the basic functionality of each method
  }

  @Test
  public void testReachingUsesJoinOp() {
    ReachingUses a = new ReachingUses();
    ReachingUses b = new ReachingUses();

    a.mayUseMap.put(Var.create("A"), Node.createIntLiteral(1));
    b.mayUseMap.put(Var.create("A"), Node.createIntLiteral(2));
    b.mayUseMap.put(Var.create("B"), Node.createIntLiteral(3));

    ReachingUses result = new ReachingUsesJoinOp().apply(Sets.newArrayList(a, b));

    assertEquals(a.mayUseMap, result.mayUseMap);
    assertEquals(b.mayUseMap, result.mayUseMap);
    assertEquals(2, result.mayUseMap.size());
  }

  @Test(expected = AssertionError.class)
  public void testFlowThroughNullNode() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    ReachingUses input = new ReachingUses();
    maybeReachingVariableUse.flowThrough(null, input);
  }

  @Test
  public void testFlowThroughEmptyMethods() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    Node nEmptyBlock = Node.createBlock();
    Node nEmptyFunction = Node.createFunction("", null, null, null);

    ReachingUses input = new ReachingUses();

    ReachingUses output1 = maybeReachingVariableUse.flowThrough(nEmptyBlock, input);
    ReachingUsages output2 = maybeReachingVariableUse.flowThrough(nEmptyFunction, input);

    assertEquals(input, output1);
    assertEquals(input, output2);
  }

  @Test
  public void testFlowThroughName() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    Node nA = Node.createName("A");
    Node nCfgNode = Node.createBlock();

    ReachingUses input = new ReachingUses();

    maybeReachingVariableUse.flowThrough(nA, nCfgNode, input);

    Multimap<Var, Node> expectedUses = HashMultimap.create();
    expectedUses.put(Var.create("A"), nCfgNode);

    assertEquals(expectedUses, input.mayUseMap);
  }

  @Test
  public void testFlowThroughWhile() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    Node nWhile = Node.createWhile();
    Node nCfgNode = Node.createBlock();
    Node nCondition = Node.createBoolLiteral(true);

    ReachingUses input = new ReachingUses();

    maybeReachingVariableUse.flowThrough(nCondition, nCfgNode, input);
    maybeReachingVariableUse.flowThrough(nWhile, nCfgNode, input);

    Multimap<Var, Node> expectedUses = HashMultimap.create();
    assertEquals(expectedUses, input.mayUseMap);
  }

  @Test
  public void testFlowThroughIf() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    Node nIf = Node.createIf();
    Node nCfgNode = Node.createBlock();
    Node nCondition = Node.createBoolLiteral(true);

    ReachingUses input = new ReachingUses();

    maybeReachingVariableUse.flowThrough(nCondition, nCfgNode, input);
    maybeReachingVariableUse.flowThrough(nIf, nCfgNode, input);

    Multimap<Var, Node> expectedUses = HashMultimap.create();
    assertEquals(expectedUses, input.mayUseMap);
  }

  @Test
  public void testFlowThroughFor() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    Node nFor = Node.createFor();
    Node nCfgNode = Node.createBlock();
    Node nCondition = Node.createBoolLiteral(true);
    Node nInitializer = Node.createName("i").set("value", Node.createIntLiteral(0));
    Node nUpdate = Node.createAdd(Node.createName("i"), Node.createIntLiteral(1));
    Node nLoop = Node.createName("condition");

    ReachingUses input = new ReachingUses();

    maybeReachingVariableUse.flowThrough(nCondition, nCfgNode, input);
    maybeReachingVariableUse.flowThrough(nInitializer, nCfgNode, input);
    maybeReachingVariableUse.flowThrough(nFor, nCfgNode, input);

    Multimap<Var, Node> expectedUses = HashMultimap.create();
    assertEquals(expectedUses, input.mayUseMap);
  }

  @Test
  public void testFlowThroughAnd() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    Node nAnd = Node.createBinaryOp("&&", Node.createBoolLiteral(true), Node.createBoolLiteral(true));

    ReachingUses input = new ReachingUses();

    maybeReachingVariableUse.flowThrough(nAnd.getLastChild(), null, input);
    maybeReachingVariableUse.flowThrough(nAnd.getFirstChild(), null, input);
    ReachingUses output = maybeReachingVariableUse.flowThrough(nAnd, null, input);

    Multimap<Var, Node> expectedUses = HashMultimap.create();
    assertEquals(expectedUses, output.mayUseMap);
  }

  @Test
  public void testFlowThroughHook() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    Node nHook = Node.createCall(Node.createName("__defineGetter__"), Node.createName("getA"), null, null);

    ReachingUses input = new ReachingUses();

    maybeReachingVariableUse.flowThrough(nHook.getLastChild(), null, input);
    maybeReachingVariableUse.flowThrough(nHook.getFirstChild().getNext(), null, input);
    maybeReachingVariableUse.flowThrough(nHook.getFirstChild(), null, input);
    ReachingUses output = maybeReachingVariableUse.flowThrough(nHook, null, input);

    Multimap<Var, Node> expectedUses = HashMultimap.create();
    assertEquals(expectedUses, output.mayUseMap);
  }

  @Test
  public void testGetUses() {
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(null, null, null);
    Node nDefNode = Node.createName("A").set("value", Node.createIntLiteral(1));
    Scope jsScope = new Scope();

    Collection<Node> uses = maybeReachingVariableUse.getUses("A", nDefNode);
    assertTrue(uses.isEmpty());

    GraphNode<Node, Branch> nCfgNode = getCfg().getNode(nDefNode);
    FlowState<ReachingUses> state = nCfgNode.getAnnotation();
    ReachingUses output = state.getOut();

    assertEquals(Sets.newHashSet(nDefNode), Sets.difference(Sets.newHashSet(state.getIn().getInput().getSource()), Sets.newHashSet(output.mayUseMap.keySet())));

    Uses uses2 = maybeReachingVariableUse.getUses("A", nDefNode);
    assertEquals(Sets.newHashSet(nDefNode), Sets.newHashSet(uses2));
  }
}