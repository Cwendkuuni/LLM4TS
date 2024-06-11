import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PeepholeOptimizationsPassTest {
  
  private PeepholeOptimizationsPass pass;
  
  @Before
  public void setUp() {
    pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2, optimization3);
  }
  
  @Test
  public void testProcess() {
    // TODO: Add test logic here
  }
  
  @Test
  public void testGetCompiler() {
    AbstractCompiler expected = compiler;
    AbstractCompiler actual = pass.getCompiler();
    assertEquals(expected, actual);
  }
  
  @Test
  public void testTraverse() {
    // TODO: Add test logic here
  }
  
  @Test
  public void testShouldRetraverse() {
    // TODO: Add test logic here
  }
  
  @Test
  public void testShouldVisit() {
    // TODO: Add test logic here
  }
  
  @Test
  public void testExitNode() {
    // TODO: Add test logic here
  }
  
  @Test
  public void testVisit() {
    // TODO: Add test logic here
  }
  
  @Test
  public void testBeginTraversal() {
    // TODO: Add test logic here
  }
  
  @Test
  public void testEndTraversal() {
    // TODO: Add test logic here
  }
}