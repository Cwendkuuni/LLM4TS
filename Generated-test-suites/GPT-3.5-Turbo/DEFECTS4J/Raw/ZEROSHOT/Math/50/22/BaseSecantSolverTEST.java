import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

  @Test
  public void testSolveImplementation() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        // implement objective function here
      }
    };

    double root = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        // implement function here
      }
    }, 0, 10);

    // assert root value here
  }

  @Test
  public void testSolveMethod1() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    double root = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        // implement function here
      }
    }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

    // assert root value here
  }

  @Test
  public void testSolveMethod2() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    double root = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        // implement function here
      }
    }, 0, 10, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);

    // assert root value here
  }

  @Test
  public void testSolveMethod3() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    double root = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        // implement function here
      }
    }, 0, 10, 5);

    // assert root value here
  }
}