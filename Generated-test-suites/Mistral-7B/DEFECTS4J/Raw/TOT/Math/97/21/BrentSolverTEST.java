package org.spaceroots.mantissa.roots;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction; // Assuming RationalFunction is used as ComputableFunction
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  @Test
  public void testFindRoot_SimpleCase() {
    RationalFunction function = new RationalFunction("x^2 - 2");
    ConvergenceChecker checker = new ConvergenceChecker.Standard();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    double root = 1.5;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), epsilon);
    assertEquals(true, result);
  }

  @Test
  public void testFindRoot_ComplexFunction() {
    RationalFunction function = new RationalFunction("x^3 + 2 * x^2 - 3 * x + 1");
    ConvergenceChecker checker = new ConvergenceChecker.Standard();
    BrentSolver solver = new BrentSolver();

    double x0 = -2.0;
    double x1 = 1.0;
    double root = 0.5;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), epsilon);
    assertEquals(true, result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() {
    RationalFunction function = new RationalFunction("x^2 - 2 / 0");
    ConvergenceChecker checker = new ConvergenceChecker.Standard();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRoot_IllegalArgumentException() {
    RationalFunction function = new RationalFunction("x");
    ConvergenceChecker checker = new ConvergenceChecker.Standard();
    BrentSolver solver = new BrentSolver();

    boolean result = solver.findRoot(function, checker, 100, 1.0, Double.NaN, 3.0, Double.NaN);
  }

  @Test
  public void testFindRoot_ConvergenceChecker() {
    RationalFunction function = new RationalFunction("x^2 - 2");
    ConvergenceChecker checker = new ConvergenceChecker.Custom(); // Custom ConvergenceChecker implementation
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    double root = 1.5;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), epsilon);
    assertEquals(true, result);
  }

}