import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTest.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeepholeOptimizationsPassTest extends CompilerPassTestBase {

 private static AbstractCompiler compiler;
 private static PeepholeOptimizationsPass pass;

 @BeforeClass
 public static void setup() {
 compiler = createCompiler();
 pass = new PeepholeOptimizationsPass(compiler,
 new ConstantFolding(), new DeadCodeRemoval());
 }

 @Test
 public void testProcess_noChanges() {
 Node rootNode = createSimpleFunctionNode("function foo() {}");
 pass.process(null, rootNode);
 assertEquals(createNode("function foo() {}"), rootNode);
 }

 @Test
 public void testProcess_constantFolding() {
 Node rootNode = createNode("function foo() { return 1 + 1; }");
 pass.process(null, rootNode);
 assertEquals(createNode("return 2;"), rootNode);
 }

 @Test
 public void testProcess_deadCodeRemoval() {
 Node rootNode = createNode("function foo() { return 1 + 1; } if (false) {}");
 pass.process(null, rootNode);
 assertEquals(createNode("function foo() { return 1 + 1; }"), rootNode);
 }

 @Test
 public void testProcess_constantFoldingAndDeadCodeRemoval() {
 Node rootNode = createNode("function foo() { return 1 + 1; if (false) {} }");
 pass.process(null, rootNode);
 assertEquals(createNode("return 2;"), rootNode);
 }

 @Test(expected = AssertionError.class)
 public void testProcess_optimizationError() {
 AbstractPeepholeOptimization optimization = new OptimizationWithError();
 pass = new PeepholeOptimizationsPass(compiler, optimization);
 Node rootNode = createNode("function foo() {}");
 pass.process(null, rootNode);
 fail("An optimization error should have been thrown.");
 }

 @Test
 public void testShouldVisit_function() {
 Node functionNode = createFunctionDeclaration("foo");
 assertTrue(pass.shouldVisit(functionNode));
 }

 @Test
 public void testShouldVisit_script() {
 Node scriptNode = createScriptNode("function foo() {}");
 assertTrue(pass.shouldVisit(scriptNode));
 }

 @Test
 public void testShouldVisit_functionWithDisabledChildScopes() {
 Node functionNode = createFunctionDeclaration("foo");
 pass.traversalState.peek().traverseChildScopes = false;
 assertFalse(pass.shouldVisit(functionNode));
 }

 @Test
 public void testShouldVisit_scriptWithDisabledChildScopes() {
 Node scriptNode = createScriptNode("function foo() {}");
 pass.traversalState.peek().traverseChildScopes = false;
 assertFalse(pass.shouldVisit(scriptNode));
 }

 @Test
 public void testShouldRetraverse_function() {
 Node functionNode = createFunctionDeclaration("foo");
 assertTrue(pass.shouldRetraverse(functionNode));
 }

 @Test
 public void testShouldRetraverse_script() {
 Node scriptNode = createScriptNode("function foo() {}");
 assertFalse(pass.shouldRetraverse(scriptNode));
 }

 @Test
 public void testShouldRetraverse_functionWithChangedParent() {
 Node parentNode = createFunctionDeclaration("parent");
 Node functionNode = createFunctionDeclaration("foo");
 functionNode.setParent(parentNode);
 assertTrue(pass.shouldRetraverse(functionNode));
 }

 @Test
 public void testShouldRetraverse_scriptWithChangedParent() {
 Node parentNode = createFunctionDeclaration("parent");
 Node scriptNode = createScriptNode("function foo() {}");
 scriptNode.setParent(parentNode);
 assertFalse(pass.shouldRetraverse(scriptNode));
 }

 @Test
 public void testExitNode_function() {
 Node functionNode = createFunctionDeclaration("foo");
 pass.traversalState.push();
 pass.exitNode(functionNode);
 assertEquals(1, pass.traversalState.states.size());
 }

 @Test
 public void testExitNode_script() {
 Node scriptNode = createScriptNode("function foo() {}");
 pass.traversalState.push();
 pass.exitNode(scriptNode);
 assertEquals(1, pass.traversalState.states.size());
 }

 @Test
 public void testVisit_simpleNode() {
 Node node = createNumberNode(1);
 pass.visit(node);
 assertEquals(node, pass.currentVersionOfNode);
 }

 @Test
 public void testVisit_complexNode() {
 Node rootNode = createNode("function foo() { return (a + b) * 2; }");
 pass.process(null, rootNode);
 assertEquals("return 2 * (a + b);", rootNode.getSource());
 }

 @Test
 public void testVisit_multipleOptimizations() {
 Node rootNode = createNode("function foo() { return (a + 1) * (b + 1); }");
 pass.process(null, rootNode);
 assertEquals("return a + b + 2;", rootNode.getSource());
 }

 @Test
 public void testBeginTraversal() {
 pass.beginTraversal();
 assertTrue(Arrays.stream(pass.peepholeOptimizations).allMatch(opt -> opt.isTraversalStarted()));
 }

 @Test
 public void testEndTraversal() {
 pass.endTraversal();
 assertTrue(Arrays.stream(pass.peepholeOptimizations).allMatch(opt -> opt.isTraversalEnded()));
 }
}

class ConstantFolding implements AbstractPeepholeOptimization {
 @Override
 public Node optimizeSubtree(Node root) {
 if (root.isBinaryOperator()) {
 Node left = root.getFirstChild();
 Node right = root.getLastChild();
 if (root.getOperator().equals(JSOP_ADD)) {
 int leftValue = getIntValue(left);
 int rightValue = getIntValue(right);
 if (leftValue != NodeOperations.UNDEFINED && rightValue != NodeOperations.UNDEFINED) {
 return NodeOperations.makeIntNode(leftValue + rightValue);
 }
 }
 }
 return null;
 }

 @Override
 public void beginTraversal(CompilerPass compiler) {
 // no-op
 }

 @Override
 public void endTraversal(CompilerPass compiler) {
 // no-op
 }

 @Override
 public boolean isTraversalStarted() {
 return true;
 }

 @Override
 public boolean isTraversalEnded() {
 return true;
 }
}

class DeadCodeRemoval implements AbstractPeepholeOptimization {
 @Override
 public Node optimizeSubtree(Node root) {
 if (root.isIfStatement()) {
 Node condition = root.getFirstChild();
 Node consequent = root.getLastChild().getFirstChild();
 if (condition.isLiteral(JS_FALSE)) {
 return consequent;
 }
 }
 return null;
 }

 @Override
 public void beginTraversal(CompilerPass compiler) {
 // no-op
 }

 @Override
 public void endTraversal(CompilerPass compiler) {
 // no-op
 }

 @Override
 public boolean isTraversalStarted() {
 return true;
 }

 @Override
 public boolean isTraversalEnded() {
 return true;
 }
}

class OptimizationWithError implements AbstractPeepholeOptimization {
 @Override
 public Node optimizeSubtree(Node root) {
 throw new AssertionError("This optimization should not be run.");
 }

 @Override
 public void beginTraversal(CompilerPass compiler) {
 // no-op
 }

 @Override
 public void endTraversal(CompilerPass compiler) {
 // no-op
 }

 @Override
 public boolean isTraversalStarted() {
 return true;
 }

 @Override
 public boolean isTraversalEnded() {
 return true;
 }
}