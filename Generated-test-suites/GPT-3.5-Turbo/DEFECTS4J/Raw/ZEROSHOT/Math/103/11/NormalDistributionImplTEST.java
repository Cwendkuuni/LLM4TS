import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {
    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }

    @Test
    public void testGetMean() {
        assertEquals(0.0, normalDistribution.getMean(), 0.0001);
    }

    @Test
    public void testSetMean() {
        normalDistribution.setMean(1.5);
        assertEquals(1.5, normalDistribution.getMean(), 0.0001);
    }

    @Test
    public void testGetStandardDeviation() {
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationNegative() {
        normalDistribution.setStandardDeviation(-1.0);
    }

    @Test
    public void testCumulativeProbability() {
        try {
            double result = normalDistribution.cumulativeProbability(0.0);
            assertEquals(0.5, result, 0.0001);
        } catch (MathException e) {
            fail();
        }
    }

    @Test
    public void testInverseCumulativeProbability() {
        try {
            double result = normalDistribution.inverseCumulativeProbability(0.5);
            assertEquals(0.0, result, 0.0001);
        } catch (MathException e) {
            fail();
        }
    }

    @Test
    public void testInverseCumulativeProbabilityLowerBound() {
        try {
            double result = normalDistribution.inverseCumulativeProbability(0.0);
            assertEquals(Double.NEGATIVE_INFINITY, result, 0.0001);
        } catch (MathException e) {
            fail();
        }
    }

    @Test
    public void testInverseCumulativeProbabilityUpperBound() {
        try {
            double result = normalDistribution.inverseCumulativeProbability(1.0);
            assertEquals(Double.POSITIVE_INFINITY, result, 0.0001);
        } catch (MathException e) {
            fail();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityInvalidProbability() {
        try {
            normalDistribution.inverseCumulativeProbability(-1.0);
        } catch (MathException e) {
            fail();
        }
    }

    @Test
    public void testGetDomainLowerBound() {
        double result = normalDistribution.getDomainLowerBound(0.4);
        assertEquals(-Double.MAX_VALUE, result, 0.0001);

        result = normalDistribution.getDomainLowerBound(0.6);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        double result = normalDistribution.getDomainUpperBound(0.4);
        assertEquals(0.0, result, 0.0001);

        result = normalDistribution.getDomainUpperBound(0.6);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        double result = normalDistribution.getInitialDomain(0.4);
        assertEquals(-1.0, result, 0.0001);

        result = normalDistribution.getInitialDomain(0.6);
        assertEquals(1.0, result, 0.0001);

        result = normalDistribution.getInitialDomain(0.5);
        assertEquals(0.0, result, 0.0001);
    }
}