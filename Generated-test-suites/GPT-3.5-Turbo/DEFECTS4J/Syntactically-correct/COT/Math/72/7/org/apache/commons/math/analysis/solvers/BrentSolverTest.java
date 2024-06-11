import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    // Test case 1: Basic functionality
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) { return x - 2.0; }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        if (Math.abs(f0) < 1e-6) return LOW;
        if (Math.abs(f1) < 1e-6) return HIGH;
        return UNCONVERGED;
      }
    };
    boolean result = solver.findRoot(function, checker, 100, 0.0, -2.0, 4.0, 2.0);
    assertTrue(result);
    assertEquals(2.0, solver.getRoot(), 1e-6);

    // Test case 2: Edge case where interval contains multiple roots
    ComputableFunction function2 = new ComputableFunction() {
      public double valueAt(double x) { return (x - 1.0) * (x - 2.0) * (x - 3.0); }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        if (Math.abs(f0) < 1e-6) return LOW;
        if (Math.abs(f1) < 1e-6) return HIGH;
        return UNCONVERGED;
      }
    };
    boolean result2 = solver.findRoot(function2, checker2, 100, 0.0, -6.0, 6.0, 6.0);
    assertTrue(result2);
    double root2 = solver.getRoot();
    assertTrue(root2 >= 1.0 && root2 <= 3.0);

    // Test case 3: Exception handling
    ComputableFunction function3 = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Function evaluation error");
      }
    };
    ConvergenceChecker checker3 = new ConvergenceChecker() {
      public int converged(double x0, double f0, double x1, double f1) {
        return UNCONVERGED;
      }
    };
    boolean result3 = solver.findRoot(function3, checker3, 100, 0.0, -2.0, 4.0, 2.0);
    assertFalse(result3);
  }
}

