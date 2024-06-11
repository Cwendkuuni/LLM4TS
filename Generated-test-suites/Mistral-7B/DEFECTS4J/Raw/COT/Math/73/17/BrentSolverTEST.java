import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionConstants;
import org.spaceroots.mantissa.functions.scalar.TestFunctions;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_simpleFunction() throws FunctionException {
    ComputableFunction function = TestFunctions.sinFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 0.0;
    int maxIter = 100;
    assertTrue(solver.findRoot(function, checker, maxIter, x0, FunctionConstants.sin(x0), x1, FunctionConstants.sin(x1)));
    assertEquals(Math.PI / 2, solver.getRoot(), 0.01);
  }

  @Test
  public void testFindRoot_functionWithRootAtZero() throws FunctionException {
    ComputableFunction function = TestFunctions.xSquaredFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = -1.0;
    int maxIter = 100;
    assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertEquals(0, solver.getRoot(), 0.01);
  }

  @Test
  public void testFindRoot_nonconvergingFunction() throws FunctionException {
    ComputableFunction function = TestFunctions.sinXSquaredFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();
    double x0 = 0.0;
    double x1 = 1.0;
    int maxIter = 100;
    assertFalse(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_functionThrowsException() throws FunctionException {
    ComputableFunction function = () -> { throw new FunctionException("Test exception"); };
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();
    double x0 = 1.0;
    double x1 = 0.0;
    int maxIter = 100;
    solver.findRoot(function, checker, maxIter, x0, Double.NaN, x1, Double.NaN);
  }

  @Test
  public void testFindRoot_functionWithLargeInterval() throws FunctionException {
    ComputableFunction function = TestFunctions.expFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Linear();
    BrentSolver solver = new BrentSolver();
    double x0 = -10.0;
    double x1 = 10.0;
    int maxIter = 100;
    assertTrue(solver.findRoot(function, checker, maxIter, x0, function.valueAt(x0), x1, function.valueAt(x1)));
    assertTrue(Double.isFinite(solver.getRoot()));
  }

}