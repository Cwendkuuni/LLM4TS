import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LiveVariablesAnalysisTest {

  private LiveVariablesAnalysis liveVariablesAnalysis;

  @Before
  public void setUp() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
  }

  @Test
  public void testGetEscapedLocals() {
    assertTrue(liveVariablesAnalysis.getEscapedLocals().isEmpty());
  }

  @Test
  public void testGetVarIndex() {
    assertEquals(0, liveVariablesAnalysis.getVarIndex("var1"));
    assertEquals(1, liveVariablesAnalysis.getVarIndex("var2"));
    assertEquals(2, liveVariablesAnalysis.getVarIndex("var3"));
  }

  @Test
  public void testIsForward() {
    assertFalse(liveVariablesAnalysis.isForward());
  }

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis.LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    assertEquals(3, entryLattice.liveSet.size());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis.LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    assertEquals(3, initialEstimateLattice.liveSet.size());
  }

  @Test
  public void testFlowThrough() {
    Node node = new Node(Token.SCRIPT);
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(3);
    LiveVariablesAnalysis.LiveVariableLattice result = liveVariablesAnalysis.flowThrough(node, input);
    assertEquals(input.liveSet, result.liveSet);
  }

  @Test
  public void testComputeGenKill() {
    BitSet gen = new BitSet();
    BitSet kill = new BitSet();
    boolean conditional = false;
    Node n = new Node(Token.EXPR_RESULT);
    liveVariablesAnalysis.computeGenKill(n, gen, kill, conditional);
    assertTrue(gen.isEmpty());
    assertTrue(kill.isEmpty());
  }

  @Test
  public void testAddToSetIfLocal() {
    BitSet set = new BitSet();
    Node node = new Node(Token.NAME, "var1");
    liveVariablesAnalysis.addToSetIfLocal(node, set);
    assertTrue(set.isEmpty());
  }

  @Test
  public void testMarkAllParametersEscaped() {
    liveVariablesAnalysis.markAllParametersEscaped();
    assertFalse(liveVariablesAnalysis.getEscapedLocals().isEmpty());
  }

  @Test
  public void testIsArgumentsName() {
    Node node1 = new Node(Token.NAME, "arguments");
    Node node2 = new Node(Token.NAME, "var");
    assertTrue(liveVariablesAnalysis.isArgumentsName(node1));
    assertFalse(liveVariablesAnalysis.isArgumentsName(node2));
  }
}