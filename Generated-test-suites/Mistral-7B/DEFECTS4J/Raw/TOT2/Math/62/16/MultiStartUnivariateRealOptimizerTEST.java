package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.initializer.*;
import org.apache.commons.math3.optim.nonlinear.scalers.ExponentialScaler;
import org.apache.commons.math3.optim.point.DoublePointValuePair;
import org.apache.commons.math3.optim.point.PointValuePair;
import org.apache.commons.math3.optim.statistics.DescriptiveStatistics;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction testFunction;
    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizerMock;
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer;
    private final int NUM_STARTS = 3;

    @Before
    public void setUp() {
        testFunction = mock(UnivariateRealFunction.class);
        optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        when(optimizerMock.optimize(testFunction, any(), any(), anyDouble(), anyDouble()))
                .thenThrow(new MaxEvalException("Mock Exception", 10));
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, NUM_STARTS, new RandomDataGenerator());
    }

    @Test
    public void testOptimize() throws OptimizationException {
        // Test optimize with no starts
        assertNull(multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 1.0, 2.0));

        // Test optimize with valid starts
        double[] starts = new double[]{1.1, 2.2, 3.3};
        for (int i = 0; i < NUM_STARTS; ++i) {
            when(optimizerMock.optimize(testFunction, any(), any(), anyDouble(), anyDouble()))
                    .thenReturn(new DoublePointValuePair(starts[i], 0.0));
        }
        PointValuePair result = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 1.0, 2.0);
        assertEquals(0.0, result.getValue(), 0.001);
        verify(optimizerMock, times(NUM_STARTS)).optimize(testFunction, any(), any(), anyDouble(), anyDouble());
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws OptimizationException {
        when(optimizerMock.optimize(testFunction, any(), any(), anyDouble(), anyDouble()))
                .thenThrow(new ConvergenceException("Mock Convergence Exception"));
        multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 1.0, 2.0);
    }

    @Test
    public void testGetOptima() throws OptimizationException {
        double[] starts = new double[]{1.1, 2.2, 3.3};
        when(optimizerMock.optimize(testFunction, any(), any(), anyDouble(), anyDouble()))
                .thenReturn(new DoublePointValuePair(starts[0], 0.0));
        when(optimizerMock.optimize(testFunction, any(), any(), anyDouble(), anyDouble()))
                .thenReturn(new DoublePointValuePair(starts[1], 1.0));
        when(optimizerMock.optimize(testFunction, any(), any(), anyDouble(), anyDouble()))
                .thenReturn(new DoublePointValuePair(starts[2], 2.0));
        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();

        assertEquals(NUM_STARTS, optima.length);
        for (int i = 0; i < NUM_STARTS; ++i) {
            assertEquals(starts[i], optima[i].getPoint(), 0.001);
            assertNotNull(optima[i].getValue());
        }
    }

    @Test
    public void testScaling() throws OptimizationException {
        UnivariateRealFunction testFunction = new ScaledFunction(new SimpleFunction(0), 100.0);
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, NUM_STARTS, new RandomDataGenerator());

        when(optimizerMock.optimize(testFunction, any(), any(), anyDouble(), anyDouble()))
                .thenThrow(new MaxEvalException("Mock Exception", 10));
        PointValuePair result = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 1.0, 2.0);
        assertNull(result);

        ExponentialScaler scaler = new ExponentialScaler();
        UnivariateRealFunction scalableTestFunction = new ScaledFunction(testFunction, scaler);
        when(optimizerMock.optimize(scalableTestFunction, any(), any(), anyDouble(), anyDouble()))
                .thenReturn(new DoublePointValuePair(0.5, 0.1));

        result = multiStartOptimizer.optimize(scalableTestFunction, GoalType.MINIMIZE, 1.0, 2.0);
        assertEquals(0.5, result.getPoint(), 0.001);
        assertEquals(0.1, result.getValue(), 0.001);

        UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertEquals(NUM_STARTS, optima.length);
    }
}