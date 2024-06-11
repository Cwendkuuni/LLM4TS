import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.direct.AbstractSimplex;
import org.junit.Before;
import org.junit.Test;

public class SimplexOptimizerTest {
  
  private SimplexOptimizer optimizer;

  @Before
  public void setUp() {
    optimizer = new SimplexOptimizer();
  }

  @Test
  public void testSetSimplex() {
    AbstractSimplex simplex = new AbstractSimplex(2);
    optimizer.setSimplex(simplex);
    assertEquals(simplex, optimizer.getSimplex());
  }

  @Test
  public void testOptimizeInternal() {
    MultivariateFunction function = new MultivariateFunction() {
      public double value(double[] point) {
        return point[0] * point[0] + point[1] * point[1];
      }
    };

    PointValuePair result = optimizer.optimizeInternal(100, function, GoalType.MINIMIZE);
  
    double[] expectedPoint = {0.0, 0.0};
    PointValuePair expected = new PointValuePair(expectedPoint, 0.0);
    assertEquals(expected, result);
  }
  
  @Test
  public void testDoOptimize() {
    AbstractSimplex simplex = new AbstractSimplex(2);
    double[] start = {1.0, 2.0};
  
    try {
      optimizer.setSimplex(simplex);
      optimizer.setStartPoint(start);
  
      ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
        public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
          return true;
        }
      };
      optimizer.setConvergenceChecker(checker);
  
      PointValuePair result = optimizer.doOptimize();
  
      double[] expectedPoint = {1.0, 2.0};
      PointValuePair expected = new PointValuePair(expectedPoint, 0.0);
      assertEquals(expected, result);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}