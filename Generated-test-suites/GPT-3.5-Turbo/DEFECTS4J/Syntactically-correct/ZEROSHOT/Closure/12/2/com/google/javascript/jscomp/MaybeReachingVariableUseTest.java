import org.junit.*;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.GraphNode;

public class MaybeReachingVariableUseTest {

  ControlFlowGraph<Node> cfg;
  Scope jsScope;
  AbstractCompiler compiler;
  MaybeReachingVariableUse reachingVariableUse;

  @Before
  public void setUp() {
    cfg = new ControlFlowGraph<Node>();
    jsScope = new Scope();
    compiler = new AbstractCompiler();
    reachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, compiler);
  }

  @Test
  public void testCreateEntryLattice() {
    MaybeReachingVariableUse.ReachingUses result = reachingVariableUse.createEntryLattice();
    assertNotNull(result);
  }

  @Test
  public void testCreateInitialEstimateLattice() {
    MaybeReachingVariableUse.ReachingUses result = reachingVariableUse.createInitialEstimateLattice();
    assertNotNull(result);
  }

  @Test
  public void testIsForward() {
    boolean result = reachingVariableUse.isForward();
    assertFalse(result);
  }

  @Test
  public void testFlowThrough() {
    Node n = new Node(Token.MUL);
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses result = reachingVariableUse.flowThrough(n, input);
    assertNotNull(result);
  }

  @Test
  public void testGetUses() {
    String name = "testVariable";
    Node defNode = new Node(Token.NUMBER);
    reachingVariableUse.getUses(name, defNode);
  }

  @Test
  public void testComputeMayUse() {
    Node n = new Node(Token.ADD);
    Node cfgNode = new Node(Token.FOR);
    MaybeReachingVariableUse.ReachingUses output = new MaybeReachingVariableUse.ReachingUses();
    boolean conditional = true;
    reachingVariableUse.computeMayUse(n, cfgNode, output, conditional);
  }

  @Test
  public void testAddToUseIfLocal() {
    String name = "testVariable";
    Node node = new Node(Token.STRING);
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    reachingVariableUse.addToUseIfLocal(name, node, use);
  }

  @Test
  public void testRemoveFromUseIfLocal() {
    String name = "testVariable";
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    reachingVariableUse.removeFromUseIfLocal(name, use);
  }
}