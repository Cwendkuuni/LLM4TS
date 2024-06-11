import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionConstants;
import org.spaceroots.mantissa.functions.scalar.RootFinder;
import org.spaceroots.mantissa.functions.scalar.TestFunctions;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testBrentSolver_rootFindsRoot() {
    ComputableFunction function = TestFunctions.squareFunction();
    RootFinder rootsFinder = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xDistance, double fDistance) {
        return ConvergenceChecker.CONVERGED;
      }
    };

    double x0 = 1.0;
    double f0 = 1.0;
    double x1 = 2.0;
    double f1 = 0.0;

    rootsFinder.findRoot(function, checker, 100, x0, f0, x1, f1);

    Assert.assertNotEquals(Double.NaN, rootsFinder.getRoot());
    Assert.assertEquals(FunctionConstants.EPSILON, Math.abs(function.valueAt(rootsFinder.getRoot())), 0.0);
  }

  @Test
  public void testBrentSolver_rootFindsRootInInterval() {
    ComputableFunction function = TestFunctions.squareFunction();
    RootFinder rootsFinder = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xDistance, double fDistance) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 1.0;
    double f0 = 1.0;
    double x1 = 2.0;
    double f1 = 0.0;

    rootsFinder.findRoot(function, checker, 100, x0, f0, x1, f1);

    Assert.assertNotEquals(Double.NaN, rootsFinder.getRoot());
    Assert.assertEquals(x1, rootsFinder.getRoot(), 0.0);
  }

  @Test(expected = FunctionException.class)
  public void testBrentSolver_functionThrowsException() {
    ComputableFunction function = () -> { throw new FunctionException("FunctionException"); };
    RootFinder rootsFinder = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xDistance, double fDistance) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 1.0;
    double f0 = 1.0;
    double x1 = 2.0;
    double f1 = 2.0;

    rootsFinder.findRoot(function, checker, 100, x0, f0, x1, f1);
  }

  @Test
  public void testBrentSolver_maxIterationsExceeded() {
    ComputableFunction function = TestFunctions.squareFunction();
    RootFinder rootsFinder = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xDistance, double fDistance) {
        return ConvergenceChecker.LOW;
      }
    };

    double x0 = 1.0;
    double f0 = 1.0;
    double x1 = 2.0;
    double f1 = 0.0;

    rootsFinder.findRoot(function, checker, 0, x0, f0, x1, f1);

    Assert.assertEquals(Double.NaN, rootsFinder.getRoot());
  }

  @Test
  public void testBrentSolver_rootIsInInterval() {
    ComputableFunction function = TestFunctions.squareFunction();
    RootFinder rootsFinder = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xDistance, double fDistance) {
        return ConvergenceChecker.HIGH;
      }
    };

    double x0 = 1.0;
    double f0 = 1.0;
    double x1 = 2.0;
    double f1 = 0.0;

    rootsFinder.findRoot(function, checker, 100, x0, f0, x1, f1);

    Assert.assertNotEquals(Double.NaN, rootsFinder.getRoot());
    Assert.assertEquals(x0, rootsFinder.getRoot(), 0.0);
  }

  @Test
  public void testBrentSolver_rootIsOutsideInterval() {
    ComputableFunction function = TestFunctions.squareFunction();
    RootFinder rootsFinder = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double xDistance, double fDistance) {
        return ConvergenceChecker.HIGH;
      }
    };

    double x0 = 1.0;
    double f0 = 1.0;
    double x1 = 0.0;
    double f1 = 0.0;

    rootsFinder.findRoot(function, checker, 100, x0, f0, x1, f1);

    Assert.assertEquals(Double.NaN, rootsFinder.getRoot());
  }

}