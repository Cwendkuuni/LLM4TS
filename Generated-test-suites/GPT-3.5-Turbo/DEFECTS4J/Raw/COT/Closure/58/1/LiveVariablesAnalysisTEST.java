
import org.junit.Assert;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testIsLive() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
    Assert.assertFalse(lattice.isLive(0));
  }

  @Test
  public void testIsLiveWithVar() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
    lattice.liveSet.set(0);
    Assert.assertTrue(lattice.isLive(0));
  }

  @Test
  public void testIsLiveWithVarObject() {
    LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(10);
    LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(10);
    lattice1.liveSet.set(0);
    Assert.assertTrue(lattice1.isLive(0));
    Assert.assertFalse(lattice2.isLive(0));
  }

  @Test
  public void testGetEscapedLocals() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    Set<Var> escaped = analysis.getEscapedLocals();
    Assert.assertNotNull(escaped);
    Assert.assertEquals(0, escaped.size());
  }

  @Test
  public void testGetVarIndex() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    int index = analysis.getVarIndex("varName");
    Assert.assertEquals(1, index);
  }

  @Test
  public void testIsForward() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    Assert.assertFalse(analysis.isForward());
  }

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
    Assert.assertNotNull(lattice);
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
    Assert.assertNotNull(lattice);
  }

  @Test
  public void testFlowThrough() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(10);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
    Assert.assertNotNull(result);
  }

  @Test
  public void testComputeGenKill() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    BitSet gen = new BitSet(10);
    BitSet kill = new BitSet(10);
    boolean conditional = false;
    analysis.computeGenKill(node, gen, kill, conditional);
    Assert.assertNotNull(gen);
    Assert.assertNotNull(kill);
  }

  @Test
  public void testAddToSetIfLocal() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(10);
    Node node = new Node(Token.NAME, "varName");
    analysis.addToSetIfLocal(node, lattice.liveSet);
    Assert.assertTrue(lattice.isLive(0));
  }

  @Test
  public void testMarkAllParametersEscaped() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    analysis.markAllParametersEscaped();
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    Assert.assertNotNull(escapedLocals);
    Assert.assertEquals(10, escapedLocals.size());
  }

  @Test
  public void testIsArgumentsName() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    Node argumentsNode = new Node(Token.NAME, "arguments");
    boolean isArgumentsName = analysis.isArgumentsName(argumentsNode);
    Assert.assertTrue(isArgumentsName);
  }
}

