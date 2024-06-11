import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;

    @Before
    public void setUp() {
        // Initialize the optimizer with a single-start optimizer and a random generator
        // Number of starts is set to 3 in this test case
        optimizer = new MultiStartUnivariateRealOptimizer(new BaseUnivariateRealOptimizer(), 3, new RandomGenerator());
    }

    @Test
    public void testOptimize_withValidFunction_shouldReturnOptimalValue() {
        // Set up the test function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        // Set up the goal type as MINIMIZE
        GoalType goal = GoalType.MINIMIZE;

        // Set up the search range and the start value
        double min = -10;
        double max = 10;
        double startValue = 5;

        // Call the optimize method
        UnivariateRealPointValuePair result = optimizer.optimize(function, goal, min, max, startValue);

        // Check if the optimal value is correct
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testOptimize_withInvalidFunction_shouldThrowFunctionEvaluationException() {
        // Set up the test function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                throw new RuntimeException("Function evaluation failed");
            }
        };

        // Set up the goal type as MINIMIZE
        GoalType goal = GoalType.MINIMIZE;

        // Set up the search range and the start value
        double min = -10;
        double max = 10;
        double startValue = 5;

        // Call the optimize method and expect a FunctionEvaluationException to be thrown
        try {
            optimizer.optimize(function, goal, min, max, startValue);
            fail("Expected FunctionEvaluationException");
        } catch (FunctionEvaluationException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetOptima_withNoOptimumComputedYet_shouldThrowMathIllegalStateException() {
        // Call the getOptima method without calling optimize first and expect a MathIllegalStateException to be thrown
        try {
            optimizer.getOptima();
            fail("Expected MathIllegalStateException");
        } catch (MathIllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetOptima_withOptimumComputed_shouldReturnOptimaArray() {
        // Set up the test function
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        // Set up the goal type as MINIMIZE
        GoalType goal = GoalType.MINIMIZE;

        // Set up the search range and the start value
        double min = -10;
        double max = 10;
        double startValue = 5;

        // Call the optimize method
        optimizer.optimize(function, goal, min, max, startValue);

        // Call the getOptima method and check if the returned array is not null
        assertNotNull(optimizer.getOptima());
    }

}