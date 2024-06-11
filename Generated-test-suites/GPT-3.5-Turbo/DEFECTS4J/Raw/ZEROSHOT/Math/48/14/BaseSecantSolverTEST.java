import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

  private class TestFunction implements UnivariateRealFunction {
    @Override
    public double value(double x) {
      return x*x - 4;
    }
  }

  @Test
  public void testSolve() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
    TestFunction f = new TestFunction();
    double result = solver.solve(1000, f, 0, 10);
    assertEquals(2.0, result, 1e-6);
  }

  @Test
  public void testSolveWithStartValue() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
    TestFunction f = new TestFunction();
    double result = solver.solve(1000, f, 0, 10, 5);
    assertEquals(2.0, result, 1e-6);
  }
}