package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.exception.MathIllegalStateException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.random.RandomGenerator;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class MultiStartUnivariateRealOptimizerTest {

    private BaseUnivariateRealOptimizer<FUNC> optimizerMock;
    private RandomGenerator generatorMock;
    private MultiStartUnivariateRealOptimizer<FUNC> multiStartOptimizer;
    private FUNC functionMock;

    @Before
    public void setUp() {
        optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        generatorMock = mock(RandomGenerator.class);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 3, generatorMock);
        functionMock = mock(FUNC.class);
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checkerMock = mock(ConvergenceChecker.class);
        multiStartOptimizer.setConvergenceChecker(checkerMock);
        verify(optimizerMock).setConvergenceChecker(checkerMock);
    }

    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<UnivariateRealPointValuePair> checkerMock = mock(ConvergenceChecker.class);
        when(optimizerMock.getConvergenceChecker()).thenReturn(checkerMock);
        Assert.assertEquals(checkerMock, multiStartOptimizer.getConvergenceChecker());
        verify(optimizerMock).getConvergenceChecker();
    }

    @Test
    public void testGetMaxEvaluations() {
        int maxEvaluations = 500;
        when(optimizerMock.getMaxEvaluations()).thenReturn(maxEvaluations);
        Assert.assertEquals(maxEvaluations, multiStartOptimizer.getMaxEvaluations());
        verify(optimizerMock).getMaxEvaluations();
    }

    @Test
    public void testGetEvaluations() {
        int totalEvaluations = 42;
        when(optimizerMock.getEvaluations()).thenReturn(totalEvaluations);
        Assert.assertEquals(totalEvaluations, multiStartOptimizer.getEvaluations());
        verify(optimizerMock).getEvaluations();
    }

    @Test
    public void testSetMaxEvaluations() {
        int maxEvaluations = 100;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        verify(optimizerMock).setMaxEvaluations(maxEvaluations);
    }

    @Test
    public void testGetOptima() {
        UnivariateRealPointValuePair[] optima = new UnivariateRealPointValuePair[3];
        optima[0] = new UnivariateRealPointValuePair(1.0, 1.0);
        optima[1] = new UnivariateRealPointValuePair(2.0, 2.0);
        optima[2] = new UnivariateRealPointValuePair(3.0, 3.0);
        when(optimizerMock.getEvaluations()).thenReturn(100);
        multiStartOptimizer.getOptima();
        verify(optimizerMock).getEvaluations();
    }

    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaNoOptimumComputed() {
        when(optimizerMock.getEvaluations()).thenReturn(0);
        multiStartOptimizer.getOptima();
        verify(optimizerMock).getEvaluations();
    }

    @Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergenceWithAnyStartPoint() {
        when(optimizerMock.optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0)).thenThrow(ConvergenceException.class);
        when(optimizerMock.getEvaluations()).thenReturn(5);
        multiStartOptimizer.optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0);
        verify(optimizerMock, times(3)).optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0);
    }

    @Test
    public void testOptimize() {
        UnivariateRealPointValuePair optima = new UnivariateRealPointValuePair(0.0, 0.0);
        when(optimizerMock.optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0)).thenReturn(optima);
        when(optimizerMock.getEvaluations()).thenReturn(10);
        multiStartOptimizer.optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0);
        verify(optimizerMock, times(3)).optimize(functionMock, GoalType.MINIMIZE, 0.0, 1.0);
    }

}