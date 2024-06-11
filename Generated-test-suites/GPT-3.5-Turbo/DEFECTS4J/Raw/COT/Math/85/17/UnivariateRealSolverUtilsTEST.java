import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.junit.Assert;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

  @Test
  public void testSolve() throws Exception {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 2;
      }
    };
    double x0 = 1;
    double x1 = 3;

    double expected = 2;
    double actual = UnivariateRealSolverUtils.solve(f, x0, x1);
    Assert.assertEquals(expected, actual, 0.00001);
  }

  @Test
  public void testSolveWithAbsoluteAccuracy() throws Exception {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 2;
      }
    };
    double x0 = 1;
    double x1 = 3;
    double absoluteAccuracy = 0.0001;

    double expected = 2;
    double actual = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    Assert.assertEquals(expected, actual, 0.00001);
  }

  @Test
  public void testBracket() throws Exception {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 2;
      }
    };
    double initial = 2;
    double lowerBound = 1;
    double upperBound = 3;

    double[] expected = {1.0, 3.0};
    double[] actual = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    Assert.assertArrayEquals(expected, actual, 0.00001);
  }

  @Test
  public void testBracketWithMaximumIterations() throws Exception {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 2;
      }
    };
    double initial = 2;
    double lowerBound = 1;
    double upperBound = 3;
    int maximumIterations = 100;

    double[] expected = {1.0, 3.0};
    double[] actual = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound, maximumIterations);
    Assert.assertArrayEquals(expected, actual, 0.00001);
  }

  @Test
  public void testMidpoint() throws Exception {
    double a = 1;
    double b = 3;

    double expected = 2;
    double actual = UnivariateRealSolverUtils.midpoint(a, b);
    Assert.assertEquals(expected, actual, 0.00001);
  }

}