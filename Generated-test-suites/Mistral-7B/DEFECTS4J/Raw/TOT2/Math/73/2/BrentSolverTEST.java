import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.FunctionThatReturnsXSquared;
import org.spaceroots.mantissa.functions.FunctionThatReturnsZero;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testBrentSolverConstruction() {
    BrentSolver solver = new BrentSolver();
    assertNotNull(solver);
  }

  @Test
  public void testFindRootDefaultFunction() throws FunctionException {
    ComputableFunction function = new FunctionThatReturnsXSquared();
    BrentSolver solver = new BrentSolver();
    assertFalse(solver.findRoot(function, new ConvergenceChecker(), 10, 0, 1, 1, 1));
    // Add assertions to check the root value and number of iterations if the test passes
  }

  @Test
  public void testFindRootZeroFunction() throws FunctionException {
    ComputableFunction function = new FunctionThatReturnsZero();
    BrentSolver solver = new BrentSolver();
    // Add assertions to check the root value and number of iterations if the test passes
    assertFalse(solver.findRoot(function, new ConvergenceChecker(), 10, 1, 0, 2, 0));
  }

  @Test(expected = NullPointerException.class)
  public void testFindRootNullFunction() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(null, new ConvergenceChecker(), 10, 0, 1, 1, 1);
  }

  @Test
  public void testGetRoot() {
    BrentSolver solver = new BrentSolver();
    solver.findRoot(new FunctionThatReturnsXSquared(), new ConvergenceChecker(), 10, 0, 1, 1, 1);
    assertEquals(Math.sqrt(1), solver.getRoot(), epsilon);
  }

  @Test
  public void testNoRootFound() {
    BrentSolver solver = new BrentSolver();
    assertNull(solver.getRoot());
  }

  @Test(expected = FunctionException.class)
  public void testGetRootBeforeFindRoot() {
    BrentSolver solver = new BrentSolver();
    assertThrows(FunctionException.class, () -> solver.getRoot());
  }

  @Test
  public void testConvergenceChecker() {
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergenceStatus(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };
    assertNotNull(checker);
  }

  @Test
  public void testConvergenceCheckerLow() {
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergenceStatus(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.LOW;
      }
    };
    assertTrue(checker.converged(0, 0, 1, 1));
  }

  @Test
  public void testConvergenceCheckerHigh() {
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int convergenceStatus(double xLow, double fLow, double xHigh, double fHigh) {
        return ConvergenceChecker.HIGH;
      }
    };
    assertFalse(checker.converged(0, 0, 1, 1));
  }

}