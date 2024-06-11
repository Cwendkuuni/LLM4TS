package org.apache.commons.math3.distribution.fitting;

import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.stat.correlation.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructor() {
        double[][] testData = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization instance = new MultivariateNormalMixtureExpectationMaximization(testData);
        assertNotNull(instance);
    }

    @Test
    public void testFit() {
        double[][] testData = {{1.0, 2.0}, {3.0, 4.0}};
        MultivariateNormalMixtureExpectationMaximization instance = new MultivariateNormalMixtureExpectationMaximization(testData);
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            instance.fit(initialMixture, 1000, 1.0E-5);
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("Exception not expected");
        }
        assertNotNull(instance.getFittedModel());
    }

    @Test
    public void testEstimate() {
        double[][] testData = {{1.0, 2.0}, {3.0, 4.0}};
        int numComponents = 2;
        try {
            MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(testData, numComponents);
            assertNotNull(result);
        } catch (NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("Exception not expected");
        }
    }

}