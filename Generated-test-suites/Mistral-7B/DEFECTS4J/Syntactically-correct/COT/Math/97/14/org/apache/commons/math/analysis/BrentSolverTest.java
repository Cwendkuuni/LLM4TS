package org.spaceroots.mantissa.roots.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

@RunWith(Parameterized.class)
public class BrentSolverTest {

  private ComputableFunction function;
  private double x0;
  private double x1;
  private int maxIter;
  private ConvergenceChecker checker;

  public BrentSolverTest(ComputableFunction function, double x0, double x1, int maxIter, ConvergenceChecker checker) {
    this.function = function;
    this.x0 = x0;
    this.x1 = x1;
    this.maxIter = maxIter;
    this.checker = checker;
  }

  @Parameterized.Parameters(name = "{index}: findRoot({0}, {1}, {2}, {3}, {4})")
  public static List<Object[]> data() {
    List<Object[]> data = new ArrayList<>();

    // Test function f(x) = x^3 - 2
    ComputableFunction function1 = new FunctionSquareRoot();
    data.add(new Object[] { function1, 1.0, 2.0, 10, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (fLow < 0) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
      }
    }});

    // Test function f(x) = sin(x)
    ComputableFunction function2 = new FunctionSine();
    data.add(new Object[] { function2, 0.0, Math.PI, 10, new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        return (Math.abs(fLow) < 0.001) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
      }
    }});

    return data;
  }

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    double root = solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(root != Double.NaN);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() throws FunctionException {
    ComputableFunction function = new FunctionDivideByZero();
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertTrue(solver.getRoot() != Double.NaN);
  }

}

public interface ConvergenceChecker {
  int converged(double xLow, double fLow, double xHigh, double fHigh);

  static final int LOW = 0;
  static final int HIGH = 1;
  static final int NONE = 2;
}

public class FunctionSquareRoot implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return Math.pow(x, 3) - 2;
  }
}

public class FunctionSine implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    return Math.sin(x);
  }
}

public class FunctionDivideByZero implements ComputableFunction {
  @Override
  public double valueAt(double x) {
    throw new ArithmeticException("Division by zero");
  }
}