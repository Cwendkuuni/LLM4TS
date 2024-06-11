import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.roots.ConvergenceChecker.ConvergenceType;

/**
 * This class tests the {@link BrentSolver} class.
 */
public class BrentSolverTest {

  /**
   * Tests the {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * method with the function {@code x -> x^3 - 2}.
   */
  @Test
  public void testFindRootXCubedMinusTwo() {
    ComputableFunction function = FunctionFactory.polynomial(3, -2);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public ConvergenceType convergenceType(double xLow, double fLow, double xHigh, double fHigh) {
        if (xLow > xHigh) {
          return ConvergenceType.LOW;
        } else if (xHigh < xLow) {
          return ConvergenceType.HIGH;
        }
        return ConvergenceType.INDEFINITE;
      }
    };
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1))
        ? solver.getRoot() : Double.NaN;

    assertTrue(Double.isFinite(root));
    assertEquals(-1.0, root, 1e-10);
  }

  /**
   * Tests the {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * method with the function {@code x -> x^2 - 1}.
   */
  @Test
  public void testFindRootXSquaredMinusOne() {
    ComputableFunction function = FunctionFactory.polynomial(2, -1);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public ConvergenceType convergenceType(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-10) {
          return ConvergenceType.LOW;
        } else if (Math.abs(fHigh) < 1e-10) {
          return ConvergenceType.HIGH;
        }
        return ConvergenceType.INDEFINITE;
      }
    };
    BrentSolver solver = new BrentSolver();

    double x0 = 0.5;
    double x1 = 1.5;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1))
        ? solver.getRoot() : Double.NaN;

    assertTrue(Double.isFinite(root));
    assertEquals(0.0, root, 1e-10);
  }

  /**
   * Tests the {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * method with the function {@code x -> sin(x)} in the interval {@code [0, 1]}.
   */
  @Test
  public void testFindRootSin() {
    ComputableFunction function = FunctionFactory.trigonometric(Math::sin);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public ConvergenceType convergenceType(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-10) {
          return ConvergenceType.LOW;
        } else if (Math.abs(fHigh) < 1e-10) {
          return ConvergenceType.HIGH;
        }
        return ConvergenceType.INDEFINITE;
      }
    };
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 1.0;
    double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1))
        ? solver.getRoot() : Double.NaN;

    assertTrue(Double.isFinite(root));
    assertFalse(root == 0.0);
    assertTrue(Math.abs(Math.sin(root)) < 1e-10);
  }

  /**
   * Tests the {@link BrentSolver#findRoot(ComputableFunction, ConvergenceChecker, int, double, double, double, double)}
   * method with the function {@code x -> x^3 - 2} in the interval {@code [1, 2]} which has no root.
   */
  @Test
  public void testFindRootXCubedMinusTwoNoRoot() {
    ComputableFunction function = FunctionFactory.polynomial(3, -2);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public ConvergenceType convergenceType(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-10) {
          return ConvergenceType.LOW;
        } else if (Math.abs(fHigh) < 1e-10) {
          return ConvergenceType.HIGH;
        }
        return ConvergenceType.INDEFINITE;
      }
    };
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    boolean rootFound = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertFalse(rootFound);
  }

}