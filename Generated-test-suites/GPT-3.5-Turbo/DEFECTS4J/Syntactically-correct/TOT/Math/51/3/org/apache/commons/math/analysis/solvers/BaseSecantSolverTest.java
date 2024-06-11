import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
    }

    @Test
    public void testSolve_ExactRootOnLeftSide_ReturnsRoot() {
        // Arrange
        double expected = 0.0;

        // Act
        double result = solver.solve(100, (x) -> x, -1.0, 1.0, 0.0);

        // Assert
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_ExactRootOnRightSide_ReturnsRoot() {
        // Arrange
        double expected = 0.0;

        // Act
        double result = solver.solve(100, (x) -> x, -1.0, 1.0, 1.0);

        // Assert
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_ExactRootInRange_ReturnsRoot() {
        // Arrange
        double expected = 0.0;

        // Act
        double result = solver.solve(100, (x) -> x, -1.0, 1.0, 0.5);

        // Assert
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_ExactRootOnBoundary_ReturnsRoot() {
        // Arrange
        double expected = 0.0;

        // Act
        double result = solver.solve(100, (x) -> x, 0.0, 1.0, 0.0);

        // Assert
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_ExactRootNotInRange_ThrowsMathIllegalStateException() {
        // Arrange
        UnivariateRealFunction f = (x) -> x;

        // Act & Assert
        assertThrows(MathIllegalStateException.class, () -> solver.solve(100, f, -1.0, 0.0, 1.0));
    }

    @Test
    public void testSolve_MaxEvaluationReached_ThrowsTooManyEvaluationsException() {
        // Arrange
        UnivariateRealFunction f = (x) -> x;

        // Act & Assert
        assertThrows(TooManyEvaluationsException.class, () -> solver.solve(1, f, -1.0, 1.0, 0.0));
    }

    @Test
    public void testSolve_FunctionValueAccuracyReached_ReturnsApproximation() {
        // Arrange
        double expected = 0.00040932202301885776;

        // Act
        double result = solver.solve(100, (x) -> (x * x) - 0.0000001, -1.0, 1.0, 0.5);

        // Assert
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_RelativeAccuracyReached_ReturnsApproximation() {
        // Arrange
        double expected = 1.0;

        // Act
        double result = solver.solve(100, (x) -> (x * x) - 1.0, 0.0, 2.0, 0.5);

        // Assert
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_AbsoluteAccuracyReached_ReturnsApproximation() {
        // Arrange
        double expected = 2.000009973346234;

        // Act
        double result = solver.solve(100, (x) -> (x * x) - 4.0, 0.0, 3.0, 2.5);

        // Assert
        assertEquals(expected, result, 1e-6);
    }

}