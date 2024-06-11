package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.junit.Test;

public class MultivariateNormalDistributionTest {

    @Test
    public void testConstructorWithVectorAndMatrix() {
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.1}, {0.1, 1.0}};

        MultivariateNormalDistribution mnd = null;
        try {
            mnd = new MultivariateNormalDistribution(new Well19937c(), means, covariances);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception was thrown when it shouldn't be");
        }

        assertEquals(means.length, mnd.getDimension());
        assertEquals(means, mnd.getMeans());

        RealMatrix covMatrix = mnd.getCovariances();
        assertArrayEquals(covariances, covMatrix.getData(), 0.001);

        double[] std = mnd.getStandardDeviations();
        assertArrayEquals(new double[]{Math.sqrt(covariances[0][0]), Math.sqrt(covariances[1][1])}, std, 0.001);
    }

    @Test
    public void testSample() {
        RandomGenerator rng = new Well19937c();
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.1}, {0.1, 1.0}};
        MultivariateNormalDistribution mnd = null;
        try {
            mnd = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception was thrown when it shouldn't be");
        }

        double[] sample = mnd.sample();
        assertEquals(means.length, sample.length);
        for (int i = 0; i < sample.length; i++) {
            assertEquals(Double.class, sample[i].getClass());
        }

        for (int i = 0; i < means.length; i++) {
            assertEquals(means[i], sample[i], 0.001);
        }
    }

    @Test
    public void testDensity() {
        RandomGenerator rng = new Well19937c();
        double[] means = {1.0, 2.0};
        double[][] covariances = {{1.0, 0.1}, {0.1, 1.0}};
        MultivariateNormalDistribution mnd = null;
        try {
            mnd = new MultivariateNormalDistribution(rng, means, covariances);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception was thrown when it shouldn't be");
        }

        double[] testVals = {1.5, 1.5};
        double expectedDensity = 0.12748456911727314;
        double density = 0;
        try {
            density = mnd.density(testVals);
        } catch (DimensionMismatchException e) {
            e.printStackTrace();
            fail("Exception was thrown when it shouldn't be");
        }

        assertEquals(expectedDensity, density, 0.0001);
    }
}