package org.example.junit.test.brentsolver;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {
 // Add additional test cases if needed

 @Test
 public void testFindRootNormal() {
 // Test case for method findRoot() with normal input values
 ComputableFunction function = new MyComputableFunction();
 ConvergenceChecker checker = new MyConvergenceChecker();
 int maxIter = 10;
 double x0 = 1.0;
 double f0 = function.valueAt(x0);
 double x1 = 2.0;
 double f1 = function.valueAt(x1);

 try {
 BrentSolver solver = new BrentSolver();
 boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
 assertTrue(result);
 double root = solver.getRoot();
 // Add assertions to check the root value is between x0 and x1
 Assert.assertNotEquals(Double.NaN, root);
 Assert.assertTrue(x0 <= root && root <= x1);
 } catch (FunctionException e) {
 Assert.fail("FunctionException was thrown unexpectedly");
 }
 }

 // Test case for method findRoot() with edge cases
 @Test
 public void testFindRootEdgeCases() {
 // Test case for method findRoot() with edge cases
 ComputableFunction function = new MyComputableFunction();
 ConvergenceChecker checker = new MyConvergenceChecker();
 int maxIter = 10;
 double x0 = -1.0;
 double f0 = function.valueAt(x0);
 double x1 = 0.0;
 double f1 = function.valueAt(x1);

 try {
 BrentSolver solver = new BrentSolver();
 boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
 // Add assertions to check the root is not in the interval [x0, x1]
 Assert.assertFalse(result);
 } catch (FunctionException e) {
 // Expected FunctionException, as the function has no roots in this interval
 }
 }

 // Test case for method findRoot() with exception handling
 @Test(expected = FunctionException.class)
 public void testFindRootException() {
 // Test case for method findRoot() with exception handling
 ComputableFunction function = new MyComputableFunctionThrowingException();
 ConvergenceChecker checker = new MyConvergenceChecker();
 int maxIter = 10;
 double x0 = 1.0;
 double f0 = Double.NaN;
 double x1 = 2.0;
 double f1 = Double.NaN;

 try {
 BrentSolver solver = new BrentSolver();
 solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
 fail("FunctionException was not thrown");
 } catch (FunctionException e) {
 // Expected FunctionException, as the input functions are NaN
 }
 }

 // Test case for method findRoot() with convergence criteria
 @Test
 public void testFindRootConvergenceCriteria() {
 // Test case for method findRoot() with convergence criteria
 ComputableFunction function = new MyComputableFunction();
 ConvergenceChecker checker = new MyConvergenceChecker() {
 @Override
 public ConvergenceChecker.ConvergenceResult converged(double x, double fx, double xPrev, double fxPrev) {
 return ConvergenceChecker.LOW;
 }
 };
 int maxIter = 10;
 double x0 = 1.0;
 double f0 = function.valueAt(x0);
 double x1 = 2.0;
 double f1 = function.valueAt(x1);

 try {
 BrentSolver solver = new BrentSolver();
 boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
 assertTrue(result);
 double root = solver.getRoot();
 // Add assertions to check the root value is between x0 and x1
 Assert.assertNotEquals(Double.NaN, root);
 Assert.assertTrue(x0 <= root && root <= x1);
 } catch (FunctionException e) {
 Assert.fail("FunctionException was thrown unexpectedly");
 }
 }

 // Test case for method findRoot() with unconverged state
 @Test
 public void testFindRootUnconverged() {
 // Test case for method findRoot() with unconverged state
 ComputableFunction function = new MyUnconvergedComputableFunction();
 ConvergenceChecker checker = new MyConvergenceChecker();
 int maxIter = 10;
 double x0 = 1.0;
 double f0 = function.valueAt(x0);
 double x1 = 2.0;
 double f1 = function.valueAt(x1);

 try {
 BrentSolver solver = new BrentSolver();
 boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
 assertFalse(result);
 } catch (FunctionException e) {
 fail("FunctionException was not thrown");
 }
 }

}