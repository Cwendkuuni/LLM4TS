import org.junit.Test;
import static org.junit.Assert.*;

public class SimplexOptimizerTest {
  
  @Test
  public void testOptimizeInternal() {
    SimplexOptimizer optimizer = new SimplexOptimizer();
    
    int maxEval = 100;
    MultivariateFunction f = new MultivariateFunction() {
      public double value(double[] point) {
        return point[0] * point[0] + point[1] * point[1];
      }
    };
    GoalType goalType = GoalType.MINIMIZE;
    OptimizationData[] optData = new OptimizationData[0];
    
    PointValuePair result = optimizer.optimizeInternal(maxEval, f, goalType, optData);
    
    double[] expectedPoint = {0.0, 0.0};
    double expectedValue = 0.0;
    
    assertArrayEquals(expectedPoint, result.getPoint(), 0.001);
    assertEquals(expectedValue, result.getValue(), 0.001);
  }
  
  @Test
  public void testSetSimplex() {
    SimplexOptimizer optimizer = new SimplexOptimizer();
    
    AbstractSimplex simplex = new AbstractSimplex() {
      public void build(double[] startPoint) {}
      public void evaluate(MultivariateFunction function, Comparator<PointValuePair> comparator) {}
      public void iterate(MultivariateFunction function, Comparator<PointValuePair> comparator) {}
      public PointValuePair[] getPoints() {
        return new PointValuePair[0];
      }
      public int getSize() {
        return 0;
      }
      public double[] getPoint(int index) {
        return new double[0];
      }
    };
    
    optimizer.setSimplex(simplex);
    
    assertSame(simplex, optimizer.getSimplex());
  }
  
  @Test
  public void testParseOptimizationData() {
    SimplexOptimizer optimizer = new SimplexOptimizer();
    
    OptimizationData[] optData = new OptimizationData[1];
    AbstractSimplex simplex = new AbstractSimplex() {
      public void build(double[] startPoint) {}
      public void evaluate(MultivariateFunction function, Comparator<PointValuePair> comparator) {}
      public void iterate(MultivariateFunction function, Comparator<PointValuePair> comparator) {}
      public PointValuePair[] getPoints() {
        return new PointValuePair[0];
      }
      public int getSize() {
        return 0;
      }
      public double[] getPoint(int index) {
        return new double[0];
      }
    };
    optData[0] = simplex;
    
    optimizer.parseOptimizationData(optData);
    
    assertSame(simplex, optimizer.getSimplex());
  }

  @Test
  public void testDoOptimize() {
    SimplexOptimizer optimizer = new SimplexOptimizer();
    
    AbstractSimplex simplex = new AbstractSimplex() {
      public void build(double[] startPoint) {}
      public void evaluate(MultivariateFunction function, Comparator<PointValuePair> comparator) {}
      public void iterate(MultivariateFunction function, Comparator<PointValuePair> comparator) {}
      public PointValuePair[] getPoints() {
        return new PointValuePair[0];
      }
      public int getSize() {
        return 0;
      }
      public double[] getPoint(int index) {
        return new double[0];
      }
    };
    optimizer.setSimplex(simplex);
    
    PointValuePair result = optimizer.doOptimize();
    
    double[] expectedPoint = {0.0, 0.0};
    double expectedValue = 0.0;
    
    assertArrayEquals(expectedPoint, result.getPoint(), 0.001);
    assertEquals(expectedValue, result.getValue(), 0.001);
  }
  
  @Test(expected = NullArgumentException.class)
  public void testDoOptimizeNullSimplex() {
    SimplexOptimizer optimizer = new SimplexOptimizer();
    
    optimizer.doOptimize();
  }
}