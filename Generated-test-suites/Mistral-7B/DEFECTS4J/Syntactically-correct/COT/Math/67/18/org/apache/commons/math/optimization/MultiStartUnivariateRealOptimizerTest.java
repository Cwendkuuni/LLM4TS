package org.apache.commons.math.optimization.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.NonConvergentIterationException;
import org.apache.commons.math3.optim.OptimizationException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test file for MultiStartUnivariateRealOptimizer class
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction testFunction;
    private UnivariateRealOptimizer optimizerMock;
    private RandomGenerator randomGeneratorMock;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;

    @Before
    public void setUp() {
        testFunction = new PolynomialFunction(new double[] { 1.0, -3.0 }, 2);
        optimizerMock = mock(UnivariateRealOptimizer.class);
        randomGeneratorMock = mock(RandomGenerator.class);

        when(optimizerMock.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0)).thenReturn(new PointValuePair(0.5, 1.25));
        when(optimizerMock.getFunctionValue()).thenReturn(1.25);
        when(optimizerMock.getResult()).thenReturn(0.5);
        when(optimizerMock.getIterationCount()).thenReturn(100);
        when(optimizerMock.getEvaluations()).thenReturn(200);
        when(optimizerMock.getAbsoluteAccuracy()).thenReturn(1e-6);
        when(optimizerMock.getRelativeAccuracy()).thenReturn(1e-5);
        when(optimizerMock.isDiverging()).thenReturn(false);
        when(optimizerMock.isConverged()).thenReturn(false);
        when(optimizerMock.getOptima()).thenThrow(new IllegalStateException());
        when(optimizerMock.getOptimaValues()).thenThrow(new IllegalStateException());
        when(optimizerMock.setMaxIterations(Integer.MAX_VALUE)).thenReturn(this.optimizerMock);
        when(optimizerMock.setMaxEvaluations(Integer.MAX_VALUE)).thenReturn(this.optimizerMock);

        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizerMock, 3, randomGeneratorMock);
    }

    @Test
    public void testGetFunctionValue() {
        assertEquals(1.25, multiStartOptimizer.getFunctionValue(), 1e-6);
    }

    @Test
    public void testGetResult() {
        assertEquals(0.5, multiStartOptimizer.getResult(), 1e-6);
    }

    @Test
    public void testGetIterationCount() {
        assertEquals(0, multiStartOptimizer.getIterationCount());
    }

    @Test
    public void testGetMaxIterations() {
        assertEquals(Integer.MAX_VALUE, multiStartOptimizer.getMaxIterations());
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(Integer.MAX_VALUE, multiStartOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, multiStartOptimizer.getEvaluations());
    }

    @Test
    public void testGetAbsoluteAccuracy() {
        assertEquals(1e-6, multiStartOptimizer.getAbsoluteAccuracy(), 1e-9);
    }

    @Test
    public void testGetRelativeAccuracy() {
        assertEquals(1e-5, multiStartOptimizer.getRelativeAccuracy(), 1e-9);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptimaNotCalledOptimize() throws IllegalStateException {
        multiStartOptimizer.getOptima();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetOptimaValuesNotCalledOptimize() throws IllegalStateException {
        multiStartOptimizer.getOptimaValues();
    }

    @Test
    public void testOptima() throws ConvergenceException, FunctionEvaluationException, IllegalStateException {
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
        double[] optima = multiStartOptimizer.getOptima();
        double[] expectedOptima = new double[] { 0.5, 0.5, 0.5 };
        assertArrayEquals(expectedOptima, optima, 1e-6);

        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        double[] expectedOptimaValues = new double[] { 1.25, 1.25, 1.25 };
        assertArrayEquals(expectedOptimaValues, optimaValues, 1e-6);
    }

    @Test(expected = IllegalStateException.class)
    public void testOptimaBeforeOptimize() throws IllegalStateException {
        multiStartOptimizer.getOptima();
    }

    @Test
    public void testOptimizeSuccess() throws ConvergenceException, FunctionEvaluationException {
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(0.5, multiStartOptimizer.getResult(), 1e-6);
        assertEquals(1.25, multiStartOptimizer.getFunctionValue(), 1e-6);

        double[] optima = multiStartOptimizer.getOptima();
        double[] expectedOptima = new double[] { 0.5, 0.5, 0.5 };
        assertArrayEquals(expectedOptima, optima, 1e-6);

        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        double[] expectedOptimaValues = new double[] { 1.25, 1.25, 1.25 };
        assertArrayEquals(expectedOptimaValues, optimaValues, 1e-6);

        assertEquals(3, multiStartOptimizer.getIterationCount());
        assertEquals(6, multiStartOptimizer.getEvaluations());
        assertEquals(1e-6, multiStartOptimizer.getAbsoluteAccuracy(), 1e-9);
        assertEquals(1e-5, multiStartOptimizer.getRelativeAccuracy(), 1e-9);
        assertNull(multiStartOptimizer.isDiverging());
        assertNull(multiStartOptimizer.isConverged());
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergence() throws ConvergenceException, FunctionEvaluationException {
        when(optimizerMock.isConverged()).thenReturn(false);
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);

        assertNull(multiStartOptimizer.getOptima());
        assertNull(multiStartOptimizer.getOptimaValues());

        assertEquals(3, multiStartOptimizer.getIterationCount());
        assertEquals(6, multiStartOptimizer.getEvaluations());
        assertEquals(1e-6, multiStartOptimizer.getAbsoluteAccuracy(), 1e-9);
        assertEquals(1e-5, multiStartOptimizer.getRelativeAccuracy(), 1e-9);
        assertNull(multiStartOptimizer.isDiverging());
        assertTrue(multiStartOptimizer.isConverged());
    }

    @Test(expected = NonConvergentIterationException.class)
    public void testOptimizeDivergence() throws ConvergenceException, FunctionEvaluationException {
        when(optimizerMock.isDiverging()).thenReturn(true);
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0.0, 1.0);

        assertNull(multiStartOptimizer.getOptima());
        assertNull(multiStartOptimizer.getOptimaValues());

        assertEquals(3, multiStartOptimizer.getIterationCount());
        assertEquals(6, multiStartOptimizer.getEvaluations());
        assertEquals(1e-6, multiStartOptimizer.getAbsoluteAccuracy(), 1e-9);
        assertEquals(1e-5, multiStartOptimizer.getRelativeAccuracy(), 1e-9);
        assertTrue(multiStartOptimizer.isDiverging());
        assertNull(multiStartOptimizer.isConverged());
    }
}