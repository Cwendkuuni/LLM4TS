import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.FunctionUtils;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot_Simple() {
    ComputableFunction function = new FunctionUtils.SinFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double[] expectedRoots = {-1.0, 1.0};
    double[][] inputs = {{-2.0, 2.0, 0.0}, {1.0, 3.0, 2.0}};

    for (int i = 0; i < inputs.length; i++) {
      double[] input = inputs[i];
      double[] expectedRoot = expectedRoots[i];
      boolean result = solver.findRoot(function, checker, 100, input[0], input[1], input[2], input[3]);
      Assert.assertEquals(expectedRoot[0], solver.getRoot(), epsilon);
      Assert.assertTrue(result);
    }
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_FunctionException() {
    ComputableFunction function = new FunctionUtils.DivideByZeroFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double[] inputs = {0.0, 0.0, 0.0};
    solver.findRoot(function, checker, 100, inputs[0], inputs[1], inputs[2], inputs[3]);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFindRoot_IllegalArgumentException() {
    ComputableFunction function = new FunctionUtils.ConstantFunction(Double.NaN);
    ConvergenceChecker checker = new ConvergenceChecker.Default();
    BrentSolver solver = new BrentSolver();

    double[] inputs = {0.0, 0.0, 0.0};
    solver.findRoot(function, checker, 100, inputs[0], inputs[1], inputs[2], inputs[3]);
  }

  @Test(expected = ConvergenceChecker.ConvergenceStatus.FAILED.class)
  public void testFindRoot_ConvergenceFailed() {
    ComputableFunction function = new FunctionUtils.SinFunction();
    ConvergenceChecker checker = new ConvergenceChecker.MaxIterations(1);
    BrentSolver solver = new BrentSolver();

    double[] inputs = {-2.0, 2.0, 0.0};
    solver.findRoot(function, checker, 100, inputs[0], inputs[1], inputs[2], inputs[3]);
  }

}