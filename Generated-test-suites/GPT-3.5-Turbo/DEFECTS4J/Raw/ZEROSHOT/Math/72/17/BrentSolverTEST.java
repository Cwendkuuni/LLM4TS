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
      public int converged(double xLow, double fLow, double xHigh, double fHigh) throws FunctionException {
        if (Math.abs(xLow - xHigh) < 1e-5) {
          return LOW;
        } else if (Math.abs(fLow) < 1e-5) {
          return HIGH;
        } else {
          return NONE;
        }
      }
    };
    
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);
    
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-5);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    
    double root = 3.0;
    
    // Set root using reflection
    try {
      Class<?> cls = solver.getClass();
      Field field = cls.getDeclaredField("root");
      field.setAccessible(true);
      field.set(solver, root);
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    assertEquals(root, solver.getRoot(), 0.0);
  }

}