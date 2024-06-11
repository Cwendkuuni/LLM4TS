import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BisectionSolver;

public class BisectionSolverTest {
  // Test constructor
  @Test
  public void testConstructor() {
    BisectionSolver solver = new BisectionSolver();
    assertNotNull(solver);
  }

  // Test solve with deprecated method
  @Test
  public void testSolveDeprecated() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x*x - 4;
      }
    };
    double result = new BisectionSolver(f).solve(0, 10);
    assertEquals(2.0, result, 1E-6);
  }

  // Test solve with deprecated method
  @Test
  public void testSolveDeprecatedWithInitial() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x*x - 4;
      }
    };
    double result = new BisectionSolver(f).solve(0, 10, 5);
    assertEquals(2.0, result, 1E-6);
  }

  // Test solve
  @Test
  public void testSolve() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
    public double value(double x) throws FunctionEvaluationException {
        return x*x - 4;
      }
    };
    BisectionSolver solver = new BisectionSolver();
    double result = solver.solve(f, 0, 10);
    assertEquals(2.0, result, 1E-6);
  }

  // Test solve with initial
  @Test
  public void testSolveWithInitial() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x*x - 4;
      }
    };
    BisectionSolver solver = new BisectionSolver();
    double result = solver.solve(f, 0, 10, 5);
    assertEquals(2.0, result, 1E-6);
  }

  // Test solve with root at min
  @Test
  public void testSolveRootAtMin() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x*x - 4;
      }
    };
    BisectionSolver solver = new BisectionSolver();
    double result = solver.solve(f, -2, 10);
    assertEquals(-2.0, result, 1E-6);
  }

  // Test solve with root at max
  @Test
  public void testSolveRootAtMax() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x*x - 4;
      }
    };
    BisectionSolver solver = new BisectionSolver();
    double result = solver.solve(f, -10, 2);
    assertEquals(2.0, result, 1E-6);
  }

  // Test solve with no root
  @Test(expected = RuntimeException.class)
  public void testSolveNoRoot() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x*x + 4;
      }
    };
    BisectionSolver solver = new BisectionSolver();
    double result = solver.solve(f, -2, 2);
  }

  // Test solve with max iterations exceeded
  @Test(expected = RuntimeException.class)
  public void testSolveMaxIterationsExceeded() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        return x*x - 4;
      }
    };
    BisectionSolver solver = new BisectionSolver();
    solver.setMaximalIterationCount(5);
    double result = solver.solve(f, 0, 10);
  }

  // Test solve with function evaluation exception
  @Test(expected = RuntimeException.class)
  public void testSolveFunctionEvaluationException() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) throws FunctionEvaluationException {
        if (x == 2) {
          throw new FunctionEvaluationException("Error evaluating function");
        }
        return x*x - 4;
      }
    };
    BisectionSolver solver = new BisectionSolver();
    double result = solver.solve(f, 0, 10);
  }

}