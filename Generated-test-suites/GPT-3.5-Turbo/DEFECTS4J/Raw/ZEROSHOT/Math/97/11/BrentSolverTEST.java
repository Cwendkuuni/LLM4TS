import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 16;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (fLow < 0 && fHigh > 0) ? LOW : (fLow > 0 && fHigh < 0) ? HIGH : INDeterminate;
      }
    };
    int maxIter = 1000;
    double x0 = -10;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(rootFound);
    assertEquals(4.0, solver.getRoot(), 1e-6);
  }
  
  @Test
  public void testFindRootNoRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (fLow < 0 && fHigh > 0) ? LOW : (fLow > 0 && fHigh < 0) ? HIGH : INDeterminate;
      }
    };
    int maxIter = 1000;
    double x0 = -10;
    double f0 = function.valueAt(x0);
    double x1 = 10;
    double f1 = function.valueAt(x1);
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertFalse(rootFound);
  }

  // Add more test cases for comprehensive testing of the BrentSolver class
  // ...

}