import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestCase;
import com.google.javascript.jscomp.Node;
import java.util.ArrayList;

public class PeepholeOptimizationsPassTest extends CompilerPassTestCase {

 private PeepholeOptimizationsPass peepholeOptimizationsPass;
 private AbstractCompiler compiler;
 private AbstractPeepholeOptimization optimization1;
 private AbstractPeepholeOptimization optimization2;

 @Before
 public void setUp() throws Exception {
 compiler = newSimpleCompiler();
 peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
 }

 @Test
 public void testProcess() {
 Node root = asNode("function foo() {}");
 peepholeOptimizationsPass.process(null, root);
 assertNull(root);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCompiler() {
 peepholeOptimizationsPass = new PeepholeOptimizationsPass(null, optimization1, optimization2);
 }

 @Test
 public void testEmptyOptimizationsArray() {
 peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, (AbstractPeepholeOptimization[]) new AbstractPeepholeOptimization[0]);
 }

 @Test
 public void testVisitNullNode() {
 peepholeOptimizationsPass.visit(null);
 }

 @Test
 public void testShouldVisitFunctionOrScript() {
 Node functionNode = asNode("function foo() {}");
 ScopeState state = peepholeOptimizationsPass.traversalState.peek();
 assertTrue(peepholeOptimizationsPass.shouldVisit(functionNode));
 }

 @Test
 public void testShouldNotVisitNonFunctionOrScript() {
 Node numberNode = asNode("10");
 assertFalse(peepholeOptimizationsPass.shouldVisit(numberNode));
 }

 @Test
 public void testShouldVisitWithTraverseChildScopesTrue() {
 Node functionWithChildNode = asNode("function foo() { return 1 + a; }");
 ScopeState state = peepholeOptimizationsPass.traversalState.peek();
 assertTrue(peepholeOptimizationsPass.shouldVisit(functionWithChildNode));
 }

 @Test
 public void testShouldNotVisitWithTraverseChildScopesFalse() {
 Node functionWithChildNode = asNode("function foo() { return 1 + a; }");
 peepholeOptimizationsPass.traversalState.peek().traverseChildScopes = false;
 assertFalse(peepholeOptimizationsPass.shouldVisit(functionWithChildNode));
 }

 @Test
 public void testShouldRetraverseFunctionOrScript() {
 Node functionNode = asNode("function foo() {}");
 ScopeState state = peepholeOptimizationsPass.traversalState.peek();
 assertTrue(peepholeOptimizationsPass.shouldRetraverse(functionNode));
 }

 @Test
 public void testShouldNotRetraverseNonFunctionOrScript() {
 Node numberNode = asNode("10");
 assertFalse(peepholeOptimizationsPass.shouldRetraverse(numberNode));
 }

 @Test
 public void testShouldNotRetraverseFunctionWithNoChangedFlag() {
 Node functionNode = asNode("function foo() {}");
 ScopeState state = new ScopeState();
 peepholeOptimizationsPass.traversalState = new StateStack();
 peepholeOptimizationsPass.traversalState.push();
 peepholeOptimizationsPass.traversalState.peek().changed = false;
 assertFalse(peepholeOptimizationsPass.shouldRetraverse(functionNode));
 }

 @Test
 public void testShouldRetraverseFunctionWithChangedFlag() {
 Node funcionWithChangedNode = asNode("function foo() { return a; }");
 ScopeState state = new ScopeState();
 peepholeOptimizationsPass.traversalState = new StateStack();
 peepholeOptimizationsPass.traversalState.push();
 peepholeOptimizationsPass.traversalState.peek().changed = true;
 assertTrue(peepholeOptimizationsPass.shouldRetraverse(funcionWithChangedNode));
 }

 @Test
 public void testExitNodeNullNode() {
 peepholeOptimizationsPass.exitNode(null);
 }

 @Test
 public void testBeginTraversalAndEndTraversal() {
 peepholeOptimizationsPass.beginTraversal();
 peepholeOptimizationsPass.endTraversal();
 }

 @Test(expected = RuntimeException.class)
 public void testBeginTraversalWithoutPeepholeOptimizations() {
 peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, null);
 peepholeOptimizationsPass.beginTraversal();
 }

 @Test(expected = RuntimeException.class)
 public void testEndTraversalWithoutPeepholeOptimizations() {
 peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, null);
 peepholeOptimizationsPass.endTraversal();
 }

 @Test
 public void testVisitAndNothingChanged() {
 Node initialNode = asNode("function foo() { return a + b; }");
 peepholeOptimizationsPass.visit(initialNode);
 assertEquals(initialNode.toString(), peepholeOptimizationsPass.compiler.getSource().getCode(initialNode).toString());
 }

 @Test
 public void testVisitAndSomethingChanged() {
 Node initialNode = asNode("function foo() { return a + b; }");
 Node optimizedNode = asNode("function foo() { return 10; }");
 peepholeOptimizationsPass.visit(initialNode);
 assertEquals(optimizedNode.toString(), peepholeOptimizationsPass.compiler.getSource().getCode(initialNode).toString());
 }

 @Test(expected = NullPointerException.class)
 public void testVisitAndNullOptimization() {
 Node initialNode = asNode("function foo() { return a + b; }");
 peepholeOptimizationsPass.peepholeOptimizations[0] = null;
 peepholeOptimizationsPass.visit(initialNode);
 }
}

class ScopeState {
 boolean changed;
 boolean traverseChildScopes;
}