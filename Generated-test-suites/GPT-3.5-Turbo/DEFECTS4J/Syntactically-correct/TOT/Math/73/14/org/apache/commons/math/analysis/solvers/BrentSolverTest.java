import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot(){
    BrentSolver solver = new BrentSolver();
    
    // Typical use case
    ComputableFunction function = new ComputableFunction(){
      public double valueAt(double x){
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker(){
      public int converged(double xLow, double fLow, double xHigh, double fHigh){
        if(fLow * fHigh < 0){
          return LOW;
        }
        return CONTINUE;
      }
    };
    int maxIter = 50;
    double x0 = 1.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);
    
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(2.0, solver.getRoot(), 0.001);
    
    // Edge case: Lower bound is the root
    x0 = -2.0;
    f0 = function.valueAt(x0);
    x1 = 3.0;
    f1 = function.valueAt(x1);
    
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(x0, solver.getRoot(), 0.001);
    
    // Edge case: Higher bound is the root
    x0 = -2.0;
    f0 = function.valueAt(x0);
    x1 = 5.0;
    f1 = function.valueAt(x1);
    
    assertTrue(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
    assertEquals(x1, solver.getRoot(), 0.001);
    
    // Error scenario: Function does not converge within the maximum number of iterations
    x0 = 1.0;
    f0 = function.valueAt(x0);
    x1 = 3.0;
    f1 = function.valueAt(x1);
    maxIter = 5;
    
    assertFalse(solver.findRoot(function, checker, maxIter, x0, f0, x1, f1));
  }
}