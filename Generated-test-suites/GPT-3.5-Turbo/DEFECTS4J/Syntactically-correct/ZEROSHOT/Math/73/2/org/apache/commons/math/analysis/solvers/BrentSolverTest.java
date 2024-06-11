import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;
  
  @Before
  public void setUp() {
    solver = new BrentSolver();
  }
  
  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 2;
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-10) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-10) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    
    double x0 = 0;
    double f0 = -2;
    double x1 = 2;
    double f1 = 2;
    
    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(Math.sqrt(2), solver.getRoot(), 1e-10);
  }
  
  @Test
  public void testFindRootNoRootInInterval() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 2;
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-10) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-10) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    
    double x0 = 0;
    double f0 = -1;
    double x1 = 1;
    double f1 = 1;
    
    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertFalse(result);
    assertTrue(Double.isNaN(solver.getRoot()));
  }
  
  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        throw new FunctionException();
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-10) {
          return LOW;
        } else if (Math.abs(fHigh) < 1e-10) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    
    double x0 = 0;
    double f0 = -1;
    double x1 = 1;
    double f1 = 1;
    
    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertFalse(result);
    assertTrue(Double.isNaN(solver.getRoot()));
  }

}