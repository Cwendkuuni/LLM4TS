import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.FDistributionTest;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest extends FDistribution {

    @Test
    public void testFDistributionConstruction() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertEquals(2.0, fd.getNumeratorDegreesOfFreedom(), 0.001);
        assertEquals(3.0, fd.getDenominatorDegreesOfFreedom(), 0.001);
        assertInstanceOf(FDistribution.class, fd);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() {
        new FDistribution(-1.0, 1.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDegreesOfFreedom() {
        new FDistribution(0.0, 1.0);
    }

    @Test
    public void testFDistributionDensity() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double x = 1.5;
        double expected = 0.4176698098384846;
        double result = fd.density(x);
        assertEquals(expected, result, 0.001);
    }

    @Test
    public void testFDistributionInverseCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double p = 0.95;
        double x = fd.inverseCumulativeProbability(p);
        double expected = 2.480654133680578;
        assertEquals(expected, x, 0.001);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double x = 1.5;
        double p = fd.cumulativeProbability(x);
        double expected = 0.9202192196228109;
        assertEquals(expected, p, 0.001);
    }

    @Test
    public void testFDistributionVariance() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double variance = fd.getNumericalVariance();
        double expected = 1.3164558543952363;
        assertEquals(expected, variance, 0.001);
    }

    @Test
    public void testFDistributionMean() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double mean = fd.getNumericalMean();
        double expected = 1.5;
        assertEquals(expected, mean, 0.001);
    }

    @Test
    public void testFDistributionZeroParameters() {
        FDistribution fd = new FDistribution(0.0, 0.0);
        assertNull(fd);
    }

    @Test
    public void testFDistributionNegativeValues() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double x = -1.0;
        double result = fd.density(x);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testFDistributionNaNValues() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double x = Double.NaN;
        double result = fd.density(x);
        assertEquals(0.0, result, 0.001);
    }

    @Test
    public void testFDistributionNegativeParameters() {
        FDistribution fd = new FDistribution(-2.0, 3.0);
        assertInstanceOf(NotStrictlyPositiveException.class, fd);
    }

    @Test
    public void testFDistributionZeroParametersDenominator() {
        FDistribution fd = new FDistribution(2.0, 0.0);
        assertInstanceOf(NotStrictlyPositiveException.class, fd);
    }

    @Test
    public void testFDistributionZeroParametersNumerator() {
        FDistribution fd = new FDistribution(0.0, 2.0);
        assertInstanceOf(NotStrictlyPositiveException.class, fd);
    }

    @Test
    public void testFDistributionSupport() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double x = fd.getSupportLowerBound();
        assertEquals(0.0, x, 0.001);

        x = fd.getSupportUpperBound();
        assertEquals(Double.POSITIVE_INFINITY, x, 0.001);

        assertFalse(fd.isSupportLowerBoundInclusive());
        assertFalse(fd.isSupportUpperBoundInclusive());
        assertTrue(fd.isSupportConnected());
    }

    @Test
    public void testFDistributionRandomGeneration() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        RandomGenerator rg = fd.getRandomGenerator();
        double x;
        for (int i = 0; i < 10000; i++) {
            x = rg.nextDouble();
            assertTrue(x >= 0);
            assertTrue(x <= 1);
        }
    }

    @Test
    public void testFDistributionBetaFunction() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double x = fd.cumulativeProbability(0.5);
        double expected = Beta.regularizedBeta(1.0, 1.5);
        assertEquals(expected, x, 0.001);
    }
}