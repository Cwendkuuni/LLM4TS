import org.junit.jupiter.api.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import static org.junit.jupiter.api.Assertions.*;

public class BisectionSolverTest {

    private BisectionSolver solver;

    @Test
    void testSolve_WithContinuousFunction_ReturnsZero() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 1;
            }
        };
        solver = new BisectionSolver(f);

        // Act
        double result = solver.solve(0, 2);

        // Assert
        assertEquals(1, result);
    }

    @Test
    void testSolve_WithNonContinuousFunction_ThrowsException() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                if (x == 1) {
                    throw new FunctionEvaluationException("Function evaluation exception");
                }
                return x - 1;
            }
        };
        solver = new BisectionSolver(f);

        // Act & Assert
        assertThrows(FunctionEvaluationException.class, () -> {
            solver.solve(0, 2);
        });
    }

    @Test
    void testSolve_WithMaxIterationsExceeded_ThrowsException() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 1;
            }
        };
        solver = new BisectionSolver(f);

        // Act & Assert
        assertThrows(MaxIterationsExceededException.class, () -> {
            solver.solve(-10, 10);
        });
    }

    // Add more test cases for other methods

}