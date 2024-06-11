import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {
  
  @Test
  public void testDefaultConstructor() {
    SimplexOptimizer optimizer = new SimplexOptimizer();
    assertNotNull(optimizer);
  }
  
  @Test
  public void testConstructorWithConvergenceChecker() {
    SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker());
    assertNotNull(optimizer);
  }
  
  @Test
  public void testConstructorWithThresholds() {
    SimplexOptimizer optimizer = new SimplexOptimizer(0.1, 0.01);
    assertNotNull(optimizer);
  }
  
  @Test
  public void testSetSimplex() {
    SimplexOptimizer optimizer = new SimplexOptimizer();
    AbstractSimplex simplex = new AbstractSimplex();
    optimizer.setSimplex(simplex);
  }
  
  @Test(expected = NullArgumentException.class)
  public void testDoOptimizeWithNullSimplex() {
    SimplexOptimizer optimizer = new SimplexOptimizer();
    optimizer.doOptimize();
  }
  
  @Test(expected = NullArgumentException.class)
  public void testDoOptimizeWithNullStartPoint() {
    SimplexOptimizer optimizer = new SimplexOptimizer();
    AbstractSimplex simplex = new AbstractSimplex();
    optimizer.setSimplex(simplex);
    optimizer.doOptimize();
  }
  
  @Test
  public void testDoOptimize() {
    SimplexOptimizer optimizer = new SimplexOptimizer();
    AbstractSimplex simplex = new AbstractSimplex();
    simplex.build(new double[] {0.0, 0.0});
    optimizer.setSimplex(simplex);
    optimizer.setStartPoint(new double[] {1.0, 1.0});
    optimizer.doOptimize();
  }
  
}