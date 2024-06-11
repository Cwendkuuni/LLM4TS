import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {

 private MaybeReachingVariableUse analyzer;
 private ControlFlowGraph<Node> controlFlowGraph;
 private Scope jsScope;

 @Before
 public void setup() {
 controlFlowGraph = new ControlFlowGraph<>(new Node("#sourceFile"));
 jsScope = new Scope(controlFlowGraph.getEntryNode());
 MaybeReachingVariableUse maybeReachingVariableUse = new MaybeReachingVariableUse(
 controlFlowGraph, jsScope, null);
 analyzer = maybeReachingVariableUse;
 }

 @Test
 public void testEmptyProgram() {
 // Basic test for empty program
 ReachingUses result = analyzer.analyze(controlFlowGraph.getEntryNode());
 assertEquals(new ReachingUses(), result);
 }

 @Test
 public void testLocalVariableUse() {
 // Basic test for local variable use
 controlFlowGraph.addEdge(controlFlowGraph.getEntryNode(), createNode("A = 1;"));
 controlFlowGraph.addEdge(createNode("A = 1;"), createNode("print(A);"));
 ReachingUses result = analyzer.analyze(controlFlowGraph.getEntryNode());
 ImmutableSet<Node> uses = result.mayUseMap.get(jsScope.getVar("A")).keySet();
 assertTrue(uses.contains(controlFlowGraph.getNode(createNode("print(A)"))));
 }

 @Test
 public void testConditionalUse() {
 // Basic test for conditional use
 controlFlowGraph.addEdge(controlFlowGraph.getEntryNode(), createNode("if (x) { A = 1; }"));
 controlFlowGraph.addEdge(controlFlowGraph.getEntryNode(), createNode("print(A);"));
 controlFlowGraph.addEdge(createNode("if (x) { A = 1; }"), createNode("A = 1;"));
 ReachingUses result = analyzer.analyze(controlFlowGraph.getEntryNode());
 ImmutableSet<Node> uses = result.mayUseMap.get(jsScope.getVar("A")).keySet();
 assertTrue(uses.contains(controlFlowGraph.getNode(createNode("print(A)"))));
 }

 @Test
 public void testEscapedVariableUse() {
 // Test for escaped variable use
 controlFlowGraph.addEdge(controlFlowGraph.getEntryNode(), createNode("function foo() { var x = 1; return x; }"));
 controlFlowGraph.addEdge(createNode("function foo() { var x = 1; return x; }"), createNode("A = foo();"));
 controlFlowGraph.addEdge(createNode("A = foo();"), createNode("print(A);"));
 ReachingUses result = analyzer.analyze(controlFlowGraph.getEntryNode());
 ImmutableSet<Node> uses = result.mayUseMap.get(jsScope.getVar("A")).keySet();
 assertFalse(uses.contains(controlFlowGraph.getNode(createNode("function foo() { var x = 1; return x; }"))));
 }

 @Test(expected = IllegalStateException.class)
 public void testNullControlFlowGraph() {
 MaybeReachingVariableUse analyzer = new MaybeReachingVariableUse(null, jsScope, null);
 analyzer.analyze(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testNullScope() {
 MaybeReachingVariableUse analyzer = new MaybeReachingVariableUse(controlFlowGraph, null, null);
 analyzer.analyze(controlFlowGraph.getEntryNode());
 }

 @Test(expected = NullPointerException.class)
 public void testNullVar() {
 ReachingUses result = analyzer.getUses(null, controlFlowGraph.getEntryNode());
 }
}