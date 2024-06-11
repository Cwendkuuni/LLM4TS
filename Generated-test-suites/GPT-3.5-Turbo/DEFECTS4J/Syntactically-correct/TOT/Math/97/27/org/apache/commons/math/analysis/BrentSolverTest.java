import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot_typicalCase() {
    BrentSolver solver = new BrentSolver();
    double x0 = 0;
    double f0 = 4;
    double x1 = 1;
    double f1 = 1;
    ComputableFunction function = new ComputableFunctionImpl();
    ConvergenceChecker checker = new ConvergenceCheckerImpl();
    int maxIter = 100;
    
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertTrue(result);
    assertEquals(0.6180339887498949, solver.getRoot(), 1e-9);
  }
  
  @Test
  public void testFindRoot_edgeCase() {
    BrentSolver solver = new BrentSolver();
    double x0 = -1;
    double f0 = 1;
    double x1 = 1;
    double f1 = -1;
    ComputableFunction function = new ComputableFunctionImpl();
    ConvergenceChecker checker = new ConvergenceCheckerImpl();
    int maxIter = 10;
    
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 1e-9);
  }
  
  @Test
  public void testFindRoot_errorScenario() {
    BrentSolver solver = new BrentSolver();
    double x0 = 0;
    double f0 = 0;
    double x1 = 1;
    double f1 = 1;
    ComputableFunction function = new ComputableFunctionImpl();
    ConvergenceChecker checker = new ConvergenceCheckerImpl();
    int maxIter = 5;
    
    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertFalse(result);
  }
  
  private class ComputableFunctionImpl implements ComputableFunction {

    public double valueAt(double x) {
      return x * x - 1;
    }
    
  }
  
  private class ConvergenceCheckerImpl implements ConvergenceChecker {

    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
      if (Math.abs(fLow) < 1e-9) {
        return ConvergenceChecker.LOW;
      } else if (Math.abs(fHigh) < 1e-9) {
        return ConvergenceChecker.HIGH;
      } else {
        return ConvergenceChecker.NONE;
      }
    }
    
  }

}