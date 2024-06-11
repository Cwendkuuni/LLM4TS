import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class ContinuedFractionTest {

 private static class TestContinuedFraction extends ContinuedFraction {
  @Override
  protected double getA(int n, double x) {
   switch (n) {
    case 0:
     return 2.0;
    case 1:
     return 1.0;
    default:
     return -x;
   }
  }

  @Override
  protected double getB(int n, double x) {
   switch (n) {
    case 0:
     return 1.0;
    case 1:
     return 2.0;
    default:
     return 0.0;
   }
  }
 }

 @Test
 public void testEvaluateSquareRootOf2() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1e-6;
  double expected = 1.4142135623730955;

  double result = continuedFraction.evaluate(2.0, epsilon);
  assertEquals(expected, result, epsilon);
 }

 @Test
 public void testEvaluateSquareRootOf2WithMaxIterations() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1e-6;
  int maxIterations = 10;
  double expected = 1.4142135623730955;

  double result = continuedFraction.evaluate(2.0, epsilon, maxIterations);
  assertEquals(expected, result, epsilon);
 }

 @Test
 public void testEvaluateSquareRootOf2WithEpsilonOnly() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1e-6;

  assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(2.0, epsilon, 5));
 }

 @Test
 public void testEvaluateSquareRootOf2WithMaxIterationsOnly() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  int maxIterations = 5;

  assertThrows(MaxCountExceededException.class, () -> continuedFraction.evaluate(2.0, 1e-6, maxIterations));
 }

 @Test
 public void testEvaluateSquareRootOf2WithEpsilonAndMaxIterations() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1e-6;
  int maxIterations = 15;

  double expected = 1.4142135623730955;
  double result = continuedFraction.evaluate(2.0, epsilon, maxIterations);
  assertEquals(expected, result, epsilon);
 }

 @Test
 public void testEvaluateZero() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  ContinuedFractionException expectedException = new ContinuedFractionException();
  expectedException.setLocalizedMessage(LocalizedFormats.CONTINUED_FRACTION_DIVISION_BY_ZERO);

  assertThrows(expectedException.getClass(), () -> continuedFraction.evaluate(0.0, 1e-6, Integer.MAX_VALUE));
 }

 @Test
 public void testEvaluateNegativeX() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1e-6;

  double result = continuedFraction.evaluate(-2.0, epsilon);
  assertEquals(-expected, result, epsilon);
  expected *= -1;
 }

 @Test
 public void testEvaluateNegativeXWithMaxIterations() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1e-6;
  int maxIterations = 10;

  double result = continuedFraction.evaluate(-2.0, epsilon, maxIterations);
  assertEquals(-expected, result, epsilon);
 }

 @Test
 public void testEvaluateBigX() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1e-6;

  double bigX = Double.POSITIVE_INFINITY;
  double result = continuedFraction.evaluate(bigX, epsilon);
  assertEquals(Double.NaN, result);
 }

 @Test
 public void testEvaluateBigXWithMaxIterations() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1e-6;
  int maxIterations = 10;

  double bigX = Double.POSITIVE_INFINITY;
  double result = continuedFraction.evaluate(bigX, epsilon, maxIterations);
  assertEquals(Double.NaN, result);
 }

 @Test
 public void testEvaluateInfinitelyLargeOrSmallA() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1e-6;
  int maxIterations = 10;

  double infinitelyLargeA = Double.POSITIVE_INFINITY;
  double infinitelySmallA = -Double.MAX_VALUE;

  assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(1.0, epsilon, maxIterations,
      infinitelyLargeA, 1.0));
  assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(1.0, epsilon, maxIterations,
      infinitelySmallA, 1.0));
 }

 @Test
 public void testEvaluateInfinitelyLargeOrSmallB() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1e-6;
  int maxIterations = 10;

  double infinitelyLargeB = Double.POSITIVE_INFINITY;
  double infinitelySmallB = -Double.MAX_VALUE;

  assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(1.0, epsilon, maxIterations, 1.0,
      infinitelyLargeB));
  assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(1.0, epsilon, maxIterations, 1.0,
      infinitelySmallB));
 }

 @Test
 public void testEvaluateInfinitelyLargeOrSmallAAndB() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1e-6;
  int maxIterations = 10;

  double infinitelyLargeA = Double.POSITIVE_INFINITY;
  double infinitelySmallB = -Double.MAX_VALUE;

  assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(1.0, epsilon, maxIterations,
      infinitelyLargeA, infinitelySmallB));
 }

 @Test
 public void testEvaluateConvergenceException() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1e-16;
  int maxIterations = 15;

  ContinuedFractionException expectedException = new ContinuedFractionException();
  expectedException.setLocalizedMessage(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE);

  assertThrows(expectedException.getClass(), () -> continuedFraction.evaluate(2.0, epsilon, maxIterations));
 }

 @Test
 public void testEvaluateNaNException() {
  ContinuedFraction continuedFraction = new TestContinuedFraction();
  double epsilon = 1e-6;
  int maxIterations = 15;

  assertThrows(ConvergenceException.class, () -> continuedFraction.evaluate(0.0, epsilon, maxIterations));

  ContinuedFractionException expectedException = new ContinuedFractionException();
  expectedException.setLocalizedMessage(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE);

  assertThrows(expectedException.getClass(), () -> continuedFraction.evaluate(2.0, epsilon, maxIterations));
 }
}