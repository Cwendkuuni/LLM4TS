import org.junit.Test;
import static org.junit.Assert.*;

public class NormalDistributionImplTest {

    NormalDistributionImpl normalDistribution;

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
    public void testConstructor() {
        normalDistribution = new NormalDistributionImpl(2.5, 3.2);
        assertEquals(2.5, normalDistribution.getMean(), 0.0001);
        assertEquals(3.2, normalDistribution.getStandardDeviation(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationWithZero() {
        normalDistribution.setStandardDeviation(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetStandardDeviationWithNegativeValue() {
        normalDistribution.setStandardDeviation(-2.5);
    }

    @Test
    public void testSetMean() {
        normalDistribution.setMean(4.2);
        assertEquals(4.2, normalDistribution.getMean(), 0.0001);
    }

    @Test
    public void testCumulativeProbability() throws MathException {
        double result = normalDistribution.cumulativeProbability(1.0);
        assertEquals(0.8413, result, 0.0001);
    }

    @Test
    public void testCumulativeProbabilityWithNegativeValue() throws MathException {
        double result = normalDistribution.cumulativeProbability(-1.5);
        assertEquals(0.0668, result, 0.0001);
    }

    @Test()
    public void testInverseCumulativeProbability() throws MathException {
        double result = normalDistribution.inverseCumulativeProbability(0.5);
        assertEquals(0.0, result, 0.0001);
    }

    @Test()
    public void testInverseCumulativeProbabilityWithLowerBound() throws MathException {
        double result = normalDistribution.inverseCumulativeProbability(0);
        assertEquals(Double.NEGATIVE_INFINITY, result, 0.0001);
    }

    @Test()
    public void testInverseCumulativeProbabilityWithUpperBound() throws MathException {
        double result = normalDistribution.inverseCumulativeProbability(1);
        assertEquals(Double.POSITIVE_INFINITY, result, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInverseCumulativeProbabilityWithInvalidProbability() throws MathException {
        normalDistribution.inverseCumulativeProbability(1.5);
    }

    @Test
    public void testGetDomainLowerBound() {
        double result = normalDistribution.getDomainLowerBound(0.25);
        assertEquals(-Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetDomainLowerBoundWithEqualProbability() {
        double result = normalDistribution.getDomainLowerBound(0.5);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testGetDomainUpperBound() {
        double result = normalDistribution.getDomainUpperBound(0.75);
        assertEquals(Double.MAX_VALUE, result, 0.0001);
    }

    @Test
    public void testGetDomainUpperBoundWithEqualProbability() {
        double result = normalDistribution.getDomainUpperBound(0.5);
        assertEquals(0.0, result, 0.0001);
    }

    @Test
    public void testGetInitialDomain() {
        double result = normalDistribution.getInitialDomain(0.3);
        assertEquals(-0.6031, result, 0.0001);
    }

    @Test
    public void testGetInitialDomainWithEqualProbability() {
        double result = normalDistribution.getInitialDomain(0.5);
        assertEquals(1.0, result, 0.0001);
    }
}