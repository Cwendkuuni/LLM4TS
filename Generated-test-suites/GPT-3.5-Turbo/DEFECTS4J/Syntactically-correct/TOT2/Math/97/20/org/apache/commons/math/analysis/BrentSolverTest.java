import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot_TypicalCase() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        return x - 5;
      }
    };
    try {
      boolean rootFound = solver.findRoot(function, new ConvergenceChecker(), 100, 0.0, -5.0, 10.0, 5.0);
      assertTrue(rootFound);
      assertEquals(5.0, solver.getRoot(), 1e-10);
    } catch (FunctionException e) {
      fail("Exception thrown during testFindRoot_TypicalCase");
    }
  }

  @Test
  public void testFindRoot_EdgeCases() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        if (x == 0.0) {
          throw new FunctionException("Division by zero");
        }
        return 1 / x;
      }
    };
    try {
      boolean rootFound = solver.findRoot(function, new ConvergenceChecker(), 100, -1.0, -1.0, 1.0, 1.0);
      assertFalse(rootFound);
    } catch (FunctionException e) {
      assertTrue(e.getMessage().equals("Division by zero"));
    }
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    assertEquals(Double.NaN, solver.getRoot(), 1e-10);
  }

}