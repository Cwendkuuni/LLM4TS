import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {
  
  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4.0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh) < 1e-6) {
          return HIGH;
        } else if (Math.abs(fLow) < 1e-6) {
          return LOW;
        } else {
          return UNCONVERGED;
        }
      }
    };
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, 0.0, -4.0, 2.0, 0.0);
    double root = solver.getRoot();
    assertEquals(2.0, root, 1e-10);
  }
  
  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    double root = solver.getRoot();
    assertEquals(Double.NaN, root, 0.0);
  }

}