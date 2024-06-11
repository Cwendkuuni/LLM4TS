import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double y1, double x2, double y2) {
        if (Math.abs(y2) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else if (Math.abs(y1) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    int maxIter = 100;
    double x0 = 0;
    double f0 = Math.sin(0);
    double x1 = Math.PI;
    double f1 = Math.sin(Math.PI);
    
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    double root = solver.getRoot();
    
    Assert.assertTrue(rootFound);
    Assert.assertEquals(0.0, root, 1e-6);
  }
  
  // Add additional test cases for other public methods
  
}