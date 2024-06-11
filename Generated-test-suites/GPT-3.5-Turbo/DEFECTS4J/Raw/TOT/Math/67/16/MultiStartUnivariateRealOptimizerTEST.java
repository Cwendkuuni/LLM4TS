import org.apache.commons.math.ConvergenceException;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.random.RandomGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MultiStartUnivariateRealOptimizerTest {

    private MultiStartUnivariateRealOptimizer optimizer;
    private UnivariateRealOptimizer singleOptimizer;
    private RandomGenerator randomGenerator;

    @Before
    public void setUp() {
        singleOptimizer = mock(UnivariateRealOptimizer.class);
        randomGenerator = mock(RandomGenerator.class);
        optimizer = new MultiStartUnivariateRealOptimizer(singleOptimizer, 3, randomGenerator);
    }

    @Test
    public void testGetFunctionValue() {
        when(singleOptimizer.getFunctionValue()).thenReturn(10.0);
        double result = optimizer.getFunctionValue();
        assertEquals(10.0, result, 0.001);
        verify(singleOptimizer, times(1)).getFunctionValue();
    }

    @Test
    public void testGetResult() {
        when(singleOptimizer.getResult()).thenReturn(5.0);
        double result = optimizer.getResult();
        assertEquals(5.0, result, 0.001);
        verify(singleOptimizer, times(1)).getResult();
    }

    @Test
    public void testGetAbsoluteAccuracy() {
        when(singleOptimizer.getAbsoluteAccuracy()).thenReturn(0.01);
        double result = optimizer.getAbsoluteAccuracy();
        assertEquals(0.01, result, 0.001);
        verify(singleOptimizer, times(1)).getAbsoluteAccuracy();
    }

    @Test
    public void testGetIterationCount() {
        when(singleOptimizer.getIterationCount()).thenReturn(10);
        int result = optimizer.getIterationCount();
        assertEquals(10, result);
        verify(singleOptimizer, times(1)).getIterationCount();
    }

    @Test
    public void testGetMaximalIterationCount() {
        optimizer.setMaximalIterationCount(100);
        int result = optimizer.getMaximalIterationCount();
        assertEquals(100, result);
    }

    @Test
    public void testGetMaxEvaluations() {
        optimizer.setMaxEvaluations(200);
        int result = optimizer.getMaxEvaluations();
        assertEquals(200, result);
    }

    @Test
    public void testGetEvaluations() {
        when(singleOptimizer.getEvaluations()).thenReturn(20);
        int result = optimizer.getEvaluations();
        assertEquals(20, result);
        verify(singleOptimizer, times(1)).getEvaluations();
    }

    @Test
    public void testGetRelativeAccuracy() {
        when(singleOptimizer.getRelativeAccuracy()).thenReturn(0.001);
        double result = optimizer.getRelativeAccuracy();
        assertEquals(0.001, result, 0.001);
        verify(singleOptimizer, times(1)).getRelativeAccuracy();
    }

    @Test
    public void testResetAbsoluteAccuracy() {
        optimizer.resetAbsoluteAccuracy();
        verify(singleOptimizer, times(1)).resetAbsoluteAccuracy();
    }

    @Test
    public void testResetMaximalIterationCount() {
        optimizer.resetMaximalIterationCount();
        verify(singleOptimizer, times(1)).resetMaximalIterationCount();
    }

    @Test
    public void testResetRelativeAccuracy() {
        optimizer.resetRelativeAccuracy();
        verify(singleOptimizer, times(1)).resetRelativeAccuracy();
    }

    @Test
    public void testSetAbsoluteAccuracy() {
        optimizer.setAbsoluteAccuracy(0.001);
        verify(singleOptimizer, times(1)).setAbsoluteAccuracy(0.001);
    }

    @Test
    public void testSetMaximalIterationCount() {
        optimizer.setMaximalIterationCount(100);
        int result = optimizer.getMaximalIterationCount();
        assertEquals(100, result);
    }

    @Test
    public void testSetMaxEvaluations() {
        optimizer.setMaxEvaluations(200);
        int result = optimizer.getMaxEvaluations();
        assertEquals(200, result);
    }

    @Test
    public void testSetRelativeAccuracy() {
        optimizer.setRelativeAccuracy(0.001);
        verify(singleOptimizer, times(1)).setRelativeAccuracy(0.001);
    }

    @Test
    public void testGetOptima() {
        double[] optima = {1.0, 2.0, 3.0};
        when(singleOptimizer.getOptima()).thenReturn(optima);
        double[] result = optimizer.getOptima();
        assertEquals(optima, result);
        verify(singleOptimizer, times(1)).getOptima();
    }

    @Test
    public void testGetOptimaValues() {
        double[] optimaValues = {10.0, 20.0, 30.0};
        when(singleOptimizer.getOptimaValues()).thenReturn(optimaValues);
        double[] result = optimizer.getOptimaValues();
        assertEquals(optimaValues, result);
        verify(singleOptimizer, times(1)).getOptimaValues();
    }

    @Test(expected = OptimizationException.class)
    public void testOptimize_NoConvergence() throws ConvergenceException, FunctionEvaluationException {
        when(singleOptimizer.optimize(Mockito.any(), Mockito.any(), Mockito.anyDouble(), Mockito.anyDouble())).thenThrow(new ConvergenceException());
        optimizer.optimize(Mockito.any(), Mockito.any(), Mockito.anyDouble(), Mockito.anyDouble());
    }

    @Test
    public void testOptimize() throws ConvergenceException, FunctionEvaluationException {
        when(singleOptimizer.optimize(Mockito.any(), Mockito.any(), Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(5.0);
        double result = optimizer.optimize(Mockito.any(), Mockito.any(), Mockito.anyDouble(), Mockito.anyDouble());
        assertEquals(5.0, result, 0.001);
        verify(singleOptimizer, times(1)).optimize(Mockito.any(), Mockito.any(), Mockito.anyDouble(), Mockito.anyDouble());
    }

    @Test
    public void testOptimize_WithStartValue() throws ConvergenceException, FunctionEvaluationException {
        when(singleOptimizer.optimize(Mockito.any(), Mockito.any(), Mockito.anyDouble(), Mockito.anyDouble())).thenReturn(7.0);
        double result = optimizer.optimize(Mockito.any(), Mockito.any(), Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble());
        assertEquals(7.0, result, 0.001);
        verify(singleOptimizer, times(1)).optimize(Mockito.any(), Mockito.any(), Mockito.anyDouble(), Mockito.anyDouble());
    }
}