import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PeepholeOptimizationsPassTest {
  private PeepholeOptimizationsPass pass;
  private Node externs;
  private Node root;

  @Before
  public void setUp() {
    pass = new PeepholeOptimizationsPass();
    externs = new Node();
    root = new Node();
  }

  @Test
  public void testProcess_WithNoOptimizations_NoChangeReported() {
    pass.process(externs, root);
    assertFalse(pass.getCompiler().hasCodeChanged());
  }

  @Test
  public void testProcess_AddConstantFoldingOptimization_CodeChanged() {
    AbstractPeepholeOptimization constantFolding = new ConstantFoldingOptimization();
    pass = new PeepholeOptimizationsPass(constantFolding);
    pass.process(externs, root);
    assertTrue(pass.getCompiler().hasCodeChanged());
  }

  @Test
  public void testProcess_AddUselessCodeRemovalOptimization_CodeChanged() {
    AbstractPeepholeOptimization uselessCodeRemoval = new UselessCodeRemovalOptimization();
    pass = new PeepholeOptimizationsPass(uselessCodeRemoval);
    pass.process(externs, root);
    assertTrue(pass.getCompiler().hasCodeChanged());
  }

  @Test
  public void testProcess_WithMultipleOptimizations_CodeChanged() {
    AbstractPeepholeOptimization constantFolding = new ConstantFoldingOptimization();
    AbstractPeepholeOptimization uselessCodeRemoval = new UselessCodeRemovalOptimization();
    pass = new PeepholeOptimizationsPass(constantFolding, uselessCodeRemoval);
    pass.process(externs, root);
    assertTrue(pass.getCompiler().hasCodeChanged());
  }

  @Test
  public void testProcess_WithMultipleOptimizations_CodeOptimizedCorrectly() {
    AbstractPeepholeOptimization constantFolding = new ConstantFoldingOptimization();
    AbstractPeepholeOptimization uselessCodeRemoval = new UselessCodeRemovalOptimization();
    pass = new PeepholeOptimizationsPass(constantFolding, uselessCodeRemoval);
    pass.process(externs, root);
    String optimizedCode = pass.getCompiler().getOptimizedCode();

    // assert optimized code is correct
    // ...

    assertEquals("expected", optimizedCode);
  }
}