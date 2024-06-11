import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.graph.GraphNode;
import com.google.javascript.rhino.Node;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MaybeReachingVariableUseTest {

  @Test
  public void testSimpleVariableUse() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  var y = x;" +
        "  return y;" +
        "}";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, new AbstractCompiler());
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    Node entryNode = analysis.getEntryNode();
    Node returnNode = analysis.getReturnNode();

    analysis.computeReachingUses(entryNode, returnNode);

    Collection<Node> uses = analysis.getUses("x", returnNode);
    assertNotNull(uses);
    assertEquals(1, uses.size());
    assertEquals(analysis.getCFG().getNode(2), uses.iterator().next());
  }

  @Test
  public void testVariableEscapedByAssignment() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  var y = x;" +
        "  x = 2;" +
        "  return y;" +
        "}";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, new AbstractCompiler());
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    Node entryNode = analysis.getEntryNode();
    Node returnNode = analysis.getReturnNode();

    analysis.computeReachingUses(entryNode, returnNode);

    Collection<Node> uses = analysis.getUses("x", returnNode);
    assertNotNull(uses);
    assertEquals(0, uses.size());
  }

  @Test
  public void testVariableEscapedByFunctionCall() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  var y = bar(x);" +
        "  return y;" +
        "}" +
        "function bar(a) {" +
        "  a = 2;" +
        "  return a;" +
        "}";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, new AbstractCompiler());
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    Node entryNode = analysis.getEntryNode();
    Node returnNode = analysis.getReturnNode();

    analysis.computeReachingUses(entryNode, returnNode);

    Collection<Node> uses = analysis.getUses("x", returnNode);
    assertNotNull(uses);
    assertEquals(0, uses.size());
  }

  @Test
  public void testVariableEscapedByTryCatch() {
    String jsCode = "function foo() {" +
        "  try {" +
        "    var x = 1;" +
        "    throw new Error('Test');" +
        "    var y = x;" +
        "  } catch (e) {" +
        "    var x = 2;" +
        "    return y;" +
        "  }" +
        "}" +
        "function bar() {}";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, new AbstractCompiler());
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    Node entryNode = analysis.getEntryNode();
    Node returnNode = analysis.getReturnNode();

    analysis.computeReachingUses(entryNode, returnNode);

    Collection<Node> uses = analysis.getUses("x", returnNode);
    assertNotNull(uses);
    assertEquals(0, uses.size());
  }

  @Test
  public void testVariableEscapedByIfStatement() {
    String jsCode = "function foo() {" +
        "  var x = 1;" +
        "  if (true) {" +
        "    var y = x;" +
        "  } else {" +
        "    var x = 2;" +
        "  }" +
        "  return y;" +
        "}";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, new AbstractCompiler());
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    Node entryNode = analysis.getEntryNode();
    Node returnNode = analysis.getReturnNode();

    analysis.computeReachingUses(entryNode, returnNode);

    Collection<Node> uses = analysis.getUses("x", returnNode);
    assertNotNull(uses);
    assertEquals(0, uses.size());
  }

  @Test
  public void testVariableEscapedByForStatement() {
    String jsCode = "function foo() {" +
        "  for (var x in [1, 2, 3]) {" +
        "    var y = x;" +
        "  }" +
        "  return y;" +
        "}";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, new AbstractCompiler());
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    Node entryNode = analysis.getEntryNode();
    Node returnNode = analysis.getReturnNode();

    analysis.computeReachingUses(entryNode, returnNode);

    Collection<Node> uses = analysis.getUses("x", returnNode);
    assertNotNull(uses);
    assertEquals(0, uses.size());
  }

  @Test
  public void testVariableEscapedByFunctionDeclaration() {
    String jsCode = "function foo() {" +
        "  function bar(x) {" +
        "    var y = x;" +
        "  }" +
        "  return bar(1);" +
        "}" +
        "function bar(a) {" +
        "  a = 2;" +
        "  return a;" +
        "}";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, new AbstractCompiler());
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    Node entryNode = analysis.getEntryNode();
    Node returnNode = analysis.getReturnNode();

    analysis.computeReachingUses(entryNode, returnNode);

    Collection<Node> uses = analysis.getUses("x", returnNode);
    assertNotNull(uses);
    assertEquals(0, uses.size());
  }

  @Test
  public void testVariableEscapedByExpressionStatement() {
    String jsCode = "function foo() {" +
        "  (1 + 1) * 2;" +
        "  return 1;" +
        "}";

    Scope jsScope = new Scope();
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, new AbstractCompiler());
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
    Node entryNode = analysis.getEntryNode();
    Node returnNode = analysis.getReturnNode();

    analysis.computeReachingUses(entryNode, returnNode);

    Collection<Node> uses = analysis.getUses("x", returnNode);
    assertNotNull(uses);
    assertEquals(0, uses.size());
  }

  @Test(expected = AssertionError.class)
  public void testNullScope() {
    String jsCode = "function foo() {}";
    Scope jsScope = null;
    ControlFlowGraph<Node> cfg = new ControlFlowGraph<>(jsScope, new AbstractCompiler());
    MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, jsScope, null);
  }
}