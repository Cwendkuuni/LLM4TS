package org.apache.commons.math.special;

import org.apache.commons.math.MathException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GammaTest {

    private Gamma gamma;

    @Before
    public void setUp() {
        gamma = new Gamma();
    }

    @Test
    public void testLogGamma_WithValidInput_ShouldReturnCorrectValue() {
        // Typical use case
        double input = 5.0;
        double expected = 3.1780538303479458;
        assertEquals(expected, gamma.logGamma(input), 0.000001);

        // Edge case
        input = 0.5;
        expected = -0.7350915140342665;
        assertEquals(expected, gamma.logGamma(input), 0.000001);

        // Error scenario
        input = -1.0;
        double result = gamma.logGamma(input);
        assertEquals(Double.NaN, result, 0.0);
    }

    @Test
    public void testRegularizedGammaP_WithValidInput_ShouldReturnCorrectValue() throws MathException {
        // Typical use case
        double a = 2.0;
        double x = 1.0;
        double epsilon = 1e-9;
        int maxIterations = Integer.MAX_VALUE;
        double expected = 0.5939941502901614;
        assertEquals(expected, gamma.regularizedGammaP(a, x, epsilon, maxIterations), 0.000001);

        // Edge case
        a = 0.5;
        x = 0.0;
        epsilon = 1e-9;
        maxIterations = Integer.MAX_VALUE;
        expected = 0.0;
        assertEquals(expected, gamma.regularizedGammaP(a, x, epsilon, maxIterations), 0.000001);

        // Error scenario
        a = -1.0;
        x = 1.0;
        epsilon = 1e-9;
        maxIterations = Integer.MAX_VALUE;
        double result = gamma.regularizedGammaP(a, x, epsilon, maxIterations);
        assertEquals(Double.NaN, result, 0.0);
    }

    @Test
    public void testRegularizedGammaQ_WithValidInput_ShouldReturnCorrectValue() throws MathException {
        // Typical use case
        double a = 0.3;
        double x = 0.5;
        double epsilon = 1e-9;
        int maxIterations = Integer.MAX_VALUE;
        double expected = 0.8838623170678768;
        assertEquals(expected, gamma.regularizedGammaQ(a, x, epsilon, maxIterations), 0.000001);

        // Edge case
        a = 1.0;
        x = 0.0;
        epsilon = 1e-9;
        maxIterations = Integer.MAX_VALUE;
        expected = 1.0;
        assertEquals(expected, gamma.regularizedGammaQ(a, x, epsilon, maxIterations), 0.000001);

        // Error scenario
        a = -1.0;
        x = 1.0;
        epsilon = 1e-9;
        maxIterations = Integer.MAX_VALUE;
        double result = gamma.regularizedGammaQ(a, x, epsilon, maxIterations);
        assertEquals(Double.NaN, result, 0.0);
    }

}