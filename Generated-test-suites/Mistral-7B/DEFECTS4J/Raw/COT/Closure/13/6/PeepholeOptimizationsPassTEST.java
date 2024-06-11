import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import org.junit.Test;

public class PeepholeOptimizationsPassTest {
  

  @Test
  public void testPeepholeOptimizationsPass_NullCompiler() {
    assertNull(new PeepholeOptimizationsPass(null, new AbstractPeepholeOptimization[0]));
  }

  @Test
  public void testPeepholeOptimizationsPass_EmptyOptimizations() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
    assertSame(compiler, pass.getCompiler());
    assertArrayEquals(new AbstractPeepholeOptimization[0], pass.peepholeOptimizations);
  }

  @Test
  public void testPeepholeOptimizationsPass_ValidConstruction() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    AbstractPeepholeOptimization optimization1 = mock(AbstractPeepholeOptimization.class);
    AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
    assertSame(compiler, pass.getCompiler());
    assertArrayEquals(new AbstractPeepholeOptimization[]{optimization1, optimization2}, pass.peepholeOptimizations);
  }

  @Test
  public void testProcess_NullExterns() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
    assertNull(pass.process(null, null));
  }

  @Test
  public void testProcess_NullRoot() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
    assertNull(pass.process(new Node(), null));
  }

  @Test
  public void testProcess_EmptyAST() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
    Node emptyAST = new Node();
    assertNull(pass.process(emptyAST, new Node()));
  }

  @Test
  public void testTraverse_NullNode() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
    assertFalse(pass.shouldVisit(null));
  }

  @Test
  public void testShouldRetraverse_TraversingFunctionNode() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
    Node functionNode = new Node();
    ScopeState previousState = new ScopeState();
    traversalState.peek = () -> previousState;
    traversalState.push = (ignored) -> {};
    traversalState.pop = () -> {};
    assertTrue(pass.shouldRetraverse(functionNode));
  }

  @Test
  public void testShouldVisit_TraversingFunctionNodeWithFalseTraverseChildScopes() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
    Node functionNode = new Node();
    ScopeState previousState = new ScopeState();
    traversalState.peek = () -> previousState;
    traversalState.push = (ignored) -> {};
    traversalState.pop = () -> {};
    previousState.traverseChildScopes = false;
    assertFalse(pass.shouldVisit(functionNode));
  }

  @Test
  public void testShouldVisit_TraversingScriptNode() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
    Node scriptNode = new Node();
    ScopeState previousState = new ScopeState();
    traversalState.peek = () -> previousState;
    traversalState.push = (ignored) -> {};
    traversalState.pop = () -> {};
    assertTrue(pass.shouldVisit(scriptNode));
  }

  @Test
  public void testVisit_NoOptimizationApplied() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
    Node node = new Node();
    pass.visit(node);
    assertSame(node, pass.currentVersionOfNode);
  }

  @Test
  public void testVisit_OptimizationAppliedOnce() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        return new Node();
      }
    });
    Node node = new Node();
    Node newNode = new Node();
    pass.currentVersionOfNode = node;
    Node optimizedNode = pass.optimizeSubtree(node);
    assertNotSame(node, optimizedNode);
    assertSame(optimizedNode, pass.currentVersionOfNode);
  }

  @Test
  public void testVisit_OptimizationAppliedMultipleTimes() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        return new Node();
      }
    });
    Node node = new Node();
    Node newNode = new Node();
    pass.currentVersionOfNode = node;

    Node optimizedNode1 = pass.optimizeSubtree(node);
    assertNotSame(node, optimizedNode1);
    assertSame(optimizedNode1, pass.currentVersionOfNode);

    Node optimizedNode2 = pass.optimizeSubtree(optimizedNode1);
    assertNotSame(optimizedNode1, optimizedNode2);
    assertSame(optimizedNode2, pass.currentVersionOfNode);
  }

  @Test(expected = IllegalStateException.class)
  public void testVisit_TooManyIterations() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
    Node node = new Node();
    for (int i = 0; i < 10000; i++) {
      pass.visit(node);
    }
  }

  @Test
  public void testBeginTraversal() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
    for (AbstractPeepholeOptimization optimization : pass.peepholeOptimizations) {
      optimization.beginTraversal(compiler);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBeginTraversal_NullCompiler() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(null, new AbstractPeepholeOptimization[0]);
    optimization.beginTraversal(pass.getCompiler());
  }

  @Test
  public void testEndTraversal() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
    for (AbstractPeepholeOptimization optimization : pass.peepholeOptimizations) {
      optimization.endTraversal(compiler);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEndTraversal_NullCompiler() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, new AbstractPeepholeOptimization[0]);
    optimization.endTraversal(null);
  }

  
}