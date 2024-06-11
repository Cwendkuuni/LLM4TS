import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

  @Test
  public void testSolve_ExactSolution() {
    // Arrange
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return 0; // Exact solution
      }
    };

    // Act
    double result = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return 0; // Dummy implementation
      }
    }, -10, 10);

    // Assert
    Assert.assertEquals(0.0, result, 0.0);
  }

  @Test
  public void testSolve_AnySide_AllowedSolution() {
    // Arrange
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return 1; // Positive function value
      }
    };

    // Act
    double result = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return x; // Dummy implementation
      }
    }, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

    // Assert
    Assert.assertEquals(0.0, result, 0.0);
  }

  @Test
  public void testSolve_LeftSide_AllowedSolution() {
    // Arrange
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return 1; // Positive function value
      }
    };

    // Act
    double result = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return x; // Dummy implementation
      }
    }, -10, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);

    // Assert
    Assert.assertEquals(-10.0, result, 0.0);
  }

  @Test
  public void testSolve_RightSide_AllowedSolution() {
    // Arrange
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return 1; // Positive function value
      }
    };

    // Act
    double result = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return x; // Dummy implementation
      }
    }, -10, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

    // Assert
    Assert.assertEquals(10.0, result, 0.0);
  }

  @Test
  public void testSolve_BelowSide_AllowedSolution() {
    // Arrange
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return 1; // Positive function value
      }
    };

    // Act
    double result = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return x; // Dummy implementation
      }
    }, -10, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);

    // Assert
    Assert.assertEquals(-10.0, result, 0.0);
  }

  @Test
  public void testSolve_AboveSide_AllowedSolution() {
    // Arrange
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return -1; // Negative function value
      }
    };

    // Act
    double result = solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return x; // Dummy implementation
      }
    }, -10, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);

    // Assert
    Assert.assertEquals(10.0, result, 0.0);
  }

  @Test(expected = MathInternalError.class)
  public void testSolve_InvalidAllowedSolution_ThrowsMathInternalError() {
    // Arrange
    BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
      @Override
      protected double computeObjectiveValue(double x) {
        return 1; // Positive function value
      }
    };

    // Act
    solver.solve(100, new UnivariateRealFunction() {
      @Override
      public double value(double x) {
        return x; // Dummy implementation
      }
    }, -10, 10, BaseSecantSolver.AllowedSolution.NONE);
  }
}