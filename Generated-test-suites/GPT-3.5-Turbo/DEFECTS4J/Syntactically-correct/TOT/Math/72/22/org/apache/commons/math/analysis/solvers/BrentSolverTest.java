import org.junit.Assert;
import org.junit.Test;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    // Typical use case
    // The given interval [0, 2] contains a root at 1.0
    BrentSolver solver = new BrentSolver();
    TestFunction function = new TestFunction();
    ConvergenceChecker checker = new ConvergenceChecker();
    boolean rootFound = solver.findRoot(function, checker, 100, 0.0, function.valueAt(0.0), 2.0, function.valueAt(2.0));
    double expectedRoot = 1.0;
    Assert.assertTrue(rootFound);
    Assert.assertEquals(expectedRoot, solver.getRoot(), 0.000001);

    // Edge cases
    // The given interval [0, 0] contains a root at 0.0
    solver = new BrentSolver();
    function = new TestFunction();
    checker = new ConvergenceChecker();
    rootFound = solver.findRoot(function, checker, 100, 0.0, function.valueAt(0.0), 0.0, function.valueAt(0.0));
    expectedRoot = 0.0;
    Assert.assertTrue(rootFound);
    Assert.assertEquals(expectedRoot, solver.getRoot(), 0.000001);

    // The given interval [-1, 1] contains a root at 0.5
    solver = new BrentSolver();
    function = new TestFunction();
    checker = new ConvergenceChecker();
    rootFound = solver.findRoot(function, checker, 100, -1.0, function.valueAt(-1.0), 1.0, function.valueAt(1.0));
    expectedRoot = 0.5;
    Assert.assertTrue(rootFound);
    Assert.assertEquals(expectedRoot, solver.getRoot(), 0.000001);

    // Error scenarios
    // The given interval [0, 1] does not contain a root
    solver = new BrentSolver();
    function = new TestFunction();
    checker = new ConvergenceChecker();
    rootFound = solver.findRoot(function, checker, 100, 0.0, function.valueAt(0.0), 1.0, function.valueAt(1.0));
    Assert.assertFalse(rootFound);
  }

  @Test
  public void testGetRoot() {

  }

}