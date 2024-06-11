import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ConstantFunction;
import org.spaceroots.mantissa.functions.scalar.SquareFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

/**
 * Tests for the BrentSolver class.
 */
public class BrentSolverTest {

  /**
   * Test finding a root using a constant function.
   */
  @Test
  public void testConstantFunction() throws FunctionException {
    // Given
    ComputableFunction function = new ConstantFunction(4.0);
    ConvergenceChecker checker = new ConvergenceChecker.Polynomial(0.001);
    BrentSolver brentSolver = new BrentSolver();

    // When
    double root = brentSolver.findRoot(function, checker, 10, -10.0, 10.0)[0];

    // Then
    Assert.assertEquals("Root should be 4.0", 4.0, root, 0.01);
  }

  /**
   * Test finding a root using a square function.
   */
  @Test
  public void testSquareFunction() throws FunctionException {
    // Given
    ComputableFunction function = new SquareFunction();
    ConvergenceChecker checker = new ConvergenceChecker.Polynomial(0.001);
    BrentSolver brentSolver = new BrentSolver();

    // When
    double root = brentSolver.findRoot(function, checker, 10, -5.0, 5.0)[0];

    // Then
    Assert.assertEquals("Root should be 1.0", 1.0, root, 0.01);
  }

  /**
   * Test finding a root using a function with a single root.
   */
  @Test
  public void testFunctionWithASingleRoot() throws FunctionException {
    // Given
    ComputableFunction function = new FunctionWithASingleRoot();
    ConvergenceChecker checker = new ConvergenceChecker.Polynomial(0.001);
    BrentSolver brentSolver = new BrentSolver();

    // When
    double root = brentSolver.findRoot(function, checker, 10, -2.0, 2.0)[0];

    // Then
    Assert.assertEquals("Root should be 0.5", 0.5, root, 0.01);
  }

  /**
   * Test finding a root using a function with no root.
   */
  @Test(expected = FunctionException.class)
  public void testFunctionWithNoRoot() throws FunctionException {
    // Given
    ComputableFunction function = new FunctionWithNoRoot();
    ConvergenceChecker checker = new ConvergenceChecker.Polynomial(0.001);
    BrentSolver brentSolver = new BrentSolver();

    // When and Then
    brentSolver.findRoot(function, checker, 10, -10.0, 10.0);
  }

  /**
   * Test finding a root using a function with multiple roots.
   */
  @Test(expected = FunctionException.class)
  public void testFunctionWithMultipleRoots() throws FunctionException {
    // Given
    ComputableFunction function = new FunctionWithMultipleRoots();
    ConvergenceChecker checker = new ConvergenceChecker.Polynomial(0.001);
    BrentSolver brentSolver = new BrentSolver();

    // When and Then
    brentSolver.findRoot(function, checker, 10, -5.0, 5.0);
  }

  /**
   * A simple function for testing.
   */
  public static class FunctionWithASingleRoot implements ComputableFunction {

    @Override
    public double valueAt(double x) {
      return Math.pow(x - 0.5, 2) - 0.25;
    }
  }

  /**
   * A function with no root.
   */
  public static class FunctionWithNoRoot implements ComputableFunction {

    @Override
    public double valueAt(double x) {
      return Math.sin(Math.PI / 2.0) * x;
    }
  }

  /**
   * A function with multiple roots.
   */
  public static class FunctionWithMultipleRoots implements ComputableFunction {

    @Override
    public double valueAt(double x) {
      return Math.pow(x - 1.0, 3) - 1.0;
    }
  }
}