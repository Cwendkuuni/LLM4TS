import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionCreation() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
        assertNotNull(fd);
        assertEquals(numeratorDegreesOfFreedom, fd.getNumeratorDegreesOfFreedom(), 0.0);
        assertEquals(denominatorDegreesOfFreedom, fd.getDenominatorDegreesOfFreedom(), 0.0);
    }

    @Test
    public void testFDistributionDensity() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double x = 1.5;
        double density = fd.density(x);
        assertEquals(expectedDensity(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, x), density, 0.001);
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double x = 1.5;
        double cdf = fd.cumulativeProbability(x);
        assertEquals(expectedCDF(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom, x), cdf, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeNumeratorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = -2.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeDenominatorDegreesOfFreedom() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = -2.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
    }

    @Test
    public void testFDistributionSupport() {
        double numeratorDegreesOfFreedom = 3.0;
        double denominatorDegreesOfFreedom = 5.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double lowerBound = fd.getSupportLowerBound();
        double upperBound = fd.getSupportUpperBound();

        assertTrue(lowerBound >= 0);
        assertTrue(upperBound > 0);
    }

    @Test
    public void testFDistributionGetNumericalMean() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 7.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double mean = fd.getNumericalMean();
        double expectedMean = numeratorDegreesOfFreedom / (numeratorDegreesOfFreedom - 2);
        assertEquals(expectedMean, mean, 0.001);
    }

    @Test
    public void testFDistributionGetNumericalVariance() {
        double numeratorDegreesOfFreedom = 5.0;
        double denominatorDegreesOfFreedom = 7.0;
        FDistribution fd = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);

        double variance = fd.getNumericalVariance();
        double expectedVariance = (2 * Math.pow(denominatorDegreesOfFreedom, 2) *
                                  (numeratorDegreesOfFreedom + denominatorDegreesOfFreedom - 2)) /
                                 ((numeratorDegreesOfFreedom * (denominatorDegreesOfFreedom - 2) *
                                   (denominatorDegreesOfFreedom - 4)));
        assertEquals(expectedVariance, variance, 0.001);
    }

    private double expectedCDF(double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom, double x) {
        return Beta.regularizedBeta((numeratorDegreesOfFreedom * x) / (denominatorDegreesOfFreedom + numeratorDegreesOfFreedom * x),
                                0.5 * numeratorDegreesOfFreedom, 0.5 * denominatorDegreesOfFreedom);
    }

    private double expectedDensity(double numeratorDegreesOfFreedom, double denominatorDegreesOfFreedom, double x) {
        double nhalf = numeratorDegreesOfFreedom / 2;
        double mhalf = denominatorDegreesOfFreedom / 2;
        double logx = Math.log(x);
        double logn = Math.log(numeratorDegreesOfFreedom);
        double logm = Math.log(denominatorDegreesOfFreedom);
        double lognxm = Math.log(numeratorDegreesOfFreedom * x + denominatorDegreesOfFreedom);

        return Math.exp(nhalf * logn + nhalf * logx - logx +
                        mhalf * logm - nhalf * lognxm - mhalf * lognxm - Beta.logBeta(nhalf, mhalf));
    }
}