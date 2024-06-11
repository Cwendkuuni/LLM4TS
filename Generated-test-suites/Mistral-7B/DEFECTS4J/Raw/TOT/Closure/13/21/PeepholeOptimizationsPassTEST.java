import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeChangeHandler;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.rhino.NodeFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PeepholeOptimizationsPassTest {

 private AbstractCompiler compiler;
 private NodeFactory nodeFactory;
 private List<AbstractPeepholeOptimization> optimizations;
 private PeepholeOptimizationsPass pass;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 nodeFactory = mock(NodeFactory.class);
 optimizations = Arrays.asList(new ConstantFoldingOptimization(), new DeadCodeRemovalOptimization());
 pass = new PeepholeOptimizationsPass(compiler, optimizations.toArray(new AbstractPeepholeOptimization[0]));
 }

 @Test
 public void testEmptyAST() {
 Node root = nodeFactory.createScript("", 0);
 pass.process(Collections.emptyList(), root);
 verifyNoMoreInteractions(compiler);
 }

 @Test
 public void testSimpleVariableAssignment() {
 Node root = createNode("var x = 5;");
 pass.process(Collections.emptyList(), root);
 verify(compiler, times(1)).addChangeHandler(any(PeepholeChangeHandler.class));
 verifyNoMoreInteractions(compiler);
 }

 @Test
 public void testConstantFolding() {
 Node root = createNode("var x = 5; var y = x + 1; var z = x + 1;");
 Node expectedRoot = createNode("var x = 5; var y = 6; var z = 6;");
 pass.process(Collections.emptyList(), root);
 verify(compiler).addChangeHandler(any(PeepholeChangeHandler.class));
 verify(compiler).removeChangeHandler(any(PeepholeChangeHandler.class));
 verify(compiler).setRootNode(expectedRoot);
 }

 @Test
 public void testDeadCodeRemoval() {
 Node root = createNode("var x = 5; var y = x + 1; //comment var z = x + 1;");
 Node expectedRoot = createNode("var x = 5; var y = x + 1;");
 pass.process(Collections.emptyList(), root);
 verify(compiler).addChangeHandler(any(PeepholeChangeHandler.class));
 verify(compiler).removeChangeHandler(any(PeepholeChangeHandler.class));
 verify(compiler).setRootNode(expectedRoot);
 }

 @Test
 public void testDeadCodeRemovalWithFunction() {
 Node root = createNode("function foo() { var x = 5; var y = x + 1; //comment var z = x + 1; }");
 Node expectedRoot = createNode("function foo() { var x = 5; var y = x + 1; }");
 pass.process(Collections.emptyList(), root);
 verify(compiler).addChangeHandler(any(PeepholeChangeHandler.class));
 verify(compiler).removeChangeHandler(any(PeepholeChangeHandler.class));
 verify(compiler).setRootNode(expectedRoot);
 }

 @Test(expected = AssertionError.class)
 public void testDuplicateOptimization() {
 optimizations.add(new ConstantFoldingOptimization());
 optimizations.add(new ConstantFoldingOptimization());
 pass = new PeepholeOptimizationsPass(compiler, optimizations.toArray(new AbstractPeepholeOptimization[0]));
 Node root = createNode("var x = 5; var y = x + 1;");
 pass.process(Collections.emptyList(), root);
 }

 @Test
 public void testErrorDuringOptimization() {
 doReturn(new Error("Boom")).when(compiler).addChangeHandler(any(PeepholeChangeHandler.class));
 Node root = createNode("var x = 5;");
 pass.process(Collections.emptyList(), root);
 verify(compiler).addChangeHandler(any(PeepholeChangeHandler.class));
 verifyThrows(Error.class, () -> pass.process(Collections.emptyList(), root));
 }

 private Node createNode(String script) {
 Node node = nodeFactory.createScript(script, 0);
 return nodeFactory.createScript(script, 0).getFirstChild();
 }

 private void verifyNoMoreInteractions(AbstractCompiler compiler) {
 verify(compiler, never()).addChangeHandler(any(PeepholeChangeHandler.class));
 verify(compiler, never()).removeChangeHandler(any(PeepholeChangeHandler.class));
 }

 private void verify(AbstractCompiler compiler, int times) {
 verify(compiler, times(times)).addChangeHandler(any(PeepholeChangeHandler.class));
 }
}

class ConstantFoldingOptimization implements AbstractPeepholeOptimization {
 @Override
 public Node optimizeSubtree(Node node) {
 // Constant folding implementation here
 return node;
 }

 @Override
 public void beginTraversal(AbstractCompiler compiler) {
 // Empty
 }

 @Override
 public void endTraversal(AbstractCompiler compiler) {
 // Empty
 }
}

class DeadCodeRemovalOptimization implements AbstractPeepholeOptimization {
 @Override
 public Node optimizeSubtree(Node node) {
 // Dead code removal implementation here
 return node;
 }

 @Override
 public void beginTraversal(AbstractCompiler compiler) {
 // Empty
 }

 @Override
 public void endTraversal(AbstractCompiler compiler) {
 // Empty
 }
}