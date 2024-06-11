import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.special.Beta;
import org.junit.Test;
import static org.junit.Assert.*;

public class FDistributionTest {

    @Test
    public void testFDistributionConstruction() {
        try {
            final double numeratorDegreesOfFreedom = 3.0;
            final double denominatorDegreesOfFreedom = 5.0;
            FDistribution fDistribution = new FDistribution(numeratorDegreesOfFreedom, denominatorDegreesOfFreedom);
            assertEquals("Expected numerator degrees of freedom: " + numeratorDegreesOfFreedom, numeratorDegreesOfFreedom, fDistribution.getNumeratorDegreesOfFreedom());
            assertEquals("Expected denominator degrees of freedom: " + denominatorDegreesOfFreedom, denominatorDegreesOfFreedom, fDistribution.getDenominatorDegreesOfFreedom());
        } catch (NotStrictlyPositiveException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFDistributionDensity() {
        final FDistribution fDistribution = new FDistribution(3.0, 5.0);
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            final double density = fDistribution.density(x);
            final double expectedDensity = 1.0 / (Beta.beta(3.0, 5.0) * Math.pow(x, 2.5) * Math.pow(5.0, 2.5) * Math.pow(2.0, 4.5) * Math.pow(Math.PI, 1.5) * Math.sin(Math.PI / 4.0) * Math.sin(Math.PI / 4.0));
            assertEquals("Expected density for x = " + x + ": " + expectedDensity, expectedDensity, density, 0.001);
        }
    }

    @Test
    public void testFDistributionCumulativeProbability() {
        final FDistribution fDistribution = new FDistribution(3.0, 5.0);
        for (double x = 0.0; x <= 5.0; x += 0.1) {
            final double cumulativeProbability = fDistribution.cumulativeProbability(x);
            final double expectedCumulativeProbability = Beta.regularizedBeta(3.0 * x / 8.0, 3.0 / 2.0, 5.0 / 2.0);
            assertEquals("Expected cumulative probability for x = " + x + ": " + expectedCumulativeProbability, expectedCumulativeProbability, cumulativeProbability, 0.001);
        }
    }

    @Test
    public void testFDistributionNumericalMean() {
        final FDistribution fDistribution = new FDistribution(3.0, 5.0);
        double mean = 0.0;
        for (int i = 0; i < 100000; i++) {
            mean += fDistribution.density(FastMath.nextGaussian());
        }
        mean /= 100000.0;
        final double expectedMean = 5.0 / 2.0;
        assertEquals("Expected mean: " + expectedMean, expectedMean, fDistribution.getNumericalMean(), 0.001);
        assertEquals("Expected mean from simulation: " + mean, expectedMean, mean, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionNegativeNumeratorDegreesOfFreedom() {
        new FDistribution(-3.0, 5.0);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFDistributionZeroOrNegativeDenominatorDegreesOfFreedom() {
        new FDistribution(3.0, -5.0);
    }
}