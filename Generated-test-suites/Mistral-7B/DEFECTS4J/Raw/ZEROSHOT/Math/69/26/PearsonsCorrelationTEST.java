import org.apache.commons.math3.exception.MathException;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.junit.Assert;
import org.junit.Test;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.Arrays;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        Assert.assertNull(pearsonCorrelation.correlationMatrix);
        Assert.assertEquals(0, pearsonCorrelation.nObs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInputData() {
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRectangularDataWithLessThanTwoRowsOrTwoColumns() {
        double[][] data = {{1}, {1, 2}};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
    }

    @Test
    public void testRectangularDataWithTwoRowsAndTwoColumns() {
        double[][] data = {{1, 2}, {3, 4}};
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8}, {0.8, 1.0}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(data);
        Assert.assertArrayEquals(expectedCorrelationMatrix.getData(), pearsonCorrelation.correlationMatrix.getData(), 0.001);
        Assert.assertEquals(2, pearsonCorrelation.nObs);
    }

    @Test
    public void testRealMatrixWithTwoColumns() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        RealMatrix inputMatrix = new BlockRealMatrix(data);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8660254037844386}, {0.8660254037844386, 1.0}});
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(inputMatrix);
        Assert.assertArrayEquals(expectedCorrelationMatrix.getData(), pearsonCorrelation.correlationMatrix.getData(), 0.001);
        Assert.assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testRealMatrixWithMoreThanTwoColumns() {
        double[][] data = {{1, 2, 3}, {3, 4, 5}, {5, 6, 7}};
        RealMatrix inputMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(inputMatrix);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8124031345171983, 0.6351285614221362},
                {0.8124031345171983, 1.0, 0.7406498591462536},
                {0.6351285614221362, 0.7406498591462536, 1.0}});
        Assert.assertArrayEquals(expectedCorrelationMatrix.getData(), pearsonCorrelation.correlationMatrix.getData(), 0.001);
        Assert.assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testCovariance() throws MathException {
        double[][] data = {{1, 2, 3}, {3, 4, 5}, {5, 6, 7}};
        Covariance covariance = new Covariance(new BlockRealMatrix(data));
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covarianceMatrix, 3);
        RealMatrix expectedCorrelationMatrix = new Array2DRowRealMatrix(new double[][]{{1.0, 0.8124031345171983, 0.6351285614221362},
                {0.8124031345171983, 1.0, 0.7406498591462536},
                {0.6351285614221362, 0.7406498591462536, 1.0}});
        Assert.assertArrayEquals(expectedCorrelationMatrix.getData(), pearsonCorrelation.correlationMatrix.getData(), 0.001);
        Assert.assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testCorrelationStandardErrors() throws MathException {
        double[][] data = {{1, 2, 3}, {3, 4, 5}, {5, 6, 7}};
        RealMatrix inputMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(inputMatrix);
        RealMatrix expectedCorrelationStandardErrors = new Array2DRowRealMatrix(new double[][]{{0.4519223598181005, 0.3326115947887942, 0.3326115947887942},
                {0.3326115947887942, 0.4519223598181005, 0.4519223598181005},
                {0.3326115947887942, 0.4519223598181005, 0.4519223598181005}});
        Assert.assertArrayEquals(expectedCorrelationStandardErrors.getData(), pearsonCorrelation.getCorrelationStandardErrors().getData(), 0.001);
        Assert.assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {3, 4, 5}, {5, 6, 7}};
        RealMatrix inputMatrix = new BlockRealMatrix(data);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(inputMatrix);
        RealMatrix expectedCorrelationPValues = new Array2DRowRealMatrix(new double[][]{{1.0, 0.0, 0.0},
                {0.0, 1.0, 0.0},
                {0.0, 0.0, 1.0}});
        Assert.assertArrayEquals(expectedCorrelationPValues.getData(), pearsonCorrelation.getCorrelationPValues().getData(), 0.001);
        Assert.assertEquals(3, pearsonCorrelation.nObs);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {3, 4, 5};
        double correlation = PearsonsCorrelation.correlation(xArray, yArray);
        Assert.assertEquals(0.8124031345171983, correlation, 0.001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidLength() {
        double[] xArray = {1};
        double[] yArray = {2, 3, 4};
        PearsonsCorrelation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInsufficientData() {
        double[] xArray = {};
        double[] yArray = {};
        PearsonsCorrelation.correlation(xArray, yArray);
    }
}