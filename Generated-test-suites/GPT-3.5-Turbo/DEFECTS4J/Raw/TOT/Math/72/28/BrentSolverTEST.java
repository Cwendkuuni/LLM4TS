import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;
  private static final double EPSILON = 1e-10;

  @Before
  public void setUp() {
    solver = new BrentSolver();
  }

  /* Test findRoot() method */

  // Typical use case
  @Test
  public void testFindRoot_typical() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4.0;
      }
    };

    boolean result = solver.findRoot(function, null, 100, 0, -4, 3, 5);
    assertTrue(result);

    double expected = 2.0;
    assertEquals(expected, solver.getRoot(), EPSILON);
  }

  // Edge case - Roots at boundary values
  @Test
  public void testFindRoot_boundary() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x;
      }
    };

    boolean result = solver.findRoot(function, null, 100, 0, 0, 5, 25);
    assertTrue(result);

    double expected = 0.0;
    assertEquals(expected, solver.getRoot(), EPSILON);
  }

  // Error scenario - No root in interval
  @Test
  public void testFindRoot_noRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x + 1.0;
      }
    };

    boolean result = solver.findRoot(function, null, 100, 0, 1, 2, 5);
    assertFalse(result);
  }

  /* Get the abscissa of the root */

  // Typical use case
  @Test
  public void testGetRoot_typical() {
    double expected = 2.0;
    solver.getRoot();
    assertEquals(expected, solver.getRoot(), EPSILON);
  }

}