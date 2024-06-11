import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    ComputableFunction function = new RationalFunction("x^2 - 2");
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double f0 = 3.0;
    double x1 = 3.0;
    double f1 = 9.0;

    assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    assertTrue(Math.abs(solver.getRoot() - 2.0) < 0.001);
  }
}