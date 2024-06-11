import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionConstruction() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertNotNull(dist);
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);

        UniformRealDistribution distCustom = new UniformRealDistribution(1.5, 2.5);
        assertNotNull(distCustom);
        assertEquals(1.5, distCustom.getLowerBound(), 0.0);
        assertEquals(2.5, distCustom.getUpperBound(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionExceptionLowerThanUpper() {
        new UniformRealDistribution(2.0, 1.5);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.5, 1.5);
        for (double i = 0.5; i < 1.5; i += 0.01) {
            double density = dist.density(i);
            assertEquals(1.0 / (1.5 - 0.5), density, 0.01);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.5, 1.5);
        for (double i = 0.5; i < 1.5; i += 0.01) {
            double cumProb = dist.cumulativeProbability(i);
            assertEquals(i - 0.5 / (1.5 - 0.5), cumProb, 0.01);
        }
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0.5, 1.5);
        assertEquals(0.75, dist.getNumericalMean(), 0.01);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0.5, 1.5);
        assertEquals(0.022222222222222226, dist.getNumericalVariance(), 0.0001);
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0.5, 1.5);
        for (int i = 0; i < 10; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0.5 && sample <= 1.5);
        }

        Well19937c rng = new Well19937c();
        UniformRealDistribution distCustomRng = new UniformRealDistribution(rng, 0.5, 1.5);
        for (int i = 0; i < 10; i++) {
            double sample = distCustomRng.sample();
            assertTrue(sample >= 0.5 && sample <= 1.5);
        }
    }

}