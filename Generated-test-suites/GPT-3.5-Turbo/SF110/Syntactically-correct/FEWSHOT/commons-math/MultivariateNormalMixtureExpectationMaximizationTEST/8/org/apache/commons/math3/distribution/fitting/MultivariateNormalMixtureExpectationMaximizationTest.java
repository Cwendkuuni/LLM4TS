package org.apache.commons.math3.distribution.fitting;

import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testMultivariateNormalMixtureExpectationMaximization() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        try {
            MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution fittedModel = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
            assertEquals(fittedModel, m.getFittedModel());
        } catch (Exception e) {
            assertFalse(e);
        }
    }

    @Test
    public void testFit() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        try {
            MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
            int maxIterations = 1000;
            double threshold = 1.0E-5;
            m.fit(initialMixture, maxIterations, threshold);
            assertTrue(m.getLogLikelihood() >= 0.0);
        } catch (Exception e) {
            assertFalse(e);
        }
    }

    @Test
    public void testFitWithDefaultValues() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        try {
            MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(new ArrayList<Pair<Double, MultivariateNormalDistribution>>());
            m.fit(initialMixture);
            assertTrue(m.getLogLikelihood() >= 0.0);
        } catch (Exception e) {
            assertFalse(e);
        }
    }

    @Test
    public void testEstimate() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int numComponents = 3;
        try {
            MixtureMultivariateNormalDistribution m = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
            assertTrue(m.getComponents().size() == numComponents);
        } catch (Exception e) {
            assertFalse(e);
        }
    }
}