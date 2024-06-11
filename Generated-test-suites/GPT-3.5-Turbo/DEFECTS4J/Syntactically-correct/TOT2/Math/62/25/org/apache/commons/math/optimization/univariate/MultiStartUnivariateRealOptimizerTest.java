import static org.junit.Assert.*;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() {
        // Test optimize method with typical input values
        // Create a dummy UnivariateRealFunction for testing
        UnivariateRealFunction dummyFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x - 4 * x + 4;
            }
        };
        
        // Create a dummy single-start optimizer for testing
        BaseUnivariateRealOptimizer<FUNC> dummyOptimizer = new BaseUnivariateRealOptimizer<FUNC>() {
            @Override
            public UnivariateRealPointValuePair optimize(FUNC f, GoalType goal, double min, double max) {
                // Dummy optimization logic, just for testing
                // Returns the minimum value point pair
                return new UnivariateRealPointValuePair(2.0, 0.0);
            }
        };
        
        // Create a MultiStartUnivariateRealOptimizer for testing
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(dummyOptimizer, 3, new RandomGenerator());

        // Test the optimize method
        UnivariateRealPointValuePair result = optimizer.optimize(dummyFunction, GoalType.MINIMIZE, 0.0, 10.0);
        
        assertNotNull(result);
        assertEquals(2.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    // Add more test methods for other public methods in the MultiStartUnivariateRealOptimizer class

}