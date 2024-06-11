import org.junit.Test;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.MathIllegalArgumentException;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

  @Test
  public void testSolve1() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5;
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

    assertEquals(5.0, solver.solve(100, f, 0, 10), 1e-6);
  }

  @Test
  public void testSolve2() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 1;
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

    assertEquals(1.0, solver.solve(100, f, 0, 10), 1e-6);
    assertEquals(-1.0, solver.solve(100, f, -10, 0), 1e-6);
  }

  @Test
  public void testSolve3() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

    assertEquals(2.0, solver.solve(100, f, 0, 10), 1e-6);
    assertEquals(-2.0, solver.solve(100, f, -10, 0), 1e-6);
  }

  @Test(expected = MathIllegalStateException.class)
  public void testSolve4() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x;
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

    solver.solve(100, f, 0, 10);
  }

  @Test(expected = MathIllegalArgumentException.class)
  public void testSolve5() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x;
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

    solver.solve(100, f, 0, 0);
  }

  @Test(expected = MathInternalError.class)
  public void testSolve6() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x;
      }
    };

    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

    solver.solve(100, f, 0, 10);
  }
}