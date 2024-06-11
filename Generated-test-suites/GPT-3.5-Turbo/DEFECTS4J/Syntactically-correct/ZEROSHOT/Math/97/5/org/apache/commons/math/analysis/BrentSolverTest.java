import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        return Math.pow(x, 2) - 4;
      }
    };
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x1, double f1, double x2, double f2) {
        if (Math.abs(f1) < Math.abs(f2)) {
          return ConvergenceChecker.LOW;
        } else {
          return ConvergenceChecker.HIGH;
        }
      }
    };
    double x0 = 1.0;
    double f0 = -3.0;
    double x1 = 2.0;
    double f1 = 0.0;
    boolean rootFound = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    Assert.assertTrue(rootFound);
    Assert.assertEquals(2.0, solver.getRoot(), 0.001);
  }

  // Add more test methods to test other methods in the BrentSolver class

}