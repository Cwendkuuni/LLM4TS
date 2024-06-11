import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

public class LiveVariablesAnalysisTest {
  
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private Set<Scope.Var> escaped;
  
  @Before
  public void setUp() {
    // Initialize the ControlFlowGraph and Scope
    cfg = new ControlFlowGraph<Node>();
    jsScope = new Scope();
    escaped = Sets.newHashSet();
  }
  
  @Test
  public void testFlowThrough() {
    // Create a test node
    Node testNode = new Node(Token.NAME, "test");
    
    // Create the LiveVariableAnalysis instance
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    
    // Create the input lattice
    LiveVariableLattice input = new LiveVariableLattice(jsScope.getVarCount());
    
    // Apply the flowThrough method
    LiveVariableLattice result = liveVariablesAnalysis.flowThrough(testNode, input);
    
    // Validate the result
    assertNotNull(result);
  }
  
  @Test
  public void testCreateEntryLattice() {
    // Create the LiveVariableAnalysis instance
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    
    // Create the entry lattice
    LiveVariableLattice entryLattice = liveVariablesAnalysis.createEntryLattice();
    
    // Validate the entry lattice
    assertNotNull(entryLattice);
    assertEquals(jsScope.getVarCount(), entryLattice.liveSet.size());
  }
  
  @Test
  public void testCreateInitialEstimateLattice() {
    // Create the LiveVariableAnalysis instance
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    
    // Create the initial estimate lattice
    LiveVariableLattice initialEstimateLattice = liveVariablesAnalysis.createInitialEstimateLattice();
    
    // Validate the initial estimate lattice
    assertNotNull(initialEstimateLattice);
    assertEquals(jsScope.getVarCount(), initialEstimateLattice.liveSet.size());
  }
  
  @Test
  public void testGetEscapedLocals() {
    // Create the LiveVariableAnalysis instance
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    
    // Get the escaped locals
    Set<Scope.Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    
    // Validate the escaped locals
    assertNotNull(escapedLocals);
  }
  
  @Test
  public void testGetVarIndex() {
    // Create the LiveVariableAnalysis instance
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    
    // Get the var index
    int varIndex = liveVariablesAnalysis.getVarIndex("var");
    
    // Validate the var index
    assertEquals(0, varIndex);
  }
  
  @Test
  public void testIsForward() {
    // Create the LiveVariableAnalysis instance
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    
    // Check if the analysis is forward
    boolean isForward = liveVariablesAnalysis.isForward();
    
    // Validate the isForward flag
    assertEquals(false, isForward);
  }
  
  @Test
  public void testMarkAllParametersEscaped() {
    // Create the LiveVariableAnalysis instance
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    
    // Mark all parameters escaped
    liveVariablesAnalysis.markAllParametersEscaped();
    
    // Get the escaped locals
    Set<Scope.Var> escapedLocals = liveVariablesAnalysis.getEscapedLocals();
    
    // Validate the escaped locals
    assertNotNull(escapedLocals);
  }
  
  @Test
  public void testIsArgumentsName() {
    // Create a test node
    Node testNode = new Node(Token.NAME, "arguments");
    
    // Create the LiveVariableAnalysis instance
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);
    
    // Check if the node is arguments name
    boolean isArgumentsName = liveVariablesAnalysis.isArgumentsName(testNode);
    
    // Validate the isArgumentsName flag
    assertEquals(true, isArgumentsName);
  }

}