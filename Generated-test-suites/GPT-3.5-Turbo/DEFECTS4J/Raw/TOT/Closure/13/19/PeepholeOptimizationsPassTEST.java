import com.google.common.collect.Lists;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PeepholeOptimizationsPassTest {

  private PeepholeOptimizationsPass peepholeOptimizationsPass;
  private AbstractCompiler compiler;
  private ScopeState state;
  private StateStack stateStack;

  @Before
  public void setUp() {
    compiler = new Compiler();
    ArrayList<AbstractPeepholeOptimization> peepholeOptimizations = Lists.newArrayList();
    peepholeOptimizations.add(new AbstractPeepholeOptimization() {
      @Override
      public Node optimizeSubtree(Node n) {
        return n;
      }
    });
    peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, peepholeOptimizations.toArray(new AbstractPeepholeOptimization[0]));
    state = new ScopeState();
    stateStack = new StateStack();
  }

  @Test
  public void testProcess_WhenExternsAndRootAreNull_ShouldNotThrowException() {
    Node externs = null;
    Node root = null;
    
    peepholeOptimizationsPass.process(externs, root);
    // No assertion required as the method does not throw any exceptions
  }

  @Test
  public void testProcess_WhenExternsIsNull_ShouldNotThrowException() {
    Node externs = null;
    Node root = new Node(Token.SCRIPT);
    
    peepholeOptimizationsPass.process(externs, root);
    // No assertion required as the method does not throw any exceptions
  }

  @Test
  public void testProcess_WhenRootIsNull_ShouldNotThrowException() {
    Node externs = new Node(Token.SCRIPT);
    Node root = null;
    
    peepholeOptimizationsPass.process(externs, root);
    // No assertion required as the method does not throw any exceptions
  }

  @Test
  public void testProcess_WhenExternsAndRootAreNotEmpty_ShouldNotThrowException() {
    Node externs = new Node(Token.SCRIPT);
    Node root = new Node(Token.SCRIPT);
    
    peepholeOptimizationsPass.process(externs, root);
    // No assertion required as the method does not throw any exceptions
  }

  @Test
  public void testGetCompiler_ShouldReturnCorrectCompiler() {
    AbstractCompiler expectedCompiler = compiler;
    
    AbstractCompiler actualCompiler = peepholeOptimizationsPass.getCompiler();
    
    assertEquals(expectedCompiler, actualCompiler);
  }

  @Test
  public void testPeepholeChangeHandler_ShouldReportChange() {
    PeepholeOptimizationsPass.PeepholeChangeHandler handler = peepholeOptimizationsPass.new PeepholeChangeHandler();

    handler.reportChange();
    assertTrue(peepholeOptimizationsPass.traversalState.peek().changed);
  }

  @Test
  public void testPeepholeChangeHandler_ShouldNotReportChangeWhenStateIsNotChanged() {
    PeepholeOptimizationsPass.PeepholeChangeHandler handler = peepholeOptimizationsPass.new PeepholeChangeHandler();
    
    assertFalse(peepholeOptimizationsPass.traversalState.peek().changed);
  }

  @Test
  public void testShouldRetraverse_WhenNodeParentIsNull_ShouldReturnFalse() {
    Node node = new Node(Token.EMPTY);
    
    boolean shouldRetraverse = peepholeOptimizationsPass.shouldRetraverse(node);
    
    assertFalse(shouldRetraverse);
  }

  @Test
  public void testShouldRetraverse_WhenNodeParentIsNotNullAndNodeIsNotFunctionOrScript_ShouldReturnFalse() {
    Node node = new Node(Token.EMPTY);
    node.setParent(new Node(Token.SCRIPT));
    
    boolean shouldRetraverse = peepholeOptimizationsPass.shouldRetraverse(node);
    
    assertFalse(shouldRetraverse);
  }

  @Test
  public void testShouldRetraverse_WhenNodeParentIsNotNullAndNodeIsFunctionOrScriptAndStateChanged_ShouldReturnTrue() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    peepholeOptimizationsPass.traversalState.peek().changed = true;
    
    boolean shouldRetraverse = peepholeOptimizationsPass.shouldRetraverse(node);
    
    assertTrue(shouldRetraverse);
  }

  @Test
  public void testShouldRetraverse_WhenNodeParentIsNotNullAndNodeIsFunctionOrScriptAndStateUnchanged_ShouldReturnFalse() {
    Node node = new Node(Token.FUNCTION);
    node.setParent(new Node(Token.SCRIPT));
    
    boolean shouldRetraverse = peepholeOptimizationsPass.shouldRetraverse(node);
    
    assertFalse(shouldRetraverse);
  }

  @Test
  public void testShouldVisit_WhenNodeIsNotFunctionOrScript_ShouldReturnTrue() {
    Node node = new Node(Token.EMPTY);
    
    boolean shouldVisit = peepholeOptimizationsPass.shouldVisit(node);
    
    assertTrue(shouldVisit);
  }

  @Test
  public void testShouldVisit_WhenNodeIsFunctionOrScriptAndStateTraverseChildScopesIsFalse_ShouldReturnFalse() {
    Node node = new Node(Token.SCRIPT);
    peepholeOptimizationsPass.traversalState.peek().traverseChildScopes = false;
    
    boolean shouldVisit = peepholeOptimizationsPass.shouldVisit(node);
    
    assertFalse(shouldVisit);
  }

  @Test
  public void testShouldVisit_WhenNodeIsFunctionOrScriptAndStateTraverseChildScopesIsTrue_ShouldReturnTrue() {
    Node node = new Node(Token.SCRIPT);
    peepholeOptimizationsPass.traversalState.peek().traverseChildScopes = true;
    
    boolean shouldVisit = peepholeOptimizationsPass.shouldVisit(node);
    
    assertTrue(shouldVisit);
  }

  @Test
  public void testExitNode_WhenNodeIsNotFunctionOrScript_ShouldPopFromStateStack() {
    Node node = new Node(Token.EMPTY);
    stateStack.push();
    
    peepholeOptimizationsPass.exitNode(node);
    
    assertEquals(0, stateStack.currentDepth);
  }

  @Test
  public void testExitNode_WhenNodeIsFunctionOrScript_ShouldPopFromStateStack() {
    Node node = new Node(Token.FUNCTION);
    stateStack.push();
    
    peepholeOptimizationsPass.exitNode(node);
    
    assertEquals(0, stateStack.currentDepth);
  }

  @Test
  public void testVisit_WhenNodeIsNull_ShouldNotThrowException() {
    Node node = null;
    
    peepholeOptimizationsPass.visit(node);
    // No assertion required as the method does not throw any exceptions
  }

  @Test
  public void testVisit_WhenNodeIsCannotBeOptimized_ShouldNotThrowException() {
    Node node = new Node(Token.ADD, new Node(Token.NAME, new Node(Token.STRING, "Hello")),
                                          new Node(Token.NAME, new Node(Token.STRING, "World")));
    
    peepholeOptimizationsPass.visit(node);
    // No assertion required as the method does not throw any exceptions
  }

  @Test
  public void testBeginTraversal_ShouldCallBeginTraversalForAllOptimizations() {
    AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
      @Override
      public void beginTraversal(AbstractCompiler compiler) {
        state.visited = true;
      }
    };
    ArrayList<AbstractPeepholeOptimization> peepholeOptimizations = Lists.newArrayList();
    peepholeOptimizations.add(optimization);
    peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, peepholeOptimizations.toArray(new AbstractPeepholeOptimization[0]));

    peepholeOptimizationsPass.beginTraversal();

    assertTrue(state.visited);
  }

  @Test
  public void testEndTraversal_ShouldCallEndTraversalForAllOptimizations() {
    AbstractPeepholeOptimization optimization = new AbstractPeepholeOptimization() {
      @Override
      public void endTraversal(AbstractCompiler compiler) {
        state.visited = true;
      }
    };
    ArrayList<AbstractPeepholeOptimization> peepholeOptimizations = Lists.newArrayList();
    peepholeOptimizations.add(optimization);
    peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, peepholeOptimizations.toArray(new AbstractPeepholeOptimization[0]));

    peepholeOptimizationsPass.endTraversal();

    assertTrue(state.visited);
  }

  private class ScopeState {
    boolean changed;
    boolean traverseChildScopes;
    boolean visited;
    ScopeState() {
      reset();
    }

    void reset() {
      changed = false;
      traverseChildScopes = true;
      visited = false;
    }
  }

  private class StateStack {
    private ArrayList<ScopeState> states = Lists.newArrayList();
    private int currentDepth = 0;

    StateStack() {
      states.add(new ScopeState());
    }

    ScopeState peek() {
      return states.get(currentDepth);
    }

    void push() {
      currentDepth++;
      if (states.size() <= currentDepth) {
        states.add(new ScopeState());
      } else {
        states.get(currentDepth).reset();
      }
    }

    void pop() {
      currentDepth--;
    }
  }
}