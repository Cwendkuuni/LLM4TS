import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.FunctionConstants;
import org.spaceroots.mantissa.functions.scalar.RootsFinder;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.UnaryFunction;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new UnaryFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        if (Math.abs(f) < 0.001) {
          return ConvergenceChecker.CONVERGED;
        }
        if (Math.abs(x - xPrevious) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.ITERATION;
      }
    };

    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;
    double root = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithException() throws FunctionException {
    ComputableFunction function = new UnaryFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("Test Exception");
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.ITERATION;
      }
    };

    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 3.0;

    solver.findRoot(function, checker, 100, x0, FunctionConstants.ZERO, x1, FunctionConstants.ZERO);
  }

  @Test
  public void testFindRootWithSmallInterval() throws FunctionException {
    ComputableFunction function = new UnaryFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(Math.PI / 2.0 - x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        if (Math.abs(f) < 0.001) {
          return ConvergenceChecker.CONVERGED;
        }
        if (Math.abs(x - xPrevious) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.ITERATION;
      }
    };

    BrentSolver solver = new BrentSolver();

    double x0 = 0.0;
    double x1 = 1.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertEquals(1.0 / Math.sqrt(2.0), solver.getRoot(), 0.01);
  }

  @Test
  public void testFindRootWithLargeInterval() throws FunctionException {
    ComputableFunction function = new UnaryFunction() {
      @Override
      public double valueAt(double x) {
        return Math.exp(Math.log(10.0) / 3.0) * (x - 1.0);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        if (Math.abs(f) < 0.001) {
          return ConvergenceChecker.CONVERGED;
        }
        if (Math.abs(x - xPrevious) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.ITERATION;
      }
    };

    BrentSolver solver = new BrentSolver();

    double x0 = 1.0;
    double x1 = 100.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertTrue(Math.abs(solver.getRoot() - 1.0 + Math.E / Math.LOG10E) < 0.1);
  }

  @Test
  public void testFindRootWithMultipleRoots() throws FunctionException {
    ComputableFunction function = new UnaryFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        if (Math.abs(f) < 0.001) {
          return ConvergenceChecker.CONVERGED;
        }
        if (Math.abs(x - xPrevious) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.ITERATION;
      }
    };

    BrentSolver solver = new BrentSolver();

    double x0 = -2.0;
    double x1 = 2.0;

    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

    Assert.assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRootWithDivergentFunction() throws FunctionException {
    ComputableFunction function = new UnaryFunction() {
      @Override
      public double valueAt(double x) {
        if (x < 0.0) {
          return Math.log(Math.abs(x));
        } else {
          return Math.pow(x, 2);
        }
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xPrevious, double fPrevious) {
        return ConvergenceChecker.ITERATION;
      }
    };

    BrentSolver solver = new BrentSolver();

    double x0 = -1.0;
    double x1 = 1.0;

    try {
      solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
      fail("Expected FunctionException");
    } catch (FunctionException fe) {
      // Expected
    }
  }

}