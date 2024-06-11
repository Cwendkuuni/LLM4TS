import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class FDistributionTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNonPositiveNumeratorDegreesOfFreedom() {
        new FDistribution(-1.0, 2.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNonPositiveDenominatorDegreesOfFreedom() {
        new FDistribution(2.0, -1.0);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertEquals(3.0, distribution.getNumeratorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertEquals(5.0, distribution.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testDensity() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double result = distribution.density(1.5);
        assertEquals(0.16980617913825847, result, 1e-9);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double result = distribution.cumulativeProbability(1.5);
        assertEquals(0.9318136208368945, result, 1e-9);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertEquals(3.2, distribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        double result = distribution.getNumericalVariance();
        assertEquals(0.3571211542348373, result, 0.0);
    }

    @Test
    public void testCalculateNumericalVariance() {
        FDistribution distribution = new FDistribution(3.0, 7.0);
        double result = distribution.calculateNumericalVariance();
        assertEquals(0.5294986792462165, result, 0.0);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertEquals(0.0, distribution.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertEquals(Double.POSITIVE_INFINITY, distribution.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        FDistribution distribution = new FDistribution(3.0, 5.0);
        assertTrue(distribution.isSupportConnected());
    }
}