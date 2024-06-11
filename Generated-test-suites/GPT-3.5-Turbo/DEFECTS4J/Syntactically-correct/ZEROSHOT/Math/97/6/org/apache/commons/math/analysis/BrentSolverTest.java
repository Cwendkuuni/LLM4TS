import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new ComputableFunction() {
      public double valueAt(double x) {
        // Insert your function implementation here
        return 0.0;
      }
    };
    checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        // Insert your convergence checker implementation here
        return 0;
      }
    };
  }

  @Test
  public void testFindRoot() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);
    boolean found = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertTrue(found);
    assertEquals(0.0, solver.getRoot(), 1e-6);
  }

  @Test
  public void testGetRoot() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double f0 = function.valueAt(x0);
    double f1 = function.valueAt(x1);
    boolean found = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertTrue(found);
    double root = solver.getRoot();
    assertEquals(root, solver.getRoot(), 1e-6);
  }

}