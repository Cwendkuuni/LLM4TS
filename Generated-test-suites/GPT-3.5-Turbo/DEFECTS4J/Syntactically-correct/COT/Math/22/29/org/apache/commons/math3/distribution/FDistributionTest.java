
import static org.junit.Assert.*;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;

public class FDistributionTest {

    private FDistribution fdistribution;

    @Before
    public void setUp() {
        fdistribution = new FDistribution(2, 3);
    }

    @Test
    public void testDensity() {
        assertEquals(0.301652174, fdistribution.density(0.5), 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0, fdistribution.cumulativeProbability(0), 1e-9);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        assertEquals(2.0, fdistribution.getNumeratorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        assertEquals(3.0, fdistribution.getDenominatorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(3.0, fdistribution.getNumericalMean(), 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(18.0, fdistribution.getNumericalVariance(), 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0.0, fdistribution.getSupportLowerBound(), 1e-9);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(Double.POSITIVE_INFINITY, fdistribution.getSupportUpperBound(), 1e-9);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(fdistribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertFalse(fdistribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(fdistribution.isSupportConnected());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructor() {
        new FDistribution(0, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidConstructor2() {
        new FDistribution(2, 0);
    }
}
