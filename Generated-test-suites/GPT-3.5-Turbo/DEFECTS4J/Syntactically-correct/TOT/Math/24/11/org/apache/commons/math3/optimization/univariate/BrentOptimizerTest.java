import org.junit.Test;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testOptimize_whenObjectiveFunctionIsQuadratic_thenResultShouldBeMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double[] coefficients = {1, 0, 0}; // Quadratic function: x^2 
        UnivariatePointValuePair result = optimizer.optimize(
                1000, (a) -> coefficients[0] * Math.pow(a, 2) + coefficients[1] * a + coefficients[2], 
                BrentOptimizer.GoalType.MINIMIZE, 0, 10);
        
        assertEquals(result.getPoint(), 0, 1e-6);
        assertEquals(result.getValue(), 0, 1e-6);
    }

    @Test
    public void testOptimize_whenObjectiveFunctionIsLinear_thenResultShouldBeMinimum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double[] coefficients = {1, 2}; // Linear function: x + 2 
        UnivariatePointValuePair result = optimizer.optimize(
                1000, (a) -> coefficients[0] * a + coefficients[1],
                BrentOptimizer.GoalType.MINIMIZE, -10, 10);

        assertEquals(result.getPoint(), -2, 1e-6);
        assertEquals(result.getValue(), 0, 1e-6);
    }

    @Test
    public void testOptimize_whenObjectiveFunctionIsConstant_thenResultShouldBeTheSameForAllInputs() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double[] coefficients = {5}; // Constant function: 5 
        UnivariatePointValuePair result1 = optimizer.optimize(
                1000, (a) -> coefficients[0],
                BrentOptimizer.GoalType.MINIMIZE, -10, 10);
        UnivariatePointValuePair result2 = optimizer.optimize(
                1000, (a) -> coefficients[0],
                BrentOptimizer.GoalType.MINIMIZE, -10, 10);

        assertEquals(result1.getPoint(), result2.getPoint(), 1e-6);
        assertEquals(result1.getValue(), result2.getValue(), 1e-6);
    }

    @Test
    public void testOptimize_whenGoalTypeIsMaximize_thenResultShouldBeMaximum() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double[] coefficients = {-1, 0, 0}; // Quadratic function: -x^2
        UnivariatePointValuePair result = optimizer.optimize(
                1000, (a) -> coefficients[0] * Math.pow(a, 2) + coefficients[1] * a + coefficients[2],
                BrentOptimizer.GoalType.MAXIMIZE, -10, 10);

        assertEquals(result.getPoint(), 0, 1e-6);
        assertEquals(result.getValue(), 0, 1e-6);
    }

    @Test
    public void testOptimize_whenInvalidBounds_thenIllegalArgumentExceptionShouldBeThrown() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        double[] coefficients = {1, 0, 0}; // Quadratic function: x^2 
      
        try {
            optimizer.optimize(1000, (a) -> coefficients[0] * Math.pow(a, 2) + coefficients[1] * a + coefficients[2],
                    BrentOptimizer.GoalType.MINIMIZE, 10, -10);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Invalid bounds: lower bound (10) must be less than or equal to upper bound (-10)");
        }
    }
}