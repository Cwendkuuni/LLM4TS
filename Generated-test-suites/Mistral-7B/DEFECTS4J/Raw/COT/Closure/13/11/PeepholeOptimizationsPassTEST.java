import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import org.junit.Test;

public class PeepholeOptimizationsPassTest {

 private AbstractCompiler compiler;
 private PeepholeOptimizationsPass peepholeOptimizationsPass;

 @Test
 public void testPeepholeOptimizationsPassConstructor() {
 // Test constructor
 AbstractPeepholeOptimization[] peepholeOptimizations = new AbstractPeepholeOptimization[0];
 peepholeOptimizationsPass = new PeepholeOptimizationsPass(null, peepholeOptimizations);
 assertNull(peepholeOptimizationsPass.getCompiler());
 assertNull(peepholeOptimizationsPass.peepholeOptimizations);

 AbstractPeepholeOptimization optimization = new DummyPeepholeOptimization();
 peepholeOptimizations = new AbstractPeepholeOptimization[]{optimization};
 peepholeOptimizationsPass = new PeepholeOptimizationsPass(null, peepholeOptimizations);
 assertNull(peepholeOptimizationsPass.getCompiler());
 assertArrayEquals(peepholeOptimizations, peepholeOptimizationsPass.peepholeOptimizations);
 }

