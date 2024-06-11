package org.apache.commons.math3.distribution.fitting;

import org.apache.commons.math3.exception.util.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    private double[][] data;

    @Before
    public void setUp() {
        data = new double[][]{{1,2},{3,4},{5,6}};
    }

    @Test
    public void testConstructor() {
        try {
            MultivariateNormalMixtureExpectationMaximization me = new MultivariateNormalMixtureExpectationMaximization(data);
            assertEquals(data, me.data);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor_DataLengthLessThan1() {
        try {
            double[][] invalidData = new double[0][0];
            MultivariateNormalMixtureExpectationMaximization me = new MultivariateNormalMixtureExpectationMaximization(invalidData);
            fail("Invalid data length not detected");
        } catch (NotStrictlyPositiveException e) {
            assertEquals(0, e.getArgument());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor_DimensionMismatch() {
        try {
            double[][] invalidData = new double[][]{{1,2},{3}};
            MultivariateNormalMixtureExpectationMaximization me = new MultivariateNormalMixtureExpectationMaximization(invalidData);
            fail("Dimension mismatch not detected");
        } catch (DimensionMismatchException e) {
            assertEquals(1, e.getDimension1());
            assertEquals(2, e.getDimension2());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testConstructor_NumberIsTooSmall() {
        try {
            double[][] invalidData = new double[][]{{1,2},{3,4}};
            MultivariateNormalMixtureExpectationMaximization me = new MultivariateNormalMixtureExpectationMaximization(invalidData);
            fail("Number is too small not detected");
        } catch (MathIllegalArgumentException e) {
            assertEquals(null, e.getSpecifier());
            assertEquals(2, e.getArguments()[0]);
            assertEquals(2, e.getArguments()[1]);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFit() {
        try {
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
            int maxIterations = 1000;
            double threshold = 0.00001;

            MultivariateNormalMixtureExpectationMaximization me = new MultivariateNormalMixtureExpectationMaximization(data);
            me.fit(initialMixture, maxIterations, threshold);
            assertEquals(null, me.getLogLikelihood());
            assertEquals(initialMixture.getComponents(), me.getFittedModel().getComponents());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFit_MaxIterationsLessThan1() {
        try {
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
            int maxIterations = 0;
            double threshold = 0.00001;

            MultivariateNormalMixtureExpectationMaximization me = new MultivariateNormalMixtureExpectationMaximization(data);
            me.fit(initialMixture, maxIterations, threshold);
            fail("Max iterations less than 1 not detected");
        } catch (NotStrictlyPositiveException e) {
            assertEquals(0, e.getArgument());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFit_ThresholdLessThanDoubleMinValue() {
        try {
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
            int maxIterations = 1000;
            double threshold = Double.MIN_VALUE - 0.00001;

            MultivariateNormalMixtureExpectationMaximization me = new MultivariateNormalMixtureExpectationMaximization(data);
            me.fit(initialMixture, maxIterations, threshold);
            fail("Threshold less than Double.MIN_VALUE not detected");
        } catch (NotStrictlyPositiveException e) {
            assertEquals(Double.MIN_VALUE - 0.00001, e.getArgument(), 0.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFit_NumMeanColumnsNotEqualToNumCols() {
        try {
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
            int maxIterations = 1000;
            double threshold = 0.0001;

            MultivariateNormalMixtureExpectationMaximization me = new MultivariateNormalMixtureExpectationMaximization(data);
            me.fit(initialMixture, maxIterations, threshold);
            fail("Num mean columns not equal to num cols not detected");
        } catch (DimensionMismatchException e) {
            // The test does not check the actual values, since data size is fixed.
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFit() {
        try {
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetLogLikelihood() {
        try {
            MultivariateNormalMixtureExpectationMaximization me = new MultivariateNormalMixtureExpectationMaximization(data);
            assertEquals(0.0, me.getLogLikelihood(), 0.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetFittedModel() {
        try {
            MultivariateNormalMixtureExpectationMaximization me = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution fittedModel = new MixtureMultivariateNormalDistribution();
            assertEquals(fittedModel.getComponents(), me.getFittedModel().getComponents());
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}