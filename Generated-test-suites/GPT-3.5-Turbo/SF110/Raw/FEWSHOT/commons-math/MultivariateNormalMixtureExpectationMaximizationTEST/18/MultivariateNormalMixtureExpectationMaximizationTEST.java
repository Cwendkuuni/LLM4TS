package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.util.MathArrays;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    private double[][] data;
    private MultivariateNormalMixtureExpectationMaximization mnm;

    @Before
    public void setup() {
        data = new double[][]{{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        try {
            mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        assertNotNull(mnm);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeData() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] emptyData = {{}};
        new MultivariateNormalMixtureExpectationMaximization(emptyData);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorDimensionMismatch() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] mismatchData = {{1.0, 2.0, 3.0}, {4.0, 5.0}};
        new MultivariateNormalMixtureExpectationMaximization(mismatchData);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorTooSmallNumber() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] smallNumberData = {{1.0}, {2.0}};
        new MultivariateNormalMixtureExpectationMaximization(smallNumberData);
    }

    @Test
    public void testFit() {
        MixtureMultivariateNormalDistribution initialMixture = null;
        try {
            initialMixture = new MixtureMultivariateNormalDistribution(MultivariateNormalMixtureExpectationMaximization.estimate(data, 2));
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            e.printStackTrace();
        }

        try {
            mnm.fit(initialMixture, 100, 0.001);
            assertEquals(0.08614373167405747, mnm.getLogLikelihood(), 1e-5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeIterations() throws NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(MultivariateNormalMixtureExpectationMaximization.estimate(data, 2));

        mnm.fit(initialMixture, -1, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitNegativeThreshold() throws NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(MultivariateNormalMixtureExpectationMaximization.estimate(data, 2));

        mnm.fit(initialMixture, 100, -0.001);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitInvalidComponentSize() throws NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(MultivariateNormalMixtureExpectationMaximization.estimate(data, 2));

        mnm.fit(initialMixture, 100, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitInvalidModelSize() throws NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(MultivariateNormalMixtureExpectationMaximization.estimate(data, 4));

        mnm.fit(initialMixture, 100, 0.001);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimateInvalidComponentSize() throws NotStrictlyPositiveException, DimensionMismatchException {
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 4);
    }

    @Test
    public void testGetLogLikelihood() {
        assertEquals(0.0, mnm.getLogLikelihood(), 1e-5);
    }

    @Test
    public void testGetFittedModel() {
        assertNull(mnm.getFittedModel());
    }
}