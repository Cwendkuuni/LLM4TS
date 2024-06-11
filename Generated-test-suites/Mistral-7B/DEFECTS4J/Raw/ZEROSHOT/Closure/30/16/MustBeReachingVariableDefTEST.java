import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.AbstractCfgNode;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class MustBeReachingVariableDefTest {

  @Test
  public void testMustBeReachingVariableDef_SimpleTest() {
    Scope mockScope = mock(Scope.class);
    AbstractCompiler mockCompiler = mock(AbstractCompiler.class);

    ControlFlowGraph<Node> mockControlFlowGraph = mock(ControlFlowGraph.class);
    when(mockControlFlowGraph.getNode(any(Node.class))).thenReturn(
        mock(AbstractCfgNode.class));

    MustBeReachingVariableDef mustBeReachingVariableDef = new MustBeReachingVariableDef(
        mockControlFlowGraph, mockScope, mockCompiler);

    Node useNode = mock(Node.class);
    Node definitionNode = mock(Node.class);

    when(mockControlFlowGraph.hasNode(useNode)).thenReturn(true);
    when(mockControlFlowGraph.getNode(useNode)).thenReturn(
        mock(AbstractCfgNode.class));

    when(mockControlFlowGraph.getNode(definitionNode)).thenReturn(
        mock(AbstractCfgNode.class));

    when(mockControlFlowGraph.getNode(definitionNode).getAnnotation())
        .thenReturn(new FlowState<>(new MustDef()));

    when(mockScope.getVar(anyString())).thenAnswer(invocation -> {
      String name = invocation.callRealMethod();
      Var var = new Var(mock(Node.class), name, 0, null, null);
      return var;
    });

    when(mockScope.getVars()).thenReturn(Sets.newHashSet(new Var(null, "x", 0, null, null), new Var(null, "y", 0, null, null)));

    Node def = mustBeReachingVariableDef.getDef("x", useNode);
    assertNotNull(def);
    assertNotNull(mustBeReachingVariableDef.getDef("y", useNode));
    assertNull(mustBeReachingVariableDef.getDef("z", useNode));

    assertFalse(mustBeReachingVariableDef.dependsOnOuterScopeVars("x", useNode));
    assertFalse(mustBeReachingVariableDef.dependsOnOuterScopeVars("y", useNode));
    assertFalse(mustBeReachingVariableDef.dependsOnOuterScopeVars("z", useNode));
  }
}