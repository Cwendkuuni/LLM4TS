package org.apache.commons.math3.optimization.univariate;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePairFactory;
import org.apache.commons.math3.optim.nonlinear.scalings.AbstractScalingFunction;
import org.apache.commons.math3.optim.nonlinear.scalings.LogScale;
import org.apache.commons.math3.optim.nonlinear.scalings.LinearScale;
import org.apache.commons.math3.optim.nonlinear.scalings.SquareRootScale;
import org.apache.commons.math3.special.E;

public class BrentOptimizerTest {

 private UnivariateOptimizer optimizer;
 private UnivariateFunction function;

 @Before
 public void setup() {
 function = new MyFunction();
 optimizer = new BrentOptimizer(1e-5, 1e-5);
 }

 /**
 * Basic test case for the constructor with two arguments.
 */
 @Test
 public void testBrentOptimizer() {
 optimizer = new BrentOptimizer(1e-5, 1e-5);
 assertNotNull(optimizer);
 }

 /**
 * Basic test case for the constructor with one argument.
 */
 @Test
 public void testBrentOptimizerOneArg() {
 optimizer = new BrentOptimizer(1e-5);
 assertNotNull(optimizer);
 }

 /**
 * Test the optimization of a simple function.
 */
 @Test
 public void testSimpleFunction() {
 PointValuePair result = optimizer.optimize(function, new InitialGuess(0.5), null);
 assertNotNull(result);
 double expected = 0.25;
 assertEquals(expected, result.getPoint(), 1e-6);
 }

 /**
 * Test the optimization of a simple function with user defined convergence criterion.
 */
 @Test
 public void testSimpleFunctionWithConvergenceChecker() {
 UnivariateOptimizer optimizerWithChecker
 = new BrentOptimizer(1e-5, 1e-5, new MyConvergenceChecker());
 PointValuePair result = optimizerWithChecker.optimize(function, new InitialGuess(0.5), null);
 assertNotNull(result);
 double expected = 0.25;
 assertEquals(expected, result.getPoint(), 1e-6);
 }

 /**
 * Test the optimization of a function with relative tolerance less than machine epsilon.
 */
 @Test(expected = NumberIsTooSmallException.class)
 public void testSmallRelativeTolerance() {
 optimizer = new BrentOptimizer(1e-15, 1e-5);
 optimizer.optimize(function, new InitialGuess(0.5), null);
 }

 /**
 * Test the optimization of a function with absolute tolerance less than zero.
 */
 @Test(expected = NotStrictlyPositiveException.class)
 public void testNegativeAbsoluteTolerance() {
 optimizer = new BrentOptimizer(1e-5, -1e-5);
 optimizer.optimize(function, new InitialGuess(0.5), null);
 }

 /**
 * Test the optimization of a function with relative tolerance equal to zero.
 */
 @Test
 public void testZeroRelativeTolerance() {
 optimizer = new BrentOptimizer(0, 1e-5);
 PointValuePair result = optimizer.optimize(function, new InitialGuess(0.5), null);
 assertNotNull(result);
 assertTrue(Double.isNaN(result.getPoint()));
 }

 /**
 * Test the optimization of a function with a scaling function.
 */
 @Test
 public void testFunctionWithScaling() {
 UnivariateFunction scaledFunction = new MyFunctionWithScaling(new SquareRootScale());
 PointValuePair result = optimizer.optimize(scaledFunction, new InitialGuess(1), null);
 assertNotNull(result);
 double expected = 1 / Math.sqrt(2);
 assertEquals(expected, result.getPoint(), 1e-6);
 }

 /**
 * Test the optimization of a function with a scaling function and user defined convergence criterion.
 */
 @Test
 public void testFunctionWithScalingAndConvergenceChecker() {
 UnivariateFunction scaledFunction = new MyFunctionWithScaling(new SquareRootScale());
 UnivariateOptimizer optimizerWithChecker
 = new BrentOptimizer(1e-5, 1e-5, new MyConvergenceChecker());
 PointValuePair result = optimizerWithChecker.optimize(scaledFunction, new InitialGuess(1), null);
 assertNotNull(result);
 double expected = 1 / Math.sqrt(2);
 assertEquals(expected, result.getPoint(), 1e-6);
 }

 /**
 * Test the optimization of a function with a large relative tolerance.
 */
 @Test
 public void testLargeRelativeTolerance() {
 optimizer = new BrentOptimizer(1e6, 1e-5);
 PointValuePair result = optimizer.optimize(function, new InitialGuess(0.5), null);
 assertNotNull(result);
 }

 /**
 * Test the optimization of a function with a large absolute tolerance.
 */
 @Test
 public void testLargeAbsoluteTolerance() {
 optimizer = new BrentOptimizer(1e-5, 1e6);
 PointValuePair result = optimizer.optimize(function, new InitialGuess(0.5), null);
 assertNotNull(result);
 }

 /**
 * Test the optimization of a function with a very large absolute tolerance.
 */
 @Test
 public void testVeryLargeAbsoluteTolerance() {
 optimizer = new BrentOptimizer(1e9, 1e-5);
 PointValuePair result = optimizer.optimize(function, new InitialGuess(0.5), null);
 assertNotNull(result);
 }

 /**
 * A simple univariate function for testing.
 */
 private static class MyFunction implements UnivariateFunction {
 @Override
 public double value(double x) {
 return E.EULER_NUMBER.doubleValue() - x;
 }
 }

 /**
 * A univariate function with a scaling function.
 */
 private static class MyFunctionWithScaling implements UnivariateFunction {
 private final UnivariateFunction function;
 private final AbstractScalingFunction scalingFunction;

 public MyFunctionWithScaling(AbstractScalingFunction scalingFunction) {
 this.function = new MyFunction();
 this.scalingFunction = scalingFunction;
 }

 @Override
 public double value(double x) {
 double scaledX = scalingFunction.transform(x);
 return function.value(scaledX);
 }
 }

 /**
 * A convergence checker for testing.
 */
 private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
 @Override
 public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
 return false;
 }
 }

}