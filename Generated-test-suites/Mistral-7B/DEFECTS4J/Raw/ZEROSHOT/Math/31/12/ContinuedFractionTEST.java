import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ContinuedFractionTest {

 private static final double EPSILON = 1e-9;
 private static final int MAX_ITERATIONS = 100;

 @Test
 public void testEvaluateSimpleContinuedFraction() {
 ContinuedFractionSqrt testee = new ContinuedFractionSqrt();
 double x = 2.0;
 double result = testee.evaluate(x);
 double expected = FastMath.sqrt(x);
 assertEquals(expected, result, EPSILON);
 }

 @Test(expected = ConvergenceException.class)
 public void testEvaluateDivergentContinuedFraction() {
 ContinuedFractionLog testee = new ContinuedFractionLog();
 double x = 0.0;
 testee.evaluate(x, EPSILON, MAX_ITERATIONS);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testEvaluateNonConvergentContinuedFraction() {
 ContinuedFractionBessel testee = new ContinuedFractionBessel();
 double x = -1.0;
 testee.evaluate(x, EPSILON, MAX_ITERATIONS);
 }

 @Test
 public void testEvaluateComplexContinuedFraction() {
 ContinuedFractionPi testee = new ContinuedFractionPi();
 double x = 3.1415926535897932384626433832795;
 double result = testee.evaluate(x);
 double expected = x;
 assertEquals(expected, result, EPSILON);
 }

 @Test
 public void testEvaluateComplexContinuedFractionWithEpsilon() {
 ContinuedFractionPi testee = new ContinuedFractionPi();
 double x = 3.1415926535897932384626433832795;
 double epsilon = 1e-12;
 double result = testee.evaluate(x, epsilon);
 double expected = x;
 assertEquals(expected, result, epsilon);
 }

 @Test
 public void testEvaluateComplexContinuedFractionWithMaxIterations() {
 ContinuedFractionSqrt testee = new ContinuedFractionSqrt();
 double x = 16.0;
 double result = testee.evaluate(x, EPSILON, 5);
 double expected = FastMath.sqrt(x);
 assertEquals(expected, result, EPSILON);
 }

 @Test(expected = ConvergenceException.class)
 public void testEvaluateComplexContinuedFractionWithMaxIterationsAndDivergence() {
 ContinuedFractionLog testee = new ContinuedFractionLog();
 double x = 0.0;
 testee.evaluate(x, EPSILON, 5);
 }

 @Test(expected = MaxCountExceededException.class)
 public void testEvaluateComplexContinuedFractionWithMaxIterationsAndNonConvergence() {
 ContinuedFractionBessel testee = new ContinuedFractionBessel();
 double x = -1.0;
 testee.evaluate(x, EPSILON, 5);
 }
}

class ContinuedFractionSqrt extends ContinuedFraction {
 @Override
 protected double getA(int n, double x) {
 if (n == 0) {
 return 1.0;
 } else {
 return 2.0 * x / (2.0 * n - 1.0);
 }
 }

 @Override
 protected double getB(int n, double x) {
 return 1.0;
 }
}

class ContinuedFractionLog extends ContinuedFraction {
 @Override
 protected double getA(int n, double x) {
 if (n == 0) {
 return 1.0;
 } else {
 return x / ((double) n);
 }
 }

 @Override
 protected double getB(int n, double x) {
 return n;
 }
}

class ContinuedFractionPi extends ContinuedFraction {
 @Override
 protected double getA(int n, double x) {
 if (n % 2 == 0) {
 return -1.0 / (2.0 * n - 1.0);
 } else {
 return 1.0 / (2.0 * n - 1.0);
 }
 }

 @Override
 protected double getB(int n, double x) {
 return 2.0 * n;
 }
}

class ContinuedFractionBessel extends ContinuedFraction {
 @Override
 protected double getA(int n, double x) {
 return 1.0 / (x + ((double) n) * ((double) n));
 }

 @Override
 protected double getB(int n, double x) {
 return -((x + ((double) n)) / ((x + ((double) n) * ((double) n))));
 }
}