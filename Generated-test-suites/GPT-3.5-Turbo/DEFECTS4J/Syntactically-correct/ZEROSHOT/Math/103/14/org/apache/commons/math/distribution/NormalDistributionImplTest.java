import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl(0.0, 1.0);
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0.0);
    }

    @Test
    public void testSetMean() {
        normalDistribution.setMean(1.0);
        assertEquals(1.0, normalDistribution.getMean(), 0.0);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0);
    }

    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationWithNegativeValue() {
        normalDistribution.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            double cdf = normalDistribution.cumulativeProbability(1.0);
            assertEquals(0.8413447460685429, cdf, 0.0001);
        }
        catch (MathException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            double inverseCdf = normalDistribution.inverseCumulativeProbability(0.8413447460685429);
            assertEquals(1.0, inverseCdf, 0.0001);
        }
        catch (MathException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability() {
        try {
            double inverseCdf = normalDistribution.inverseCumulativeProbability(2.0);
        }
        catch (MathException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        double lowerBound = normalDistribution.getDomainLowerBound(0.3);
        assertEquals(-Double.MAX_VALUE, lowerBound, 0.0);
    }

    @Test
    public void testGetDomainUpperBound() {
        double upperBound = normalDistribution.getDomainUpperBound(0.7);
        assertEquals(Double.MAX_VALUE, upperBound, 0.0);
    }

    @Test
    public void testGetInitialDomain() {
        double initialDomain = normalDistribution.getInitialDomain(0.5);
        assertEquals(0.0, initialDomain, 0.0);
    }

}