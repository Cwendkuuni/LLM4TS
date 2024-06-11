import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import static org.junit.Assert.*;

public class PeepholeOptimizationsPassTest {

 private AbstractCompiler compiler;
 private AbstractPeepholeOptimization optimization1;
 private AbstractPeepholeOptimization optimization2;
 private CompilerPass peepholeOptimizationsPass;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 optimization1 = mock(AbstractPeepholeOptimization.class);
 optimization2 = mock(AbstractPeepholeOptimization.class);
 peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
 }

 @Test
 public void testEmptyAST() {
 Node root = null;
 Node externs = null;
 peepholeOptimizationsPass.process(externs, root);
 verify(compiler, never()).addChangeHandler(any(CodeChangeHandler.class));
 }

 @Test
 public void testOptimization1() {
 Node root = Node.newIntLiteral(5);
 Node externs = Node.newObjectLiteral();
 when(optimization1.optimizeSubtree(root)).thenReturn(Node.newIntLiteral(10));
 peepholeOptimizationsPass.process(externs, root);
 verify(compiler).addChangeHandler(optimization1);
 verify(optimization1).beginTraversal(compiler);
 verify(optimization1).endTraversal(compiler);
 assertEquals(Node.newIntLiteral(10), root);
 }

 @Test
 public void testOptimization2() {
 Node root = Node.newIntLiteral(5);
 Node externs = Node.newObjectLiteral();
 when(optimization2.optimizeSubtree(root)).thenReturn(Node.newNullLiteral());
 peepholeOptimizationsPass.process(externs, root);
 verify(compiler).addChangeHandler(optimization2);
 verify(optimization2).beginTraversal(compiler);
 verify(optimization2).endTraversal(compiler);
 assertEquals(Node.newNullLiteral(), root);
 }

 @Test
 public void testMultipleOptimizations() {
 Node root = Node.newIntLiteral(5);
 Node externs = Node.newObjectLiteral();
 when(optimization1.optimizeSubtree(root)).thenReturn(Node.newIntLiteral(10));
 when(optimization2.optimizeSubtree(Node.newIntLiteral(10))).thenReturn(Node.newNullLiteral());
 peepholeOptimizationsPass.process(externs, root);
 verify(compiler).addChangeHandler(optimization1);
 verify(optimization1).beginTraversal(compiler);
 verify(optimization1).endTraversal(compiler);
 verify(compiler).addChangeHandler(optimization2);
 verify(optimization2).beginTraversal(compiler);
 verify(optimization2).endTraversal(compiler);
 assertEquals(Node.newNullLiteral(), root);
 }

 @Test(expected = AssertionError.class)
 public void testErrorInOptimization() {
 Node root = Node.newIntLiteral(5);
 Node externs = Node.newObjectLiteral();
 when(optimization1.optimizeSubtree(root)).thenThrow(new AssertionError());
 peepholeOptimizationsPass.process(externs, root);
 }
}