import org.junit.Test;
import static org.junit.Assert.*;

public class UnivariateRealSolverUtilsTest {

    @Test
    public void testSolve_withDefaultSolver_shouldReturnZeroOfFunction() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double x0 = 0.0;
        double x1 = 3.0;

        // Act
        double result = UnivariateRealSolverUtils.solve(f, x0, x1);

        // Assert
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolve_withCustomAccuracy_shouldReturnZeroOfFunctionWithGivenAccuracy() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double x0 = 0.0;
        double x1 = 3.0;
        double absoluteAccuracy = 0.001;

        // Act
        double result = UnivariateRealSolverUtils.solve(f, x0, x1, absoluteAccuracy);

        // Assert
        assertEquals(2.0, result, 0.001);
    }

    @Test
    public void testBracket_withValidParameters_shouldReturnBracketedRoot() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };
        double initial = 1.0;
        double lowerBound = -2.0;
        double upperBound = 3.0;

        // Act
        double[] result = UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);

        // Assert
        assertEquals(-1.0, result[0], 0.0001);
        assertEquals(2.0, result[1], 0.0001);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testBracket_withInvalidFunction_shouldThrowFunctionEvaluationException() {
        // Arrange
        UnivariateRealFunction f = null;
        double initial = 1.0;
        double lowerBound = -2.0;
        double upperBound = 3.0;

        // Act
        UnivariateRealSolverUtils.bracket(f, initial, lowerBound, upperBound);
    }
}