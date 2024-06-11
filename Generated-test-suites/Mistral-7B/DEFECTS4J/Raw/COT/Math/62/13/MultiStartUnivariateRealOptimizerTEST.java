package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvalExceededException;
import org.apache.commons.math3.optim.BaseOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.SimpleGoalType;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.UniformRealDistribution;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;

/**
 * Test cases for MultiStartUnivariateRealOptimizer class.
 *
 * @author John Collins
 */
public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {

    private static final int NUM_STARTS = 5;
    private static final int MAX_EVALUATIONS = 10;
    private static final double MIN = -10.0;
    private static final double MAX = 10.0;

    private BaseOptimizer<FUNC> optimizer;
    private MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer;
    private UnivariateRealFunction function;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer<>();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapper =
                new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS,
                        new RandomDataGenerator());
        multiStartOptimizer = (MultiStartUnivariateRealOptimizer<FUNC>) optimizerWrapper;
        function = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0, -1.0}, 4);
        multiStartOptimizer.setMaxEvaluations(MAX_EVALUATIONS);
    }

    /**
     * Test functionality of optimize method with minimum goal type.
     *
     * @throws Exception if optimize fails
     */
    @Test
    public void testMinimize() throws Exception {
        UnivariateOptimizer<UnivariateRealFunction> optimizer = multiStartOptimizer;
        GoalType goalType = SimpleGoalType.MINIMIZE;
        double expectedMinimum = -3.0;

        PointValuePair result = optimizer.optimize(function, goalType, MIN, MAX);
        assertEquals(expectedMinimum, result.getPoint().getX(), 0.001);
        assertEquals(expectedMinimum, result.getValue(), 0.001);

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(NUM_STARTS, optima.length);
        assertEquals(expectedMinimum, optima[0].getValue(), 0.001);
    }

    /**
     * Test functionality of optimize method with maximum goal type.
     *
     * @throws Exception if optimize fails
     */
    @Test
    public void testMaximize() throws Exception {
        UnivariateOptimizer<UnivariateRealFunction> optimizer = multiStartOptimizer;
        GoalType goalType = SimpleGoalType.MAXIMIZE;
        double expectedMaximum = 3.0;

        PointValuePair result = optimizer.optimize(function, goalType, MIN, MAX);
        assertEquals(expectedMaximum, result.getPoint().getX(), 0.001);
        assertEquals(expectedMaximum, result.getValue(), 0.001);

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(NUM_STARTS, optima.length);
        assertEquals(expectedMaximum, optima[0].getValue(), 0.001);
    }

    /**
     * Test exception handling when optimization exceeds the maximum number of
     * evaluations.
     *
     * @throws Exception if optimize fails
     */
    @Test(expected = ConvergenceException.class)
    public void testMaxEvaluations() throws Exception {
        multiStartOptimizer.setMaxEvaluations(1);
        multiStartOptimizer.optimize(function, SimpleGoalType.MINIMIZE, MIN, MAX);
    }

    /**
     * Test exception handling when optimization fails to converge with any
     * starting point.
     */
    @Test
    public void testNoConvergence() throws Exception {
        optimizer = new FibonacciSearchOptimizer<>();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapper =
                new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS,
                        new RandomDataGenerator());
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                (MultiStartUnivariateRealOptimizer<UnivariateRealFunction>) optimizerWrapper;

        multiStartOptimizer.setMaxEvaluations(MAX_EVALUATIONS);
        UnivariateRealFunction function = (f) -> Erf.erf(f.value(0.0));

        try {
            multiStartOptimizer.optimize(function, SimpleGoalType.MINIMIZE, MIN, MAX);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }
    }

    /**
     * Test exception handling when optimization reaches its maximum number of
     * evaluations.
     */
    @Test
    public void testMaxEvaluationsException() throws Exception {
        optimizer = new FibonacciSearchOptimizer<>();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerWrapper =
                new MultiStartUnivariateRealOptimizer<>(optimizer, NUM_STARTS,
                        new RandomDataGenerator());
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer =
                (MultiStartUnivariateRealOptimizer<UnivariateRealFunction>) optimizerWrapper;

        multiStartOptimizer.setMaxEvaluations(1);
        UnivariateRealFunction function = (f) -> f.value(f.value(0.0));

        try {
            multiStartOptimizer.optimize(function, SimpleGoalType.MINIMIZE, MIN, MAX);
            fail("Expected MaxEvalExceededException");
        } catch (MaxEvalExceededException e) {
            // Expected
        }
    }
}