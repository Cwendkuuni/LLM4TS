import com.google.javascript.jscomp.MaybeReachingVariableUse;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MaybeReachingVariableUseTest {

 private static Node createNode(String code) {
 Node script = new Node();
 script.setSource(code, "Test", 1);
 Node root = NodeOperations.parseValue(script).asFunction().getBody();
 return root;
 }

 @Test
 public void testSimpleUse() {
 Scope scope = new Scope();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(scope, null);
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

 String code = "var x = 1;\nAlert(x);";
 Node node = createNode(code);
 analysis.computeReachingUses(node);

 Collection<Node> uses = analysis.getUses("x", node);
 assertEquals(1, uses.size());
 assertEquals(node, uses.iterator().next());
 }

 @Test
 public void testAssignmentUse() {
 Scope scope = new Scope();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(scope, null);
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

 String code = "var x = 1;\nx = 2;\nAlert(x);";
 Node node = createNode(code);
 analysis.computeReachingUses(node);

 Collection<Node> uses = analysis.getUses("x", node);
 assertEquals(1, uses.size());
 assertEquals(node.getChild(1), uses.iterator().next());
 }

 @Test
 public void testConditionalUse() {
 Scope scope = new Scope();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(scope, null);
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

 String code = "var x = 1;\nif (true) {\n x = 2;\n}\nAlert(x);";
 Node node = createNode(code);
 analysis.computeReachingUses(node);

 Collection<Node> uses = analysis.getUses("x", node);
 assertEquals(1, uses.size());
 assertEquals(node.getChild(1), uses.iterator().next());
 }

 @Test
 public void testFunctionUse() {
 Scope scope = new Scope();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(scope, null);
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

 String code = "function test() {\n var x = 1;\n test2();\n Alert(x);\n}\nfunction test2() {\n x = 2;\n}";
 Node node = createNode(code);
 analysis.computeReachingUses(node);

 Collection<Node> uses = analysis.getUsages(node);
 assertEquals(2, uses.size());
 assertTrue(uses.contains(NodeOperations.findFirstDescendantByName(node, "test2")));
 assertTrue(uses.contains(NodeOperations.findFirstDescendantByName(node, "x")));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullScope() {
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCFG() {
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(null, new Scope(), null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCompiler() {
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(new ControlFlowGraph<Node>(null, null), null, null);
 }

 @Test
 public void testNoUses() {
 Scope scope = new Scope();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(scope, null);
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

 String code = "var x = 1;";
 Node node = createNode(code);
 analysis.computeReachingUses(node);

 Collection<Node> uses = analysis.getUses("x", node);
 assertTrue(uses.isEmpty());
 }

 @Test
 public void testEscapedVariable() {
 Scope scope = new Scope();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(scope, null);
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

 String code = "function foo(x) {\n var y = x;\n}\nvar x = 1;\nfoo(x);\nAlert(x);";
 Node node = createNode(code);
 analysis.computeReachingUses(node);

 Collection<Node> uses = analysis.getUses("x", node);
 assertEquals(0, uses.size());
 }

 @Test
 public void testFunctionParameter() {
 Scope scope = new Scope();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(scope, null);
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

 String code = "function foo(x) {\n Alert(x);\n}";
 Node node = createNode(code);
 analysis.computeReachingUses(node);

 Collection<Node> uses = analysis.getUses("x", node);
 assertEquals(0, uses.size());
 }

 @Test
 public void testCatchVariable() {
 Scope scope = new Scope();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(scope, null);
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

 String code = "try {\n var x = 1;\n}\n catch (e) {\n x = 2;\n}\n Alert(x);";
 Node node = createNode(code);
 analysis.computeReachingUses(node);

 Collection<Node> uses = analysis.getUsages(node);
 assertEquals(2, uses.size());
 assertTrue(uses.contains(NodeOperations.findFirstDescendantByName(node, "try")));
 assertTrue(uses.contains(NodeOperations.findFirstDescendantByName(node, "x")));
 }

 @Test
 public void testGlobalVariable() {
 Scope scope = new Scope();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(scope, null);
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

 String code = "var x = 1;\n function test() {\n Alert(x);\n}";
 Node node = createNode(code);
 analysis.computeReachingUses(node);

 Collection<Node> uses = analysis.getUsages(node);
 assertEquals(1, uses.size());
 assertTrue(uses.contains(NodeOperations.findFirstDescendantByName(node, "x")));
 }

 @Test
 public void testGlobalVariableUse() {
 Scope scope = new Scope();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(scope, null);
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

 String code = "var x = 1;\n Alert(x);\n function test() {}";
 Node node = createNode(code);
 analysis.computeReachingUses(node);

 Collection<Node> uses = analysis.getUsages(node);
 assertEquals(1, uses.size());
 assertTrue(uses.contains(NodeOperations.findFirstDescendantByName(node, "x")));
 }

 @Test
 public void testGlobalVariableAssignment() {
 Scope scope = new Scope();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(scope, null);
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

 String code = "var x = 1;\n x = 2;\n Alert(x);";
 Node node = createNode(code);
 analysis.computeReachingUses(node);

 Collection<Node> uses = analysis.getUsages(node);
 assertEquals(1, uses.size());
 assertTrue(uses.contains(NodeOperations.findFirstDescendantByName(node, "x")));
 }

 @Test
 public void testGlobalVariableAssignment2() {
 Scope scope = new Scope();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(scope, null);
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

 String code = "var x = 1;\n x = 2;\n var y = x;\n Alert(y);";
 Node node = createNode(code);
 analysis.computeReachingUses(node);

 Collection<Node> uses = analysis.getUsages(node);
 assertEquals(1, uses.size());
 assertTrue(uses.contains(NodeOperations.findFirstDescendantByName(node, "x")));
 }

 @Test
 public void testGlobalVariableUseWithFunctionCall() {
 Scope scope = new Scope();
 ControlFlowGraph<Node> cfg = new ControlFlowGraph<Node>(scope, null);
 MaybeReachingVariableUse analysis = new MaybeReachingVariableUse(cfg, scope, null);

 String code = "var x = 1;\n function test() {\n Alert(x);\n}";
 Node node = createNode(code);
 Node callNode = NodeOperations.newCallNode("test");
 Node root = NodeOperations.newBlockNode(null, null, null, null, null, null, null, null, null, null, null, null, null, callNode);
 analysis.computeReachingUses(root);

 Collection<Node> uses = analysis.getUsages(node);
 assertEquals(1, uses.size());
 assertTrue(uses.contains(NodeOperations.findFirstDescendantByName(node, "x")));
 }
}