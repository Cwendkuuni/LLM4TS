import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.solvers.BisectionSolver;

public class BisectionSolverTest {

  private BisectionSolver solver;
  private UnivariateRealFunction f;

  @Before
  public void setUp() {
    solver = new BisectionSolver();
    f = new UnivariateFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x - 2;
      }
    };
  }

  @Test
  public void testSolve() throws MaxIterationsExceededException, FunctionEvaluationException {
    double result = solver.solve(f, 1, 3);
    assertEquals(result, 2, 0.0001);
  }

  @Test(expected = MaxIterationsExceededException.class)
  public void testMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
    double result = solver.solve(f, 1, 3, 1);
  }

  @Test(expected = FunctionEvaluationException.class)
  public void testFunctionEvaluationError() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction errorFunction = new UnivariateFunction() {
      public double value(double x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException("Error evaluating function");
      }
    };
    double result = solver.solve(errorFunction, 1, 3);
  }

  @Test
  public void testLowerBoundary() throws MaxIterationsExceededException, FunctionEvaluationException {
    double result = solver.solve(f, 1, 2);
    assertEquals(result, 2, 0.0001);
  }

  @Test
  public void testUpperBoundary() throws MaxIterationsExceededException, FunctionEvaluationException {
    double result = solver.solve(f, 2, 3);
    assertEquals(result, 2, 0.0001);
  }

  @Test
  public void testZeroInterval() throws MaxIterationsExceededException, FunctionEvaluationException {
    double result = solver.solve(f, 2, 2);
    assertEquals(result, 2, 0.0001);
  }

  @Test
  public void testExtremeValues() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateFunction() {
      public double value(double x) throws FunctionEvaluationException {
        if (x == Double.POSITIVE_INFINITY) {
          return Double.POSITIVE_INFINITY;
        } else if (x == Double.NEGATIVE_INFINITY) {
          return Double.NEGATIVE_INFINITY;
        } else {
          throw new FunctionEvaluationException("Error evaluating function");
        }
      }
    };

    double result = solver.solve(f, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    assertTrue(Double.isInfinite(result));
  }

  @Test
  public void testNaNValues() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return Double.NaN;
      }
    };

    double result = solver.solve(f, 1, 3);
    assertTrue(Double.isNaN(result));
  }

}