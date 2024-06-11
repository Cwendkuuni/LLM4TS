import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    SimpleFunction function = new SimpleFunction("x^2 - 4") {};
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xOld, double fxOld) {
        return ConvergenceChecker.NONE;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;
    double[] expected = {1.5, 2.0};
    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), 0.001);
  }
}