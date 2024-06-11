import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() {
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double f0 = -1.0;
    double x1 = 2.0;
    double f1 = 1.0;
    
    ComputableFunction func = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 1;
      }
    };
    
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };
    
    try {
      boolean rootFound = solver.findRoot(func, checker, 100, x0, f0, x1, f1);
      assertTrue(rootFound);
      assertEquals(1.0, solver.getRoot(), 1e-10);
    } catch (FunctionException e) {
      fail("Unexpected FunctionException");
    }
  }
  
  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    assertEquals(Double.NaN, solver.getRoot(), 1e-10);
  }

}