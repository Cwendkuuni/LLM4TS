import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.functions.SineFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SineFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.CONTINUE;
      }
    };
    function = new SineFunction();
  }

  @Test
  public void testFindRootSimpleCase() throws FunctionException {
    double root = Math.PI / 2;
    double x0 = 0.0;
    double x1 = 3.0;
    double tolerance = 0.001;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), tolerance);
    assertFalse(Double.isNaN(solver.getRoot()));
    assertFalse(result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootFunctionException() throws FunctionException {
    function = new SineFunction();
    double x0 = 0.0;
    double x1 = 0.0;
    double tolerance = 0.001;

    solver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN);
  }

  @Test
  public void testFindRootConvergenceChecker() throws FunctionException {
    double root = 1.0;
    double x0 = 0.0;
    double x1 = 2.0;
    double tolerance = 0.01;

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (Math.abs(xHigh - xLow) < tolerance) ? ConvergenceChecker.LOW : ConvergenceChecker.CONTINUE;
      }
    };

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), tolerance);
    assertFalse(Double.isNaN(solver.getRoot()));
    assertFalse(result);
  }

  @Test
  public void testFindRootMaxIter() throws FunctionException {
    double root = Math.E;
    double x0 = 0.0;
    double x1 = 1.0;
    double tolerance = 0.01;

    boolean result = solver.findRoot(function, checker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), tolerance);
    assertFalse(Double.isNaN(solver.getRoot()));
    assertFalse(result);
  }

  @Test
  public void testFindRootIterationLimit() throws FunctionException {
    double root = 1.0;
    double x0 = 0.0;
    double x1 = 2.0;
    double tolerance = 0.01;

    boolean result = solver.findRoot(function, checker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), tolerance);
    assertFalse(Double.isNaN(solver.getRoot()));
    assertFalse(result);
  }

  @Test
  public void testFindRootRootInInterval() throws FunctionException {
    double root = 0.0;
    double x0 = -1.0;
    double x1 = 1.0;
    double tolerance = 0.01;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), tolerance);
    assertFalse(Double.isNaN(solver.getRoot()));
    assertFalse(result);
  }

  @Test
  public void testFindRootRootNotInInterval() throws FunctionException {
    double root = 2.0;
    double x0 = -1.0;
    double x1 = 1.0;
    double tolerance = 0.01;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertFalse(result);
    assertFalse(Double.isNaN(solver.getRoot()));
    assertFalse(Double.isFinited(solver.getRoot()));
  }

  @Test
  public void testFindRootNoRootInInterval() throws FunctionException {
    double root = Double.NaN;
    double x0 = 0.0;
    double x1 = 0.0;
    double tolerance = 0.01;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), tolerance);
    assertFalse(Double.isNaN(solver.getRoot()));
    assertFalse(result);
  }

}