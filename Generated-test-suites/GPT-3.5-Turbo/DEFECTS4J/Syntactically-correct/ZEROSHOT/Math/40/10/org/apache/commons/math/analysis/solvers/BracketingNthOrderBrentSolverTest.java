import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BracketingNthOrderBrentSolverTest {

  private BracketingNthOrderBrentSolver solver;

  @Before
  public void initialize() {
    solver = new BracketingNthOrderBrentSolver();
  }

  @Test
  public void testMaximalOrder() {
    assertEquals(5, solver.getMaximalOrder());
  }

  @Test(expected = NumberIsTooSmallException.class)
  public void testMaximalOrderException() {
    new BracketingNthOrderBrentSolver(1e-6, 1);
  }

  @Test
  public void testSolve() {
    double solution = solver.solve(100, new UnivariateFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    }, 0, 3);
    assertEquals(2, solution, 1e-6);
  }

  @Test
  public void testSolveWithStartValue() {
    double solution = solver.solve(100, new UnivariateFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    }, 0, 3, 1);
    assertEquals(2, solution, 1e-6);
  }

  @Test
  public void testSolveWithAllowedSolution() {
    double solution = solver.solve(100, new UnivariateFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    }, 0, 3, AllowedSolution.ABOVE_SIDE);
    assertEquals(2, solution, 1e-6);
  }

  @Test
  public void testSolveWithAllowedSolutionAndStartValue() {
    double solution = solver.solve(100, new UnivariateFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    }, 0, 3, 1, AllowedSolution.ABOVE_SIDE);
    assertEquals(2, solution, 1e-6);
  }

}