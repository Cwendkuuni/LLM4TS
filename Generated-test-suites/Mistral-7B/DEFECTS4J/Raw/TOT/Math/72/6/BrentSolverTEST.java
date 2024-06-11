import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction; // For the sake of testing, let's assume a simple RationalFunction is used as a ComputableFunction
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link BrentSolver}.
 */
public class BrentSolverTest {

  private BrentSolver brentSolver;
  private ConvergenceChecker convergenceChecker;
  private ComputableFunction computableFunction;

  @Before
  public void setUp() {
    brentSolver = new BrentSolver();
    convergenceChecker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.NONE;
      }
    };
    computableFunction = new RationalFunction((x) -> 1.0 / (x * x) - 2.0 / (x + 1.0));
  }

  /**
   * Test finding the root of a simple function.
   */
  @Test
  public void testFindRootSimpleFunction() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = -0.8284570425190451;

    assertTrue(brentSolver.findRoot(computableFunction, convergenceChecker, 100, x0, computableFunction.valueAt(x0), x1, computableFunction.valueAt(x1)));
    assertEquals(expectedRoot, brentSolver.getRoot(), epsilon);
  }

  /**
   * Test finding the root of a function with a very large interval.
   */
  @Test
  public void testFindRootLargeInterval() throws FunctionException {
    double x0 = 1.0;
    double x1 = 1000.0;
    double expectedRoot = -0.71277539062502034;

    assertTrue(brentSolver.findRoot(computableFunction, convergenceChecker, 100, x0, computableFunction.valueAt(x0), x1, computableFunction.valueAt(x1)));
    assertEquals(expectedRoot, brentSolver.getRoot(), epsilon);
  }

  /**
   * Test finding the root of a function with a small interval.
   */
  @Test
  public void testFindRootSmallInterval() throws FunctionException {
    double x0 = 1.0;
    double x1 = 1.1;
    double expectedRoot = -0.71277539062502034;

    assertTrue(brentSolver.findRoot(computableFunction, convergenceChecker, 100, x0, computableFunction.valueAt(x0), x1, computableFunction.valueAt(x1)));
    assertEquals(expectedRoot, brentSolver.getRoot(), epsilon);
  }

  /**
   * Test finding the root of a function with convergence checker.
   */
  @Test
  public void testFindRootWithConvergenceChecker() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = -0.8284570425190451;

    ConvergenceChecker convergenceChecker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xLow - xHigh) < epsilon) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(xHigh - xLow) < epsilon) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };

    assertTrue(brentSolver.findRoot(computableFunction, convergenceChecker, 100, x0, computableFunction.valueAt(x0), x1, computableFunction.valueAt(x1)));
    assertEquals(expectedRoot, brentSolver.getRoot(), epsilon);
  }

  /**
   * Test finding the root of a function that has no root.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootNoRoot() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    ComputableFunction computableFunction = (x) -> Math.sin(x) / x;

    assertFalse(brentSolver.findRoot(computableFunction, convergenceChecker, 100, x0, computableFunction.valueAt(x0), x1, computableFunction.valueAt(x1)));
  }
}