package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.AveragePointInitializer;
import org.apache.commons.math3.optim.initializer.ValueInitializer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.RandomValuesGenerator;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {

    private MultiStartUnivariateRealOptimizer<FUNC> optimizer;
    private UnivariateRealFunction function;
    private ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker;
    private RandomValuesGenerator random;

    @Before
    public void setUp() {
        function = new PolynomialFunction(2, -1, 1);
        optimizationSettings();
        optimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, random);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test
    public void testMultiStartOptimize() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealPointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertFalse(Double.isNaN(result.getValue()));
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test
    public void testMultiStartOptimizeMaximization() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealPointValuePair result = optimizer.optimize(function, GoalType.MAXIMIZE, -10.0, 10.0);
        assertNotNull(result);
        assertNotNull(result.getPoint());
        assertFalse(Double.isNaN(result.getValue()));
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test
    public void testMultiStartOptimizeNoSolution() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction functionWithNoSolution = new PolynomialFunction(3, -1, 1);
        UnivariateRealPointValuePair result = optimizer.optimize(functionWithNoSolution, GoalType.MINIMIZE, -10.0, 10.0);
        assertNull(result);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test
    public void testMultiStartGetOptima() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertNotNull(optima);
        assertArrayEquals(new UnivariateRealPointValuePair[]{null, null, null}, optima);

        optimizer = new MultiStartUnivariateRealRealOptimizer<>(new FibonacciFunction(), 3, random);
        optima = optimizer.getOptima();
        assertNotNull(optima);
        assertEquals(3, optima.length);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getEvaluations()}.
     */
    @Test
    public void testMultiStartGetEvaluations() {
        int evaluationsBefore = optimizer.getEvaluations();
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
        int evaluationsAfter = optimizer.getEvaluations();
        assertEquals(3 * optimizer.getMaxEvaluations(), evaluationsBefore + evaluationsAfter);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testMultiStartGetMaxEvaluations() {
        int maxEvaluations = optimizer.getMaxEvaluations();
        optimizer.setMaxEvaluations(5);
        int newMaxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(5, newMaxEvaluations);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setConvergenceChecker(ConvergenceChecker)}.
     */
    @Test
    public void testMultiStartSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        optimizer.setConvergenceChecker(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testMultiStartGetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        optimizer.setConvergenceChecker(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test method for constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiStartUnivariateRealOptimizerIllegalArgumentException() {
        new MultiStartUnivariateRealOptimizer<>(null, 3, random);
    }

    /**
     * Test method for constructor.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiStartUnivariateRealOptimizerIllegalArgumentException2() {
        new MultiStartUnivariateRealOptimizer<>(optimizer, -1, random);
    }

    /**
     * Test method for constructor.
     */
    @Test(expected = NullPointerException.class)
    public void testMultiStartUnivariateRealOptimizerNullPointerException() {
        new MultiStartUnivariateRealOptimizer<>(optimizer, 3, null);
    }

    /**
     * Test method for constructor.
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerConstruction() {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(function, 3, random);
        assertSame(function, optimizer.getOptimizer());
        assertEquals(3, optimizer.getStarts());
        assertNotNull(optimizer.getGenerator());
        optimizer.setMaxEvaluations(5);
        assertEquals(5, optimizer.getMaxEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiStartOptimizeIllegalArgumentException() {
        optimizer.optimize(function, null, -10.0, 10.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiStartOptimizeIllegalArgumentException2() {
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, -10.0, 10.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMultiStartOptimizeIllegalArgumentException3() {
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0, -10.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testMultiStartOptimizeFunctionEvaluationException() throws FunctionEvaluationException {
        when(function.value(anyDouble())).thenThrow(new FunctionEvaluationException("Test FunctionEvaluationException"));
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test(expected = ConvergenceException.class)
    public void testMultiStartOptimizeConvergenceException() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealPointValuePair result = mock(UnivariateRealPointValuePair.class);
        when(optimizer.optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                .thenReturn(result);
        when(result.isConverged()).thenReturn(false);
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test(expected = ConvergenceException.class)
    public void testMultiStartOptimizeConvergenceException2() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealPointValuePair result = mock(UnivariateRealPointValuePair.class);
        when(optimizer.optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                .thenReturn(result);
        when(result.isConverged()).thenReturn(true);
        when(optimizer.optimizer.getMaxEvaluations()).thenReturn(1);
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test(expected = ConvergenceException.class)
    public void testMultiStartOptimizeConvergenceException3() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealPointValuePair result1 = mock(UnivariateRealPointValuePair.class);
        UnivariateRealPointValuePair result2 = mock(UnivariateRealPointValuePair.class);
        UnivariateRealPointValuePair result3 = mock(UnivariateRealPointValuePair.class);
        when(optimizer.optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                .thenReturn(result1);
        when(optimizer.optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                .thenReturn(result2);
        when(optimizer.optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                .thenThrow(new ConvergenceException("Test ConvergenceException"));
        when(result1.isConverged()).thenReturn(false);
        when(result2.isConverged()).thenReturn(false);
        optimizer.optimize(function, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test method for constructor.
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerConstruction2() {
        UnivariateRealFunction function = mock(UnivariateRealFunction.class);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mock(BaseUnivariateRealOptimizer.class);
        when(optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                .thenReturn(new UnivariateRealPointValuePair(1.0, 1.0));
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer2 = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, random);
        assertEquals(optimizer, optimizer2.getOptimizer());
        assertEquals(3, optimizer2.getStarts());
        assertNotNull(optimizer2.getGenerator());
        optimizer2.setMaxEvaluations(5);
        assertEquals(5, optimizer2.getMaxEvaluations());
    }

    private void optimizationSettings() {
        optimizer = new FibonacciOptimizer();
        convergenceChecker = mock(ConvergenceChecker.class);
        random = mock(RandomValuesGenerator.class);
        when(random.nextDouble(anyDouble(), anyDouble())).thenReturn(0.5);
        when(convergenceChecker.isConverged(any(UnivariateRealPointValuePair.class)))
                .thenReturn(false);
        when(convergenceChecker.isConverged(any(UnivariateRealPointValuePair.class))).thenReturn(false);
        when(convergenceChecker.isConverged(any(UnivariateRealPointValuePair.class))).thenReturn(false);
        when(optimizer.getMaxEvaluations()).thenReturn(5);
        optimizer.setMaxEvaluations(5);
        optimizer.setConvergenceChecker(convergenceChecker);
    }
}