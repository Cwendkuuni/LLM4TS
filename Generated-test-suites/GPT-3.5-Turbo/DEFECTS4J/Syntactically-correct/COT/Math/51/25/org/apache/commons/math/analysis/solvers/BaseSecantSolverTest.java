
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.solvers.BracketedUnivariateRealSolver;
import org.junit.Test;

public class BaseSecantSolverTest {

  @Test
  public void testSolve() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.sin(x);
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

    double solution = solver.solve(100, f, 0, 2*Math.PI);
    assertEquals(Math.PI, solution, 1e-6);
  }

  @Test
  public void testSolveWithBounds() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.sin(x);
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

    double solution = solver.solve(100, f, 0, Math.PI);
    assertEquals(Math.PI, solution, 1e-6);
    solution = solver.solve(100, f, Math.PI, 2*Math.PI);
    assertEquals(Math.PI*2, solution, 1e-6);
  }

  @Test
  public void testSolveWithStartValue() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.sin(x);
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

    double solution = solver.solve(100, f, 0, 2*Math.PI, Math.PI/2);
    assertEquals(Math.PI, solution, 1e-6);
  }

  @Test
  public void testSolveWithAllowedSolutionAnySide() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.sin(x);
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

    double solution = solver.solve(100, f, 0, 2*Math.PI, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(Math.PI, solution, 1e-6);
  }

  @Test
  public void testSolveWithAllowedSolutionLeftSide() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.sin(x);
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

    double solution = solver.solve(100, f, 0, 2*Math.PI, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
    assertEquals(0, solution, 1e-6);
  }

  @Test
  public void testSolveWithAllowedSolutionRightSide() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.sin(x);
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

    double solution = solver.solve(100, f, 0, 2*Math.PI, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
    assertEquals(2*Math.PI, solution, 1e-6);
  }

  @Test
  public void testSolveWithAllowedSolutionBelowSide() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.sin(x);
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

    double solution = solver.solve(100, f, 0, 2*Math.PI, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
    assertEquals(Math.PI, solution, 1e-6);
  }

  @Test
  public void testSolveWithAllowedSolutionAboveSide() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.sin(x);
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

    double solution = solver.solve(100, f, 0, 2*Math.PI, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
    assertEquals(Math.PI, solution, 1e-6);
  }

  @Test(expected = MathInternalError.class)
  public void testInvalidAllowedSolution() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return Math.sin(x);
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY, BaseSecantSolver.Method.REGULA_FALSI);

    double solution = solver.solve(100, f, 0, 2*Math.PI, BaseSecantSolver.AllowedSolution.valueOf("INVALID"));
  }

}
