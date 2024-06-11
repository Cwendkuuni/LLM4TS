import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import static org.junit.Assert.*;

public class BrentSolverTest {

  @Test
  public void testFindRoot_typicalCase() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x - 4;  // f(x) = x^2 - 4
      }
    };

    double lowerBound = 1.0;
    double upperBound = 3.0;
    double tolerance = 1e-6;

    assertTrue(solver.findRoot(function, solver, 100, lowerBound, function.valueAt(lowerBound), upperBound, function.valueAt(upperBound)));

    // Check that the root is within the given bounds
    double root = solver.getRoot();
    assertTrue(root >= lowerBound && root <= upperBound);

    // Check if the root satisfies the function equation within the given tolerance
    double functionValueAtRoot = function.valueAt(root);
    assertTrue(Math.abs(functionValueAtRoot) < tolerance);
  }

  @Test
  public void testFindRoot_edgeCase() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) {
        return x * x;  // f(x) = x^2
      }
    };

    double lowerBound = Double.POSITIVE_INFINITY;
    double upperBound = Double.POSITIVE_INFINITY;

    assertTrue(solver.findRoot(function, solver, 100, lowerBound, function.valueAt(lowerBound), upperBound, function.valueAt(upperBound)));

    // Check that the root is within the given bounds
    double root = solver.getRoot();
    assertEquals(0.0, root, Double.MIN_VALUE);

    // Check if the root satisfies the function equation within the given tolerance
    double functionValueAtRoot = function.valueAt(root);
    assertEquals(0.0, functionValueAtRoot, Double.MIN_VALUE);
  }

  @Test(expected = FunctionException.class)
  public void testFindRoot_errorScenario() throws FunctionException {
    BrentSolver solver = new BrentSolver();

    ComputableFunction function = new ComputableFunction() {
      @Override
      public double valueAt(double x) throws FunctionException {
        throw new FunctionException("Error evaluating function");
      }
    };

    double lowerBound = -10.0;
    double upperBound = 10.0;

    // This should throw a FunctionException
    solver.findRoot(function, solver, 100, lowerBound, function.valueAt(lowerBound), upperBound, function.valueAt(upperBound));
  }
}