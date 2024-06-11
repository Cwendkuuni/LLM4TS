import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    // Test case 1: Typical use case
    ComputableFunction function1 = new ComputableFunction() {
        public double valueAt(double x) {
            return x * x - 4;
        }
    };
    ConvergenceChecker checker1 = new ConvergenceChecker() {
        public int converged(double x1, double f1, double x2, double f2) {
            return LOW;
        }
    };
    BrentSolver solver1 = new BrentSolver();
    boolean result1 = solver1.findRoot(function1, checker1, 100, -2, -4, 2, 0);
    assertTrue(result1);
    assertEquals(2.0, solver1.getRoot(), 0);

    // Test case 2: Edge case (Interval that does not contain a root)
    ComputableFunction function2 = new ComputableFunction() {
        public double valueAt(double x) {
            return Math.exp(x) + 1;
        }
    };
    ConvergenceChecker checker2 = new ConvergenceChecker() {
        public int converged(double x1, double f1, double x2, double f2) {
            return LOW;
        }
    };
    BrentSolver solver2 = new BrentSolver();
    boolean result2 = solver2.findRoot(function2, checker2, 100, -2, -4, 2, 0);
    assertFalse(result2);
    assertTrue(Double.isNaN(solver2.getRoot()));

    // Test case 3: Error scenario (Function throws exception)
    ComputableFunction function3 = new ComputableFunction() {
        public double valueAt(double x) throws FunctionException {
            throw new FunctionException("Function evaluation error");
        }
    };
    ConvergenceChecker checker3 = new ConvergenceChecker() {
        public int converged(double x1, double f1, double x2, double f2) {
            return LOW;
        }
    };
    BrentSolver solver3 = new BrentSolver();
    boolean result3 = solver3.findRoot(function3, checker3, 100, -2, -4, 2, 0);
    assertFalse(result3);
    assertTrue(Double.isNaN(solver3.getRoot()));
  }
}