package org.apache.commons.math3.distribution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.MathArrays;

/**
 * Test class for MultivariateNormalDistribution.
 */
@RunWith(Parameterized.class)
public class MultivariateNormalDistributionTest {

    private final double[] means;
    private final double[][] covariances;
    private final int dimension;

    public MultivariateNormalDistributionTest(double[] means, double[][] covariances) {
        this.means = means;
        this.covariances = covariances;
        this.dimension = means.length;
    }

    @Parameters
    public static List<Object[]> data() {
        return new ArrayList<Object[]>() {{
            add(new double[]{new double[]{0.0}, new double[0][]}, new Object[]{"empty distribution"});
            add(new double[]{new double[]{1.0, 2.0}, new double[][]{ {1.0, 2.0}, {2.0, 1.0} }});
            add(new double[]{new double[]{1.0, 2.0}, new double[][]{ {1.0, 2.0}, {2.0, 1.0}, {2.0, 2.0} }});
        }};
    }

    @Test
    public void testConstructorWithMeansAndCovariances() throws Exception {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        assertEquals(means.length, dist.getDimension());
        assertEquals(Arrays.toString(means), dist.getMeans().toString());
        RealMatrix covarianceMatrix = dist.getCovariances();
        assertEquals(covariances.length, covarianceMatrix.getRowDimension());
        assertEquals(covariances[0].length, covarianceMatrix.getColumnDimension());
        assertArrayEquals("Covariance matrix should be equals.", covariances, covarianceMatrix.getData());
    }

    @Test(expected = DimensionMismatchException.class)
    public void testExceptionWithInconsistentMeansAndCovariances() throws Exception {
        new MultivariateNormalDistribution(means, covariances.length > means.length ? covariances : new double[means.length][covariances.length]);
    }

    @Test(expected = SingularMatrixException.class)
    public void testExceptionWithSingularCovarianceMatrix() throws Exception {
        new MultivariateNormalDistribution(means, covariances);
        final EigenDecomposition covarianceMatrixDec = new EigenDecomposition(covariances[0]);
        covarianceMatrixDec.getEigenvalues()[0] *= -1;
        final double[][] covarianceMatrixData = covarianceMatrixDec.getData();
        new MultivariateNormalDistribution(means, covarianceMatrixData);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testExceptionWithNonPositiveDefiniteCovarianceMatrix() throws Exception {
        new MultivariateNormalDistribution(means, covariances);
        final EigenDecomposition covarianceMatrixDec = new EigenDecomposition(covariances[0]);
        final double[] covarianceMatrixEigenvalues = covarianceMatrixDec.getRealEigenvalues();
        for (int i = 0; i < covarianceMatrixEigenvalues.length; i++) {
            if (covarianceMatrixEigenvalues[i] < 0) {
                covarianceMatrixEigenvalues[i] *= -1;
                break;
            }
        }
        final double[][] covarianceMatrixData = covarianceMatrixDec.getData();
        new MultivariateNormalDistribution(means, covarianceMatrixData);
    }

    @Test
    public void testGetStandardDeviations() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] std = dist.getStandardDeviations();
        for (int i = 0; i < std.length; i++) {
            assertEquals("Standard deviation [" + i + "] should be equals.", Math.sqrt(covariances[i][i]), std[i], 1e-14);
        }
    }

    @Test
    public void testDensity() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = new double[dimension];
        for (int i = 0; i < testValues.length; i++) {
            testValues[i] = i;
        }
        double density = dist.density(testValues);
        RealVector vector = dist.getMeans();
        double exponentTerm = dist.getExponentTerm(testValues);
        double densityExpected = FastMath.pow(2 * FastMath.PI, -testValues.length / 2)
            * FastMath.pow(dist.getCovariances().determinant(), -0.5) * exponentTerm;
        assertEquals("Density should be equals.", densityExpected, density, 1e-14);
    }

    @Test
    public void testSample() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] sampleValues = dist.sample();
        for (int i = 0; i < sampleValues.length; i++) {
            assertEquals("Value [" + i + "] should be in the range.", -3, Double.MAX_VALUE, sampleValues[i], -3 + dist.getStandardDeviations()[i]);
            assertEquals("Value [" + i + "] should be in the range.", 3, Double.MAX_VALUE, sampleValues[i], 3 + dist.getStandardDeviations()[i]);
        }
        RealVector vector = dist.getMeans();
        double[] testValues = new double[dimension];
        for (int i = 0; i < testValues.length; i++) {
            testValues[i] = i + vector.getEntry(i);
        }
        double[] sampleValuesExpected = new double[testValues.length];
        for (int i = 0; i < sampleValuesExpected.length; i++) {
            sampleValuesExpected[i] = testValues[i] - vector.getEntry(i);
        }
        for (int i = 0; i < sampleValues.length; i++) {
            assertEquals("Sample [" + i + "] should be equals to the expected.", sampleValuesExpected[i], sampleValues[i], 1e-14);
        }
    }

    @Test
    public void testGetExponentTerm() {
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(means, covariances);
        double[] testValues = new double[dimension];
        for (int i = 0; i < testValues.length; i++) {
            testValues[i] = i;
        }
        double exponentTerm = dist.getExponentTerm(testValues);
        RealMatrix covarianceMatrix = dist.getCovariances();
        double[] centered = new double[testValues.length];
        for (int i = 0; i < centered.length; i++) {
            centered[i] = testValues[i] - dist.getMeans()[i];
        }
        RealMatrix centeredMatrix = new Array2DRowRealMatrix(centered);
        RealMatrix product = covarianceMatrixInverse.multiply(centeredMatrix);
        double exponentTermExpected = FastMath.pow(2 * FastMath.PI, -dimensionality / 2)
            * FastMath.pow(dist.getCovariances().determinant(), -0.5) * Precision.doubleArrayToString(product.operate(centered));
        assertEquals("Exponent term should be equals.", exponentTermExpected, exponentTerm, 1e-14);
    }
}