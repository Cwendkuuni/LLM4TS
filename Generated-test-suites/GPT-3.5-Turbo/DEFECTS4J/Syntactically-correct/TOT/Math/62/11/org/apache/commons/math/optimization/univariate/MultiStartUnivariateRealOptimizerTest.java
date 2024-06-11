import org.apache.commons.math.analysis.UnivariateFunction;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.util.FastMath;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.optimization.univariate.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private class TestUnivariateFunction implements UnivariateFunction {
        public double value(double x) {
            return x*x;
        }
    }

    private TestUnivariateFunction function;
    private MultiStartUnivariateRealOptimizer<TestUnivariateFunction> optimizer;

    @Before
    public void setUp() {
        function = new TestUnivariateFunction();
        optimizer = new MultiStartUnivariateRealOptimizer<TestUnivariateFunction>(
                new BrentOptimizer(), 10, new RandomGenerator(){
            public double nextDouble() {
                return 0.5;
            }
        });
    }

    @Test
    public void testOptimize() throws Exception {
        UnivariateRealPointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10, 10);
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 1e-5);
        assertEquals(0.0, result.getValue(), 1e-5);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertNotNull(optima);
        assertEquals(10, optima.length);
        for (int i = 0; i < 10; i++) {
            assertNotNull(optima[i]);
            assertEquals(0.0, optima[i].getPoint(), 1e-5);
            assertEquals(0.0, optima[i].getValue(), 1e-5);
        }
    }

    @Test
    public void testOptimizeUnbounded() throws Exception {
        UnivariateRealPointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
        assertNotNull(result);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertNotNull(optima);
        assertEquals(10, optima.length);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaWithoutOptimize() throws Exception {
        optimizer.getOptima();
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() throws Exception {
        TestUnivariateFunction unstableFunction = new TestUnivariateFunction() {
            public double value(double x) {
                return Math.random();
            }
        };

        optimizer.optimize(unstableFunction, GoalType.MINIMIZE, -10, 10);
    }

    @Test
    public void testEquals() throws Exception {
        MultiStartUnivariateRealOptimizer<TestUnivariateFunction> other = null;
        assertTrue(optimizer.equals(optimizer));
        assertFalse(optimizer.equals(null));
        assertFalse(optimizer.equals(new Object()));
        assertFalse(optimizer.equals(other));

        other = new MultiStartUnivariateRealOptimizer<TestUnivariateFunction>(
                new BrentOptimizer(), 10, new RandomGenerator(){
            public double nextDouble() {
                return 0.5;
            }
        });
        assertTrue(optimizer.equals(other));
        assertTrue(other.equals(optimizer));

        other.setMaxEvaluations(100);
        assertFalse(optimizer.equals(other));
        assertFalse(other.equals(optimizer));
    }

    @Test
    public void testHashCode() throws Exception {
        MultiStartUnivariateRealOptimizer<TestUnivariateFunction> other = new MultiStartUnivariateRealOptimizer<TestUnivariateFunction>(
                new BrentOptimizer(), 10, new RandomGenerator(){
            public double nextDouble() {
                return 0.5;
            }
        });
        assertTrue(other.hashCode() == optimizer.hashCode());

        other.setMaxEvaluations(100);
        assertFalse(other.hashCode() == optimizer.hashCode());
    }

}