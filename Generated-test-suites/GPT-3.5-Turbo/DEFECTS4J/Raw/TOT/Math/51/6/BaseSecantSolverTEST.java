import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setup() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve_WhenRootIsWithinBounds_ReturnsRoot() {
        // Arrange
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        };

        // Act
        double root = solver.solve(100, function, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Assert
        assertEquals(5, root, 0);
    }

    @Test
    public void testSolve_WhenRootIsAtMin_ReturnsMin() {
        // Arrange
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 0;
            }
        };

        // Act
        double root = solver.solve(100, function, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Assert
        assertEquals(0, root, 0);
    }

    @Test
    public void testSolve_WhenRootIsAtMax_ReturnsMax() {
        // Arrange
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 10;
            }
        };

        // Act
        double root = solver.solve(100, function, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Assert
        assertEquals(10, root, 0);
    }

    @Test
    public void testSolve_WhenRootIsNotBracketed_ThrowsMathInternalError() {
        // Arrange
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5;
            }
        };

        // Act & Assert
        try {
            solver.solve(100, function, 0, 4, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Expected MathInternalError to be thrown");
        } catch (MathInternalError ex) {
            // Expected exception
        }
    }
}