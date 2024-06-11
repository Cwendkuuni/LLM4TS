import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class BaseSecantSolverTest {

    @Mock
    private UnivariateRealFunction mockFunction;

    @Test
    public void testSolve_WhenRootWithinBounds_ExpectValidSolution() {
        // Arrange
        when(mockFunction.value(1.0)).thenReturn(10.0);
        when(mockFunction.value(2.0)).thenReturn(5.0);

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI);

        // Act
        double result = solver.solve(100, mockFunction, 1.0, 2.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Assert
        assertEquals(1.5, result, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_WhenUpperAndLowerBoundsEqual_ExpectIllegalArgumentException() {
        // Arrange
        when(mockFunction.value(1.0)).thenReturn(10.0);
        when(mockFunction.value(1.0)).thenReturn(5.0);

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI);

        // Act
        double result = solver.solve(100, mockFunction, 1.0, 1.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Assert
        // Expecting an IllegalArgumentException
    }

    @Test
    public void testSolve_WhenRootOutsideOfBounds_ExpectValidSolutionOnOneSide() {
        // Arrange
        when(mockFunction.value(1.0)).thenReturn(10.0);
        when(mockFunction.value(2.0)).thenReturn(-5.0);

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI);

        // Act
        double result = solver.solve(100, mockFunction, 1.0, 2.0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);

        // Assert
        assertEquals(1.4, result, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }

    @Test(expected = RuntimeException.class)
    public void testSolve_WhenInvalidFunctionProvided_ExpectRuntimeException() {
        // Arrange
        when(mockFunction.value(1.0)).thenThrow(new RuntimeException());

        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI);

        // Act
        double result = solver.solve(100, mockFunction, 1.0, 2.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        // Assert
        // Expecting a RuntimeException
    }

    @Test
    public void testSolve_WhenMultipleSolutions_ExpectValidRootBasedOnAllowedSolution() {
        // Arrange
        when(mockFunction.value(1.0)).thenReturn(10.0);
        when(mockFunction.value(2.0)).thenReturn(-5.0);
        
        BaseSecantSolver solver = new BaseSecantSolver(BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY,
                BaseSecantSolver.Method.REGULA_FALSI);

        // Act
        double result = solver.solve(100, mockFunction, 1.0, 2.0, BaseSecantSolver.AllowedSolution.BELOW_SIDE);

        // Assert
        assertEquals(1.4, result, BaseSecantSolver.DEFAULT_ABSOLUTE_ACCURACY);
    }
}