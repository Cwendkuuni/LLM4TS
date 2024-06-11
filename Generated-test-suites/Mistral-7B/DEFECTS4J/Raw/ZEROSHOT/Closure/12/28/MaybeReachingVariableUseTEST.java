import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MaybeReachingVariableUseTest {

  @org.junit.Test
  public void testMaybeReachingVariableUse() {
    // given
    Scope jsScope = mock(Scope.class);
    ControlFlowGraph<Node> controlFlowGraph = mock(ControlFlowGraph.class);
    MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, null);

    // when
    Multimap<Var, Node> result = maybeReachingVariableUse.getUses("variableName", new Node(Token.NAME, "defNode"));

    // then
    assertEquals(Collections.singletonList(new Node(Token.NAME, "useNode")), result.get("variableName"));

    // when and then for multiple uses
    when(controlFlowGraph.getNode("defNode2")).thenReturn(new GraphNode<Node, Branch>(new Node(Token.NAME, "defNode2")) {
      @Override
      public FlowState<ReachingUses> getAnnotation() {
        return null;
      }
    });
    result = maybeReachingVariableUse.getUses("variableName", new Node(Token.NAME, "defNode2"));
    assertEquals(Sets.newHashSet(new Node(Token.NAME, "useNode1"), new Node(Token.NAME, "useNode2")), result.get("variableName").values());
  }
}