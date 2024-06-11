import org.junit.Test;
import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRootTypicalCase() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
        public double valueAt(double x) {
            return x * x - 4;
        }
    };
    ConvergenceChecker checker = new ConvergenceChecker();
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, 100, -10, -96, 10, 96);
    assertTrue(rootFound);
    assertEquals(2, solver.getRoot(), 0.000001);
  }

  @Test
  public void testFindRootNegativeCase() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
        public double valueAt(double x) {
            return x * x + 4;
        }
    };
    ConvergenceChecker checker = new ConvergenceChecker();
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, 100, -10, -96, 10, 96);
    assertFalse(rootFound);
  }

  @Test
  public void testFindRootEdgeCase() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
        public double valueAt(double x) {
            return x;
        }
    };
    ConvergenceChecker checker = new ConvergenceChecker();
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, 100, -10, -96, 10, 96);
    assertTrue(rootFound);
    assertEquals(0, solver.getRoot(), 0.000001);
  }

  @Test
  public void testFindRootErrorCase() throws FunctionException {
    ComputableFunction function = new ComputableFunction() {
        public double valueAt(double x) {
            throw new FunctionException("Error");
        }
    };
    ConvergenceChecker checker = new ConvergenceChecker();
    BrentSolver solver = new BrentSolver();
    boolean rootFound = solver.findRoot(function, checker, 100, -10, -96, 10, 96);
    assertFalse(rootFound);
  }
}