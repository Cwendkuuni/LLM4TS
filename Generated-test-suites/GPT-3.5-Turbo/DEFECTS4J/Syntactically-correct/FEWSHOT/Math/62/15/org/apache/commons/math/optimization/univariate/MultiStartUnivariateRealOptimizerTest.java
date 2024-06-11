package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.convergence.SimpleScalarValueChecker;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    private int starts;
    private RandomGenerator generator;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;
    private UnivariateRealFunction function;
    private GoalType goal;
    private double min;
    private double max;
    private double startValue;

    @Before
    public void setUp() {
        optimizer = mock(BaseUnivariateRealOptimizer.class);
        starts = 3;
        generator = mock(RandomGenerator.class);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, generator);
        function = mock(UnivariateRealFunction.class);
        goal = GoalType.MINIMIZE;
        min = 0.0;
        max = 1.0;
        startValue = 0.5;
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new SimpleScalarValueChecker();
        multiStartOptimizer.setConvergenceChecker(checker);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = multiStartOptimizer.getConvergenceChecker();
        assertNull(checker);
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = multiStartOptimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        int evaluations = multiStartOptimizer.getEvaluations();
        assertEquals(0, evaluations);
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
    }

    @Test
    public void testGetOptima() {
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertNull(optima);
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaThrowsMathIllegalStateException() {
        multiStartOptimizer.getOptima();
    }

    @Test
    public void testOptimize() {
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, goal, min, max);
        assertNull(result);
    }

    @Test
    public void testOptimizeWithStartValue() {
        UnivariateRealPointValuePair result = multiStartOptimizer.optimize(function, goal, min, max, startValue);
        assertNull(result);
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeThrowsConvergenceException() {
        multiStartOptimizer.optimize(function, goal, min, max);
    }

    @Test
    public void testSortPairs() {
        UnivariateRealPointValuePair[] pairs = new UnivariateRealPointValuePair[3];
        pairs[0] = null;
        pairs[1] = new UnivariateRealPointValuePair(0.1, 0.2);
        pairs[2] = new UnivariateRealPointValuePair(0.3, 0.4);
        multiStartOptimizer.sortPairs(goal);
    }
}