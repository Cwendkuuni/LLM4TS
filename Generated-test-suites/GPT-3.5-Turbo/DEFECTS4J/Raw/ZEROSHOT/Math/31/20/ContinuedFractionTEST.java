import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.ContinuedFraction;
import org.junit.Assert;
import org.junit.Test;

public class ContinuedFractionTest {

  private class ContinuedFractionImplementation extends ContinuedFraction {
    @Override
    protected double getA(int n, double x) {
      return 1;
    }

    @Override
    protected double getB(int n, double x) {
      return 1;
    }
  }

  private ContinuedFractionImplementation cFrac;

  @Test
  public void testEvaluateDefaultEpsilon() {
    cFrac = new ContinuedFractionImplementation();
    double x = 1.234;
    double expected = 1.0;
    double result = cFrac.evaluate(x);
    Assert.assertEquals(expected, result, 0.000001);
  }

  @Test
  public void testEvaluateCustomEpsilon() {
    cFrac = new ContinuedFractionImplementation();
    double x = 1.234;
    double epsilon = 0.00001;
    double expected = 1.0;
    double result = cFrac.evaluate(x, epsilon);
    Assert.assertEquals(expected, result, epsilon);
  }

  @Test
  public void testEvaluateCustomEpsilonMaxIterations() {
    cFrac = new ContinuedFractionImplementation();
    double x = 1.234;
    double epsilon = 0.00001;
    int maxIterations = 100;
    double expected = 1.0;
    double result = cFrac.evaluate(x, epsilon, maxIterations);
    Assert.assertEquals(expected, result, epsilon);
  }

  @Test(expected = ConvergenceException.class)
  public void testEvaluateConvergenceException() {
    cFrac = new ContinuedFractionImplementation();
    double x = 1.234;
    int maxIterations = 5;
    cFrac.evaluate(x, maxIterations);
  }

  @Test(expected = MaxCountExceededException.class)
  public void testEvaluateMaxCountExceededException() {
    cFrac = new ContinuedFractionImplementation();
    double x = 1.234;
    double epsilon = 0.00001;
    int maxIterations = 5;
    cFrac.evaluate(x, epsilon, maxIterations);
  }
}