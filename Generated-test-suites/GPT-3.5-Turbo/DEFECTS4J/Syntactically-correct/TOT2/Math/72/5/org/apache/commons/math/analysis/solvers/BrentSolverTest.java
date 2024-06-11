import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow * fHigh < 0) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.LOW;
        }
      }
    };
    
    double x0 = 0.0;
    double f0 = -4.0;
    double x1 = 5.0;
    double f1 = 21.0;
    
    boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testFindRootException() {
    BrentSolver solver = new BrentSolver();
    
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Function execution failed");
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return 0;
      }
    };
    
    double x0 = 0.0;
    double f0 = -4.0;
    double x1 = 5.0;
    double f1 = 21.0;
    
    try {
      solver.findRoot(function, checker, 100, x0, f0, x1, f1);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // Exception caught as expected
    }
  }

  @Test
  public void testFindRootEdgeCases() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x;
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return 0;
      }
    };
    
    double x0 = 1.0;
    double f0 = 1.0;
    double x1 = 1.0;
    double f1 = 1.0;
    
    boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    
    assertTrue(rootFound);
    assertEquals(1.0, solver.getRoot(), 1e-10);
  }

}