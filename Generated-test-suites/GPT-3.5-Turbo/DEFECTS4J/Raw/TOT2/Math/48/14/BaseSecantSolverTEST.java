import static org.junit.Assert.assertEquals;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.junit.Test;

public class BaseSecantSolverTest {
  
  BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
  
  @Test
  public void testSolve() {
    UnivariateRealFunction f = new TestFunction();
    double result = solver.solve(100, f, -10, 10);
    assertEquals(0.0, f.value(result), 1e-6);
  }
  
  @Test(expected = ConvergenceException.class)
  public void testSolveNoSolution() {
    UnivariateRealFunction f = new TestFunctionNoSolution();
    solver.solve(100, f, -10, 10);
  }
  
  private class TestFunction implements UnivariateRealFunction {
    public double value(double x) {
      return x - 5;
    }
  }
  
  private class TestFunctionNoSolution implements UnivariateRealFunction {
    public double value(double x) {
      return x * x + 1;
    }
  }
  
}