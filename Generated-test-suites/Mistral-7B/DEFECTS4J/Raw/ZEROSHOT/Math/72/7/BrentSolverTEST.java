import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.*;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private FunctionFactory functionFactory;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xNext, double fxNext) {
        return ConvergenceChecker.INDEFINITE;
      }
    };
    functionFactory = new FunctionFactory();
  }

  @After
  public void tearDown() {
    solver = null;
    checker = null;
    functionFactory = null;
  }

  @Test
  public void testFindRoot_simpleCase() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction(x -> x * x - 2.0);
    double root = solver.findRoot(function, checker, 10, -2.0, 2.0);
    assertTrue(root > -0.01 && root < 0.01);
    assertEquals(Math.sqrt(2.0), root, 0.01);
  }

  @Test
  public void testFindRoot_noRootInInterval() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction(x -> x * x - 4.0);
    assertFalse(solver.findRoot(function, checker, 10, -2.0, 2.0));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionException() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction(x -> 1.0 / Math.sqrt(x));
    solver.findRoot(function, checker, 10, 4.0, 8.0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRoot_invalidArgs() {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = null;
    solver.findRoot(function, checker, 10, 1.0, 1.0);
  }

  @Test(expected = IllegalStateException.class)
  public void testFindRoot_alreadyFoundRoot() {
    BrentSolver solver = new BrentSolver();
    solver.setRoot(1.0);
    ComputableFunction function = functionFactory.createFunction(x -> x - 1.0);
    assertFalse(solver.findRoot(function, checker, 10, 1.0, 1.0));
  }

  @Test
  public void testGetRoot() {
    ComputableFunction function = functionFactory.createFunction(x -> x * x - 2.0);
    solver.findRoot(function, checker, 10, -2.0, 2.0);
    double root = solver.getRoot();
    assertTrue(root > -0.01 && root < 0.01);
    assertEquals(Math.sqrt(2.0), root, 0.01);
  }

}