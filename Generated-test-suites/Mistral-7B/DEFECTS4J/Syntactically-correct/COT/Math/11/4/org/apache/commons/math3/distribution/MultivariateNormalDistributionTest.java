package org.apache.commons.math3.distribution;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFactory;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    private static final int DIM = 2;
    private static final double EPSILON = 1e-12;

    private RandomGenerator rng;
    private MultivariateNormalDistribution dist;

    @Before
    public void setUp() {
        rng = new Well19937c();
        double[] means = new double[]{0.0, 0.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};

        try {
            dist = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate MultivariateNormalDistribution.", e);
        }
    }

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testConstructor() {
        Assert.assertArrayEquals(means, dist.getMeans());
        Assert.assertEquals(DIM, dist.getDimension());

        RealMatrix covMat = dist.getCovariances();
        Array2DRowRealMatrix covMatCopy = RealMatrixFactory.copy(covMat);
        Assert.assertEquals(covMatCopy, covMat);
    }

    /**
     * Test density method with valid input.
     */
    @Test
    public void testDensity() {
        double[] testValues = new double[]{0.0, 0.0};
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -DIM / 2) *
                FastMath.pow(dist.covarianceMatrixDeterminant, -0.5) *
                getExponentTerm(testValues);

        double actualDensity = dist.density(testValues);
        Assert.assertEquals(expectedDensity, actualDensity, EPSILON);
    }

    /**
     * Test density method with invalid input (dimension mismatch).
     */
    @Test(expected = DimensionMismatchException.class)
    public void testDensityInvalidInput() {
        double[] invalidValues = new double[]{0.0, 0.0, 0.0};
        dist.density(invalidValues);
    }

    /**
     * Test sample method.
     */
    @Test
    public void testSample() {
        double[] sampleValues = dist.sample();
        Assert.assertArrayEquals(means, sampleValues, EPSILON);

        for (int i = 0; i < DIM; i++) {
            double expectedValue = rng.nextGaussian();
            double actualValue = sampleValues[i];
            Assert.assertEquals(expectedValue, actualValue, EPSILON);
        }
    }

    /**
     * Test getStandardDeviations method.
     */
    @Test
    public void testGetStandardDeviations() {
        double[] stdDeviations = dist.getStandardDeviations();
        for (int i = 0; i < DIM; i++) {
            Assert.assertEquals(Math.sqrt(dist.getCovariances().getData()[i][i]), stdDeviations[i], EPSILON);
        }
    }

    /**
     * Test getExponentTerm method with valid input.
     */
    private double getExponentTerm(final double[] values) {
        final double[] centered = MathArrays.copyOf(values, DIM);
        final double[] preMultiplied = dist.covarianceMatrixInverse.operate(centered);
        double sum = 0;
        for (int i = 0; i < preMultiplied.length; i++) {
            sum += preMultiplied[i] * centered[i];
        }
        return FastMath.exp(-0.5 * sum);
    }
}