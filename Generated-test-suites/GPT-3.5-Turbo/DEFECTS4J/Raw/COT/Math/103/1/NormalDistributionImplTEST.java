import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.MathException;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0);
    }

    @Test
    public void testSetMean() {
        normalDistribution.setMean(2.5);
        assertEquals(2.5, normalDistribution.getMean(), 0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normalDistribution.cumulativeProbability(0), 0);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbabilityWithException() throws MathException {
        normalDistribution.cumulativeProbability(20);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, normalDistribution.inverseCumulativeProbability(0), 0);
        assertEquals(Double.POSITIVE_INFINITY, normalDistribution.inverseCumulativeProbability(1), 0);
    }

    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDistribution.getDomainLowerBound(0.3), 0);
        assertEquals(0.0, normalDistribution.getDomainLowerBound(0.7), 0);
    }

    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, normalDistribution.getDomainUpperBound(0.3), 0);
        assertEquals(0.0, normalDistribution.getDomainUpperBound(0.7), 0);
    }

    @Test
    public void testGetInitialDomain() {
        assertEquals(-1.0, normalDistribution.getInitialDomain(0.3), 0);
        assertEquals(1.0, normalDistribution.getInitialDomain(0.7), 0);
        assertEquals(0.0, normalDistribution.getInitialDomain(0.5), 0);
    }

}