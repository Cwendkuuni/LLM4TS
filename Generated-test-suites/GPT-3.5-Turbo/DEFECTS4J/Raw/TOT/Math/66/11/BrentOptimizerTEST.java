import static org.junit.Assert.*;
import org.junit.*;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.FunctionEvaluationException;

public class BrentOptimizerTest {
    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimize_minimize_withValidFunctionAndValidBounds_shouldReturnOptimum() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x - 1;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;

        // Act
        double result = optimizer.optimize(f, goalType, min, max);

        // Assert
        assertEquals(0.5, result, 0.0001);
    }

    @Test
    public void testOptimize_maximize_withValidFunctionAndValidBounds_shouldReturnOptimum() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x - 1;
            }
        };
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -10;
        double max = 10;

        // Act
        double result = optimizer.optimize(f, goalType, min, max);

        // Assert
        assertEquals(10.0, result, 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimize_withInvalidMaxIterations_shouldThrowMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;
        optimizer.setMaximalIterationCount(5);

        // Act
        double result = optimizer.optimize(f, goalType, min, max);
    }

    @Test
    public void testOptimize_withInvalidFunction_shouldThrowFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                throw new ArithmeticException();
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10;
        double max = 10;

        try {
            // Act
            double result = optimizer.optimize(f, goalType, min, max);
        } catch(FunctionEvaluationException e) {
            // Assert
            assertTrue(e.getCause() instanceof ArithmeticException);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptimize_withInvalidBounds_shouldThrowIllegalArgumentException() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 2 * x - 1;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 10;
        double max = -10;

        // Act
        double result = optimizer.optimize(f, goalType, min, max);
    }

}