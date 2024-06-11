import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionConstruction() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);

        UniformRealDistribution dist1 = new UniformRealDistribution(0.1, 0.2);
        assertEquals(0.1, dist1.getSupportLowerBound(), 0.0);
        assertEquals(0.2, dist1.getSupportUpperBound(), 0.0);

        UniformRealDistribution dist2 = new UniformRealDistribution(0.1, 0.2, 0.001);
        assertEquals(0.1, dist2.getSupportLowerBound(), 0.0);
        assertEquals(0.2, dist2.getSupportUpperBound(), 0.0);
        assertEquals(0.001, dist2.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidConstruction() {
        new UniformRealDistribution(1.0, 1.0);
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = dist.density(x);
            double expected = 1.0 / (1.0 - 0.0);
            assertEquals(expected, density, 0.001);
        }
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cumProb = dist.cumulativeProbability(x);
            double expected = x / (1.0 - 0.0);
            assertEquals(expected, cumProb, 0.001);
        }
    }

    @Test
    public void testUniformRealDistributionGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        double expected = 0.5;
        assertEquals(expected, dist.getNumericalMean(), 0.0);
    }

    @Test
    public void testUniformRealDistributionGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        double expected = (1.0 - 0.0) * (1.0 - 0.0) / 12;
        assertEquals(expected, dist.getNumericalVariance(), 0.0);
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        double lower = 0.0;
        double upper = 1.0;
        int numTrials = 10000;
        Well19937c rng = new Well19937c();
        for (int i = 0; i < numTrials; i++) {
            double sample = dist.sample(rng);
            assertTrue(sample >= lower && sample <= upper);
        }
    }

}