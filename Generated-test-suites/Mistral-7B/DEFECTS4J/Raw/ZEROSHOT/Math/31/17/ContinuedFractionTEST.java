import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

 private static class TestContinuedFraction extends ContinuedFraction {
  @Override
  protected double getA(int n, double x) {
    return (n % 2 == 0) ? 1.0 / (2.0 * x + n) : -1.0 / (2.0 * x + n);
  }

  @Override
  protected double getB(int n, double x) {
    return 1.0;
  }
 }

 @Test
 public void testEvaluateSquareRootOfTwo() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1.0e-14;
  double x = 1.0;

  double result = continuedFraction.evaluate(x, epsilon);
  assertEquals(1.4142135623730955, result, epsilon);
 }

 @Test(expected = ConvergenceException.class)
 public void testEvaluateSquareRootOfTwoUnstable() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1.0e-14;
  double x = -1.0;

  continuedFraction.evaluate(x, epsilon, Integer.MAX_VALUE);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testEvaluateSine() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1.0e-14;
  double x = Math.PI / 2.0;

  continuedFraction.evaluate(x, epsilon, 10);
 }

}