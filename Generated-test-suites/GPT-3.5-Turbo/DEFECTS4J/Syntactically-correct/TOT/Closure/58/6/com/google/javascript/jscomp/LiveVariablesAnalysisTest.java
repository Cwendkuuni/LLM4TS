import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.FunctionInjector;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

public class LiveVariablesAnalysisTest {

  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
  }

  @Test
  public void testConstructor() {
    Node rootNode = new Node(Token.SCRIPT);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
    Scope jsScope = new Scope(rootNode, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Set<Var> escaped = analysis.getEscapedLocals();
    assertNotNull(escaped);
    assertTrue(escaped.isEmpty());

    int varIndex = analysis.getVarIndex("x");
    assertEquals(-1, varIndex);

    varIndex = analysis.getVarIndex("arguments");
    assertEquals(-1, varIndex);
  }
  
  @Test
  public void testComputeGenKill() {
    Node rootNode = new Node(Token.SCRIPT);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
    Scope jsScope = new Scope(rootNode, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    BitSet genSet = new BitSet(10);
    BitSet killSet = new BitSet(10);
    boolean conditional = false;

    Node conditionNode = new Node(Token.NAME);
    conditionNode.setString("x");
    Node n = new Node(Token.IF, conditionNode);

    analysis.computeGenKill(n, genSet, killSet, conditional);

    assertFalse(genSet.get(0));
    assertFalse(killSet.get(0));
  }

  @Test
  public void testFlowThrough() {
    Node rootNode = new Node(Token.SCRIPT);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
    Scope jsScope = new Scope(rootNode, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    BitSet liveSet = new BitSet(10);
    liveSet.set(2);
    LiveVariablesAnalysis.LiveVariableLattice input = new LiveVariablesAnalysis.LiveVariableLattice(10);
    input.liveSet.or(liveSet);

    Node n = new Node(Token.NAME);
    n.setString("x");

    LiveVariablesAnalysis.LiveVariableLattice result = analysis.flowThrough(n, input);

    assertTrue(result.liveSet.get(0));
    assertTrue(result.liveSet.get(2));
  }

  @Test
  public void testMarkAllParametersEscaped() {
    Node rootNode = new Node(Token.SCRIPT);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
    Scope jsScope = new Scope(rootNode, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Node lpNode = new Node(Token.LP);
    rootNode.addChildToBack(lpNode);

    int numParams = 3;
    for (int i = 0; i < numParams; i++) {
      Node arg = new Node(Token.NAME);
      arg.setString("param" + i);
      lpNode.addChildToBack(arg);
    }

    analysis.markAllParametersEscaped();

    Set<Var> escaped = analysis.getEscapedLocals();
    assertNotNull(escaped);
    assertEquals(numParams, escaped.size());
    for (int i = 0; i < numParams; i++) {
      assertTrue(escaped.contains(jsScope.getVar("param" + i)));
    }
  }

  @Test
  public void testIsArgumentsName() {
    Node rootNode = new Node(Token.SCRIPT);
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(rootNode);
    Scope jsScope = new Scope(rootNode, null);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, jsScope, compiler);

    Node n = new Node(Token.NAME);
    n.setString("arguments");

    assertTrue(analysis.isArgumentsName(n));
  }

}