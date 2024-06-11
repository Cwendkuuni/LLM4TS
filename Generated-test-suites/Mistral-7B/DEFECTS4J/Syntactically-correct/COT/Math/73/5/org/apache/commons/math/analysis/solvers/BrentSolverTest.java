import org.junit.Test;
import org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.MockFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_BasicFunctionality() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    MockFunction function = new MockFunction();
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    Assert.assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_ConvergenceCheck() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return x < xPrevious ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
      }
    };

    MockFunction function = new MockFunction();
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertTrue(result);
    Assert.assertEquals(root, solver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    MockFunction function = new MockFunctionThrowingException();
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRoot_MaxIterations() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    MockFunction function = new MockFunction();
    double x0 = 1.0;
    double x1 = 2.0;
    double root = 1.5;

    boolean result = solver.findRoot(function, checker, 0, x0, function.valueAt(x0), x1, function.valueAt(x1));
    Assert.assertFalse(result);
  }

  @Test
  public void testFindRoot_DifferentRoots() {
    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xPrevious, double fxPrevious) {
        return ConvergenceChecker.NONE;
      }
    };

    MockFunction function = new MockFunctionTwoRoots();
    double x0 = 1.0;
    double x1 = 2.0;
    double root1 = 1.5;
    double root2 = 3.0;

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));

    double rootFound = solver.getRoot();
    if (root1 < root2) {
      Assert.assertEquals(root1, rootFound, epsilon);
    } else {
      Assert.assertEquals(root2, rootFound, epsilon);
    }
  }

}

class MockFunction implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return Math.pow(x, 2) - 2.0;
  }
}

class MockFunctionThrowingException implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    throw new FunctionException();
  }
}

class MockFunctionTwoRoots implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    if (x > 1.5) {
      return Math.pow(x, 2) - 3.0;
    } else {
      return Math.pow(x, 2) - 1.0;
    }
  }
}