import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphEdge;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Set;

public class LiveVariablesAnalysisTest {
  private LiveVariablesAnalysis analyzer;
  private Scope jsScope;
  
  @Before
  public void setUp() {
    Node script = new Node(Token.SCRIPT);
    DiGraph<Node, DiGraphEdge<Node, ControlFlowGraph.Branch>> cfg = ControlFlowGraph.builder(script).build();
    jsScope = new Scope(script);
    analyzer = new LiveVariablesAnalysis(cfg, jsScope, new AbstractCompiler());
  }
  
  @Test
  public void testComputeGenKill_AdditionAssignment() {
    Node parent = new Node(Token.CALL);
    Node lhs = new Node(Token.NAME, "a");
    Node rhs = new Node(Token.NAME, "b");
    Node assignNode = new Node(Token.ASSIGN_ADD);
    assignNode.addChildToFront(lhs);
    assignNode.addChildToBack(rhs);
    parent.addChildToFront(assignNode);
    
    BitSet gen = new BitSet(jsScope.getVarCount());
    BitSet kill = new BitSet(jsScope.getVarCount());
    analyzer.computeGenKill(assignNode, gen, kill, false);
    
    BitSet expectedGen = new BitSet(jsScope.getVarCount());
    BitSet expectedKill = new BitSet(jsScope.getVarCount());
    expectedGen.set(jsScope.getVar("a").index);
    expectedGen.set(jsScope.getVar("b").index);
    
    assertEquals(expectedGen, gen);
    assertEquals(expectedKill, kill);
  }
  
  @Test
  public void testComputeGenKill_FunctionDeclaration() {
    Node function = new Node(Token.FUNCTION);
    Node child = new Node(Token.NAME, "x");
    function.addChildToBack(child);
    Node parent = new Node(Token.SCRIPT);
    parent.addChildToBack(function);
    
    BitSet gen = new BitSet(jsScope.getVarCount());
    BitSet kill = new BitSet(jsScope.getVarCount());
    analyzer.computeGenKill(function, gen, kill, false);
    
    BitSet expectedGen = new BitSet(jsScope.getVarCount());
    BitSet expectedKill = new BitSet(jsScope.getVarCount());
    expectedKill.set(jsScope.getVar("x").index);
    
    assertEquals(expectedGen, gen);
    assertEquals(expectedKill, kill);
  }
  
  @Test
  public void testFlowThrough() {
    Node parent = new Node(Token.CALL);
    Node lhs = new Node(Token.NAME, "a");
    Node rhs = new Node(Token.NAME, "b");
    Node assignNode = new Node(Token.ASSIGN);
    assignNode.addChildToFront(lhs);
    assignNode.addChildToBack(rhs);
    parent.addChildToFront(assignNode);
    
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    input.liveSet.set(jsScope.getVar("b").index);
    
    LiveVariablesAnalysis.LiveVariableLattice result = analyzer.flowThrough(assignNode, input);
    
    LiveVariablesAnalysis.LiveVariableLattice expected = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    expected.liveSet.set(jsScope.getVar("a").index);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testIsLive() {
    LiveVariablesAnalysis.LiveVariableLattice lattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    lattice.liveSet.set(jsScope.getVar("a").index);
    
    assertTrue(lattice.isLive(jsScope.getVar("a")));
    assertFalse(lattice.isLive(jsScope.getVar("b")));
  }
  
  @Test
  public void testGetEscapedLocals() {
    Set<Var> escaped = analyzer.getEscapedLocals();
    Set<Var> expectedEscaped = new HashSet<>();
    
    assertEquals(expectedEscaped, escaped);
  }
  
  @Test
  public void testGetVarIndex() {
    int index = analyzer.getVarIndex("a");
    
    assertEquals(jsScope.getVar("a").index, index);
  }
  
  @Test
  public void testCreateEntryLattice() {
    LiveVariablesAnalysis.LiveVariableLattice entryLattice = analyzer.createEntryLattice();
    LiveVariablesAnalysis.LiveVariableLattice expectedLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    
    assertEquals(expectedLattice, entryLattice);
  }
  
  @Test
  public void testCreateInitialEstimateLattice() {
    LiveVariablesAnalysis.LiveVariableLattice initialLattice = analyzer.createInitialEstimateLattice();
    LiveVariablesAnalysis.LiveVariableLattice expectedLattice = new LiveVariablesAnalysis.LiveVariableLattice(jsScope.getVarCount());
    
    assertEquals(expectedLattice, initialLattice);
  }
  
  @Test
  public void testIsForward() {
    assertFalse(analyzer.isForward());
  }
  
  @Test
  public void testMarkAllParametersEscaped() {
    analyzer.markAllParametersEscaped();
    Set<Var> escaped = analyzer.getEscapedLocals();
    Set<Var> expectedEscaped = jsScope.getVars();
    
    assertEquals(expectedEscaped, escaped);
  }
  
  @Test
  public void testIsArgumentsName() {
    Node nameNode = new Node(Token.NAME, "arguments");
    
    assertTrue(analyzer.isArgumentsName(nameNode));
  }
  
  @Test
  public void testIsArgumentsName_NotArguments() {
    Node nameNode = new Node(Token.NAME, "notArguments");
    
    assertFalse(analyzer.isArgumentsName(nameNode));
  }
  
  @Test
  public void testIsArgumentsName_VariableDeclared() {
    Node nameNode = new Node(Token.NAME, "arguments");
    jsScope.declare("arguments", null, null, false);
    
    assertFalse(analyzer.isArgumentsName(nameNode));
  }
}