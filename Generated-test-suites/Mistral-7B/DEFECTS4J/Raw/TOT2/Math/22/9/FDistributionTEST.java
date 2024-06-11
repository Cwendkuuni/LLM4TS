import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionConstruction() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        assertNotNull(fd);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDegreesOfFreedom() {
        new FDistribution(-1.0, 3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroNumeratorDegreesOfFreedom() {
        new FDistribution(0.0, 3.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroDenominatorDegreesOfFreedom() {
        new FDistribution(2.0, 0.0);
    }

    @Test
    public void testDensity() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double density = fd.density(1.5);
        assertTrue("Density should be positive", density > 0);
    }

    @Test
    public void testCumulativeProbability() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double x = 1.5;
        double p = fd.cumulativeProbability(x);
        double expected = Beta.regularizedBeta((2.0 * x) / (3.0 + 2.0 * x), 1.0, 1.5);
        assertEquals("Cumulative probability should match the theoretical value", expected, p, 0.001);
    }

    @Test
    public void testGetNumeratorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double degreesOfFreedom = fd.getNumeratorDegreesOfFreedom();
        assertEquals("Numerator degrees of freedom should be 2", 2.0, degreesOfFreedom, 0.0);
    }

    @Test
    public void testGetDenominatorDegreesOfFreedom() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double degreesOfFreedom = fd.getDenominatorDegreesOfFreedom();
        assertEquals("Denominator degrees of freedom should be 3", 3.0, degreesOfFreedom, 0.0);
    }

    @Test
    public void testGetSolverAbsoluteAccuracy() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double accuracy = fd.getSolverAbsoluteAccuracy();
        assertEquals("Solver absolute accuracy should be the default value (1e-9)", 1e-9, accuracy, 1e-12);
    }

    @Test
    public void testGetNumericalMean() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double mean = fd.getNumericalMean();
        assertEquals("Mean should be 3/2", 1.5, mean, 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double variance = fd.getNumericalVariance();
        double expected = (2.0 * 3.0 * 3.0 * 3.0) / (2.0 * (3.0 - 2.0) * (3.0 - 4.0));
        assertEquals("Variance should match the theoretical value", expected, variance, 0.001);
    }

    @Test
    public void testGetSupportLowerBound() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double lowerBound = fd.getSupportLowerBound();
        assertEquals("Lower bound should be 0", 0.0, lowerBound, 1e-12);
    }

    @Test
    public void testGetSupportUpperBound() {
        FDistribution fd = new FDistribution(2.0, 3.0);
        double upperBound = fd.getSupportUpperBound();
        assertEquals("Upper bound should be positive infinity", Double.POSITIVE_INFINITY, upperBound);
    }

    @Test
    public void testSupportIsConnected() {
        FDistribution fd = new FDistribution(2.0, 3.0);
       assertTrue("Support should be connected", fd.isSupportConnected());
    }

    @Test
    public void testSample() {
        FDistribution fd = new FDistribution(new Well19937c(), 2.0, 3.0);
        double x = fd.inverseCumulativeProbability(0.25);
        double density = fd.density(x);
        double mean = fd.getNumericalMean();
        double variance = fd.getNumericalVariance();
        assertEquals("Sample should be close to the mean", mean, x, 0.01);
        assertTrue("Density should be close to the theoretical value",
                Precision.isClose(fd.density(x), density, 0.001));
        assertTrue("Variance should be close to the theoretical value",
                Precision.isClose(Math.pow(x - mean, 2), variance, 0.01));
    }
}