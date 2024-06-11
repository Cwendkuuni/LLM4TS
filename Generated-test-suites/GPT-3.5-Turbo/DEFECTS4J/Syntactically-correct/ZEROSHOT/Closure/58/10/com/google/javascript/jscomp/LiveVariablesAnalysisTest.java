import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {
  
  private LiveVariablesAnalysis analyzer;
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    cfg = new ControlFlowGraph<>();
    jsScope = new Scope(null);
    compiler = new AbstractCompiler();
    analyzer = new LiveVariablesAnalysis(cfg, jsScope, compiler);
  }

  @Test
  public void testGetEscapedLocals() {
    assertNotNull(analyzer.getEscapedLocals());
  }

  @Test
  public void testGetVarIndex() {
    assertEquals(0, analyzer.getVarIndex("var1"));
    assertEquals(1, analyzer.getVarIndex("var2"));
  }

  @Test
  public void testIsForward() {
    assertFalse(analyzer.isForward());
  }

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = analyzer.createEntryLattice();
    assertNotNull(lattice);
    assertEquals(0, lattice.liveSet.cardinality());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = analyzer.createInitialEstimateLattice();
    assertNotNull(lattice);
    assertEquals(0, lattice.liveSet.cardinality());
  }

  @Test
  public void testFlowThrough() {
    Node node = new Node(Token.SCRIPT);
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(2);
    LiveVariablesAnalysis.LiveVariableLattice result = analyzer.flowThrough(node, input);
    assertNotNull(result);
    assertEquals(input, result);
  }

  @Test
  public void testComputeGenKill() {
    Node n = new Node(Token.BLOCK);
    BitSet gen = new BitSet(2);
    BitSet kill = new BitSet(2);
    boolean conditional = true;
    analyzer.computeGenKill(n, gen, kill, conditional);
    assertEquals(0, gen.cardinality());
    assertEquals(0, kill.cardinality());
  }

  @Test
  public void testAddToSetIfLocal() {
    Node node = new Node(Token.NAME, "var3");
    BitSet set = new BitSet(3);
    analyzer.addToSetIfLocal(node, set);
    assertEquals(0, set.cardinality());

    node = new Node(Token.NAME, "var1");
    analyzer.addToSetIfLocal(node, set);
    assertEquals(1, set.cardinality());
  }

  @Test
  public void testMarkAllParametersEscaped() {
    analyzer.markAllParametersEscaped();
    assertTrue(analyzer.getEscapedLocals().contains(jsScope.getVar("arg1")));
    assertTrue(analyzer.getEscapedLocals().contains(jsScope.getVar("arg2")));
  }

  @Test
  public void testIsArgumentsName() {
    Node node = new Node(Token.NAME, "arguments");
    assertTrue(analyzer.isArgumentsName(node));
  }
}