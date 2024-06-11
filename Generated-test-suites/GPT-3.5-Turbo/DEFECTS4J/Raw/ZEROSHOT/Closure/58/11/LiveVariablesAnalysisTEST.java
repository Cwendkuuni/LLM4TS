import static org.junit.Assert.*;

import java.util.BitSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphEdge;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class LiveVariablesAnalysisTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private LiveVariablesAnalysis analysis;

  @Before
  public void setup() {
    cfg = new ControlFlowGraph<>();
    jsScope = new Scope(null, null);
    analysis = new LiveVariablesAnalysis(cfg, jsScope, null);
  }

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
    assertNotNull(lattice);
    assertEquals(0, lattice.liveSet.length());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
    assertNotNull(lattice);
    assertEquals(0, lattice.liveSet.length());
  }

  @Test
  public void testFlowThrough() {
    Node node = new Node(Token.NAME, "test");
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(1);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);

    assertNotNull(result);
  }

  @Test
  public void testComputeGenKillWithArguments() {
    Node node = new Node(Token.NAME, "arguments");
    BitSet gen = new BitSet();
    BitSet kill = new BitSet();
    boolean conditional = false;

    analysis.computeGenKill(node, gen, kill, conditional);

    assertTrue(analysis.getEscapedLocals().isEmpty());
    assertEquals(0, gen.length());
    assertEquals(0, kill.length());
  }

  @Test
  public void testComputeGenKillWithLocalVariable() {
    Node node = new Node(Token.NAME, "test");
    BitSet gen = new BitSet();
    BitSet kill = new BitSet();
    boolean conditional = false;

    analysis.computeGenKill(node, gen, kill, conditional);

    assertTrue(analysis.getEscapedLocals().isEmpty());
    assertEquals(1, gen.length());
    assertEquals(1, kill.length());
  }

  @Test
  public void testComputeGenKillWithConditionalAssignment() {
    Node node = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "test");
    Node rhs = new Node();
    node.addChildToBack(lhs);
    node.addChildToBack(rhs);
    BitSet gen = new BitSet();
    BitSet kill = new BitSet();
    boolean conditional = true;

    analysis.computeGenKill(node, gen, kill, conditional);

    assertTrue(analysis.getEscapedLocals().isEmpty());
    assertEquals(1, gen.length());
    assertEquals(0, kill.length());
  }

  @Test
  public void testComputeGenKillWithRegularAssignment() {
    Node node = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME, "test");
    Node rhs = new Node();
    node.addChildToBack(lhs);
    node.addChildToBack(rhs);
    BitSet gen = new BitSet();
    BitSet kill = new BitSet();
    boolean conditional = false;

    analysis.computeGenKill(node, gen, kill, conditional);

    assertTrue(analysis.getEscapedLocals().isEmpty());
    assertEquals(1, gen.length());
    assertEquals(1, kill.length());
  }

  @Test
  public void testComputeGenKillWithNestedNodes() {
    Node node = new Node(Token.SCRIPT);
    Node var1 = new Node(Token.VAR);
    Node varName1 = new Node(Token.NAME, "test1");
    Node assign1 = new Node(Token.ASSIGN);
    Node lhs1 = new Node(Token.NAME, "test1");
    Node rhs1 = new Node();
    Node ifStmt = new Node(Token.IF);
    Node condition = new Node(Token.NAME, "test2");
    Node var2 = new Node(Token.VAR);
    Node varName2 = new Node(Token.NAME, "test2");
    Node assign2 = new Node(Token.ASSIGN);
    Node lhs2 = new Node(Token.NAME, "test2");
    Node rhs2 = new Node();
    node.addChildToBack(var1);
    var1.addChildToBack(varName1);
    var1.addChildToBack(assign1);
    assign1.addChildToBack(lhs1);
    assign1.addChildToBack(rhs1);
    node.addChildToBack(ifStmt);
    ifStmt.addChildToBack(condition);
    ifStmt.addChildToBack(var2);
    var2.addChildToBack(varName2);
    var2.addChildToBack(assign2);
    assign2.addChildToBack(lhs2);
    assign2.addChildToBack(rhs2);

    BitSet gen = new BitSet();
    BitSet kill = new BitSet();
    boolean conditional = false;

    analysis.computeGenKill(node, gen, kill, conditional);

    assertTrue(analysis.getEscapedLocals().isEmpty());
    assertEquals(2, gen.length());
    assertEquals(2, kill.length());
  }

  @Test
  public void testMarkAllParametersEscaped() {
    analysis.markAllParametersEscaped();

    assertFalse(analysis.getEscapedLocals().isEmpty());
  }

  @Test
  public void testIsArgumentsNameWithMatchingName() {
    Node node = new Node(Token.NAME, "arguments");

    boolean result = analysis.isArgumentsName(node);

    assertTrue(result);
  }

  @Test
  public void testIsArgumentsNameWithNonMatchingName() {
    Node node = new Node(Token.NAME, "test");

    boolean result = analysis.isArgumentsName(node);

    assertFalse(result);
  }

  @Test
  public void testIsArgumentsNameWithDeclaredName() {
    Node node = new Node(Token.NAME, "arguments");
    Scope scope = new Scope(null, null);
    scope.declare("arguments", null, null, false);

    boolean result = analysis.isArgumentsName(node);

    assertFalse(result);
  }
}