import org.junit.Test;

public class BaseSecantSolverTest {

  @Test
  public void testSolve() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return 0;
      }
    };
    double result = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return 0;
      }
    }, 0, 1);
    assertEquals(0, result, 1e-6);
  }

  @Test
  public void testSolveWithAllowedSolutionRightSide() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return -x;
      }
    };
    double result = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return -x;
      }
    }, -1, 0, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
    assertEquals(0, result, 1e-6);
  }

  @Test
  public void testSolveWithAllowedSolutionLeftSide() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return -x;
      }
    };
    double result = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return -x;
      }
    }, -1, 0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
    assertEquals(-1, result, 1e-6);
  }

  @Test
  public void testSolveWithAllowedSolutionBelowSide() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return -x;
      }
    };
    double result = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return -x;
      }
    }, -1, 0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
    assertEquals(0, result, 1e-6);
  }

  @Test
  public void testSolveWithAllowedSolutionAboveSide() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return -x;
      }
    };
    double result = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return -x;
      }
    }, -1, 0, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
    assertEquals(-1, result, 1e-6);
  }

  @Test
  public void testSolveWithMaxEvalReached() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return 2 * x;
      }
    };
    double result = solver.solve(1, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return 2 * x;
      }
    }, 0, 1);
    assertEquals(solver.getMax(), result, 1e-6);
  }

  @Test
  public void testSolveWithInvalidRange() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return -x;
      }
    };
    try {
      double result = solver.solve(100, new UnivariateRealFunction() {
        @Override
        public double value(double x) {
          return -x;
        }
      }, 0, -1);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Exception thrown as expected
    }
  }

  @Test
  public void testSolveWithFunctionValueAccuracyReached() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return Math.sin(x);
      }
    };
    double result = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return Math.sin(x);
      }
    }, -Math.PI / 2, Math.PI / 2);
    assertEquals(0, result, 1e-6);
  }

  @Test
  public void testSolveWithRelativeAccuracyReached() {
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return Math.sin(x);
      }
    };
    double result = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return Math.sin(x);
      }
    }, -Math.PI / 6, Math.PI / 6);
    assertEquals(0, result, 1e-6);
  }

}