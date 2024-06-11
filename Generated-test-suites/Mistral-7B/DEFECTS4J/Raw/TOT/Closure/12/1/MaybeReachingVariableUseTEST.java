import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private static final String TEST_CODE = "function foo() {" +
      "  var a = 1;" +
      "  if (true) {" +
      "    a = 2;" +
      "  }" +
      "  return a;" +
      "}";

  private Scope jsScope;
  private ControlFlowGraph<Node> cfg;
  private MaybeReachingVariableUse analyzer;

  @Before
  public void setUp() {
    jsScope = new Scope();
    jsScope.setParent(null);
    jsScope.setLocalVars(Sets.newHashMap());

    cfg = new ControlFlowGraph<Node>(NodeUtil.parse(TEST_CODE), jsScope);
    analyzer = new MaybeReachingVariableUse(cfg, jsScope, null);
  }

  @Test
  public void testSimpleAssignment() {
    Node defNode = cfg.getStartNode();
    ReachingUses defUses = analyzer.run(defNode);

    Collection<Node> uses = analyzer.getUses("a", defNode);
    assertFalse("No uses for variable 'a' at the beginning", uses.isEmpty());
    assertNotNull("Use list should not be null", uses);
    assertTrue("Use list should contain the definition node", uses.contains(defNode));
  }

  @Test
  public void testConditionalAssignment() {
    Node ifNode = cfg.getNode(
        NodeUtil.findFirstChildWithToken(cfg.getStartNode(), Token.IF));
    ReachingUses ifUses = analyzer.run(ifNode);

    Collection<Node> uses = analyzer.getUses("a", ifNode);

    assertFalse("No uses for variable 'a' before conditional", uses.isEmpty());
    assertNotNull("Use list should not be null", uses);
    assertFalse("Use list should not contain definition node before conditional",
        uses.contains(cfg.getNode(NodeUtil.getConditionExpression(ifNode))));
    assertTrue("Use list should contain the definition node after the conditional",
        uses.contains(cfg.getNode(cfg.getNode(ifNode).getLastChild())));
  }

  @Test
  public void testNoUses() {
    Node defNode = cfg.getNode(NodeUtil.findFirstChildWithToken(
        cfg.getNode(cfg.getNode(cfg.getStartNode()).getLastChild()), Token.RETURN));
    ReachingUses defUses = analyzer.run(defNode);

    Collection<Node> uses = analyzer.getUses("a", defNode);
    assertNull("No uses for variable 'a' at the end", uses);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullScope() {
    new MaybeReachingVariableUse<>(cfg, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompiler() {
    new MaybeReachingVariableUse<>(cfg, jsScope, null);
  }
}