import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WhenFunctionHasRootInRange_ShouldReturnRoot() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Act
        double result = solver.solve(100, f, 0, 10);

        // Assert
        Assert.assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSolve_WhenFunctionHasMultipleRootsInRange_ShouldReturnClosestRoot() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

        // Act
        double result = solver.solve(100, f, 0, Math.PI/2);

        // Assert
        Assert.assertEquals(Math.PI/2, result, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_WhenFunctionHasNoRootsInGivenRange_ShouldThrowIllegalArgumentException() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        // Act
        double result = solver.solve(100, f, 5, 10);

        // Assert (should throw exception)
    }

    @Test
    public void testSolve_WhenFunctionHasRootOnBoundary_ShouldReturnBoundary() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Act
        double result = solver.solve(100, f, 0, 5);

        // Assert
        Assert.assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolve_WhenFunctionValueOnBoundaryIsWithinAccuracy_ShouldReturnBoundary() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        // Act
        double result = solver.solve(100, f, 0, 1, 5, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        // Assert
        Assert.assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSolve_WhenFunctionValueOnBoundaryIsBelowAccuracy_ShouldReturnBoundary() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Act
        double result = solver.solve(100, f, 0, 1, 5, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        // Assert
        Assert.assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSolve_WhenFunctionValueOnBoundaryIsAboveAccuracy_ShouldReturnRootWithinAccuracy() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Act
        double result = solver.solve(100, f, 0, 0.9, 5, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);

        // Assert
        Assert.assertEquals(5.0, result, 1e-6);
    }

}