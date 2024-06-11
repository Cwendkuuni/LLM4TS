import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws Exception {

    // Test case 1: x^2 - 2 = 0
    ComputableFunction function = new RationalFunction("x", 1, 2);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        double eps = 1e-12;
        return Math.abs(fx) < eps || Math.abs(x - xLow) < eps ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = -2.0;
    double x1 = 2.0;
    double root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    assertEquals("Root not found", -1.0, root);

    // Test case 2: x^3 - 2 = 0
    function = new RationalFunction("x", 1, 3, 2);
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        double eps = 1e-12;
        return Math.abs(fx) < eps || Math.abs(x - xLow) < eps ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    solver = new BrentSolver();
    x0 = -2.0;
    x1 = 2.0;
    root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    assertEquals("Root not found", -1.0, root);

    // Test case 3: x^3 - 2 = 0 with x0 = 1
    function = new RationalFunction("x", 1, 3, 2);
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        double eps = 1e-12;
        return Math.abs(fx) < eps || Math.abs(x - xLow) < eps ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    solver = new BrentSolver();
    x0 = 1.0;
    x1 = 2.0;
    root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    assertEquals("Root not found", 1.0, root);

    // Test case 4: x^3 - 1 = 0
    function = new RationalFunction("x", 1, 3, 1);
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        double eps = 1e-12;
        return Math.abs(fx) < eps || Math.abs(x - xLow) < eps ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    solver = new BrentSolver();
    x0 = -2.0;
    x1 = 2.0;
    root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    assertEquals("Root not found", 1.0, root);

    // Test case 5: x^3 - 1 = 0 with x0 = 1
    function = new RationalFunction("x", 1, 3, 1);
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        double eps = 1e-12;
        return Math.abs(fx) < eps || Math.abs(x - xLow) < eps ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    solver = new BrentSolver();
    x0 = 1.0;
    x1 = 2.0;
    root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    assertEquals("Root not found", 1.0, root);

    // Test case 6: x^3 - 1 = 0 with x1 = 1
    function = new RationalFunction("x", 1, 3, 1);
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xLow, double fxLow) {
        double eps = 1e-12;
        return Math.abs(fx) < eps || Math.abs(x - xLow) < eps ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };

    solver = new BrentSolver();
    x0 = -2.0;
    x1 = 1.0;
    root = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

    assertEquals("Root not found", 1.0, root);

  }

}