import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

  private BrentSolver solver;
  private double epsilon;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    epsilon = Math.pow(2.0, -52);
  }

  @Test
  public void testFindRoot() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
      public double valueAt(double x) {
        return x * x - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < epsilon) {
          return LOW;
        }
        if (Math.abs(f2) < epsilon) {
          return HIGH;
        }
        return NONE;
      }
    };
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 3.0;
    double f1 = function.valueAt(x1);
    boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    assertTrue(rootFound);
    assertEquals(2.0, solver.getRoot(), epsilon);
  }

  @Test
  public void testGetRoot() {
    solver.getRoot();
  }

}