package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class BrentOptimizerTest {

 @Test
 public void testConstructor() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6);
 assertEquals(1.0E-6, optimizer.getRelativeThreshold(), 0.0);
 assertEquals(1.0E-6, optimizer.getAbsoluteThreshold(), 0.0);
 }

 @Test(expected = NotStrictlyPositiveException.class)
 public void testConstructorNegativeAbsoluteThreshold() {
 new BrentOptimizer(1.0E-6, -1.0E-6);
 }

 @Test(expected = NumberIsTooSmallException.class)
 public void testConstructorSmallRelativeThreshold() {
 new BrentOptimizer(0.5 * FastMath.ulp(1d), 1.0E-6);
 }

 @Test
 public void testDoOptimizeBasic() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, null);
 UnivariateFunction function = f -> Math.pow(f, 2) - Math.log(Math.abs(f) + 1.0);
 double min = optimizer.optimize(function, -5.0, 5.0)[0];
 assertEquals(-2.718281828459045, min, 1.0E-6);
 }

 @Test
 public void testDoOptimizeWithConvergenceChecker() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, (iter, a, b) -> {
 if (iter % 10 == 0) {
 return true;
 }
 return false;
 });
 UnivariateFunction function = f -> Math.pow(f, 2) - Math.log(Math.abs(f) + 1.0);
 double min = optimizer.optimize(function, -5.0, 5.0)[0];
 assertEquals(-2.718281828459045, min, 1.0E-6);
 // Assert that convergence checker was called 11 times (once for the initial point, and 10 times during the optimization)
 assertTrue(optimizer.getIterations() >= 11);
 }

 @Test
 public void testDoOptimizeWithConvergenceCheckerEarlyTermination() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, (iter, a, b) -> {
 if (iter % 3 == 0) {
 return true;
 }
 return false;
 });
 UnivariateFunction function = f -> Math.pow(f, 2) - Math.log(Math.abs(f) + 1.0);
 double min = optimizer.optimize(function, -5.0, 5.0)[0];
 assertEquals(-2.718281828459045, min, 1.0E-6);
 // Assert that convergence checker was called 4 times (once for the initial point, and 3 times during the optimization)
 assertTrue(optimizer.getIterations() >= 4);
 }

 @Test
 public void testDoOptimizeWithCustomGoalType() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, null);
 UnivariateFunction function = f -> Math.pow(f, 2);
 optimizer.setGoalType(GoalType.MAXIMIZE);
 double max = optimizer.optimize(function, -5.0, 5.0)[0];
 assertEquals(25.0, max, 1.0E-6);
 }

 @Test
 public void testDoOptimizeWithBadStartValue() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, null);
 UnivariateFunction function = f -> Math.pow(f, 2);
 double[] result = optimizer.optimize(function, Double.NaN, 5.0);
 assertNull(result);
 }

 @Test
 public void testDoOptimizeWithBadEndValue() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, null);
 UnivariateFunction function = f -> Math.pow(f, 2);
 double[] result = optimizer.optimize(function, -5.0, Double.NaN);
 assertNull(result);
 }

 @Test
 public void testDoOptimizeWithInvalidInterval() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, null);
 UnivariateFunction function = f -> Math.pow(f, 2);
 double[] result = optimizer.optimize(function, 5.0, 5.0);
 assertArrayEquals(new double[]{5.0, Double.NaN}, result);
 }

 @Test
 public void testDoOptimizeWithDivergingFunction() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, null);
 UnivariateFunction function = f -> 1.0 / f;
 double[] result = optimizer.optimize(function, -5.0, 5.0);
 assertArrayEquals(new double[]{-5.0, Double.NaN}, result);
 // Assert that no iteration was performed
 assertFalse(optimizer.getIterations() > 0);
 }

 @Test
 public void testDoOptimizeWithInvalidFunctionValue() {
 BrentOptimizer optimizer = new BrentOptimizer(1.0E-6, 1.0E-6, null);
 UnivariateFunction function = f -> Double.NaN;
 double[] result = optimizer.optimize(function, -5.0, 5.0);
 assertArrayEquals(new double[]{-5.0, Double.NaN}, result);
 // Assert that no iteration was performed
 assertFalse(optimizer.getIterations() > 0);
 }
}