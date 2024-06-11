import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FDistributionTest {
    private FDistribution fDistribution;

    @Before
    public void setUp() {
        fDistribution = new FDistribution(1, 2);
    }

    @Test
    public void testDensity() {
        double result = fDistribution.density(1);
        assertEquals(0.198943, result, 1e-6);
    }

    @Test
    public void testCumulativeProbability() {
        double result = fDistribution.cumulativeProbability(1);
        assertEquals(0.606531, result, 1e-6);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        double result = fDistribution.getNumeratorDegreesOfFreedom();
        assertEquals(1, result, 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        double result = fDistribution.getDenominatorDegreesOfFreedom();
        assertEquals(2, result, 0);
    }

    @Test
    public void testGetNumericalMean() {
        double result = fDistribution.getNumericalMean();
        assertEquals(2, result, 0);
    }

    @Test
    public void testGetNumericalVariance() {
        double result = fDistribution.getNumericalVariance();
        assertEquals(Double.NaN, result, 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        double result = fDistribution.getSupportLowerBound();
        assertEquals(0, result, 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        double result = fDistribution.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, result, 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        boolean result = fDistribution.isSupportLowerBoundInclusive();
        assertTrue(result);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        boolean result = fDistribution.isSupportUpperBoundInclusive();
        assertFalse(result);
    }

    @Test
    public void testIsSupportConnected() {
        boolean result = fDistribution.isSupportConnected();
        assertTrue(result);
    }
}