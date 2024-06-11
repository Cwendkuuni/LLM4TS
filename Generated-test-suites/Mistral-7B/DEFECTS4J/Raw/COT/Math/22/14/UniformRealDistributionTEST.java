import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionConstructorWithDefaultValues() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(0.0, distribution.getLowerBound(), 0.0);
        assertEquals(1.0, distribution.getUpperBound(), 0.0);
        assertNotNull(distribution.getRandomGenerator());
        assertInstanceOf(Well19937c.class, distribution.getRandomGenerator());
    }

    @Test
    public void testUniformRealDistributionConstructorWithCustomValues() {
        UniformRealDistribution distribution = new UniformRealDistribution(5.0, 10.0);
        assertEquals(5.0, distribution.getLowerBound(), 0.0);
        assertEquals(10.0, distribution.getUpperBound(), 0.0);
        assertNotNull(distribution.getRandomGenerator());
        assertInstanceOf(Well19937c.class, distribution.getRandomGenerator());
    }

    @Test
    public void testUniformRealDistributionConstructorWithInvalidValues() {
        try {
            new UniformRealDistribution(10.0, 10.0);
            fail("Expected NumberIsTooLargeException");
        } catch (NumberIsTooLargeException e) {
            // expected
        }
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = distribution.getLowerBound(); x < distribution.getUpperBound(); x++) {
            double density = distribution.density(x);
            double expectedDensity = 1.0 / (distribution.getUpperBound() - distribution.getLowerBound());
            assertEquals(expectedDensity, density, 0.001);
        }

        for (double x = distribution.getUpperBound(); x > distribution.getLowerBound(); x--) {
            double density = distribution.density(x);
            assertEquals(0.0, density, 0.001);
        }
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (double x = distribution.getLowerBound(); x <= distribution.getUpperBound(); x += 0.01) {
            double cumulativeProbability = distribution.cumulativeProbability(x);
            double expectedCumulativeProbability = x - distribution.getLowerBound() / (distribution.getUpperBound() - distribution.getLowerBound());
            assertEquals(expectedCumulativeProbability, cumulativeProbability, 0.001);
        }
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        for (int i = 0; i < 1000; i++) {
            double sample = distribution.sample();
            assertTrue(sample >= distribution.getLowerBound());
            assertTrue(sample < distribution.getUpperBound());
        }
    }

    @Test
    public void testUniformRealDistributionGetSolverAbsoluteAccuracy() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(UniformRealDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY, distribution.getSolverAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testUniformRealDistributionGetNumericalMean() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.5, distribution.getNumericalMean(), 0.0);
    }

    @Test
    public void testUniformRealDistributionGetNumericalVariance() {
        UniformRealDistribution distribution = new UniformRealDistribution(0.0, 1.0);
        assertEquals(0.005555555555555555, distribution.getNumericalVariance(), 0.0);
    }

    @Test
    public void testUniformRealDistributionGetSupportLowerBound() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(distribution.getLowerBound(), distribution.getSupportLowerBound());
    }

    @Test
    public void testUniformRealDistributionGetSupportUpperBound() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertEquals(distribution.getUpperBound(), distribution.getSupportUpperBound());
    }

    @Test
    public void testUniformRealDistributionIsSupportLowerBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertTrue(distribution.isSupportLowerBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportUpperBoundInclusive() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertFalse(distribution.isSupportUpperBoundInclusive());
    }

    @Test
    public void testUniformRealDistributionIsSupportConnected() {
        UniformRealDistribution distribution = new UniformRealDistribution();
        assertTrue(distribution.isSupportConnected());
    }
}