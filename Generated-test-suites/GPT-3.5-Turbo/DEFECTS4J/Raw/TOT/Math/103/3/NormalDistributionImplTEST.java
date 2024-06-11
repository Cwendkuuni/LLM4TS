import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0, 1);
    }

    @Test
    public void testGetMean() {
        assertEquals(0, normalDistribution.getMean(), 0);
    }

    @Test
    public void testSetMean() {
        normalDistribution.setMean(2);
        assertEquals(2, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2);
        assertEquals(2, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0.0001);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), 0);
            assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), 0);
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }

    @Test
    public void testInverseCumulativeProbabilityWithInvalidProbability() {
        try {
            normalDistribution.inverseCumulativeProbability(2);
            fail("IllegalArgumentException not thrown");
        } catch (IllegalArgumentException e) {
            // pass
        } catch (MathException e) {
            fail("MathException thrown");
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.3), 0);
        assertEquals(0, normalDistribution.getDomainLowerBound(0.7), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.3), 0);
        assertEquals(0, normalDistribution.getDomainUpperBound(0.7), 0);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(-1, normalDistribution.getInitialDomain(0), 0);
        assertEquals(1, normalDistribution.getInitialDomain(1), 0);
        assertEquals(0, normalDistribution.getInitialDomain(0.5), 0);
    }
}