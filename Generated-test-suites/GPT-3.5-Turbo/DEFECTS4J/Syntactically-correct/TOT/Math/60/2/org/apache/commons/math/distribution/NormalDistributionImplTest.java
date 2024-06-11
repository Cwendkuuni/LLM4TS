import org.apache.commons.math.MathException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.util.LocalizedFormats;
import org.apache.commons.math.distribution.NormalDistributionImpl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private double mean;
    private double sd;
    private double inverseCumAccuracy;
    private NormalDistributionImpl normalDistribution;

    @Before
    public void setup() {
        mean = 0.0;
        sd = 1.0;
        inverseCumAccuracy = 1e-9;
        normalDistribution = new NormalDistributionImpl(mean, sd, inverseCumAccuracy);
    }

    @Test
    public void testGetMean() {
        // Typical use case
        assertEquals(mean, normalDistribution.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        // Typical use case
        assertEquals(sd, normalDistribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testDensity() {
        // Typical use case
        double x = 1.0;
        double expectedDensity = 0.24197072451914337;
        assertEquals(expectedDensity, normalDistribution.density(x), 1e-15);

        // Edge case
        x = mean;
        expectedDensity = 0.3989422804014327;
        assertEquals(expectedDensity, normalDistribution.density(x), 1e-15);

        // Error scenario
        x = Double.NaN;
        double NaNResult = Double.NaN;
        assertEquals(NaNResult, normalDistribution.density(x), 0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        // Typical use case
        double x = 1.5;
        double expectedCumulativeProbability = 0.9331927987311419;
        assertEquals(expectedCumulativeProbability, normalDistribution.cumulativeProbability(x), 1e-15);

        // Edge case
        x = mean;
        double edgeCaseResult = 0.5;
        assertEquals(edgeCaseResult, normalDistribution.cumulativeProbability(x), 1e-15);

        // Error scenario
        x = Double.POSITIVE_INFINITY;
        double positiveInfinityResult = 1.0;
        assertEquals(positiveInfinityResult, normalDistribution.cumulativeProbability(x), 0.0);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        // Typical use case
        double p = 0.9;
        double expectedInverseCumulativeProbability = 1.2815515655446004;
        assertEquals(expectedInverseCumulativeProbability, normalDistribution.inverseCumulativeProbability(p), 1e-15);

        // Error scenario
        p = -0.5;
        try {
            normalDistribution.inverseCumulativeProbability(p);
            fail("OutOfRangeException should have been thrown");
        } catch (org.apache.commons.math.exception.OutOfRangeException e) {
            assertEquals(LocalizedFormats.OUT_OF_RANGE_SIMPLE, e.getSpecifier());
        }
    }

    @Test
    public void testSample() throws MathException {
        // Typical use case
        double sample = normalDistribution.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);

        // Error scenario
        normalDistribution = new NormalDistributionImpl();
        try {
            normalDistribution.sample();
            fail("MathException should have been thrown");
        } catch (MathException e) {
            // Test passed
        }
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        // Typical use case
        assertEquals(inverseCumAccuracy, normalDistribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testGetDomainLowerBound() {
        // Typical use case
        double p = 0.4;
        double expectedDomainLowerBound = -Double.MAX_VALUE;
        assertEquals(expectedDomainLowerBound, normalDistribution.getDomainLowerBound(p), 0.0);

        // Edge case
        p = 0.5;
        expectedDomainLowerBound = mean;
        assertEquals(expectedDomainLowerBound, normalDistribution.getDomainLowerBound(p), 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        // Typical use case
        double p = 0.4;
        double expectedDomainUpperBound = mean;
        assertEquals(expectedDomainUpperBound, normalDistribution.getDomainUpperBound(p), 0.0);

        // Edge case
        p = 0.5;
        expectedDomainUpperBound = Double.MAX_VALUE;
        assertEquals(expectedDomainUpperBound, normalDistribution.getDomainUpperBound(p), 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        // Typical use case
        double p = 0.2;
        double expectedInitialDomain = mean - sd;
        assertEquals(expectedInitialDomain, normalDistribution.getInitialDomain(p), 0.0);

        // Edge case
        p = 0.5;
        expectedInitialDomain = mean;
        assertEquals(expectedInitialDomain, normalDistribution.getInitialDomain(p), 0.0);
    }
}