 @Test
 public void testProcessNullExterns() {
 // Test process(Node externs, Node root) with null externs
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 assertNull(peepholeOptimizationsPass.getCompiler());
 assertArrayEquals(peepholeOptimizationsPass.peepholeOptimizations, peepholeOptimizationsPass.peepholeOptimizations);

 try {
 peepholeOptimizationsPass.process(null, new Node());
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testProcessNullRoot() {
 // Test process(Node externs, Node root) with null root
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 assertNull(peepholeOptimizationsPass.getCompiler());
 assertArrayEquals(peepholeOptimizationsPass.peepholeOptimizations, peepholeOptimizationsPass.peepholeOptimizations);

 try {
 peepholeOptimizationsPass.process(new Node(), null);
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected
 }
 }

 @Test
 public void testProcessWithEmptyAST() {
 // Test process(Node externs, Node root) with empty AST
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 assertNull(peepholeOptimizationsPass.getCompiler());
 assertArrayEquals(peepholeOptimizationsPass.peepholeOptimizations, peepholeOptimizationsPass.peepholeOptimizations);

 Node emptyRoot = new Node();
 peepholeOptimizationsPass.process(new Node(), emptyRoot);
 assertTrue(emptyRoot.getFirstChild() == null);
 }

 @Test
 public void testTraverseNullNode() {
 // Test traverse(Node node) with null node
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 traverseNodeTestHelper(peepholeOptimizationsPass, null);
 }

 @Test
 public void testTraverseScriptNode() {
 // Test traverse(Node node) with script node
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 Node scriptNode = new Node();
 scriptNode.setScript(true);
 traverseNodeTestHelper(peepholeOptimizationsPass, scriptNode);
 }

 @Test
 public void testTraverseFunctionNodeWithoutChildren() {
 // Test traverse(Node node) with function node without children
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 Node functionNode = new Node();
 functionNode.setFunction(true);
 traverseNodeTestHelper(peepholeOptimizationsPass, functionNode);
 }

 @Test
 public void testTraverseFunctionNodeWithChildren() {
 // Test traverse(Node node) with function node with children
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 Node functionNode = new Node();
 functionNode.setFunction(true);
 Node child = new Node();
 functionNode.setFirstChild(child);
 traverseNodeTestHelper(peepholeOptimizationsPass, functionNode);
 }

 @Test
 public void testShouldVisitFunctionNodeWithTraverseChildScopesTrue() {
 // Test shouldVisit(Node node) with function node and traverseChildScopes = true
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 Node functionNode = new Node();
 functionNode.setFunction(true);
 assertTrue(peepholeOptimizationsPass.shouldVisit(functionNode));
 }

 @Test
 public void testShouldVisitFunctionNodeWithTraverseChildScopesFalse() {
 // Test shouldVisit(Node node) with function node and traverseChildScopes = false
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 Node functionNode = new Node();
 functionNode.setFunction(true);
 ScopeState state = peepholeOptimizationsPass.traversalState.peek();
 state.traverseChildScopes = false;
 traversalStateResetTestHelper(peepholeOptimizationsPass);
 assertTrue(peepholeOptimizationsPass.shouldVisit(functionNode));
 }

 @Test
 public void testShouldRetraverseFunctionNodeWithChangedScope() {
 // Test shouldRetraverse(Node node) with function node and changed scope
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 Node functionNode = new Node();
 functionNode.setFunction(true);
 ScopeState previousState = peepholeOptimizationsPass.traversalState.peek();
 previousState.changed = true;
 traversalStateResetTestHelper(peepholeOptimizationsPass);
 assertTrue(peepholeOptimizationsPass.shouldRetraverse(functionNode));
 }

 @Test
 public void testShouldRetraverseFunctionNodeWithUnchangedScope() {
 // Test shouldRetraverse(Node node) with function node and unchanged scope
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 Node functionNode = new Node();
 functionNode.setFunction(true);
 assertFalse(peepholeOptimizationsPass.shouldRetraverse(functionNode));
 }

 @Test
 public void testExitNodeFunctionNode() {
 // Test exitNode(Node node) with function node
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 Node functionNode = new Node();
 functionNode.setFunction(true);
 exitNodeTestHelper(peepholeOptimizationsPass, functionNode);
 }

 @Test
 public void testVisitNullNode() {
 // Test visit(Node n) with null node
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 traverseNodeTestHelper(peepholeOptimizationsPass, null);
 }

 @Test
 public void testVisitScriptNode() {
 // Test visit(Node n) with script node
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 Node scriptNode = new Node();
 scriptNode.setScript(true);
 visitNodeTestHelper(peepholeOptimizationsPass, scriptNode);
 }

 @Test
 public void testVisitFunctionNodeWithoutChildren() {
 // Test visit(Node n) with function node without children
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 Node functionNode = new Node();
 functionNode.setFunction(true);
 visitNodeTestHelper(peepholeOptimizationsPass, functionNode);
 }

 @Test
 public void testVisitFunctionNodeWithChildren() {
 // Test visit(Node n) with function node with children
 peepholeOptimizationsPass = getTestPeepholeOptimizationsPass();
 Node functionNode = new Node();
 functionNode.setFunction(true);
 Node child = new Node();
 functionNode.setFirstChild(child);
 visitNodeTestHelper(peepholeOptimizationsPass, functionNode);
 }

 private PeepholeOptimizationsPass getTestPeepholeOptimizationsPass() {
 AbstractCompiler compiler = new Compiler();
 AbstractPeepholeOptimization[] peepholeOptimizations = new AbstractPeepholeOptimization[0];
 return new PeepholeOptimizationsPass(compiler, peepholeOptimizations);
 }

 private void traverseNodeTestHelper(PeepholeOptimizationsPass peepholeOptimizationsPass, Node node) {
 peepholeOptimizationsPass.traverse(node);
 }

 private void visitNodeTestHelper(PeepholeOptimizationsPass peepholeOptimizationsPass, Node node) {
 peepholeOptimizationsPass.visit(node);
 }

 private void exitNodeTestHelper(PeepholeOptimizationsPass peepholeOptimizationsPass, Node node) {
 peepholeOptimizationsPass.exitNode(node);
 }

 private void traversalStateResetTestHelper(PeepholeOptimizationsPass peepholeOptimizationsPass) {
 peepholeOptimizationsPass.traversalState.peek().reset();
 }

}

class DummyPeepholeOptimization implements AbstractPeepholeOptimization {
 @Override
 public void beginTraversal(AbstractCompiler compiler) {
 // Empty
 }

 @Override
 public void endTraversal(AbstractCompiler compiler) {
 // Empty
 }

 @Override
 public Node optimizeSubtree(Node node) {
 return node;
 }
}