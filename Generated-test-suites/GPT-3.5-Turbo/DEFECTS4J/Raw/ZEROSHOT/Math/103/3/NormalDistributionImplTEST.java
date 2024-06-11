import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() throws Exception {
        normalDistribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0.01);
    }

    @Test
    public void testSetMean() {
        normalDistribution.setMean(1.0);
        assertEquals(1.0, normalDistribution.getMean(), 0.01);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.01);
    }

    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.01);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0.01);
        } catch (MathException e) {
            fail("An exception occurred");
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0.0), 0.01);
        } catch (MathException e) {
            fail("An exception occurred");
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.getDomainLowerBound(0.0), 0.01);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(1.0), 0.01);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(-1.0, normalDistribution.getInitialDomain(0.25), 0.01);
    }
}