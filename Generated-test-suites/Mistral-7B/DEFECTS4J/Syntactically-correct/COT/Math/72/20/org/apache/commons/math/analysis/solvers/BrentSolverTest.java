import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

  @Test
  public void testFindRoot_BasicFunction() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 3;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.CONVERGED;
        }
        if (Math.abs(x - xPrevious) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(fxPrevious) < 0.001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double fx0 = 2.0;
    double x1 = 2.0;
    double fx1 = -1.0;
    solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    double expectedRoot = 1.0;
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_Exception_Function() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Test exception");
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double fx0 = 0.0;
    double x1 = 2.0;
    double fx1 = 1.0;
    solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_Exception_Checkpoint() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        throw new FunctionException("Test exception");
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double fx0 = 0.0;
    double x1 = 2.0;
    double fx1 = Math.sin(2.0);
    solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
  }

  @Test
  public void testFindRoot_RootNearBoundary() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.CONVERGED;
        }
        if (Math.abs(x - xPrevious) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(fxPrevious) < 0.001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 0.1;
    double fx0 = 0.01;
    double x1 = 0.2;
    double fx1 = 0.05;
    solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    double expectedRoot = 0.144224957031252;
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_RootCloseToEachOther() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.CONVERGED;
        }
        if (Math.abs(x - xPrevious) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(fxPrevious) < 0.001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 1.5;
    double fx0 = 2.25;
    double x1 = 1.6;
    double fx1 = 1.96;
    solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    double expectedRoot = 1.56372939302561;
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_RootFarApart() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.CONVERGED;
        }
        if (Math.abs(x - xPrevious) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(fxPrevious) < 0.001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = -2.0;
    double fx0 = 4.0;
    double x1 = 2.0;
    double fx1 = -4.0;
    solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    double expectedRoot1 = -1.61517261308138;
    double expectedRoot2 = 1.61517261308138;
    double root1 = solver.getRoot();
    double root2 = solver.getRoot();
    assertEquals(expectedRoot1, root1, 0.001);
    assertEquals(expectedRoot2, root2, 0.001);
  }

  @Test
  public void testFindRoot_RootAtIntervalEnd() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 0.001;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        if (Math.abs(fx) < 0.001) {
          return ConvergenceChecker.CONVERGED;
        }
        if (Math.abs(x - xPrevious) < 0.001) {
          return ConvergenceChecker.LOW;
        }
        if (Math.abs(fxPrevious) < 0.001) {
          return ConvergenceChecker.HIGH;
        }
        return ConvergenceChecker.NOT_CONVERGED;
      }
    };
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double fx0 = 0.0;
    double x1 = 0.001;
    double fx1 = 1.0e-6;
    solver.findRoot(function, checker, 100, x0, fx0, x1, fx1);
    double expectedRoot = 0.0;
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

}