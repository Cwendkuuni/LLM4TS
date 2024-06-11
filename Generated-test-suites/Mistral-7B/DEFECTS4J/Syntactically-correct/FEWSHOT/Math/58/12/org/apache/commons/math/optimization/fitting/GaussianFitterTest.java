package org.apache.commons.math.optimization.fitting;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.optim.WeightedObservedPoint;

import org.junit.Test;

public class GaussianFitterTest {

    @Test
    public void testGaussianFitter() {
        WeightedObservedPoint[] observations = new WeightedObservedPoint[] {
            new WeightedObservedPoint(4.0254623, 531026.0, 1.0),
            new WeightedObservedPoint(4.03128248, 984167.0, 2.0),
            new WeightedObservedPoint(4.03839603, 1887233.0, 3.0),
            new WeightedObservedPoint(4.04421621, 2687152.0, 4.0),
            new WeightedObservedPoint(4.05132976, 3461228.0, 5.0),
            new WeightedObservedPoint(4.05326982, 3580526.0, 6.0),
            new WeightedObservedPoint(4.05779662, 3439750.0, 7.0),
            new WeightedObservedPoint(4.0636168, 2877648.0, 8.0),
            new WeightedObservedPoint(4.06943698, 2175960.0, 9.0),
            new WeightedObservedPoint(4.07525716, 1447024.0, 10.0),
            new WeightedObservedPoint(4.08237071, 717104.0, 11.0),
            new WeightedObservedPoint(4.08366408, 620014.0, 12.0)
        };

        GaussianFitter fitter = new GaussianFitter(new LevenbergMarquardtOptimizer());
        fitter.addObservedPoints(observations);

        double[] parameters = fitter.fit();
        assertEquals(3, parameters.length);
        assertEquals(3.0965743E7, parameters[0], 0.001);
        assertEquals(4.052086, parameters[1], 0.001);
        assertEquals(0.2134257, parameters[2], 0.001);

        // Test with initial guess
        double[] initialGuess = new double[] { 1.0, 4.0, 0.5 };
        parameters = fitter.fit(initialGuess);
        assertEquals(3, parameters.length);
        assertEquals(3.0965743E7, parameters[0], 0.001);
        assertEquals(4.052086, parameters[1], 0.001);
        assertEquals(0.2134257, parameters[2], 0.001);

        // Test with empty initial guess
        initialGuess = new double[3];
        try {
            fitter.fit(initialGuess);
            assertNull(parameters);
        } catch (OutOfRangeException e) {
            // Expected
        }
    }
}