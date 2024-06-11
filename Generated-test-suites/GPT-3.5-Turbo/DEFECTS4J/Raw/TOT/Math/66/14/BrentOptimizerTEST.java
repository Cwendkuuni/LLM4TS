import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }

    @Test
    public void testOptimize_WithStartValue_ReturnsOptimum() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x - 2, 2) - 1;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 3.0;
        double startValue = 2.5;

        // Act
        double result = 0;
        try {
            result = optimizer.optimize(f, goalType, min, max, startValue);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }

        // Assert
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testOptimize_WithoutStartValue_ReturnsOptimum() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x - 3, 2) - 2;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 2.0;
        double max = 4.0;

        // Act
        double result = 0;
        try {
            result = optimizer.optimize(f, goalType, min, max);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            fail("Exception should not be thrown");
        }

        // Assert
        assertEquals(3.0, result, 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_WithNegativeRelativeAccuracy_ThrowsException() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 3.0;

        // Act
        optimizer.setRelativeAccuracy(-1.0);
        optimizer.optimize(f, goalType, min, max);

        // Assert (Exception expected)
        fail("NotStrictlyPositiveException should be thrown");
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimize_WithNegativeAbsoluteAccuracy_ThrowsException() throws MaxIterationsExceededException, FunctionEvaluationException {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 1.0;
        double max = 3.0;

        // Act
        optimizer.setAbsoluteAccuracy(-1.0);
        optimizer.optimize(f, goalType, min, max);

        // Assert (Exception expected)
        fail("NotStrictlyPositiveException should be thrown");
    }

    @Test
    public void testOptimize_MaxIterationsExceeded_ThrowsException() {
        // Arrange
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x - 1, 2) - 4;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 5.0;
        optimizer.setMaximalIterationCount(100);

        // Act
        try {
            optimizer.optimize(f, goalType, min, max);
            fail("MaxIterationsExceededException should be thrown");
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            // Assert (Exception expected)
            assertTrue(e instanceof MaxIterationsExceededException);
        }
    }

}