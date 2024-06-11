import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    private NormalDistributionImpl normalDistribution;

    @Before
    public void setUp() {
        normalDistribution = new NormalDistributionImpl();
    }

    // Test cases for getMean() method

    @Test
    public void testGetMean() {
        double expected = 0.0;
        double actual = normalDistribution.getMean();
        assertEquals(expected, actual, 0.0);
    }

    // Test cases for getStandardDeviation() method

    @Test
    public void testGetStandardDeviation() {
        double expected = 1.0;
        double actual = normalDistribution.getStandardDeviation();
        assertEquals(expected, actual, 0.0);
    }

    // Test cases for density(x) method

    @Test
    public void testDensity() {
        double x = 1.0;
        double expected = 0.24197072451914337;
        double actual = normalDistribution.density(x);
        assertEquals(expected, actual, 0.000000001);
    }

    // Test cases for cumulativeProbability(x) method

    @Test
    public void testCumulativeProbability() throws MathException {
        double x = 1.0;
        double expected = 0.8413447460685429;
        double actual = normalDistribution.cumulativeProbability(x);
        assertEquals(expected, actual, 0.000000001);
    }

    @Test(expected = MathException.class)
    public void testCumulativeProbability_Failure() throws MathException {
        double x = 10000.0;
        normalDistribution.cumulativeProbability(x);
    }

    // Test cases for inverseCumulativeProbability(p) method

    @Test
    public void testInverseCumulativeProbability() throws MathException {
        double p = 0.5;
        double expected = 0.0;
        double actual = normalDistribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testInverseCumulativeProbability_p0() throws MathException {
        double p = 0.0;
        double expected = Double.NEGATIVE_INFINITY;
        double actual = normalDistribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testInverseCumulativeProbability_p1() throws MathException {
        double p = 1.0;
        double expected = Double.POSITIVE_INFINITY;
        double actual = normalDistribution.inverseCumulativeProbability(p);
        assertEquals(expected, actual, 0.0);
    }

    @Test(expected = MathException.class)
    public void testInverseCumulativeProbability_InvalidP() throws MathException {
        double p = 2.0;
        normalDistribution.inverseCumulativeProbability(p);
    }

    // Test cases for sample() method

    @Test
    public void testSample() throws MathException {
        double actual = normalDistribution.sample();
        assertNotNull(actual);
    }

    // Test cases for getDomainLowerBound(p) method

    @Test
    public void testGetDomainLowerBound() {
        double p = 0.4;
        double expected = -Double.MAX_VALUE;
        double actual = normalDistribution.getDomainLowerBound(p);
        assertEquals(expected, actual, 0.0);
    }

    // Test cases for getDomainUpperBound(p) method

    @Test
    public void testGetDomainUpperBound() {
        double p = 0.6;
        double expected = Double.MAX_VALUE;
        double actual = normalDistribution.getDomainUpperBound(p);
        assertEquals(expected, actual, 0.0);
    }

    // Test cases for getInitialDomain(p) method

    @Test
    public void testGetInitialDomain() {
        double p = 0.5;
        double expected = 0.0;
        double actual = normalDistribution.getInitialDomain(p);
        assertEquals(expected, actual, 0.0);
    }

}