package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.UnivariateRealDistribution;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testMultivariateNormalDistribution() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        assertEquals(2, dist.getDimension());
        assertEquals(means.length, dist.getMeans().length);
        assertEquals(covariances[0].length, dist.getCovariances().getColumnDimension());
        assertEquals(covariances[0].length, dist.getCovariances().getRowDimension());
        assertNotNull(dist.getCovariances());
        assertNotNull(dist.getCovariancesInverse());
        assertNotNull(dist.getCovarianceMatrixDeterminant());
        assertNotNull(dist.getSamplingMatrix());
    }

    @Test(expected = SingularMatrixException.class)
    public void testSingularMatrixException() {
        double[] means = {1.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 0.5}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = NonPositiveDefiniteMatrixException.class)
    public void testNonPositiveDefiniteMatrixException() {
        double[] means = {1.0};
        double[][] covariances = {{1.0, -1.0}, {-1.0, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        double[] means = {1.0};
        double[][] covariances = {{1.0}, {1.0, 1.0}};
        new MultivariateNormalDistribution(new Well19937c(), means, covariances);
    }

    @Test
    public void testGetMeans() {
        double[] means = {1.0, 2.0};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, new double[][]{{1.0, 0.5}, {0.5, 1.0}});
        assertEquals(2, dist.getMeans().length);
        assertArrayEquals(new double[]{1.0, 2.0}, dist.getMeans());
    }

    @Test
    public void testGetCovariances() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        RealMatrix cov = dist.getCovariances();
        assertEquals(cov.getRowDimension(), cov.getColumnDimension());
        assertEquals(2, cov.getRowDimension());
        assertEquals(2, cov.getColumnDimension());
        assertArrayEquals(cov.getData(), covariances);
    }

    @Test
    public void testGetCovarianceMatrixDeterminant() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        assertEquals(1.0, dist.getCovarianceMatrixDeterminant(), 1e-10);
    }

    @Test
    public void testDensity() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        double[] values = {1.5, 2.5};
        double density = dist.density(values);

        double r = Math.sqrt(2 * Math.PI);
        double d = Math.pow(Math.E, -1.0 / 2.0);
        double covDet = dist.getCovarianceMatrixDeterminant();
        double[] meansVector = dist.getMeans();
        double[] sdv = dist.getStandardDeviations();
        double[] centered = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            centered[i] = values[i] - meansVector[i];
        }
        double[] preMultiplied = dist.getCovarianceMatrixInverse().operate(centered);
        double exponentTerm = FastMath.exp(-0.5 * FastMath.dot(preMultiplied, centered));
        double densityExpected = r * r * d * covDet * exponentTerm;
        assertEquals(densityExpected, density, 1e-10);

        values[0] = 0.5;
        density = dist.density(values);
        densityExpected = r * r * d * covDet * exponentTerm * Math.exp(-0.5 * (FastMath.pow(0.5 - means[0], 2) + FastMath.pow(2.5 - means[1], 2)));
        assertEquals(densityExpected, density, 1e-10);
    }

    @Test
    public void testSample() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);

        double[] vals = dist.sample();
        assertNotNull(vals);
        assertEquals(vals.length, dist.getDimension());
        for (int i = 0; i < vals.length; i++) {
            assertNotNull(vals[i]);
        }

        vals = dist.sample();
        double[] meansVector = dist.getMeans();
        double[] sdv = dist.getStandardDeviations();
        ArrayRealVector meansVectorReal = new ArrayRealVector(meansVector);
        ArrayRealVector sdvReal = new ArrayRealVector(sdv);
        RealMatrix samplingMatrix = dist.getSamplingMatrix();

        for (int i = 0; i < vals.length; i++) {
            double mu = meansVectorReal.getEntry(i);
            double std = sdvReal.getEntry(i);
            double z = random.nextGaussian();
            double v = samplingMatrix.getEntry(i, i) * z + meansVectorReal.getEntry(i);
            assertEquals(mu + std * z, v, 1e-6);
        }
    }

    @Test
    public void testGetStandardDeviations() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] sdv = dist.getStandardDeviations();
        assertEquals(2, sdv.length);
        assertEquals(1.0, sdv[0], 1e-6);
        assertEquals(1.0, sdv[1], 1e-6);
    }

    @Test
    public void testGetExponentTerm() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.5}, {0.5, 1.0}};
        MultivariateNormalDistribution dist = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        double[] sdv = dist.getStandardDeviations();

        double[] values = {1.5, 2.5};
        double exponentTerm = dist.getExponentTerm(values);

        RealMatrix inverse = dist.getCovarianceMatrixInverse();
        double[] centeredValues = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            centeredValues[i] = values[i] - means[i];
        }
        double[] preMultiplied = inverse.operate(centeredValues);
        double exponentTermExpected = FastMath.exp(-0.5 * FastMath.dot(preMultiplied, centeredValues));
        assertEquals(exponentTermExpected, exponentTerm, 1e-10);
    }

    @Test
    public void testSampleFromUnivariateDistribution() {
        double mean = 1.0;
        double stdDev = 1.0;
        UnivariateRealDistribution dist = new NormalDistribution(mean, stdDev);
        double[] vals = new double[10_000];

        for (int i = 0; i < vals.length; i++) {
            vals[i] = dist.sample();
        }

        double sum = 0;
        for (int i = 0; i < vals.length; i++) {
            sum += vals[i];
        }
        double average = sum / vals.length;
        assertEquals(mean, average, 1e-6);
    }
}