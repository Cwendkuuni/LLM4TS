package org.apache.commons.cli;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

 private BrentOptimizer optimizer;

 @Before
 public void setUp() {
 optimizer = new BrentOptimizer(1e-5, 1e-5);
 }

 @Test
 public void testDoOptimize_InitialGuessBetweenMinAndMax() {
 // Given
 double function = x -> x * x;
 double min = -2.0;
 double max = 2.0;
 double initialGuess = 0.0;

 // When
 UnivariatePointValuePair result = optimizer.doOptimize(function, min, max, initialGuess);

 // Then
 assertEquals(-1.4142135625, result.getX(), 0.1); // The result should be close to the expected value, sqrt(-1)
 assertFalse(Double.isNaN(result.getValue()));
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testDoOptimizer_RelativeThresholdTooSmall() {
 optimizer = new BrentOptimizer(1e-8, 1e-5);
 // This test case is not passing because the relative threshold is too small
 // and the optimization algorithm will not converge.
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testDoOptimizer_AbsoluteThresholdNotPositive() {
 optimizer = new BrentOptimizer(1e-5, -1e-5);
 // This test case is not passing because the absolute threshold is not positive
 }

 @Test
 public void testDoOptimize_MinimizingFunction() {
 // Given
 double function = x -> x * x;
 double min = -2.0;
 double max = 2.0;
 double initialGuess = 1.0;

 // When
 UnivariatePointValuePair result = optimizer.doOptimize(function, min, max, initialGuess);

 // Then
 assertEquals(0.0, result.getX(), 0.1);
 assertFalse(Double.isNaN(result.getValue()));
 }

 @Test
 public void testDoOptimize_MaximizingFunction() {
 // Given
 double function = x -> Math.exp(-x);
 double min = Double.NEGATIVE_INFINITY;
 double max = 0.0;
 double initialGuess = 1.0;

 // When
 UnivariatePointValuePair result = optimizer.doOptimize(function, min, max, initialGuess);

 // Then
 assertEquals(0.0, result.getX(), 0.1);
 assertFalse(Double.isNaN(result.getValue()));
 }

 @Test
 public void testDoOptimize_ConvergenceChecker() {
 // Given
 double function = x -> Math.abs(Math.sin(x)) + 1e-8;
 double min = 0.0;
 double max = 2 * Math.PI;
 double initialGuess = Math.PI / 2;
 ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = (iter, previous, current) -> iter > 10;

 // When
 optimizer = new BrentOptimizer(1e-5, 1e-5, convergenceChecker);
 UnivariatePointValuePair result = optimizer.doOptimize(function, min, max, initialGuess);

 // Then
 assertEquals(Math.PI / 2, result.getX(), 0.1);
 assertFalse(Double.isNaN(result.getValue()));
 }
}