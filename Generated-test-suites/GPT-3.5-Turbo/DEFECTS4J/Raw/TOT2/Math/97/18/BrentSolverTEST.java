import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;

public class BrentSolverTest {

  @Test
  public void testConstructor() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
  }

  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x*x - 4;
      }
    };
    BrentSolver solver = new BrentSolver();
    assertTrue(solver.findRoot(function, new ConvergenceChecker(), 100, 0, -4, 3, 5));
    assertEquals(2.0, solver.getRoot(), 1e-10);
  }

  @Test
  public void testGetRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x*x - 9;
      }
    };
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, new ConvergenceChecker(), 100, 1, -8, 3, 0);
    assertEquals(3.0, solver.getRoot(), 1e-10);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootWithException() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Function failed");
      }
    };
    BrentSolver solver = new BrentSolver();
    solver.findRoot(function, new ConvergenceChecker(), 100, 1, -1, 2, 2);
  }

}