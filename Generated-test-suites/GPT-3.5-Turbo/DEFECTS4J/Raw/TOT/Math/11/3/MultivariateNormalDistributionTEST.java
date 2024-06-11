import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonPositiveDefiniteMatrixException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    private MultivariateNormalDistribution distribution;

    @Before
    public void setup() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
            {1.0, 0.5, 0.3},
            {0.5, 2.0, 0.2},
            {0.3, 0.2, 0.8}
        };
        try {
            distribution = new MultivariateNormalDistribution(means, covariances);
        } catch (DimensionMismatchException | SingularMatrixException | NonPositiveDefiniteMatrixException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetMeans() {
        double[] expected = {1.0, 2.0, 3.0};
        double[] actual = distribution.getMeans();
        assertEquals(expected.length, actual.length);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], 0.000001);
        }
    }

    @Test
    public void testGetCovariances() {
        double[][] expected = {
            {1.0, 0.5, 0.3},
            {0.5, 2.0, 0.2},
            {0.3, 0.2, 0.8}
        };
        double[][] actual = distribution.getCovariances().getData();
        assertEquals(expected.length, actual.length);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i].length, actual[i].length);
            for (int j = 0; j < expected[i].length; j++) {
                assertEquals(expected[i][j], actual[i][j], 0.000001);
            }
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] expected = {1.0, Math.sqrt(2.0), Math.sqrt(0.8)};
        double[] actual = distribution.getStandardDeviations();
        assertEquals(expected.length, actual.length);

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i], 0.000001);
        }
    }

    @Test
    public void testDensity() {
        double[] values = {1.0, 2.0, 3.0};
        double expected = 0.039604;
        double actual = distribution.density(values);
        assertEquals(expected, actual, 0.000001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDensityDimensionMismatch() {
        double[] values = {1.0, 2.0};
        distribution.density(values);
    }

    @Test
    public void testSample() {
        int numSamples = 1000;
        double[][] samples = new double[numSamples][3];

        for (int i = 0; i < numSamples; i++) {
            samples[i] = distribution.sample();
        }

        double[] means = {0.0, 0.0, 0.0};
        double[][] covariances = {
            {1, 0.5, 0.3},
            {0.5, 2.0, 0.2},
            {0.3, 0.2, 0.8}
        };

        double[] sampleMeans = new double[3];
        double[][] sampleCovariances = new double[3][3];
        for (double[] sample : samples) {
            sampleMeans[0] += sample[0];
            sampleMeans[1] += sample[1];
            sampleMeans[2] += sample[2];

            sampleCovariances[0][0] += sample[0] * sample[0];
            sampleCovariances[1][1] += sample[1] * sample[1];
            sampleCovariances[2][2] += sample[2] * sample[2];
            sampleCovariances[0][1] += sample[0] * sample[1];
            sampleCovariances[0][2] += sample[0] * sample[2];
            sampleCovariances[1][2] += sample[1] * sample[2];
        }

        for (int i = 0; i < 3; i++) {
            sampleMeans[i] /= numSamples;
            for (int j = 0; j < 3; j++) {
                sampleCovariances[i][j] /= numSamples;
                if (i != j) {
                    assertEquals(0.0, sampleCovariances[i][j], 0.05);
                }
            }
        }

        double maxDiffMean = MathArrays.distance(sampleMeans, means);
        assertTrue(maxDiffMean < 0.1);

        double maxDiffCov = MathArrays.distance(sampleCovariances, covariances);
        assertTrue(maxDiffCov < 0.1);
    }

    @Test
    public void testConstructorWithRandomGenerator() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
            {1.0, 0.5, 0.3},
            {0.5, 2.0, 0.2},
            {0.3, 0.2, 0.8}
        };
        RandomDataGenerator rng = new RandomDataGenerator();
        distribution = new MultivariateNormalDistribution(rng, means, covariances);
        assertEquals(means.length, distribution.getMeans().length);
        assertEquals(covariances.length, distribution.getCovariances().getData().length);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDimensionMismatch() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
            {1.0, 0.5, 0.3},
            {0.5, 2.0, 0.2},
            {0.3, 0.2}
        };
        distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = SingularMatrixException.class)
    public void testConstructorSingularMatrix() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
            {1.0, 0.5, 0.3},
            {0.5, 2.0, 0.2},
            {0.3, 0.1, 0.8}
        };
        distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testConstructorNonPositiveDefiniteMatrix() {
        double[] means = {1.0, 2.0, 3.0};
        double[][] covariances = {
            {1.0, 0.5, 0.3},
            {0.5, 2.0, 0.2},
            {0.3, 0.2, -0.8}
        };
        distribution = new MultivariateNormalDistribution(means, covariances);
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorNullMeans() {
        double[][] covariances = {
            {1.0, 0.5, 0.3},
            {0.5, 2.0, 0.2},
            {0.3, 0.2, 0.8}
        };
        distribution = new MultivariateNormalDistribution(null, covariances);
    }

    @Test(expected = NullArgumentException.class)
    public void testConstructorNullCovariances() {
        double[] means = {1.0, 2.0, 3.0};
        distribution = new MultivariateNormalDistribution(means, null);
    }
}