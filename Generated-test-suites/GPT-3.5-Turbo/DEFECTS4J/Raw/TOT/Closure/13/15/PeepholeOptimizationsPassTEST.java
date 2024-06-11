import static org.junit.Assert.*;

import com.google.common.collect.ImmutableSet;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;


public class PeepholeOptimizationsPassTest {

  private PeepholeOptimizationsPass peepholeOptimizationsPass;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[] {};
    peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimizations);
  }

  @Test
  public void testProcess_withEmptyExternsAndRoot_expectNoChange() {
    Node externs = new Node(Node.Type.EXTERN_EXPR);
    Node root = new Node(Node.Type.ROOT);
    peepholeOptimizationsPass.process(externs, root);
    assertFalse(peepholeOptimizationsPass.getCompiler().hasCodeChanged());
  }

  @Test
  public void testProcess_withEmptyExternsAndRootAndOneOptimization_expectChange() {
    AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        return node;
      }
    };
    Node externs = new Node(Node.Type.EXTERN_EXPR);
    Node root = new Node(Node.Type.ROOT);
    peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimization);
    peepholeOptimizationsPass.process(externs, root);
    assertTrue(peepholeOptimizationsPass.getCompiler().hasCodeChanged());
  }

  @Test
  public void testProcess_withEmptyExternsAndRootAndMultipleOptimizations_expectChange() {
    AbstractPeepholeOptimization optimization1 = new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        return node;
      }
    };
    AbstractPeepholeOptimization optimization2 = new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        return node;
      }
    };
    AbstractPeepholeOptimization optimization3 = new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        return node;
      }
    };
    Node externs = new Node(Node.Type.EXTERN_EXPR);
    Node root = new Node(Node.Type.ROOT);
    peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2, optimization3);
    peepholeOptimizationsPass.process(externs, root);
    assertTrue(peepholeOptimizationsPass.getCompiler().hasCodeChanged());
  }

  @Test
  public void testShouldRetraverse_withNodeParentIsNotNullAndNodeIsFunction_expectTrue() {
    Node node = new Node(Node.Type.FUNCTION);
    Node parent = new Node(Node.Type.SCRIPT);
    node.setParent(parent);
    assertTrue(peepholeOptimizationsPass.shouldRetraverse(node));
  }

  @Test
  public void testShouldRetraverse_withNodeParentIsNotNullAndNodeIsScript_expectTrue() {
    Node node = new Node(Node.Type.SCRIPT);
    Node parent = new Node(Node.Type.SCRIPT);
    node.setParent(parent);
    assertTrue(peepholeOptimizationsPass.shouldRetraverse(node));
  }

  @Test
  public void testShouldRetraverse_withNodeParentIsNotNullAndNodeIsNotFunctionOrScript_expectFalse() {
    Node node = new Node(Node.Type.VAR);
    Node parent = new Node(Node.Type.SCRIPT);
    node.setParent(parent);
    assertFalse(peepholeOptimizationsPass.shouldRetraverse(node));
  }

  @Test
  public void testShouldVisit_withNodeIsFunctionAndPreviousScopeTraverseChildScopesIsFalse_expectFalse() {
    peepholeOptimizationsPass.traversalState.peek().traverseChildScopes = false;
    Node node = new Node(Node.Type.FUNCTION);
    assertFalse(peepholeOptimizationsPass.shouldVisit(node));
  }

  @Test
  public void testShouldVisit_withNodeIsScriptAndPreviousScopeTraverseChildScopesIsFalse_expectFalse() {
    peepholeOptimizationsPass.traversalState.peek().traverseChildScopes = false;
    Node node = new Node(Node.Type.SCRIPT);
    assertFalse(peepholeOptimizationsPass.shouldVisit(node));
  }

  @Test
  public void testShouldVisit_withNodeIsFunctionAndPreviousScopeTraverseChildScopesIsTrue_expectTrue() {
    peepholeOptimizationsPass.traversalState.peek().traverseChildScopes = true;
    Node node = new Node(Node.Type.FUNCTION);
    assertTrue(peepholeOptimizationsPass.shouldVisit(node));
  }

  @Test
  public void testShouldVisit_withNodeIsScriptAndPreviousScopeTraverseChildScopesIsTrue_expectTrue() {
    peepholeOptimizationsPass.traversalState.peek().traverseChildScopes = true;
    Node node = new Node(Node.Type.SCRIPT);
    assertTrue(peepholeOptimizationsPass.shouldVisit(node));
  }

  @Test
  public void testExitNode_withNodeIsFunction_expectPopTraversalState() {
    Node node = new Node(Node.Type.FUNCTION);
    peepholeOptimizationsPass.exitNode(node);
    assertEquals(0, peepholeOptimizationsPass.traversalState.currentDepth);
  }

  @Test
  public void testExitNode_withNodeIsScript_expectPopTraversalState() {
    Node node = new Node(Node.Type.SCRIPT);
    peepholeOptimizationsPass.exitNode(node);
    assertEquals(0, peepholeOptimizationsPass.traversalState.currentDepth);
  }

  @Test
  public void testVisit_withCurrentVersionOfNodeIsNull_expectNoChange() {
    Node node = new Node(Node.Type.SCRIPT);
    boolean somethingChanged = false;
    Node currentVersionOfNode = null;
    int iterationCount = 0;

    do {
      somethingChanged = false;

      if (iterationCount == 0) {
        assertEquals(node, currentVersionOfNode);
      } else {
        assertEquals(node.getChildAtIndex(0), currentVersionOfNode);
      }

      iterationCount++;
    } while(somethingChanged);

    assertEquals(1, iterationCount);
  }

  @Test
  public void testVisit_withCurrentVersionOfNodeIsNotNullAndNewVersionOfNodeIsNull_expectNoChange() {
    Node node = new Node(Node.Type.VAR);
    boolean somethingChanged = false;
    Node currentVersionOfNode = node;
    int iterationCount = 0;

    do {
      somethingChanged = false;

      if (iterationCount == 0) {
        assertEquals(node, currentVersionOfNode);
      } else {
        assertNull(currentVersionOfNode);
        somethingChanged = false;
        break;
      }

      iterationCount++;

    } while(somethingChanged);

    assertEquals(1, iterationCount);
  }

  @Test
  public void testVisit_withCurrentVersionOfNodeIsNotNullAndNewVersionOfNodeIsNotNull_expectChange() {
    Node node = new Node(Node.Type.VAR);
    boolean somethingChanged = false;
    Node currentVersionOfNode = node;
    int iterationCount = 0;

    do {
      somethingChanged = false;

      if (iterationCount == 0) {
        assertEquals(node, currentVersionOfNode);
      } else if (iterationCount == 1) {
        assertEquals(node, currentVersionOfNode);
      } else if (iterationCount == 2) {
        assertEquals(node, currentVersionOfNode);
      }

      iterationCount++;

    } while(somethingChanged);

    assertEquals(3, iterationCount);
  }

  @Test
  public void testBeginTraversal_withOneOptimization_expectOptimizationBeginTraversal() {
    AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        return node;
      }

      @Override
      void beginTraversal(AbstractCompiler compiler) {
        assertTrue(compiler == PeepholeOptimizationsPassTest.this.compiler);
      }
    };
    peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimization);
    peepholeOptimizationsPass.beginTraversal();
  }

  @Test
  public void testEndTraversal_withOneOptimization_expectOptimizationEndTraversal() {
    AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node node) {
        return node;
      }

      @Override
      void endTraversal(AbstractCompiler compiler) {
        assertTrue(compiler == PeepholeOptimizationsPassTest.this.compiler);
      }
    };
    peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimization);
    peepholeOptimizationsPass.endTraversal();
  }

}