package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initialguess.InitialGuess;
import org.apache.commons.math3.optim.univariate.*;
import org.apache.commons.math3.util.Precision;

import java.util.concurrent.ThreadLocalRandom;

public class BrentOptimizerTest {

 private static final double EPSILON = 1e-10;

 @org.junit.Test
 public void testBrentOptimizer() {
 // Test constructor with default values.
 BrentOptimizer optimizer = new BrentOptimizer(0.0);

 // Test constructor with custom values.
 BrentOptimizer optimizer1 = new BrentOptimizer(1e-6, 1e-6);

 // Test constructor with null ConvergenceChecker.
 BrentOptimizer optimizer2 = new BrentOptimizer(1e-6, 1e-6, null);

 // Test method optimize.
 Function function = new FunctionWithGoldenSectionTestFunction();
 UnivariateOptimizerResult result = optimizer.optimize(function, new InitialGuess(0.0));
 UnivariatePointValuePair pointValuePair = result.getPoint();
 double expected = 0.0;
 assertEquals(expected, pointValuePair.getX(), EPSILON);
 assertEquals(expected, result.getValue(), EPSILON);

 // Test method optimize with custom values.
 result = optimizer1.optimize(function, new InitialGuess(0.0));
 pointValuePair = result.getPoint();
 expected = Precision.round(Math.sqrt(2.0), EPSILON);
 assertTrue(pointValuePair.getX() > 0.0);
 assertFalse(Precision.isClose(pointValuePair.getX(), expected, EPSILON));

 // Test method optimize with custom values and ConvergenceChecker.
 ResultChecker resultChecker = new ResultChecker();
 optimizer2.setConvergenceChecker(resultChecker);
 result = optimizer2.optimize(function, new InitialGuess(0.0));
 assertEquals(1, resultChecker.getIterationCount());
 assertFalse(resultChecker.isConverged());

 // Test method optimize with null function.
 assertThrows(NullPointerException.class, () -> optimizer.optimize(null, new InitialGuess(0.0)));

 // Test method optimize with invalid InitialGuess.
 assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(function, null));

 // Test method optimize with function that always returns NaN.
 Function functionNaN = new FunctionWithNaNTestFunction();
 assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(functionNaN, new InitialGuess(0.0)));

 // Test method optimize with function that always returns infinity.
 Function functionInf = new FunctionWithInfTestFunction();
 assertThrows(IllegalArgumentException.class, () -> optimizer.optimize(functionInf, new InitialGuess(0.0)));
 }

 private static class ResultChecker implements ConvergenceChecker<UnivariatePointValuePair> {
 private int iterationCount;

 @Override
 public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
 iterationCount = iteration;
 return false;
 }
 }

 private static class FunctionWithGoldenSectionTestFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return x * x - 1;
 }
 }

 private static class FunctionWithNaNTestFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Double.NaN;
 }
 }

 private static class FunctionWithInfTestFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return Double.POSITIVE_INFINITY;
 }
 }
}