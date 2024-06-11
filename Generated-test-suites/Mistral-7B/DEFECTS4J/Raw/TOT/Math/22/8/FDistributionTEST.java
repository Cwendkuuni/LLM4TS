package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link FDistribution}.
 */
public class FDistributionTest {

    /**
     * Tests constructor.
     */
    @Test
    public void testConstructor() {
        try {
            new FDistribution(2.0, 3.0);
            new FDistribution(2.0, 0.0);
            new FDistribution(0.0, 3.0);
            new FDistribution(0.0, 0.0);
        } catch (NotStrictlyPositiveException e) {
            // Expected
        }

        new FDistribution(3.0, 3.0);
        new FDistribution(5.0, 3.0);
        new FDistribution(3.0, 5.0);
        new FDistribution(5.0, 5.0);
        new FDistribution(3.0, 10.0);
        new FDistribution(10.0, 3.0);
    }

    /**
     * Tests density method.
     */
    @Test
    public void testDensity() {
        FDistribution f = new FDistribution(3.0, 5.0);
        double[] x = {0.0, 0.5, 1.0, 2.0, 3.0, 4.0, 5.0, 10.0};
        double[] expected = {0.0, 0.116, 0.231, 0.361, 0.470, 0.532, 0.5, 0.0};
        for (int i = 0; i < x.length; i++) {
            double result = f.density(x[i]);
            assertEquals(expected[i], result, 1e-6);
        }
    }

    /**
     * Tests cumulativeProbability method.
     */
    @Test
    public void testCumulativeProbability() {
        FDistribution f = new FDistribution(3.0, 5.0);
        double[] x = {0.0, 0.1, 0.3, 0.5, 1.0, 2.0, 3.0, 4.0, 5.0, 10.0};
        double[] expected = {0.0, 0.037, 0.138, 0.378, 0.6, 0.811, 0.954, 0.995, 0.999, 1.0};
        for (int i = 0; i < x.length; i++) {
            double result = f.cumulativeProbability(x[i]);
            assertEquals(expected[i], result, 1e-6);
        }
    }

    /**
     * Tests getNumeratorDegreesOfFreedom method.
     */
    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution f = new FDistribution(3.0, 5.0);
        assertEquals(3.0, f.getNumeratorDegreesOfFreedom(), 1e-6);
    }

    /**
     * Tests getDenominatorDegreesOfFreedom method.
     */
    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution f = new FDistribution(3.0, 5.0);
        assertEquals(5.0, f.getDenominatorDegreesOfFreedom(), 1e-6);
    }

    /**
     * Tests getSolverAbsoluteAccuracy method.
     */
    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution f = new FDistribution(3.0, 5.0);
        assertEquals(FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY,
                     f.getSolverAbsoluteAccuracy(), 1e-6);
    }

    /**
     * Tests getNumericalMean method.
     */
    @Test
    public void testGetNumericalMean() {
        FDistribution f = new FDistribution(3.0, 5.0);
        assertEquals(3.375, f.getNumericalMean(), 1e-6);
    }

    /**
     * Tests getNumericalVariance method.
     */
    @Test
    public void testGetNumericalVariance() {
        FDistribution f = new FDistribution(3.0, 5.0);
        assertEquals(1.328, f.getNumericalVariance(), 1e-6);
    }

    /**
     * Tests getSupportLowerBound method.
     */
    @Test
    public void testGetSupportLowerBound() {
        FDistribution f = new FDistribution(3.0, 5.0);
        assertEquals(0.0, f.getSupportLowerBound(), 1e-6);
    }

    /**
     * Tests getSupportUpperBound method.
     */
    @Test
    public void testGetSupportUpperBound() {
        FDistribution f = new FDistribution(3.0, 5.0);
        assertEquals(Double.POSITIVE_INFINITY, f.getSupportUpperBound(), 1e-6);
    }

    /**
     * Tests isSupportLowerBoundInclusive method.
     */
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution f = new FDistribution(3.0, 5.0);
        assertTrue(f.isSupportLowerBoundInclusive());
    }

    /**
     * Tests isSupportUpperBoundInclusive method.
     */
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution f = new FDistribution(3.0, 5.0);
        assertFalse(f.isSupportUpperBoundInclusive());
    }

    /**
     * Tests isSupportConnected method.
     */
    @Test
    public void testIsSupportConnected() {
        FDistribution f = new FDistribution(3.0, 5.0);
        assertTrue(f.isSupportConnected());
    }
}