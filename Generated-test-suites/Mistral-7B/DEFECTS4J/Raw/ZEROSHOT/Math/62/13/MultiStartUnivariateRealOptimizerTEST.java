package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeCriterion;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private static final double EPSILON = 1e-7;
    private static final double MIN = -2;
    private static final double MAX = 2;
    private static final double START_MIN = -1;
    private static final double START_MAX = 1;

    private UnivariateOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer<PolynomialFunction> multiStartOptimizer;

    @Before
    public void setUp() {
        optimizer = new FibonacciOptimizer();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new RandomDataGenerator());
    }

    @Test
    public void testOptimizeMin() throws Exception {
        final PolynomialFunction f = new PolynomialFunction(new double[]{-1, 1, -1, 6, 2}, 5);
        GoalType goal = GoalType.MINIMIZE;
        PointValuePair result = multiStartOptimizer.optimize(f, goal, MIN, MAX);
        assertNotNull(result);
        assertEquals(GoalType.MINIMIZE, result.getType());
        assertEquals(MIN, result.getPoint(), EPSILON);
        assertEquals(-3.3348979462733843, result.getValue(), EPSILON);
        assertArrayEquals(multiStartOptimizer.getOptima(), new PointValuePair[]{result, null, null, null, null, null, null, null, null, null});
    }

    @Test
    public void testOptimizeMax() throws Exception {
        final PolynomialFunction f = new PolynomialFunction(new double[]{1, -1, 1, -6, 2}, 5);
        GoalType goal = GoalType.MAXIMIZE;
        PointValuePair result = multiStartOptimizer.optimize(f, goal, MIN, MAX);
        assertNotNull(result);
        assertEquals(GoalType.MAXIMIZE, result.getType());
        assertEquals(MAX, result.getPoint(), EPSILON);
        assertEquals(3.3348979462733843, result.getValue(), EPSILON);
        assertArrayEquals(multiStartOptimizer.getOptima(), new PointValuePair[]{null, null, null, null, null, null, null, null, new PointValuePair(MAX, 3.3348979462733843), null});
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() throws Exception {
        final PolynomialFunction f = new PolynomialFunction(new double[]{-1, 1, -1, 6, 2}, 5);
        GoalType goal = GoalType.MINIMIZE;
        multiStartOptimizer.optimize(f, goal, MIN, MAX);
    }

    private static void assertArrayEquals(Object expected, Object actual) {
        if (expected == null) {
            assertNull(actual);
        } else if (expected.getClass().isArray()) {
            assertArrayEquals((Object[])expected, (Object[])actual);
        } else {
            assertEquals(expected, actual);
        }
    }

    private static void assertArrayEquals(Object[] expected, Object[] actual) {
        if (expected.length != actual.length) {
            fail("Arrays have different lengths.");
        }
        for (int i = 0; i < expected.length; ++i) {
            assertEquals(expected[i], actual[i]);
        }
    }
}