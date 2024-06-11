import static org.junit.Assert.*;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
    for (int i = 0; i < 5; i++) {
      assertFalse(lattice.isLive(i));
    }
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(5);
    for (int i = 0; i < 5; i++) {
      assertFalse(lattice.isLive(i));
    }
  }

  @Test
  public void testFlowThrough() {
    ControlFlowGraph<Node> cfg = createCFG();
    Scope jsScope = createScope();
    AbstractCompiler compiler = createCompiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(5);
    input.liveSet.set(0);
    input.liveSet.set(2);
    input.liveSet.set(4);

    Node node = cfg.getEntry().getValue();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);

    assertFalse(result.isLive(0));
    assertTrue(result.isLive(1));
    assertFalse(result.isLive(2));
    assertFalse(result.isLive(3));
    assertFalse(result.isLive(4));
  }

  @Test
  public void testGetVarIndex() {
    ControlFlowGraph<Node> cfg = createCFG();
    Scope jsScope = createScope();
    AbstractCompiler compiler = createCompiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    int index = analysis.getVarIndex("x");
    assertEquals(0, index);
  }

  @Test
  public void testGetEscapedLocals() {
    ControlFlowGraph<Node> cfg = createCFG();
    Scope jsScope = createScope();
    AbstractCompiler compiler = createCompiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Set<Scope.Var> escaped = analysis.getEscapedLocals();
    assertTrue(escaped.isEmpty());
  }

  private ControlFlowGraph<Node> createCFG() {
    // TODO: implement your own logic to create a ControlFlowGraph from the source code
    return null;
  }

  private Scope createScope() {
    // TODO: implement your own logic to create a Scope from the source code
    return null;
  }

  private AbstractCompiler createCompiler() {
    // TODO: implement your own logic to create an AbstractCompiler
    return null;
  }
}