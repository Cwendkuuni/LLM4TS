import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LiveVariablesAnalysisTest {

  private LiveVariablesAnalysis liveVariablesAnalysis;
  private ControlFlowGraph<Node> cfg;
  private Scope scope;

  @Before
  public void setUp() {
    // Create a ControlFlowGraph
    cfg = createCFG();

    // Create a Scope
    scope = createScope();

    // Create a LiveVariablesAnalysis object
    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, scope, compiler);
  }

  private ControlFlowGraph<Node> createCFG() {
    // Create a ControlFlowGraph with some nodes and edges
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();

    Node node1 = new Node(Token.SCRIPT);
    Node node2 = new Node(Token.BLOCK);
    Node node3 = new Node(Token.WHILE);
    Node node4 = new Node(Token.IF);

    cfg.createDirectedEdge(node1, node2);
    cfg.createDirectedEdge(node2, node3);
    cfg.createDirectedEdge(node2, node4);

    return cfg;
  }

  private Scope createScope() {
    // Create a Scope with some variables
    Scope scope = new Scope();
    
    // Define some variables in the scope
    scope.createAndDeclareVariable("var1");
    scope.createAndDeclareVariable("var2");
    scope.createAndDeclareVariable("var3");

    return scope;
  }

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = liveVariablesAnalysis.createEntryLattice();
    
    // Check if the lattice has the correct number of variables
    assertEquals(scope.getVarCount(), lattice.liveSet.size());

    // Check if all variables are set to false (not live)
    for (int i = 0; i < scope.getVarCount(); i++) {
      assertFalse(lattice.isLive(i));
    }
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = liveVariablesAnalysis.createInitialEstimateLattice();
    
    // Check if the lattice has the correct number of variables
    assertEquals(scope.getVarCount(), lattice.liveSet.size());

    // Check if all variables are set to false (not live)
    for (int i = 0; i < scope.getVarCount(); i++) {
      assertFalse(lattice.isLive(i));
    }
  }

  @Test
  public void testFlowThrough() {
    Node node = new Node(Token.NAME, "var1");
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(scope.getVarCount());

    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(node, input);

    // Check if the variable "var1" is now live
    assertTrue(result.isLive(0));
  }

  @Test
  public void testGetEscapedLocals() {
    Set<Scope.Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    
    // Check if the set of escaped locals is empty
    assertTrue(escapedLocals.isEmpty());
  }

  @Test
  public void testGetVarIndex() {
    // Get the index of "var1"
    int var1Index = liveVariablesAnalysis.getVarIndex("var1");

    // Check if the index of "var1" is correct
    assertEquals(0, var1Index);
  }

  @Test
  public void testIsForward() {
    // Check if the analysis is not forward
    assertFalse(liveVariablesAnalysis.isForward());
  }

  @Test
  public void testJoinOp() {
    // Create a list of lattices
    LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(scope.getVarCount());
    LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(scope.getVarCount());
    lattice2.liveSet.set(0); // Set "var1" to live in lattice2

    List<LiveVariablesAnalysis.LiveVariableLattice> lattices = List.of(lattice1, lattice2);

    LiveVariablesAnalysis.LiveVariableLattice result = new LiveVariablesAnalysis.LiveVariableJoinOp().apply(lattices);

    // Check if "var1" is live in the result lattice
    assertTrue(result.isLive(0));
  }

  @Test
  public void testComputeGenKill() {
    Node node = new Node(Token.NAME, "var1");
    BitSet gen = new BitSet(scope.getVarCount());
    BitSet kill = new BitSet(scope.getVarCount());
    boolean conditional = false;

    liveVariablesAnalysis.computeGenKill(node, gen, kill, conditional);

    // Check if "var1" is in the gen set and not in the kill set
    assertTrue(gen.get(0));
    assertFalse(kill.get(0));
  }

  @Test
  public void testAddToSetIfLocal() {
    Node node = new Node(Token.NAME, "var1");
    BitSet set = new BitSet(scope.getVarCount());

    liveVariablesAnalysis.addToSetIfLocal(node, set);

    // Check if "var1" is in the set
    assertTrue(set.get(0));
  }

  @Test
  public void testMarkAllParametersEscaped() {
    liveVariablesAnalysis.markAllParametersEscaped();

    Set<Scope.Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    
    // Check if all parameters are marked as escaped
    for (int i = 0; i < scope.getVarCount(); i++) {
      assertTrue(escapedLocals.contains(scope.getVar(i)));
    }
  }

  @Test
  public void testIsArgumentsName() {
    Node node1 = new Node(Token.NAME, "arguments");
    Node node2 = new Node(Token.NAME, "var1");

    boolean result1 = liveVariablesAnalysis.isArgumentsName(node1);
    boolean result2 = liveVariablesAnalysis.isArgumentsName(node2);

    // Check if the first node is recognized as the "arguments" name
    assertTrue(result1);

    // Check if the second node is not recognized as the "arguments" name
    assertFalse(result2);
  }

  // Additional test cases for other methods

  @Test
  public void testGetEscapedLocalsWithEscapedVariables() {
    // Create a Set of escaped variables
    Set<Scope.Var> escapedLocals = Sets.newHashSet(scope.getVar(0), scope.getVar(1));

    // Set the escaped variables in the LiveVariablesAnalysis object
    liveVariablesAnalysis.setEscapedLocals(escapedLocals);

    // Get the escaped locals
    Set<Scope.Var> result = liveVariablesAnalysis.getEscapedLocals();

    // Check if the result is the same as the escapedLocals
    assertEquals(escapedLocals, result);
  }

  @Test
  public void testHashCode() {
    LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(scope.getVarCount());
    LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(lattice1);

    // Check if the hash codes of lattice1 and lattice2 are equal
    assertEquals(lattice1.hashCode(), lattice2.hashCode());
  }

  // More test cases can be added for other methods

}