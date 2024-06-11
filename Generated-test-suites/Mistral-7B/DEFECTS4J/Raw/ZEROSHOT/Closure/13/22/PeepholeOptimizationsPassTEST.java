import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeType.FUNCTION;
import static com.google.javascript.jscomp.NodeType.SCRIPT;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassTestUtils;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PeepholeOptimizationsPass.class})
public class PeepholeOptimizationsPassTest {

  private static final Node DUMMY_EXTERNS = CompilerPassTestUtils.createDummyExterns();
  private static final Node DUMMY_ROOT = CompilerPassTestUtils.createDummyRoot();

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;

  @Before
  public void setup() {
    compiler = PowerMockito.mock(AbstractCompiler.class);
    passManager = new CompilerPassManager(compiler);
  }

  @Test
  public void testEmptyPass() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
    pass.process(DUMMY_EXTERNS, DUMMY_ROOT);

    Traversal traversal = compiler.getTraversal();
    assertTrue(traversal.getNumberOfVisitedNodes() == 0);
  }

  @Test
  public void testSinglePass() {
    AbstractPeepholeOptimization optimizer = PowerMockito.mock(AbstractPeepholeOptimization.class);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizer);
    pass.process(DUMMY_EXTERNS, DUMMY_ROOT);

    Traversal traversal = compiler.getTraversal();
    assertTrue(traversal.getNumberOfVisitedNodes() > 0);

    PowerMockito.verifyStatic();
    optimizer.beginTraversal(compiler);
    optimizer.endTraversal(compiler);
  }

  @Test
  public void testMultiplePasses() {
    List<AbstractPeepholeOptimization> optimizers = new ArrayList<>();
    optimizers.addAll(Arrays.asList(
        PowerMockito.mock(AbstractPeepholeOptimization.class),
        PowerMockito.mock(AbstractPeepholeOptimization.class)
    ));

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizers.toArray(new AbstractPeepholeOptimization[0]));
    pass.process(DUMMY_EXTERNS, DUMMY_ROOT);

    Traversal traversal = compiler.getTraversal();
    assertTrue(traversal.getNumberOfVisitedNodes() > 0);

    for (AbstractPeepholeOptimization optimizer : optimizers) {
      PowerMockito.verifyStatic();
      optimizer.beginTraversal(compiler);
      optimizer.endTraversal(compiler);
    }
  }

  @Test
  public void testOptimizationVisit() {
    AbstractPeepholeOptimization optimizer = PowerMockito.mock(AbstractPeepholeOptimization.class);
    Node newNode = PowerMockito.mock(Node.class);

    PowerMockito.when(optimizer.optimizeSubtree(DUMMY_ROOT)).thenReturn(newNode);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizer);
    pass.process(DUMMY_EXTERNS, DUMMY_ROOT);

    Traversal traversal = compiler.getTraversal();
    Traversal.TraversalCallback callback = PowerMockito.mock(Traversal.TraversalCallback.class);

    traversal.enumerate(callback);
    assertTrue(callback.wasVisited(DUMMY_ROOT));
    assertNull(callback.getUserData());

    PowerMockito.verifyStatic();
    optimizer.beginTraversal(compiler);
    optimizer.endTraversal(compiler);
  }

  @Test
  public void testOptimizationChangeHandler() {
    AbstractPeepholeOptimization optimizer = PowerMockito.mock(AbstractPeepholeOptimization.class);
    PeepholeChangeHandler changeHandler = new PeepholeChangeHandler();

    PowerMockito.when(optimizer.optimizeSubtree(DUMMY_ROOT)).thenReturn(null);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizer);

    compiler.addChangeHandler(changeHandler);
    pass.process(DUMMY_EXTERNS, DUMMY_ROOT);
    compiler.removeChangeHandler(changeHandler);

    assertTrue(changeHandler.reportChangeCalls == 0);

    PowerMockito.when(optimizer.optimizeSubtree(DUMMY_ROOT)).thenReturn(DUMMY_ROOT);

    compiler.addChangeHandler(changeHandler);
    pass.process(DUMMY_EXTERNS, DUMMY_ROOT);
    compiler.removeChangeHandler(changeHandler);

    assertTrue(changeHandler.reportChangeCalls == 1);
  }

  @Test
  public void testOptimizationWithScope() {
    AbstractPeepholeOptimization optimizer = PowerMockito.mock(AbstractPeepholeOptimization.class);
    Scope rootScope = PowerMockito.mock(Scope.class);
    Scope childScope = PowerMockito.mock(Scope.class);

    Traversal.TraversalCallback callback = PowerMockito.mock(Traversal.TraversalCallback.class);

    PowerMockito.when(optimizer.optimizeSubtree(DUMMY_ROOT)).thenReturn(DUMMY_ROOT);
    PowerMockito.when(optimizer.optimizeSubtree(PowerMockito.any(Node.class))).thenReturn(null);
    PowerMockito.when(rootScope.getParentScope()).thenReturn(childScope);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizer);
    pass.process(DUMMY_EXTERNS, DUMMY_ROOT);

    traversal.enumerate(callback);
    assertTrue(callback.wasVisited(DUMMY_ROOT));
    assertNull(callback.getUserData());

    PowerMockito.verifyStatic();
    optimizer.beginTraversal(compiler);
    optimizer.endTraversal(compiler);

    PowerMockito.verify(CallbackUtils.getCallbackForScope(childScope)).wasInvoked();
  }

  @Test
  public void testNoRetraversal() {
    AbstractPeepholeOptimization optimizer = PowerMockito.mock(AbstractPeepholeOptimization.class);
    Node newNode = PowerMockito.mock(Node.class);

    PowerMockito.when(optimizer.optimizeSubtree(DUMMY_ROOT)).thenReturn(newNode);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizer);
    pass.process(DUMMY_EXTERNS, DUMMY_ROOT);

    Traversal traversal = compiler.getTraversal();
    Traversal.TraversalCallback callback = PowerMockito.mock(Traversal.TraversalCallback.class);

    traversal.enumerate(callback);
    assertTrue(callback.wasVisited(DUMMY_ROOT));
    assertNull(callback.getUserData());

    PowerMockito.verifyStatic();
    optimizer.beginTraversal(compiler);
    optimizer.endTraversal(compiler);

    PowerMockito.verify(callback, PowerMockito.never()).revisitNode(DUMMY_ROOT);
  }

  @Test
  public void testRetraversal() {
    AbstractPeepholeOptimization optimizer = PowerMockito.mock(AbstractPeepholeOptimization.class);
    Node newNode = PowerMockito.mock(Node.class);

    PowerMockito.when(optimizer.optimizeSubtree(DUMMY_ROOT)).thenReturn(newNode);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizer);

    Traversal traversal = compiler.getTraversal();
    Traversal.TraversalCallback callback = PowerMockito.mock(Traversal.TraversalCallback.class);

    traversal.enumerate(callback);

    PowerMockito.verify(callback).revisitNode(DUMMY_ROOT);

    PowerMockito.verifyStatic();
    optimizer.beginTraversal(compiler);
    optimizer.endTraversal(compiler);
  }

  @Test
  public void testRetraversalWithFunction() {
    AbstractPeepholeOptimization optimizer = PowerMockito.mock(AbstractPeepholeOptimization.class);
    Node functionNode = PowerMockito.mock(Node.class);
    Scope rootScope = PowerMockito.mock(Scope.class);
    Scope childScope = PowerMockito.mock(Scope.class);

    PowerMockito.when(optimizer.optimizeSubtree(DUMMY_ROOT)).thenReturn(null);
    PowerMockito.when(optimizer.optimizeSubtree(functionNode)).thenReturn(functionNode);
    PowerMockito.when(rootScope.getParentScope()).thenReturn(childScope);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizer);

    Traversal traversal = compiler.getTraversal();
    Traversal.TraversalCallback callback = PowerMockito.mock(Traversal.TraversalCallback.class);

    traversal.enumerate(callback);

    PowerMockito.verify(callback).revisitNode(functionNode);

    PowerMockito.verify(callback, PowerMockito.never()).revisitNode(DUMMY_ROOT);

    PowerMockito.verifyStatic();
    optimizer.beginTraversal(compiler);
    optimizer.endTraversal(compiler);

    PowerMockito.verify(CallbackUtils.getCallbackForScope(childScope)).wasInvoked();
  }

  @Test
  public void testRetraversalWithScript() {
    AbstractPeepholeOptimization optimizer = PowerMockito.mock(AbstractPeepholeOptimization.class);
    Node scriptNode = PowerMockito.mock(Node.class);
    Scope rootScope = PowerMockito.mock(Scope.class);
    Scope childScope = PowerMockito.mock(Scope.class);

    PowerMockito.when(optimizer.optimizeSubtree(DUMMY_ROOT)).thenReturn(null);
    PowerMockito.when(optimizer.optimizeSubtree(scriptNode)).thenReturn(scriptNode);
    PowerMockito.when(rootScope.getParentScope()).thenReturn(childScope);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizer);

    Traversal traversal = compiler.getTraversal();
    Traversal.TraversalCallback callback = PowerMockito.mock(Traversal.TraversalCallback.class);

    traversal.enumerate(callback);

    PowerMockito.verify(callback).revisitNode(scriptNode);

    PowerMockito.verify(callback, PowerMockito.never()).revisitNode(DUMMY_ROOT);

    PowerMockito.verifyStatic();
    optimizer.beginTraversal(compiler);
    optimizer.endTraversal(compiler);

    PowerMockito.verify(CallbackUtils.getCallbackForScope(childScope)).wasInvoked();
  }
}

class PeepholeChangeHandler {
  int reportChangeCalls = 0;

  void reportChange() {
    reportChangeCalls++;
  }
}