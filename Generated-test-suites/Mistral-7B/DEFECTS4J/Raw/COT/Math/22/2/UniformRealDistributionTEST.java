import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionConstruction() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0, distribution.getSupportLowerBound(), 0.01);
        assertEquals(1, distribution.getSupportUpperBound(), 0.01);

        UniformRealDistribution distributionCustom = new UniformRealDistribution(1, 2);
        assertEquals(1, distributionCustom.getSupportLowerBound(), 0.01);
        assertEquals(2, distributionCustom.getSupportUpperBound(), 0.01);

        Well19937c rng = new Well19937c();
        UniformRealDistribution distributionRNG = new UniformRealDistribution(rng, 1, 2);
        assertEquals(1, distributionRNG.getSupportLowerBound(), 0.01);
        assertEquals(2, distributionRNG.getSupportUpperBound(), 0.01);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution();

        for (double x = 0.0; x <= 1.0; x += 0.01) {
            assertEquals(1.0 / (1.0 - 0.0), distribution.density(x), 0.001);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution();

        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cdf = distribution.cumulativeProbability(x);
            assertEquals(x, cdf, 0.001);
        }
    }

    @Test
    public void testNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.5, distribution.getNumericalMean(), 0.001);

        UniformRealDistribution distributionCustom = new UniformRealDistribution(1, 2);
        assertEquals(1.5, distributionCustom.getNumericalMean(), 0.001);
    }

    @Test
    public void testNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.008333333333333333, distribution.getNumericalVariance(), 0.0001);

        UniformRealDistribution distributionCustom = new UniformRealDistribution(1, 2);
        assertEquals(0.1666666666666667, distributionCustom.getNumericalVariance(), 0.0001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testNumberIsTooLargeException() {
        UniformRealDistribution distribution = new UniformRealDistribution(2, 1);
    }

    @Test
    public void testSample() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= distribution.getSupportLowerBound() && sample < distribution.getSupportUpperBound());
        }
    }

}