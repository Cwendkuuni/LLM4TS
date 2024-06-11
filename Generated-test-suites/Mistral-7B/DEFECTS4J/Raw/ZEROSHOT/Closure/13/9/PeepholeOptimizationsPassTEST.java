import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.jscomp.Node;
import org.junit.Before;
import org.junit.Test;

public class PeepholeOptimizationsPassTest extends CompilerPassTestCase {

 private PeepholeOptimizationsPass peepholeOptimizationsPass;
 private AbstractCompiler compiler;
 private AbstractPeepholeOptimization optimization1;
 private AbstractPeepholeOptimization optimization2;

 @Before
 public void setUp() throws Exception {
 compiler = newCompiler();
 optimization1 = new DummyPeepholeOptimization();
 optimization2 = new DummyPeepholeOptimization();
 peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
 }

 @Test
 public void testProcess() throws Exception {
 Node root = compileTestSource("testData/PeepholeOptimizationsPassTest.js");
 peepholeOptimizationsPass.process(null, root);
 assertCompiledCorrectly("testData/PeepholeOptimizationsPassTest_expected.js", root);
 }

 @Test
 public void testPeepholeChangeHandler() throws Exception {
 Node root = asNode("function foo() { return 1 + 1; }");
 peepholeOptimizationsPass.process(null, root);
 peepholeOptimizationsPass.traverse(root.getFirstChild());
 assertTrue(peepholeOptimizationsPass.traversalState.peek().changed);
 }

 @Test
 public void testShouldVisit() throws Exception {
 Node root = asNode("function foo() { return 1 + 1; }");
 assertTrue(peepholeOptimizationsPass.shouldVisit(root.getFirstChild()));
 }

 @Test
 public void testShouldRetraverse() throws Exception {
 Node root = asNode("function foo() { return 1 + 1; }");
 assertFalse(peepholeOptimizationsPass.shouldRetraverse(root));
 }

 @Test
 public void testExitNode() throws Exception {
 Node root = asNode("function foo() { return 1 + 1; }");
 peepholeOptimizationsPass.traverse(root);
 peepholeOptimizationsPass.exitNode(root);
 assertTrue(peepholeOptimizationsPass.traversalState.isEmpty());
 }

 @Test
 public void testVisit() throws Exception {
 Node root = asNode("function foo() { return 1 + 1; }");
 peepholeOptimizationsPass.traverse(root);
 Node visitorNode = root.getFirstChild().getFirstChild();
 Node optimizedNode = visitorNode.cloneTree();
 peepholeOptimizationsPass.visit(visitorNode);
 Node actualNode = optimizedNode;
 assertNodeEqual(optimizedNode, root.getFirstChild().getFirstChild());
 }

 @Test
 public void testBeginTraversal() throws Exception {
 peepholeOptimizationsPass.beginTraversal();
 assertTrue(optimization1.wasTraversed());
 assertTrue(optimization2.wasTraversed());
 }

 @Test
 public void testEndTraversal() throws Exception {
 peepholeOptimizationsPass.endTraversal();
 assertFalse(optimization1.wasTraversed());
 assertFalse(optimization2.wasTraversed());
 }
}

class DummyPeepholeOptimization implements AbstractPeepholeOptimization, CompilerPass {
 @Override
 public Node optimizeSubtree(Node node) {
 return node;
 }

 @Override
 public void beginTraversal(CompilerPass pass) {
 // empty
 }

 @Override
 public void endTraversal(CompilerPass pass) {
 // empty
 }

 @Override
 public boolean shouldTraverse(Node node, Node parent) {
 return true;
 }

 @Override
 public boolean shouldHandle(Node node, Node parent) {
 return true;
 }

 @Override
 public void handle(Node node, Node parent, CodeChangeHandler changeHandler) {
 // empty
 }

 @Override
 public void process(Node externs, Node root) {
 // empty
 }

 @Override
 public String getShortName() {
 return "DummyPeepholeOptimization";
 }

 @Override
 public boolean wasTraversed() {
 return false;
 }
}