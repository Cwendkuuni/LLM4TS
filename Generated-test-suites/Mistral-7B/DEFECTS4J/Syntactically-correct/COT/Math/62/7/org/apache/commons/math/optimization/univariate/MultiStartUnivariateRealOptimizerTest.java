package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.BaseUnivariateRealOptimizer;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.UnivariateRealPointValuePair;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 *
 * @param <FUNC> Type of the objective function to be optimized.
 */
public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {

    private BaseUnivariateRealOptimizer<FUNC> optimizer;
    private MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer;
    private RandomGenerator generator;
    private UnivariateRealFunction testFunction;

    @Before
    public void setup() {
        optimizer = mock(BaseUnivariateRealOptimizer.class);
        generator = mock(RandomGenerator.class);
        testFunction = mock(UnivariateRealFunction.class);
        when(optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0)).thenReturn(new UnivariateRealPointValuePair(0.5, 1.0));
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, generator);
    }

    /**
     * Test method getConvergenceChecker().
     */
    @Test
    public void testGetConvergenceChecker() {
        assertSame(optimizer.getConvergenceChecker(), multiStartOptimizer.getConvergenceChecker());
    }

    /**
     * Test method getEvaluations().
     */
    @Test
    public void testGetEvaluations() {
        assertEquals(0, multiStartOptimizer.getEvaluations());
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(3, multiStartOptimizer.getEvaluations());
    }

    /**
     * Test method getMaxEvaluations().
     */
    @Test
    public void testGetMaxEvaluations() {
        assertEquals(0, multiStartOptimizer.getMaxEvaluations());
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0, 10);
        assertEquals(10, multiStartOptimizer.getMaxEvaluations());
    }

    /**
     * Test method optimize().
     * Positive test case.
     */
    @Test
    public void testOptimize() throws FunctionEvaluationException {
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
        UnivariateRealPointValuePair optimum = multiStartOptimizer.getOptima()[0];
        assertNotNull(optimum);
        assertEquals(0.5, optimum.getPoint(), 0.001);
        assertEquals(1.0, optimum.getValue(), 0.001);

        // Test that all evaluate calls have been made
        when(optimizer.getEvaluations()).thenReturn(3);
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0, 10);
        assertEquals(10, multiStartOptimizer.getEvaluations());
    }

    /**
     * Test method optimize().
     * Negative test case - FunctionEvaluationException.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionEvaluationException() throws FunctionEvaluationException {
        when(optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0)).thenThrow(new FunctionEvaluationException());
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
    }

    /**
     * Test method optimize().
     * Negative test case - ConvergenceException.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeConvergenceException() throws FunctionEvaluationException {
        when(optimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0)).thenThrow(new ConvergenceException());
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
    }

    /**
     * Test method optimize().
     * Negative test case - MathIllegalStateException.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testOptimizeMathIllegalStateException() throws FunctionEvaluationException {
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
        when(optimizer.getOptima()).thenReturn(new UnivariateRealPointValuePair[0]);
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
    }

    /**
     * Test method optimize().
     * Edge case - maxEvaluations is less than the number of starts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeMaxEvaluationsLessThanStarts() {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, generator);
        multiStartOptimizer.setMaxEvaluations(2);
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
    }

    /**
     * Test method optimize().
     * Edge case - maxEvaluations is equal to the number of starts.
     */
    @Test
    public void testOptimizeMaxEvaluationsEqualsStarts() throws FunctionEvaluationException {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, generator);
        multiStartOptimizer.setMaxEvaluations(3);
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(3, optima.length);
        assertArrayEquals(new Object[] { new UnivariateRealPointValuePair(0.5, 1.0),
                                          new UnivariateRealPointValuePair(0.5, 1.0),
                                          new UnivariateRealPointValuePair(0.5, 1.0) },
                         optima, 0.001);
    }

    /**
     * Test method optimize().
     * Edge case - maxEvaluations is greater than the number of starts.
     */
    @Test
    public void testOptimizeMaxEvaluationsGreaterThanStarts() throws FunctionEvaluationException {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, generator);
        multiStartOptimizer.setMaxEvaluations(4);
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(3, optima.length);
        assertArrayEquals(new Object[] { new UnivariateRealPointValuePair(0.5, 1.0),
                                          new UnivariateRealPointValuePair(0.5, 1.0),
                                          new UnivariateRealPointValuePair(0.5, 1.0) },
                         optima, 0.001);
    }

    /**
     * Test method optimize().
     * Edge case - number of starts is less than or equal to 1.
     */
    @Test
    public void testOptimizeStartsLessThanOrEqualToOne() {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 1, generator);
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
        assertArrayEquals(new Object[] {}, multiStartOptimizer.getOptima());
    }

    /**
     * Test method optimize().
     * Edge case - goal type is null.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeGoalTypeNull() {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, generator);
        multiStartOptimizer.optimize(testFunction, null, 0.0, 1.0);
    }

    /**
     * Test method optimize().
     * Edge case - min and max are the same.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeMinMaxSame() {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, generator);
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 0.0);
    }

    /**
     * Test method optimize().
     * Edge case - startValue is less than min.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeStartValueLessThanMin() {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, generator);
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, -1.0, 0.0);
    }

    /**
     * Test method optimize().
     * Edge case - startValue is greater than max.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeStartValueGreaterThanMax() {
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, generator);
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0, 2.0);
    }
}