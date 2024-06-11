import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.RandomUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        
        UniformRealDistribution distCustom = new UniformRealDistribution(2.5, 5.5);
        assertEquals(2.5, distCustom.getLowerBound(), 0.0);
        assertEquals(5.5, distCustom.getUpperBound(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionConstructorThrowsExceptionWhenLowerBoundIsGreaterThanUpperBound() {
        new UniformRealDistribution(5.0, 4.0);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = 0.0; x < 1.0; x += 0.01) {
            double density = dist.density(x);
            assertEquals(0.001, density, 0.0001);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (double x = 0.0; x < 1.0; x += 0.01) {
            double cdf = dist.cumulativeProbability(x);
            assertEquals(x, cdf, 0.001);
        }
    }

    @Test
    public void testInvalidInputForCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution();
        try {
            dist.cumulativeProbability(2.0);
            fail();
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getLowerBound() && sample < dist.getUpperBound());
        }
    }

    @Test
    public void testGetNumericalMean() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.5, dist.getNumericalMean(), 0.001);
    }

    @Test
    public void testGetNumericalVariance() {
        UniformRealDistribution dist = new UniformRealDistribution();
        double variance = dist.getNumericalVariance();
        assertEquals(0.0625, variance, 0.01);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportLowerBoundInclusive());
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertFalse(dist.isSupportUpperBoundInclusive());
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertTrue(dist.isSupportConnected());
    }
}