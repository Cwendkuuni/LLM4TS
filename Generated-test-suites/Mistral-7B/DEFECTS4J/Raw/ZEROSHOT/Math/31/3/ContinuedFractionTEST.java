import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

 private static class TestContinuedFraction extends ContinuedFraction {

 @Override
 protected double getA(int n, double x) {
 return n == 0 ? 1.0 : n % 2 == 0 ? x : -1.0 / x;
 }

 @Override
 protected double getB(int n, double x) {
 return n == 0 ? 0.0 : n % 2 == 0 ? 1.0 : x;
 }
 }

 @Test
 public void testEvaluateSquareRootOfTwo() {
 ContinuedFraction continuedFraction = new TestContinuedFraction();
 double epsilon = 1e-10;
 int maxIterations = 100;
 double x = 1.5;
 double result = continuedFraction.evaluate(x, epsilon, maxIterations);
 double expectedResult = Math.sqrt(2.0);
 assertEquals(expectedResult, result, epsilon);
 }

 @Test(expected = ConvergenceException.class)
 public void testEvaluateSquareRootOfNegativeNumber() {
 ContinuedFraction continuedFraction = new TestContinuedFraction();
 double epsilon = 1e-10;
 int maxIterations = 100;
 double x = -1.0;
 continuedFraction.evaluate(x, epsilon, maxIterations);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testEvaluateNonConvergentContinuedFraction() {
 ContinuedFraction continuedFraction = new TestContinuedFraction();
 double epsilon = 1e-10;
 int maxIterations = 1;
 double x = 1.5;
 continuedFraction.evaluate(x, epsilon, maxIterations);
 }

 @Test
 public void testEvaluateZero() {
 ContinuedFraction continuedFraction = new TestContinuedFraction();
 double epsilon = 1e-10;
 int maxIterations = 100;
 double x = 0.0;
 double result = continuedFraction.evaluate(x, epsilon, maxIterations);
 assertEquals(0.0, result, epsilon);
 }

 @Test
 public void testEvaluateNegativeNumber() {
 ContinuedFraction continuedFraction = new TestContinuedFraction();
 double epsilon = 1e-10;
 int maxIterations = 100;
 double x = -1.5;
 double result = continuedFraction.evaluate(x, epsilon, maxIterations);
 assertEquals(-Math.sqrt(2.0), result, epsilon);
 }
}