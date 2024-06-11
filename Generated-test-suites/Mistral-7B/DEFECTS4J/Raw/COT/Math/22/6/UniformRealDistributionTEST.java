import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;
import static org.junit.Assert.*;

public class UniformRealDistributionTest {

    @Test
    public void testUniformRealDistributionDefaultConstructor() {
        UniformRealDistribution dist = new UniformRealDistribution();
        assertEquals(0.0, dist.getLowerBound(), 0.0);
        assertEquals(1.0, dist.getUpperBound(), 0.0);
        assertEquals(0.5, dist.getNumericalMean(), 0.0);
        assertEquals(0.25, dist.getNumericalVariance(), 0.0);
    }

    @Test
    public void testUniformRealDistributionWithBounds() {
        UniformRealDistribution dist = new UniformRealDistribution(1.0, 3.0);
        assertEquals(1.0, dist.getLowerBound(), 0.0);
        assertEquals(3.0, dist.getUpperBound(), 0.0);
        assertEquals(2.0, dist.getNumericalMean(), 0.0);
        assertEquals(0.75, dist.getNumericalVariance(), 0.0);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testUniformRealDistributionWithInvalidBounds() {
        new UniformRealDistribution(3.0, 1.0);
    }

    @Test
    public void testUniformRealDistributionWithRandomGenerator() {
        RandomGenerator rng = new Well19937c();
        UniformRealDistribution dist = new UniformRealDistribution(rng, 1.0, 3.0);
        assertEquals(1.0, dist.getLowerBound(), 0.0);
        assertEquals(3.0, dist.getUpperBound(), 0.0);
        assertEquals(2.0, dist.getNumericalMean(), 0.0);
        assertEquals(0.75, dist.getNumericalVariance(), 0.0);
    }

    @Test
    public void testUniformRealDistributionDensity() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        for (double x = 0.1; x <= 0.3; x += 0.01) {
            double density = dist.density(x);
            double expectedDensity = 1 / (0.3 - 0.1);
            assertEquals(expectedDensity, density, 0.01);
        }

        for (double x = 0.0; x < 0.1 || x > 0.3; x += 0.01) {
            double density = dist.density(x);
            assertEquals(0.0, density, 0.01);
        }
    }

    @Test
    public void testUniformRealDistributionCumulativeProbability() {
        UniformRealDistribution dist = new UniformRealDistribution(0.1, 0.3);
        for (double x = 0.1; x <= 0.3; x += 0.01) {
            double cumProb = dist.cumulativeProbability(x);
            double expectedCumProb = (x - 0.1) / (0.3 - 0.1);
            assertEquals(expectedCumProb, cumProb, 0.01);
        }

        for (double x = 0.0; x < 0.1 || x > 0.3; x += 0.01) {
            double cumProb = dist.cumulativeProbability(x);
            assertEquals(0.0, cumProb, 0.01);
        }
    }

    @Test
    public void testUniformRealDistributionSample() {
        UniformRealDistribution dist = new UniformRealDistribution();
        for (int i = 0; i < 1000; i++) {
            double sample = dist.sample();
            assertTrue(sample >= dist.getLowerBound());
            assertTrue(sample < dist.getUpperBound());
        }
    }

    @Test
    public void testUniformRealDistributionSampleMultipleThreads() {
        UniformRealDistribution dist = new UniformRealDistribution();
        int numThreads = 10;
        int numSamplesPerThread = 100;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < numSamplesPerThread; j++) {
                    double sample = dist.sample();
                    assertTrue(sample >= dist.getLowerBound());
                    assertTrue(sample < dist.getUpperBound());
                }
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}