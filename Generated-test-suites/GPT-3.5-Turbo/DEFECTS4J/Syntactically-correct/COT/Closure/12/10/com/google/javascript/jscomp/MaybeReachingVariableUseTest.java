
import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class MaybeReachingVariableUseTest {

  private MaybeReachingVariableUse maybeReachingVariableUse;
  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private Node functionNode;

  @Before
  public void setUp() {
    Node root = new Node(Token.ROOT);
    cfg = new ControlFlowGraph<>(root);
    jsScope = new Scope(root, null);
    maybeReachingVariableUse = new MaybeReachingVariableUse(cfg, jsScope, null);
    functionNode = new Node(Token.FUNCTION);
  }

  @Test
  public void testFlowThrough() {
    Node n = new Node(Token.VAR);
    Node varName = new Node(Token.NAME, "x");
    Node varValue = new Node(Token.NUMBER, "10");
    varName.addChildToBack(varValue);
    n.addChildToBack(varName);
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(n, input);
    assertEquals(1, output.mayUseMap.size());
  }

  @Test
  public void testAddToUseIfLocal() {
    Node node = new Node(Token.NAME, "x");
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    maybeReachingVariableUse.addToUseIfLocal("x", node, use);
    assertEquals(1, use.mayUseMap.size());
  }

  @Test
  public void testRemoveFromUseIfLocal() {
    Node node = new Node(Token.NAME, "x");
    MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();
    maybeReachingVariableUse.addToUseIfLocal("x", node, use);
    assertEquals(1, use.mayUseMap.size());
    maybeReachingVariableUse.removeFromUseIfLocal("x", use);
    assertEquals(0, use.mayUseMap.size());
  }

  @Test
  public void testGetUses() {
    Node defNode = new Node(Token.VAR);
    Node varName = new Node(Token.NAME, "x");
    Node varValue = new Node(Token.NUMBER, "10");
    varName.addChildToBack(varValue);
    defNode.addChildToBack(varName);
    MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
    MaybeReachingVariableUse.ReachingUses output = maybeReachingVariableUse.flowThrough(defNode, input);
    Collection<Node> uses = maybeReachingVariableUse.getUses("x", defNode);
    assertEquals(1, uses.size());
  }
}
