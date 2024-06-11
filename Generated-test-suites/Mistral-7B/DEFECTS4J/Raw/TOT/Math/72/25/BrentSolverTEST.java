import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.DummyFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private DummyFunction function;

  @Before
  public void setup() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (Math.abs(xLow - xHigh) < 0.001) {
          return ConvergenceChecker.BOTH;
        }
        return ConvergenceChecker.NONE;
      }
    };
    function = new DummyFunction();
  }

  @Test
  public void testFindRoot_simple() throws FunctionException {
    double root = 1.0;
    double x0 = 1.5;
    double x1 = 0.5;
    double expectedRoot = 1.0;

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionThrowsException() throws FunctionException {
    double x0 = 1.0;
    double x1 = 2.0;
    DummyFunction function = new DummyFunction() {
      @Override
      public double valueAt(double x) {
        throw new FunctionException("dummy exception");
      }
    };

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRoot_rootInBetween() throws FunctionException {
    double root = 2.0;
    double x0 = 1.5;
    double x1 = 2.5;
    double expectedRoot = 2.0;

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_rootAtEdge() throws FunctionException {
    double root = 0.0;
    double x0 = -1.0;
    double x1 = 0.0;
    double expectedRoot = 0.0;

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_rootAtBeginning() throws FunctionException {
    double root = 0.0;
    double x0 = 0.0;
    double x1 = 1.0;
    double expectedRoot = 0.0;

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_rootAtEnd() throws FunctionException {
    double root = 2.0;
    double x0 = 1.0;
    double x1 = 2.5;
    double expectedRoot = 2.0;

    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(expectedRoot, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_rootNotFound() throws FunctionException {
    double x0 = 0.0;
    double x1 = 1.0;
    double functionValueAtX0 = 1.0;
    double functionValueAtX1 = 1.0;
    DummyFunction function = new DummyFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) / x;
      }
    };

    solver.findRoot(function, checker, 10, x0, functionValueAtX0, x1, functionValueAtX1);
    assertEquals(Double.NaN, solver.getRoot(), 0.001);
  }

  @Test
  public void testFindRoot_maxIterReached() throws FunctionException {
    double root = 1.0;
    double x0 = 1.5;
    double x1 = 0.5;
    double functionValueAtX0 = 1.5;
    double functionValueAtX1 = -1.5;
    DummyFunction function = new DummyFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x - root, 2) * Math.sin(Math.PI * x / 2);
      }
    };

    solver.findRoot(function, checker, 1, x0, functionValueAtX0, x1, functionValueAtX1);
    assertEquals(Double.NaN, solver.getRoot(), 0.001);
  }
}