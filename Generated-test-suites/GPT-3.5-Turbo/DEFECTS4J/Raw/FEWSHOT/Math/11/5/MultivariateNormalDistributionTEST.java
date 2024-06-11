import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.MathArrays;
import org.junit.BeforeClass;
import org.junit.Test;

public class MultivariateNormalDistributionTest {
    private static final double EPSILON = 1e-9;
    private static final int MAX_DIMENSION = 10;
    private static final int MAX_SAMPLES = 1000000;

    private static RandomGenerator rng;

    @BeforeClass
    public static void setupClass() {
        rng = new DummyRandomGenerator();
    }

    @Test
    public void testConstructor() {
        double[] means = {1, 2, 3};
        double[][] covariances = {{1, 0, 0}, {0, 2, 0}, {0, 0, 3}};

        try {
            MultivariateNormalDistribution distribution =
                    new MultivariateNormalDistribution(rng, means, covariances);

            assertNotNull(distribution);
            assertEquals(means.length, distribution.getDimension());
            assertEquals(means, distribution.getMeans());
            assertTrue(MathArrays.isEqual(covariances, distribution.getCovariances().getData(), EPSILON));
            assertTrue(MathArrays.isEqual(covariances,
                    distribution.getCovarianceMatrix().getData(), EPSILON));
        } catch (Exception e) {
            fail("Failed to create MultivariateNormalDistribution: " + e.getMessage());
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDimensionMismatch() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0, 0}, {0, 2, 0}, {0, 0, 3}};

        MultivariateNormalDistribution distribution =
                new MultivariateNormalDistribution(rng, means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorSingularMatrix() {
        double[] means = {0, 0};
        double[][] covariances = {{0, 0}, {0, 0}};

        MultivariateNormalDistribution distribution =
                new MultivariateNormalDistribution(rng, means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrix() {
        double[] means = {1, 2};
        double[][] covariances = {{1, -1}, {-1, 2}};

        MultivariateNormalDistribution distribution =
                new MultivariateNormalDistribution(rng, means, covariances);
    }

    @Test
    public void testDensity() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};

        MultivariateNormalDistribution distribution =
                new MultivariateNormalDistribution(rng, means, covariances);

        double[] values = {0, 0};
        double expectedDensity = 0.159154943;
        double actualDensity = distribution.density(values);

        assertEquals(expectedDensity, actualDensity, EPSILON);
    }

    @Test
    public void testSample() {
        double[] means = {0, 0};
        double[][] covariances = {{1, 0}, {0, 1}};

        MultivariateNormalDistribution distribution =
                new MultivariateNormalDistribution(rng, means, covariances);

        double[] sample = distribution.sample();

        assertNotNull(sample);
        assertEquals(means.length, sample.length);
    }

    @Test
    public void testSampleMean() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};

        MultivariateNormalDistribution distribution =
                new MultivariateNormalDistribution(rng, means, covariances);

        double[] sampleMean = new double[means.length];
        int numSamples = 10000;

        for (int i = 0; i < numSamples; i++) {
            double[] sample = distribution.sample();
            MathArrays.parallelAdd(sampleMean, sample);
        }

        MathArrays.scaleInPlace(1.0 / numSamples, sampleMean);

        assertTrue(MathArrays.isEqual(means, sampleMean, EPSILON));
    }

    @Test
    public void testSampleCovariance() {
        double[] means = {1, 2};
        double[][] covariances = {{1, 0}, {0, 1}};

        MultivariateNormalDistribution distribution =
                new MultivariateNormalDistribution(rng, means, covariances);

        int dimension = means.length;
        double[][] sampleCovariance = new double[dimension][dimension];
        double[] sampleMean = new double[dimension];
        int numSamples = 10000;

        for (int i = 0; i < numSamples; i++) {
            double[] sample = distribution.sample();

            double[][] centeredSample = new double[dimension][];
            for (int j = 0; j < dimension; j++) {
                centeredSample[j] = new double[]{sample[j] - means[j]};
            }

            MathArrays.outerProductInPlace(centeredSample, centeredSample);
            MathArrays.parallelAdd(sampleCovariance, centeredSample);
            MathArrays.parallelAdd(sampleMean, sample);
        }

        MathArrays.scaleInPlace(1.0 / numSamples, sampleCovariance);
        MathArrays.scaleInPlace(1.0 / numSamples, sampleMean);

        assertTrue(MathArrays.isEqual(covariances, sampleCovariance, EPSILON));
    }

    @Test
    public void testSamplingPerformance() {
        double[] means = new double[MAX_DIMENSION];
        double[][] covariances = new double[MAX_DIMENSION][MAX_DIMENSION];
        for (int i = 0; i < MAX_DIMENSION; i++) {
            means[i] = i;
            covariances[i][i] = i + 1;
        }

        MultivariateNormalDistribution distribution =
                new MultivariateNormalDistribution(rng, means, covariances);

        distribution.sample();
        int numSamples = 0;
        long startTime = System.currentTimeMillis();

        while (numSamples < MAX_SAMPLES) {
            distribution.sample();
            numSamples++;
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        System.out.println("Samples: " + numSamples);
        System.out.println("Elapsed Time (ms): " + elapsedTime);
    }

    private static class DummyRandomGenerator implements RandomGenerator {
        @Override
        public void setSeed(long seed) {
        }

        @Override
        public void setSeed(int[] seed) {
        }

        @Override
        public void setSeed(int seed) {
        }

        @Override
        public void nextBytes(byte[] bytes) {
        }

        @Override
        public int nextInt() {
            return 0;
        }

        @Override
        public int nextInt(int n) {
            return 0;
        }

        @Override
        public long nextLong() {
            return 0;
        }

        @Override
        public boolean nextBoolean() {
            return false;
        }

        @Override
        public float nextFloat() {
            return 0;
        }

        @Override
        public double nextDouble() {
            return 0;
        }

        @Override
        public double nextGaussian() {
            return 0;
        }
    }
}