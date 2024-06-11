import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        return LOW;
      }
    };
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = -1.0;
    double x1 = 2.0;
    double f1 = 1.0;
    
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertTrue(rootFound);
    assertEquals(1.0, solver.getRoot(), 0.0);
  }

  @Test
  public void testFindRootNoRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        return LOW;
      }
    };
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = -1.0;
    double x1 = 1.0;
    double f1 = 0.0;
    
    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertFalse(rootFound);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException();
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        return LOW;
      }
    };
    int maxIter = 100;
    double x0 = 0.0;
    double f0 = -1.0;
    double x1 = 2.0;
    double f1 = 1.0;
    
    solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new ComputableFunction() {
      public double valueAt(double x) {
        return x - 1;
      }
    }, new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        return LOW;
      }
    }, 100, 0.0, -1.0, 2.0, 1.0);
    
    assertEquals(1.0, solver.getRoot(), 0.0);
  }

}