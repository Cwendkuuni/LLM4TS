import org.junit.Test;
import org.apache.commons.math3.distribution.FDistribution;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(0.056170291, fd.density(1), 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(0.478410214, fd.cumulativeProbability(1), 1e-9);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(3, fd.getNumeratorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(5, fd.getDenominatorDegreesOfFreedom(), 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(2.5, fd.getNumericalMean(), 1e-9);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(10, fd.getNumericalVariance(), 1e-9);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(0, fd.getSupportLowerBound(), 1e-9);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(3, 5);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 1e-9);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution fd = new FDistribution(3, 5);
        assertTrue(fd.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution fd = new FDistribution(3, 5);
        assertFalse(fd.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution fd = new FDistribution(3, 5);
        assertTrue(fd.isSupportConnected());
    }
}