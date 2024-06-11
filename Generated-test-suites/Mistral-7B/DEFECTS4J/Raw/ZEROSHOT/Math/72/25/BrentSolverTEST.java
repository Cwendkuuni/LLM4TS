import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    // Test case 1: x^2 - 1 = 0
    ComputableFunction function = new RationalFunction("x", 1, 1, -1);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return (Math.abs(fx) < 0.001) ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double fx0 = 0.0;
    double x1 = 2.0;
    double fx1 = 3.0;
    boolean found = solver.findRoot(function, checker, 10, x0, fx0, x1, fx1);
    double root = solver.getRoot();
    assertTrue(found);
    assertEquals(1.0, root, 0.001);

    // Test case 2: x^3 - 2 = 0
    function = new RationalFunction("x", 1, 1, -2, 1, 1);
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return (Math.abs(fx) < 0.01) ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
      }
    };
    solver = new BrentSolver();
    x0 = 1.0;
    fx0 = 0.0;
    x1 = 3.0;
    fx1 = 7.0;
    found = solver.findRoot(function, checker, 10, x0, fx0, x1, fx1);
    root = solver.getRoot();
    assertTrue(found);
    assertEquals(-1.0, root, 0.01);
    assertEquals(-1.5811388300841898d, root, 0.00000001); // Approximate solution from Wolfram Alpha

    // Test case 3: x^2 - 2 > 0
    function = new RationalFunction("x", 1, 1, -2);
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return (fx > 0.0) ? ConvergenceChecker.LOW : ConvergenceChecker.NOT_CONVERGED;
      }
    };
    solver = new BrentSolver();
    x0 = 0.0;
    fx0 = 2.0;
    x1 = 1.0;
    fx1 = -1.0;
    found = solver.findRoot(function, checker, 10, x0, fx0, x1, fx1);
    root = solver.getRoot();
    assertTrue(found);
    assertTrue(root > 0.0);

    // Test case 4: x^2 + 1 > 0
    function = new RationalFunction("x", 1, 1, 1);
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return (fx > 0.0) ? ConvergenceChecker.HIGH : ConvergenceChecker.NOT_CONVERGED;
      }
    };
    solver = new BrentSolver();
    x0 = -2.0;
    fx0 = -3.0;
    x1 = 2.0;
    fx1 = 3.0;
    found = solver.findRoot(function, checker, 10, x0, fx0, x1, fx1);
    root = solver.getRoot();
    assertTrue(found);
    assertTrue(root < 0.0);
  }

}