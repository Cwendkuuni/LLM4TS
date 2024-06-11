import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.TestFunctions;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_simpleFunction() throws FunctionException {
    ComputableFunction function = new TestFunctions.SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver brentSolver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    boolean foundRoot = brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertTrue("Root not found", foundRoot);
    Assert.assertEquals("Root not equals to expected", root, brentSolver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_complexFunction() throws FunctionException {
    ComputableFunction function = new TestFunctions.ComplexFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Quadratic();
    BrentSolver brentSolver = new BrentSolver();

    double x0 = 0.1;
    double x1 = 1.0;
    double root = 0.3626865;

    boolean foundRoot = brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertTrue("Root not found", foundRoot);
    Assert.assertEquals("Root not equals to expected", root, brentSolver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() throws FunctionException {
    ComputableFunction function = new TestFunctions.ExceptionFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver brentSolver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    brentSolver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN);
  }

  @Test
  public void testFindRoot_convergenceChecker() throws FunctionException {
    ComputableFunction function = new TestFunctions.SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Custom();
    BrentSolver brentSolver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    boolean foundRoot = brentSolver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertTrue("Root not found", foundRoot);
    Assert.assertEquals("Root not equals to expected", 1.5, brentSolver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_maxIterationReached() throws FunctionException {
    ComputableFunction function = new TestFunctions.SimpleFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver brentSolver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 2.0;

    boolean foundRoot = brentSolver.findRoot(function, checker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertFalse("Root found when it should not", foundRoot);
    Assert.assertEquals("Root is NaN when it should not", Double.NaN, brentSolver.getRoot());
  }

}