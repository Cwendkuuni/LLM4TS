import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution = new NormalDistributionImpl(0, 1);

    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testDensity() {
        assertEquals(0.24197072451914337, normalDistribution.density(0), 1e-10);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            assertEquals(0.5, normalDistribution.cumulativeProbability(0), 1e-10);
        } catch (MathException e) {
            fail("Failed to calculate cumulative probability");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            assertEquals(0, normalDistribution.inverseCumulativeProbability(0), 1e-10);
        } catch (MathException e) {
            fail("Failed to calculate inverse cumulative probability");
        }
    }

    @Test
    public void testSample() {
        try {
            double sample = normalDistribution.sample();
            assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
        } catch (MathException e) {
            fail("Failed to generate a random sample");
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.2), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.8), 0);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(-1, normalDistribution.getInitialDomain(0.4), 0);
    }
}