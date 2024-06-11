import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionConstructor() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(3.0, 5.0);
        assertEquals(3.0, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(5.0, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionInverseCumulativeProbability() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double p = 0.95;
        double result = fd.inverseCumulativeProbability(p);
        double expected = Precision.round(fd.cumulativeProbability(result), 5);
        assertEquals(expected, p, 0.00001);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double x = 1.5;
        double expected = fd.density(x);
        double result = fd.density(x);
        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testFDistributionMean() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        double mean = fd.getNumericalMean();
        assertEquals(5.5, mean, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionInvalidArguments() throws NotStrictlyPositiveException {
        new FDistribution(-2.0, 5.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeNumeratorDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(0.0, 5.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDenominatorDegreesOfFreedom() throws NotStrictlyPositiveException {
        new FDistribution(3.0, 0.0);
    }

    @Test
    public void testFDistributionVariance() {
        FDistribution fd = new FDistribution(5.0, 3.0);
        double variance = fd.getNumericalVariance();
        assertEquals(1.385538223863, variance, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionInvalidVarianceCalculation() throws NotStrictlyPositiveException {
        FDistribution fd = new FDistribution(3.0, 2.0);
        fd.calculateNumericalVariance();
    }

    @Test
    public void testFDistributionSupportBounds() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        assertEquals(0.0, fd.getSupportLowerBound(), 0.0);
        assertEquals(Double.POSITIVE_INFINITY, fd.getSupportUpperBound(), 0.0);
        assertTrue(fd.isSupportLowerBoundInclusive());
        assertFalse(fd.isSupportUpperBoundInclusive());
    }

    @Test
    public void testFDistributionSupportConnected() {
        FDistribution fd = new FDistribution(3.0, 5.0);
        assertTrue(fd.isSupportConnected());
    }
}