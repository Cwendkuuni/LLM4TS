import org.apache.commons.math.MathException;
import org.apache.commons.math.stat.correlation.PearsonsCorrelation;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testComputeCorrelationMatrix() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        double[][] expectedMatrix = {{1, 1, 1}, {1, 1, 1}, {1, 1, 1}};
        double[][] actualMatrix = pearsonsCorrelation.getCorrelationMatrix().getData();

        Assert.assertArrayEquals(expectedMatrix, actualMatrix);
    }

    @Test
    public void testComputeCorrelationMatrixWithInvalidData() {
        double[][] data = {{1, 2}, {4, 5}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        Assert.assertNull(pearsonsCorrelation.getCorrelationMatrix());
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        double[][] expectedErrors = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        double[][] actualErrors = pearsonsCorrelation.getCorrelationStandardErrors().getData();

        Assert.assertArrayEquals(expectedErrors, actualErrors);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation(data);

        double[][] expectedPValues = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        double[][] actualPValues = pearsonsCorrelation.getCorrelationPValues().getData();

        Assert.assertArrayEquals(expectedPValues, actualPValues);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();

        double expectedCorrelation = 1;
        double actualCorrelation = pearsonsCorrelation.correlation(xArray, yArray);

        Assert.assertEquals(expectedCorrelation, actualCorrelation, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationWithInvalidData() {
        double[] xArray = {1};
        double[] yArray = {4};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();

        pearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceMatrixData = {{4, 6}, {6, 9}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();

        double[][] expectedCorrelationMatrixData = {{1, 1}, {1, 1}};
        double[][] actualCorrelationMatrixData = pearsonsCorrelation.covarianceToCorrelation(
                new BlockRealMatrix(covarianceMatrixData)).getData();

        Assert.assertArrayEquals(expectedCorrelationMatrixData, actualCorrelationMatrixData);
    }

    @Test(expected = MathRuntimeException.class)
    public void testCovarianceToCorrelationWithInvalidData() {
        double[][] covarianceMatrixData = {{4}};
        PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();

        pearsonsCorrelation.covarianceToCorrelation(new BlockRealMatrix(covarianceMatrixData));
    }
}