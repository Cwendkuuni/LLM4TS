import org.junit.Assert;
import org.junit.Test;

public class SimplexOptimizerTest {

  @Test
  public void testConstructorDefaultConvergenceChecker() {
    // Create the optimizer
    SimplexOptimizer optimizer = new SimplexOptimizer();

    // Get the convergence checker
    ConvergenceChecker<PointValuePair> checker = optimizer.getConvergenceChecker();

    // Check if the convergence checker is an instance of SimpleValueChecker
    Assert.assertTrue(checker instanceof SimpleValueChecker);
  }

  @Test
  public void testConstructorConvergenceChecker() {
    // Create the convergence checker
    ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-5, 1e-5);

    // Create the optimizer with the convergence checker
    SimplexOptimizer optimizer = new SimplexOptimizer(checker);

    // Get the convergence checker
    ConvergenceChecker<PointValuePair> resultChecker = optimizer.getConvergenceChecker();

    // Check if the convergence checker is the same as the one passed in the constructor
    Assert.assertEquals(checker, resultChecker);
  }

  @Test
  public void testConstructorThresholds() {
    // Create the optimizer with the thresholds
    SimplexOptimizer optimizer = new SimplexOptimizer(1e-5, 1e-5);

    // Get the convergence checker
    ConvergenceChecker<PointValuePair> checker = optimizer.getConvergenceChecker();

    // Check if the convergence checker is an instance of SimpleValueChecker
    Assert.assertTrue(checker instanceof SimpleValueChecker);

    // Check if the thresholds are set correctly
    SimpleValueChecker valueChecker = (SimpleValueChecker) checker;
    Assert.assertEquals(1e-5, valueChecker.getRelativeThreshold(), 0);
    Assert.assertEquals(1e-5, valueChecker.getAbsoluteThreshold(), 0);
  }

  @Test
  public void testSetSimplex() {
    // Create the optimizer
    SimplexOptimizer optimizer = new SimplexOptimizer();

    // Create the simplex
    AbstractSimplex simplex = new Simplex();

    // Set the simplex
    optimizer.setSimplex(simplex);

    // Get the simplex
    AbstractSimplex resultSimplex = optimizer.getSimplex();

    // Check if the simplex is the same as the one set
    Assert.assertEquals(simplex, resultSimplex);
  }

  @Test(expected = NullArgumentException.class)
  public void testOptimizeWithNullSimplex() {
    // Create the optimizer
    SimplexOptimizer optimizer = new SimplexOptimizer();

    // Set the start point
    double[] startPoint = new double[]{1.0, 2.0, 3.0};

    // Optimize with null simplex
    optimizer.optimize(100, new MultivariateFunction() {
      public double value(double[] point) {
        return 0;
      }
    }, GoalType.MINIMIZE, startPoint);
  }

  @Test
  public void testOptimize() {
    // Create the optimizer
    SimplexOptimizer optimizer = new SimplexOptimizer();

    // Create the simplex
    AbstractSimplex simplex = new Simplex();

    // Set the simplex
    optimizer.setSimplex(simplex);

    // Set the start point
    double[] startPoint = new double[]{1.0, 2.0, 3.0};

    // Optimize
    PointValuePair result = optimizer.optimize(100, new MultivariateFunction() {
      public double value(double[] point) {
        return point[0] + point[1] + point[2];
      }
    }, GoalType.MINIMIZE, startPoint);

    // Check if the result point has the correct values
    Assert.assertEquals(0.0, result.getPoint()[0], 0);
    Assert.assertEquals(0.0, result.getPoint()[1], 0);
    Assert.assertEquals(0.0, result.getPoint()[2], 0);

    // Check if the result value is correct
    Assert.assertEquals(0.0, result.getValue(), 0);
  }

}