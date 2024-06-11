import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_simple() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        if (Math.abs(fx) < 0.01) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, -2.0, -2.0, 2.0, 2.0);
    double root = solver.getRoot();
    assertEquals(0.0, root, 0.01);
  }

  @Test
  public void testFindRoot_convergedHigh() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 3) - 2;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        if (fx > 0.0) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, -1.0, -1.0, 1.0, 1.0);
    double root = solver.getRoot();
    assertEquals(-1.0, root, 0.01);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x < 0) {
          throw new FunctionException("Function undefined for negative x.");
        }
        return Math.sin(Math.PI / x);
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, 100, 0.1, 0.1, 1.0, 1.0);
  }

  @Test
  public void testFindRoot_maxIter() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(Math.E, x) - Math.E;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(function, checker, 0, 1.0, 1.0, 2.0, 2.0);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // expected
    }
  }

  @Test
  public void testFindRoot_intervalEmpty() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sqrt(x + 1) - 2;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();
    try {
      solver.findRoot(function, checker, 100, 2.0, 2.0, 1.0, 1.0);
      fail("Expected FunctionException");
    } catch (FunctionException e) {
      // expected
    }
  }

  @Test
  public void testFindRoot_diffRoots() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };

    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrev, double fxPrev) {
        if (Math.abs(fx) < 0.01) {
          return ConvergenceChecker.LOW;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };

    BrentSolver solver = new BrentSolver();

    // root 1
    solver.findRoot(function, checker, 100, -2.5, -4.25, 1.5, 1.25);
    double root1 = solver.getRoot();
    assertEquals(-1.0, root1, 0.01);

    // root 2
    solver.reset();
    solver.findRoot(function, checker, 100, 2.5, 4.25, 3.5, 4.75);
    double root2 = solver.getRoot();
    assertEquals(1.0, root2, 0.01);
  }

}