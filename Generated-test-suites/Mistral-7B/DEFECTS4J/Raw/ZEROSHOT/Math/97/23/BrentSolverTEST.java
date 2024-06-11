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
      public int converged(double x, double fx, double x2, double fx2) {
        return ConvergenceChecker.LOW;
      }
    };
    functionFactory = new FunctionFactory();
  }

  @Test
  public void testFindRootSimple() throws Exception {
    ComputableFunction function = functionFactory.createFunction(x -> Math.pow(x, 2) - 2);
    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, Math.pow(x0, 2), x1, Math.pow(x1, 2));
    double root = solver.getRoot();
    assertEquals(-1.0, root, 0.001);
  }

  @Test
  public void testFindRootComplex() throws Exception {
    ComputableFunction function = functionFactory.createFunction(x -> Math.log(1 + Math.pow(x, 2)) - x);
    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
    double root = solver.getRoot();
    assertEquals(1.0, root, 0.01);
  }

  @Test(expected = FunctionException.class)
  public void testFindRootException() throws Exception {
    ComputableFunction function = functionFactory.createFunction((x) -> { throw new FunctionException("test"); });
    double x0 = 1.0;
    double x1 = 2.0;
    solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

}