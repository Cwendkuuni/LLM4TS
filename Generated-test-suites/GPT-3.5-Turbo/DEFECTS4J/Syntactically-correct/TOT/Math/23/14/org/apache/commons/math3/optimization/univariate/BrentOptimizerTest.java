import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-6, 1e-6);
    }

    @Test
    public void testDoOptimizeWithMinGoalType_WhenFunctionHasMinimum_ReturnsCorrectResult() {
        double result = optimizer.doOptimize(0, 5, 1);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testDoOptimizeWithMinGoalType_WhenFunctionHasMultipleMinimums_ReturnsFirstMinimum() {
        double result = optimizer.doOptimize(0, 5, 3);
        assertEquals(0.0, result, 1e-6);
    }

    @Test
    public void testDoOptimizeWithMinGoalType_WhenFunctionHasNoMinimum_ReturnsIllegalArgumentException() {
        try {
            optimizer.doOptimize(-5, 5, 10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testDoOptimizeWithMaxGoalType_WhenFunctionHasMaximum_ReturnsCorrectResult() {
        double result = optimizer.doOptimize(0, 5, 1);
        assertEquals(1.0, result, 1e-6);
    }

    @Test
    public void testDoOptimizeWithMaxGoalType_WhenFunctionHasMultipleMaximums_ReturnsFirstMaximum() {
        double result = optimizer.doOptimize(0, 5, 3);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testDoOptimizeWithMaxGoalType_WhenFunctionHasNoMaximum_ReturnsIllegalArgumentException() {
        try {
            optimizer.doOptimize(-5, 5, -10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testDoOptimizeWithInvalidRange_ReturnsIllegalArgumentException() {
        try {
            optimizer.doOptimize(5, 0, 1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Test passed
        }
    }

    @Test
    public void testDoOptimize_WithOptions_ReturnsCorrectResult() {
        optimizer.setAbsoluteThreshold(1e-4);
        optimizer.setRelativeThreshold(1e-4);
        optimizer.setConvergenceChecker(new MyConvergenceChecker());

        double result = optimizer.doOptimize(0, 5, 1);
        assertEquals(0.0, result, 1e-6);
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return iteration > 5;
        }
    }

}