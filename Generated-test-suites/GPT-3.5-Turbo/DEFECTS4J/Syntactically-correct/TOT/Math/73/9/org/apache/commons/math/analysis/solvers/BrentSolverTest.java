import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRootWithTypicalUseCase() {
    BrentSolver solver = new BrentSolver();
    // Test case with a function that has a root in the interval [0, 1]
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);
    boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(foundRoot);
    assertEquals(expectedRoot, solver.getRoot(), epsilon);
  }

  @Test
  public void testFindRootWithEdgeCases() {
    BrentSolver solver = new BrentSolver();
    // Test case with a linear function that has a root at x = 0
    double x0 = -0.1;
    double f0 = function.valueAt(x0);
    double x1 = 0.1;
    double f1 = function.valueAt(x1);
    boolean foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertTrue(foundRoot);
    assertEquals(expectedRoot, solver.getRoot(), epsilon);

    // Test case with a constant function that does not have a root
    x0 = -1.0;
    f0 = function.valueAt(x0);
    x1 = 1.0;
    f1 = function.valueAt(x1);
    foundRoot = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
    assertFalse(foundRoot);
  }

  @Test
  public void testFindRootWithErrorScenarios() {
    BrentSolver solver = new BrentSolver();
    // Test case with a function that throws an exception
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);
    try {
      solver.findRoot(faultyFunction, checker, maxIter, x0, f0, x1, f1);
      fail("Exception not thrown");
    } catch (FunctionException e) {
      // Exception correctly thrown, test passed
    }
  }

}