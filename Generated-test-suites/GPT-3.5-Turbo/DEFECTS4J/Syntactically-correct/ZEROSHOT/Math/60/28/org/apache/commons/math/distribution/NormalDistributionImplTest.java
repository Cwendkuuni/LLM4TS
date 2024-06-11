import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    // Create a test instance
    NormalDistributionImpl normalDistributionImpl = new NormalDistributionImpl();

    // Test the getMean method
    @Test
    public void testGetMean() {
        assertEquals(0, normalDistributionImpl.getMean(), 0);
    }

    // Test the getStandardDeviation method
    @Test
    public void testGetStandardDeviation() {
        assertEquals(1, normalDistributionImpl.getStandardDeviation(), 0);
    }

    // Test the density method
    @Test
    public void testDensity() {
        assertEquals(0.3989422804014327, normalDistributionImpl.density(0), 0);
    }

    // Test the cumulativeProbability method
    @Test
    public void testCumulativeProbability() throws MathException {
        assertEquals(0.5, normalDistributionImpl.cumulativeProbability(0), 0);
    }

    // Test the inverseCumulativeProbability method
    @Test
    public void testInverseCumulativeProbability() throws MathException {
        assertEquals(Double.NEGATIVE_INFINITY, normalDistributionImpl.inverseCumulativeProbability(0), 0);
    }

    // Test the sample method
    @Test
    public void testSample() throws MathException {
        double sample = normalDistributionImpl.sample();
        assertTrue(sample >= Double.NEGATIVE_INFINITY && sample <= Double.POSITIVE_INFINITY);
    }

    // Test the getDomainLowerBound method
    @Test
    public void testGetDomainLowerBound() {
        assertEquals(-Double.MAX_VALUE, normalDistributionImpl.getDomainLowerBound(0), 0);
    }

    // Test the getDomainUpperBound method
    @Test
    public void testGetDomainUpperBound() {
        assertEquals(Double.MAX_VALUE, normalDistributionImpl.getDomainUpperBound(1), 0);
    }

    // Test the getInitialDomain method
    @Test
    public void testGetInitialDomain() {
        assertEquals(-1, normalDistributionImpl.getInitialDomain(0.5), 0);
    }

    // Test constructor with mean and standard deviation
    @Test
    public void testConstructorWithMeanAndStandardDeviation() {
        NormalDistributionImpl normalDistributionImpl = new NormalDistributionImpl(10, 5);
        assertEquals(10, normalDistributionImpl.getMean(), 0);
        assertEquals(5, normalDistributionImpl.getStandardDeviation(), 0);
    }

    // Test constructor with mean, standard deviation and inverse cumulative distribution accuracy
    @Test
    public void testConstructorWithMeanStandardDeviationAndInverseCumulativeDistributionAccuracy() {
        NormalDistributionImpl normalDistributionImpl = new NormalDistributionImpl(10, 5, 1e-6);
        assertEquals(10, normalDistributionImpl.getMean(), 0);
        assertEquals(5, normalDistributionImpl.getStandardDeviation(), 0);
        assertEquals(1e-6, normalDistributionImpl.getSolverAbsoluteAccuracy(), 0);
    }

    // Test constructor with mean and standard deviation, should throw NotStrictlyPositiveException
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithMeanAndStandardDeviation_Exception() {
        new NormalDistributionImpl(10, 0);
    }
}