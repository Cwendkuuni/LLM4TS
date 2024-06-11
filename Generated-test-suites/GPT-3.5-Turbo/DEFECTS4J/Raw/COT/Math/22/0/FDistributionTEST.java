import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.RandomDataGenerator;

import static org.junit.Assert.*;
import org.junit.Test;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(3, 5);
        assertEquals(0.1692730101, distribution.density(1), 1e-9);
        assertEquals(0, distribution.density(0), 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(3, 5);
        assertEquals(0.6802494311, distribution.cumulativeProbability(1), 1e-9);
        assertEquals(0, distribution.cumulativeProbability(0), 1e-9);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3, 5);
        assertEquals(3, distribution.getNumeratorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3, 5);
        assertEquals(5, distribution.getDenominatorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(3, 5);
        assertEquals(2.5, distribution.getNumericalMean(), 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(3, 5);
        assertEquals(10, distribution.getNumericalVariance(), 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(3, 5);
        assertEquals(0, distribution.getSupportLowerBound(), 1e-9);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(3, 5);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), 1e-9);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(3, 5);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(3, 5);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(3, 5);
        assertTrue(distribution.isSupportConnected());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNumeratorDegreesOfFreedomLessThanEqualToZero() {
        FDistribution distribution = new FDistribution(0, 5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testDenominatorDegreesOfFreedomLessThanEqualToZero() {
        FDistribution distribution = new FDistribution(3, 0);
    }

}