import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;

  @Before
  public void setUp() {
    solver = new BrentSolver();
  }

  @Test
  public void findRootTest() throws FunctionException {
    // Test case 1
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow < 0 && fHigh > 0) {
          return ConvergenceChecker.LOW;
        } else if (fLow > 0 && fHigh < 0) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.ONGOING;
        }
      }
    };
    
    boolean success1 = solver.findRoot(function1, checker1, 100, 1, -3, 2, 0);
    assertTrue(success1);
    assertEquals(2.0, solver.getRoot(), 0);
    
    // Test case 2
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fHigh) < 0.01) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.ONGOING;
        }
      }
    };
    
    boolean success2 = solver.findRoot(function2, checker2, 100, 1, -1, 3, 2);
    assertTrue(success2);
    assertEquals(Math.PI, solver.getRoot(), 0.01);
  }
}