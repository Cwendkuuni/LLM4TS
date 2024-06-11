import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.MathInternalError;
import org.junit.Before;
import org.junit.Test;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    private UnivariateRealFunction function;

    @Before
    public void setUp() {
        solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        function = mock(UnivariateRealFunction.class);
    }

    @Test
    public void testSolve_WithRootInInterval_AnySide() {
        // Arrange
        double min = 0.0;
        double max = 2.0;
        AllowedUnivariateRealSolverSolution allowedSolution = AllowedUnivariateRealSolverSolution.ANY_SIDE;
        double expectedResult = 1.0;

        when(function.value(0.0)).thenReturn(-1.0);
        when(function.value(2.0)).thenReturn(1.0);
        when(function.value(1.0)).thenReturn(0.0);

        // Act
        double result = solver.solve(100, function, min, max, allowedSolution);

        // Assert
        assertEquals(expectedResult, result, 1e-6);
    }

    @Test
    public void testSolve_WithRootInInterval_LeftSide() {
        // Arrange
        double min = 0.0;
        double max = 2.0;
        AllowedUnivariateRealSolverSolution allowedSolution = AllowedUnivariateRealSolverSolution.LEFT_SIDE;
        double expectedResult = 0.999999;

        when(function.value(0.0)).thenReturn(-1.0);
        when(function.value(2.0)).thenReturn(1.0);
        when(function.value(0.999999)).thenReturn(0.000001);

        // Act
        double result = solver.solve(100, function, min, max, allowedSolution);

        // Assert
        assertEquals(expectedResult, result, 1e-6);
    }

    @Test
    public void testSolve_WithRootInInterval_RightSide() {
        // Arrange
        double min = 0.0;
        double max = 2.0;
        AllowedUnivariateRealSolverSolution allowedSolution = AllowedUnivariateRealSolverSolution.RIGHT_SIDE;
        double expectedResult = 1.0;

        when(function.value(0.0)).thenReturn(-1.0);
        when(function.value(2.0)).thenReturn(1.0);
        when(function.value(1.0)).thenReturn(0.0);

        // Act
        double result = solver.solve(100, function, min, max, allowedSolution);

        // Assert
        assertEquals(expectedResult, result, 1e-6);
    }

    @Test
    public void testSolve_WithRootInInterval_BelowSide() {
        // Arrange
        double min = 0.0;
        double max = 2.0;
        AllowedUnivariateRealSolverSolution allowedSolution = AllowedUnivariateRealSolverSolution.BELOW_SIDE;
        double expectedResult = 1.0;

        when(function.value(0.0)).thenReturn(-1.0);
        when(function.value(2.0)).thenReturn(1.0);
        when(function.value(1.0)).thenReturn(0.0);

        // Act
        double result = solver.solve(100, function, min, max, allowedSolution);

        // Assert
        assertEquals(expectedResult, result, 1e-6);
    }

    @Test
    public void testSolve_WithRootInInterval_AboveSide() {
        // Arrange
        double min = 0.0;
        double max = 2.0;
        AllowedUnivariateRealSolverSolution allowedSolution = AllowedUnivariateRealSolverSolution.ABOVE_SIDE;
        double expectedResult = 1.0;

        when(function.value(0.0)).thenReturn(-1.0);
        when(function.value(2.0)).thenReturn(1.0);
        when(function.value(1.0)).thenReturn(0.0);

        // Act
        double result = solver.solve(100, function, min, max, allowedSolution);

        // Assert
        assertEquals(expectedResult, result, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_InvalidAllowedSolution() {
        // Arrange
        double min = 0.0;
        double max = 2.0;
        AllowedUnivariateRealSolverSolution allowedSolution = AllowedUnivariateRealSolverSolution.INVALID;

        // Act
        solver.solve(100, function, min, max, allowedSolution);

        // Assert
        // Expecting MathInternalError to be thrown
    }

    @Test
    public void testSolve_WithNoRoots_AnySide() {
        // Arrange
        double min = 0.0;
        double max = 2.0;
        AllowedUnivariateRealSolverSolution allowedSolution = AllowedUnivariateRealSolverSolution.ANY_SIDE;
        double expectedResult = Double.NaN;

        when(function.value(0.0)).thenReturn(1.0);
        when(function.value(2.0)).thenReturn(1.0);

        // Act
        double result = solver.solve(100, function, min, max, allowedSolution);

        // Assert
        assertEquals(expectedResult, result, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_WithInvalidFunction() {
        // Arrange
        double min = 0.0;
        double max = 2.0;
        AllowedUnivariateRealSolverSolution allowedSolution = AllowedUnivariateRealSolverSolution.ANY_SIDE;

        // Act
        solver.solve(100, null, min, max, allowedSolution);

        // Assert
        // Expecting MathInternalError to be thrown
    }

}