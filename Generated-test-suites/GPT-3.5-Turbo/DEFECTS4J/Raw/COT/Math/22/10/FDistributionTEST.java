import org.junit.Test;
import org.apache.commons.math3.distribution.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(3, 4);
        double result = fd.density(1.5);
        assertEquals(0.2222222222222222, result, 0.0000001);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(3, 4);
        double result = fd.cumulativeProbability(1.5);
        assertEquals(0.7797335667033354, result, 0.0000001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(3, 4);
        double result = fd.getNumeratorDegreesOfFreedom();
        assertEquals(3, result, 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(3, 4);
        double result = fd.getDenominatorDegreesOfFreedom();
        assertEquals(4, result, 0);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(3, 4);
        double result = fd.getNumericalMean();
        assertEquals(2.6666666666666665, result, 0.0000001);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(3, 4);
        double result = fd.getNumericalVariance();
        assertEquals(3.130434782608696, result, 0.0000001);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(3, 4);
        double result = fd.getSupportLowerBound();
        assertEquals(0, result, 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(3, 4);
        double result = fd.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, result, 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(3, 4);
        boolean result = fd.isSupportLowerBoundInclusive();
        assertTrue(result);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(3, 4);
        boolean result = fd.isSupportUpperBoundInclusive();
        assertFalse(result);
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(3, 4);
        boolean result = fd.isSupportConnected();
        assertTrue(result);
    }

}