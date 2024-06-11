import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistribution() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 7.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        assertEquals(numeratorDegreesOfFreedom, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fd.getDenominatorDegreesOfFreedom(), 0.0);

        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0.0);
        assertEquals(0.0, fd.getSupportLowerBound(), 0.0);
        assertTrue(fd.isSupportLowerBoundInclusive());
        assertFalse(fd.isSupportUpperBoundInclusive());
        assertTrue(fd.isSupportConnected());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeDegreesOfFreedom() throws NotStrictlyPositiveException {
        double numeratorDegreesOfFreedom = -2.0;
        double denominatorDegreesOfFreedom = 4.0;
        new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    @Test
    public void testDensity() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 7.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double x = 1.5;
        double expected = 0.133970541474974;
        double actual = fd.density(x);
        assertEquals(expected, actual, 1e-8);
    }

    @Test
    public void testCumulativeProbability() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 7.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double x = 2.5;
        double expected = 0.959892493491141;
        double actual = fd.cumulativeProbability(x);
        assertEquals(expected, actual, 1e-8);
    }

    @Test
    public void testGetNumericalMean() {
        double numeratorDegreesOfFreedom = 10.0;
        double denominatorDegreesOfFreedom = 20.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double expected = 5.0 / 3.0;
        double actual = fd.getNumericalMean();
        assertEquals(expected, actual, 1e-8);
    }

    @Test
    public void testGetNumericalVariance() {
        double numeratorDegreesOfFreedom = 10.0;
        double denominatorDegreesOfFreedom = 20.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double expected = 2.2829364513256;
        double actual = fd.getNumericalVariance();
        assertEquals(expected, actual, 1e-8);
    }
}