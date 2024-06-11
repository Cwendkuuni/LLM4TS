
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  private BrentSolver solver;
  
  @Before
  public void setUp() {
    solver = new BrentSolver();
  }
  
  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return 2 * x - 1;
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
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
    
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);
    
    boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    
    assertTrue(rootFound);
    assertEquals(0.5, solver.getRoot(), 1e-6);
  }
  
  // Additional test cases for potential edge cases and exceptions

  @Test
  public void testFindRootWithZeroInterval() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return x;
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    };
    
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 0.0;
    double f1 = function.valueAt(x1);
    
    boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    
    assertFalse(rootFound);
  }
  
  @Test(expected = FunctionException.class)
  public void testFindRootWithInvalidFunction() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Invalid function");
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    };
    
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);
    
    boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
  }

}
