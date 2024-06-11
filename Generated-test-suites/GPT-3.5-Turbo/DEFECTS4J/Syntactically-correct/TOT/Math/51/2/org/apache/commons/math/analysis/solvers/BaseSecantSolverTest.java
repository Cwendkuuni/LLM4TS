import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WhenFunctionIsConstant_ReturnsExpectedResult() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 1.0; // Constant function
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Act
        double result = solver.solve(100, f, 0, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Assert
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolve_WhenFunctionHasNoRoot_ReturnsExpectedResult() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 1.0; // No root
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Act
        double result = solver.solve(100, f, 0, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Assert
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testSolve_WhenFunctionHasRoot_ReturnsExpectedResult() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x; // Root at x = 0
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

        // Act
        double result = solver.solve(100, f, -1, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Assert
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testSolve_WhenFunctionCannotConverge_ThrowsMathInternalError() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x; // Cannot converge
            }
        };
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        try {
            // Act
            double result = solver.solve(100, f, 0, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Expected MathInternalError to be thrown");
        } catch (MathInternalError e) {
            // Assert
            assertTrue(e.getMessage().contains("Cannot converge"));
        }
    }

    // More test cases...

}