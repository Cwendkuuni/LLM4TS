import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {
  
  private BaseSecantSolver solver;
  
  @Before
  public void setUp() {
    solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
  }
  
  @Test
  public void testSolve_ExactRoot() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5;
      }
    };
    double root = solver.solve(100, f, 4, 6, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(5.0, root, 1e-6);
  }
  
  @Test
  public void testSolve_NoRootInInterval() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5;
      }
    };
    double root = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(Double.NaN, root, 1e-6);
  }
  
  @Test
  public void testSolve_UnderApproximation() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5;
      }
    };
    double root = solver.solve(100, f, 2, 6, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(4.999999, root, 1e-6);
  }
  
  @Test
  public void testSolve_OverApproximation() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5;
      }
    };
    double root = solver.solve(100, f, 4, 8, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(5.000001, root, 1e-6);
  }
  
  @Test
  public void testSolve_LeftSideSolution() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5;
      }
    };
    double root = solver.solve(100, f, 4, 6, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
    assertEquals(4.999999, root, 1e-6);
  }
  
  @Test
  public void testSolve_RightSideSolution() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5;
      }
    };
    double root = solver.solve(100, f, 4, 6, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
    assertEquals(5.000001, root, 1e-6);
  }
  
  @Test
  public void testSolve_BelowSideSolution() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5;
      }
    };
    double root = solver.solve(100, f, 4, 6, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
    assertEquals(4.999999, root, 1e-6);
  }
  
  @Test
  public void testSolve_AboveSideSolution() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5;
      }
    };
    double root = solver.solve(100, f, 4, 6, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
    assertEquals(5.000001, root, 1e-6);
  }
  
  @Test
  public void testSolve_TypicalCase() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x * x - 4;
      }
    };
    double root = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(2.0, root, 1e-6);
  }
  
  @Test
  public void testSolve_EdgeCase() {
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5;
      }
    };
    double root = solver.solve(100, f, -10, 20, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    assertEquals(5.0, root, 1e-6);
  }
  
  @Test(expected = MathInternalError.class)
  public void testSolve_InvalidMethod() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.SECANT);
    UnivariateRealFunction f = new UnivariateRealFunction() {
      public double value(double x) {
        return x - 5;
      }
    };
    solver.solve(100, f, 4, 6, BaseSecantSolver.AllowedSolution.ANY_SIDE);
  }
  
}