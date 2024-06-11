import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.*;
import org.apache.commons.math.random.JDKRandomGenerator;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;

    @Before
    public void setUp() {
        UnivariateRealOptimizer singleStartOptimizer = new NewtonSolver();
        int starts = 5;
        optimizer = new MultiStartUnivariateRealOptimizer(singleStartOptimizer, starts, new JDKRandomGenerator());
    }

    @Test
    public void testOptimize_minimumStarts() throws Exception {
        UnivariateRealFunction f = x -> x * x;
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);

        assertEquals(0.0, result, 1e-10);

        double[] optima = optimizer.getOptima();
        assertEquals(5, optima.length);
        for (double value : optima) {
            assertEquals(0.0, value, 1e-10);
        }

        double[] optimaValues = optimizer.getOptimaValues();
        assertEquals(5, optimaValues.length);
        for (double value : optimaValues) {
            assertEquals(0.0, value, 1e-10);
        }
    }

    @Test
    public void testOptimize_maximumStarts() throws Exception {
        UnivariateRealFunction f = x -> -x * x;
        double result = optimizer.optimize(f, GoalType.MAXIMIZE, -10, 10);

        assertEquals(0.0, result, 1e-10);

        double[] optima = optimizer.getOptima();
        assertEquals(5, optima.length);
        for (double value : optima) {
            assertEquals(0.0, value, 1e-10);
        }

        double[] optimaValues = optimizer.getOptimaValues();
        assertEquals(5, optimaValues.length);
        for (double value : optimaValues) {
            assertEquals(0.0, value, 1e-10);
        }
    }

    @Test(expected=OptimizationException.class)
    public void testOptimize_noConvergence() throws Exception {
        UnivariateRealFunction f = x -> 1 / (x - 1);
        optimizer.optimize(f, GoalType.MINIMIZE, 1, 2);
    }

    @Test
    public void testOptimize_errorScenario() throws Exception {
        UnivariateRealFunction f = x -> {
            throw new RuntimeException("Function evaluation exception");
        };

        double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);

        assertEquals(Double.NaN, result, 1e-10);

        double[] optima = optimizer.getOptima();
        assertEquals(5, optima.length);
        for (double value : optima) {
            assertEquals(Double.NaN, value, 1e-10);
        }

        double[] optimaValues = optimizer.getOptimaValues();
        assertEquals(5, optimaValues.length);
        for (double value : optimaValues) {
            assertEquals(Double.NaN, value, 1e-10);
        }
    }

}