import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CodeChangeHandler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.rhino.Node;

public class PeepholeOptimizationsPassTest {
  
  private AbstractCompiler compiler;
  private PeepholeOptimizationsPass pass;
  
  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    pass = new PeepholeOptimizationsPass(compiler);
  }
  
  @Test
  public void testProcess() {
    Node externs = new Node();
    Node root = new Node();
    
    pass.process(externs, root);
    
    // TODO: Add assertion for the expected result
  }
  
  @Test
  public void testTraverse_shouldVisit_whenNodeIsFunctionAndPreviousScopeChanged_shouldReturnFalse() {
    Node node = new Node();
    node.setIsFunction();
    
    assertFalse(pass.shouldVisit(node));

    // TODO: Add assertion for the expected result
  }
  
  @Test
  public void testTraverse_shouldVisit_whenNodeIsFunctionAndPreviousScopeNotChanged_shouldReturnTrue() {
    Node node = new Node();
    node.setIsFunction();
    
    assertTrue(pass.shouldVisit(node));

    // TODO: Add assertion for the expected result
  }
  
  @Test
  public void testTraverse_shouldRetraverse_whenNodeIsFunctionAndParentNotNullAndScopeChanged_shouldReturnTrue() {
    Node node = new Node();
    node.setIsFunction();
    node.setParent(new Node());
    
    assertTrue(pass.shouldRetraverse(node));

    // TODO: Add assertion for the expected result
  }
  
  @Test
  public void testTraverse_shouldRetraverse_whenNodeIsFunctionAndParentNotNullAndScopeNotChanged_shouldReturnFalse() {
    Node node = new Node();
    node.setIsFunction();
    node.setParent(new Node());
    
    assertFalse(pass.shouldRetraverse(node));

    // TODO: Add assertion for the expected result
  }
  
  @Test
  public void testTraverse_traverse_shouldVisitAllChildNodesAndVisitNode() {
    Node root = createSampleNode();
    
    pass.traverse(root);

    // TODO: Add assertions for the expected results
  }
  
  @Test
  public void testVisit_shouldOptimizeSubtreeForAllPeepholeOptimizations() {
    Node node = createSampleNode();
    
    pass.visit(node);

    // TODO: Add assertions for the expected results
  }
  
  private Node createSampleNode() {
    Node root = new Node();
    Node child1 = new Node();
    Node child2 = new Node();
    
    root.addChildToFront(child1);
    root.addChildToFront(child2);
    
    return root;
  }
  
}