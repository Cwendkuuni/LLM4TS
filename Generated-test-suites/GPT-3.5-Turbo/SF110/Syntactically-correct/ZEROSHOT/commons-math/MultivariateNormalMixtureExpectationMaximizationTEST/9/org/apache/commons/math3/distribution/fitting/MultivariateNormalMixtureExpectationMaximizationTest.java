package org.apache.commons.math3.distribution.fitting;

import org.apache.commons.math3.exception.util.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.*;
import org.apache.commons.math3.linear.*;
import org.apache.commons.math3.exception.*;
import java.util.*;
import org.apache.commons.math3.stat.correlation.*;
import org.junit.*;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    private double[][] data;
    private MultivariateNormalMixtureExpectationMaximization m;

    @Before
    public void setUp() {
        data = new double[][]{{1.0, 1.0}, {2.0, 2.0}, {3.0, 3.0}};
        m = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativeDataLength() {
        data = new double[][]{};
        m = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructor_DifferentDataLength() {
        data = new double[][]{{1.0, 1.0}, {2.0, 2.0, 2.0}, {3.0, 3.0}};
        m = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_DataLengthTooSmall() {
        data = new double[][]{{1.0, 1.0}, {2.0}, {3.0, 3.0}};
        m = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 1000;
        double threshold = 1.0E-5;

        try{
            // test lower bound of maxIterations
            m.fit(initialMixture, 0, threshold);
            fail("Exception not thrown for invalid maxIterations");
        } catch (NotStrictlyPositiveException e) {
            assertEquals("maxIterations must be strictly positive", e.getMessage());
        }

        try{
            // test lower bound of threshold
            m.fit(initialMixture, maxIterations, 0);
            fail("Exception not thrown for invalid threshold");
        } catch (NotStrictlyPositiveException e) {
            assertEquals("threshold must be strictly positive", e.getMessage());
        }

        try{
            // test different numCols between data and initialMixture
            initialMixture = new MixtureMultivariateNormalDistribution();
            initialMixture.getComponents().add(new Pair<>(1.0, new MultivariateNormalDistribution(new double[]{1.0}, new double[][]{{1.0}})));
            m.fit(initialMixture, maxIterations, threshold);
            fail("Exception not thrown for different dimensions");
        } catch (DimensionMismatchException e) {
            assertEquals("number of mean columns must match number of columns in data", e.getMessage());
        }

        // test SingularMatrixException
        initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.getComponents().add(new Pair<>(1.0, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}})));
        try{
            m.fit(initialMixture, maxIterations, threshold);
            fail("Exception not thrown for invalid matrix");
        } catch (SingularMatrixException e) {
            assertEquals("Matrix is singular", e.getMessage());
        }

        // test valid input
        initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.getComponents().add(new Pair<>(1.0, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}})));
        try{
            m.fit(initialMixture, maxIterations, threshold);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFit_WithDefaultParameters() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();

        try{
            // test SingularMatrixException
            initialMixture = new MixtureMultivariateNormalDistribution();
            initialMixture.getComponents().add(new Pair<>(1.0, new MultivariateNormalDistribution(new double[]{0.0, 0.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}})));
            m.fit(initialMixture);
            fail("Exception not thrown for invalid matrix");
        } catch (SingularMatrixException e) {
            assertEquals("Matrix is singular", e.getMessage());
        }

        // test valid input
        initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.getComponents().add(new Pair<>(1.0, new MultivariateNormalDistribution(new double[]{1.0, 1.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}})));
        try{
            m.fit(initialMixture);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimate_NegativeDataLength() {
        data = new double[][]{};
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimate_NumComponentsTooSmall() {
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimate_NumComponentsTooLarge() {
        MultivariateNormalMixtureExpectationMaximization.estimate(data, 4);
    }

    @Test
    public void testEstimate() {
        try{
            MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
            assertNotNull(result);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetLogLikelihood() {
        try{
            double logLikelihood = m.getLogLikelihood();

            assertEquals(0.0, logLikelihood, 0.0);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testGetFittedModel() {
        try{
            MixtureMultivariateNormalDistribution fittedModel = m.getFittedModel();

            assertNotNull(fittedModel);
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}