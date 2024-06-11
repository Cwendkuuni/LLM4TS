import org.junit.Test;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class FDistributionTest {

    // Test constructor
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidDegreesOfFreedom() {
        new FDistribution(0, 1);
    }

    // Test density
    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(3, 4);
        assertEquals(0.0564930150371122, distribution.density(0.5), 0.0001);
    }

    // Test cumulative probability
    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(3, 4);
        assertEquals(0.5946524253518085, distribution.cumulativeProbability(0.5), 0.0001);
    }

    // Test getNumericalMean
    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(3, 4);
        assertEquals(1.6, distribution.getNumericalMean(), 0.0001);
    }

    // Test getNumericalVariance
    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(3, 4);
        assertEquals(5.2, distribution.getNumericalVariance(), 0.0001);
    }

    // Test getSupportLowerBound
    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(3, 4);
        assertEquals(0, distribution.getSupportLowerBound(), 0.0001);
    }

    // Test getSupportUpperBound
    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(3, 4);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), 0.0001);
    }

    // Test isSupportLowerBoundInclusive
    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(3, 4);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    // Test isSupportUpperBoundInclusive
    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(3, 4);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    // Test isSupportConnected
    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(3, 4);
        assertTrue(distribution.isSupportConnected());
    }
}