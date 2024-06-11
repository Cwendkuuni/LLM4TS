import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  private BrentSolver solver;
  private ComputableFunction function;
  private ConvergenceChecker checker;

  @Before
  public void setUp() {
    solver = new BrentSolver();
    function = new MockComputableFunction();
    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xOld, double fxOld) {
        return ConvergenceChecker.UNDECIDED;
      }
    };
  }

  @Test
  public void testFindRoot_BasicFunctionality() {
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.5;
    double[] inputs = {x0, x1};
    double[] outputs = {1.0, 1.5, 2.0};

    for (int i = 0; i < inputs.length; i++) {
      function.setInput(i, inputs[i]);
      solver.findRoot(function, checker, 10, inputs[i], function.valueAt(inputs[i]), inputs[i + 1], function.valueAt(inputs[i + 1]));
      Assert.assertEquals(expectedRoot, solver.getRoot(), epsilon);
      function.reset();
      solver.reset();
    }
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() {
    double x0 = 1.0;
    double x1 = 2.0;
    function.setException(new FunctionException("Test Exception"));
    solver.findRoot(function, checker, 10, x0, function.valueAt(x0), x1, function.valueAt(x1));
  }

  @Test
  public void testFindRoot_ConvergenceChecker() {
    double x0 = 1.0;
    double x1 = 2.0;
    double expectedRoot = 1.5;
    double[] inputs = {x0, x1};
    double[] outputs = {1.0, 1.5, 2.0};

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double fx, double xOld, double fxOld) {
        if (x == expectedRoot) {
          return ConvergenceChecker.LOW;
        } else if (x > expectedRoot) {
          return ConvergenceChecker.HIGH;
        } else {
          return ConvergenceChecker.UNDECIDED;
        }
      }
    };

    for (int i = 0; i < inputs.length; i++) {
      function.setInput(i, inputs[i]);
      solver.findRoot(function, checker, 10, inputs[i], function.valueAt(inputs[i]), inputs[i + 1], function.valueAt(inputs[i + 1]));
      Assert.assertEquals(expectedRoot, solver.getRoot(), epsilon);
      function.reset();
      solver.reset();
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRoot_IllegalArgumentException() {
    solver.findRoot(null, checker, 10, 1.0, 1.0, 2.0, 2.0);
  }
}