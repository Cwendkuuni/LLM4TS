package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Comparator;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.RealConvergenceChecker;
import org.apache.commons.math.optimization.RealPointValuePair;
import org.junit.Before;
import org.junit.Test;

public class MultiDirectionalTest {

    private MultiDirectional optimizer;
    private Comparator<RealPointValuePair> mockComparator;

    @Before
    public void setUp() {
        optimizer = new MultiDirectional();
        mockComparator = mock(Comparator.class);
    }
    
    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0, 2.0}, 2.0),
                                          new RealPointValuePair(new double[]{2.0, 3.0}, 3.0)};
        
        when(mockComparator.compare(any(), any())).thenReturn(-1);
        
        optimizer.iterateSimplex(mockComparator);
        
        verify(mockComparator, times(2)).compare(any(), any());
    }
    
    @Test
    public void testEvaluateNewSimplex() throws FunctionEvaluationException, OptimizationException {
        RealPointValuePair[] original = {new RealPointValuePair(new double[]{1.0, 2.0}, 2.0),
                                          new RealPointValuePair(new double[]{2.0, 3.0}, 3.0)};
        
        RealPointValuePair result = optimizer.evaluateNewSimplex(original, 1.0, mockComparator);
        double[] expectedResult = {2.0, 3.0};
        
        assertEquals(expectedResult.length, result.getPointRef().length);
        for (int i = 0; i < expectedResult.length; i++) {
            assertEquals(expectedResult[i], result.getPointRef()[i], 0.0001);
        }
        
        verify(mockComparator).compare(any(), any());
    }
}