import static org.junit.Assert.*;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testGetEscapedLocals() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    assertTrue(analysis.getEscapedLocals().isEmpty());
  }

  @Test
  public void testGetVarIndex() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    assertEquals(0, analysis.getVarIndex("var1"));
    assertEquals(1, analysis.getVarIndex("var2"));
    assertEquals(2, analysis.getVarIndex("var3"));
  }

  @Test
  public void testIsForward() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    assertFalse(analysis.isForward());
  }

  @Test
  public void testCreateEntryLattice() {
    Scope jsScope = new Scope();
    jsScope.addVar(new Var(0, "var1"));
    jsScope.addVar(new Var(1, "var2"));
    jsScope.addVar(new Var(2, "var3"));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, jsScope, null);
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
    assertFalse(lattice.isLive(0));
    assertFalse(lattice.isLive(1));
    assertFalse(lattice.isLive(2));
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    Scope jsScope = new Scope();
    jsScope.addVar(new Var(0, "var1"));
    jsScope.addVar(new Var(1, "var2"));
    jsScope.addVar(new Var(2, "var3"));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, jsScope, null);
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
    assertFalse(lattice.isLive(0));
    assertFalse(lattice.isLive(1));
    assertFalse(lattice.isLive(2));
  }

  @Test
  public void testFlowThrough() {
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(3);
    input.liveSet.set(0);
    input.liveSet.set(1);
    assertFalse(input.isLive(2));
    Node node = new Node(Token.NAME, new Node(Token.NAME, new Node(Token.NAME)));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
    assertTrue(result.isLive(0));
    assertTrue(result.isLive(1));
    assertFalse(result.isLive(2));
  }

  @Test
  public void testComputeGenKill() {
    Node n = new Node(Token.NAME);
    BitSet gen = new BitSet(3);
    BitSet kill = new BitSet(3);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    analysis.computeGenKill(n, gen, kill, true);
    assertFalse(gen.get(0));
    assertFalse(gen.get(1));
    assertFalse(gen.get(2));
    assertFalse(kill.get(0));
    assertFalse(kill.get(1));
    assertFalse(kill.get(2));
  }

  @Test
  public void testAddToSetIfLocal() {
    Scope jsScope = new Scope();
    jsScope.addVar(new Var(0, "var1"));
    jsScope.addVar(new Var(1, "var2"));
    jsScope.addVar(new Var(2, "var3"));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, jsScope, null);
    Node node = new Node(Token.NAME);
    BitSet set = new BitSet(3);
    analysis.addToSetIfLocal(node, set);
    assertFalse(set.get(0));
    assertFalse(set.get(1));
    assertFalse(set.get(2));
    node = new Node(Token.NAME, "var1");
    set = new BitSet(3);
    analysis.addToSetIfLocal(node, set);
    assertTrue(set.get(0));
    assertFalse(set.get(1));
    assertFalse(set.get(2));
  }

  @Test
  public void testMarkAllParametersEscaped() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    analysis.markAllParametersEscaped();
    assertTrue(analysis.getEscapedLocals().isEmpty());
  }

  @Test
  public void testIsArgumentsName() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node node = new Node(Token.NAME, "arg");
    assertFalse(analysis.isArgumentsName(node));
    node = new Node(Token.NAME, "arguments");
    assertTrue(analysis.isArgumentsName(node));
  }

}