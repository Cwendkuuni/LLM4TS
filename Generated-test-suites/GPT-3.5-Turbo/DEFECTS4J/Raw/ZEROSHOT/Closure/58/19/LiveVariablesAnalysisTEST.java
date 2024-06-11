import static org.junit.Assert.*;
import org.junit.Test;

public class LiveVariablesAnalysisTest {
  
  @Test
  public void testIsLive() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
    assertFalse(lattice.isLive(0));
    assertFalse(lattice.isLive(1));
    assertFalse(lattice.isLive(2));
    assertFalse(lattice.isLive(3));
    assertFalse(lattice.isLive(4));
    lattice.liveSet.set(0);
    assertTrue(lattice.isLive(0));
    assertFalse(lattice.isLive(1));
    assertFalse(lattice.isLive(2));
    assertFalse(lattice.isLive(3));
    assertFalse(lattice.isLive(4));
    lattice.liveSet.set(1);
    assertTrue(lattice.isLive(0));
    assertTrue(lattice.isLive(1));
    assertFalse(lattice.isLive(2));
    assertFalse(lattice.isLive(3));
    assertFalse(lattice.isLive(4));
    lattice.liveSet.set(2);
    assertTrue(lattice.isLive(0));
    assertTrue(lattice.isLive(1));
    assertTrue(lattice.isLive(2));
    assertFalse(lattice.isLive(3));
    assertFalse(lattice.isLive(4));
    lattice.liveSet.clear();
    assertFalse(lattice.isLive(0));
    assertFalse(lattice.isLive(1));
    assertFalse(lattice.isLive(2));
    assertFalse(lattice.isLive(3));
    assertFalse(lattice.isLive(4));
  }
  
  @Test
  public void testEquals() {
    LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
    LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
    LiveVariablesAnalysis.LiveVariableLattice lattice3 = new LiveVariablesAnalysis.LiveVariableLattice(5);
    assertTrue(lattice1.equals(lattice2));
    assertFalse(lattice1.equals(lattice3));
    lattice1.liveSet.set(0);
    assertFalse(lattice1.equals(lattice2));
    assertFalse(lattice1.equals(lattice3));
    lattice2.liveSet.set(0);
    assertTrue(lattice1.equals(lattice2));
    assertFalse(lattice1.equals(lattice3));
    lattice2.liveSet.set(1);
    assertFalse(lattice1.equals(lattice2));
    assertFalse(lattice1.equals(lattice3));
    lattice3.liveSet.set(0);
    assertFalse(lattice1.equals(lattice2));
    assertFalse(lattice1.equals(lattice3));
    lattice3.liveSet.set(1);
    assertFalse(lattice1.equals(lattice2));
    assertTrue(lattice1.equals(lattice3));
  }
  
  @Test
  public void testToString() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
    assertEquals("{}", lattice.toString());
    lattice.liveSet.set(0);
    assertEquals("{0}", lattice.toString());
    lattice.liveSet.set(1);
    assertEquals("{0, 1}", lattice.toString());
    lattice.liveSet.set(2);
    assertEquals("{0, 1, 2}", lattice.toString());
    lattice.liveSet.clear();
    assertEquals("{}", lattice.toString());
  }
  
  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
    assertNotNull(lattice);
    assertEquals(0, lattice.liveSet.cardinality());
  }
  
  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
    assertNotNull(lattice);
    assertEquals(0, lattice.liveSet.cardinality());
  }
  
  @Test
  public void testIsForward() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    assertFalse(analysis.isForward());
  }
  
  @Test
  public void testGetEscapedLocals() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    assertNotNull(analysis.getEscapedLocals());
  }
  
  @Test
  public void testGetVarIndex() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    assertEquals(0, analysis.getVarIndex("var1"));
    assertEquals(1, analysis.getVarIndex("var2"));
    assertEquals(2, analysis.getVarIndex("var3"));
    assertEquals(3, analysis.getVarIndex("var4"));
    assertEquals(4, analysis.getVarIndex("var5"));
  }
  
  @Test
  public void testFlowThrough() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(5);
    LiveVariablesAnalysis.LiveVariableLattice result1 = analysis.flowThrough(null, lattice1);
    assertNotNull(result1);
    assertEquals(lattice1, result1);
    LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(5);
    lattice2.liveSet.set(0);
    lattice2.liveSet.set(1);
    lattice2.liveSet.set(2);
    lattice2.liveSet.set(3);
    lattice2.liveSet.set(4);
    LiveVariablesAnalysis.LiveVariableLattice result2 = analysis.flowThrough(null, lattice2);
    assertNotNull(result2);
    assertEquals(lattice2, result2);
  }
  
  @Test
  public void testComputeGenKill() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
    BitSet gen = new BitSet(5);
    BitSet kill = new BitSet(5);
    boolean conditional = false;
    analysis.computeGenKill(null, gen, kill, conditional);
    assertEquals(0, gen.cardinality());
    assertEquals(0, kill.cardinality());
    Node n1 = new Node(Token.NAME, "var1");
    Node n2 = new Node(Token.NAME, "var2");
    Node n3 = new Node(Token.NAME, "var3");
    n1.setNext(n2);
    n2.setNext(n3);
    analysis.computeGenKill(n1, gen, kill, conditional);
    assertEquals(3, gen.cardinality());
    assertTrue(gen.get(0));
    assertTrue(gen.get(1));
    assertTrue(gen.get(2));
    assertEquals(3, kill.cardinality());
    assertTrue(kill.get(0));
    assertTrue(kill.get(1));
    assertTrue(kill.get(2));
    conditional = true;
    analysis.computeGenKill(n1, gen, kill, conditional);
    assertEquals(3, gen.cardinality());
    assertTrue(gen.get(0));
    assertTrue(gen.get(1));
    assertTrue(gen.get(2));
    assertEquals(0, kill.cardinality());
  }
  
  @Test
  public void testAddToSetIfLocal() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
    Node node = new Node(Token.NAME, "var1");
    analysis.addToSetIfLocal(node, lattice.liveSet);
    assertFalse(lattice.isLive(0));
    assertFalse(lattice.isLive(1));
    assertFalse(lattice.isLive(2));
    assertFalse(lattice.isLive(3));
    assertFalse(lattice.isLive(4));
    Var var = new Var("var1", null, 0);
    analysis.jsScope.declare(var, null);
    analysis.addToSetIfLocal(node, lattice.liveSet);
    assertTrue(lattice.isLive(0));
    assertFalse(lattice.isLive(1));
    assertFalse(lattice.isLive(2));
    assertFalse(lattice.isLive(3));
    assertFalse(lattice.isLive(4));
  }
  
  @Test
  public void testMarkAllParametersEscaped() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    analysis.markAllParametersEscaped();
    assertEquals(0, analysis.getEscapedLocals().size());
  }
  
  @Test
  public void testIsArgumentsName() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node n1 = new Node(Token.NAME, "arguments");
    Node n2 = new Node(Token.NAME, "args");
    assertFalse(analysis.isArgumentsName(n1));
    assertTrue(analysis.isArgumentsName(n2));
    Var var = new Var("arguments", null, 1);
    analysis.jsScope.declare(var, null);
    assertFalse(analysis.isArgumentsName(n1));
    assertFalse(analysis.isArgumentsName(n2));
  }
}