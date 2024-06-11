import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.random.JDKRandomGenerator;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testOptimize() throws Exception {
        RandomGenerator generator = new JDKRandomGenerator();
        BaseUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer<>(null, 5, generator);

        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x; // Simple function for testing
            }
        };

        try {
            UnivariateRealPointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
            assertNotNull(result);
            assertEquals(0.0, result.getPoint(), 1e-10);
            assertEquals(0.0, result.getValue(), 1e-10);
        } catch (MathIllegalStateException e) {
            fail("Unexpected MathIllegalStateException");
        } catch (ConvergenceException e) {
            fail("Unexpected ConvergenceException");
        }
    }

    @Test
    public void testGetOptima() {
        RandomGenerator generator = new JDKRandomGenerator();
        BaseUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer<>(null, 5, generator);

        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x; // Simple function for testing
            }
        };

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertNotNull(optima);
        assertEquals(5, optima.length);
    }

}