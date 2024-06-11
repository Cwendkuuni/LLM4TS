import org.apache.commons.math.MathException;
import org.junit.Test;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(5, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        assertEquals(2, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        double density = normalDistribution.density(7);
        assertEquals(0.120985362, density, 0.000001);
    }

    @Test
    public void testDensityWhenNegativeX() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        double density = normalDistribution.density(-3);
        assertEquals(0.008764151, density, 0.000001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        double cumulativeProbability = normalDistribution.cumulativeProbability(7);
        assertEquals(0.841344746, cumulativeProbability, 0.000001);
    }

    @Test
    public void testCumulativeProbabilityWhenNegativeX() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        double cumulativeProbability = normalDistribution.cumulativeProbability(-3);
        assertEquals(0.022750132, cumulativeProbability, 0.000001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityException() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        normalDistribution.cumulativeProbability(100);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0.841344746);
        assertEquals(7, inverseCumulativeProbability, 0);
    }

    @Test
    public void testInverseCumulativeProbabilityWhenZeroP() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0);
        assertEquals(Double.NEGATIVE_INFINITY, inverseCumulativeProbability, 0);
    }

    @Test
    public void testInverseCumulativeProbabilityWhenOneP() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, inverseCumulativeProbability, 0);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        double sample = normalDistribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        double domainLowerBound = normalDistribution.getDomainLowerBound(0.3);
        assertEquals(-Double.MAX_VALUE, domainLowerBound, 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        double domainUpperBound = normalDistribution.getDomainUpperBound(0.3);
        assertEquals(Double.MAX_VALUE, domainUpperBound, 0);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(5, 2);
        double initialDomain = normalDistribution.getInitialDomain(0.3);
        assertEquals(7, initialDomain, 0);
    }
}