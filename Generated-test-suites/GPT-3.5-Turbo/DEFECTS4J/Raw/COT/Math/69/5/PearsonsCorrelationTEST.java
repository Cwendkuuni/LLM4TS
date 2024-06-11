
import org.junit.Test;
import static org.junit.Assert.*;

public class PearsonsCorrelationTest {

    // Create a PearsonsCorrelation instance without data
    @Test
    public void testEmptyConstructor() {
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        assertNull(correlation.getCorrelationMatrix());
    }

    // Create a PearsonsCorrelation from a rectangular array
    @Test
    public void testArrayConstructor() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        assertNotNull(correlation.getCorrelationMatrix());
    }

    // Create a PearsonsCorrelation from a RealMatrix
    @Test
    public void testMatrixConstructor() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(matrix);
        assertNotNull(correlation.getCorrelationMatrix());
    }

    // Create a PearsonsCorrelation from a Covariance
    @Test
    public void testCovarianceConstructor() {
        Covariance covariance = new Covariance(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        PearsonsCorrelation correlation = new PearsonsCorrelation(covariance);
        assertNotNull(correlation.getCorrelationMatrix());
    }

    // Create a PearsonsCorrelation from a covariance matrix
    @Test
    public void testCovarianceMatrixConstructor() {
        RealMatrix covarianceMatrix = new BlockRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        int numberOfObservations = 3;
        PearsonsCorrelation correlation = new PearsonsCorrelation(covarianceMatrix, numberOfObservations);
        assertNotNull(correlation.getCorrelationMatrix());
    }

    // Test computeCorrelationMatrix(RealMatrix matrix)
    @Test
    public void testComputeCorrelationMatrixRealMatrix() {
        RealMatrix matrix = new BlockRealMatrix(new double[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = correlation.computeCorrelationMatrix(matrix);
        assertNotNull(correlationMatrix);
    }

    // Test computeCorrelationMatrix(double[][] data)
    @Test
    public void testComputeCorrelationMatrixArray() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        RealMatrix correlationMatrix = correlation.computeCorrelationMatrix(data);
        assertNotNull(correlationMatrix);
    }

    // Test correlation(double[] xArray, double[] yArray)
    @Test
    public void testCorrelation() {
        double[] xArray = {1, 2, 3};
        double[] yArray = {4, 5, 6};
        PearsonsCorrelation correlation = new PearsonsCorrelation();
        double result = correlation.correlation(xArray, yArray);
        assertEquals(1.0, result, 0.0001);
    }

    // Test getCorrelationStandardErrors()
    @Test
    public void testGetCorrelationStandardErrors() {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationStandardErrors = correlation.getCorrelationStandardErrors();
        assertNotNull(correlationStandardErrors);
    }

    // Test getCorrelationPValues()
    @Test
    public void testGetCorrelationPValues() throws MathException {
        double[][] data = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        PearsonsCorrelation correlation = new PearsonsCorrelation(data);
        RealMatrix correlationPValues = correlation.getCorrelationPValues();
        assertNotNull(correlationPValues);
    }
}

