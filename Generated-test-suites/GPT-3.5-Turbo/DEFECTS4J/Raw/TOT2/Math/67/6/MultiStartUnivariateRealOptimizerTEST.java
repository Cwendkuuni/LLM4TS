import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.UnivariateRealOptimizer;
import org.apache.commons.math.random.JDKRandomGenerator;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testGetFunctionValue() {
        // Setup
        UnivariateRealFunction function = x -> x * x;
        UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new DummyOptimizer(), 1, new JDKRandomGenerator());

        // Test
        double expected = 25.0;
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);

        // Verify
        assertEquals(expected, optimizer.getFunctionValue(), 0.001);
    }

    @Test
    public void testGetOptima() {
        // Setup
        UnivariateRealFunction function = x -> x * x;
        UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new DummyOptimizer(), 2, new JDKRandomGenerator());

        // Test
        double[] expected = {25.0, 25.0};
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0);
        double[] optima = optimizer.getOptima();

        // Verify
        assertArrayEquals(expected, optima, 0.001);
    }

    @Test
    public void testOptimizeWithConvergenceException() {
        // Setup
        UnivariateRealFunction function = x -> {throw new FunctionEvaluationException(x);};
        UnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(new DummyOptimizer(), 1, new JDKRandomGenerator());

        // Test & Verify
        assertThrows(ConvergenceException.class, () -> optimizer.optimize(function, GoalType.MINIMIZE, 0.0, 10.0));
    }

    // DummyOptimizer class for testing
    private class DummyOptimizer implements UnivariateRealOptimizer {
        public double getResult() {
            return 0.0;
        }
    }

}