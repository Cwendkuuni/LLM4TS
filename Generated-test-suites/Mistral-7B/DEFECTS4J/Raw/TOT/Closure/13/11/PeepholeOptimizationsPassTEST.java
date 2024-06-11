import static org.junit.Assert.*;
import static com.google.javascript.jscomp.PeepholeOptimizationsPass.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the PeepholeOptimizationsPass class.
 */
public class PeepholeOptimizationsPassTest {
  private Compiler compiler;
  private CompilerOptions options;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
  }

  /**
   * Test for the optimization pass with no optimizations.
   */
  @Test
  public void testNoOptimizations() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler);
    Node root = createTestNode();
    pass.process(null, root);
    assertEquals(root.toString(), pass.getCompiler().toSource(root));
  }

  /**
   * Test for the constant folding optimization.
   */
  @Test
  public void testConstantFolding() {
    AbstractPeepholeOptimization optimization = new AddConstFolding();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
    Node root = createTestNodeWithConstants();
    pass.process(null, root);
    assertEquals("('a' + 1)", pass.getCompiler().toSource(root));
  }

  /**
   * Test for the useless code removal optimization.
   */
  @Test
  public void testUselessCodeRemoval() {
    AbstractPeepholeOptimization optimization = new DeadCodeElimination();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
    Node root = createTestNodeWithDeadCode();
    pass.process(null, root);
    assertEquals("function foo() {}\nreturn foo()", pass.getCompiler().toSource(root));
  }

  /**
   * Test for the error handling during optimization.
   */
  @Test(expected = AssertionError.class)
  public void testErrorHandling() {
    AbstractPeepholeOptimization optimization = new InvalidOptimization();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
    Node root = createTestNodeWithError();
    pass.process(null, root);
  }

  private Node createTestNode() {
    // A simple Node for testing.
    return new Node("function foo() {}");
  }

  private Node createTestNodeWithConstants() {
    // A Node with some constants for testing constant folding.
    return new Node("function foo() { return 'a' + 1; }");
  }

  private Node createTestNodeWithDeadCode() {
    // A Node with some dead code for testing useless code removal.
    return new Node("function foo() { return foo(); }");
  }

  private Node createTestNodeWithError() {
    // A Node with an error for testing error handling.
    return new Node("function foo() { throw new Error('Test Error'); }");
  }
}