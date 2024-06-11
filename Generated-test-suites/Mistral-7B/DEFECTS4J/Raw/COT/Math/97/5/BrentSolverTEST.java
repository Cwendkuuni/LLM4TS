package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.functions.scalar.SineFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_simple() throws Exception {
    SineFunction function = new SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 2.0;
    double root = Math.PI;

    assertTrue(solver.findRoot(function, checker, 10, x0, 0.0, x1, FunctionUtils.sine(x1)));
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_convergedLow() throws Exception {
    SineFunction function = new SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 1.5;
    double root = 1.0;

    assertTrue(solver.findRoot(function, checker, 10, x0, FunctionUtils.sine(x0), x1, FunctionUtils.sine(x1)));
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_convergedHigh() throws Exception {
    SineFunction function = new SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    double x0 = 2.5;
    double x1 = 3.0;
    double root = 2.0;

    assertTrue(solver.findRoot(function, checker, 10, x0, FunctionUtils.sine(x0), x1, FunctionUtils.sine(x1)));
    assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_failed() throws Exception {
    SineFunction function = new SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 2.0;
    double root = Math.PI;

    assertFalse(solver.findRoot(function, checker, 1, x0, 0.0, x1, FunctionUtils.sine(x1)));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() throws Exception {
    SineFunction function = new SineFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    Exception exception = new Exception();
    FunctionUtils.throwFunctionException(function, exception);
    double x0 = 0.0;
    double x1 = 2.0;
    double root = Math.PI;

    try {
      solver.findRoot(function, checker, 10, x0, 0.0, x1, FunctionUtils.sine(x1));
    } catch (FunctionException fe) {
      // expected
    }
  }

  @Test
  public void testFindRoot_nonConverged() throws Exception {
    FunctionUtils.TanFunction function = new FunctionUtils.TanFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = Math.PI / 2.0;
    double root = Math.PI / 4.0;

    assertFalse(solver.findRoot(function, checker, 10, x0, FunctionUtils.tan(x0), x1, FunctionUtils.tan(x1)));
  }

  @Test
  public void testFindRoot_zeroFunction() throws Exception {
    FunctionUtils.ZeroFunction function = new FunctionUtils.ZeroFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 0.0;
    double root = 0.0;

    assertTrue(solver.findRoot(function, checker, 10, x0, 0.0, x1, 0.0));
    assertEquals(root, solver.getRoot(), epsilon);
  }

}