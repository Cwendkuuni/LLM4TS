import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.NormalDistributionImpl;
import org.apache.commons.math3.special.Erf;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    @Test
    public void testNormalDistributionImplConstructor() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(0, dist.getMean(), 0.0001);
        assertEquals(1, dist.getStandardDeviation(), 0.0001);

        NormalDistributionImpl distCustomAccuracy = new NormalDistributionImpl(1, 1, 0.00001);
        assertEquals(1, distCustomAccuracy.getMean(), 0.00001);
        assertEquals(1, distCustomAccuracy.getStandardDeviation(), 0.00001);
    }

    @Test
    public void testGetMean() {
        NormalDistributionImpl dist = new NormalDistributionImpl(3, 2);
        assertEquals(3, dist.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        NormalDistributionImpl dist = new NormalDistributionImpl(3, 2);
        assertEquals(2, dist.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testDensity() {
        NormalDistributionImpl dist = new NormalDistributionImpl(3, 2);
        double densityValue = dist.density(4);
        assertEquals(0.13416409894101, densityValue, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCumulativeProbabilityNegativeX() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        dist.cumulativeProbability(-1);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityOutOfRangeX() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        dist.cumulativeProbability(100);
    }

    @Test
    public void testCumulativeProbability() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double result = dist.cumulativeProbability(0.5);
        assertEquals(0.68268946213661, result, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbabilityNegativeP() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        try {
            dist.inverseCumulativeProbability(-0.1);
        } catch (MathException e) {
            assertEquals("Probability must be between 0 and 1", e.getMessage());
        }
    }

    @Test
    public void testInverseCumulativeProbabilityZeroP() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        try {
            dist.inverseCumulativeProbability(0);
        } catch (MathException e) {
            assertEquals("Probability must be between 0 and 1", e.getMessage());
        }
    }

    @Test
    public void testInverseCumulativeProbabilityOneP() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        try {
            dist.inverseCumulativeProbability(1);
        } catch (MathException e) {
            assertEquals("Probability must be between 0 and 1", e.getMessage());
        }
    }

    @Test
    public void testInverseCumulativeProbabilityPositiveP() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double result = dist.inverseCumulativeProbability(0.5);
        assertEquals(0, result, 0.0001);
    }

    @Test
    public void testSample() throws MathException {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double sampleValue = dist.sample();
        assertTrue(sampleValue > -3 && sampleValue < 3);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        assertEquals(1e-9, dist.getSolverAbsoluteAccuracy(), 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double lowerBound = dist.getDomainLowerBound(0.5);
        assertEquals(-3.06834775851465, lowerBound, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double upperBound = dist.getDomainUpperBound(0.5);
        assertEquals(2.06834775851465, upperBound, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        NormalDistributionImpl dist = new NormalDistributionImpl(0, 1);
        double initialDomain = dist.getInitialDomain(0.5);
        assertEquals(0.068347758514647, initialDomain, 0.0001);
    }
}