import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.functionname;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;

  @Before
  public void setUp() {
    solver = new BrentSolver();
  }

  @Test
  public void testFindRoot() throws FunctionException {
    double x0 = -10.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);
    
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return functionname(x);
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLo, double yLo, double xHi, double yHi) {
        if (Math.abs(yLo) < 1e-6) {
          return LOW;
        } else if (Math.abs(yHi) < 1e-6) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    
    boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 1e-6);
  }

  // Write tests for other methods in BrentSolver class
  
  
}