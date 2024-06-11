import static org.junit.Assert.*;
import org.junit.Test;

public class PeepholeOptimizationsPassTest {

  @Test
  public void testProcess() {
    // Create compiler and optimizations
    AbstractCompiler compiler = new AbstractCompiler();
    AbstractPeepholeOptimization[] optimizations = {new ExampleOptimization()};

    // Create peephole optimization pass
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimizations);

    // Call process method
    pass.process(new Node(), new Node());

    // Test that process method has been called
    assertTrue(pass.processCalled);
  }

  // Example optimization implementation for testing
  private class ExampleOptimization extends AbstractPeepholeOptimization {
    @Override
    protected Node optimizeSubtree(Node node) {
      // Do optimization here
      return node;
    }
  }
}