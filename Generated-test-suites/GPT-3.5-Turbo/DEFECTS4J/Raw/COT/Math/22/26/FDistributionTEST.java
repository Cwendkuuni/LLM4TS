import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Before;
import org.junit.Test;

public class FDistributionTest {
    private FDistribution fd;

    @Before
    public void setUp() {
        fd = new FDistribution(2, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeNumeratorDegreesOfFreedom() {
        fd = new FDistribution(-2, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeDenominatorDegreesOfFreedom() {
        fd = new FDistribution(2, -3);
    }

    @Test
    public void testDensity() {
        assertEquals(0.0, fd.density(0.0), 0);
        assertEquals(0.21460183660255172, fd.density(1.0), 1e-6);
        assertEquals(0.0, fd.density(-1.0), 0);
    }

    @Test
    public void testCumulativeProbability() {
        assertEquals(0.0, fd.cumulativeProbability(0.0), 0);
        assertEquals(0.24242424242424243, fd.cumulativeProbability(1.0), 1e-6);
        assertEquals(0.0, fd.cumulativeProbability(-1.0), 0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        assertEquals(2, fd.getNumeratorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        assertEquals(3, fd.getDenominatorDegreesOfFreedom(), 0);
    }

    @Test
    public void testGetNumericalMean() {
        assertEquals(3, fd.getNumericalMean(), 0);
    }

    @Test
    public void testGetNumericalVariance() {
        assertEquals(Double.NaN, fd.getNumericalVariance(), 0);
    }

    @Test
    public void testGetSupportLowerBound() {
        assertEquals(0, fd.getSupportLowerBound(), 0);
    }

    @Test
    public void testGetSupportUpperBound() {
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        assertTrue(fd.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        assertTrue(!fd.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        assertTrue(fd.isSupportConnected());
    }
}