
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithEmptyData() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] data = {};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithDifferentColumnSizes() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithSmallColumnSize() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] data = {{1.0}};
        new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testFit() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture);
        double logLikelihood = m.getLogLikelihood();
        assertNotNull(logLikelihood);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularMatrix() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = {{1.0, 2.0}, {1.0, 2.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeMaxIterations() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, -1, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithNegativeThreshold() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture, 100, -1.0E-5);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithDifferentColumnSizesInInitialMixture() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        double[][] means = {{1.0, 2.0}, {1.0, 2.0}};
        double[][][] covMats = {{{1.0, 0.0}, {0.0, 1.0}}, {{1.0, 0.0}, {0.0, 1.0}}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution(means, covMats);
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture);
    }

    @Test
    public void testGetFittedModel() throws NotStrictlyPositiveException, DimensionMismatchException, SingularMatrixException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization m = new MultivariateNormalMixtureExpectationMaximization(data);
        m.fit(initialMixture);
        MixtureMultivariateNormalDistribution fittedModel = m.getFittedModel();
        assertNotNull(fittedModel);
    }

    @Test
    public void testEstimate() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        int numComponents = 3;
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
        assertNotNull(estimatedModel);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithSmallDataSize() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0, 3.0}};
        int numComponents = 3;
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateWithSmallNumComponents() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        int numComponents = 1;
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimateWithLargeNumComponents() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] data = {{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}, {7.0, 8.0, 9.0}};
        int numComponents = 10;
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
    }
}
