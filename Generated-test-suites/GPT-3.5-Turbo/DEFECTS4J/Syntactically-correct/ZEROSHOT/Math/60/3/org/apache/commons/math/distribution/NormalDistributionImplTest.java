import org.apache.commons.math.distribution.NormalDistributionImpl;
import org.apache.commons.math.MathException;
import org.junit.Test;

import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testGetMean() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double mean = normalDistribution.getMean();
        assertEquals(0, mean, 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double standardDeviation = normalDistribution.getStandardDeviation();
        assertEquals(1, standardDeviation, 0.0001);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double density = normalDistribution.density(0);
        assertEquals(0.3989, density, 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double cumulativeProbability = normalDistribution.cumulativeProbability(0);
        assertEquals(0.5, cumulativeProbability, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double inverseCumulativeProbability = normalDistribution.inverseCumulativeProbability(0.5);
        assertEquals(0, inverseCumulativeProbability, 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double sample = normalDistribution.sample();
        assertNotNull(sample);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double domainLowerBound = normalDistribution.getDomainLowerBound(0.5);
        assertEquals(-Double.MAX_VALUE, domainLowerBound, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double domainUpperBound = normalDistribution.getDomainUpperBound(0.5);
        assertEquals(Double.MAX_VALUE, domainUpperBound, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);
        double initialDomain = normalDistribution.getInitialDomain(0.5);
        assertEquals(1, initialDomain, 0.0001);
    }
}