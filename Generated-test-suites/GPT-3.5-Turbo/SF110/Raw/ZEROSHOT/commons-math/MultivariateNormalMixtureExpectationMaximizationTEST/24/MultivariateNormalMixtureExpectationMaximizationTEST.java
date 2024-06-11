package org.apache.commons.math3.distribution.fitting;

import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testFit() throws Exception {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int maxIterations = 1000;
        double threshold = 1.0E-5;

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(null, maxIterations, threshold);
        assertNotNull(em.getFittedModel());
    }

    @Test
    public void testFitWithInitialMixture() throws Exception {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int maxIterations = 1000;
        double threshold = 1.0E-5;
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(initialMixture, maxIterations, threshold);
        assertNotNull(em.getFittedModel());
    }

    @Test
    public void testFitWithDefaultArguments() throws Exception {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(null);
        assertNotNull(em.getFittedModel());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidMaxIterations() throws Exception {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int maxIterations = 0;
        double threshold = 1.0E-5;

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(null, maxIterations, threshold);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidThreshold() throws Exception {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int maxIterations = 1000;
        double threshold = 0;

        MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
        em.fit(null, maxIterations, threshold);
    }

    @Test
    public void testEstimate() throws Exception {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int numComponents = 2;

        MixtureMultivariateNormalDistribution mixture =
                MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
        assertNotNull(mixture);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidNumComponents() throws Exception {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int numComponents = 0;

        MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidData() throws Exception {
        double[][] data = {};
        int numComponents = 2;

        MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
    }
}