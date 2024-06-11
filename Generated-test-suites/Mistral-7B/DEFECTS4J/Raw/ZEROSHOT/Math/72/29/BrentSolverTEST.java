import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionFactory;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertEquals;

public class BrentSolverTest {

  private BrentSolver solver;
  private ConvergenceChecker checker;
  private FunctionFactory functionFactory;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        return ConvergenceChecker.NONE;
      }
    };
    functionFactory = new FunctionFactory();
  }

  @Test
  public void testFindRootSimple() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction(x -> Math.pow(x, 2) - 2);
    double x0 = 1.0;
    double x1 = 2.0;
    double root = -1.0;
    boolean result = solver.findRoot(function, checker, 10, x0, Math.pow(x0, 2), x1, Math.pow(x1, 2));
    assertEquals(root, solver.getRoot(), epsilon);
    assertEquals(true, result);
  }

  @Test
  public void testFindRootComplex() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction(x -> Math.sin(x) / x - 1);
    double x0 = 1.0;
    double x1 = 2.0;
    double root = Math.PI;
    boolean result = solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
    assertEquals(root, solver.getRoot(), epsilon);
    assertEquals(true, result);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() throws FunctionException {
    ComputableFunction function = functionFactory.createFunction(x -> 1 / x);
    double x0 = 1.0;
    double x1 = 1.1;
    double root = 0.0;
    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

}