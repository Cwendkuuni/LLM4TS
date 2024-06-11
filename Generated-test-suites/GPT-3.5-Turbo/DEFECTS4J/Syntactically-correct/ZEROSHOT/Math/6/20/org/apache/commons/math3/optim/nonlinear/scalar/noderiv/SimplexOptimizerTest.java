import org.junit.Test;
import org.junit.Assert;
import java.util.Comparator;

public class SimplexOptimizerTest {

  @Test
  public void testOptimize() {
    SimplexOptimizer optimizer = new SimplexOptimizer();

    MultivariateFunction f = new MultivariateFunction() {
      public double value(double[] point) {
        return point[0] + point[1];
      }
    };

    GoalType goalType = GoalType.MINIMIZE;
    double[] startPoint = {0.0, 0.0};

    PointValuePair result = optimizer.optimize(100, f, goalType, startPoint);

    Assert.assertEquals(0.0, result.getPoint()[0], 0.00001);
    Assert.assertEquals(0.0, result.getPoint()[1], 0.00001);
    Assert.assertEquals(0.0, result.getValue(), 0.00001);
  }

  @Test
  public void testSetSimplex() {
    SimplexOptimizer optimizer = new SimplexOptimizer();
    AbstractSimplex simplex = new AbstractSimplex();

    optimizer.setSimplex(simplex);

    Assert.assertEquals(simplex, optimizer.getSimplex());
  }

  @Test
  public void testParseOptimizationData() {
    SimplexOptimizer optimizer = new SimplexOptimizer();
    OptimizationData[] optData = new OptimizationData[]{new AbstractSimplex()};

    optimizer.parseOptimizationData(optData);

    Assert.assertEquals(optData[0], optimizer.getSimplex());
  }

  @Test
  public void testDoOptimize() {
    SimplexOptimizer optimizer = new SimplexOptimizer();
    AbstractSimplex simplex = new AbstractSimplex();
    double[] startPoint = {0.0, 0.0};

    optimizer.setSimplex(simplex);
    optimizer.setStartPoint(startPoint);

    PointValuePair result = optimizer.doOptimize();

    Assert.assertEquals(0.0, result.getPoint()[0], 0.00001);
    Assert.assertEquals(0.0, result.getPoint()[1], 0.00001);
    Assert.assertEquals(0.0, result.getValue(), 0.00001);
  }
}