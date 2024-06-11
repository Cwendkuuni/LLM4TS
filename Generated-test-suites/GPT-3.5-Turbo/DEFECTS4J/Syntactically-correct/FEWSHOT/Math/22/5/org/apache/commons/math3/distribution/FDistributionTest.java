import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;

public class FDistributionTest {
    private FDistribution distribution;

    @Before
    public void setUp() {
        distribution = new FDistribution(2.0, 3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeNumeratorDegreesOfFreedom() {
        new FDistribution(-2.0, 3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeDenominatorDegreesOfFreedom() {
        new FDistribution(2.0, -3.0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        assertEquals(2.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        assertEquals(3.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testDensity() {
        assertEquals(0.0, distribution.density(0), 0.0);
        assertEquals(0.42621329349206254, distribution.density(1), 1e-15);
        assertEquals(0.1930647057070912, distribution.density(2), 1e-15);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, distribution.cumulativeProbability(0), 0.0);
        assertEquals(0.08838834764831843, distribution.cumulativeProbability(1), 1e-15);
        assertEquals(0.2710815852462633, distribution.cumulativeProbability(2), 1e-15);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(3.0, distribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(18.0, distribution.getNumericalVariance(), 0.0);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(distribution.isSupportConnected());
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        assertEquals(1e-9, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testConstructorWithRandomGenerator() {
        assertNotNull(new FDistribution(new SeededRandomGenerator(), 2.0, 3.0, 1e-9));
    }
}