package org.apache.commons.math3.distribution.fitting;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    private MultivariateNormalMixtureExpectationMaximization m;

    @Before
    public void setUp() {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        m = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidDataLength() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] data = {};
        MultivariateNormalMixtureExpectationMaximization testM = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInvalidDataSize() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] data = {{1.0}, {2.0, 3.0}};
        MultivariateNormalMixtureExpectationMaximization testM = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidDataLength2() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] data = {{1.0, 2.0}};
        MultivariateNormalMixtureExpectationMaximization testM = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 1000;
        double threshold = 1.0E-5;
        m.fit(initialMixture, maxIterations, threshold);
        assertNotNull(m.getFittedModel());
        assertEquals(maxIterations, m.getLogLikelihood(), threshold);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidMaxIterations() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 0;
        double threshold = 1.0E-5;
        m.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidThreshold() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 1000;
        double threshold = -1.0E-5;
        m.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithInvalidComponentDimension() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 1000;
        double threshold = 1.0E-5;
        double[][] invalidData = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        MultivariateNormalMixtureExpectationMaximization invalidModel = new MultivariateNormalMixtureExpectationMaximization(invalidData);
        invalidModel.fit(initialMixture, maxIterations, threshold);
    }

    @Test
    public void testFitWithDefaultParameters() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        m.fit(initialMixture);
        assertNotNull(m.getFittedModel());
        assertEquals(MultivariateNormalMixtureExpectationMaximization.DEFAULT_MAX_ITERATIONS, m.getLogLikelihood(), MultivariateNormalMixtureExpectationMaximization.DEFAULT_THRESHOLD);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidDataLength() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0}};
        int numComponents = 2;
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateWithInvalidNumComponents() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        int numComponents = 1;
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimateWithInvalidNumComponents2() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        int numComponents = 4;
        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
    }

    @Test
    public void testGetLogLikelihood() {
        double logLikelihood = m.getLogLikelihood();
        assertEquals(0.0, logLikelihood, 0.0);
    }

    @Test
    public void testGetFittedModel() {
        MixtureMultivariateNormalDistribution fittedModel = m.getFittedModel();
        assertNotNull(fittedModel);
    }
}