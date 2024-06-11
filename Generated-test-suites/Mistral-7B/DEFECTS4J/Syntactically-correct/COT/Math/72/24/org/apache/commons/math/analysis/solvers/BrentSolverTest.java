package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.spaceroots.mantissa.functions.scalar.DummyFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

@RunWith(JUnit4.class)
public class BrentSolverTest {

  @Test
  public void testFindRoot_simple() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    DummyFunction function = new DummyFunction(1.0, 0.0);

    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 10;

    boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(1.5, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_convergenceAtLowerBound() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    DummyFunction function = new DummyFunction(1.0, 0.0);

    double x0 = 0.0;
    double x1 = 1.0;
    int maxIter = 10;

    boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_convergenceAtHigherBound() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    DummyFunction function = new DummyFunction(1.0, 0.0);

    double x0 = 3.0;
    double x1 = 2.0;
    int maxIter = 10;

    boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    DummyExceptionFunction function = new DummyExceptionFunction();

    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 10;

    solver.findRoot(function, checker, maxIter, x0, Double.NaN, x1, Double.NaN);
  }

  @Test
  public void testFindRoot_maxIterationsReached() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    DummyFunction function = new DummyFunction(Math.sin, Math.PI);

    double x0 = 0.0;
    double x1 = Math.PI;
    int maxIter = 1;

    boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertFalse(result);
  }

  @Test
  public void testFindRoot_rootOutsideInterval() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    DummyFunction function = new DummyFunction(Math.sin, Math.PI);

    double x0 = 0.0;
    double x1 = 1.0;
    int maxIter = 10;

    boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertFalse(result);
  }

  @Test
  public void testFindRoot_functionValueZero() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    DummyFunction function = new DummyFunction(Math.pow, 0.0);

    double x0 = 0.0;
    double x1 = 1.0;
    int maxIter = 10;

    boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertFalse(result);
  }

  @Test
  public void testFindRoot_rootCloseToLowerBound() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    DummyFunction function = new DummyFunction(Math.sin, Math.PI / 2.0);

    double x0 = 0.0;
    double x1 = Math.PI / 2.0;
    int maxIter = 10;

    boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(Math.PI / 4.0, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_rootCloseToHigherBound() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    DummyFunction function = new DummyFunction(Math.sin, 3.0 * Math.PI / 2.0);

    double x0 = 2.0 * Math.PI;
    double x1 = 3.0 * Math.PI / 2.0;
    int maxIter = 10;

    boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(3.0 * Math.PI / 4.0, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_rootInMiddleOfInterval() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    DummyFunction function = new DummyFunction(Math.sin, Math.PI / 4.0);

    double x0 = 0.0;
    double x1 = Math.PI / 2.0;
    int maxIter = 10;

    boolean result = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(result);
    assertEquals(Math.PI / 4.0, solver.getRoot(), 0.001);
  }

}

class DummyFunction implements ComputableFunction {
  private final double a;
  private final double b;
  private final Function f;

  public DummyFunction(Function f, double a) {
    this.f = f;
    this.a = a;
    this.b = b;
  }

  public DummyFunction(Function f, double a, double b) {
    this.f = f;
    this.a = a;
    this.b = b;
  }

  @Override
  public double valueAt(double x) {
    if (b == Double.NaN) {
      return f.valueAt(x);
    } else if (x > a && x < b) {
      return f.valueAt(x);
    } else {
      throw new UnsupportedOperationException();
    }
  }
}

class DummyExceptionFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    throw new FunctionException("Test exception");
  }
}