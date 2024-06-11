import org.apache.commons.math.analysis.solvers.UnivariateRealSolverUtils;
import org.junit.Test;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_withValidInterval_shouldReturnZero() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5; // Example equation: f(x) = x - 5
            }
        };
        double x0 = 4.0; // Lower bound
        double x1 = 6.0; // Upper bound

        // Act
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);

        // Assert
        assertEquals(5.0, result, 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_withInvalidInterval_shouldThrowConvergenceException() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5; // Example equation: f(x) = x - 5
            }
        };
        double x0 = 6.0; // Lower bound greater than upper bound
        double x1 = 4.0; // Upper bound smaller than lower bound

        // Act
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_withNullFunction_shouldThrowFunctionEvaluationException() {
        // Arrange
        UnivariateRealFunction f = null;
        double x0 = 4.0; // Lower bound
        double x1 = 6.0; // Upper bound

        // Act
        UnivariateRealSolverUtils.solve(f, x0, x1);
    }

    @Test
    public void testSolve_withValidAccuracy_shouldReturnZero() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5; // Example equation: f(x) = x - 5
            }
        };
        double x0 = 4.9; // Lower bound
        double x1 = 5.1; // Upper bound
        double absoluteAccuracy = 0.1;

        // Act
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);

        // Assert
        assertEquals(5.0, result, 0.1);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_withInvalidAccuracy_shouldThrowConvergenceException() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5; // Example equation: f(x) = x - 5
            }
        };
        double x0 = 4.9; // Lower bound
        double x1 = 5.1; // Upper bound
        double absoluteAccuracy = -0.1; // Invalid absolute accuracy

        // Act
        UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);
    }

    @Test
    public void testBracket_withValidValues_shouldReturnBrackets() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5; // Example equation: f(x) = x - 5
            }
        };
        double initial = 4.5; // Initial midpoint
        double lowerBound = 4.0; // Lower bound
        double upperBound = 6.0; // Upper bound

        // Act
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);

        // Assert
        assertEquals(initial - 1, result[0], 0.00001);
        assertEquals(initial + 1, result[1], 0.00001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracket_withNullFunction_shouldThrowFunctionEvaluationException() {
        // Arrange
        UnivariateRealFunction f = null;
        double initial = 4.5; // Initial midpoint
        double lowerBound = 4.0; // Lower bound
        double upperBound = 6.0; // Upper bound

        // Act
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test(expected = ConvergenceException.class)
    public void testBracket_withInvalidBounds_shouldThrowConvergenceException() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x - 5; // Example equation: f(x) = x - 5
            }
        };
        double initial = 4.5; // Initial midpoint
        double lowerBound = 6.0; // Lower bound greater than upper bound
        double upperBound = 4.0; // Upper bound smaller than lower bound

        // Act
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }

    @Test
    public void testMidpoint_withPositiveValues_shouldReturnMidpoint() {
        // Arrange
        double a = 4.0;
        double b = 6.0;

        // Act
        double result = UnivariateRealSolverUtils.midpoint(a, b);

        // Assert
        assertEquals(5.0, result, 0.00001);
    }

    @Test
    public void testMidpoint_withNegativeValues_shouldReturnMidpoint() {
        // Arrange
        double a = -4.0;
        double b = -6.0;

        // Act
        double result = UnivariateRealSolverUtils.midpoint(a, b);

        // Assert
        assertEquals(-5.0, result, 0.00001);
    }

    @Test
    public void testMidpoint_withPositiveAndNegativeValues_shouldReturnMidpoint() {
        // Arrange
        double a = -4.0;
        double b = 6.0;

        // Act
        double result = UnivariateRealSolverUtils.midpoint(a, b);

        // Assert
        assertEquals(1.0, result, 0.00001);
    }

}