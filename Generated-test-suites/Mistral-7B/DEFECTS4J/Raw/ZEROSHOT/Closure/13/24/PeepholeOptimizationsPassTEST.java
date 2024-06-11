import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPasses;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PeepholeOptimizationsPassTest {

  private AbstractCompiler compiler;
  private AbstractPeepholeOptimization optimizationMock1;
  private AbstractPeepholeOptimization optimizationMock2;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);

    optimizationMock1 = mock(AbstractPeepholeOptimization.class);
    optimizationMock2 = mock(AbstractPeepholeOptimization.class);
  }

  @Test
  public void testPeepholeOptimizationsPassCreation() {
    List<AbstractPeepholeOptimization> optimizations = Arrays.asList(optimizationMock1, optimizationMock2);
    CompilerPass peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimizations.toArray(new AbstractPeepholeOptimization[0]));
    assertNotNull(peepholeOptimizationsPass);
  }

  @Test
  public void testProcessNode_shouldVisitFunctionOrScript_true() {
    CompilerPass peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimizationMock1, optimizationMock2);
    Node node = mock(Node.class);
    assertFalse(peepholeOptimizationsPass.shouldVisit(node));

    node = mock(Node.class);
    node.setIsFunction(true);
    assertTrue(peepholeOptimizationsPass.shouldVisit(node));

    node = mock(Node.class);
    node.setIsScript(true);
    assertTrue(peepholeOptimizationsPass.shouldVisit(node));
  }

  @Test
  public void testProcessNode_shouldVisitFunctionOrScript_false() {
    CompilerPass peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimizationMock1, optimizationMock2);
    Node node = mock(Node.class);
    node.setIsFunction(false);
    node.setIsScript(false);
    assertFalse(peepholeOptimizationsPass.shouldVisit(node));
  }

  @Test
  public void testProcessNode_shouldRetraverse_true() {
    CompilerPass peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimizationMock1, optimizationMock2);
    Node node = mock(Node.class);
    node.setIsFunction(true);
    ScopeState traversalState = peepholeOptimizationsPass.traversalState.peek();
    traversalState.traverseChildScopes = false;
    assertTrue(peepholeOptimizationsPass.shouldRetraverse(node));
  }

  @Test
  public void testProcessNode_shouldRetraverse_false() {
    CompilerPass peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimizationMock1, optimizationMock2);
    Node node = mock(Node.class);
    node.setIsFunction(true);
    ScopeState traversalState = peepholeOptimizationsPass.traversalState.peek();
    traversalState.changed = true;
    assertFalse(peepholeOptimizationsPass.shouldRetraverse(node));
  }

  @Test
  public void testTraverse_shouldNotTraverse_null() {
    CompilerPass peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimizationMock1, optimizationMock2);
    Node node = null;
    peepholeOptimizationsPass.traverse(node);
  }

  @Test
  public void testTraverse_shouldVisitChildren() {
    CompilerPass peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimizationMock1, optimizationMock2);
    Node root = mock(Node.class);
    Node child = mock(Node.class);

    pepeholeOptimizationsPass.traverse(root);
    verify(optimizationMock1).optimizeSubtree(child);
    verify(optimizationMock2).optimizeSubtree(child);
  }

  @Test
  public void testProcess_shouldCallBeginTraversal() {
    CompilerPass peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimizationMock1, optimizationMock2);
    Node root = mock(Node.class);
    peepholeOptimizationsPass.process(null, root);
    verify(optimizationMock1).beginTraversal(compiler);
    verify(optimizationMock2).beginTraversal(compiler);
  }

  @Test
  public void testProcess_shouldCallEndTraversal() {
    CompilerPass peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimizationMock1, optimizationMock2);
    Node root = mock(Node.class);
    peepholeOptimizationsPass.process(null, root);
    verify(optimizationMock1).endTraversal(compiler);
    verify(optimizationMock2).endTraversal(compiler);
  }

  @Test
  public void testProcess_shouldCallVisitor() {
    CompilerPass peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimizationMock1, optimizationMock2);
    Node root = mock(Node.class);
    peepholeOptimizationsPass.process(null, root);
    Node node = mock(Node.class);
    peepholeOptimizationsPass.visit(node);
    verify(optimizationMock1).optimizeSubtree(node);
    verify(optimizationMock2).optimizeSubtree(node);
  }

  @Test
  public void testProcess_shouldNotProcessNullNode() {
    CompilerPass peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimizationMock1, optimizationMock2);
    Node root = null;
    peepholeOptimizationsPass.process(null, root);
    assertNull(compiler.getChangeHandler());
  }
}