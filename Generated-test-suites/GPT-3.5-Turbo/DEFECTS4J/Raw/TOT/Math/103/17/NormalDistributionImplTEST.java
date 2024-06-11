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
    public void testDefaultConstructor() {
        assertEquals(0.0, normalDistribution.getMean(), 0.0001);
        assertEquals(1.0, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testParameterizedConstructor() {
        normalDistribution = new NormalDistributionImpl(3.2, 2.5);
        assertEquals(3.2, normalDistribution.getMean(), 0.0001);
        assertEquals(2.5, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test
    public void testSetMean() {
        normalDistribution.setMean(5.5);
        assertEquals(5.5, normalDistribution.getMean(), 0.0001);
    }

    @Test
    public void testSetStandardDeviation() {
        normalDistribution.setStandardDeviation(2.0);
        assertEquals(2.0, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationInvalid() {
        normalDistribution.setStandardDeviation(0.0);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        normalDistribution.setMean(5.0);
        normalDistribution.setStandardDeviation(2.0);

        // Test typical use case
        double result1 = normalDistribution.cumulativeProbability(6.0);
        assertEquals(0.691462, result1, 0.0001);

        // Test edge case
        double result2 = normalDistribution.cumulativeProbability(5.0);
        assertEquals(0.5, result2, 0.0001);

        // Test error scenario
        double result3 = normalDistribution.cumulativeProbability(10.0);
        assertEquals(0.8413447, result3, 0.0001);
    }

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        normalDistribution.setMean(5.0);
        normalDistribution.setStandardDeviation(2.0);

        // Test typical use case
        double result1 = normalDistribution.inverseCumulativeProbability(0.7);
        assertEquals(5.67449, result1, 0.0001);

        // Test edge case
        double result2 = normalDistribution.inverseCumulativeProbability(0.0);
        assertEquals(Double.NEGATIVE_INFINITY, result2, 0.0001);

        // Test edge case
        double result3 = normalDistribution.inverseCumulativeProbability(1.0);
        assertEquals(Double.POSITIVE_INFINITY, result3, 0.0001);

        // Test error scenario
        double result4 = normalDistribution.inverseCumulativeProbability(-0.5);
        assertEquals(Double.NaN, result4, 0.0001);
    }

    @Test
    public void testGetDomainLowerBound() {
        double result1 = normalDistribution.getDomainLowerBound(0.3);
        assertEquals(Double.NEGATIVE_INFINITY, result1, 0.0001);

        double result2 = normalDistribution.getDomainLowerBound(0.5);
        assertEquals(0.0, result2, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        double result1 = normalDistribution.getDomainUpperBound(0.2);
        assertEquals(0.0, result1, 0.0001);

        double result2 = normalDistribution.getDomainUpperBound(0.7);
        assertEquals(Double.POSITIVE_INFINITY, result2, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        double result1 = normalDistribution.getInitialDomain(0.2);
        assertEquals(-1.0, result1, 0.0001);

        double result2 = normalDistribution.getInitialDomain(0.8);
        assertEquals(1.0, result2, 0.0001);
    }

}