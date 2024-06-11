import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.DummyFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Test constructor.
   */
  @Test
  public void testConstructor() {
    BrentSolver brentSolver = new BrentSolver();
    assertNotNull(brentSolver);
    assertNaN(brentSolver.getRoot());
  }

  /**
   * Test findRoot method with a simple case.
   */
  @Test
  public void testFindRootSimple() throws FunctionException {
    BrentSolver brentSolver = new BrentSolver();
    ComputableFunction function = new DummyFunction(2.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;

    boolean result = brentSolver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, brentSolver.getRoot(), epsilon);
    assertTrue(result);
  }

  /**
   * Test findRoot method with a convergence at a lower boundary.
   */
  @Test
  public void testFindRootLowerBound() throws FunctionException {
    BrentSolver brentSolver = new BrentSolver();
    ComputableFunction function = new DummyFunction(2.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;

    boolean result = brentSolver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, brentSolver.getRoot(), epsilon);
    assertTrue(result);
  }

  /**
   * Test findRoot method with a convergence at a higher boundary.
   */
  @Test
  public void testFindRootHigherBound() throws FunctionException {
    BrentSolver brentSolver = new BrentSolver();
    ComputableFunction function = new DummyFunction(2.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.HIGH;
      }
    };

    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;

    boolean result = brentSolver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, brentSolver.getRoot(), epsilon);
    assertTrue(result);
  }

  /**
   * Test findRoot method with a convergence at a root.
   */
  @Test
  public void testFindRootRoot() throws FunctionException {
    BrentSolver brentSolver = new BrentSolver();
    ComputableFunction function = new DummyFunction(2.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.ROOT;
      }
    };

    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;

    boolean result = brentSolver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, brentSolver.getRoot(), epsilon);
    assertTrue(result);
  }

  /**
   * Test findRoot method with maximum iterations.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootMaxIter() throws FunctionException {
    BrentSolver brentSolver = new BrentSolver();
    ComputableFunction function = new DummyFunction(2.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;

    boolean result = brentSolver.findRoot(function, checker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1));
    fail("Should throw FunctionException when maxIter is 0.");
  }

}