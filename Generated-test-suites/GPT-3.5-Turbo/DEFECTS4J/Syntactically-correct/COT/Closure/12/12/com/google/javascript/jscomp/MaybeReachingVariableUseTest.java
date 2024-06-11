import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSSourceFile;
import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.JSErrorHelper;
import com.google.javascript.rhino.Node;

import java.util.Collection;

import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private static final SourceFile EMPTY_EXTERNS = SourceFile.fromCode("externs", "");
  private static final Compiler.Options OPTIONS = new Compiler.Options();

  private MaybeReachingVariableUse.ReachingUses runVariableUseAnalysis(String js) {
    Compiler compiler = new Compiler();
    compiler.disableThreads();
    compiler.compile(
        EMPTY_EXTERNS,
        ImmutableList.of(SourceFile.fromCode("testcode", js)),
        OPTIONS);
    Node root = compiler.getRoot();
    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, true, true);
    MaybeReachingVariableUse variableUseAnalysis = null;
    ControlFlowGraph<Node> cfg = cfa.getCfg();
    for (ControlFlowGraph<Node> graph : cfg.getDisconnectedGraphs()) {
      Scope scope = graph.getEntry().getAnnotation().getScope();
      variableUseAnalysis = new MaybeReachingVariableUse(graph, scope, compiler);
      variableUseAnalysis.analyze();
    }
    return variableUseAnalysis == null ? null : variableUseAnalysis.getOut().get(root);
  }

  @Test
  public void testUseOfVariable() {
    String js = "var x = 1; var y = x; alert(y);";
    MaybeReachingVariableUse.ReachingUses reachingUses = runVariableUseAnalysis(js);
    Collection<Node> uses = reachingUses.getUses("x", null);
    assertEquals(1, uses.size());
    assertTrue(uses.containsNode("y"));
  }

  // Add other test cases for the identified edge cases and exception handling scenarios

}