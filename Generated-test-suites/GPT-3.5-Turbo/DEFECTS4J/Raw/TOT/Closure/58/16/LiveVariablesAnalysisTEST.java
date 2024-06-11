import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class LiveVariablesAnalysisTest {

  @Test
  public void testConstructor() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
    Scope jsScope = new Scope(new Node(Token.SCRIPT));
    AbstractCompiler compiler = new AbstractCompiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    assertNotNull(analysis);
  }

  @Test
  public void testGetEscapedLocals() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
    Scope jsScope = new Scope(new Node(Token.SCRIPT));
    AbstractCompiler compiler = new AbstractCompiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Set<Var> escapedLocals = analysis.getEscapedLocals();

    assertNotNull(escapedLocals);
  }

  @Test
  public void testGetVarIndex() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
    Scope jsScope = new Scope(new Node(Token.SCRIPT));
    AbstractCompiler compiler = new AbstractCompiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    int varIndex = analysis.getVarIndex("var");

    assertEquals(-1, varIndex);
  }

  @Test
  public void testIsForward() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
    Scope jsScope = new Scope(new Node(Token.SCRIPT));
    AbstractCompiler compiler = new AbstractCompiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    boolean isForward = analysis.isForward();

    assertFalse(isForward);
  }

  @Test
  public void testCreateEntryLattice() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
    Scope jsScope = new Scope(new Node(Token.SCRIPT));
    AbstractCompiler compiler = new AbstractCompiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    LiveVariableLattice entryLattice = analysis.createEntryLattice();

    assertNotNull(entryLattice);
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
    Scope jsScope = new Scope(new Node(Token.SCRIPT));
    AbstractCompiler compiler = new AbstractCompiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    LiveVariableLattice initialEstimateLattice = analysis.createInitialEstimateLattice();

    assertNotNull(initialEstimateLattice);
  }

  @Test
  public void testFlowThrough() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
    Scope jsScope = new Scope(new Node(Token.SCRIPT));
    AbstractCompiler compiler = new AbstractCompiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Node node = new Node(Token.SCRIPT);
    LiveVariableLattice input = new LiveVariableLattice(jsScope.getVarCount());

    LiveVariableLattice result = analysis.flowThrough(node, input);

    assertNotNull(result);
  }

  @Test
  public void testComputeGenKill() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
    Scope jsScope = new Scope(new Node(Token.SCRIPT));
    AbstractCompiler compiler = new AbstractCompiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Node n = new Node(Token.SCRIPT);
    BitSet gen = new BitSet();
    BitSet kill = new BitSet();
    boolean conditional = false;

    analysis.computeGenKill(n, gen, kill, conditional);

    assertNotNull(gen);
    assertNotNull(kill);
  }

  @Test
  public void testAddToSetIfLocal() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
    Scope jsScope = new Scope(new Node(Token.SCRIPT));
    AbstractCompiler compiler = new AbstractCompiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Node node = new Node(Token.NAME, "var");
    BitSet set = new BitSet();

    analysis.addToSetIfLocal(node, set);

    assertTrue(set.isEmpty());
  }

  @Test
  public void testMarkAllParametersEscaped() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
    Scope jsScope = new Scope(new Node(Token.SCRIPT));
    AbstractCompiler compiler = new AbstractCompiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    analysis.markAllParametersEscaped();

    Set<Var> escapedLocals = analysis.getEscapedLocals();

    assertNotNull(escapedLocals);
  }

  @Test
  public void testIsArgumentsName() {
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(new Node(Token.SCRIPT));
    Scope jsScope = new Scope(new Node(Token.SCRIPT));
    AbstractCompiler compiler = new AbstractCompiler();
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Node n = new Node(Token.NAME, "arguments");

    boolean isArgumentsName = analysis.isArgumentsName(n);

    assertFalse(isArgumentsName);
  }

}