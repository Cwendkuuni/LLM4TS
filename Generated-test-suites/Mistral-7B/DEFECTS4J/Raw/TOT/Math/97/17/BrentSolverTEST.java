import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link BrentSolver}.
 */
public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private ComputableFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double x2, double fx2) {
        return (fx > 0) ? ConvergenceChecker.LOW : (fx < 0) ? ConvergenceChecker.HIGH : ConvergenceChecker.NONE;
      }
    };
    function = FunctionUtils.sinFunction();
  }

  /**
   * Test finding root of a simple function.
   * @throws FunctionException if finding root fails
   */
  @Test
  public void testFindRootSimpleFunction() throws FunctionException {
    double x0 = 0.0;
    double x1 = Math.PI;
    double root = Math.PI / 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), epsilon);
  }

  /**
   * Test finding root of a function with multiple roots.
   * @throws FunctionException if finding root fails
   */
  @Test
  public void testFindRootMultipleRootsFunction() throws FunctionException {
    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), epsilon);
  }

  /**
   * Test finding root of a function with only one root close to the interval boundary.
   */
  @Test
  public void testFindRootRootNearBoundary() {
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    solver = new BrentSolver();
    assertThrows(FunctionException.class, () -> solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  /**
   * Test finding root of a function with no root in the interval.
   */
  @Test
  public void testFindRootNoRootInInterval() {
    double x0 = 0.0;
    double x1 = 1.0;
    double root = 2.0;

    solver = new BrentSolver();
    assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  /**
   * Test getting null root.
   */
  @Test
  public void testGetRootNullRoot() {
    solver.setRoot(null);
    assertNotNull(solver.getRoot());
  }

}