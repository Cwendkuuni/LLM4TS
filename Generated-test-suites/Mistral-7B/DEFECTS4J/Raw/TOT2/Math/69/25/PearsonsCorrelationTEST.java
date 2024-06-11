import org.apache.commons.math3.linear.BlockRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.junit.Assert;
import org.junit.Test;

public class PearsonsCorrelationTest {

    @Test
    public void testEmptyMatrix() {
        RealMatrix emptyMatrix = new BlockRealMatrix(new double[0][0]);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        try {
            pearsonCorrelation.new PearsonsCorrelation(emptyMatrix);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIdentityMatrix() {
        double[][] identityMatrix = {{1, 0}, {0, 1}};
        RealMatrix matrix = new BlockRealMatrix(identityMatrix);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1, 1}, {1, 1}});
        Assert.assertEquals(expected, correlationMatrix);
    }

    @Test
    public void testTwoArraysSameLengthPositiveValues() {
        double[] xArray = {1, 2, 3, 4};
        double[] yArray = {1, 2, 3, 4};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        double correlation = pearsonCorrelation.correlation(xArray, yArray);
        Assert.assertEquals(1, correlation, 0.01); // correlation should be 1 for identical arrays
    }

    @Test
    public void testTwoArraysDifferentLengths() {
        double[] xArray = {1, 2, 3, 4};
        double[] yArray = {1, 2};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        try {
            pearsonCorrelation.correlation(xArray, yArray);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testTwoArraysNegativeValues() {
        double[] xArray = {-1, -2, -3, -4};
        double[] yArray = {1, 2, 3, 4};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        try {
            pearsonCorrelation.correlation(xArray, yArray);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testTwoArraysZeroValues() {
        double[] xArray = {0, 0, 0, 0};
        double[] yArray = {0, 0, 0, 0};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        double correlation = pearsonCorrelation.correlation(xArray, yArray);
        Assert.assertEquals(1, correlation, 0.01); // correlation should be 1 for identical arrays with zeros
    }

    @Test
    public void testTwoArraysDiffValues() {
        double[] xArray = {1, 2, 3, 4};
        double[] yArray = {5, 6, 7, 8};
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        double correlation = pearsonCorrelation.correlation(xArray, yArray);
        Assert.assertEquals(-0.8, correlation, 0.01);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] covarianceMatrix = {{1, 2}, {2, 1}};
        RealMatrix covariance = new BlockRealMatrix(covarianceMatrix);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = pearsonCorrelation.covarianceToCorrelation(covariance);
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1, 0.447213595500434},{0.447213595500434, 1}});
        Assert.assertEquals(expected, correlationMatrix);
    }

    @Test
    public void testFromCovariance() {
        double[][] covarianceMatrix = {{1, 2}, {2, 1}};
        Covariance covariance = new Covariance(new BlockRealMatrix(covarianceMatrix));
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(covariance);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expected = new BlockRealMatrix(new double[][]{{1, 0.447213595500434},{0.447213595500434, 1}});
        Assert.assertEquals(expected, correlationMatrix);
    }

    @Test
    public void testFromRealMatrix() {
        double[][] matrixData = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        RealMatrix matrix = new BlockRealMatrix(matrixData);
        PearsonsCorrelation pearsonCorrelation = new PearsonsCorrelation(matrix);
        RealMatrix correlationMatrix = pearsonCorrelation.getCorrelationMatrix();
        RealMatrix expected = new BlockRealMatrix(new double[][]{{0.99971123, 0.99936532, 0.99910385},
                {0.99936532, 0.99986062, 0.99964785},
                {0.99910385, 0.99964785, 0.99988537}});
        Assert.assertEquals(expected, correlationMatrix);
    }
}