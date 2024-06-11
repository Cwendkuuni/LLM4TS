import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  // Test findRoot method with basic functionality
  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow == 0) {
          return LOW;
        }
        if (fHigh == 0) {
          return HIGH;
        }
        if (fLow * fHigh < 0) {
          return ZERO;
        }
        return NONE;
      }
    };
    boolean rootFound = solver.findRoot(function, checker, 100, -1, function.valueAt(-1), 1, function.valueAt(1));
    assertTrue(rootFound);
    assertEquals(1.0, solver.getRoot(), 0.0001);
  }

  // Test findRoot method with edge case where function values are equal to zero
  @Test
  public void testFindRootWithZeroFunctionValues() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return 0;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow == 0) {
          return LOW;
        }
        if (fHigh == 0) {
          return HIGH;
        }
        if (fLow * fHigh < 0) {
          return ZERO;
        }
        return NONE;
      }
    };
    boolean rootFound = solver.findRoot(function, checker, 100, -1, function.valueAt(-1), 1, function.valueAt(1));
    assertTrue(rootFound);
    assertEquals(0.0, solver.getRoot(), 0.0001);
  }

  // Test findRoot method with edge case where abscissa of lower bound is equal to upper bound
  @Test
  public void testFindRootWithEqualLowerAndUpperBounds() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 1;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double xLow, double fLow, double xHigh, double fHigh) {
        if (fLow == 0) {
          return LOW;
        }
        if (fHigh == 0) {
          return HIGH;
        }
        if (fLow * fHigh < 0) {
          return ZERO;
        }
        return NONE;
      }
    };
    boolean rootFound = solver.findRoot(function, checker, 100, 1, function.valueAt(1), 1, function.valueAt(1));
    assertTrue(rootFound);
    assertEquals(1.0, solver.getRoot(), 0.0001);
  }

  // Test getRoot method
  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.getRoot();
  }

}