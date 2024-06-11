import org.junit.Test;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.AllowedSolution;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {

  // Test computeObjectiveValue()
  @Test
  public void testComputeObjectiveValue() {
    BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    double result = solver.computeObjectiveValue(3.0);
    assertEquals(9.0, result, 1e-6);
  }

  // Test verifyBracketing() - valid bracketing
  @Test
  public void testVerifyBracketingValid() {
    BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    try {
      solver.verifyBracketing(2.0, 3.0);
    } catch (Exception e) {
      fail("Exception thrown when bracketing is valid");
    }
  }

  // Test verifyBracketing() - invalid bracketing
  @Test(expected = math.exception.MathIllegalArgumentException.class)
  public void testVerifyBracketingInvalid() {
    BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    solver.verifyBracketing(4.0, 3.0);
  }

  // Test solve() - exact root
  @Test
  public void testSolveExactRoot() {
    BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5.0;
      }
    };
    double result = solver.solve(100, f, 0.0, 10.0, AllowedSolution.ANY_SIDE);
    assertEquals(5.0, result, 1e-6);
  }

  // Test solve() - function value is too small
  @Test
  public void testSolveFunctionValueTooSmall() {
    BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5.0;
      }
    };
    double result = solver.solve(100, f, 0.0, 10.0, AllowedSolution.ANY_SIDE);
    assertTrue(Math.abs(f.value(result)) <= 1e-6);
  }

  // Test solve() - within given accuracies
  @Test
  public void testSolveWithinAccuracies() {
    BaseSecantSolver solver = new BaseSecantSolverTestImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5.0;
      }
    };
    double result = solver.solve(100, f, 0.0, 10.0, AllowedSolution.ANY_SIDE);
    assertTrue(Math.abs(result - 5.0) <= Math.max(1e-6 * Math.abs(result), 1e-6));
  }

  // Custom implementation of BaseSecantSolver for testing purposes
  private class BaseSecantSolverTestImpl extends BaseSecantSolver {
    public BaseSecantSolverTestImpl(double absoluteAccuracy, Method method) {
      super(absoluteAccuracy, method);
    }
    // Override computeObjectiveValue method
    protected double computeObjectiveValue(double x) {
      return x * x;
    }
  }

}