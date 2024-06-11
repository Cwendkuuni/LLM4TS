import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.RandomUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(1.0, dist.getSupportUpperBound(), 0.0);
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 0.0 && sample < 1.0);
        }
    }

    @Test
    public void testConstructWithLowerAndUpperBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        assertEquals(1.0, dist.getSupportLowerBound(), 0.0);
        assertEquals(2.0, dist.getSupportUpperBound(), 0.0);
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 1.0 && sample < 2.0);
        }
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testConstructWithLowerGreaterThanUpperBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(2.0, 1.0);
    }

    @Test
    public void testDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        for (double x = 1.0; x < 2.0; x += 0.01) {
            double density = dist.density(x);
            double expected = 1.0 / (dist.getSupportUpperBound() - dist.getSupportLowerBound());
            assertEquals(expected, density, 0.0001);
        }
    }

    @Test
    public void testCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        for (double x = 1.0; x < 2.0; x += 0.01) {
            double cumProb = dist.cumulativeProbability(x);
            double expected = x - dist.getSupportLowerBound() / (dist.getSupportUpperBound() - dist.getSupportLowerBound());
            assertEquals(expected, cumProb, 0.0001);
        }
    }

    @Test
    public void testSample() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        for (int i = 0; i < 100; i++) {
            double sample = dist.sample();
            assertTrue(sample >= 1.0 && sample < 2.0);
        }
    }

    @Test
    public void testSampleMultipleTimes() {
        UniformRealDistribution dist = new UniformRealDistribution(0.0, 1.0);
        double[] samples = new double[1000];
        for (int i = 0; i < 1000; i++) {
            samples[i] = dist.sample();
        }
        for (int i = 0; i < 1000; i++) {
            assertTrue(samples[i] >= 0.0 && samples[i] <= 1.0);
        }
    }

    @Test
    public void testMean() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        double mean = dist.getNumericalMean();
        double expected = (dist.getSupportLowerBound() + dist.getSupportUpperBound()) / 2.0;
        assertEquals(expected, mean, 0.0001);
    }

    @Test
    public void testVariance() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        double variance = dist.getNumericalVariance();
        double expected = Math.pow((dist.getSupportUpperBound() - dist.getSupportLowerBound()), 2) / 12.0;
        assertEquals(expected, variance, 0.0001);
    }

    @Test
    public void testGetSupportLowerBound() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        double lowerBound = dist.getSupportLowerBound();
        assertEquals(1.0, lowerBound, 0.0);
    }

    @Test
    public void testGetSupportUpperBound() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        double upperBound = dist.getSupportUpperBound();
        assertEquals(2.0, upperBound, 0.0);
    }

    @Test
    public void testIsSupportLowerBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        boolean isInclusive = dist.isSupportLowerBoundInclusive();
        assertTrue(isInclusive);
    }

    @Test
    public void testIsSupportUpperBoundInclusive() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        boolean isExclusive = dist.isSupportUpperBoundInclusive();
        assertFalse(isExclusive);
    }

    @Test
    public void testIsSupportConnected() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 2.0);
        boolean isConnected = dist.isSupportConnected();
        assertTrue(isConnected);
    }
}