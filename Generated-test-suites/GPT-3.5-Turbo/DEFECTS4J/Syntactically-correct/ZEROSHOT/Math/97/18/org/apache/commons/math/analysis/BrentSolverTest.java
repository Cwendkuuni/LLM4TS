import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;
  
  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-5) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-5) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NOT_CONVERGED;
        }
      }
    };
  }
  
  @Test
  public void testFindRoot() throws FunctionException {
    boolean found = solver.findRoot(function, checker, 100, 1.0, -3.0, 3.0, 7.0);
    assertTrue("Root not found", found);
    double root = solver.getRoot();
    assertEquals("Incorrect root", 2.0, root, 1e-5);
  }
  
  @Test
  public void testFindRootNoInterval() throws FunctionException {
    boolean found = solver.findRoot(function, checker, 100, 0.0, 0.0, 0.0, 0.0);
    assertFalse("Root found without interval", found);
  }
  
  @Test
  public void testFindRootInvalidInterval() throws FunctionException {
    boolean found = solver.findRoot(function, checker, 100, 2.0, -3.0, 1.0, -2.0);
    assertFalse("Root found with invalid interval", found);
  }
  
  @Test
  public void testFindRootConvergenceChecker() throws FunctionException {
    boolean found = solver.findRoot(function, new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < Math.abs(fHigh)) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.HIGH;
        }
      }
    }, 100, 1.0, -3.0, 3.0, 7.0);
    assertTrue("Root not found with custom convergence checker", found);
    double root = solver.getRoot();
    assertEquals("Incorrect root with custom convergence checker", 2.0, root, 1e-5);
  }
  
}