import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
  }

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };

    assertTrue(solver.findRoot(function, new ConvergenceChecker(), 100, 1, -3, 3, 7));
    assertEquals(2.0, solver.getRoot(), 1e-10);
  }

  // Edge Cases and Exception Handling

  @Test
  public void testFindRootWithSameIntervalBounds() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };

    assertFalse(solver.findRoot(function, new ConvergenceChecker(), 100, 1, -3, 1, -3));
  }

  @Test
  public void testFindRootWithMaxIterationsExceeded() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x * x - 4;
      }
    };

    assertFalse(solver.findRoot(function, new ConvergenceChecker(), 1, 1, -3, 3, 7));
  }

}