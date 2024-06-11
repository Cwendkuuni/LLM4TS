import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class LiveVariablesAnalysisTest {

  @Test
  public void testCreateEntryLattice() {
    Scope scope = new Scope(new Node(Token.BLOCK));
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(new Node(Token.BLOCK));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, new AbstractCompiler());
    LiveVariablesAnalysis.LiveVariableLattice lattice = analysis.createEntryLattice();
    BitSet expectedLiveSet = new BitSet(scope.getVarCount());
    assertEquals(expectedLiveSet, lattice.liveSet);
  }

  @Test
  public void testFlowThrough() {
    Node node = new Node(Token.NAME, new Node(Token.STRING, "x"));
    node.addChildToBack(new Node(Token.STRING, "y"));
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(1);
    input.liveSet.set(0, true);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(new ControlFlowGraph<Node>(new Node(Token.BLOCK)), new Scope(node), new AbstractCompiler());
    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(node, input);
    BitSet expectedLiveSet = new BitSet(1);
    expectedLiveSet.set(0, true);
    assertEquals(expectedLiveSet, result.liveSet);
  }

  @Test
  public void testComputeGenKill() {
    Node node = new Node(Token.NAME, new Node(Token.STRING, "x"));
    node.addChildToBack(new Node(Token.STRING, "y"));
    BitSet gen = new BitSet(1);
    BitSet kill = new BitSet(1);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(new ControlFlowGraph<Node>(new Node(Token.BLOCK)), new Scope(node), new AbstractCompiler());
    analysis.computeGenKill(node, gen, kill, false);
    BitSet expectedGen = new BitSet(1);
    expectedGen.set(0, true);
    BitSet expectedKill = new BitSet(1);
    expectedKill.set(0, true);
    assertEquals(expectedGen, gen);
    assertEquals(expectedKill, kill);
  }

  @Test
  public void testAddToSetIfLocal() {
    Node node = new Node(Token.NAME, new Node(Token.STRING, "x"));
    Scope scope = new Scope(new Node(Token.BLOCK));
    scope.getVar("x").index = 0;
    BitSet set = new BitSet(1);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(new ControlFlowGraph<Node>(new Node(Token.BLOCK)), scope, new AbstractCompiler());
    analysis.addToSetIfLocal(node, set);
    BitSet expectedSet = new BitSet(1);
    expectedSet.set(0, true);
    assertEquals(expectedSet, set);
  }

  @Test
  public void testMarkAllParametersEscaped() {
    Node node = new Node(Token.NAME, new Node(Token.STRING, "x"));
    Scope scope = new Scope(new Node(Token.BLOCK));
    scope.getVar("x").index = 0;
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(new ControlFlowGraph<Node>(new Node(Token.BLOCK)), scope, new AbstractCompiler());
    analysis.markAllParametersEscaped();
    assertTrue(analysis.getEscapedLocals().contains(scope.getVar("x")));
  }

  @Test
  public void testIsArgumentsName() {
    Node node = new Node(Token.NAME, new Node(Token.STRING, "arguments"));
    Scope scope = new Scope(new Node(Token.BLOCK));
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(new ControlFlowGraph<Node>(new Node(Token.BLOCK)), scope, new AbstractCompiler());
    assertTrue(analysis.isArgumentsName(node));
  }
}