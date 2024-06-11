import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_MaxEvalGreaterThanZero_FunctionRootWithinBounds_ReturnsApproxRoot() {
        // Arrange
        UnivariateRealFunction f = x -> x - 1;
        double min = 0;
        double max = 2;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        double expected = 1.0;

        // Act
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, min, max, allowedSolution);

        // Assert
        Assert.assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_MaxEvalGreaterThanZero_FunctionRootAtMin_ReturnsRoot() {
        // Arrange
        UnivariateRealFunction f = x -> x;
        double min = 0;
        double max = 2;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        double expected = 0.0;

        // Act
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, min, max, allowedSolution);

        // Assert
        Assert.assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_MaxEvalGreaterThanZero_MaxEvalExceeded_ThrowsMathInternalError() {
        // Arrange
        UnivariateRealFunction f = x -> x - 1;
        double min = 0;
        double max = 2;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;

        // Act
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            solver.solve(0, f, min, max, allowedSolution);
            Assert.fail("Expected MathInternalError to be thrown");
        } catch (MathInternalError e) {
            // Assert
            Assert.assertEquals("Maximal number of evaluations exceeded", e.getMessage());
        }
    }

    @Test
    public void testSolve_MaxEvalGreaterThanZero_NoRootWithinBounds_ThrowsMathInternalError() {
        // Arrange
        UnivariateRealFunction f = x -> x + 1;
        double min = 0;
        double max = 2;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;

        // Act
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        try {
            solver.solve(100, f, min, max, allowedSolution);
            Assert.fail("Expected MathInternalError to be thrown");
        } catch (MathInternalError e) {
            // Assert
            Assert.assertEquals("Function values at endpoints do not have different signs", e.getMessage());
        }
    }

    @Test
    public void testSolve_MaxEvalGreaterThanZero_FunctionValueAccuracyReached_ReturnsApproxRoot() {
        // Arrange
        UnivariateRealFunction f = x -> x - 1;
        double min = 0;
        double max = 2;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        double expected = 1.0;

        // Act
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, min, max, allowedSolution);

        // Assert
        Assert.assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_MaxEvalGreaterThanZero_ApproximationWithinAccuracy_ReturnsApproxRoot() {
        // Arrange
        UnivariateRealFunction f = x -> x - 1;
        double min = 0;
        double max = 2;
        AllowedSolution allowedSolution = AllowedSolution.ANY_SIDE;
        double expected = 1.0;

        // Act
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, min, max, allowedSolution);

        // Assert
        Assert.assertEquals(expected, result, 1e-6);
    }

}