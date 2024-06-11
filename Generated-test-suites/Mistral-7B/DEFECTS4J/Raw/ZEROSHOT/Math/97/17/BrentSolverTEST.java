import org.junit.Test;
import org.junit.Assert;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws Exception {
    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.pow(x, 2) - 4.0;
      }
    };

    BrentSolver solver = new BrentSolver();
    ConvergenceChecker checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xMin, double fMin) {
        return (Math.abs(f) < 0.001) ? ConvergenceChecker.BOTH : ConvergenceChecker.NONE;
      }
    };

    Assert.assertTrue(solver.findRoot(function, checker, 100, -2.0, -3.0, 2.0, 3.0));
    Assert.assertEquals(0.0, solver.getRoot(), 0.001);

    solver.setRoot(Double.NaN); // reset root for next test

    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.exp(x) - 2.0;
      }
    };

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xMin, double fMin) {
        return Math.abs(f) < 0.0001 ? ConvergenceChecker.BOTH : ConvergenceChecker.NONE;
      }
    };

    Assert.assertTrue(solver.findRoot(function, checker, 100, -3.0, -2.0, 0.0, 2.0));
    Assert.assertEquals(1.0, solver.getRoot(), 0.0001);

    solver.setRoot(Double.NaN); // reset root for next test

    function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return Math.sin(x) - x;
      }
    };

    checker = new ConvergenceChecker() {
      @Override
      public int converged(double x, double f, double xMin, double fMin) {
        return Math.abs(f) < 0.01 ? ConvergenceChecker.BOTH : ConvergenceChecker.NONE;
      }
    };

    Assert.assertTrue(solver.findRoot(function, checker, 100, 0.0, 1.0, 0.0, 2.0));
    Assert.assertEquals(1.2732395447352251, solver.getRoot(), 0.01);
  }

}