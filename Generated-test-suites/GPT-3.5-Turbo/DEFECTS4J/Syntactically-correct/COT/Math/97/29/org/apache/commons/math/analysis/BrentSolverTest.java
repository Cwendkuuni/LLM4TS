
import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new MyFunction();
    ConvergenceChecker checker = new MyConvergenceChecker();
    int maxIter = 100;
    double x0 = -5.0;
    double f0 = function.valueAt(x0);
    double x1 = 5.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 1e-15);
  }

  @Test
  public void testFindRoot_GivenIntervalContainsTwoRoots_ShouldReturnOneRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new MyFunction();
    ConvergenceChecker checker = new MyConvergenceChecker();
    int maxIter = 100;
    double x0 = -2.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(result);
    assertEquals(0.0, solver.getRoot(), 1e-15);
  }

  @Test
  public void testFindRoot_GivenIntervalDoesNotContainRoot_ShouldReturnFalse() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new MyFunction();
    ConvergenceChecker checker = new MyConvergenceChecker();
    int maxIter = 100;
    double x0 = -5.0;
    double f0 = function.valueAt(x0);
    double x1 = -4.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_GivenInvalidFunction_ShouldThrowException() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new InvalidFunction();
    ConvergenceChecker checker = new MyConvergenceChecker();
    int maxIter = 100;
    double x0 = -5.0;
    double f0 = function.valueAt(x0);
    double x1 = 5.0;
    double f1 = function.valueAt(x1);

    solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
  }

  @Test
  public void testFindRoot_GivenMaxIterationsReached_ShouldReturnFalse() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new MyFunction();
    ConvergenceChecker checker = new MyConvergenceChecker();
    int maxIter = 10;
    double x0 = -5.0;
    double f0 = function.valueAt(x0);
    double x1 = 5.0;
    double f1 = function.valueAt(x1);

    boolean result = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertFalse(result);
  }

  @Test
  public void testGetRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new MyFunction();
    ConvergenceChecker checker = new MyConvergenceChecker();
    int maxIter = 100;
    double x0 = -5.0;
    double f0 = function.valueAt(x0);
    double x1 = 5.0;
    double f1 = function.valueAt(x1);

    solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    
    assertEquals(0.0, solver.getRoot(), 1e-15);
  }

  @Test
  public void testGetRoot_GivenNoRootFound_ShouldReturnNaN() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new MyFunction();
    ConvergenceChecker checker = new MyConvergenceChecker();
    int maxIter = 100;
    double x0 = -5.0;
    double f0 = function.valueAt(x0);
    double x1 = -4.0;
    double f1 = function.valueAt(x1);

    solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(Double.isNaN(solver.getRoot()));
  }
}

class MyFunction implements ComputableFunction {
  public double valueAt(double x) {
    return x * x - 4;
  }
}

class MyConvergenceChecker implements ConvergenceChecker {
  public int converged(double xLow, double fLow, double xHigh, double fHigh) {
    double tolS = 2 * BrentSolver.epsilon * Math.abs(xHigh);
    if (Math.abs(xLow) < tolS) {
      return ConvergenceChecker.LOW;
    } else if (Math.abs(xHigh) < tolS) {
      return ConvergenceChecker.HIGH;
    } else {
      return ConvergenceChecker.NONE;
    }
  }
}

class InvalidFunction implements ComputableFunction {
  public double valueAt(double x) throws FunctionException {
    throw new FunctionException("Invalid function");
  }
}
