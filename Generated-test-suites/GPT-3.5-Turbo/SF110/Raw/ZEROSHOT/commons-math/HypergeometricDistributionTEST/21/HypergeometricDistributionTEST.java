package org.apache.commons.math3.distribution;

import org.junit.*;
import static org.junit.Assert.*;

public class HypergeometricDistributionTest {

    // Test constructor
    @Test
    public void testConstructor()
    {
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(-5, 2, 3);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // Pass
        } catch (Exception e) {
            fail("Expected NotStrictlyPositiveException");
        }
        
        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(5, -2, 3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Pass
        } catch (Exception e) {
            fail("Expected NotPositiveException");
        }

        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, -3);
            fail("Expected NotPositiveException");
        } catch (NotPositiveException e) {
            // Pass
        } catch (Exception e) {
            fail("Expected NotPositiveException");
        }

        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(5, 6, 3);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Pass
        } catch (Exception e) {
            fail("Expected NumberIsTooLargeException");
        }

        try {
            HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, 6);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // Pass
        } catch (Exception e) {
            fail("Expected NumberIsTooLargeException");
        }

        HypergeometricDistribution distribution = new HypergeometricDistribution(5, 2, 3);
        assertEquals(2, distribution.getNumberOfSuccesses());
        assertEquals(5, distribution.getPopulationSize());
        assertEquals(3, distribution.getSampleSize());
    }

    // Test cumulativeProbability method
    @Test
    public void testCumulativeProbability()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0.0, distribution.cumulativeProbability(1), 0.0001);
        assertEquals(0.3, distribution.cumulativeProbability(2), 0.0001);
        assertEquals(0.6, distribution.cumulativeProbability(3), 0.0001);
        assertEquals(0.9, distribution.cumulativeProbability(4), 0.0001);
        assertEquals(1.0, distribution.cumulativeProbability(5), 0.0001);
    }

    // Test getDomain method
    @Test
    public void testGetDomain()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        int[] domain = distribution.getDomain(10, 5, 3);
        assertEquals(0, domain[0]);
        assertEquals(3, domain[1]);
    }

    // Test getLowerDomain method
    @Test
    public void testGetLowerDomain()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        int lowerDomain = distribution.getLowerDomain(10, 5, 3);
        assertEquals(2, lowerDomain);
    }

    // Test getNumberOfSuccesses method
    @Test
    public void testGetNumberOfSuccesses()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(5, distribution.getNumberOfSuccesses());
    }

    // Test getPopulationSize method
    @Test
    public void testGetPopulationSize()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(10, distribution.getPopulationSize());
    }

    // Test getSampleSize method
    @Test
    public void testGetSampleSize()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(3, distribution.getSampleSize());
    }

    // Test getUpperDomain method
    @Test
    public void testGetUpperDomain()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        int upperDomain = distribution.getUpperDomain(5, 3);
        assertEquals(3, upperDomain);
    }

    // Test probability method
    @Test
    public void testProbability()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0.21428571428571427, distribution.probability(1), 0.0001);
        assertEquals(0.42857142857142855, distribution.probability(2), 0.0001);
        assertEquals(0.2857142857142857, distribution.probability(3), 0.0001);
    }

    // Test upperCumulativeProbability method
    @Test
    public void testUpperCumulativeProbability()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(1.0, distribution.upperCumulativeProbability(1), 0.0001);
        assertEquals(0.7, distribution.upperCumulativeProbability(2), 0.0001);
        assertEquals(0.4, distribution.upperCumulativeProbability(3), 0.0001);
        assertEquals(0.1, distribution.upperCumulativeProbability(4), 0.0001);
        assertEquals(0.0, distribution.upperCumulativeProbability(5), 0.0001);
    }

    // Test innerCumulativeProbability method
    @Test
    public void testInnerCumulativeProbability()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        double innerCumulativeProbability = distribution.innerCumulativeProbability(3, 5, 1);
        assertEquals(0.5714285714285714, innerCumulativeProbability, 0.0001);
    }

    // Test getNumericalMean method
    @Test
    public void testGetNumericalMean()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(1.5, distribution.getNumericalMean(), 0.0001);
    }

    // Test getNumericalVariance method
    @Test
    public void testGetNumericalVariance()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(0.3522727272727273, distribution.getNumericalVariance(), 0.0001);
    }

    // Test calculateNumericalVariance method
    @Test
    public void testCalculateNumericalVariance()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        double calculateNumericalVariance = distribution.calculateNumericalVariance();
        assertEquals(0.3522727272727273, calculateNumericalVariance, 0.0001);
    }

    // Test getSupportLowerBound method
    @Test
    public void testGetSupportLowerBound()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(3, distribution.getSupportLowerBound());
    }

    // Test getSupportUpperBound method
    @Test
    public void testGetSupportUpperBound()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertEquals(5, distribution.getSupportUpperBound());
    }

    // Test isSupportConnected method
    @Test
    public void testIsSupportConnected()
    {
        HypergeometricDistribution distribution = new HypergeometricDistribution(10, 5, 3);

        assertTrue(distribution.isSupportConnected());
    }
}