import static org.junit.Assert.*;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  // Test case for createEntryLattice method
  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(new ControlFlowGraph<Node>(),
        new Scope(), new AbstractCompiler());
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
    
    assertNotNull(lattice);
    assertEquals(0, lattice.liveSet.cardinality());
  }
  
  // Test case for createInitialEstimateLattice method
  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(new ControlFlowGraph<Node>(),
        new Scope(), new AbstractCompiler());
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createInitialEstimateLattice();
    
    assertNotNull(lattice);
    assertEquals(0, lattice.liveSet.cardinality());
  }
  
  // Test case for flowThrough method
  @Test
  public void testFlowThrough() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(new ControlFlowGraph<Node>(),
        new Scope(), new AbstractCompiler());
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(3);
    
    // Set some variables as live in input lattice
    input.liveSet.set(0);
    input.liveSet.set(2);
    
    Node node = new Node(Token.VAR, new Node(Token.NAME, "var1"), new Node(Token.NAME, "var2"));
    
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
    
    assertNotNull(result);
    assertTrue(result.liveSet.get(0));
    assertFalse(result.liveSet.get(1));
    assertTrue(result.liveSet.get(2));
  }
  
  // Test case for getEscapedLocals method
  @Test
  public void testGetEscapedLocals() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(new ControlFlowGraph<Node>(),
        new Scope(), new AbstractCompiler());
    
    assertTrue(analysis.getEscapedLocals().isEmpty());
  }
  
  // Test case for getVarIndex method
  @Test
  public void testGetVarIndex() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(new ControlFlowGraph<Node>(),
        new Scope(), new AbstractCompiler());
    analysis.jsScope.addLocal("var1");
    analysis.jsScope.addLocal("var2");
    
    assertEquals(0, analysis.getVarIndex("var1"));
    assertEquals(1, analysis.getVarIndex("var2"));
  }
  
  // Test case for isForward method
  @Test
  public void testIsForward() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(new ControlFlowGraph<Node>(),
        new Scope(), new AbstractCompiler());
    
    assertFalse(analysis.isForward());
  }
  
  // Test case for LiveVariableJoinOp.apply method
  @Test
  public void testJoinOpApply() {
    LiveVariablesAnalysis.LiveVariableLattice lattice1 = new LiveVariablesAnalysis.LiveVariableLattice(3);
    LiveVariablesAnalysis.LiveVariableLattice lattice2 = new LiveVariablesAnalysis.LiveVariableLattice(3);
    LiveVariablesAnalysis.LiveVariableLattice lattice3 = new LiveVariablesAnalysis.LiveVariableLattice(3);
    
    // Set some variables as live in the lattices
    lattice1.liveSet.set(0);
    lattice1.liveSet.set(1);
    
    lattice2.liveSet.set(1);
    lattice2.liveSet.set(2);
    
    lattice3.liveSet.set(0);
    lattice3.liveSet.set(2);
    
    LiveVariablesAnalysis.LiveVariableJoinOp joinOp = new LiveVariablesAnalysis.LiveVariableJoinOp();
    LiveVariablesAnalysis.LiveVariableLattice result = joinOp.apply(Arrays.asList(lattice1, lattice2, lattice3));
    
    assertNotNull(result);
    assertTrue(result.liveSet.get(0));
    assertTrue(result.liveSet.get(1));
    assertTrue(result.liveSet.get(2));
  }
  
  // Test case for markAllParametersEscaped method
  @Test
  public void testMarkAllParametersEscaped() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(new ControlFlowGraph<Node>(),
        new Scope(), new AbstractCompiler());
    
    analysis.jsScope.addLocal("var1");
    analysis.jsScope.addLocal("var2");
    Node lp = new Node(Token.LP, new Node(Token.NAME, "var1"), new Node(Token.NAME, "var2"));
    analysis.markAllParametersEscaped();
    
    assertTrue(analysis.getEscapedLocals().contains(analysis.jsScope.getVar("var1")));
    assertTrue(analysis.getEscapedLocals().contains(analysis.jsScope.getVar("var2")));
  }

  // Test case for isArgumentsName method
  @Test
  public void testIsArgumentsName() {
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(new ControlFlowGraph<Node>(),
        new Scope(), new AbstractCompiler());
    
    Node argumentsNode1 = new Node(Token.NAME, "arguments");
    Node argumentsNode2 = new Node(Token.NAME, "args");
    
    assertTrue(analysis.isArgumentsName(argumentsNode1));
    assertFalse(analysis.isArgumentsName(argumentsNode2));
  }

}