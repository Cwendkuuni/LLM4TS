//Test case for MultiDirectional class

package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Comparator;
import java.util.Random;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.DirectSearchOptimizer;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class MultiDirectionalTest {

    private MultiDirectional multiDirectional;

    @Before
    public void setUp() {
        multiDirectional = new MultiDirectional();
    }

    @Test
    public void testDefaultCoefficients() {
        assertEquals(2.0, multiDirectional.getKhi(), 0.0);
        assertEquals(0.5, multiDirectional.getGamma(), 0.0);
    }

    @Test
    public void testCustomCoefficients() {
        multiDirectional = new MultiDirectional(3.0, 0.7);
        assertEquals(3.0, multiDirectional.getKhi(), 0.0);
        assertEquals(0.7, multiDirectional.getGamma(), 0.0);
    }

    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        Comparator<RealPointValuePair> comparator = new Comparator<RealPointValuePair>() {
            @Override
            public int compare(RealPointValuePair p1, RealPointValuePair p2) {
                return Double.compare(p1.getValue(), p2.getValue());
            }
        };

        DirectSearchOptimizer optimizer = mock(DirectSearchOptimizer.class);
        when(optimizer.getConvergenceChecker()).thenReturn(mock(RealConvergenceChecker.class));

        double[] point1 = {1.0, 2.0, 3.0};
        double[] point2 = {2.0, 3.0, 4.0};
        double[] point3 = {3.0, 4.0, 5.0};
        double[] point4 = {4.0, 5.0, 6.0};

        RealPointValuePair[] simplex = {new RealPointValuePair(point1, 0.0),
                new RealPointValuePair(point2, 1.0),
                new RealPointValuePair(point3, 2.0),
                new RealPointValuePair(point4, 3.0)};

        ArgumentCaptor<RealPointValuePair[]> argumentCaptor = ArgumentCaptor.forClass(RealPointValuePair[].class);

        when(optimizer.getSimplex()).thenReturn(simplex);

        multiDirectional.iterateSimplex(comparator);

        verify(optimizer, times(2)).incrementIterationsCounter();
        verify(optimizer).evaluateNewSimplex(argumentCaptor.capture(), eq(1.0), eq(comparator));
        verify(optimizer).evaluateNewSimplex(argumentCaptor.capture(), eq(2.0), eq(comparator));

        RealPointValuePair[] capturedSimplex1 = argumentCaptor.getAllValues().get(0);
        RealPointValuePair[] capturedSimplex2 = argumentCaptor.getAllValues().get(1);

        assertEquals(point4, capturedSimplex1[2].getPointRef(), 0.0);
        assertEquals(5.0, capturedSimplex2[2].getPointRef()[0], 0.0);
        assertEquals(5.0, capturedSimplex2[2].getPointRef()[1], 0.0);
        assertEquals(6.0, capturedSimplex2[2].getPointRef()[2], 0.0);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testEvaluateNewSimplexFunctionEvaluationException() throws FunctionEvaluationException, OptimizationException {
        DirectSearchOptimizer optimizer = mock(DirectSearchOptimizer.class);
        when(optimizer.getConvergenceChecker()).thenReturn(mock(RealConvergenceChecker.class));

        double[] point1 = {1.0, 2.0, 3.0};
        double[] point2 = {2.0, 3.0, 4.0};
        double[] point3 = {3.0, 4.0, 5.0};
        double[] point4 = {4.0, 5.0, 6.0};

        RealPointValuePair[] original = {new RealPointValuePair(point1, 0.0),
                new RealPointValuePair(point2, 1.0),
                new RealPointValuePair(point3, 2.0),
                new RealPointValuePair(point4, 3.0)};

        RealPointValuePair[] simplex = {new RealPointValuePair(point1, 0.0),
                new RealPointValuePair(point2, 0.0),
                new RealPointValuePair(point3, 0.0),
                new RealPointValuePair(point4, 0.0)};

        when(optimizer.getSimplex()).thenReturn(simplex);
        when(optimizer.evaluateNewSimplex(original, 1.0, null)).thenThrow(FunctionEvaluationException.class);

        multiDirectional.evaluateNewSimplex(original, 1.0, null);
    }

    @Test(expected = OptimizationException.class)
    public void testEvaluateNewSimplexMaximalNumberEvaluationsExceeded() throws FunctionEvaluationException, OptimizationException {
        DirectSearchOptimizer optimizer = mock(DirectSearchOptimizer.class);
        when(optimizer.getConvergenceChecker()).thenReturn(mock(RealConvergenceChecker.class));

        double[] point1 = {1.0, 2.0, 3.0};
        double[] point2 = {2.0, 3.0, 4.0};
        double[] point3 = {3.0, 4.0, 5.0};
        double[] point4 = {4.0, 5.0, 6.0};

        RealPointValuePair[] original = {new RealPointValuePair(point1, 0.0),
                new RealPointValuePair(point2, 1.0),
                new RealPointValuePair(point3, 2.0),
                new RealPointValuePair(point4, 3.0)};

        RealPointValuePair[] simplex = {new RealPointValuePair(point1, 0.0),
                new RealPointValuePair(point2, 0.0),
                new RealPointValuePair(point3, 0.0),
                new RealPointValuePair(point4, 0.0)};

        when(optimizer.getSimplex()).thenReturn(simplex);
        when(optimizer.evaluateNewSimplex(original, 0.5, null)).thenThrow(OptimizationException.class);

        multiDirectional.evaluateNewSimplex(original, 0.5, null);
    }
}