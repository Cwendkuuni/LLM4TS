import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws Exception {
    double functionValue = 3.0;
    ComputableFunction function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return functionValue * Math.pow(x, 2) + 2 * x + 1;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrev, double fPrev) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 10, x0, functionValue * Math.pow(x0, 2) + 2 * x0 + 1, x1, functionValue * Math.pow(x1, 2) + 2 * x1 + 1);
    assertEquals(1.5, solver.getRoot(), 0.001); // expected root
  }
}