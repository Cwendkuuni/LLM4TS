package org.apache.commons.math3.distribution.fitting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testConstructorValidData() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization mixture = new MultivariateNormalMixtureExpectationMaximization(data);
        assertEquals(data, mixture.getData());
    }

    @Test
    public void testConstructorInvalidData_NotStrictlyPositiveException() {
        double[][] data = {};
        assertThrows(NotStrictlyPositiveException.class,
                () -> new MultivariateNormalMixtureExpectationMaximization(data));
    }

    @Test
    public void testConstructorInvalidData_DimensionMismatchException() {
        double[][] data = {{1.0, 2.0}, {3.0, 4.0, 5.0}};
        assertThrows(DimensionMismatchException.class,
                () -> new MultivariateNormalMixtureExpectationMaximization(data));
    }

    @Test
    public void testConstructorInvalidData_NumberIsTooSmallException() {
        double[][] data = {{1.0}, {2.0}};
        assertThrows(NotStrictlyPositiveException.class,
                () -> new MultivariateNormalMixtureExpectationMaximization(data));
    }

    @Test
    public void testFit_ValidInput() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization mixture = new MultivariateNormalMixtureExpectationMaximization();
        mixture.fit(initialMixture);
        // Add assertions for expected output
    }

    @Test
    public void testFit_InvalidInput_NotStrictlyPositiveException() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization mixture = new MultivariateNormalMixtureExpectationMaximization();
        assertThrows(NotStrictlyPositiveException.class,
                () -> mixture.fit(initialMixture, 0, 1.0E-5));
    }

    @Test
    public void testFit_InvalidInput_SingularMatrixException() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization mixture = new MultivariateNormalMixtureExpectationMaximization();
        // Provide invalid input to cause SingularMatrixException
        assertThrows(SingularMatrixException.class,
                () -> mixture.fit(initialMixture, 1000, 1.0E-5));
    }
}