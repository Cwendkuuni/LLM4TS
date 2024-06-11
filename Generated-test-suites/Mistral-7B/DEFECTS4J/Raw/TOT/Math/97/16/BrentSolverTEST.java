package org.spaceroots.mantissa.roots.test;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link BrentSolver} class.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setup() {
    solver = new BrentSolver();
    function = FunctionUtils.createSineFunction();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xHigh, double fxHigh) {
        return ConvergenceChecker.LOW;
      }
    };
  }

  /**
   * Test finding a root in an interval with a small tolerance.
   */
  @Test
  public void testFindRootSmallTolerance() {
    double x0 = 0.1;
    double x1 = 1.0;
    double expectedRoot = Math.PI / 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

  /**
   * Test finding a root in an interval with a large tolerance.
   */
  @Test
  public void testFindRootLargeTolerance() {
    double x0 = 0.1;
    double x1 = 1.0;
    double expectedRoot = Math.PI;

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(expectedRoot, solver.getRoot(), 0.1);
  }

  /**
   * Test handling an error when the function is not differentiable at the root.
   */
  @Test
  public void testFindRootNotDifferentiable() {
    double x0 = 0.0;
    double x1 = 0.0;
    double expectedExceptionMessage = "The function must be differentiable at the root.";

    ComputableFunction function = () -> Math.pow(Math.E, Math.log(Math.abs(Math.sin(Math.PI / 2.0))));

    assertThrows(FunctionException.class, () -> {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    });

    assertNotNull(solver.getRoot());
    assertEquals(expectedExceptionMessage, solver.getMessage());
  }

  /**
   * Test handling an error when the function is not defined in the interval.
   */
  @Test
  public void testFindRootUndefinedFunction() {
    double x0 = 0.0;
    double x1 = 1.0;

    ComputableFunction function = () -> {
      throw new UnsupportedOperationException();
    };

    assertFalse(solver.findRoot(function, checker, 10, x0, Double.NaN, x1, Double.NaN));
  }

}