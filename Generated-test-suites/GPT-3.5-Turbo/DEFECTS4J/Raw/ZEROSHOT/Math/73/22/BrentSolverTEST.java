import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    // Test 1
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(f2) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    boolean root1 = solver.findRoot(function1, checker1, 1000, 1, -3, 5, 21);
    assertTrue(root1);
    assertEquals(2, solver.getRoot(), 1e-6);

    // Test 2
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(f2) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    boolean root2 = solver.findRoot(function2, checker2, 1000, 0, 0, Math.PI, 0);
    assertTrue(root2);
    assertEquals(Math.PI/2, solver.getRoot(), 1e-6);
  }

  @Test
  public void testGetRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    // Test 1
    ComputableFunction function1 = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(f2) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    solver.findRoot(function1, checker1, 1000, 1, -3, 5, 21);
    assertEquals(2, solver.getRoot(), 1e-6);

    // Test 2
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) {
        return Math.sin(x);
      }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < 1e-6) {
          return ConvergenceChecker.LOW;
        } else if (Math.abs(f2) < 1e-6) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.NONE;
        }
      }
    };
    solver.findRoot(function2, checker2, 1000, 0, 0, Math.PI, 0);
    assertEquals(Math.PI/2, solver.getRoot(), 1e-6);
  }
}