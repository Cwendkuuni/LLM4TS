package org.apache.commons.math3.distribution;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.exception.util.*;

public class HypergeometricDistributionTest {

    @Test
    public void testConstructorValidInputs() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);
            assertEquals(10, dist.getPopulationSize());
            assertEquals(5, dist.getNumberOfSuccesses());
            assertEquals(3, dist.getSampleSize());
        } catch (Exception e) {
            fail("Constructor threw an exception");
        }
    }

    @Test
    public void testConstructorInvalidInputs() {
        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(-10, 5, 3);
            fail("Constructor did not throw NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected exception
        } catch (Exception e) {
            fail("Constructor threw incorrect exception");
        }

        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, -5, 3);
            fail("Constructor did not throw NotPositiveException");
        } catch (NotPositiveException e) {
            // expected exception
        } catch (Exception e) {
            fail("Constructor threw incorrect exception");
        }

        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, -3);
            fail("Constructor did not throw NotPositiveException");
        } catch (NotPositiveException e) {
            // expected exception
        } catch (Exception e) {
            fail("Constructor threw incorrect exception");
        }

        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 15, 10);
            fail("Constructor did not throw NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected exception
        } catch (Exception e) {
            fail("Constructor threw incorrect exception");
        }

        try {
            HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 10);
            fail("Constructor did not throw NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected exception
        } catch (Exception e) {
            fail("Constructor threw incorrect exception");
        }
    }

    @Test
    public void testCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0.0, dist.cumulativeProbability(-1), 0.0001);
        assertEquals(0.0, dist.cumulativeProbability(0), 0.0001);
        assertEquals(0.1389, dist.cumulativeProbability(1), 0.0001);
        assertEquals(0.6667, dist.cumulativeProbability(2), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(3), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, dist.cumulativeProbability(5), 0.0001);
    }

    @Test
    public void testGetDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        int[] domain = dist.getDomain(10, 5, 3);
        int[] expected = {0, 3};
        assertArrayEquals(expected, domain);
    }

    @Test
    public void testGetLowerDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        int lowerDomain = dist.getLowerDomain(10, 5, 3);
        assertEquals(2, lowerDomain);
    }

    @Test
    public void testGetNumberOfSuccesses() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        assertEquals(5, dist.getNumberOfSuccesses());
    }

    @Test
    public void testGetPopulationSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        assertEquals(10, dist.getPopulationSize());
    }

    @Test
    public void testGetSampleSize() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        assertEquals(3, dist.getSampleSize());
    }

    @Test
    public void testGetUpperDomain() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        int upperDomain = dist.getUpperDomain(5, 3);
        assertEquals(3, upperDomain);
    }

    @Test
    public void testProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0.1667, dist.probability(0), 0.0001);
        assertEquals(0.4444, dist.probability(1), 0.0001);
        assertEquals(0.25, dist.probability(2), 0.0001);
        assertEquals(0.0, dist.probability(3), 0.0001);
        assertEquals(0.0, dist.probability(4), 0.0001);
        assertEquals(0.0, dist.probability(5), 0.0001);
    }

    @Test
    public void testUpperCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        assertEquals(1.0, dist.upperCumulativeProbability(-1), 0.0001);
        assertEquals(1.0, dist.upperCumulativeProbability(0), 0.0001);
        assertEquals(1.0, dist.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.3611, dist.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.0417, dist.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.0, dist.upperCumulativeProbability(5), 0.0001);
    }

    @Test
    public void testInnerCumulativeProbability() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        double innerCumulativeProbability = dist.innerCumulativeProbability(2, 2, 1);
        assertEquals(0.6667, innerCumulativeProbability, 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        assertEquals(1.5, dist.getNumericalMean(), 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0.4737, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testCalculateNumericalVariance() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        double calculateNumericalVariance = dist.calculateNumericalVariance();
        assertEquals(0.4737, calculateNumericalVariance, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0, dist.getSupportLowerBound());
    }

    @Test
    public void testGetSupportUpperBound() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        assertEquals(3, dist.getSupportUpperBound());
    }

    @Test
    public void testIsSupportConnected() {
        HypergeometricDistribution dist = new HypergeometricDistribution(10, 5, 3);

        assertTrue(dist.isSupportConnected());
    }
}