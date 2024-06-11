package org.apache.commons.math.analysis.solvers;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

  private class TestFunction implements UnivariateRealFunction {
      public double value(double x) {
          return x * x - 4;
      }
  }

  @Test
  public void testSolve() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS) {
      @Override
      protected double computeObjectiveValue(double x) {
        return new TestFunction().value(x);
      }
    };

    try {
      solver.solve(100, new TestFunction(), 0, 5);
      fail("Expected ConvergenceException");
    } catch (ConvergenceException e) {
      // expected
    }

    double result = solver.solve(100, new TestFunction(), 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(2.0, result, 0.000001);

    result = solver.solve(100, new TestFunction(), 0, 5, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
    assertEquals(2.0, result, 0.000001);

    result = solver.solve(100, new TestFunction(), 0, 5, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
    assertEquals(2.0, result, 0.000001);

    result = solver.solve(100, new TestFunction(), 0, 5, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
    assertEquals(2.0, result, 0.000001);

    result = solver.solve(100, new TestFunction(), 0, 5, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
    assertEquals(2.0, result, 0.000001);
  }
}