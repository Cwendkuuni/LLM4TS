import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.junit.Test;

import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);

        // Typical case
        double density1 = distribution.density(5);
        assertEquals(0.1, density1, 0.0001);

        // Edge cases
        double density2 = distribution.density(0);
        assertEquals(0.1, density2, 0.0001);

        double density3 = distribution.density(10);
        assertEquals(0.1, density3, 0.0001);

        // Error scenario
        double density4 = distribution.density(-5);
        assertEquals(0.0, density4, 0.0001);
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);

        // Typical case
        double cumulative1 = distribution.cumulativeProbability(5);
        assertEquals(0.5, cumulative1, 0.0001);

        // Edge cases
        double cumulative2 = distribution.cumulativeProbability(0);
        assertEquals(0.0, cumulative2, 0.0001);

        double cumulative3 = distribution.cumulativeProbability(10);
        assertEquals(1.0, cumulative3, 0.0001);

        // Error scenario
        double cumulative4 = distribution.cumulativeProbability(-5);
        assertEquals(0.0, cumulative4, 0.0001);
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);

        // Typical case
        double mean1 = distribution.getNumericalMean();
        assertEquals(5.0, mean1, 0.0001);

        // Edge case
        UniformRealDistribution distribution2 = new UniformRealDistribution(0, 0);
        double mean2 = distribution2.getNumericalMean();
        assertEquals(0, mean2, 0.0001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);

        // Typical case
        double variance1 = distribution.getNumericalVariance();
        assertEquals(8.333, variance1, 0.001);

        // Edge case
        UniformRealDistribution distribution2 = new UniformRealDistribution(0, 0);
        double variance2 = distribution2.getNumericalVariance();
        assertEquals(0, variance2, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);

        // Typical case
        double lowerBound1 = distribution.getSupportLowerBound();
        assertEquals(0, lowerBound1, 0.0001);

        // Edge case
        UniformRealDistribution distribution2 = new UniformRealDistribution(0, 0);
        double lowerBound2 = distribution2.getSupportLowerBound();
        assertEquals(0, lowerBound2, 0.0001);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);

        // Typical case
        double upperBound1 = distribution.getSupportUpperBound();
        assertEquals(10, upperBound1, 0.0001);

        // Edge case
        UniformRealDistribution distribution2 = new UniformRealDistribution(0, 0);
        double upperBound2 = distribution2.getSupportUpperBound();
        assertEquals(0, upperBound2, 0.0001);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);

        // Typical case
        boolean inclusive1 = distribution.isSupportLowerBoundInclusive();
        assertTrue(inclusive1);

        // Edge case
        UniformRealDistribution distribution2 = new UniformRealDistribution(0, 0);
        boolean inclusive2 = distribution2.isSupportLowerBoundInclusive();
        assertTrue(inclusive2);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);

        // Typical case
        boolean inclusive1 = distribution.isSupportUpperBoundInclusive();
        assertFalse(inclusive1);

        // Edge case
        UniformRealDistribution distribution2 = new UniformRealDistribution(0, 0);
        boolean inclusive2 = distribution2.isSupportUpperBoundInclusive();
        assertFalse(inclusive2);
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);

        // Typical case
        boolean connected1 = distribution.isSupportConnected();
        assertTrue(connected1);

        // Edge case
        UniformRealDistribution distribution2 = new UniformRealDistribution(0, 0);
        boolean connected2 = distribution2.isSupportConnected();
        assertTrue(connected2);
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0, 10);

        // Typical case
        double sample1 = distribution.sample();
        assertTrue(sample1 >= 0 && sample1 <= 10);

        // Edge cases
        UniformRealDistribution distribution2 = new UniformRealDistribution(0, 0);
        double sample2 = distribution2.sample();
        assertEquals(0, sample2, 0.0001);

        UniformRealDistribution distribution3 = new UniformRealDistribution(10, 10);
        double sample3 = distribution3.sample();
        assertEquals(10, sample3, 0.0001);
    }
}