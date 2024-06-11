import org.junit.Test;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.MultivariateOptimizer;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.direct.SimplexOptimizer;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.apache.commons.math3.exception.NullArgumentException;

public class SimplexOptimizerTest {
  
  private static final double REL = 1e-10;
  private static final double ABS = 1e-10;
  
  private SimplexOptimizer simplexOptimizer;
  
  public SimplexOptimizerTest() {
    ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(REL, ABS);
    simplexOptimizer = new SimplexOptimizer(checker);
  }
  
  @Test
  public void testOptimize() {
    MultivariateFunction f = new MultivariateFunction() {
      public double value(double[] point) {
        if (point.length != 2) {
          throw new IllegalArgumentException("Point dimension must be 2");
        }
        return point[0] * point[0] + point[1] * point[1];
      }
    };
    
    double[] start = {1.0, 1.0};
    
    PointValuePair result = simplexOptimizer.optimize(100, f, GoalType.MINIMIZE, start);
    
    double[] expectedPoint = {0.0, 0.0};
    double expectedValue = 0.0;
    PointValuePair expected = new PointValuePair(expectedPoint, expectedValue);
    
    assertArrayEquals(expected.getPoint(), result.getPoint(), ABS);
    assertEquals(expected.getValue(), result.getValue(), ABS);
  }
  
  @Test(expected = NullArgumentException.class)
  public void testSetSimplexNull() {
    AbstractSimplex simplex = null;
    simplexOptimizer.setSimplex(simplex);
  }
  
  @Test
  public void testSetSimplex() {
    AbstractSimplex simplex = new MySimplex();
    simplexOptimizer.setSimplex(simplex);
  }
  
  private class MySimplex extends AbstractSimplex {
    public MySimplex() {
      super(2);
    }
  }
  
}