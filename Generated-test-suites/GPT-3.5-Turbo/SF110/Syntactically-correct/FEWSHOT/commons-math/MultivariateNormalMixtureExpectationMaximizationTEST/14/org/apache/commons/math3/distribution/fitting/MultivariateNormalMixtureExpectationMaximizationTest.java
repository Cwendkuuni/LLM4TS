package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.util.Pair;

import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    private double[][] data;
    private MultivariateNormalMixtureExpectationMaximization testObject;

    @Before
    public void setUp() {
        data = new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0, 6.0 }, { 7.0, 8.0, 9.0 } };
        testObject = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testConstructor() {
        assertNotNull(testObject);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidData() {
        double[][] invalidData = new double[0][];
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInvalidData2() {
        double[][] invalidData = new double[][] { { 1.0, 2.0, 3.0 }, { 4.0, 5.0 } };
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidData3() {
        double[][] invalidData = new double[][] { { 1.0 }, { 2.0 } };
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test
    public void testFit() {
        MixtureMultivariateNormalDistribution initialMixture =
                new MixtureMultivariateNormalDistribution(
                        java.util.Arrays.asList(
                                new Pair<Double, org.apache.commons.math3.distribution.MultivariateNormalDistribution>(0.5, new org.apache.commons.math3.distribution.MultivariateNormalDistribution(new double[] {0, 0, 0}, org.apache.commons.math3.util.MathUtils.createRealIdentityMatrix(3)))));

        try {
            testObject.fit(initialMixture, 1000, 1.0E-5);
        } catch (Exception e) {
            fail("Should not throw any exception");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidMaxIterations() {
        MixtureMultivariateNormalDistribution initialMixture =
                new MixtureMultivariateNormalDistribution(
                        java.util.Arrays.asList(
                                new Pair<Double, org.apache.commons.math3.distribution.MultivariateNormalDistribution>(0.5, new org.apache.commons.math3.distribution.MultivariateNormalDistribution(new double[] {0, 0, 0}, org.apache.commons.math3.util.MathUtils.createRealIdentityMatrix(3)))));

        try {
            testObject.fit(initialMixture, 0, 1.0E-5);
        } catch (Exception e) {
            assertTrue(e instanceof NotStrictlyPositiveException);
            assertEquals("0 is not strictly positive", e.getMessage());
            throw (NotStrictlyPositiveException) e;
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidThreshold() {
        MixtureMultivariateNormalDistribution initialMixture =
                new MixtureMultivariateNormalDistribution(
                        java.util.Arrays.asList(
                                new Pair<Double, org.apache.commons.math3.distribution.MultivariateNormalDistribution>(0.5, new org.apache.commons.math3.distribution.MultivariateNormalDistribution(new double[] {0, 0, 0}, org.apache.commons.math3.util.MathUtils.createRealIdentityMatrix(3)))));

        try {
            testObject.fit(initialMixture, 1000, 0);
        } catch (Exception e) {
            assertTrue(e instanceof NotStrictlyPositiveException);
            assertEquals("0.0 is not strictly positive", e.getMessage());
            throw (NotStrictlyPositiveException) e;
        }
    }
    
    @Test
    public void testFitConvergenceException() {
        MixtureMultivariateNormalDistribution initialMixture =
                new MixtureMultivariateNormalDistribution(
                        java.util.Arrays.asList(
                                new Pair<Double, org.apache.commons.math3.distribution.MultivariateNormalDistribution>(0.5, new org.apache.commons.math3.distribution.MultivariateNormalDistribution(new double[] {0, 0, 0}, org.apache.commons.math3.util.MathUtils.createRealIdentityMatrix(3)))));

        try {
            testObject.fit(null);
            fail("Should throw ConvergenceException");
        } catch (Exception e) {
            assertTrue(e instanceof ConvergenceException);
            assertEquals("Estimation failed", e.getMessage());
        }
    }

    @Test
    public void testEstimate() {
        try {
            MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        
            assertNotNull(result);
        } catch (Exception e) {
            fail("Should not throw any exception");
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidData() {
        double[][] invalidData = new double[0][];
        MultivariateNormalMixtureExpectationMaximization.estimate(invalidData, 2);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateWithInvalidNumComponents() {
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimateWithInvalidNumComponents2() {
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 4);
    }

    @Test
    public void testGetLogLikelihood() {
        assertEquals(0.0, testObject.getLogLikelihood(), 1e-15);
    }

    @Test
    public void testGetFittedModel() {
        assertNotNull(testObject.getFittedModel());
    }

}