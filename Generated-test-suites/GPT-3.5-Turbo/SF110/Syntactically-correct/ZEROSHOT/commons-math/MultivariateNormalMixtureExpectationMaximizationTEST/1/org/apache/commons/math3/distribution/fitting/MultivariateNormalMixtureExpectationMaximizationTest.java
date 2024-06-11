import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.SingularMatrixException;
import org.apache.commons.math3.exception.ConvergenceException;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    private static double[][] data;

    @BeforeClass
    public static void setUp() {
        data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidData() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] invalidData = new double[][]{};
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithInvalidDataDimension() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] invalidData = new double[][]{{1.0, 2.0}, {3.0}};
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidDataSize() throws NotStrictlyPositiveException, DimensionMismatchException, NumberIsTooSmallException {
        double[][] invalidData = new double[][]{{1.0}};
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test
    public void testFitWithValidData() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        mnm.fit(initialMixture);
        MixtureMultivariateNormalDistribution fittedModel = mnm.getFittedModel();
        assertNotNull(fittedModel);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidMaxIterations() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        mnm.fit(initialMixture, 0, 1.0E-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidThreshold() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        mnm.fit(initialMixture, 1000, -0.5);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithInvalidMixtureDimensions() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        double[][] invalidData = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0}};
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(invalidData);
        mnm.fit(initialMixture, 1000, 1.0E-5);
    }

    @Test
    public void testEstimateWithValidData() throws NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 2);
        assertNotNull(estimatedModel);
    }
  
    @Test(expected = NotStrictlyPositiveException.class)
    public void testEstimateWithInvalidData() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] invalidData = new double[][]{{1.0}};
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(invalidData, 2);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateWithInvalidComponents() throws NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 1);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimateWithInvalidComponentsSize() throws NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution estimatedModel = MultivariateNormalMixtureExpectationMaximization.estimate(data, 3);
    }

    @Test
    public void testGetLogLikelihood() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        mnm.fit(initialMixture);
        double logLikelihood = mnm.getLogLikelihood();
        assertEquals(0.0, logLikelihood, 1.0E-5);
    }

    @Test
    public void testGetFittedModel() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        MultivariateNormalMixtureExpectationMaximization mnm = new MultivariateNormalMixtureExpectationMaximization(data);
        mnm.fit(initialMixture);
        MixtureMultivariateNormalDistribution fittedModel = mnm.getFittedModel();
        assertNotNull(fittedModel);
    }
}