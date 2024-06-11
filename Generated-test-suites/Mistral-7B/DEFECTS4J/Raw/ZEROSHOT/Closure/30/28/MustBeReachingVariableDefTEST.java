import static org.junit.Assert.*;
import com.google.javascript.jscomp.MustDefJoin;
import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.rhino.Node;

public class MustBeReachingVariableDefTest {

  @SuppressWarnings("unchecked")
  private MustBeReachingVariableDef<Node, MustBeReachingVariableDef.MustDef> createMustDefAnalyzer(
      ControlFlowGraph<Node> cfg, Scope jsScope, AbstractCompiler compiler) {
    return new MustBeReachingVariableDef<>(cfg, jsScope, compiler);
  }

  @org.junit.Test
  public void testMustDefAnalysis() {
    // Create a simple ControlFlowGraph for testing.
    String code = "function foo() {"
        + "var x = 1;"
        + "var y = 2;"
        + "var z = 3;"
        + "x = y + z;"
        + "return x;"
        + "}";

    Scope scope = new Scope();
    AbstractCompiler compiler = new Compiler();
    ControlFlowGraph<Node> cfg = compiler.getCFG(new Parser(new TokenStream(code)), scope);

    MustBeReachingVariableDef<Node, MustBeReachingVariableDef.MustDef> analyzer = createMustDefAnalyzer(cfg, scope, compiler);

    // Test the getDef method.
    Node useNode = cfg.getEntryNode().getFirstChild();
    Node defNode = analyzer.getDef("x", useNode);
    assertEquals("x = y + z", defNode.toString());

    // Test the dependsOnOuterScopeVars method.
    useNode = cfg.getNode(cfg.getNodes().get(1)).getFirstChild();
    assertFalse(analyzer.dependsOnOuterScopeVars("x", useNode));

    useNode = cfg.getNode(cfg.getNodes().get(3)).getFirstChild();
    assertTrue(analyzer.dependsOnOuterScopeVars("y", useNode));

    // Test the join operation.
    MustDef a = new MustDef();
    MustDef b = new MustDef();
    MustDef c = new MustDefJoin().apply(a, b);

    Var x = scope.getVar("x");
    Var y = scope.getVar("y");
    Var z = scope.getVar("z");

    Definition xDefA = new Definition(cfg.getNode(1));
    Definition xDefB = new Definition(cfg.getNode(4));
    Definition yDef = new Definition(cfg.getNode(3));

    a.reachingDef.put(x, xDefA);
    a.reachingDef.put(y, yDef);

    b.reachingDef.put(x, xDefB);

    assertEquals(xDefA, c.reachingDef.get(x));
    assertEquals(xDefB, c.reachingDef.get(x));
    assertNull(c.reachingDef.get(y));

    // Test the flowThrough method.
    MustDef input = new MustDef();
    input.reachingDef.put(x, null);
    input.reachingDef.put(y, yDef);
    input.reachingDef.put(z, null);

    MustDef output = analyzer.flowThrough(cfg.getNode(2), input);
    assertTrue(output.reachingDef.containsKey(z));
    assertNull(output.reachingDef.get(y));
  }
}