import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.TestFunctions;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRootSimple() {
    ComputableFunction function = new TestFunctions.SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    try {
      boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      assertEquals(root, solver.getRoot(), 0.001);
      assertTrue(result);
    } catch (FunctionException e) {
      fail("Test should not throw exception: " + e.getMessage());
    }
  }

  @Test
  public void testFindRootComplex() {
    ComputableFunction function = new TestFunctions.ComplexFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.5;
    double x1 = 2.0;
    double root = 1.0;

    try {
      boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      assertEquals(root, solver.getRoot(), 0.001);
      assertTrue(result);
    } catch (FunctionException e) {
      fail("Test should not throw exception: " + e.getMessage());
    }
  }

  @Test
  public void testFindRootError() {
    ComputableFunction function = new TestFunctions.ErrorFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 1.0;
    double root = 0.5;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Test expected FunctionException");
    } catch (FunctionException e) {
      assertEquals("Function not defined for complex values.", e.getMessage());
    }
  }

  @Test
  public void testFindRootConvergence() {
    ComputableFunction function = new TestFunctions.ConvergenceFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    try {
      boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
      assertFalse(result);
    } catch (FunctionException e) {
      fail("Test should not throw exception: " + e.getMessage());
    }

    try {
      result = solver.findRoot(function, checker, 1000, x0, function.valueAt(x0), x1, function.valueAt(x1));
      assertEquals(root, solver.getRoot(), 0.001);
      assertTrue(result);
    } catch (FunctionException e) {
      fail("Test should not throw exception: " + e.getMessage());
    }
  }

  @Test
  public void testFindRootMaxIter() {
    ComputableFunction function = new TestFunctions.SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    try {
      boolean result = solver.findRoot(function, checker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Test expected FunctionException");
    } catch (FunctionException e) {
      assertEquals("The number of iterations must be greater than zero.", e.getMessage());
    }
  }

}