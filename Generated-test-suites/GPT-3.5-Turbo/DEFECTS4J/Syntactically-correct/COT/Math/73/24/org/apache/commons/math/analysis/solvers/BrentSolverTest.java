import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

  @Test
  public void testFindRoot() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new TestFunction();
    ConvergenceChecker checker = new TestChecker();

    int maxIter = 100;
    double x0 = -1.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(rootFound);
    assertEquals(0.0, solver.getRoot(), 0.000001);
  }

  // Test additional scenarios and edge cases

  @Test
  public void testFindRootWithZeroFunction() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new ZeroFunction();
    ConvergenceChecker checker = new TestChecker();

    int maxIter = 100;
    double x0 = -1.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(rootFound);
    assertEquals(0.0, solver.getRoot(), 0.000001);
  }

  @Test
  public void testFindRootWithLinearFunction() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new LinearFunction(2.0, -1.0);
    ConvergenceChecker checker = new TestChecker();

    int maxIter = 100;
    double x0 = -1.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(rootFound);
    assertEquals(0.5, solver.getRoot(), 0.000001);
  }

  @Test
  public void testFindRootWithQuadraticFunction() throws FunctionException {
    BrentSolver solver = new BrentSolver();
    ComputableFunction function = new QuadraticFunction(1.0, -2.0, 1.0);
    ConvergenceChecker checker = new TestChecker();

    int maxIter = 100;
    double x0 = -1.0;
    double f0 = function.valueAt(x0);
    double x1 = 1.0;
    double f1 = function.valueAt(x1);

    boolean rootFound = solver.findRoot(function, checker, maxIter, x0, f0, x1, f1);

    assertTrue(rootFound);
    assertEquals(1.0, solver.getRoot(), 0.000001);
  }

  // Custom functions for testing

  private static class TestFunction implements ComputableFunction {

    @Override
    public double valueAt(double x) {
      return x * x;
    }
  }

  private static class ZeroFunction implements ComputableFunction {

    @Override
    public double valueAt(double x) {
      return 0.0;
    }
  }

  private static class LinearFunction implements ComputableFunction {

    private double a;
    private double b;

    public LinearFunction(double a, double b) {
      this.a = a;
      this.b = b;
    }

    @Override
    public double valueAt(double x) {
      return a * x + b;
    }
  }

  private static class QuadraticFunction implements ComputableFunction {

    private double a;
    private double b;
    private double c;

    public QuadraticFunction(double a, double b, double c) {
      this.a = a;
      this.b = b;
      this.c = c;
    }

    @Override
    public double valueAt(double x) {
      return a * x * x + b * x + c;
    }
  }

  private static class TestChecker implements ConvergenceChecker {

    @Override
    public int converged(double xLow, double fLow, double xHigh, double fHigh) {
      if (fLow == 0.0) {
        return ConvergenceChecker.LOW;
      }
      if (fHigh == 0.0) {
        return ConvergenceChecker.HIGH;
      }
      return ConvergenceChecker.NONE;
    }
  }

}