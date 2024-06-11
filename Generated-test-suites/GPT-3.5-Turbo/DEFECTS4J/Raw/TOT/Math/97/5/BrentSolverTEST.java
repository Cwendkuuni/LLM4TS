import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {
  
  private BrentSolver solver;
  
  @Before
  public void setUp() {
    solver = new BrentSolver();
  }
  
  // Testing findRoot method
  
  @Test
  public void testFindRoot_typical() {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return -1; // Low converged
        } else if (Math.abs(fHigh) < 1e-6) {
          return 1; // High converged
        } else {
          return 0; // Not converged
        }
      }
    };
    int maxIter = 1000;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);
    
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }
  
  @Test
  public void testFindRoot_edge() {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(fLow) < 1e-6) {
          return -1; // Low converged
        } else if (Math.abs(fHigh) < 1e-6) {
          return 1; // High converged
        } else {
          return 0; // Not converged
        }
      }
    };
    int maxIter = 1000;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);
    
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertTrue(rootFound);
    assertEquals(1.0, solver.getRoot(), 1e-6);
  }
  
  @Test
  public void testFindRoot_error() {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        throw new FunctionException("Function evaluation error");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return 0;
      }
    };
    int maxIter = 1000;
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 10.0;
    double f1 = function.valueAt(x1);
    
    try {
      boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      fail("No exception thrown");
    } catch (FunctionException e) {
      assertEquals("Function evaluation error", e.getMessage());
    }
  }
  
  // Testing getRoot method
  
  @Test
  public void testGetRoot_typical() {
    solver.setRoot(2.0);
    assertEquals(2.0, solver.getRoot(), 1e-6);
  }
  
  // Testing additional methods
  
  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertTrue(Double.isNaN(solver.getRoot()));
  }
  
}