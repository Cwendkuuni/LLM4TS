import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MaybeReachingVariableUseTest {

 private ControlFlowGraph<Node> controlFlowGraph;
 private Scope jsScope;
 private MaybeReachingVariableUse maybeReachingVariableUse;

 @Before
 public void setup() {
 controlFlowGraph = mock(ControlFlowGraph.class);
 jsScope = mock(Scope.class);
 maybeReachingVariableUse = new MaybeReachingVariableUse(controlFlowGraph, jsScope, null);
 }

 @Test
 public void testFlowThrough() {
 Node n1 = Mockito.mock(Node.class);
 Node n2 = Mockito.mock(Node.class);
 Node n3 = Mockito.mock(Node.class);
 Node n4 = Mockito.mock(Node.class);
 Node n5 = Mockito.mock(Node.class);

 when(controlFlowGraph.getNode(n1)).thenReturn(Mockito.mock(GraphNode.class));
 when(controlFlowGraph.getNode(n2)).thenReturn(Mockito.mock(GraphNode.class));
 when(controlFlowGraph.getNode(n3)).thenReturn(Mockito.mock(GraphNode.class));
 when(controlFlowGraph.getNode(n4)).thenReturn(Mockito.mock(GraphNode.class));
 when(controlFlowGraph.getNode(n5)).thenReturn(Mockito.mock(GraphNode.class));

 MaybeReachingVariableUse.ReachingUses input = new MaybeReachingVariableUse.ReachingUses();
 MaybeReachingVariableUse.ReachingUses output = new MaybeReachingVariableUse.ReachingUses();

 maybeReachingVariableUse.flowThrough(n1, input, output);
 assertEquals(input, output);

 maybeReachingVariableUse.flowThrough(n2, output, output);
 assertEquals(input, output);

 maybeReachingVariableUse.flowThrough(n3, output, output);
 assertEquals(input, output);

 maybeReachingVariableUse.flowThrough(n4, output, output);
 assertEquals(input, output);

 maybeReachingVariableUse.flowThrough(n5, output, output);
 assertEquals(input, output);

 maybeReachingVariableUse.flowThrough(n1, output, output);
 assertEquals(input, output);

 maybeReachingVariableUse.flowThrough(n2, output, output);
 assertEquals(input, output);

 maybeReachingVariableUse.flowThrough(n3, output, output);
 assertEquals(input, output);

 maybeReachingVariableUse.flowThrough(n4, output, output);
 assertEquals(input, output);

 maybeReachingVariableUse.flowThrough(n5, output, output);
 assertEquals(input, output);

 MaybeReachingVariableUse.ReachingUses result = new MaybeReachingVariableUse.ReachingUses();
 maybeReachingVariableUse.flowThrough(n1, output, result);
 assertEquals(input, result);
 }

 @Test
 public void testAddToUseIfLocal() {
 String name = "name";
 Node node = Mockito.mock(Node.class);
 MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();

 maybeReachingVariableUse.addToUseIfLocal(name, node, use);

 Var var = Mockito.mock(Var.class);
 when(jsScope.getVar(name)).thenReturn(var);

 assertEquals(0, use.mayUseMap.size());
 use.mayUseMap.put(var, node);
 assertEquals(1, use.mayUseMap.size());
 }

 @Test
 public void testRemoveFromUseIfLocal() {
 String name = "name";
 Node node = Mockito.mock(Node.class);
 MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();

 maybeReachingVariableUse.removeFromUseIfLocal(name, use);

 Var var = Mockito.mock(Var.class);
 when(jsScope.getVar(name)).thenReturn(var);

 assertEquals(0, use.mayUseMap.size());
 }

 @Test
 public void testGetUses() {
 String name = "name";
 Node defNode = Mockito.mock(Node.class);
 MaybeReachingVariableUse.ReachingUses use = new MaybeReachingVariableUse.ReachingUses();

 when(jsScope.getVar(name)).thenReturn(Mockito.mock(Var.class));

 maybeReachingVariableUse.addToUseIfLocal(name, defNode, use);

 Collection<Node> expectedUses = new HashSet<>();
 expectedUses.add(Mockito.mock(Node.class));

 when(controlFlowGraph.getNode(Mockito.any())).thenAnswer(i -> {
 List<GraphNode<Node, Branch>> nodes = new ArrayList<>();
 nodes.add(Mockito.mock(GraphNode.class));
 return Mockito.mock(GraphNode.class, nodes);
 });

 when(controlFlowGraph.getNode(defNode).getAnnotation()).thenReturn(Mockito.mock(FlowState.class));

 when(controlFlowGraph.getNode(Mockito.any()).getAnnotation()).thenAnswer(i -> use);

 assertEquals(expectedUses, maybeReachingVariableUse.getUses(name, defNode));
 }
}