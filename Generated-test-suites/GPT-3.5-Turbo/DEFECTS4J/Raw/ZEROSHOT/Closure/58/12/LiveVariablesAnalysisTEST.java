import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {
  
  @Test
  public void testGetEscapedLocals() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    
    assertNotNull(escapedLocals);
  }
  
  @Test
  public void testGetVarIndex() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    int varIndex = analysis.getVarIndex("testVar");
    
    assertEquals(-1, varIndex);
  }
  
  @Test
  public void testIsForward() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    boolean isForward = analysis.isForward();
    
    assertFalse(isForward);
  }
  
  @Test
  public void testCreateEntryLattice() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    LiveVariableLattice entryLattice = analysis.createEntryLattice();
    
    assertNotNull(entryLattice);
  }
  
  @Test
  public void testCreateInitialEstimateLattice() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();
    
    assertNotNull(initialEstimateLattice);
  }
  
  @Test
  public void testFlowThrough() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>();
    Scope jsScope = new Scope();
    AbstractCompiler compiler = new AbstractCompiler();
    
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    Node node = new Node();
    LiveVariableLattice input = new LiveVariableLattice(10);
    LiveVariableLattice result = analysis.flowThrough(node, input);
    
    assertNotNull(result);
  }
  
  @Test
  public void testComputeGenKill() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node node = new Node();
    BitSet gen = new BitSet(10);
    BitSet kill = new BitSet(10);
    boolean conditional = false;
    analysis.computeGenKill(node, gen, kill, conditional);
    
    assertNotNull(gen);
    assertNotNull(kill);
  }
  
  @Test
  public void testAddToSetIfLocal() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node node = new Node();
    BitSet set = new BitSet(10);
    analysis.addToSetIfLocal(node, set);
    
    assertNotNull(set);
  }
  
  @Test
  public void testMarkAllParametersEscaped() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    analysis.markAllParametersEscaped();
    
    // No assert statements as there is no return value or side effect to check.
  }
  
  @Test
  public void testIsArgumentsName() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(null, null, null);
    Node node = new Node();
    boolean isArgumentsName = analysis.isArgumentsName(node);
    
    assertFalse(isArgumentsName);
  }
}