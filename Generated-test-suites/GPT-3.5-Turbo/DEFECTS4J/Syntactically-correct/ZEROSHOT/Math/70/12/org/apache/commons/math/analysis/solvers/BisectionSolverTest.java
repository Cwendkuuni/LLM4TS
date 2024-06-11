import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;

public class BisectionSolverTest {

  @Test
  public void testSolve() throws FunctionEvaluationException, MaxIterationsExceededException {
    // Test case 1
    UnivariateRealFunction f1 = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5;
      }
    };
    BisectionSolver solver1 = new BisectionSolver(f1);
    double root1 = solver1.solve(0, 10);
    assertEquals(5.0, root1, 1E-6);

    // Test case 2
    UnivariateRealFunction f2 = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };
    BisectionSolver solver2 = new BisectionSolver(f2);
    double root2 = solver2.solve(-10, 10);
    assertEquals(2.0, root2, 1E-6);
  }

  @Test(expected = MaxIterationsExceededException.class)
  public void testSolveMaxIterationsExceeded() throws FunctionEvaluationException, MaxIterationsExceededException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.sin(x);
      }
    };
    BisectionSolver solver = new BisectionSolver(f);
    solver.setMaximalIterationCount(10);
    solver.solve(0, 2 * Math.PI);
  }

  @Test(expected = FunctionEvaluationException.class)
  public void testSolveFunctionEvaluationException() throws FunctionEvaluationException, MaxIterationsExceededException {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        throw new FunctionEvaluationException(x);
      }
    };
    BisectionSolver solver = new BisectionSolver(f);
    solver.solve(0, 10);
  }
}