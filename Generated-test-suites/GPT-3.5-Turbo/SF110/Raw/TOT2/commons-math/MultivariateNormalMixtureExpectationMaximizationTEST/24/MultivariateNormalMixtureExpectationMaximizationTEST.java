import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.distribution.fitting.MultivariateNormalMixtureExpectationMaximization;

import java.util.List;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test
    public void testDataConstructor() {
        // Test data constructor with valid and invalid inputs
        double[][] validData = {{1.0, 2.0}, {3.0, 4.0}};
        double[][] invalidData = {{1.0}, {2.0, 3.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(validData);
            assertNotNull(em);
            // Test for exception when invalid data is provided
            em = new MultivariateNormalMixtureExpectationMaximization(invalidData);
            fail("Should have thrown a NumberIsTooSmallException");
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException e) {
            // Expected Exception
        }
    }

    @Test
    public void testFitMethod() {
        // Test fit method with valid and invalid inputs
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            em.fit(initialMixture, 100, 1.0E-5);
            assertNotNull(em.getFittedModel());
            // Test for exception when maxIterations is not positive
            em.fit(initialMixture, 0, 1.0E-5);
            fail("Should have thrown a NotStrictlyPositiveException");
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException e) {
            // Expected Exception
        }
    }

    @Test
    public void testEstimateMethod() {
        // Test estimate method with valid and invalid inputs
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}, {5.0, 6.0}};
        int numComponents = 2;
        try {
            MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
            assertNotNull(result);
            // Test for exception when numComponents is less than 2
            MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
            fail("Should have thrown a NumberIsTooSmallException");
        } catch (NotStrictlyPositiveException | DimensionMismatchException | NumberIsTooSmallException | NumberIsTooLargeException e) {
            // Expected Exception
        }
    }

    @Test
    public void testGetLogLikelihood() {
        // Test getLogLikelihood method
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            em.fit(new MixtureMultivariateNormalDistribution());
            assertTrue(em.getLogLikelihood() >= 0);
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testGetFittedModel() {
        // Test getFittedModel method
        double[][] data = {{1.0, 2.0}, {3.0, 4.0}};
        try {
            MultivariateNormalMixtureExpectationMaximization em = new MultivariateNormalMixtureExpectationMaximization(data);
            MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
            em.fit(initialMixture, 1000, 1.0E-5);
            assertNotNull(em.getFittedModel());
            assertEquals(1, em.getFittedModel().getComponents().size());
        } catch (SingularMatrixException | NotStrictlyPositiveException | DimensionMismatchException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    public void testDataRow() {
        // Test DataRow class and its methods
        double[] data = {1.0, 2.0};
        MultivariateNormalMixtureExpectationMaximization.DataRow row = new MultivariateNormalMixtureExpectationMaximization.DataRow(data);
        assertEquals(1.5, row.mean, 0.0001);
        double[] newRow = row.getRow();
        assertArrayEquals(data, newRow, 0.0001);
    }
}