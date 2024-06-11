
package org.apache.commons.math.optimization.fitting.test;

import org.apache.commons.math.optimization.fitting.GaussianFitter;
import org.apache.commons.math.optimization.fitting.ParameterGuesser;
import org.apache.commons.math.optimization.fitting.WeightedObservedPoint;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class GaussianFitterTest {

    @Test
    public void testFitWithInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(null);
        fitter.addObservedPoint(0, 0);
        fitter.addObservedPoint(1, 1);
        fitter.addObservedPoint(2, 2);

        double[] initialGuess = {1, 1, 1};
        double[] actual = fitter.fit(initialGuess);
        double[] expected = {1, 1, 1};

        assertArrayEquals(expected, actual, 0.001);
    }

    @Test
    public void testFitWithoutInitialGuess() {
        GaussianFitter fitter = new GaussianFitter(null);
        fitter.addObservedPoint(0, 0);
        fitter.addObservedPoint(1, 1);
        fitter.addObservedPoint(2, 2);

        double[] actual = fitter.fit();
        double[] expected = {1, 1, 1};

        assertArrayEquals(expected, actual, 0.001);
    }

    @Test
    public void testParameterGuesser() {
        WeightedObservedPoint[] points = {
            new WeightedObservedPoint(1, 1),
            new WeightedObservedPoint(2, 2),
            new WeightedObservedPoint(3, 4),
            new WeightedObservedPoint(4, 6),
            new WeightedObservedPoint(5, 8)
        };

        ParameterGuesser guesser = new ParameterGuesser(points);
        double[] actual = guesser.guess();
        double[] expected = {8, 5, 1};

        assertArrayEquals(expected, actual, 0.001);
    }
}
