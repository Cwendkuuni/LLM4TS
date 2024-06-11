
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultivariateNormalMixtureExpectationMaximizationTest {
    private double[][] data;
    private MultivariateNormalMixtureExpectationMaximization model;

    @Before
    public void setup() {
        data = new double[][]{{1.0, 2.0, 3.0}, {4.0, 5.0, 6.0}};
        model = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidDataLength() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] invalidData = new double[][]{};
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorInvalidDataDimension() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] invalidData = new double[][]{{1.0}, {2.0, 3.0}};
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidDataLengthLessThan2() throws NotStrictlyPositiveException, DimensionMismatchException {
        double[][] invalidData = new double[][]{{1.0, 2.0}};
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitInvalidMaxIterations() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        model.fit(initialMixture, 0, 0.001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitInvalidThreshold() throws SingularMatrixException, NotStrictlyPositiveException, DimensionMismatchException {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        model.fit(initialMixture, 100, -0.001);
    }

    //... more test cases to cover the behavior of the class

    @Test
    public void testGetLogLikelihood() {
        double expected = 0.0;
        double actual = model.getLogLikelihood();
        assertEquals(expected, actual, 0.0);
    }

    @Test
    public void testGetFittedModel() {
        MixtureMultivariateNormalDistribution expected = new MixtureMultivariateNormalDistribution();
        MixtureMultivariateNormalDistribution actual = model.getFittedModel();
        assertEquals(expected, actual);
    }
}
