import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ExampleFunction;
import org.spaceroots.mantissa.functions.scalar.ExampleConvergenceChecker;

import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ExampleConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new ExampleFunction();
    checker = new ExampleConvergenceChecker();
  }

  @Test
  public void testFindRoot() {
    double x0 = 0.0;
    double f0 = function.valueAt(x0);
    double x1 = 2.0;
    double f1 = function.valueAt(x1);
    int maxIter = 100;

    try {
      boolean found = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);
      assertTrue(found);
      double root = solver.getRoot();
      assertEquals(1.0, root, 1e-6);

      // Since this is a basic test case, we don't have any edge cases or exception handling scenarios to test.
    } catch (FunctionException e) {
      fail("Unexpected exception thrown: " + e.getMessage());
    }
  }

}