import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.FunctionConstants;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 1.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xNew, double fNew) {
        return ConvergenceChecker.LOW;
      }
    };

    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double f0 = 2.0; // Math.pow(x0, 2) - 1.0
    double x1 = 2.0;
    double f1 = 0.0; // Math.pow(x1, 2) - 1.0

    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertEquals(true, result);

    double root = solver.getRoot();
    double expectedRoot = FunctionConstants.SQRT_2;
    assertEquals(expectedRoot, root, 0.001); // small tolerance
  }
}