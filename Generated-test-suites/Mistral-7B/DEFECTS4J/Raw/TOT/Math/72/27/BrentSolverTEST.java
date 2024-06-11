import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private SimpleFunction function;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(x1 - x2) < Double.MIN_VALUE) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(x2 - x1) < Double.MIN_VALUE) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.CONVERGENCE_CHECK_FAIL;
        }
      }
    };
    function = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - x;
      }
    };
  }

  @Test
  public void testRootFindingInSimpleInterval() {
    double x0 = 0.0;
    double x1 = 1.0;
    double root = Math.PI / 2.0;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(true, result);
    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testRootFindingInIntervalWithFunctionException() {
    double x0 = 0.0;
    double x1 = 1.0;
    SimpleFunction functionWithException = new SimpleFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        if (x > 0.5) {
          throw new FunctionException("FunctionException test.");
        } else {
          return Math.sin(x) - x;
        }
      }
    };

    boolean result = solver.findRoot(functionWithException, checker, 100, x0, functionWithException.valueAt(x0), x1, functionWithException.valueAt(x1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRootFindingWithIllegalArgumentException() {
    double x0 = 0.0;
    double x1 = 1.0;
    ConvergenceChecker illegalChecker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        throw new IllegalArgumentException("IllegalArgumentException test.");
      }
    };

    boolean result = solver.findRoot(function, illegalChecker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test(expected = ArithmeticException.class)
  public void testRootFindingWithArithmeticException() {
    double x0 = 0.0;
    double x1 = 1.0;
    SimpleFunction functionWithArithmeticException = new SimpleFunction() {
      @Override
      public double valueAt(double x) {
        return 1.0 / (Math.sin(x) - x);
      }
    };

    boolean result = solver.findRoot(functionWithArithmeticException, checker, 100, x0, functionWithArithmeticException.valueAt(x0), x1, functionWithArithmeticException.valueAt(x1));
  }

  @Test
  public void testRootFindingInLargeInterval() {
    double x0 = -10.0;
    double x1 = 10.0;
    double root = 0.0;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(true, result);
    assertEquals(root, solver.getRoot(), 0.01);
  }

  @Test
  public void testRootFindingInSmallIntervalWithFunctionValueZero() {
    double x0 = 0.0;
    double x1 = 0.1;
    double root = 0.0;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(true, result);
    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test
  public void testRootFindingInSmallIntervalWithFunctionValueCloseToZero() {
    double x0 = 0.01;
    double x1 = 0.02;
    double root = 0.015;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(true, result);
    assertEquals(root, solver.getRoot(), 0.001);
  }

  @Test
  public void testRootFindingWithFunctionValueChangingSign() {
    double x0 = -2.0;
    double x1 = 2.0;
    double root = 0.0;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(true, result);
    assertEquals(root, solver.getRoot(), 0.01);
  }

  @Test
  public void testRootFindingWithFunctionValueChangingSignInLargeInterval() {
    double x0 = -10.0;
    double x1 = 10.0;
    double root = 0.0;

    boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(true, result);
    assertEquals(root, solver.getRoot(), 0.01);
  }
}