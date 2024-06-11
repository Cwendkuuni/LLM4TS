import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testGetFunctionValue() {
        // Initialize
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 1, null);

        // Test
        try {
            double value = optimizer.getFunctionValue(f);

            // Assertion
            assertEquals(0.0, value, 0.0);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testOptimize() {
        // Initialize
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                // Function x^2 - 4x + 4 has minimum value of 0 at x = 2
                return x * x - 4 * x + 4;
            }
        };
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(null, 5, null);

        // Test
        try {
            double result = optimizer.optimize(f, GoalType.MINIMIZE, 0, 10);

            // Assertion
            assertEquals(2.0, result, 0.01);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
}