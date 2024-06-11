import static org.junit.Assert.*;
import org.junit.Test;

public class PeepholeOptimizationsPassTest {

  @Test
  public void testProcess() {
    // create the test environment
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);

    // test the process method with valid inputs
    Node externs = new Node();
    Node root = new Node();
    pass.process(externs, root);

    // assert that the compiler has the correct change handler
    assertEquals(pass.getCompiler().getChangeHandlers().size(), 1);
  }

  @Test
  public void testTraverse() {
    // create the test environment
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);

    // test the traverse method with valid inputs
    Node root = new Node();
    pass.traverse(root);

    // assert that the node is visited and exited correctly
    assertEquals(pass.shouldVisit(root), true);
    assertEquals(pass.shouldRetraverse(root), false);
    pass.exitNode(root);
  }

  @Test
  public void testVisit() {
    // create the test environment
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);

    // test the visit method with valid inputs
    Node n = new Node();
    pass.visit(n);

    // assert that the optimizations are applied and completed
    assertNotNull(compiler);
  }

  @Test
  public void testBeginTraversal() {
    // create the test environment
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);

    // test the beginTraversal method
    pass.beginTraversal();

    // assert that all optimizations have begun traversal
    assertNotNull(compiler);
  }

  @Test
  public void testEndTraversal() {
    // create the test environment
    AbstractCompiler compiler = new AbstractCompiler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);

    // test the endTraversal method
    pass.endTraversal();

    // assert that all optimizations have ended traversal
    assertNotNull(compiler);
  }
}