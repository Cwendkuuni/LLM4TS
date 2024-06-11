import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSecantSolverTest {

  @Test
  public void testSolve() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    UnivariateRealFunction function = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 1;
      }
    };
    double result = solver.solve(100, function, -2, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(1.0, result, 1e-6);
  }

  @Test
  public void testSolveWithStartValue() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
    UnivariateRealFunction function = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 1;
      }
    };
    double result = solver.solve(100, function, -2, 2, 0.5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(1.0, result, 1e-6);
  }

  @Test
  public void testSolveWithAllowedSolution() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
    UnivariateRealFunction function = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 1;
      }
    };
    double result = solver.solve(100, function, -2, 2, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
    assertEquals(-1.0, result, 1e-6);
  }

  @Test
  public void testDoSolve() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    solver.setRelativeAccuracy(1e-9);
    solver.setAbsoluteAccuracy(1e-12);
    solver.setFunctionValueAccuracy(1e-15);
    solver.setAllowedSolution(BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
    UnivariateRealFunction function = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 1;
      }
    };
    double result = solver.doSolve();
    assertEquals(1.0, result, 1e-6);
  }

  @Test(expected = MathInternalError.class)
  public void testDoSolveWithInvalidMethod() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
    solver.doSolve();
  }

  @Test(expected = RuntimeException.class)
  public void testVerifyBracketing() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    solver.verifyBracketing(-2, 2);
  }

}