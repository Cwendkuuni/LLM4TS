import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.Scope.Var;
import java.util.Set;

public class LiveVariablesAnalysisTest {

  @Test
  public void testGetVarIndex() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    assertEquals(3, analysis.getVarIndex("var3"));
  }

  @Test
  public void testIsLive() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
    lattice.liveSet.set(2);
    assertTrue(lattice.isLive(2));
    assertFalse(lattice.isLive(1));
  }

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
    assertEquals(0, lattice.liveSet.cardinality());
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
    assertEquals(0, lattice.liveSet.cardinality());
  }

  @Test
  public void testFlowThrough() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice inputLattice = new LiveVariablesAnalysis.LiveVariableLattice(3);
    inputLattice.liveSet.set(0);
    inputLattice.liveSet.set(1);
    LiveVariablesAnalysis.LiveVariableLattice resultLattice = analysis.flowThrough(null, inputLattice);
    assertTrue(resultLattice.isLive(0));
    assertTrue(resultLattice.isLive(1));
    assertFalse(resultLattice.isLive(2));
  }

  @Test
  public void testGetEscapedLocals() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    analysis.getEscapedLocals().add(new Var("var1", 0, null));
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(1, escapedLocals.size());
    Var escapedVar = escapedLocals.iterator().next();
    assertEquals("var1", escapedVar.name);
    assertEquals(0, escapedVar.index);
  }

  @Test
  public void testComputeGenKill() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(3);
    lattice.liveSet.set(0);
    lattice.liveSet.set(1);
    Node node = new Node(Token.ADD);
    analysis.computeGenKill(node, lattice.liveSet, lattice.liveSet, false);
    assertFalse(lattice.isLive(0));
    assertFalse(lattice.isLive(1));
  }

  @Test
  public void testMarkAllParametersEscaped() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    analysis.markAllParametersEscaped();
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(2, escapedLocals.size());
    boolean var0Escaped = false;
    boolean var1Escaped = false;
    for (Var escapedVar : escapedLocals) {
      if (escapedVar.index == 0) {
        var0Escaped = true;
      } else if (escapedVar.index == 1) {
        var1Escaped = true;
      }
    }
    assertTrue(var0Escaped);
    assertTrue(var1Escaped);
  }

  @Test
  public void testIsArgumentsName() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node nameNode = new Node(Token.NAME, "arguments");
    assertTrue(analysis.isArgumentsName(nameNode));
    nameNode.setString("otherName");
    assertFalse(analysis.isArgumentsName(nameNode));
  }

}