import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import java.util.BitSet;

public class LiveVariablesAnalysisTest {
  
  private LiveVariablesAnalysis analysis;
  private ControlFlowGraph<Node> cfg;
  private Node jsScope;
  private AbstractCompiler compiler;
  
  @Before
  public void setUp() {
    cfg = new ControlFlowGraph<>();
    jsScope = new Node();
    compiler = new AbstractCompiler();
    analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
  }
  
  @Test
  public void testGetEscapedLocals() {
    assertNotNull(analysis.getEscapedLocals());
  }
  
  @Test
  public void testGetVarIndex() {
    int index = analysis.getVarIndex("var");
    assertEquals(0, index);
  }
  
  @Test
  public void testIsForward() {
    assertFalse(analysis.isForward());
  }
  
  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.createEntryLattice();
    assertNotNull(result);
  }
  
  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.createInitialEstimateLattice();
    assertNotNull(result);
  }
  
  @Test
  public void testFlowThrough() {
    Node node = new Node();
    LiveVariablesAnalysis.LiveVariableLattice input = analysis.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
    assertNotNull(result);
  }
  
  @Test
  public void testComputeGenKill() {
    Node node = new Node();
    BitSet gen = new BitSet();
    BitSet kill = new BitSet();
    boolean conditional = false;
    analysis.computeGenKill(node, gen, kill, conditional);
  }
  
  @Test
  public void testAddToSetIfLocal() {
    Node node = new Node(Token.NAME, "var");
    BitSet set = new BitSet();
    analysis.addToSetIfLocal(node, set);
    assertTrue(set.get(0));
  }
  
  @Test
  public void testMarkAllParametersEscaped() {
    analysis.markAllParametersEscaped();
    assertTrue(analysis.getEscapedLocals().contains(jsScope.getVar(ARGUMENT_ARRAY_ALIAS)));
  }
  
  @Test
  public void testIsArgumentsName() {
    Node n = new Node(Token.NAME, ARGUMENT_ARRAY_ALIAS);
    boolean isArgumentsName = analysis.isArgumentsName(n);
    assertTrue(isArgumentsName);
  }
}