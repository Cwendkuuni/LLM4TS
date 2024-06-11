import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

  @Test
  public void testSolve() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return Math.sin(x) - x;
      }
    };

    double result = solver.solve(100, 0, 10);
    assertEquals(3.141592, result, 1e-6);
  }

  @Test
  public void testSolveInvalidInterval() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return Math.sin(x) - x;
      }
    };

    try {
      solver.solve(100, 10, 0);
      fail("IllegalArgumentException not thrown");
    } catch (IllegalArgumentException e) {
      // Pass
    }
  }

  @Test
  public void testSolveZeroFunctionValue() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return 0;
      }
    };

    double result = solver.solve(100, 0, 10);
    assertEquals(0, result, 1e-6);
  }

  @Test
  public void testSolveMaximumEvaluationsExceeded() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return Math.sin(x) - x;
      }
    };

    try {
      solver.solve(1, 0, 10);
      fail("ConvergenceException not thrown");
    } catch (ConvergenceException e) {
      // Pass
    }
  }

  @Test
  public void testSolveAbsoluteAccuracy() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-12, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return Math.sin(x) - x;
      }
    };

    double result = solver.solve(100, 0, 10);
    assertEquals(3.141592653589793, result, 1e-12);
  }

  @Test
  public void testSolveRelativeAccuracy() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-12, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return Math.sin(x) - x;
      }
    };

    double result = solver.solve(100, 0, 10);
    assertEquals(3.141592653589793, result, 1e-12);
  }

  @Test
  public void testSolveFunctionValueAccuracy() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-12, 1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      protected double computeObjectiveValue(double x) {
        return Math.sin(x) - x;
      }
    };

    double result = solver.solve(100, 0, 10);
    assertEquals(3.141592653589793, result, 1e-12);
  }
}