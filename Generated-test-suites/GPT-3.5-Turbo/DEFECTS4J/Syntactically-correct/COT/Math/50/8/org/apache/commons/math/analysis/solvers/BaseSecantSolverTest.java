import org.junit.Test;

public class BaseSecantSolverTest {

  @Test
  public void testSolve() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

    // Test Case 1
    UnivariateRealFunction f1 = new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return x - 2;
      }
    };
    double result1 = solver.solve(100, f1, 0, 4, AllowedSolution.ANY_SIDE);
    assertEquals(2.0, result1, 1e-6);

    // Test Case 2
    UnivariateRealFunction f2 = new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return x * x - 4;
      }
    };
    double result2 = solver.solve(100, f2, 0, 4, AllowedSolution.ANY_SIDE);
    assertEquals(2.0, result2, 1e-6);
  }

  @Test
  public void testSolveWithMaxEval() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

    // Test Case 3
    UnivariateRealFunction f3 = new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return x - 2;
      }
    };
    double result3 = solver.solve(5, f3, 0, 4, AllowedSolution.ANY_SIDE);
    assertEquals(2.0, result3, 1e-6);
  }

  @Test
  public void testSolveWithMinAndMax() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

    // Test Case 4
    UnivariateRealFunction f4 = new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return x - 2;
      }
    };
    double result4 = solver.solve(100, f4, 2, 4, AllowedSolution.ANY_SIDE);
    assertEquals(2.0, result4, 1e-6);
  }
}