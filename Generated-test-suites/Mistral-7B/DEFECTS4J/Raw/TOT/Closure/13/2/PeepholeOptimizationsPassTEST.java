import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.verify;

public class PeepholeOptimizationsPassTest {

 private AbstractCompiler compiler;
 private AbstractPeepholeOptimization optimization1;
 private AbstractPeepholeOptimization optimization2;
 private CompilerPass pass;

 @Before
 public void setUp() {
 initMocks(this);
 compiler = mock(AbstractCompiler.class);
 optimization1 = mock(AbstractPeepholeOptimization.class);
 optimization2 = mock(AbstractPeepholeOptimization.class);

 List<AbstractPeepholeOptimization> optimizations = Arrays.asList(optimization1, optimization2);
 pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
 }

 @Test
 public void testProcess() {
 Node rootNode = createNode("root");
 Node nodeToBeChanged = createNode("nodeToBeChanged");
 nodeToBeChanged.setFirstChild(createNode("grandChild"));
 rootNode.setFirstChild(nodeToBeChanged);

 when(compiler.getOptions()).thenReturn(new CompilerOptions());
 when(compiler.addChangeHandler(any(CodeChangeHandler.class))).thenReturn(null);
 when(compiler.removeChangeHandler(any(CodeChangeHandler.class))).thenReturn(null);
 when(optimization1.beginTraversal(compiler)).thenReturn(null);
 when(optimization2.beginTraversal(compiler)).thenReturn(null);
 when(optimization1.endTraversal(compiler)).thenReturn(null);
 when(optimization2.endTraversal(compiler)).thenReturn(null);

 pass.process(null, rootNode);

 verify(optimization1).optimizeSubtree(nodeToBeChanged);
 verify(optimization2).optimizeSubtree(nodeToBeChanged);
 }

 @Test
 public void testShouldVisitShouldReturnTrueForFunctionOrScriptNode() {
 Node rootNode = createNode("root");
 Node nodeToBeVisited = createNode("nodeToBeVisited");
 nodeToBeVisited.setIsFunction(true);
 rootNode.setFirstChild(nodeToBeVisited);

 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null, null);

 assertTrue(pass.shouldVisit(nodeToBeVisited));
 }

 @Test
 public void testShouldVisitShouldReturnFalseForNonFunctionOrScriptNode() {
 Node rootNode = createNode("root");
 Node nodeToBeVisited = createNode("nodeToBeVisited");
 rootNode.setFirstChild(nodeToBeVisited);

 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null, null);

 assertFalse(pass.shouldVisit(nodeToBeVisited));
 }

 @Test
 public void testShouldRetraverseShouldReturnTrueForFunctionOrScriptNodeWithChangedParent() {
 Node parentNode = createNode("parentNode");
 Node rootNode = createNode("root");
 Node nodeToBeRetraversed = createNode("nodeToBeRetraversed");
 parentNode.setFirstChild(nodeToBeRetraversed);
 nodeToBeRetraversed.setFirstChild(createNode("grandChild"));
 rootNode.setFirstChild(parentNode);

 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null, null);
 pass.traversalState.push();
 pass.traversalState.peek().changed = true;
 pass.traverse(rootNode);

 assertTrue(pass.traversalState.peek().traverseChildScopes);
 }

 @Test
 public void testShouldRetraverseShouldReturnFalseForNonFunctionOrScriptNodeOrUnchangedParent() {
 Node parentNode = createNode("parentNode");
 Node rootNode = createNode("root");
 Node nodeToBeRetraversed = createNode("nodeToBeRetraversed");
 parentNode.setFirstChild(nodeToBeRetraversed);
 nodeToBeRetraversed.setFirstChild(createNode("grandChild"));
 rootNode.setFirstChild(parentNode);

 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null, null);
 pass.traversalState.push();
 pass.traversalState.peek().changed = false;
 pass.traverse(rootNode);

 assertFalse(pass.traversalState.peek().traverseChildScopes);
 }

 @Test
 public void testExitNodeShouldPopStateWhenLeavingFunctionOrScriptNode() {
 Node rootNode = createNode("root");
 Node nodeToBeExited = createNode("nodeToBeExited");
 nodeToBeExited.setIsFunction(true);
 rootNode.setFirstChild(nodeToBeExited);

 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null, null);
 pass.traversalState.push();
 pass.traversalState.peek().changed = true;
 pass.exitNode(nodeToBeExited);

 assertFalse(pass.traversalState.peek().changed);
 assertFalse(pass.traversalState.peek().traverseChildScopes);
 assertFalse(pass.traversalState.size() > 1);
 }

 @Test
 public void testVisitShouldOptimizeSubTreeForAllPeepholeOptimizations() {
 Node rootNode = createNode("root");
 Node nodeToBeOptimized = createNode("nodeToBeOptimized");
 rootNode.setFirstChild(nodeToBeOptimized);

 PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null, optimization1, optimization2);
 Node optimizedNode = pass.visit(nodeToBeOptimized);

 assertNotNull(optimizedNode);
 }

 private Node createNode(String name) {
 return new Node(Node.NEW_EXPR, Lists.newArrayList(), null, null, name);
 }
}