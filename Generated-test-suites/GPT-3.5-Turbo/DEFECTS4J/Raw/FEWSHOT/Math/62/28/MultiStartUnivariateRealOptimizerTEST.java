//Test for the MultiStartUnivariateRealOptimizer class

package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;

    @Before
    public void setUp() {
        optimizer = new MultiStartUnivariateRealOptimizer<>(new UnivariateRealOptimizer(), 5, new RandomGenerator());
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checker = new ConvergenceChecker<>();
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(0, optimizer.getMaxEvaluations());
        optimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
        optimizer.optimize(new UnivariateRealFunction(), GoalType.MINIMIZE, 0, 1);
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaNoOptimumComputed() {
        optimizer.getOptima();
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergenceWithAnyStartPoint() throws FunctionEvaluationException {
        optimizer.optimize(new UnivariateRealFunction(), GoalType.MINIMIZE, 0, 1);
    }
}