import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(fHigh) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    
    BrentSolver solver = new BrentSolver();
    
    boolean rootFound = solver.findRoot(function, checker, 100, 0.0, -4.0, 10.0, 96.0);
    
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }
  
  // Add more test methods for other methods in BrentSolver class
  
}