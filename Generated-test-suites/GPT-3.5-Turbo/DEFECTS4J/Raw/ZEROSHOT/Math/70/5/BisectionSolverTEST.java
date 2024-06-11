import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;

public class BisectionSolverTest {

  @Test
  public void solve_returnsCorrectResult() throws MaxIterationsExceededException, FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.sin(x);
      }
    };

    BisectionSolver solver = new BisectionSolver();
    double result = solver.solve(f, 0, 2 * Math.PI);

    assertEquals(3.141592653589793, result, 0.00001);
  }

  @Test
  public void solve_throwsExceptionWhenMaxIterationsExceeded() throws FunctionEvaluationException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.sin(x);
      }
    };

    BisectionSolver solver = new BisectionSolver();
    double result = 0.0;

    try {
      result = solver.solve(f, -10, 10);
      fail("MaxIterationsExceededException not thrown");
    } catch (MaxIterationsExceededException e) {
      assertEquals(100, e.getMaximalIterationCount());
    }

    assertEquals(0.0, result, 0.00001);
  }
}