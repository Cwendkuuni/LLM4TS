package org.apache.commons.math.optimization.direct;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.optimization.OptimizationException;
import org.junit.Test;

public class MultiDirectionalTest {
    @Test
    public void testIterateSimplex() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
        MultiDirectional multiDirectional = new MultiDirectional();
        
        // Test for khi = 2.0 and gamma = 0.5
        RealPointValuePair[] original = new RealPointValuePair[5];
        original[0] = new RealPointValuePair(new double[] {1.0, 2.0, 3.0}, 0.0);
        original[1] = new RealPointValuePair(new double[] {2.0, 3.0, 4.0}, 0.0);
        original[2] = new RealPointValuePair(new double[] {3.0, 4.0, 5.0}, 0.0);
        original[3] = new RealPointValuePair(new double[] {4.0, 5.0, 6.0}, 0.0);
        original[4] = new RealPointValuePair(new double[] {5.0, 6.0, 7.0}, 0.0);
        
        MultiDirectional.setSimplex(original);
        multiDirectional.iterateSimplex(null);
        
        double[] expectedPoint = {1.0, 2.0, 3.0};
        double[] actualPoint = MultiDirectional.getSimplex()[0].getPointRef();
        assertEquals(expectedPoint, actualPoint);
    }
}