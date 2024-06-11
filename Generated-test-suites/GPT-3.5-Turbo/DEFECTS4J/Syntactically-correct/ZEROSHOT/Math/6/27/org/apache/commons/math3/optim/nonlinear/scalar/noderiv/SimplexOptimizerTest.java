import org.junit.Assert;
import org.junit.Test;

public class SimplexOptimizerTest {
  
  @Test
  public void testConstructorWithDefaultChecker() {
    SimplexOptimizer optimizer = new SimplexOptimizer();
    Assert.assertNotNull(optimizer);
  }
  
  @Test
  public void testConstructorWithChecker() {
    ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker();
    SimplexOptimizer optimizer = new SimplexOptimizer(checker);
    Assert.assertNotNull(optimizer);
  }
  
  @Test
  public void testConstructorWithThresholds() {
    double rel = 0.1;
    double abs = 0.01;
    SimplexOptimizer optimizer = new SimplexOptimizer(rel, abs);
    Assert.assertNotNull(optimizer);
  }
  
  @Test
  public void testSetSimplex() {
    AbstractSimplex simplex = new Simplex();
    SimplexOptimizer optimizer = new SimplexOptimizer();
    optimizer.setSimplex(simplex);
    Assert.assertEquals(simplex, optimizer.getSimplex());
  }
  
  @Test
  public void testOptimizeInternal() {
    int maxEval = 1000;
    MultivariateFunction f = new MultivariateFunction() {
      public double value(double[] point) {
        return 0.0;
      }
    };
    GoalType goalType = GoalType.MINIMIZE;
    OptimizationData[] optData = new OptimizationData[0];
    
    SimplexOptimizer optimizer = new SimplexOptimizer();
    PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
    
    Assert.assertNotNull(result);
  }
  
  @Test(expected = NullArgumentException.class)
  public void testDoOptimizeWithNullSimplex() {
    SimplexOptimizer optimizer = new SimplexOptimizer();
    optimizer.doOptimize();
  }
  
  @Test
  public void testDoOptimize() {
    final double[] startPoint = {0.0, 0.0, 0.0};
    final double[] point1 = {1.0, 0.0, 0.0};
    final double[] point2 = {0.0, 1.0, 0.0};
    final double[] point3 = {0.0, 0.0, 1.0};
    
    AbstractSimplex simplex = new AbstractSimplex() {
      public int getSize() {
        return 3;
      }
      
      public PointValuePair getPoint(int index) {
        if (index == 0) {
          return new PointValuePair(startPoint, 0.0);
        } else if (index == 1) {
          return new PointValuePair(point1, 1.0);
        } else if (index == 2) {
          return new PointValuePair(point2, 2.0);
        } else if (index == 3) {
          return new PointValuePair(point3, 3.0);
        }
        return null;
      }
    };
    
    SimplexOptimizer optimizer = new SimplexOptimizer();
    optimizer.setSimplex(simplex);
    
    PointValuePair result = optimizer.doOptimize();
    
    Assert.assertNotNull(result);
    Assert.assertArrayEquals(startPoint, result.getPoint(), 0.0);
  }
  
}