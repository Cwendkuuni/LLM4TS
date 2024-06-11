import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Test class for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Test method for the findRoot method with a simple polynomial function.
   * @throws FunctionException if an error occurs during function evaluation.
   */
  @Test
  public void testFindRootSimpleFunction() throws FunctionException {
    ComputableFunction function = new RationalFunction("x", "x^2 - 4");
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Simple();
    double x0 = 1.0;
    double x1 = 3.0;
    int maxIter = 10;

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(rootFound);
    Assert.assertEquals(x1, solver.getRoot(), 0.001);
  }

  /**
   * Test method for the findRoot method with a complex polynomial function.
   * @throws FunctionException if an error occurs during function evaluation.
   */
  @Test
  public void testFindRootComplexFunction() throws FunctionException {
    ComputableFunction function = new RationalFunction("x", "x^3 - 3 * x^2 + 3 * x - 1");
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Simple();
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 10;

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(rootFound);
    Assert.assertEquals(1.5, solver.getRoot(), 0.01);
  }

  /**
   * Test method for the findRoot method with a function having two roots.
   * @throws FunctionException if an error occurs during function evaluation.
   */
  @Test
  public void testFindRootTwoRootsFunction() throws FunctionException {
    ComputableFunction function = new RationalFunction("x", "x^2 - 4 * x + 4");
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Simple();
    double x0 = 1.0;
    double x1 = 3.0;
    int maxIter = 10;

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(rootFound);
    Assert.assertNotEquals(x1, solver.getRoot(), 0.01);

    solver.reset(); // reset for the second root
    x0 = 3.0;
    x1 = 1.0;
    rootFound = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(rootFound);
    Assert.assertEquals(x0, solver.getRoot(), 0.01);
  }

  /**
   * Test method for the convergence checker.
   */
  @Test
  public void testConvergenceChecker() {
    ComputableFunction function = new RationalFunction("x", "x^2 - 4");
    ConvergenceChecker checker = new ConvergenceChecker.Simple();

    double xLow = 1.0;
    double fLow = function.valueAt(xLow);
    double xHigh = 3.0;
    double fHigh = function.valueAt(xHigh);

    Assert.assertEquals(ConvergenceChecker.LOW, checker.converged(xLow, fLow, xHigh, fHigh));

    xHigh = 2.0;
    fHigh = function.valueAt(xHigh);

    Assert.assertEquals(ConvergenceChecker.HIGH, checker.converged(xLow, fLow, xHigh, fHigh));

    xHigh = 2.5;
    fHigh = function.valueAt(xHigh);

    Assert.assertEquals(ConvergenceChecker.NEITHER, checker.converged(xLow, fLow, xHigh, fHigh));
  }

  /**
   * Test method for the solver with a pathological function.
   * @throws FunctionException if an error occurs during function evaluation.
   */
  @Test(expected = FunctionException.class)
  public void testFindRootPathologicalFunction() throws FunctionException {
    ComputableFunction function = new RationalFunction("x", "1 / (x * x)");
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Simple();
    double x0 = 1.0;
    double x1 = 2.0;
    int maxIter = 100;

    solver.findRoot(function, checker, maxIter, x0, Double.NaN, x1, Double.NaN);
  }

  /**
   * Test method for the solver with a function having no root.
   * @throws FunctionException if an error occurs during function evaluation.
   */
  @Test
  public void testFindRootNoRootFunction() throws FunctionException {
    ComputableFunction function = new RationalFunction("x", "x^2 + 1");
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker.Simple();
    double x0 = 1.0;
    double x1 = 3.0;
    int maxIter = 100;

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertFalse(rootFound);
  }

}