package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

 private static final double EPSILON = 1e-9;

 @Test
 public void testFindRoot() throws FunctionException {
  ComputableFunction function = new MockFunction();
  ConvergenceChecker checker = new ConvergenceChecker() {
   @Override
   public int converged(double x, double fx, double xPrev, double fxPrev) {
    return (fx * fxPrev > 0) ? ConvergenceChecker.INDEFINITE : ConvergenceChecker.ITERATION;
   }
  };
  BrentSolver solver = new BrentSolver();
  double x0 = 0.0;
  double f0 = function.valueAt(x0);
  double x1 = 1.0;
  double f1 = function.valueAt(x1);

  solver.findRoot(function, checker, 100, x0, f0, x1, f1);
  double root = solver.getRoot();

  // check root
  double expectedRoot = 0.25; // a known root for this test function
  assertEquals(expectedRoot, root, EPSILON);
 }

 @Test
 public void testFindRoot_convergenceAtRoot() throws FunctionException {
  ComputableFunction function = new MockFunction();
  ConvergenceChecker checker = new ConvergenceChecker() {
   @Override
   public int converged(double x, double fx, double xPrev, double fxPrev) {
    return (fx == 0) ? ConvergenceChecker.CONVERGED : ConvergenceChecker.ITERATION;
   }
  };
  BrentSolver solver = new BrentSolver();
  double x0 = 0.0;
  double f0 = function.valueAt(x0);
  double x1 = 1.0;
  double f1 = function.valueAt(x1);

  solver.findRoot(function, checker, 100, x0, f0, x1, f1);
  double root = solver.getRoot();

  // check root
  double expectedRoot = 0.0; // a known root for this test function
  assertEquals(expectedRoot, root, EPSILON);
 }

 @Test
 public void testFindRoot_noRoot() throws FunctionException {
  ComputableFunction function = new MockFunction();
  ConvergenceChecker checker = new ConvergenceChecker() {
   @Override
   public int converged(double x, double fx, double xPrev, double fxPrev) {
    return ConvergenceChecker.ITERATION;
   }
  };
  BrentSolver solver = new BrentSolver();
  double x0 = 0.0;
  double f0 = function.valueAt(x0);
  double x1 = 1.0;
  double f1 = function.valueAt(x1);

  solver.findRoot(function, checker, 100, x0, f0, x1, f1);
  double root = solver.getRoot();

  // check root is NaN
  assertNotNull(root);
  assertFalse(Double.isFinite(root));
 }

 @Test(expected = FunctionException.class)
 public void testFindRoot_functionException() throws FunctionException {
  ComputableFunction function = new MockFunctionWithException();
  ConvergenceChecker checker = new ConvergenceChecker() {
   @Override
   public int converged(double x, double fx, double xPrev, double fxPrev) {
    return ConvergenceChecker.ITERATION;
   }
  };
  BrentSolver solver = new BrentSolver();
  double x0 = 0.0;
  double f0 = function.valueAt(x0);
  double x1 = 1.0;
  double f1 = function.valueAt(x1);

  solver.findRoot(function, checker, 100, x0, f0, x1, f1);
 }
}

class MockFunction implements ComputableFunction {
 @Override
 public double valueAt(double x) {
  return x * x - 1.0;
 }
}

class MockFunctionWithException implements ComputableFunction {
 @Override
 public double valueAt(double x) {
  throw new FunctionException("Mock Function Exception");
 }
}