import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getLowerBound(), 0.0);
        assertEquals(1.0, distribution.getUpperBound(), 0.0);
        assertEquals(0.5, distribution.getNumericalMean(), 0.0);
        assertEquals(0.25, distribution.getNumericalVariance(), 0.0);
    }

    @Test
    public void testUniformRealDistributionCustomConstructor() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 3.0);
        assertEquals(1.0, distribution.getLowerBound(), 0.0);
        assertEquals(3.0, distribution.getUpperBound(), 0.0);
        assertEquals(2.0, distribution.getNumericalMean(), 0.0);
        assertEquals(0.75, distribution.getNumericalVariance(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionInvalidLowerThanUpper() {
        new UniformRealDistribution(3.0, 1.0);
    }

    @Test
    public void testUniformRealDistributionCustomConstructorWithAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(1.0, 3.0, 0.0001);
        assertEquals(1.0, distribution.getLowerBound(), 0.0001);
        assertEquals(3.0, distribution.getUpperBound(), 0.0001);
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        for (int i = 0; i < 100; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= 0.0 && sample <= 1.0);
        }
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double density = distribution.density(x);
            assertEquals(1.0 / (1.0 - 0.0), density, 0.0001);
        }
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        for (double x = 0.0; x <= 1.0; x += 0.01) {
            double cumProb = distribution.cumulativeProbability(x);
            assertEquals(x, cumProb, 0.0001);
        }
    }
}