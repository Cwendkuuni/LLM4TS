import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.FunctionUtilities;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @org.junit.Test
  public void testFindRoot_simpleFunction() throws FunctionException {
    ComputableFunction function = new FunctionUtilities.AbsFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), epsilon);
    assertTrue(result);
  }

  @org.junit.Test
  public void testFindRoot_divergentFunction() throws FunctionException {
    ComputableFunction function = new FunctionUtilities.SinXFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 1.0;

    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // expected
    }
  }

  @org.junit.Test
  public void testFindRoot_multipleRoots() throws FunctionException {
    ComputableFunction function = new FunctionUtilities.CubicFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    double root1 = 1.5;
    double root2 = 2.5;

    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root1, solver.getRoot(), epsilon);
    assertTrue(result);

    solver.reset();

    x0 = 2.0;
    x1 = 4.0;

    result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root2, solver.getRoot(), epsilon);
    assertTrue(result);
  }

  @org.junit.Test
  public void testFindRoot_rootAtEndBoundary() throws FunctionException {
    ComputableFunction function = new FunctionUtilities.AbsFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 1.0;
    double root = 1.0;

    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), epsilon);
    assertTrue(result);
  }

  @org.junit.Test
  public void testFindRoot_rootAtStartBoundary() throws FunctionException {
    ComputableFunction function = new FunctionUtilities.AbsFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.0;

    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), epsilon);
    assertTrue(result);
  }

  @org.junit.Test
  public void testFindRoot_rootBetweenBounds() throws FunctionException {
    ComputableFunction function = new FunctionUtilities.AbsFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.5;
    double x1 = 1.5;
    double root = 1.0;

    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), epsilon);
    assertTrue(result);
  }

  @org.junit.Test
  public void testFindRoot_functionValueZeroAtBoundary() throws FunctionException {
    ComputableFunction function = new FunctionUtilities.AbsFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 0.0;
    double root = 0.0;

    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    assertEquals(root, solver.getRoot(), epsilon);
    assertTrue(result);
  }

  @org.junit.Test
  public void testFindRoot_maxIterationsReached() throws FunctionException {
    ComputableFunction function = new FunctionUtilities.SinXFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 1.0;

    try {
      solver.findRoot(function, checker, 1, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // expected
    }
  }

  @org.junit.Test
  public void testFindRoot_noRootInInterval() throws FunctionException {
    ComputableFunction function = new FunctionUtilities.FunctionWithNoRoots();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 1.0;

    try {
      solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // expected
    }
  }

}