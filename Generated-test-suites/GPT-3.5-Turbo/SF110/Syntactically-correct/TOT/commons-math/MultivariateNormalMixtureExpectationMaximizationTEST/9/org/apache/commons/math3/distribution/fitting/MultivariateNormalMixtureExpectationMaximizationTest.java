import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.distribution.MixtureMultivariateNormalDistribution;
import org.apache.commons.math3.exception.SingularMatrixException;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultivariateNormalMixtureExpectationMaximizationTest {

    private MultivariateNormalMixtureExpectationMaximization instance;
    private double[][] data;

    @Before
    public void setUp() {
        data = new double[][]{{1.0, 2.0}, {2.0, 5.0}, {3.0, 1.0}};
        instance = new MultivariateNormalMixtureExpectationMaximization(data);
    }

    @Test
    public void testConstructorWithValidData() {
        assertNotNull(instance);
        assertArrayEquals(data, instance.data);
        assertEquals(0.0, instance.logLikelihood, 0.0001);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidData() {
        double[][] invalidData = new double[][]{};
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConstructorWithMismatchedData() {
        double[][] invalidData = new double[][]{{1.0, 2.0}, {2.0, 5.0}, {3.0}};
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithSmallDimension() {
        double[][] invalidData = new double[][]{{1.0}, {2.0}, {3.0}};
        new MultivariateNormalMixtureExpectationMaximization(invalidData);
    }

    @Test
    public void testFitWithValidArgs() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 100;
        double threshold = 1.0E-4;

        instance.fit(initialMixture, maxIterations, threshold);

        assertNotNull(instance.fittedModel);
        assertEquals(instance.data[0].length, instance.fittedModel.getComponents().get(0).getSecond().getMeans().length);
        assertTrue(instance.logLikelihood != Double.NEGATIVE_INFINITY);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidMaxIterations() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 0;
        double threshold = 1.0E-4;

        instance.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testFitWithInvalidThreshold() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        int maxIterations = 100;
        double threshold = -1.0E-4;

        instance.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testFitWithMismatchedComponents() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.addDistribution(0.5, new MultivariateNormalDistribution(new double[]{1.0, 2.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}}));
        int maxIterations = 100;
        double threshold = 1.0E-4;

        instance.fit(initialMixture, maxIterations, threshold);
    }

    @Test(expected = SingularMatrixException.class)
    public void testFitWithSingularCovarianceMatrix() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();
        initialMixture.addDistribution(0.5, new MultivariateNormalDistribution(new double[]{1.0, 2.0}, new double[][]{{1.0, 0.0}, {0.0, 0.0}}));
        initialMixture.addDistribution(0.5, new MultivariateNormalDistribution(new double[]{2.0, 3.0}, new double[][]{{1.0, 0.0}, {0.0, 1.0}}));
        int maxIterations = 100;
        double threshold = 1.0E-4;

        instance.fit(initialMixture, maxIterations, threshold);
    }

    @Test
    public void testFitWithDefaultArgs() {
        MixtureMultivariateNormalDistribution initialMixture = new MixtureMultivariateNormalDistribution();

        instance.fit(initialMixture);

        assertNotNull(instance.fittedModel);
        assertEquals(instance.data[0].length, instance.fittedModel.getComponents().get(0).getSecond().getMeans().length);
        assertTrue(instance.logLikelihood != Double.NEGATIVE_INFINITY);
    }

    @Test(expected = SingularMatrixException.class)
    public void testEstimateWithInvalidData() {
        int numComponents = 2;
        double[][] invalidData = new double[][]{{1.0}, {2.0}};

        MultivariateNormalMixtureExpectationMaximization.estimate(invalidData, numComponents);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testEstimateWithSmallNumComponents() {
        int numComponents = 1;

        MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testEstimateWithLargeNumComponents() {
        int numComponents = data.length + 1;

        MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);
    }

    @Test
    public void testEstimateWithValidDataAndNumComponents() {
        int numComponents = 2;

        MixtureMultivariateNormalDistribution result = MultivariateNormalMixtureExpectationMaximization.estimate(data, numComponents);

        assertNotNull(result);
        assertEquals(numComponents, result.getComponents().size());
    }

    @Test
    public void testGetLogLikelihood() {
        assertEquals(0.0, instance.getLogLikelihood(), 0.0001);
    }

    @Test
    public void testGetFittedModel() {
        MixtureMultivariateNormalDistribution fittedModel = instance.getFittedModel();

        assertNotNull(fittedModel);
        assertArrayEquals(instance.fittedModel.getComponents().toArray(), fittedModel.getComponents().toArray());
    }

    @Test
    public void testDataRowConstructor() {
        double[] row = {1.0, 2.0};
        MultivariateNormalMixtureExpectationMaximization.DataRow dataRow = instance.new DataRow(row);

        assertNotNull(dataRow);
        assertArrayEquals(row, dataRow.row, 0.0001);
        assertNotNull(dataRow.mean);
    }

    @Test
    public void testDataRowCompareTo() {
        double[] row1 = {1.0, 2.0};
        double[] row2 = {2.0, 4.0};
        MultivariateNormalMixtureExpectationMaximization.DataRow dataRow1 = instance.new DataRow(row1);
        MultivariateNormalMixtureExpectationMaximization.DataRow dataRow2 = instance.new DataRow(row2);

        int result = dataRow1.compareTo(dataRow2);

        assertTrue(result < 0);
    }

    @Test
    public void testDataRowEquals() {
        double[] row1 = {1.0, 2.0};
        double[] row2 = {1.0, 2.0};
        MultivariateNormalMixtureExpectationMaximization.DataRow dataRow1 = instance.new DataRow(row1);
        MultivariateNormalMixtureExpectationMaximization.DataRow dataRow2 = instance.new DataRow(row2);

        assertTrue(dataRow1.equals(dataRow2));
    }

    @Test
    public void testDataRowHashCode() {
        double[] row = {1.0, 2.0};
        MultivariateNormalMixtureExpectationMaximization.DataRow dataRow = instance.new DataRow(row);

        assertNotNull(dataRow.hashCode());
    }

    @Test
    public void testDataRowGetRow() {
        double[] row = {1.0, 2.0};
        MultivariateNormalMixtureExpectationMaximization.DataRow dataRow = instance.new DataRow(row);

        assertArrayEquals(row, dataRow.getRow(), 0.0001);
    }

}