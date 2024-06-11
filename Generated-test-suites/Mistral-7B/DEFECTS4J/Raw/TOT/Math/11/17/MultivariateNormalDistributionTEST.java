package org.apache.commons.math3.distribution;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealArray2DSpectralDecomposition;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.StatUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MultivariateNormalDistribution.
 */
public class MultivariateNormalDistributionTest {

    private static final double EPSILON = 1e-6;

    private MultivariateNormalDistribution dist;
    private RandomGenerator rng;

    @Before
    public void setUp() {
        rng = new Well19937c();
        dist = new MultivariateNormalDistribution(rng, new double[] {1.0, 2.0},
                new double[][] {{3.0, 1.0}, {1.0, 4.0}});
    }

    /**
     * Test mean and standard deviation.
     */
    @Test
    public void testMeanAndStandardDeviation() {
        Assert.assertArrayEquals(new double[] {1.0, 2.0}, dist.getMeans(), EPSILON);
        Assert.assertArrayEquals(new double[] {FastMath.SQRT_3, FastMath.SQRT(4.0)},
                dist.getStandardDeviations(), EPSILON);
    }

    /**
     * Test density calculation.
     */
    @Test
    public void testDensity() {
        double[] testValues = new double[] {1.5, 2.5};
        double density = dist.density(testValues);
        double expectedDensity = FastMath.pow(2 * FastMath.PI, -2.0) *
                FastMath.pow(dist.covarianceMatrixDeterminant, -0.5) *
                dist.getExponentTerm(testValues);

        Assert.assertEquals(expectedDensity, density, EPSILON);
    }

    /**
     * Test sampling.
     */
    @Test
    public void testSampling() {
        double[] sample = dist.sample();
        RealVector mean = new ArrayRealVector(new double[] {1.0, 2.0});
        RealMatrix covarianceMatrix = new Array2DRowRealMatrix(new double[][] {{3.0, 1.0},
                {1.0, 4.0}});
        RealMatrix expectedSamplingMatrix = new RealArray2DSpectralDecomposition(covarianceMatrix)
                .getSolver().getInverse().transpose().multiply(
                covarianceMatrixEigenvectors.transpose());
        RealVector expectedSample = mean.add(expectedSamplingMatrix.operate(StatUtils.createRVG(rng, 2)));
        Assert.assertArrayEquals(expectedSample.toArray(), sample, EPSILON);
    }

    /**
     * Test exception handling.
     */
    @Test(expected = SingularMatrixException.class)
    public void testExceptionHandling() throws SingularMatrixException {
        new MultivariateNormalDistribution(rng, new double[] {1.0}, new double[][] {{1.0, 0.0},
                {0.0, 1.0}});
    }
}