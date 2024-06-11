import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.Node.*;

import com.google.common.base.Preconditions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestUtilities;
import com.google.javascript.jscomp.CompilerPassTestUtilities.TestCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ScopeState;
import com.google.javascript.jscomp.StateStack;

/**
 * Test class for PeepholeOptimizationsPass.
 */
@RunWith(JUnit4.class)
public class PeepholeOptimizationsPassTest {

  /** Compiler instance to use for testing. */
  private TestCompiler compiler;

  /** Peephole optimization pass instance to test. */
  private PeepholeOptimizationsPass pass;

  @Before
  public void setUp() {
    compiler = new TestCompiler();
    pass = new PeepholeOptimizationsPass(compiler, new AlwaysConstantFolding(), new UselessCodeRemover());
  }

  /**
   * Test method for constructor.
   */
  @Test
  public void testConstructor() {
    assertNotNull(pass);
  }

  /**
   * Test method for getCompiler method.
   */
  @Test
  public void testGetCompiler() {
    assertEquals(compiler, pass.getCompiler());
  }

  /**
   * Test method for process method.
   */
  @Test
  public void testProcess() {
    Node root = createTestNode();
    pass.process(Externs.empty(), root);
  }

  /**
   * Test method for shouldVisit method.
   */
  @Test
  public void testShouldVisit() {
    Node root = createTestNode();

    // Should visit root
    assertTrue(pass.shouldVisit(root));

    // Should not visit empty node
    Node emptyNode = new Node_Empty();
    assertFalse(pass.shouldVisit(emptyNode));

    // Should not visit script node
    Node scriptNode = new Node_Script();
    assertFalse(pass.shouldVisit(scriptNode));

    // Should not visit null node
    Node nullNode = null;
    assertFalse(pass.shouldVisit(nullNode));
  }

  /**
   * Test method for shouldRetraverse method.
   */
  @Test
  public void testShouldRetraverse() {
    Node root = createTestNode();
    pass.traverse(root);

    // Should retraverse Node with changed parent
    Node childNode = new Node_NumberLiteral(1);
    Node parentNode = new Node_Assignment(ASSIGN, new Node_Identifier("x"), childNode);
    Node grandParentNode = new Node_Script();
    grandParentNode.addChild(parentNode);
    grandParentNode.addChild(root);
    root.setParent(grandParentNode);
    assertTrue(pass.shouldRetraverse(grandParentNode));

    // Should not retraverse Node without changed parent
    Node siblingNode = new Node_NumberLiteral(2);
    Node siblingParentNode = new Node_Script();
    siblingParentNode.addChild(siblingNode);
    siblingParentNode.addChild(root);
    root.setParent(siblingParentNode);
    assertFalse(pass.shouldRetraverse(root));

    // Should not retraverse Script node
    Node scriptRetraverseNode = new Node_Script();
    assertTrue(pass.shouldRetraverse(scriptRetraverseNode));
    pass.traverse(scriptRetraverseNode);
    assertFalse(pass.shouldRetraverse(scriptRetraverseNode));

    // Should not retraverse Function node
    Node functionRetraverseNode = new Node_Function(null, false, null, null, null, null, null, null);
    assertTrue(pass.shouldRetraverse(functionRetraverseNode));
    pass.traverse(functionRetraverseNode);
    assertFalse(pass.shouldRetraverse(functionRetraverseNode));
  }

  /**
   * Test method for exitNode method.
   */
  @Test
  public void testExitNode() {
    Node root = createTestNode();
    pass.traverse(root);

    // Test exitNode for Script node
    Node scriptNode = new Node_Script();
    pass.exitNode(scriptNode);
    assertEquals(0, traversalState.peek().currentDepth);

    // Test exitNode for Function node
    Node functionNode = new Node_Function(null, false, null, null, null, null, null, null);
    pass.exitNode(functionNode);
    assertEquals(0, traversalState.peek().currentDepth);

    // Test exitNode for Node with changed parent
    Node changedParentNode = new Node_Script();
    Node childNode = new Node_NumberLiteral(1);
    Node nodeToExit = new Node_Assignment(ASSIGN, new Node_Identifier("x"), childNode);
    changedParentNode.addChild(nodeToExit);
    pass.traverse(changedParentNode);
    assertEquals(1, traversalState.peek().currentDepth);
    pass.exitNode(nodeToExit);
    assertEquals(0, traversalState.peek().currentDepth);
  }

  /**
   * Test method for visit method.
   */
  @Test
  public void testVisit() {
    Node root = createTestNode();
    pass.traverse(root);

    // Test visit for null node
    Node nullNode = null;
    pass.visit(nullNode);

    // Test visit for empty node
    Node emptyNode = new Node_Empty();
    pass.visit(emptyNode);

    // Test visit for NumberLiteral node
    Node numberNode = new Node_NumberLiteral(1);
    pass.visit(numberNode);

    // Test visit for Identifier node
    Node identifierNode = new Node_Identifier("x");
    pass.visit(identifierNode);

    // Test visit for Assignment node
    Node assignmentNode = new Node_Assignment(ASSIGN, new Node_Identifier("x"), numberNode);
    pass.visit(assignmentNode);

    // Test visit for Script node
    Node scriptNode = new Node_Script();
    pass.visit(scriptNode);

    // Test visit for Function node
    Node functionNode = new Node_Function(null, false, null, null, null, null, null, null);
    pass.visit(functionNode);
  }

  /**
   * Test method for beginTraversal method.
   */
  @Test
  public void testBeginTraversal() {
    Node root = createTestNode();
    pass.beginTraversal();
    pass.traverse(root);
    pass.endTraversal();
  }

  /**
   * Test method for endTraversal method.
   */
  @Test
  public void testEndTraversal() {
    Node root = createTestNode();
    pass.beginTraversal();
    pass.traverse(root);
    pass.endTraversal();
  }

  /**
   * Helper method for creating test Node.
   */
  private Node createTestNode() {
    Node root = new Node_Script();
    Node child1 = new Node_Identifier("x");
    Node child2 = new Node_NumberLiteral(1);
    Node assignmentNode = new Node_Assignment(ASSIGN, child1, child2);
    root.addChild(assignmentNode);
    return root;
  }

  /**
   * Helper class for TestCompiler.
   */
  private static class AlwaysConstantFolding implements AbstractPeepholeOptimization {

    @Override
    public String getDescription() {
      return "Always Constant Folding";
    }

    @Override
    public Node optimizeSubtree(Node root) {
      return new Node_NumberLiteral(1);
    }

    @Override
    public void beginTraversal(CompilerPass pass) {
      // Empty implementation
    }

    @Override
    public void endTraversal(CompilerPass pass) {
      // Empty implementation
    }
  }

  /**
   * Helper class for TestCompiler.
   */
  private static class UselessCodeRemover implements AbstractPeepholeOptimization {

    @Override
    public String getDescription() {
      return "Useless Code Remover";
    }

    @Override
    public Node optimizeSubtree(Node root) {
      return null;
    }

    @Override
    public void beginTraversal(CompilerPass pass) {
      // Empty implementation
    }

    @Override
    public void endTraversal(CompilerPass pass) {
      // Empty implementation
    }
  }
}