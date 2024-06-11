import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_basic() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new MockComputableFunction(0.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.NEITHER;
      }
    };

    assertTrue(solver.findRoot(function, checker, 100, 1.0, 2.0, 3.0, 4.0));
    assertEquals(2.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_functionConverged() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new MockComputableFunction(2.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.LOW;
      }
    };

    assertTrue(solver.findRoot(function, checker, 100, 1.0, 2.0, 3.0, 4.0));
    assertEquals(2.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_functionDiverged() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new MockComputableFunction(Double.POSITIVE_INFINITY);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.HIGH;
      }
    };

    assertFalse(solver.findRoot(function, checker, 100, 1.0, 2.0, 3.0, 4.0));
    assertEquals(Double.NaN, solver.getRoot(), epsilon);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new MockComputableFunction(Double.NaN);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.NEITHER;
      }
    };

    solver.findRoot(function, checker, 100, 1.0, 2.0, 3.0, 4.0);
  }

  @Test
  public void testFindRoot_intervalCollided() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new MockComputableFunction(0.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (x1 < x2) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.HIGH;
        }
      }
    };

    assertFalse(solver.findRoot(function, checker, 100, 1.0, 2.0, 3.0, 4.0));
    assertEquals(Double.NaN, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRoot_iterationsLimit() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new MockComputableFunction(0.0);
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.NEITHER;
      }
    };

    assertFalse(solver.findRoot(function, checker, 0, 1.0, 2.0, 3.0, 4.0));
    assertEquals(Double.NaN, solver.getRoot(), epsilon);
  }
}