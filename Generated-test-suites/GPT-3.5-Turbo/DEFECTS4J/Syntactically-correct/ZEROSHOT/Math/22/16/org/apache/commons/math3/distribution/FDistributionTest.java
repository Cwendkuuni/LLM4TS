import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution dist = new FDistribution(3, 5);

        double result = dist.density(2);

        assertEquals(0.1247003417, result, 0.0000000001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution dist = new FDistribution(3, 5);

        double result = dist.cumulativeProbability(2);

        assertEquals(0.904975848, result, 0.000000001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(3, 5);

        double result = dist.getNumeratorDegreesOfFreedom();

        assertEquals(3, result, 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution dist = new FDistribution(3, 5);

        double result = dist.getDenominatorDegreesOfFreedom();

        assertEquals(5, result, 0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution dist = new FDistribution(3, 5);

        double result = dist.getNumericalMean();

        assertEquals(1.25, result, 0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution dist = new FDistribution(3, 5);

        double result = dist.getNumericalVariance();

        assertEquals(4.6875, result, 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution dist = new FDistribution(3, 5);

        double result = dist.getSupportLowerBound();

        assertEquals(0, result, 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution dist = new FDistribution(3, 5);

        double result = dist.getSupportUpperBound();

        assertEquals(Double.POSITIVE_INFINITY, result, 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution dist = new FDistribution(3, 5);

        boolean result = dist.isSupportLowerBoundInclusive();

        assertTrue(result);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution dist = new FDistribution(3, 5);

        boolean result = dist.isSupportUpperBoundInclusive();

        assertFalse(result);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution dist = new FDistribution(3, 5);

        boolean result = dist.isSupportConnected();

        assertTrue(result);
    }
}