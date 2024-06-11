package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.goaltype.GoalType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class SimplexOptimizerTest {

    private MultivariateFunction mockFunction;
    private SimplexOptimizer optimizer;
    private Comparator<PointValuePair> mockComparator;
    private ConvergenceChecker<PointValuePair> mockChecker;

    @Before
    public void setUp() {
        mockFunction = mock(MultivariateFunction.class);
        mockComparator = mock(Comparator.class);
        mockChecker = mock(ConvergenceChecker.class);
        optimizer = new SimplexOptimizer(mockChecker);
    }

    @Test
    public void testEmptyConstructor() {
        Assert.assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithDefaultChecker() {
        optimizer = new SimplexOptimizer();
        Assert.assertNotNull(optimizer);
    }

    @Test
    public void testConstructorWithRelativeAbsoluteChecker() {
        optimizer = new SimplexOptimizer(0.01d, 0.001d);
        Assert.assertNotNull(optimizer);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetSimplexNullArgument() {
        optimizer.setSimplex(null);
    }

    @Test
    public void testSetSimplex() {
        AbstractSimplex mockSimplex = mock(AbstractSimplex.class);
        optimizer.setSimplex(mockSimplex);
        verify(optimizer).parseOptimizationData(mockSimplex);
    }

    @Test
    public void testOptimizeWithNullFunction() {
        Assert.assertNull(optimizer.optimize(10, null, GoalType.MINIMIZE, new OptimizationData[0]));
    }

    @Test
    public void testOptimizeWithNullOptimizationData() {
        Assert.assertNull(optimizer.optimize(10, mockFunction, GoalType.MINIMIZE, null));
    }

    @Test
    public void testOptimizeWithDefaultGoalType() {
        when(mockFunction.value(anyDoubleArray())).thenReturn(Math.PI * Math.PI);

        PointValuePair result = optimizer.optimize(10, mockFunction, null, new OptimizationData[0]);

        PointValuePair expected = new PointValuePair(new double[0], Math.PI * Math.PI);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOptimizeWithMinimizationGoalType() {
        when(mockFunction.value(anyDoubleArray())).thenReturn(Math.PI * Math.PI);

        optimizer = new SimplexOptimizer(GoalType.MINIMIZE);

        PointValuePair result = optimizer.optimize(10, mockFunction, GoalType.MINIMIZE, new OptimizationData[0]);

        PointValuePair expected = new PointValuePair(new double[0], Math.PI * Math.PI);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOptimizeWithMaximizationGoalType() {
        when(mockFunction.value(anyDoubleArray())).thenReturn(-Math.PI * Math.PI);

        optimizer = new SimplexOptimizer(GoalType.MAXIMIZE);

        PointValuePair result = optimizer.optimize(10, mockFunction, GoalType.MAXIMIZE, new OptimizationData[0]);

        PointValuePair expected = new PointValuePair(new double[0], -Math.PI * Math.PI);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testOptimizeWithConvergenceChecker() {
        when(mockFunction.value(anyDoubleArray())).thenReturn(Math.PI * Math.PI);
        when(mockComparator.compare(any(PointValuePair.class), any(PointValuePair.class)))
                .thenReturn(-1);

        optimizer = new SimplexOptimizer(mockChecker);

        PointValuePair result = optimizer.optimize(10, mockFunction, GoalType.MINIMIZE, new OptimizationData[0]);

        PointValuePair expected = new PointValuePair(new double[0], Math.PI * Math.PI);
        Assert.assertEquals(expected, result);

        verify(mockChecker).converged(0, any(PointValuePair.class), any(PointValuePair.class));
        verifyNoMoreInteractions(mockChecker);
    }

    @Test
    public void testOptimizeWithInitialGuess() {
        double[] initialGuess = new double[]{1.0, 2.0};
        when(mockFunction.value(anyDoubleArray())).thenReturn(Math.PI * Math.PI);

        OptimizationData optimizationData = mock(OptimizationData.class);
        when(optimizationData.getInitialGuess()).thenReturn(initialGuess);

        PointValuePair result = optimizer.optimize(10, mockFunction, GoalType.MINIMIZE, optimizationData);

        PointValuePair expected = new PointValuePair(initialGuess, Math.PI * Math.PI);
        Assert.assertEquals(expected, result);

        verify(optimizer).parseOptimizationData(optimizationData);
    }

    @Test
    public void testOptimizeWithCustomSimplex() {
        double[] initialGuess = new double[]{1.0, 2.0};
        double[] simplexPoints = new double[][]{
                {0.0, 0.0}, {1.0, 0.0}, {0.5, 0.5}};

        when(mockFunction.value(anyDoubleArray())).thenReturn(Math.PI * Math.PI);

        OptimizationData optimizationData = mock(OptimizationData.class);
        optimizationData.setParameter(AbstractSimplex.class.getName(), simplexPoints);

        PointValuePair result = optimizer.optimize(10, mockFunction, GoalType.MINIMIZE, optimizationData);

        PointValuePair expected = new PointValuePair(simplexPoints[0], Math.PI * Math.PI);
        Assert.assertEquals(expected, result);

        verify(optimizer).parseOptimizationData(optimizationData);
    }

    @Test(expected = NullArgumentException.class)
    public void testSetSimplexWithNullArgument() {
        optimizer.setSimplex(null);
    }

    @Test
    public void testParseOptimizationData() {
        AbstractSimplex mockSimplex = mock(AbstractSimplex.class);

        optimizer.parseOptimizationData(mockSimplex);

        verify(optimizer).setSimplex(mockSimplex);
    }

    @Test
    public void testDoOptimizeWithNoIterations() {
        when(mockFunction.value(anyDoubleArray())).thenReturn(Math.PI * Math.PI);

        optimizer.doOptimize();

        verify(mockFunction, times(0)).value(anyDoubleArray());
    }

    @Test
    public void testDoOptimize() {
        when(mockFunction.value(anyDoubleArray())).thenReturn(Math.PI * Math.PI);

        optimizer.doOptimize();

        verify(mockFunction, times(1)).value(anyDoubleArray());
    }
}