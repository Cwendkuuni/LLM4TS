import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() {
        // Create a mock UnivariateRealFunction
        UnivariateRealFunction mockFunction = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 4; // (x-2)^2
            }
        };

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(null, 3, null);
        optimizer.setMaxEvaluations(100);

        try {
            optimizer.optimize(mockFunction, GoalType.MINIMIZE, 0, 10);
            UnivariateRealPointValuePair[] optima = optimizer.getOptima();

            assertNotNull(optima);
            assertEquals(3, optima.length);

            // Check the optima values
            assertEquals(2.0, optima[0].getPoint(), 1e-10);
            assertEquals(0.0, optima[0].getValue(), 1e-10);

            assertNull(optima[1]); // Should be null since only one start point was used
            assertNull(optima[2]); // Should be null since only one start point was used

        } catch (FunctionEvaluationException e) {
            fail("Exception thrown in optimize method");
        }

    }
}