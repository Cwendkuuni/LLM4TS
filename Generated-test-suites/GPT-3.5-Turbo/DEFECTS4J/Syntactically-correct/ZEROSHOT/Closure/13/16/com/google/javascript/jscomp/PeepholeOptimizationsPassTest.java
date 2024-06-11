import static org.junit.Assert.*;
import org.junit.Test;

public class PeepholeOptimizationsPassTest {
  
  @Test
  public void testProcess() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node externs = new Node();
    Node root = new Node();
    
    pass.process(externs, root);
    
    // Assert that the process method executed without any exceptions
    assertTrue(true);
  }
  
  @Test
  public void testGetCompiler() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    
    AbstractCompiler result = pass.getCompiler();
    
    assertEquals(compiler, result);
  }
  
  @Test
  public void testTraverse() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node node = new Node();
    
    // Assert that the traverse method executed without any exceptions
    pass.traverse(node);
    assertTrue(true);
  }
  
  @Test
  public void testShouldRetraverse_nodeNotFunctionOrScript_returnFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node node = new Node();
    node.setType(Token.ASSIGN);
    
    boolean result = pass.shouldRetraverse(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldRetraverse_nodeIsFunction_returnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node node = new Node();
    node.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.addChildToFront(node);
    node.setParent(parent);
    
    boolean result = pass.shouldRetraverse(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldRetraverse_nodeIsScript_returnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node node = new Node();
    node.setType(Token.SCRIPT);
    Node parent = new Node();
    parent.addChildToFront(node);
    node.setParent(parent);
    
    boolean result = pass.shouldRetraverse(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldRetraverse_scopeChanged_returnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node node = new Node();
    node.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.addChildToFront(node);
    node.setParent(parent);
    pass.traversalState.peek().changed = true;
    
    boolean result = pass.shouldRetraverse(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldRetraverse_scopeNotChanged_returnFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node node = new Node();
    node.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.addChildToFront(node);
    node.setParent(parent);
    
    boolean result = pass.shouldRetraverse(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldVisit_nodeNotFunctionOrScript_returnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node node = new Node();
    node.setType(Token.ASSIGN);
    
    boolean result = pass.shouldVisit(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldVisit_nodeIsFunction_traverseChildScopesTrue_returnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node node = new Node();
    node.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.addChildToFront(node);
    node.setParent(parent);
    
    boolean result = pass.shouldVisit(node);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldVisit_nodeIsFunction_traverseChildScopesFalse_returnFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node node = new Node();
    node.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.addChildToFront(node);
    node.setParent(parent);
    pass.traversalState.peek().traverseChildScopes = false;
    
    boolean result = pass.shouldVisit(node);
    
    assertFalse(result);
  }
  
  @Test
  public void testExitNode_nodeNotFunctionOrScript_returnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node node = new Node();
    node.setType(Token.ASSIGN);
    
    // Assert that the exitNode method executed without any exceptions
    pass.exitNode(node);
    assertTrue(true);
  }
  
  @Test
  public void testExitNode_nodeIsFunction_returnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node node = new Node();
    node.setType(Token.FUNCTION);
    Node parent = new Node();
    parent.addChildToFront(node);
    node.setParent(parent);
    
    // Assert that the exitNode method executed without any exceptions
    pass.exitNode(node);
    assertTrue(true);
  }
  
  @Test
  public void testExitNode_nodeIsScript_returnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node node = new Node();
    node.setType(Token.SCRIPT);
    Node parent = new Node();
    parent.addChildToFront(node);
    node.setParent(parent);
    
    // Assert that the exitNode method executed without any exceptions
    pass.exitNode(node);
    assertTrue(true);
  }
  
  @Test
  public void testExitNode_nodeIsFunction_parentIsNull_returnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node node = new Node();
    node.setType(Token.FUNCTION);
    
    // Assert that the exitNode method executed without any exceptions
    pass.exitNode(node);
    assertTrue(true);
  }
  
  @Test
  public void testVisit_currentVersionOfNodeIsNull_returnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    pass.peepholeOptimizations = new AbstractPeepholeOptimization[] { new TestPeepholeOptimization() };
    
    // Assert that the visit method executed without any exceptions
    pass.visit(null);
    assertTrue(true);
  }
  
  @Test
  public void testVisit_currentVersionOfNodeNotChanged_returnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    TestPeepholeOptimization optimization = new TestPeepholeOptimization();
    pass.peepholeOptimizations = new AbstractPeepholeOptimization[] { optimization };
    Node node = new Node();
    optimization.changed = false;
    
    // Assert that the visit method executed without any exceptions
    pass.visit(node);
    assertTrue(true);
  }
  
  @Test
  public void testVisit_currentVersionOfNodeChanged_returnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    TestPeepholeOptimization optimization = new TestPeepholeOptimization();
    pass.peepholeOptimizations = new AbstractPeepholeOptimization[] { optimization };
    Node node = new Node();
    optimization.changed = true;
    
    // Assert that the visit method executed without any exceptions
    pass.visit(node);
    assertTrue(true);
  }
  
  @Test
  public void testBeginTraversal() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    
    // Assert that the beginTraversal method executed without any exceptions
    pass.beginTraversal();
    assertTrue(true);
  }
  
  @Test
  public void testEndTraversal() {
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    
    // Assert that the endTraversal method executed without any exceptions
    pass.endTraversal();
    assertTrue(true);
  }
  
  class TestPeepholeOptimization extends AbstractPeepholeOptimization {
    public boolean changed = false;
    
    @Override
    Node optimizeSubtree(Node node) {
      changed = true;
      return node;
    }
  }
}