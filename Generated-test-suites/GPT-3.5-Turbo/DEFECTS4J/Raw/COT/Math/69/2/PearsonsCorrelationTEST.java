import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    @Test
    public void testPearsonsCorrelation() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNotNull(correlation);
    }

    @Test
    public void testDataConstructor() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation);
    }

    @Test
    public void testMatrixConstructor() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        RealMatrix matrix = new BlockRealMatrix(data);
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        assertNotNull(correlation);
    }

    @Test
    public void testCovarianceConstructor() {
        Covariance covariance = new Covariance();
        RealMatrix covarianceMatrix = covariance.getCovarianceMatrix();
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance);
        assertNotNull(correlation);
    }

    @Test
    public void testCovarianceMatrixConstructor() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        RealMatrix covarianceMatrix = new BlockRealMatrix(data);
        int numberOfObservations = 3;
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, numberOfObservations);
        assertNotNull(correlation);
    }

    @Test
    public void testGetCorrelationMatrix() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationMatrix = correlation.getCorrelationMatrix();
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = correlation.getCorrelationStandardErrors();
        assertNotNull(correlationStandardErrors);
    }

    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = correlation.getCorrelationPValues();
        assertNotNull(correlationPValues);
    }

    @Test
    public void testComputeCorrelationMatrix1() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix matrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = correlation.computeCorrelationMatrix(matrix);
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testComputeCorrelationMatrix2() {
        double[][] data = {{1, 2}, {3, 4}, {5, 6}};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = correlation.computeCorrelationMatrix(data);
        assertNotNull(correlationMatrix);
    }

    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double result = correlation.correlation(xArray, yArray);
        assertEquals(1.0, result, 0.00001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInvalidDimensions() {
        double[] xArray = {1, 2};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCorrelationInsufficientData() {
        double[] xArray = {1};
        double[] yArray = {4};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.correlation(xArray, yArray);
    }

    @Test
    public void testCovarianceToCorrelation() {
        double[][] data = {{1, 2}, {3, 4}};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix covarianceMatrix = new BlockRealMatrix(data);
        RealMatrix correlationMatrix = correlation.covarianceToCorrelation(covarianceMatrix);
        assertNotNull(correlationMatrix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckSufficientData() {
        double[][] data = {{}};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        correlation.checkSufficientData(new BlockRealMatrix(data));
    }

}