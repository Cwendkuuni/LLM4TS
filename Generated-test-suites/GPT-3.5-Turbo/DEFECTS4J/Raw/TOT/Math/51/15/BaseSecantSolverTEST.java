import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

  private BaseSecantSolver solver;

  @Before
  public void setUp(){
    solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
  }

  @Test
  public void testSolve_ExactRoot_ReturnsRootValue() {
    double root = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5;  // Exact root is 5
      }
    }, 0, 10);

    assertEquals(5, root, 1e-6);
  }

  @Test
  public void testSolve_WithinAbsoluteError_ReturnsApproximation() {
    double root = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;  // Roots are -2 and 2, but we only need 2
      }
    }, 0, 5);

    assertEquals(2, root, 1e-6);
  }

  @Test(expected = MathInternalError.class)
  public void testSolve_InvalidMethod_ThrowsMathInternalError() {
    BaseSecantSolver invalidSolver = new BaseSecantSolver(1e-6, null);

    // The solver should throw a MathInternalError due to invalid method
    invalidSolver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x;
      }
    }, 0, 10);
  }

  @Test(expected = MathIllegalArgumentException.class)
  public void testSolve_InvalidBracketing_ThrowsMathIllegalArgumentException() {
    // No roots within the specified interval, should throw an exception
    solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x;
      }
    }, -1, -10);
  }

  @Test
  public void testSolve_AccuracyWithLeftSideSolution_ReturnsApproximation() {
    solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.LEFT_SIDE);
    
    double root = solver.solve(100, new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;  // Roots are -2 and 2, but we only need -2
      }
    }, 0, 5);

    assertEquals(-2, root, 1e-6);
  }

  // Add more test cases...

}