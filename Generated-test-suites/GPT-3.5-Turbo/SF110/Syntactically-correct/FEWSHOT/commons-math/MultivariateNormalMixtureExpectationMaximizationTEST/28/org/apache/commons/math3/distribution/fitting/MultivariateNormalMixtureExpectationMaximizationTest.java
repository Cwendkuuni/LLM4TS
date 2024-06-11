
package org.apache.commons.math3.distribution.fitting;

import org.junit.Test;

import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructorWithValidInput() {
        try {
            double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            assertNotNull(em);
        } catch (Exception e) {
            fail("Constructor threw an exception");
        }
    }

    @Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidInput() {
        double[][] data = {};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = org.apache.commons.math3.exception.DimensionMismatchException.class)
    public void testConstructorWithInvalidInput2() {
        double[][] data = {{1.0, 2.0}, {3.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = org.apache.commons.math3.exception.NumberIsTooSmallException.class)
    public void testConstructorWithInvalidInput3() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFitWithValidInput() {
        try {
            double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
            em.fit(initialMixture, 1000, 1.0E-5);
        } catch (Exception e) {
            fail("Fit method threw an exception");
        }
    }

    @Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
    public void testFitWithInvalidInput() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        em.fit(initialMixture, 0, 1.0E-5);
    }

    @Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
    public void testFitWithInvalidInput2() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        em.fit(initialMixture, 1000, 0);
    }

    @Test
    public void testGetLogLikelihood() {
        try {
            double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
            em.fit(initialMixture, 1000, 1.0E-5);
            double logLikelihood = em.getLogLikelihood();
            assertTrue(logLikelihood >= 0);
        } catch (Exception e) {
            fail("GetLogLikelihood method threw an exception");
        }
    }

    @Test
    public void testGetFittedModel() {
        try {
            double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
            em.fit(initialMixture, 1000, 1.0E-5);
            MixtureMultivariateNormalDistribution fittedModel = em.getFittedModel();
            assertNotNull(fittedModel);
        } catch (Exception e) {
            fail("GetFittedModel method threw an exception");
        }
    }

    @Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidInput() {
        double[][] data = {{1.0, 2.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }

    @Test(expected = org.apache.commons.math3.exception.NumberIsTooSmallException.class)
    public void testEstimateWithInvalidInput2() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }

    @Test(expected = org.apache.commons.math3.exception.NumberIsTooLargeException.class)
    public void testEstimateWithInvalidInput3() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
    }
}
