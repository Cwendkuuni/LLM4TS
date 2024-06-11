import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class BisectionSolverTest {

    private BisectionSolver solver;

    @Before
    public void setUp() {
        solver = new BisectionSolver();
    }

    @Test
    public void testSolve_WithValidInput_ReturnsRoot() {
        // Arrange
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        };
        double min = 0;
        double max = 10;

        // Act
        double result;
        try {
            result = solver.solve(function, min, max);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception thrown");
            return;
        }

        // Assert
        assertEquals(5, result, 0.000001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolve_WithInvalidInput_ThrowsMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 1;
            }
        };
        double min = 0;
        double max = 10;

        // Act
        solver.solve(function, min, max);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testSolve_WithInvalidInput_ThrowsFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                throw new RuntimeException("Function evaluation error");
            }
        };
        double min = 0;
        double max = 10;

        // Act
        solver.solve(function, min, max);
    }

    // Add additional test cases for other methods

}